// Classe mãe: guarda os dados que toda pessoa tem (nome e idade)
public class Pessoa {

    protected String nome;
    protected int idade;

    // Construtor: preenche os dados básicos de uma pessoa
    public Pessoa(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }
}
