package br.edu.utfpr.jogo.ejb;

import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/*
@Marcelo Aires das Neves
*/
@Singleton
@Startup
public class RankingGlobalBean {

    private Map<String, Integer> ranking = new LinkedHashMap<>();
    
    // Variável para guardar quem é o líder atual
    private String liderAtual = "";

    // Injeções necessárias para a mensageria (MDB)
    @Inject
    private JMSContext jmsContext;

    @Resource(lookup = "jms/RankingQueue")
    private Queue filaRanking;

    public void atualizarPontuacao(String nome, int pontos) {
        if (nome != null && !nome.trim().isEmpty()) {
            ranking.put(nome, pontos);
            
            // Sempre que a pontuação atualiza, verificamos se o líder mudou
            verificarNovoLider();
        }
    }

    private void verificarNovoLider() {
        Map<String, Integer> rankingOrdenado = getRankingOrdenado();
        if (rankingOrdenado.isEmpty()) return;

        // Pega o primeiro elemento do Map já ordenado
        Map.Entry<String, Integer> primeiroLugar = rankingOrdenado.entrySet().iterator().next();
        String novoLider = primeiroLugar.getKey();

        // Se o líder for diferente do atual, envia o ranking para o MDB
        if (!novoLider.equals(liderAtual)) {
            liderAtual = novoLider;
            enviarMensagemMDB(rankingOrdenado);
        }
    }

    private void enviarMensagemMDB(Map<String, Integer> rankingOrdenado) {
        StringBuilder sb = new StringBuilder();
        sb.append("===== Novo Líder: ").append(liderAtual).append(" =====\n");
        
        int posicao = 1;
        for (Map.Entry<String, Integer> entry : rankingOrdenado.entrySet()) {
            sb.append(posicao++).append(". ").append(entry.getKey())
              .append(" - ").append(entry.getValue()).append(" pontos\n");
        }

        try {
            if (jmsContext != null && filaRanking != null) {
                jmsContext.createProducer().send(filaRanking, sb.toString());
            }
        } catch (Exception e) {
            System.err.println("Erro ao enviar mensagem JMS: " + e.getMessage());
        }
    }

    // Seu método original mantido intacto!
    public Map<String, Integer> getRankingOrdenado() {
        return ranking.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey, 
                        Map.Entry::getValue, 
                        (e1, e2) -> e1, 
                        LinkedHashMap::new));
    }
}