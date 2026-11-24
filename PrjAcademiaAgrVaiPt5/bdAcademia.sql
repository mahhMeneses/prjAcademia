-- =====================================================
-- Banco da Academia (bdAcademiaJv)
-- ATENCAO: o DROP DATABASE apaga o banco antigo, se existir
-- =====================================================

DROP DATABASE IF EXISTS bdAcademiaJv;
CREATE DATABASE bdAcademiaJv CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bdAcademiaJv;

-- ---------- TABELAS ----------

CREATE TABLE tbAluno (
    idAluno INT AUTO_INCREMENT PRIMARY KEY,
    nomeAluno VARCHAR(60) NOT NULL,
    dataNasc DATE,
    cpf VARCHAR(14),
    histMed VARCHAR(500),
    cep VARCHAR(9),
    sexo VARCHAR(1),
    email VARCHAR(60)
);

CREATE TABLE tbProfessor (
    idProfessor INT AUTO_INCREMENT PRIMARY KEY,
    nomeProfessor VARCHAR(60) NOT NULL,
    dataNasc DATE,
    cpf VARCHAR(14),
    cep VARCHAR(9),
    salario DECIMAL(10,2),
    email VARCHAR(60)
);

CREATE TABLE tbFormacao (
    idFormacao INT AUTO_INCREMENT PRIMARY KEY,
    descr VARCHAR(100) NOT NULL
);

CREATE TABLE tbModalidade (
    idModalidade INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(60) NOT NULL,
    descr VARCHAR(100)
);

CREATE TABLE tbFoneAluno (
    idFoneAluno INT AUTO_INCREMENT PRIMARY KEY,
    idAluno INT NOT NULL,
    descr VARCHAR(15),
    FOREIGN KEY (idAluno) REFERENCES tbAluno(idAluno) ON DELETE CASCADE
);

CREATE TABLE tbFoneProfessor (
    idFoneProfessor INT AUTO_INCREMENT PRIMARY KEY,
    idProfessor INT NOT NULL,
    descr VARCHAR(15),
    FOREIGN KEY (idProfessor) REFERENCES tbProfessor(idProfessor) ON DELETE CASCADE
);

CREATE TABLE tbAula (
    idAula INT AUTO_INCREMENT PRIMARY KEY,
    idModalidade INT NOT NULL,
    idProfessor INT NOT NULL,
    idAluno INT NOT NULL,
    dataAula DATE NOT NULL,
    horaAula TIME NOT NULL,
    FOREIGN KEY (idModalidade) REFERENCES tbModalidade(idModalidade),
    FOREIGN KEY (idProfessor) REFERENCES tbProfessor(idProfessor),
    FOREIGN KEY (idAluno) REFERENCES tbAluno(idAluno)
);

CREATE TABLE tbProfessorFormacao (
    idProfessorFormacao INT AUTO_INCREMENT PRIMARY KEY,
    idProfessor INT NOT NULL,
    idFormacao INT NOT NULL,
    FOREIGN KEY (idProfessor) REFERENCES tbProfessor(idProfessor) ON DELETE CASCADE,
    FOREIGN KEY (idFormacao) REFERENCES tbFormacao(idFormacao)
);

-- ---------- DADOS INICIAIS ----------

INSERT INTO tbProfessor (nomeProfessor, dataNasc, cpf, cep, salario, email) VALUES
('Carlos',  '1990-03-15', '111.111.111-11', '13930-000', 3500.00, 'carlos@academia.com'),
('Marina',  '1988-07-22', '222.222.222-22', '13930-001', 3800.00, 'marina@academia.com'),
('Juliana', '1993-11-05', '333.333.333-33', '13930-002', 3200.00, 'juliana@academia.com'),
('Rafael',  '1985-01-30', '444.444.444-44', '13930-003', 4000.00, 'rafael@academia.com');

INSERT INTO tbAluno (nomeAluno, dataNasc, cpf, histMed, cep, sexo, email) VALUES
('Anthony', '2000-05-10', '555.555.555-55', 'Nenhum problema de saúde', '13930-010', 'M', 'anthony.aluno@email.com'),
('Maria',   '1998-09-18', '666.666.666-66', 'Asma leve',                '13930-011', 'F', 'maria.aluna@email.com'),
('Mariana', '2002-12-25', '777.777.777-77', 'Nenhum problema de saúde', '13930-012', 'F', 'mariana.aluna@email.com'),
('Fabio',   '1995-04-02', '888.888.888-88', 'Lesão antiga no joelho',   '13930-013', 'M', 'fabio.aluno@email.com');

INSERT INTO tbModalidade (nome, descr) VALUES
('Spinning',   'Aula de ciclismo indoor'),
('Musculação', 'Treino com pesos'),
('Yoga',       'Alongamento e respiração'),
('Funcional',  'Treino funcional em circuito');

INSERT INTO tbFormacao (descr) VALUES
('Educação Física - Bacharelado'),
('Educação Física - Licenciatura'),
('Especialização em Musculação'),
('Instrutor de Yoga certificado');

INSERT INTO tbProfessorFormacao (idProfessor, idFormacao) VALUES
(1, 1), (1, 3),
(2, 4),
(3, 2),
(4, 1);

INSERT INTO tbFoneAluno (idAluno, descr) VALUES
(1, '19999990001'),
(2, '19999990002'),
(3, '19999990003'),
(4, '19999990004');

INSERT INTO tbFoneProfessor (idProfessor, descr) VALUES
(1, '19988880001'),
(2, '19988880002'),
(3, '19988880003'),
(4, '19988880004');

INSERT INTO tbAula (idModalidade, idProfessor, idAluno, dataAula, horaAula) VALUES
(1, 1, 1, '2026-10-05', '18:30:00'),
(3, 2, 2, '2026-10-06', '07:00:00'),
(2, 4, 4, '2026-10-07', '19:00:00');

-- ---------- CONFERENCIA ----------
SHOW TABLES;

SELECT a.idAula, m.nome AS modalidade, p.nomeProfessor, al.nomeAluno, a.dataAula, a.horaAula
FROM tbAula a
JOIN tbModalidade m ON m.idModalidade = a.idModalidade
JOIN tbProfessor p  ON p.idProfessor  = a.idProfessor
JOIN tbAluno al     ON al.idAluno     = a.idAluno;