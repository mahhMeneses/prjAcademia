// Pilates HERDA de Modalidade
public class Pilates extends Modalidade {

    public Pilates() {
        super("Pilates");
    }

    // SOBRESCRITA (@Override) do método da classe mãe
    @Override
    public String getDescricao() {
        return "Exercícios de alongamento, controle e respiração.";
    }
}
