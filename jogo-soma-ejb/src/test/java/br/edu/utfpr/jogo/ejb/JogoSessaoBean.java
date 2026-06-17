package br.edu.utfpr.jogo.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateful;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Map;
import java.util.Random;
/*
@Marcelo Aires das Neves
*/

@Named("jogoBean")
@SessionScoped
@Stateful
public class JogoSessaoBean implements Serializable {

    private String nome;
    private int pontos = 0;
    private int numero1;
    private int numero2;
    private Integer respostaUsuario;
    private String mensagemFeedback = "";

    @EJB
    private RankingGlobalBean rankingGlobal;

    public void prepararNovaRodada() {
        Random random = new Random();
        this.numero1 = random.nextInt(50) + 1;
        this.numero2 = random.nextInt(50) + 1;
        this.respostaUsuario = null;
    }

    public void verificarResposta() {
        if (nome == null || nome.trim().isEmpty()) {
            mensagemFeedback = "Informe seu nome primeiro!";
            return;
        }

        int somaCorreta = numero1 + numero2;

        if (respostaUsuario != null && respostaUsuario == somaCorreta) {
            this.pontos++; // Usa a variável de instância da classe
            mensagemFeedback = "Acertou!";
            rankingGlobal.atualizarPontuacao(nome, this.pontos);
            prepararNovaRodada();
        } else {
            mensagemFeedback = "Errou! Tente novamente.";
        }
    }

    public Map<String, Integer> getRankingAtual() {
        return rankingGlobal.getRankingOrdenado();
    }

    public String getNome() { return nome; }
    public void setNome(String nome) {
        this.nome = nome; 
    }
    public int getPontos() {
        return pontos; 
    }
    public int getNumero1() {
        return numero1; 
    }
    public int getNumero2() {
        return numero2; 
    }
    public Integer getRespostaUsuario() {
        return respostaUsuario;
    }
    public void setRespostaUsuario(Integer respostaUsuario) {
        this.respostaUsuario = respostaUsuario;
    }
    public String getMensagemFeedback() {
        return mensagemFeedback; 
    }
}