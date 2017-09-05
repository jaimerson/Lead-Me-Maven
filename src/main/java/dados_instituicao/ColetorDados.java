package dados_instituicao;

import excecoes.DataException;
import modelo.Curso;

//Facade para coleta de dados da institui��o
public interface ColetorDados {

    public Curso getCurso(String nomeCurso) throws DataException;
}
