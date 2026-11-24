import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Classe responsável só por abrir a conexão com o MySQL do XAMPP
public class Conexao {

    private static final String URL =
            "jdbc:mysql://localhost:3306/academia?useSSL=false&serverTimezone=America/Sao_Paulo&characterEncoding=UTF-8";
    private static final String USUARIO = "root";
    private static final String SENHA = ""; // padrão do XAMPP é senha vazia

    // Cada chamada devolve uma conexão nova; quem usar deve fechar (try-with-resources)
    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
