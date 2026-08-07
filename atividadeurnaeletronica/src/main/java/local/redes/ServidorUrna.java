package local.redes;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Marcelo Neves
 */


public class ServidorUrna extends UnicastRemoteObject implements InterfaceUrna {
    
    private final Map<String, Integer> apuracao;

    public ServidorUrna() throws RemoteException {
        super();
        this.apuracao = new ConcurrentHashMap<>();
        iniciarApuracaoAutomatica();
    }

    @Override
    public void registrarVotos(String nomeCandidato, int numeroChapa, int quantidadeVotos) throws RemoteException {
        String chave = nomeCandidato + " (Chapa: " + numeroChapa + ")";
        
        apuracao.merge(chave, quantidadeVotos, Integer::sum);
        
        System.out.println("[Log do Servidor] Recebidos " + quantidadeVotos + " votos para " + chave);
    }

    private void iniciarApuracaoAutomatica() {
        Thread threadApuracao;
        threadApuracao = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000); // Pausa de 5 segundos
                        
                        System.out.println("\n===== APURAÇÃO OFICIAL PARCIAL =====");
                        if (apuracao.isEmpty()) {
                            System.out.println("Aguardando o envio de votos das urnas...");
                        } else {
                            apuracao.forEach((candidato, totalVotos) -> {
                                System.out.println("-> " + candidato + ": " + totalVotos + " votos totais");
                            });
                        }
                        System.out.println("====================================\n");
                        
                    } catch (InterruptedException e) {
                        System.out.println("Apurador interrompido.");
                        break;
                    }
                }
            }
        });
        threadApuracao.start();
    }

    public static void main(String[] args) {
        try {
            // Cria o registro RMI na porta padrão 1099
            LocateRegistry.createRegistry(1099);
            
            // Instancia e publica o Servidor
            ServidorUrna servidor = new ServidorUrna();
            Naming.rebind("rmi://localhost:1099/UrnaCentral", servidor);
            
            System.out.println("Servidor da Urna Central rodando e aguardando conexoes...");
            
        } catch (MalformedURLException | RemoteException e) {
            System.err.println("Erro ao iniciar o Servidor RMI: " + e.getMessage());
        }
    }
}