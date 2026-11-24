import java.util.ArrayList;

// Classe filha: Professor HERDA de Pessoa (extends Pessoa)
// Cada Professor tem uma Modalidade como especialidade, ou seja,
// cada professor é especialista em apenas uma modalidade
public class Professor extends Pessoa {

    private Modalidade especialidade;
    private ArrayList<Aula> minhasAulas;

    // Construtor da filha: usa "super" para chamar o construtor da classe mãe
    // e preencher nome e idade, e depois preenche o que é próprio do Professor
    public Professor(String nome, int idade, Modalidade especialidade) {
        super(nome, idade);
        this.especialidade = especialidade;
        this.minhasAulas = new ArrayList<>();
    }

    public Modalidade getEspecialidade() {
        return especialidade;
    }

    // Adiciona uma nova aula usando a especialidade do próprio professor
    // Assim, cada professor só cadastra aulas da sua modalidade
    public void adicionarAula(String dia, String horario) {
        Aula aula = new Aula(especialidade, dia, horario);
        minhasAulas.add(aula);
    }

    // Monta um texto com todas as aulas cadastradas por esse professor
    public String getAulasFormatadas() {
        if (minhasAulas.isEmpty()) {
            return "Você ainda não tem nenhuma aula cadastrada.";
        }

        String texto = "===== MINHAS AULAS =====\n\n";

        for (int i = 0; i < minhasAulas.size(); i++) {
            texto = texto + (i + 1) + " - " + minhasAulas.get(i) + "\n\n";
        }

        return texto;
    }

    // Monta um texto com os dados do perfil do professor
    // Repare que aqui chamamos especialidade.getDescricao(), que é POLIMORFISMO:
    // o texto muda de acordo com a modalidade (Musculação, Pilates, Crossfit ou Natação)
    public String getPerfilFormatado() {
        String texto = "===== MEU PERFIL =====\n\n";
        texto = texto + "Nome: " + nome + "\n";
        texto = texto + "Idade: " + idade + "\n";
        texto = texto + "Modalidade: " + especialidade.getNome() + "\n";
        texto = texto + "Sobre a modalidade: " + especialidade.getDescricao() + "\n";
        texto = texto + "Quantidade de aulas cadastradas: " + minhasAulas.size();

        return texto;
    }
}
