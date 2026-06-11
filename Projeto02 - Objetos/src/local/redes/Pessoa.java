package local.redes;

import java.io.Serializable; // IMPORT OBRIGATÓRIO

// @author Marcelo Neves
public class Pessoa implements Serializable { // ADICIONE O IMPLEMENTS AQUI

    // Define uma versão para o ID de serialização
    private static final long serialVersionUID = 1L;

    private String nome;
    private int idade;

    public Pessoa(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
    }

    public String getNome() { return nome; }
    public int getIdade() { return idade; }
}