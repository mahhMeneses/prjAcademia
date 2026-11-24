import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Classe responsável pela tela de login
// O topo da janela foi estilizado como o cabeçalho de um site e o sistema
// agora é exclusivo para professores, com um link para criar uma conta nova
// Continua aceitando qualquer usuário e senha: serve apenas para simular o acesso
//
// Todos os tamanhos (fontes, botões, campos, espaçamentos) são calculados a
// partir do tamanho da tela do usuário, usando um fator de escala "escala".
// Assim a tela fica proporcional tanto em monitores pequenos quanto grandes.
public class Login extends JFrame implements ActionListener {

    private static final Color COR_CABECALHO = new Color(33, 47, 60);
    private static final Color COR_DESTAQUE = new Color(41, 128, 185);
    private static final Color COR_FUNDO = new Color(245, 246, 247);

    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JLabel labelMensagem;

    // Fator de escala calculado com base na altura da tela (referência: 900px = escala 1.0)
    private double escala;
    private int larguraFormulario;
    private int alturaFormulario;

    // Construtor: monta a janela de login
    public Login() {
        super("Login - Sistema da Academia");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        calcularEscala();

        // A tela de login abre maximizada (ocupando toda a tela)
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(360, 400));

        add(criarCabecalho(), BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);
    }

    // Calcula o fator de escala e o tamanho do formulário com base na tela do usuário
    private void calcularEscala() {
        Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();

        escala = tela.height / 900.0;
        escala = Math.max(0.85, Math.min(escala, 1.8));

        larguraFormulario = (int) (tela.width * 0.30);
        alturaFormulario = (int) (tela.height * 0.52);
        larguraFormulario = Math.max(360, Math.min(larguraFormulario, 560));
        alturaFormulario = Math.max(400, Math.min(alturaFormulario, 600));
    }

    // Atalho para escalar um valor inteiro conforme o tamanho da tela
    private int esc(double valor) {
        return (int) Math.round(valor * escala);
    }

    // Cabeçalho com o "logo" da academia, no mesmo estilo usado na tela principal
    private JPanel criarCabecalho() {
        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(COR_CABECALHO);
        cabecalho.setBorder(new EmptyBorder(esc(25), esc(20), esc(25), esc(20)));

        JLabel titulo = new JLabel("GuaiaGym");
        titulo.setFont(new Font("SansSerif", Font.BOLD, esc(22)));
        titulo.setForeground(Color.WHITE);

        cabecalho.add(titulo);
        return cabecalho;
    }

    // Formulário de login: usuário, senha e ações (tudo centralizado)
    private JPanel criarFormulario() {
        JPanel painelExterno = new JPanel(new GridBagLayout());
        painelExterno.setBackground(COR_FUNDO);

        JPanel painel = new JPanel();
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 222, 226)),
                new EmptyBorder(esc(30), esc(35), esc(30), esc(35))));
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        Dimension tamanhoFormulario = new Dimension(larguraFormulario, alturaFormulario);
        painel.setPreferredSize(tamanhoFormulario);
        painel.setMinimumSize(tamanhoFormulario);
        painel.setMaximumSize(tamanhoFormulario);

        JLabel labelTitulo = new JLabel("Área do Professor");
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, esc(20)));

        JLabel labelSubtitulo = new JLabel("Entre com seu usuário e senha");
        labelSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, esc(13)));
        labelSubtitulo.setForeground(new Color(127, 140, 141));

        JLabel labelUsuario = new JLabel("Usuário:");
        labelUsuario.setFont(new Font("SansSerif", Font.PLAIN, esc(13)));
        labelUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setFont(new Font("SansSerif", Font.PLAIN, esc(13)));
        labelSenha.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoUsuario = new JTextField();
        estilizarCampo(campoUsuario);

        campoSenha = new JPasswordField();
        estilizarCampo(campoSenha);

        JButton botaoEntrar = new JButton("Entrar");
        estilizarBotaoPrincipal(botaoEntrar);
        botaoEntrar.addActionListener(this);

        JButton botaoCriarConta = new JButton("Criar conta");
        botaoCriarConta.setBorderPainted(false);
        botaoCriarConta.setContentAreaFilled(false);
        botaoCriarConta.setForeground(COR_DESTAQUE);
        botaoCriarConta.setFont(new Font("SansSerif", Font.PLAIN, esc(13)));
        botaoCriarConta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoCriarConta.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoCriarConta.addActionListener(this);

        labelMensagem = new JLabel(" ");
        labelMensagem.setForeground(new Color(192, 57, 43));
        labelMensagem.setFont(new Font("SansSerif", Font.PLAIN, esc(12)));
        labelMensagem.setAlignmentX(Component.CENTER_ALIGNMENT);

        painel.add(Box.createVerticalGlue());
        painel.add(labelTitulo);
        painel.add(Box.createVerticalStrut(esc(6)));
        painel.add(labelSubtitulo);
        painel.add(Box.createVerticalStrut(esc(28)));
        painel.add(labelUsuario);
        painel.add(Box.createVerticalStrut(esc(4)));
        painel.add(campoUsuario);
        painel.add(Box.createVerticalStrut(esc(15)));
        painel.add(labelSenha);
        painel.add(Box.createVerticalStrut(esc(4)));
        painel.add(campoSenha);
        painel.add(Box.createVerticalStrut(esc(25)));
        painel.add(botaoEntrar);
        painel.add(Box.createVerticalStrut(esc(15)));
        painel.add(botaoCriarConta);
        painel.add(Box.createVerticalStrut(esc(10)));
        painel.add(labelMensagem);
        painel.add(Box.createVerticalGlue());

        // Centraliza o formulário na tela maximizada
        painelExterno.add(painel);

        return painelExterno;
    }

    // Deixa os campos de texto e senha com altura e fonte proporcionais
    private void estilizarCampo(JTextField campo) {
        int largura = larguraFormulario - esc(70);
        campo.setFont(new Font("SansSerif", Font.PLAIN, esc(14)));
        campo.setAlignmentX(Component.CENTER_ALIGNMENT);
        campo.setMaximumSize(new Dimension(largura, esc(38)));
        campo.setPreferredSize(new Dimension(largura, esc(38)));
    }

    // Deixa o botão principal com destaque, no mesmo padrão de cor do cabeçalho
    private void estilizarBotaoPrincipal(JButton botao) {
        int largura = larguraFormulario - esc(70);
        botao.setBackground(COR_DESTAQUE);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("SansSerif", Font.BOLD, esc(15)));
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setMaximumSize(new Dimension(largura, esc(42)));
        botao.setPreferredSize(new Dimension(largura, esc(42)));
    }

    // Método chamado quando algum botão da tela é clicado
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Criar conta")) {
            this.dispose();
            CriarConta telaCriarConta = new CriarConta(getTitle());
            telaCriarConta.setVisible(true);
            return;
        }

        String usuario = campoUsuario.getText().trim();
        String senha = new String(campoSenha.getPassword());

        if (usuario.isEmpty() || senha.isEmpty()) {
            labelMensagem.setText("Preencha usuário e senha.");
            return;
        }

        // Não validamos usuário e senha de propósito: qualquer valor entra no sistema
        // Fecha a tela de login e abre a tela principal do sistema (somente professor)
        this.dispose();

        Sistema sistema = new Sistema(usuario, senha);
        sistema.setVisible(true);
    }
}