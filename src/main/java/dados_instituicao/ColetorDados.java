package dados_instituicao;

import excecoes.DataException;
import modelo.Aluno;
import modelo.Curso;

//Facade para coleta de dados da institui��o
public interface ColetorDados {

    public Curso getCurso(String nomeCurso) throws DataException;
    public Double getMediaAprovacao(String nomeCurso, String disciplina) throws DataException;
    public boolean existeUsuario(String usuario, String senha) throws DataException;
    public void carregarHistoricoAluno(Aluno aluno) throws DataException;
}
