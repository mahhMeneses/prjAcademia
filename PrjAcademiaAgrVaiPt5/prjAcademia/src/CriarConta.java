import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

// Classe responsável pela janela principal do sistema (somente professor)
// O cabeçalho é estilizado como o header de um site, e todas as telas ficam
// dentro da própria janela (usando CardLayout), em cartões centralizados
public class CriarConta extends JFrame implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Cores usadas para deixar a interface parecida com a de um site
    private static final Color COR_CABECALHO = new Color(33, 47, 60);
    private static final Color COR_BOTAO = new Color(52, 73, 94);
    private static final Color COR_BOTAO_HOVER = new Color(41, 128, 185);
    private static final Color COR_SAIR = new Color(192, 57, 43);
    private static final Color COR_FUNDO = new Color(245, 246, 247);
    private static final Color COR_SUCESSO = new Color(39, 174, 96);

    // Um professor para cada modalidade da academia
    private Professor professorMusculacao;
    private Professor professorPilates;
    private Professor professorCrossfit;
    private Professor professorNatacao;
    private Professor professorAtual;
    private Professor[] professoresDisponiveis;

    // Nome informado no login
    private String nomeUsuario;

    // Painel central que troca de conteúdo conforme o botão clicado no cabeçalho
    private JPanel painelConteudo;
    private CardLayout cardLayout;

    // Componentes que precisam ser atualizados toda vez que a tela é exibida
    private JTextArea areaAulas;
    private JTextArea areaPerfil;
    private JLabel labelConfirmacaoProfessor;

    // Construtor: recebe o nome informado no login
    public CriarConta(String nomeUsuario) {
        super("Sistema da Academia");
        this.nomeUsuario = nomeUsuario;

        setSize(900, 560);
        setMinimumSize(new Dimension(900, 560));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        criarProfessores();
        carregarAulasDoBanco();

        add(criarCabecalho(), BorderLayout.NORTH);
        add(criarPainelConteudo(), BorderLayout.CENTER);

        // A tela inicial é a lista de aulas do professor
        areaAulas.setText(professorAtual.getAulasFormatadas());
        cardLayout.show(painelConteudo, "aulas");
    }

    // Cria um professor especialista em cada modalidade
    private void criarProfessores() {
        professorMusculacao = new Professor("Carlos", 35, new Musculacao());
        professorPilates = new Professor("Marina", 29, new Pilates());
        professorCrossfit = new Professor("Rafael", 32, new Crossfit());
        professorNatacao = new Professor("Juliana", 27, new Natacao());

        // Por padrão, o sistema começa com o professor de Musculação selecionado
        professorAtual = professorMusculacao;

        professoresDisponiveis = new Professor[]{
                professorMusculacao, professorPilates, professorCrossfit, professorNatacao
        };
    }

    // Lê do MySQL as aulas já salvas e coloca na lista de cada professor
    private void carregarAulasDoBanco() {
        for (Professor professor : professoresDisponiveis) {
            try {
                for (String[] aula : AulaDAO.listarPorProfessor(professor.getNome())) {
                    professor.adicionarAula(aula[0], aula[1]);
                }
            } catch (SQLException e) {
                System.err.println("Não foi possível carregar as aulas de "
                        + professor.getNome() + ": " + e.getMessage());
            }
        }
    }

    // ---------- Cabeçalho (header) ----------

    // Monta o cabeçalho no estilo de um site: logo à esquerda e botões centralizados
    private JPanel criarCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(COR_CABECALHO);
        cabecalho.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel titulo = new JLabel("GuaiaGym");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("   |   Professor: " + nomeUsuario);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(189, 195, 199));

        JPanel painelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        painelTitulo.setOpaque(false);
        painelTitulo.add(titulo);
        painelTitulo.add(subtitulo);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        painelBotoes.setOpaque(false);
        painelBotoes.add(criarBotaoMenu("Escolher professor"));
        painelBotoes.add(criarBotaoMenu("Minhas aulas"));
        painelBotoes.add(criarBotaoMenu("Adicionar aula"));
        painelBotoes.add(criarBotaoMenu("Meu perfil"));
        painelBotoes.add(criarBotaoSair());

        cabecalho.add(painelTitulo, BorderLayout.WEST);
        cabecalho.add(painelBotoes, BorderLayout.CENTER);

        return cabecalho;
    }

    // Cria um botão do cabeçalho já estilizado, com destaque ao passar o mouse
    private JButton criarBotaoMenu(String texto) {
        JButton botao = new JButton(texto);
        botao.setFocusPainted(false);
        botao.setForeground(Color.WHITE);
        botao.setBackground(COR_BOTAO);
        botao.setBorder(new EmptyBorder(8, 14, 8, 14));
        botao.setFont(new Font("SansSerif", Font.PLAIN, 13));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.addActionListener(this);

        // Efeito simples de destaque ao passar o mouse por cima, como num site
        botao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botao.setBackground(COR_BOTAO_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botao.setBackground(COR_BOTAO);
            }
        });

        return botao;
    }

    // Botão "Sair" com cor diferente, para destacar que ele encerra o sistema
    private JButton criarBotaoSair() {
        JButton botao = new JButton("Sair");
        botao.setFocusPainted(false);
        botao.setForeground(Color.WHITE);
        botao.setBackground(COR_SAIR);
        botao.setBorder(new EmptyBorder(8, 14, 8, 14));
        botao.setFont(new Font("SansSerif", Font.PLAIN, 13));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.addActionListener(this);
        return botao;
    }

    // ---------- Conteúdo central (CardLayout) ----------

    // Monta o painel central, que troca de conteúdo com CardLayout em vez de abrir pop-ups
    private JPanel criarPainelConteudo() {
        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);
        painelConteudo.setBackground(COR_FUNDO);

        painelConteudo.add(criarPainelEscolherProfessor(), "escolherProfessor");
        painelConteudo.add(criarPainelMinhasAulas(), "aulas");
        painelConteudo.add(criarPainelAdicionarAula(), "adicionarAula");
        painelConteudo.add(criarPainelPerfil(), "perfil");

        return painelConteudo;
    }

    // Cria o título padrão usado no topo de cada cartão, centralizado
    private JLabel criarTituloTela(String texto) {
        JLabel titulo = new JLabel(texto);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        return titulo;
    }

    // Cria uma área de texto somente leitura, usada para exibir listas e perfis
    private JTextArea criarAreaTexto() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setBackground(Color.WHITE);
        area.setBorder(new EmptyBorder(10, 12, 10, 12));
        return area;
    }

    // Cria um cartão branco com tamanho fixo e o título centralizado no topo
    private JPanel criarCartao(String titulo, int largura, int altura) {
        JPanel cartao = new JPanel();
        cartao.setLayout(new BoxLayout(cartao, BoxLayout.Y_AXIS));
        cartao.setBackground(Color.WHITE);
        cartao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 222, 226)),
                new EmptyBorder(25, 30, 25, 30)));

        Dimension tamanho = new Dimension(largura, altura);
        cartao.setPreferredSize(tamanho);
        cartao.setMinimumSize(tamanho);
        cartao.setMaximumSize(tamanho);

        cartao.add(criarTituloTela(titulo));
        cartao.add(Box.createVerticalStrut(20));

        return cartao;
    }

    // Coloca o cartão no centro da tela (horizontal e vertical)
    private JPanel centralizar(JPanel cartao) {
        JPanel externo = new JPanel(new GridBagLayout());
        externo.setBackground(COR_FUNDO);
        externo.add(cartao);
        return externo;
    }

    // Cria um campo de texto centralizado e com tamanho padrão
    private JTextField criarCampo() {
        JTextField campo = new JTextField();
        campo.setMaximumSize(new Dimension(300, 32));
        campo.setAlignmentX(Component.CENTER_ALIGNMENT);
        return campo;
    }

    // Cria um rótulo centralizado para os campos do formulário
    private JLabel criarRotulo(String texto) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        return rotulo;
    }

    // ---------- Telas do professor ----------

    // Tela para trocar qual professor está ativo no momento
    private JPanel criarPainelEscolherProfessor() {
        JPanel cartao = criarCartao("Escolher professor", 420, 400);

        String[] opcoes = {"Carlos - Musculação", "Marina - Pilates", "Rafael - Crossfit", "Juliana - Natação"};

        for (int i = 0; i < opcoes.length; i++) {
            Professor professor = professoresDisponiveis[i];
            JButton botao = new JButton(opcoes[i]);
            botao.setAlignmentX(Component.CENTER_ALIGNMENT);
            botao.setMaximumSize(new Dimension(300, 40));
            botao.setFocusPainted(false);
            botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
            botao.addActionListener(ev -> {
                professorAtual = professor;
                labelConfirmacaoProfessor.setText("Professor selecionado: " + professor.getNome());
            });
            cartao.add(botao);
            cartao.add(Box.createVerticalStrut(10));
        }

        cartao.add(Box.createVerticalStrut(10));

        labelConfirmacaoProfessor = new JLabel("Professor selecionado: " + professorAtual.getNome());
        labelConfirmacaoProfessor.setForeground(COR_SUCESSO);
        labelConfirmacaoProfessor.setAlignmentX(Component.CENTER_ALIGNMENT);
        cartao.add(labelConfirmacaoProfessor);

        return centralizar(cartao);
    }

    // Tela com as aulas cadastradas pelo professor atual
    private JPanel criarPainelMinhasAulas() {
        JPanel cartao = criarCartao("Minhas aulas", 620, 440);

        areaAulas = criarAreaTexto();

        JScrollPane scroll = new JScrollPane(areaAulas);
        scroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        cartao.add(scroll);

        return centralizar(cartao);
    }

    // Tela para cadastrar uma nova aula para o professor atual
    private JPanel criarPainelAdicionarAula() {
        JPanel cartao = criarCartao("Adicionar aula", 420, 400);

        JTextField campoDia = criarCampo();
        JTextField campoHorario = criarCampo();

        JLabel labelResultado = new JLabel(" ");
        labelResultado.setForeground(COR_SUCESSO);
        labelResultado.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton botaoCadastrar = new JButton("Cadastrar aula");
        botaoCadastrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoCadastrar.setMaximumSize(new Dimension(300, 40));
        botaoCadastrar.setFocusPainted(false);
        botaoCadastrar.setBackground(COR_BOTAO_HOVER);
        botaoCadastrar.setForeground(Color.WHITE);
        botaoCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        botaoCadastrar.addActionListener(ev -> {
            String dia = campoDia.getText().trim();
            String horario = campoHorario.getText().trim();

            if (dia.isEmpty() || horario.isEmpty()) {
                labelResultado.setForeground(COR_SAIR);
                labelResultado.setText("Preencha o dia e o horário.");
                return;
            }

            try {
                AulaDAO.salvar(professorAtual.getNome(), dia, horario);
            } catch (SQLException ex) {
                labelResultado.setForeground(COR_SAIR);
                labelResultado.setText("Erro ao salvar no banco: " + ex.getMessage());
                return;
            }

            professorAtual.adicionarAula(dia, horario);
            campoDia.setText("");
            campoHorario.setText("");

            labelResultado.setForeground(COR_SUCESSO);
            labelResultado.setText("Aula cadastrada com sucesso!");
        });

        cartao.add(criarRotulo("Dia da semana:"));
        cartao.add(Box.createVerticalStrut(4));
        cartao.add(campoDia);
        cartao.add(Box.createVerticalStrut(14));
        cartao.add(criarRotulo("Horário:"));
        cartao.add(Box.createVerticalStrut(4));
        cartao.add(campoHorario);
        cartao.add(Box.createVerticalStrut(22));
        cartao.add(botaoCadastrar);
        cartao.add(Box.createVerticalStrut(14));
        cartao.add(labelResultado);

        return centralizar(cartao);
    }

    // Tela com o perfil formatado do professor atual
    private JPanel criarPainelPerfil() {
        JPanel cartao = criarCartao("Meu perfil", 620, 440);

        areaPerfil = criarAreaTexto();

        JScrollPane scroll = new JScrollPane(areaPerfil);
        scroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        cartao.add(scroll);

        return centralizar(cartao);
    }

    // ---------- Ações do cabeçalho ----------

    // Método chamado automaticamente sempre que um botão do cabeçalho é clicado
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        switch (comando) {
            case "Escolher professor":
                cardLayout.show(painelConteudo, "escolherProfessor");
                break;

            case "Minhas aulas":
                areaAulas.setText(professorAtual.getAulasFormatadas());
                cardLayout.show(painelConteudo, "aulas");
                break;

            case "Adicionar aula":
                cardLayout.show(painelConteudo, "adicionarAula");
                break;

            case "Meu perfil":
                areaPerfil.setText(professorAtual.getPerfilFormatado());
                cardLayout.show(painelConteudo, "perfil");
                break;

            case "Sair":
                System.exit(0);
                break;
        }
    }
}