package service;

//Facade para ser usada pela camada de controller (camada de apresenta��o em geral)

import excecoes.DataException;
import modelo.Curso;

public interface ServiceFacade {

    public void carregarCurso(String nomeCurso) throws DataException;
    public Curso getCurso();
    
}
