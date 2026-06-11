package br.edu.utfpr.jogo.ejb;


import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
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

    public void atualizarPontuacao(String nome, int pontos) {
        if (nome != null && !nome.trim().isEmpty()) {
            ranking.put(nome, pontos);
        }
    }

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
