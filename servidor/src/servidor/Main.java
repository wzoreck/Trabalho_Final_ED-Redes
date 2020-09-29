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

	public static int getTamanho() {
		return tamanho;
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

	public static Elemento quickSortLista(Elemento elemento) {
		if (elemento == null)
			return null;

		Elemento i = elemento;
		Elemento j = elemento.prox;
		Elemento pvt = elemento;
		Elemento store = null;
		Elemento ptr = null;
		int temp;

		while (j != null) {
			if (j.valor < pvt.valor) {
				store = i;
				ptr = i.prox;

				i = i.prox;

				temp = i.valor;
				i.valor = j.valor;
				j.valor = temp;
			}
			j = j.prox;
		}

		if (i != elemento) {
			temp = i.valor;
			i.valor = pvt.valor;
			pvt.valor = temp;

			store.prox = null;
			quickSortLista(elemento);
			store.prox = ptr;
		}

		quickSortLista(i.prox);

		return elemento;
	}

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
				int vetor[] = new int[250000];

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
				for (int i = 0; i < 250000; i++) {
					msgCliente = requisicao.readLine();

					// Atribuindo os valores recebidos na lista
					Elemento novo = new Elemento(Integer.valueOf(msgCliente));
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
					quickSortLista(inicio);
					tempoFim = System.currentTimeMillis() - tempoInicio;

					// Imprimindo a lista ordenada
					listarElementos();
					System.out.println();

					System.out.println("Tempo para ordenação em milisegundos: " + tempoFim + "ms\n"
							+ "Tempo para ordenação em segundos: " + tempoFim / 1000 + "s");
					break;
				}
			} else if (Integer.valueOf(msgCliente) == 3) {
				int vetor[] = new int[250000];

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
