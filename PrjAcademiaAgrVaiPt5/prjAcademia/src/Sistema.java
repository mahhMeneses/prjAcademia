import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

// Classe responsável pela janela principal do sistema
// O antigo JMenu foi substituído por um cabeçalho estilizado, como o header de um site,
// e todas as telas ficam dentro da própria janela (usando CardLayout), sem JOptionPane
public class Sistema extends JFrame implements ActionListener {

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

    // Dados de quem fez login (Professor ou Aluno) e o nome informado
    private String tipoUsuario;
    private String nomeUsuario;

    // Painel central que troca de conteúdo conforme o botão clicado no cabeçalho
    private JPanel painelConteudo;
    private CardLayout cardLayout;

    // Componentes que precisam ser atualizados toda vez que a tela é exibida
    private JTextArea areaAulas;
    private JTextArea areaPerfil;
    private JTextArea areaTodasAsAulas;
    private JLabel labelConfirmacaoProfessor;

    // Combo usado pelo Aluno para escolher em qual professor/modalidade a aula será cadastrada
    private JComboBox<String> comboProfessorAluno;
    private Professor[] professoresDisponiveis;

    // Construtor: recebe o tipo de usuário (Professor ou Aluno) e o nome informado no login
    public Sistema(String tipoUsuario, String nomeUsuario) {
        super("Sistema da Academia");
        this.tipoUsuario = tipoUsuario;
        this.nomeUsuario = nomeUsuario;

        setSize(720, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        criarProfessores();
        carregarAulasDoBanco();

        add(criarCabecalho(), BorderLayout.NORTH);
        add(criarPainelConteudo(), BorderLayout.CENTER);

        // Cada tipo de usuário começa numa tela inicial diferente
        if (tipoUsuario.equals("Professor")) {
            areaAulas.setText(professorAtual.getAulasFormatadas());
            cardLayout.show(painelConteudo, "aulas");
        } else {
            cardLayout.show(painelConteudo, "modalidades");
        }
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

    // Monta o cabeçalho no estilo de um site: logo/título à esquerda e botões à direita
    private JPanel criarCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(COR_CABECALHO);
        cabecalho.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel titulo = new JLabel("GuaiaGym");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("   |   " + tipoUsuario + ": " + nomeUsuario);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(189, 195, 199));

        JPanel painelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        painelTitulo.setOpaque(false);
        painelTitulo.add(titulo);
        painelTitulo.add(subtitulo);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        painelBotoes.setOpaque(false);

        if (tipoUsuario.equals("Professor")) {
            painelBotoes.add(criarBotaoMenu("Escolher professor"));
            painelBotoes.add(criarBotaoMenu("Minhas aulas"));
            painelBotoes.add(criarBotaoMenu("Adicionar aula"));
            painelBotoes.add(criarBotaoMenu("Meu perfil"));
        } else {
            painelBotoes.add(criarBotaoMenu("Modalidades"));
            painelBotoes.add(criarBotaoMenu("Todas as aulas"));
            painelBotoes.add(criarBotaoMenu("Professores"));
            painelBotoes.add(criarBotaoMenu("Adicionar aula"));
        }

        painelBotoes.add(criarBotaoSair());

        cabecalho.add(painelTitulo, BorderLayout.WEST);
        cabecalho.add(painelBotoes, BorderLayout.EAST);

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
        painelConteudo.add(criarPainelModalidades(), "modalidades");
        painelConteudo.add(criarPainelTodasAsAulas(), "todasAulas");
        painelConteudo.add(criarPainelProfessores(), "professores");
        painelConteudo.add(criarPainelAdicionarAulaAluno(), "adicionarAulaAluno");

        return painelConteudo;
    }

    // Cria o título padrão usado no topo de cada tela do conteúdo
    private JLabel criarTituloTela(String texto) {
        JLabel titulo = new JLabel(texto);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
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
        return area;
    }

    // ---------- Telas do professor ----------

    // Tela para trocar qual professor está ativo no momento
    private JPanel criarPainelEscolherProfessor() {
        JPanel painel = new JPanel();
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(25, 25, 25, 25));
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.add(criarTituloTela("Escolher professor"));
        painel.add(Box.createVerticalStrut(15));

        String[] opcoes = {"Carlos - Musculação", "Marina - Pilates", "Rafael - Crossfit", "Juliana - Natação"};

        for (int i = 0; i < opcoes.length; i++) {
            Professor professor = professoresDisponiveis[i];
            JButton botao = new JButton(opcoes[i]);
            botao.setAlignmentX(Component.LEFT_ALIGNMENT);
            botao.setMaximumSize(new Dimension(280, 35));
            botao.setFocusPainted(false);
            botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
            botao.addActionListener(ev -> {
                professorAtual = professor;
                labelConfirmacaoProfessor.setText("Professor selecionado: " + professor.getNome());
            });
            painel.add(botao);
            painel.add(Box.createVerticalStrut(8));
        }

        painel.add(Box.createVerticalStrut(10));

        labelConfirmacaoProfessor = new JLabel(" ");
        labelConfirmacaoProfessor.setForeground(COR_SUCESSO);
        labelConfirmacaoProfessor.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.add(labelConfirmacaoProfessor);

        return painel;
    }

    // Tela com as aulas cadastradas pelo professor atual
    private JPanel criarPainelMinhasAulas() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(25, 25, 25, 25));

        areaAulas = criarAreaTexto();

        painel.add(criarTituloTela("Minhas aulas"), BorderLayout.NORTH);
        painel.add(new JScrollPane(areaAulas), BorderLayout.CENTER);

        return painel;
    }

    // Tela para cadastrar uma nova aula para o professor atual (fluxo do Professor)
    private JPanel criarPainelAdicionarAula() {
        JPanel painel = new JPanel();
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(25, 25, 25, 25));
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.add(criarTituloTela("Adicionar aula"));
        painel.add(Box.createVerticalStrut(15));

        JTextField campoDia = new JTextField();
        campoDia.setMaximumSize(new Dimension(280, 30));
        campoDia.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField campoHorario = new JTextField();
        campoHorario.setMaximumSize(new Dimension(280, 30));
        campoHorario.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelResultado = new JLabel(" ");
        labelResultado.setForeground(COR_SUCESSO);
        labelResultado.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelDia = new JLabel("Dia da semana:");
        labelDia.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelHorario = new JLabel("Horário:");
        labelHorario.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton botaoCadastrar = new JButton("Cadastrar aula");
        botaoCadastrar.setAlignmentX(Component.LEFT_ALIGNMENT);
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

        painel.add(labelDia);
        painel.add(campoDia);
        painel.add(Box.createVerticalStrut(12));
        painel.add(labelHorario);
        painel.add(campoHorario);
        painel.add(Box.createVerticalStrut(18));
        painel.add(botaoCadastrar);
        painel.add(Box.createVerticalStrut(12));
        painel.add(labelResultado);

        return painel;
    }

    // Tela com o perfil formatado do professor atual
    private JPanel criarPainelPerfil() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(25, 25, 25, 25));

        areaPerfil = criarAreaTexto();

        painel.add(criarTituloTela("Meu perfil"), BorderLayout.NORTH);
        painel.add(new JScrollPane(areaPerfil), BorderLayout.CENTER);

        return painel;
    }

    // ---------- Telas do aluno ----------

    // Tela com a descrição de cada modalidade oferecida pela academia
    private JPanel criarPainelModalidades() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JTextArea area = criarAreaTexto();

        // Percorremos as 4 modalidades chamando getDescricao() de forma polimórfica
        Modalidade[] modalidades = {new Musculacao(), new Pilates(), new Crossfit(), new Natacao()};
        StringBuilder texto = new StringBuilder();

        for (Modalidade modalidade : modalidades) {
            texto.append(modalidade.getNome()).append("\n");
            texto.append("-> ").append(modalidade.getDescricao()).append("\n\n");
        }

        area.setText(texto.toString());

        painel.add(criarTituloTela("Modalidades da academia"), BorderLayout.NORTH);
        painel.add(new JScrollPane(area), BorderLayout.CENTER);

        return painel;
    }

    // Tela com todas as aulas cadastradas por todos os professores
    private JPanel criarPainelTodasAsAulas() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(25, 25, 25, 25));

        areaTodasAsAulas = criarAreaTexto();

        painel.add(criarTituloTela("Todas as aulas"), BorderLayout.NORTH);
        painel.add(new JScrollPane(areaTodasAsAulas), BorderLayout.CENTER);

        return painel;
    }

    // Tela com a lista de professores e a modalidade de cada um
    private JPanel criarPainelProfessores() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JTextArea area = criarAreaTexto();

        StringBuilder texto = new StringBuilder();

        for (Professor professor : professoresDisponiveis) {
            texto.append(professor.getNome())
                    .append(" (").append(professor.getIdade()).append(" anos) - ")
                    .append(professor.getEspecialidade().getNome())
                    .append("\n\n");
        }

        area.setText(texto.toString());

        painel.add(criarTituloTela("Professores"), BorderLayout.NORTH);
        painel.add(new JScrollPane(area), BorderLayout.CENTER);

        return painel;
    }

    // Tela para o ALUNO cadastrar uma aula: ele escolhe o professor/modalidade desejada,
    // e depois informa o dia e o horário. A aula é adicionada na lista do professor escolhido.
    private JPanel criarPainelAdicionarAulaAluno() {
        JPanel painel = new JPanel();
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(25, 25, 25, 25));
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.add(criarTituloTela("Adicionar aula"));
        painel.add(Box.createVerticalStrut(15));

        JLabel labelProfessor = new JLabel("Escolha o professor/modalidade:");
        labelProfessor.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] opcoesProfessor = {
                "Carlos - Musculação",
                "Marina - Pilates",
                "Rafael - Crossfit",
                "Juliana - Natação"
        };
        comboProfessorAluno = new JComboBox<>(opcoesProfessor);
        comboProfessorAluno.setMaximumSize(new Dimension(280, 30));
        comboProfessorAluno.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelDia = new JLabel("Dia da semana:");
        labelDia.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField campoDia = new JTextField();
        campoDia.setMaximumSize(new Dimension(280, 30));
        campoDia.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelHorario = new JLabel("Horário:");
        labelHorario.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField campoHorario = new JTextField();
        campoHorario.setMaximumSize(new Dimension(280, 30));
        campoHorario.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelResultado = new JLabel(" ");
        labelResultado.setForeground(COR_SUCESSO);
        labelResultado.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton botaoCadastrar = new JButton("Cadastrar aula");
        botaoCadastrar.setAlignmentX(Component.LEFT_ALIGNMENT);
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

            int indiceEscolhido = comboProfessorAluno.getSelectedIndex();
            Professor professorEscolhido = professoresDisponiveis[indiceEscolhido];

            try {
                AulaDAO.salvar(professorEscolhido.getNome(), dia, horario);
            } catch (SQLException ex) {
                labelResultado.setForeground(COR_SAIR);
                labelResultado.setText("Erro ao salvar no banco: " + ex.getMessage());
                return;
            }

            professorEscolhido.adicionarAula(dia, horario);
            campoDia.setText("");
            campoHorario.setText("");

            labelResultado.setForeground(COR_SUCESSO);
            labelResultado.setText("Aula cadastrada com " + professorEscolhido.getNome() + " com sucesso!");
        });

        painel.add(labelProfessor);
        painel.add(comboProfessorAluno);
        painel.add(Box.createVerticalStrut(12));
        painel.add(labelDia);
        painel.add(campoDia);
        painel.add(Box.createVerticalStrut(12));
        painel.add(labelHorario);
        painel.add(campoHorario);
        painel.add(Box.createVerticalStrut(18));
        painel.add(botaoCadastrar);
        painel.add(Box.createVerticalStrut(12));
        painel.add(labelResultado);

        return painel;
    }

    // Monta o texto com as aulas de todos os professores, usado na tela "Todas as aulas"
    private String montarTodasAsAulas() {
        StringBuilder texto = new StringBuilder();

        for (Professor professor : professoresDisponiveis) {
            texto.append("Professor: ").append(professor.getNome())
                    .append(" (").append(professor.getEspecialidade().getNome()).append(")\n");
            texto.append(professor.getAulasFormatadas());
            texto.append("\n\n");
        }

        return texto.toString();
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
                // O mesmo botão do cabeçalho leva a telas diferentes,
                // dependendo se quem está logado é Professor ou Aluno
                if (tipoUsuario.equals("Professor")) {
                    cardLayout.show(painelConteudo, "adicionarAula");
                } else {
                    cardLayout.show(painelConteudo, "adicionarAulaAluno");
                }
                break;

            case "Meu perfil":
                areaPerfil.setText(professorAtual.getPerfilFormatado());
                cardLayout.show(painelConteudo, "perfil");
                break;

            case "Modalidades":
                cardLayout.show(painelConteudo, "modalidades");
                break;

            case "Todas as aulas":
                areaTodasAsAulas.setText(montarTodasAsAulas());
                cardLayout.show(painelConteudo, "todasAulas");
                break;

            case "Professores":
                cardLayout.show(painelConteudo, "professores");
                break;

            case "Sair":
                System.exit(0);
                break;
        }
    }
}