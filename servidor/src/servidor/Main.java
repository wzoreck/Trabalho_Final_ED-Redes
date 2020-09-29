package servidor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {

	// Estrutura e manipulação lista encadeada
	public static class Elemento {
		int valor;
		int posicao;
		Elemento prox;

		public Elemento(int valor, int posicao) {
			this.valor = valor;
			this.posicao = posicao;
			this.prox = null;
		}

		public Elemento(int valor, int posicao, Elemento prox) {
			this.valor = valor;
			this.posicao = posicao;
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

	public static int getTamanho() {
		return tamanho;
	}

	public static void listarElementos() {
		Elemento aux = inicio;
		System.out.print("[");
		while (aux != null) {
			System.out.print(aux.valor + ", ");
			aux = aux.prox;
		}
		System.out.print("]\n");
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

	public static void imprimirVetor(int[] vetor) {
		System.out.print("[");
		for (int i = 0; i < vetor.length; i++) {
			System.out.print(vetor[i] + ", ");
		}
		System.out.print("]");
	}
	// ./Ordenação com vetor

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

//	public static List<Elemento> ordenarQuickLista(List<Elemento> lista, int inicio, int fim) {
//		if (inicio < fim) {
//			int pIndice = quickTrocaLista(lista, inicio, fim);
//			ordenarQuickLista(lista, inicio, pIndice - 1);
//			ordenarQuickLista(lista, pIndice + 1, fim);
//		}
//		
//		return lista;
//	}

//	public static int quickTrocaLista(List<Elemento> lista, int inicio, int fim) {
//		int aux;
//		int pivot = lista.get(fim).getValor();
//		int pIndice = inicio;
//		
//		for (int i = inicio; i < fim; i++) {
//			if (lista.get(i).getValor() <= pivot) {
//				aux = lista.get(i).getValor();
//				lista.get(i).setValor(lista.get(pIndice).getValor());
//				lista.get(pIndice).setValor(aux);
//				pIndice ++;
//			}
//		}
//		
//		aux = lista.get(pIndice).getValor();
//		lista.get(pIndice).setValor(lista.get(fim).getValor());
//		lista.get(fim).setValor(aux);
//		
//		return pIndice;
//	}

	public static void main(String[] args) throws IOException {
		Socket socket;
		ServerSocket socketServidor = new ServerSocket(2800);

		long tempoInicio, tempoFim;
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
				int vetor[] = new int[10];

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
					tempoInicio = System.currentTimeMillis();
					vetor = selectionSortVetor(vetor, vetor.length);
					tempoFim = System.currentTimeMillis() - tempoInicio;
					System.out.println("Memória usada: " + (Runtime.getRuntime().freeMemory() - rt.freeMemory()));

					// Imprimindo o vetor ordenado
					imprimirVetor(vetor);
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
					System.out.println();

					System.out.println("Tempo para ordenação em milisegundos: " + tempoFim + "ms\n"
							+ "Tempo para ordenação em segundos: " + tempoFim / 1000 + "s");
					break;
				}
			} else if (Integer.valueOf(msgCliente) == 2) {
				listaEncadeada();
				// Recebendo os valores do cliente
				for (int i = 0; i < 10; i++) {
					msgCliente = requisicao.readLine();

					// Atribuindo os valores recebidos na lista
					Elemento novo = new Elemento(Integer.valueOf(msgCliente), i);
					inserirElementoFim(novo);

					resposta.writeBytes("Valor " + msgCliente + " recebido e armazenado");
					resposta.writeBytes("\n"); // Fim da linha
					resposta.flush(); // Manda para o cliente
				}

				// Imprimindo a lista desordenada
				listarElementos();
				System.out.println();

				msgCliente = requisicao.readLine();

				switch (Integer.valueOf(msgCliente)) {
				case 1:
					tempoInicio = System.currentTimeMillis();
					bubbleSortLista(inicio, tamanho);
					tempoFim = System.currentTimeMillis() - tempoInicio;

					// Imprimindo a lista ordenada
					listarElementos();
					System.out.println();

					System.out.println("Tempo para ordenação em milisegundos: " + tempoFim + "ms\n"
							+ "Tempo para ordenação em segundos: " + tempoFim / 1000 + "s");
					break;
				case 2:
					tempoInicio = System.currentTimeMillis();
//						lista = ordenarQuickLista(lista, 0, lista.size() - 1);
					tempoFim = System.currentTimeMillis() - tempoInicio;

					// Imprimindo a lista ordenada
//						imprimirLista(lista);
					System.out.println();

					System.out.println("Tempo para ordenação em milisegundos: " + tempoFim + "ms\n"
							+ "Tempo para ordenação em segundos: " + tempoFim / 1000 + "s");
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

				tempoInicio = System.currentTimeMillis();
				Arrays.sort(vetor); // Estrutura de ordenação da linguagem
				tempoFim = System.currentTimeMillis() - tempoInicio;

				// Imprimindo o vetor ordenado
				imprimirVetor(vetor);
				System.out.println();

				System.out.println("\nTempo para ordenação em milisegundos: " + tempoFim + "\n"
						+ "Tempo para ordenação em segundos: " + tempoFim / 1000);
			}

			System.out.println();
		}
	}
}
