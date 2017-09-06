package service;

//Facade para ser usada pela camada de controller (camada de apresenta��o em geral)

import excecoes.DataException;
import modelo.Aluno;
import modelo.Disciplina;

public interface ServiceFacade {

    public Double getMediaAprovacao(Disciplina disciplina) throws DataException;
    public Aluno autenticar(String usuario, String senha) throws DataException;
    public Aluno getAlunoLogado();
    public Disciplina getDisciplinaByCodigo(String codigo);
    public Disciplina getDisciplina(String toString);
    public String[] getDisciplinasDoCursoToString();
    
}
