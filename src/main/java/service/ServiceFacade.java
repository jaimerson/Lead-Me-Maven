package service;

//Facade para ser usada pela camada de controller (camada de apresenta��o em geral)

import excecoes.AutenticacaoException;
import excecoes.DataException;
import java.util.List;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.MatrizDisciplina;

public interface ServiceFacade {

    public Double coletarMediaAprovacao(Disciplina disciplina) throws DataException;
    public Aluno autenticar(String usuario, String senha) throws DataException, AutenticacaoException;
    public Aluno coletarAlunoLogado();
    public Disciplina carregarDisciplinaByCodigo(String codigo);
    public Disciplina carregarDisciplinaByToString(String toString);
    public String[] carregarDisciplinasDoCursoToString();
    public List<MatrizDisciplina> carregarDisciplinasDisponiveis();
    
}
