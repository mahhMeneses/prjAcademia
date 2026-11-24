// Musculação HERDA de Modalidade
public class Musculacao extends Modalidade {

    // Construtor: chama o construtor da mãe (super) já passando o nome fixo
    public Musculacao() {
        super("Musculação");
    }

    // SOBRESCRITA (@Override): reescreve o método abstrato da classe mãe
    @Override
    public String getDescricao() {
        return "Treino de força com pesos livres e máquinas.";
    }
}
