package service;

//Facade para ser usada pela camada de controller (camada de apresenta��o em geral)

import excecoes.AutenticacaoException;
import excecoes.DataException;
import java.util.List;
import modelo.Aluno;
import modelo.Curso;
import modelo.Disciplina;
import modelo.MatrizDisciplina;
import modelo.Turma;

public interface ServiceFacade {
    public Aluno autenticar(String usuario, String senha) throws DataException, AutenticacaoException;
    public Aluno coletarAlunoLogado();
    public String[] carregarDisciplinasDoCursoToString(Curso curso);
    public Disciplina carregarDisciplina(Curso curso, String toString);
    public Double coletarMediaAprovacao(Curso curso, Disciplina disciplina) throws DataException;
    public List<MatrizDisciplina> carregarDisciplinasDisponiveis(Curso curso); 
    public List<Disciplina> coletarDisciplinasMaisDificeis(Curso curso) throws DataException;
    public Turma coletarTurma(Disciplina disciplina, String periodoLetivo);
    public void carregarPesoMaximoParaAluno(Aluno aluno);
    public String coletarRecomendacaoSemestre(List<MatrizDisciplina> disciplinas);
    
}
