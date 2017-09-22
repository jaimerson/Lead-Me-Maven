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

    public Double coletarMediaAprovacao(Curso curso, Disciplina disciplina) throws DataException;
    public Aluno autenticar(String usuario, String senha) throws DataException, AutenticacaoException;
    public List<Disciplina> coletarDisciplinasMaisDificeis() throws DataException;
    public Aluno coletarAlunoLogado();
    public Disciplina carregarDisciplina(Curso curso, String toString);
    public String[] carregarDisciplinasDoCursoToString(Curso curso);
    public List<MatrizDisciplina> carregarDisciplinasDisponiveis(Curso curso);
    
}
