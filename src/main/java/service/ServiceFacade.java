package service;

//Facade para ser usada pela camada de controller (camada de apresenta��o em geral)

import excecoes.DataException;
import modelo.Aluno;

public interface ServiceFacade {

    public Double getMediaAprovacao(String nomeCurso, String disciplina) throws DataException;
    public Aluno autenticar(String usuario, String senha) throws DataException;
    public Aluno getAlunoLogado();
    
}
