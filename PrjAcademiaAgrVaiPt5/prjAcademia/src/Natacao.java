// Natação HERDA de Modalidade
public class Natacao extends Modalidade {

    public Natacao() {
        super("Natação");
    }

    // SOBRESCRITA (@Override) do método da classe mãe
    @Override
    public String getDescricao() {
        return "Aula de natação para condicionamento físico.";
    }
}
