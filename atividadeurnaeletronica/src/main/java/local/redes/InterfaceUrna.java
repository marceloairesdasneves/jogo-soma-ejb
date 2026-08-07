package local.redes;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Marcelo Neves
 */


public interface InterfaceUrna extends Remote {
    
    void registrarVotos(String nomeCandidato, int numeroChapa, int quantidadeVotos) throws RemoteException;
    
}