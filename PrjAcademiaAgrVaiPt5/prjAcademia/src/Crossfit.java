// Crossfit HERDA de Modalidade
public class Crossfit extends Modalidade {

    public Crossfit() {
        super("Crossfit");
    }

    // SOBRESCRITA (@Override) do método da classe mãe
    @Override
    public String getDescricao() {
        return "Treino funcional de alta intensidade.";
    }
}
