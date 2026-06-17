package br.edu.utfpr.jogo.ejb;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/RankingQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue")
})
public class RankingMDB implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(RankingMDB.class.getName());

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage textMessage) {
                String rankingLog = textMessage.getText();
                
                // Imprime no server.log do Payara
                LOGGER.log(Level.INFO, "\n\n{0}\n", rankingLog);
            }
        } catch (JMSException e) {
            LOGGER.log(Level.SEVERE, "Erro ao processar a mensagem do ranking no MDB", e);
        }
    }
}