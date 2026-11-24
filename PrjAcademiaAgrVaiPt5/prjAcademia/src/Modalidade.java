// Classe abstrata: serve de molde para as modalidades da academia
// Não pode ser usada com "new Modalidade()", só serve para ser herdada
public abstract class Modalidade {

    protected String nome;

    // Construtor: recebe o nome da modalidade
    public Modalidade(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    // Método abstrato: não tem corpo aqui, cada filha é obrigada a implementá-lo
    // isso é a base do polimorfismo, cada modalidade descreve a aula do seu jeito
    public abstract String getDescricao();
}
