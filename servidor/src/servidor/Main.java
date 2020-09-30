package servidor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) throws Exception {
		Socket socket = null;
		ServerSocket socketServidor = new ServerSocket(2800);
		String msgDoCliente = null, msgParaCliente;
		DataOutputStream resposta = null;
		BufferedReader bufferedReader = null;
		long tempoInicio, tempoFim;
		int totalValores;
		Runtime rt;

		while (true) {
			System.out.println("Aguardando mensagem...");
			socket = socketServidor.accept(); // Aceitando uma requesição quando chegar

			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			msgDoCliente = bufferedReader.readLine();
			totalValores = Integer.valueOf(bufferedReader.readLine());
			System.out.println("Mensagem do cliente: " + msgDoCliente);

			resposta = new DataOutputStream(socket.getOutputStream());
			// Verificando qual tipo de estrura o cliente escolheu
			if (Integer.valueOf(msgDoCliente) == 1) {
				int vetor[] = new int[totalValores];

				// Recebendo os valores do cliente
				for (int i = 0; i < vetor.length; i++) {
					msgDoCliente = bufferedReader.readLine();
					System.out.println("Valor que chegou do cliente: " + msgDoCliente);

					vetor[i] = Integer.valueOf(msgDoCliente);

					msgParaCliente = msgDoCliente + " recebido e armazenado";
					responder(resposta, msgParaCliente);
				}

				// Imprimindo o vetor desordenado
				imprimirVetor(vetor);
				System.out.println();

				msgDoCliente = bufferedReader.readLine();

				switch (Integer.valueOf(msgDoCliente)) {
				case 1:
					rt = Runtime.getRuntime();
					tempoInicio = System.currentTimeMillis();
					vetor = selectionSortVetor(vetor, vetor.length);
					tempoFim = System.currentTimeMillis() - tempoInicio;
					System.out.println("Memória usada: " + (Runtime.getRuntime().freeMemory() - rt.freeMemory()));

					// Imprimindo o vetor ordenado
					imprimirVetor(vetor);
					retornarVetor(vetor, resposta);
					retornarTempo(tempoFim, resposta);

					System.out.println();
					System.out.println("Tempo para ordenação em milisegundos: " + tempoFim + "ms\n"
							+ "Tempo para ordenação em segundos: " + tempoFim / 1000 + "s");
					break;
				case 2:
					tempoInicio = System.currentTimeMillis();
					vetor = mergeSortVetor(vetor, vetor.length);
					tempoFim = System.currentTimeMillis() - tempoInicio;

					// Imprimindo o vetor ordenado
					imprimirVetor(vetor);
					retornarVetor(vetor, resposta);
					retornarTempo(tempoFim, resposta);
					
					System.out.println();
					System.out.println("Tempo para ordenação em milisegundos: " + tempoFim + "ms\n"
							+ "Tempo para ordenação em segundos: " + tempoFim / 1000 + "s");
					break;
				}
			} else if (Integer.valueOf(msgDoCliente) == 2) {
				listaEncadeada();
				// Recebendo os valores do cliente
				for (int i = 0; i < totalValores; i++) {
					msgDoCliente = bufferedReader.readLine();

					// Atribuindo os valores recebidos na lista
					Elemento novo = new Elemento(Integer.valueOf(msgDoCliente));
					inserirElementoFim(novo);

					msgParaCliente = msgDoCliente + " recebido e armazenado";
					responder(resposta, msgParaCliente);
				}

				// Imprimindo a lista desordenada
				listarElementos();
				System.out.println();

				msgDoCliente = bufferedReader.readLine();

				switch (Integer.valueOf(msgDoCliente)) {
				case 1:
					tempoInicio = System.currentTimeMillis();
					bubbleSortLista(inicio, tamanho);
					tempoFim = System.currentTimeMillis() - tempoInicio;

					// Imprimindo a lista ordenada
					listarElementos();
					retornarLista(inicio, resposta);
					retornarTempo(tempoFim, resposta);
					
					
					System.out.println();
					System.out.println("Tempo para ordenação em milisegundos: " + tempoFim + "ms\n"
							+ "Tempo para ordenação em segundos: " + tempoFim / 1000 + "s");
					break;
				case 2:
					tempoInicio = System.currentTimeMillis();
					quickSortLista(inicio);
					tempoFim = System.currentTimeMillis() - tempoInicio;

					// Imprimindo a lista ordenada
					listarElementos();
					retornarLista(inicio, resposta);
					retornarTempo(tempoFim, resposta);
					
					System.out.println();
					System.out.println("Tempo para ordenação em milisegundos: " + tempoFim + "ms\n"
							+ "Tempo para ordenação em segundos: " + tempoFim / 1000 + "s");
					break;
				}
			} else if (Integer.valueOf(msgDoCliente) == 3) {
				int vetor[] = new int[totalValores];

				for (int i = 0; i < vetor.length; i++) {
					msgDoCliente = bufferedReader.readLine();

					vetor[i] = Integer.valueOf(msgDoCliente);

					msgParaCliente = msgDoCliente + " recebido e armazenado";
					responder(resposta, msgParaCliente);
				}

				// Imprimindo o vetor desordenado
				imprimirVetor(vetor);
				System.out.println();

				tempoInicio = System.currentTimeMillis();
				Arrays.sort(vetor); // Estrutura de ordenação da linguagem
				tempoFim = System.currentTimeMillis() - tempoInicio;

				// Imprimindo o vetor ordenado
				imprimirVetor(vetor);
				retornarVetor(vetor, resposta);
				retornarTempo(tempoFim, resposta);
				
				System.out.println();
				System.out.println("\nTempo para ordenação em milisegundos: " + tempoFim + "\n"
						+ "Tempo para ordenação em segundos: " + tempoFim / 1000);

			}

			System.out.println();
		}
	}

	// Manipulação rede
	public static void responder(DataOutputStream resposta, String mensagem) throws Exception {
		resposta.writeBytes(mensagem);
		resposta.writeBytes("\n"); // Fim da linha
		resposta.flush(); // Manda para o cliente
	}

	public static void retornarVetor(int vetor[], DataOutputStream resposta) throws Exception {
		for (int i = 0; i < vetor.length; i++) {
			String msgParaCliente = String.valueOf(vetor[i]);
			responder(resposta, msgParaCliente);
		}
	}
	
	public static void retornarLista(Elemento elemento, DataOutputStream resposta) throws Exception {
		while (elemento != null) {
			String msgParaCliente = String.valueOf(elemento.valor);
			responder(resposta, msgParaCliente);
			elemento = elemento.prox;
		}
	}
	
	public static void retornarTempo(long tempoExecucao, DataOutputStream resposta) throws Exception {
		responder(resposta, String.valueOf(tempoExecucao));
	}
	// ./Manipulação rede

	// Estrutura e manipulação lista encadeada
	public static class Elemento {
		int valor;
		Elemento prox;

		public Elemento(int valor) {
			this.valor = valor;
			this.prox = null;
		}

		public Elemento(int valor, Elemento prox) {
			this.valor = valor;
			this.prox = prox;
		}
	}

	static Elemento inicio;
	static Elemento fim;
	static int tamanho;

	public static void listaEncadeada() {
		inicio = null;
		fim = null;
		tamanho = 0;
	}

	public static void listarElementos() {
		Elemento aux = inicio;
		System.out.println("[");
		while (aux != null) {
			System.out.println(aux.valor);
			aux = aux.prox;
		}
		System.out.println("]\n");
	}

	public static void inserirElementoFim(Elemento novo) {
		if (tamanho == 0) {
			inicio = novo;
			fim = novo;
		} else {
			fim.prox = novo;
			fim = novo;
		}
		tamanho++;
	}
	// ./Estrutura e manipulação lista encadeada

	// Ordenação com vetor
	// n²
	public static int[] selectionSortVetor(int vetor[], int tamanhoVetor) {
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

	// n log(n)
	public static int[] mergeSortVetor(int vetor[], int tamanhoVetor) {

		if (tamanhoVetor <= 1)
			return null;
		int aux = tamanhoVetor / 2;
		int esquerda[] = new int[aux];
		int direita[] = new int[tamanhoVetor - aux];
		for (int i = 0; i < aux; i++)
			esquerda[i] = vetor[i];
		for (int i = aux; i < tamanhoVetor; i++)
			direita[i - aux] = vetor[i];
		mergeSortVetor(esquerda, aux);
		mergeSortVetor(direita, tamanhoVetor - aux);
		merge(esquerda, direita, vetor);

		return vetor;

	}

	public static void merge(int esquerda[], int direita[], int vetor[]) {
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

	public static void imprimirVetor(int[] vetor) {
		System.out.print("[");
		for (int i = 0; i < vetor.length; i++) {
			System.out.println(vetor[i]);
		}
		System.out.print("]\n");
	}
	// ./Ordenação com vetor

	// Ordenação com lista
	public static void bubbleSortLista(Elemento inicio, int tamanho) {
		int valor;
		Elemento elemento;
		Elemento proxElemento;

		for (int i = 0; i < tamanho - 1; i++) {
			elemento = inicio;
			proxElemento = inicio.prox;
			for (int j = 0; j < tamanho - 1; j++) {
				if (elemento.valor > proxElemento.valor) {
					valor = elemento.valor;
					elemento.valor = proxElemento.valor;
					proxElemento.valor = valor;
				}
				elemento = elemento.prox;
				proxElemento = proxElemento.prox;
			}
		}
	}

	public static Elemento quickSortLista(Elemento elemento) {
		if (elemento == null)
			return null;

		Elemento elementoAux = elemento;
		Elemento proxElementoAux = elemento.prox;
		Elemento pivo = elemento;
		Elemento temporario = null;
		Elemento particao = null;
		int temp;

		while (proxElementoAux != null) {
			if (proxElementoAux.valor < pivo.valor) {
				temporario = elementoAux;
				particao = elementoAux.prox;

				elementoAux = elementoAux.prox;

				temp = elementoAux.valor;
				elementoAux.valor = proxElementoAux.valor;
				proxElementoAux.valor = temp;
			}
			proxElementoAux = proxElementoAux.prox;
		}

		if (elementoAux != elemento) {
			temp = elementoAux.valor;
			elementoAux.valor = pivo.valor;
			pivo.valor = temp;

			temporario.prox = null;
			quickSortLista(elemento);
			temporario.prox = particao;
		}

		quickSortLista(elementoAux.prox);

		return elemento;
	}
	// ./Ordenação com lista
}
