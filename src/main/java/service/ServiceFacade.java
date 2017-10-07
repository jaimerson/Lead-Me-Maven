package service;

//Facade para ser usada pela camada de controller (camada de apresenta��o em geral)

import excecoes.AutenticacaoException;
import excecoes.DataException;
import java.util.List;
import modelo.Aluno;
import modelo.Curso;
import modelo.Disciplina;
import modelo.MatrizDisciplina;

public interface ServiceFacade {
    public Aluno autenticar(String usuario, String senha) throws DataException, AutenticacaoException;
    public Aluno coletarAlunoLogado();
    public List<Disciplina> carregarDisciplinasDoCurso(Curso curso);
    public Double coletarMediaAprovacao(Curso curso, Disciplina disciplina) throws DataException;
    public List<MatrizDisciplina> carregarDisciplinasDisponiveis(Curso curso); 
    public List<Disciplina> coletarDisciplinasMaisDificeis(Curso curso) throws DataException;
    public void carregarPesoMaximoParaAluno(Aluno aluno);
    public String coletarRecomendacaoSemestre(List<MatrizDisciplina> disciplinas);
    
}
