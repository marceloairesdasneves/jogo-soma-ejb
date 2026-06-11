package local.redes;

// @author Marcelo Neves

import local.redes.Pessoa;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String[] args) {
        System.out.println("=== SERVIDOR INICIALIZADO ===");

        // Uso do try-with-resources no ServerSocket para garantir liberação da porta 50000
        try (ServerSocket server = new ServerSocket(50000)) {
            System.out.println("Aguardando conexões na porta 50000...");

            while (true) {
                // O método accept() bloqueia a execução até que um cliente conecte
                Socket clienteSocket = server.accept();
                System.out.println("\n[Nova Conexão] Cliente conectado do IP: " + clienteSocket.getInetAddress().getHostAddress());

                //  Cria uma nova Thread para atender este cliente específico.
                Thread t = new Thread(new TratadorCliente(clienteSocket));
                t.start();
            }
        } catch (Exception e) {
            System.err.println("Erro crítico no servidor: " + e.getMessage());
        }
    }

    private static class TratadorCliente implements Runnable {
        private final Socket socket;

        public TratadorCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try (ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                 PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true)) {

                Pessoa p = (Pessoa) entrada.readObject();

                // Exibe no Console do Servidor (Requisito do Enunciado)
                System.out.println("[Dados Recebidos] Nome: " + p.getNome() + " | Idade: " + p.getIdade());

                // Devolve o texto limpo para o TextArea do Cliente (Requisito do Enunciado)
                escritor.println("Recebeu do servidor:\nDados recebidos corretamente!");

            } catch (Exception e) {
                System.err.println("Erro ao processar requisição do cliente: " + e.getMessage());
            } finally {

                try {
                    socket.close();// Garante o fechamento da conexão com esse cliente após finalizar o envio
                    System.out.println("[Conexão Finalizada] Socket do cliente fechado com segurança.");
                } catch (IOException ex) {}
            }
        }
    }
}