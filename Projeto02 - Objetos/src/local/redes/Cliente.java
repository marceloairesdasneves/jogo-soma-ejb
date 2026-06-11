package local.redes;

// @author Marcelo Neves

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class Cliente extends JFrame {
    private JTextField txtNome, txtIdade;
    private JTextArea txtRetorno;
    private JButton btnEnviar;

    public Cliente() {
        setTitle("Cliente");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        painelPrincipal.add(new JLabel("Nome"), gbc);

        gbc.gridy = 1;
        txtNome = new JTextField();
        painelPrincipal.add(txtNome, gbc);

        gbc.gridy = 2;
        painelPrincipal.add(new JLabel("Idade"), gbc);

        gbc.gridy = 3;
        txtIdade = new JTextField();
        painelPrincipal.add(txtIdade, gbc);

        gbc.gridy = 4;
        painelPrincipal.add(new JLabel("Retorno do Servidor"), gbc);

        gbc.gridy = 5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        txtRetorno = new JTextArea(6, 30);
        txtRetorno.setEditable(false);
        painelPrincipal.add(new JScrollPane(txtRetorno), gbc);

        gbc.gridy = 6;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        btnEnviar = new JButton("Enviar");
        painelPrincipal.add(btnEnviar, gbc);

        add(painelPrincipal);

        btnEnviar.addActionListener(e -> enviarDados());
    }

    private void enviarDados() {
        // O cliente se conecta no localhost porta 57000
        try (Socket socket = new Socket("127.0.0.1", 50000);
             ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String nome = txtNome.getText();
            int idade = Integer.parseInt(txtIdade.getText());
            Pessoa p = new Pessoa(nome, idade);

            saida.writeObject(p);
            saida.flush();

            StringBuilder respostaCompleta = new StringBuilder();
            String linha;
            while ((linha = entrada.readLine()) != null) {
                respostaCompleta.append(linha).append("\n");
            }
            txtRetorno.setText(respostaCompleta.toString().trim());

            txtNome.setText("");
            txtIdade.setText("");

        } catch (Exception ex) {
            txtRetorno.setText("Erro ao conectar no servidor!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Cliente().setVisible(true));
    }
}