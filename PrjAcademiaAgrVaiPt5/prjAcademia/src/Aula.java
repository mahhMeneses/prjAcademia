// Representa uma aula da academia: a modalidade, o dia da semana e o horário
public class Aula {

    private Modalidade modalidade;
    private String dia;
    private String horario;

    public Aula(Modalidade modalidade, String dia, String horario) {
        this.modalidade = modalidade;
        this.dia = dia;
        this.horario = horario;
    }

    public Modalidade getModalidade() {
        return modalidade;
    }

    public String getDia() {
        return dia;
    }

    public String getHorario() {
        return horario;
    }

    // O Professor concatena a aula num texto ("1 - " + aula), então o toString monta a linha
    @Override
    public String toString() {
        return modalidade.getNome() + " | Dia: " + dia + " | Horário: " + horario;
    }
}
