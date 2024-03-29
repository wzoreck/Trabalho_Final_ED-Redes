package cliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {

		int tipoOrdenacao, tipoLista, numero, totalValores = 0;
		boolean continuar = true;
		Scanner sc = new Scanner(System.in);
		Random aleatorio = new Random();
		String msgRecebida;
		BufferedWriter bufferedWriter = null;
		BufferedReader bufferedReader;
		Socket socket = null;

		while (continuar) {
			System.out.println();
			System.out.println(
					"█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗");
			System.out.println(
					"╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝");

			System.out.println("\n"
					+ " ▄██████▄     ▄████████ ████████▄     ▄████████ ███▄▄▄▄      ▄████████     ███      ▄██████▄    ▄████████  \n"
					+ "███    ███   ███    ███ ███   ▀███   ███    ███ ███▀▀▀██▄   ███    ███ ▀█████████▄ ███    ███   ███    ███ \n"
					+ "███    ███   ███    ███ ███    ███   ███    █▀  ███   ███   ███    ███    ▀███▀▀██ ███    ███   ███    ███ \n"
					+ "███    ███  ▄███▄▄▄▄██▀ ███    ███  ▄███▄▄▄     ███   ███   ███    ███     ███   ▀ ███    ███  ▄███▄▄▄▄██▀ \n"
					+ "███    ███ ▀▀███▀▀▀▀▀   ███    ███ ▀▀███▀▀▀     ███   ███ ▀███████████     ███     ███    ███ ▀▀███▀▀▀▀▀   \n"
					+ "███    ███ ▀███████████ ███    ███   ███    █▄  ███   ███   ███    ███     ███     ███    ███ ▀███████████ \n"
					+ "███    ███   ███    ███ ███   ▄███   ███    ███ ███   ███   ███    ███     ███     ███    ███   ███    ███ \n"
					+ " ▀██████▀    ███    ███ ████████▀    ██████████  ▀█   █▀    ███    █▀     ▄████▀    ▀██████▀    ███    ███ \n"
					+ "\n");

			System.out.println(
					"█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗");
			System.out.println(
					"╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝");
			System.out.println();
			System.out.println("|--------------------------------------|");
			System.out.println("| Escolha o tipo de lista a ser criada |");
			System.out.println("|--------------------------------------|");
			System.out.println("|[1] - Vetor                           |");
			System.out.println("|[2] - Lista encadeada                 |");
			System.out.println("|[3] - Estrutura própia da linguagem   |");
			System.out.println("|--------------------------------------|");
			System.out.println("|Encerrar o programa                   |");
			System.out.println("|--------------------------------------|");
			System.out.println("|[4] - Sair                            |");
			System.out.println("|--------------------------------------|");
			System.out.println();
			System.out.print("Informe a sua escolha: ");
			tipoLista = sc.nextInt();

			if (tipoLista != 4) {
				System.out.print("Informe o tamanho da lista a ser criada: ");
				totalValores = sc.nextInt();

				socket = new Socket("localhost", 2800);
				bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

				enviar(bufferedWriter, String.valueOf(tipoLista));
				enviar(bufferedWriter, String.valueOf(totalValores));

				// Gerar valores aleatórios e enviar para o servidor
				for (int i = 0; i < totalValores; i++) {
					numero = aleatorio.nextInt(1000000);

					System.out.println();
					System.out.println((i + 1) + "º valor enviado: " + numero);

					enviar(bufferedWriter, String.valueOf(numero));

					bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					msgRecebida = bufferedReader.readLine(); // Lê a resposta do servidor
					System.out.println("resposta do servidor, valor: " + msgRecebida);
				}
			}

			if (tipoLista == 1 || tipoLista == 2) {
				System.out.println();
				System.out.println("|---------------------------------------------|");
				System.out.println("| Escolha a complexidade do tipo de ordenação |");
				System.out.println("|---------------------------------------------|");
				System.out.println("|[1] - n​²                                     |");
				System.out.println("|[2] - n.log(n)                               |");
				System.out.println("|---------------------------------------------|");
				System.out.println();
				System.out.print("Informe a sua escolha: ");
				tipoOrdenacao = sc.nextInt();

				enviar(bufferedWriter, String.valueOf(tipoOrdenacao));

				System.out.println();
				System.out.println("Retorno - Valores oredenados: ");
				System.out.println();
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				for (int i = 0; i < totalValores; i++) {
					msgRecebida = bufferedReader.readLine(); // Lê a resposta do servidor
					System.out.println(msgRecebida);
				}

				// Tempo que demorou para executar
				msgRecebida = bufferedReader.readLine();
				System.out.println();
				System.out.println("\nTempo para ordenação em milisegundos: " + msgRecebida + "\n"
						+ "Tempo para ordenação em segundos: " + Integer.valueOf(msgRecebida) / 1000);

				socket.close();
			} else if (tipoLista == 3) {
				enviar(bufferedWriter, String.valueOf(tipoLista));

				System.out.println();
				System.out.println("Retorno - Valores oredenados: ");
				System.out.println();
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				for (int i = 0; i < totalValores; i++) {
					msgRecebida = bufferedReader.readLine(); // Lê a resposta do servidor
					System.out.println(msgRecebida);
				}

				// Tempo que demorou para executar
				msgRecebida = bufferedReader.readLine();
				System.out.println();
				System.out.println("\nTempo para ordenação em milisegundos: " + msgRecebida + "\n"
						+ "Tempo para ordenação em segundos: " + Integer.valueOf(msgRecebida) / 1000);

				socket.close();
			} else {
				sc.close();
				continuar = false;
				bufferedWriter.close();
				socket.close();
			}
		}

		System.out.println();
		System.out.println("Fim do programa!");
		System.out.println();
		System.out.println("┌┐ ┬ ┬  ╔═╗┌┬┐┌─┐┌┬┐  ┌─┐┌┐┌┌┬┐  ╔╦╗┌─┐┌┐┌┬┌─┐┬  ");
		System.out.println("├┴┐└┬┘  ╠═╣ ││├─┤│││  ├─┤│││ ││   ║║├─┤││││├┤ │  ");
		System.out.println("└─┘ ┴   ╩ ╩─┴┘┴ ┴┴ ┴  ┴ ┴┘└┘─┴┘  ═╩╝┴ ┴┘└┘┴└─┘┴─┘");

	}

	public static void enviar(BufferedWriter bw, String mensagem) throws Exception {
		bw.write(mensagem); // Armazenda a mensagem a ser enviada
		bw.write("\n"); // Fim da linha
		bw.flush(); // Envia mensagem
	}
}
