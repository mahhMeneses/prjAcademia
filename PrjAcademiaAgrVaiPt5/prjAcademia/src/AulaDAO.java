import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// DAO = classe que conversa com o banco. Aqui ficam todos os comandos SQL das aulas.
public class AulaDAO {

    // Salva uma aula para o professor informado (procura o professor pelo nome)
    public static void salvar(String nomeProfessor, String dia, String horario) throws SQLException {
        String sql = "INSERT INTO aula (professor_id, dia, horario) "
                   + "SELECT id, ?, ? FROM professor WHERE nome = ?";

        try (Connection con = ConnectionFactory.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dia);
            ps.setString(2, horario);
            ps.setString(3, nomeProfessor);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Professor '" + nomeProfessor + "' não encontrado no banco.");
            }
        }
    }

    // Busca todas as aulas de um professor. Cada item é {dia, horario}
    public static List<String[]> listarPorProfessor(String nomeProfessor) throws SQLException {
        String sql = "SELECT a.dia, a.horario FROM aula a "
                   + "JOIN professor p ON p.id = a.professor_id "
                   + "WHERE p.nome = ? ORDER BY a.id";

        List<String[]> aulas = new ArrayList<>();

        try (Connection con = ConnectionFactory.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nomeProfessor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    aulas.add(new String[]{rs.getString("dia"), rs.getString("horario")});
                }
            }
        }
        return aulas;
    }
}
