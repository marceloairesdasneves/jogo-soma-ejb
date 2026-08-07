import br.edu.utfpr.atividade2manipulacaoxml.br.gui.model.util.Aluno;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import model.Disciplina;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Criar Objeto
            Aluno aluno = new Aluno(1, "João Silva", "RA123456");
            aluno.getDisciplinas().add(new Disciplina("POO01", "Programação Orientada a Objetos"));

            // 2. Marshalling (Objeto -> XML)
            JAXBContext context = JAXBContext.newInstance(Aluno.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            File arquivoXml = new File("aluno.xml");
            marshaller.marshal(aluno, arquivoXml);
            System.out.println("XML gerado com sucesso em: " + arquivoXml.getAbsolutePath());

            // 3. Unmarshalling (XML -> Objeto)
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Aluno alunoLido = (Aluno) unmarshaller.unmarshal(arquivoXml);
            System.out.println("Aluno Lido do XML: " + alunoLido.getNome() + " | RA: " + alunoLido.getRa());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}