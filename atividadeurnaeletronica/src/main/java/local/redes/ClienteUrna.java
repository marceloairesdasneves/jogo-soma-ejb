package local.redes;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Marcelo Neves
 */
public class ClienteUrna {
    
    public static void main(String[] args) {
        try {
            // Localiza o servidor no registro RMI
            InterfaceUrna servidorCentral = (InterfaceUrna) Naming.lookup("rmi://localhost:1099/UrnaCentral");
            // Usando LinkedHashMap para manter a ordem exata exigida na imagem do professor
            try (Scanner teclado = new Scanner(System.in)) {
                // Usando LinkedHashMap para manter a ordem exata exigida na imagem do professor
                Map<Integer, String> candidatosOficiais = new LinkedHashMap<>();
                candidatosOficiais.put(10, "Joao Marcio");
                candidatosOficiais.put(22, "Rita Maria");
                candidatosOficiais.put(40, "Vitor Bello");
                candidatosOficiais.put(50, "Augusto Patto");
                
                boolean continuar = true;
                
                while (continuar) {
                    System.out.println("Urna Java");
                    System.out.println("---------");
                    System.out.println("\nCandidatos:\n");
                    
                    candidatosOficiais.forEach((chapa, nome) -> System.out.println(chapa + " - " + nome));
                    
                    System.out.println("\nEntre o numero do candidato:");
                    String entradaCand = teclado.nextLine().trim();
                    
                    String nomeCandidatoValidado = null;
                    int numeroChapaValidado = 0;
                    
                    if (entradaCand.matches("\\d+")) {
                        int numeroDigitado = Integer.parseInt(entradaCand);
                        if (candidatosOficiais.containsKey(numeroDigitado)) {
                            nomeCandidatoValidado = candidatosOficiais.get(numeroDigitado);
                            numeroChapaValidado = numeroDigitado;
                        }
                    }
                    else {
                        for (Map.Entry<Integer, String> entry : candidatosOficiais.entrySet()) {
                            if (entry.getValue().equalsIgnoreCase(entradaCand)) {
                                nomeCandidatoValidado = entry.getValue();
                                numeroChapaValidado = entry.getKey();
                                break;
                            }
                        }
                    }
                    
                    if (nomeCandidatoValidado == null) {
                        System.out.println("\nCandidato nao encontrado. Tente novamente.\n");
                        continue;
                    }
                    
                    System.out.println("\nEntre o numero de votos:");
                    int votos = 0;
                    try {
                        votos = Integer.parseInt(teclado.nextLine().trim());
                        if (votos < 0) {
                            System.out.println("\nErro: A quantidade de votos nao pode ser negativa.\n");
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("\nErro: Numero de votos inválido.\n");
                        continue;
                    }
                    
                    // Envia os dados para o Servidor RMI
                    servidorCentral.registrarVotos(nomeCandidatoValidado, numeroChapaValidado, votos);
                    
                    // Exata mensagem da imagem
                    System.out.println("\nVotos enviados.");
                    
                    // Funcionalidade opcional implementada conforme o modelo
                    System.out.println("\nContinuar (S/N)?");
                    String resposta = teclado.nextLine().trim();
                    
                    if (resposta.equalsIgnoreCase("N")) {
                        continuar = false;
                    } else {
                        System.out.println("\n"); // Pula linhas para limpar o terminal na próxima rodada
                    }
                }
            }

        } catch (NumberFormatException | MalformedURLException | NotBoundException | RemoteException e) {
            System.err.println("Erro na comunicacao do Cliente: " + e.getMessage());
        }
    }
}