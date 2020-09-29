package servidor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
	
	public static int[] SelectionSortVetor(int vetor[], int tamanhoVetor) {
		for (int i = 0; i < tamanhoVetor - 1; i++) {
			int aux = i;
			int aux2;
			for (int j = i + 1; j < tamanhoVetor; j++) {
				if (vetor[j] < vetor[aux])
					aux = j;
			}
			aux2 = vetor[i];
			vetor[i] = vetor[aux];
			vetor[aux] = aux2;
		}
		return vetor;
	}
	
	public static int[] MergeSortVetor(int vetor[], int tamanhoVetor) {

		if (tamanhoVetor <= 1)
			return null;
		int aux = tamanhoVetor / 2;
		int esquerda[] = new int[aux];
		int direita[] = new int[tamanhoVetor - aux];
		for (int i = 0; i < aux; i++)
			esquerda[i] = vetor[i];
		for (int i = aux; i < tamanhoVetor; i++)
			direita[i - aux] = vetor[i];
		MergeSortVetor(esquerda, aux);
		MergeSortVetor(direita, tamanhoVetor - aux);
		Merge(esquerda, direita, vetor);
		
		return vetor;

	}

	public static void Merge(int esquerda[], int direita[], int vetor[]) {
		int nEsquerda = esquerda.length;
		int nDireita = direita.length;
		int i, j, k;
		i = j = k = 0;
		while (i < nEsquerda && j < nDireita) {
			if (esquerda[i] <= direita[j]) {
				vetor[k] = esquerda[i];
				i++;
				k++;
			} else {
				vetor[k] = direita[j];
				j++;
				k++;
			}
		}
		while (i < nEsquerda) {
			vetor[k] = esquerda[i];
			i++;
			k++;
		}
		while (j < nDireita) {
			vetor[k] = direita[j];
			j++;
			k++;
		}
	}
	
	public static List<Elemento> ordenarBubbleLista(List<Elemento> lista, int n) {
		int aux;
		boolean troca;
		
		for(int i = 0; i < n-1; i++) {
			troca = false;
			for(int j = 0; j < n-1; j++) {
				if(lista.get(j).getValor() > lista.get(j+1).getValor()) {
					aux = lista.get(j).getValor();
					lista.get(j).setValor(lista.get(j+1).getValor());
					lista.get(j+1).setValor(aux);
					troca = true;
				}
			}
			if(troca == false)
				break;
		}
		
		return lista;
	}
	
	public static List<Elemento> ordenarQuickLista(List<Elemento> lista, int inicio, int fim) {
		if (inicio < fim) {
			int pIndice = quickTrocaLista(lista, inicio, fim);
			ordenarQuickLista(lista, inicio, pIndice - 1);
			ordenarQuickLista(lista, pIndice + 1, fim);
		}
		
		return lista;
	}

	public static int quickTrocaLista(List<Elemento> lista, int inicio, int fim) {
		int aux;
		int pivot = lista.get(fim).getValor();
		int pIndice = inicio;
		
		for (int i = inicio; i < fim; i++) {
			if (lista.get(i).getValor() <= pivot) {
				aux = lista.get(i).getValor();
				lista.get(i).setValor(lista.get(pIndice).getValor());
				lista.get(pIndice).setValor(aux);
				pIndice ++;
			}
		}
		
		aux = lista.get(pIndice).getValor();
		lista.get(pIndice).setValor(lista.get(fim).getValor());
		lista.get(fim).setValor(aux);
		
		return pIndice;
	}
	
	public static void imprimirVetor(int[] vetor) {
		System.out.print("[");
		for (int i = 0; i < vetor.length; i++) {
			if (i != vetor.length - 1)
				System.out.print(vetor[i] + ", ");
			else
				System.out.print(vetor[i]);
		}
		System.out.print("]");
	}
	
	public static void imprimirLista(List<Elemento> lista) {
		System.out.print("[");
		for (int i = 0; i < lista.size(); i++) {
			if (i != lista.size() - 1)
				System.out.print(lista.get(i).getValor() + ", ");
			else
				System.out.print(lista.get(i).getValor());
		}
		System.out.print("]");
	}
	
	public static void main(String[] args) throws IOException {
		Socket socket;
		ServerSocket socketServidor = new ServerSocket(2800);;
		long inicio, fim;
		Runtime rt;
		
		while (true) {
			System.out.println("Aguardando mensagem...");
			socket = socketServidor.accept(); // Aceitando uma requesição quando chegar
			System.out.println("Chegou requisição");
			
			BufferedReader requisicao = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String msgCliente = requisicao.readLine();
			System.out.println("Mensagem do cliente: " + msgCliente);
			
			DataOutputStream resposta = new DataOutputStream(socket.getOutputStream());
			
			// Verificando qual tipo de estrura o cliente escolheu
			if (Integer.valueOf(msgCliente) == 1) {
				int vetor[] = new int[300];
				
				// Recebendo os valores do cliente
				for (int i = 0; i < vetor.length; i++) {
					msgCliente = requisicao.readLine();
					System.out.println("Valor que chegou do cliente: " + msgCliente);
					
					vetor[i] = Integer.valueOf(msgCliente);
					
					resposta.writeBytes("Valor " + msgCliente + " recebido e armazenado");
					resposta.writeBytes("\n"); // Fim da linha
					resposta.flush(); // Manda para o cliente
				}
				
				// Imprimindo o vetor desordenado
				imprimirVetor(vetor);
				System.out.println();
				
				msgCliente = requisicao.readLine();
				
				switch (Integer.valueOf(msgCliente)) {
					case 1:
						rt = Runtime.getRuntime();
						inicio = System.currentTimeMillis();
						vetor = SelectionSortVetor(vetor, vetor.length);
						fim = System.currentTimeMillis() - inicio;
						System.out.println("Memória usada: " + (Runtime.getRuntime().freeMemory() - rt.freeMemory()));
						
						// Imprimindo o vetor ordenado
						imprimirVetor(vetor);
						System.out.println();
						
						System.out.println("Tempo para ordenação em milisegundos: " + fim + "ms\n"
								+ "Tempo para ordenação em segundos: " + fim/1000 + "s");
						
						break;
					case 2:
						inicio = System.currentTimeMillis();
						vetor = MergeSortVetor(vetor, vetor.length);
						fim = System.currentTimeMillis() - inicio;
						
						// Imprimindo o vetor ordenado
						imprimirVetor(vetor);
						System.out.println();
						
						System.out.println("Tempo para ordenação em milisegundos: " + fim + "ms\n"
								+ "Tempo para ordenação em segundos: " + fim/1000 + "s");
						break;
				}
			} else if (Integer.valueOf(msgCliente) == 2) {
				Elemento elemento = new Elemento(-1, 0);
				List<Elemento> lista = new ArrayList<Elemento>();
				
				// Recebendo os valores do cliente
				for (int i = 0; i < 20; i++) {
					msgCliente = requisicao.readLine();
					
					// Atribuindo os valores recebidos na lista
					if (elemento.getValor() == -1) {
						elemento.setValor(Integer.valueOf(msgCliente));
						elemento.setPosicao(i);
						lista.add(elemento);
					} else {
						Elemento novo_elemento = new Elemento(Integer.valueOf(msgCliente), i);
						elemento.setProx(novo_elemento);
						lista.add(novo_elemento);
						elemento = novo_elemento;
					}

					resposta.writeBytes("Valor " + msgCliente + " recebido e armazenado");
					resposta.writeBytes("\n"); // Fim da linha
					resposta.flush(); // Manda para o cliente
				}
				
				// Imprimindo a lista desordenada
				imprimirLista(lista);
				System.out.println();
				
				msgCliente = requisicao.readLine();
				
				switch (Integer.valueOf(msgCliente)) {
					case 1:
						inicio = System.currentTimeMillis();
						lista = ordenarBubbleLista(lista, lista.size());
						fim = System.currentTimeMillis() - inicio;
						
						// Imprimindo a lista ordenada
						imprimirLista(lista);
						System.out.println();
						
						System.out.println("Tempo para ordenação em milisegundos: " + fim + "ms\n"
								+ "Tempo para ordenação em segundos: " + fim/1000 + "s");
						break;
					case 2:
						inicio = System.currentTimeMillis();
						lista = ordenarQuickLista(lista, 0, lista.size() - 1);
						fim = System.currentTimeMillis() - inicio;
						
						// Imprimindo a lista ordenada
						imprimirLista(lista);
						System.out.println();
						
						System.out.println("Tempo para ordenação em milisegundos: " + fim + "ms\n"
								+ "Tempo para ordenação em segundos: " + fim/1000 + "s");
						break;
				}
			} else if (Integer.valueOf(msgCliente) == 3) {
				int vetor[] = new int[20];
				
				for (int i = 0; i < vetor.length; i++) {
					msgCliente = requisicao.readLine();

					vetor[i] = Integer.valueOf(msgCliente);

					resposta.writeBytes("Valor " + msgCliente + " recebido e armazenado");
					resposta.writeBytes("\n"); // Fim da linha
					resposta.flush(); // Manda para o cliente
				}
				
				// Imprimindo o vetor desordenado
				imprimirVetor(vetor);
				System.out.println();
				
				inicio = System.currentTimeMillis();
				Arrays.sort(vetor); // Estrutura de ordenação da linguagem
				fim = System.currentTimeMillis() - inicio;
				
				// Imprimindo o vetor ordenado
				imprimirVetor(vetor);
				System.out.println();
				
				System.out.println("\nTempo para ordenação em milisegundos: " + fim + "\n"
						+ "Tempo para ordenação em segundos: " + fim/1000);
			}
						
			System.out.println();
		}
	}
}
