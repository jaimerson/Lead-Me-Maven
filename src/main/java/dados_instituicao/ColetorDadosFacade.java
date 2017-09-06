package dados_instituicao;

import excecoes.DataException;
import modelo.Aluno;
import modelo.Curso;
import modelo.Disciplina;

//Facade para coleta de dados da institui��o
public interface ColetorDadosFacade {

    public Double getMediaAprovacao(Disciplina disciplina) throws DataException;
    public boolean existeUsuario(String usuario, String senha) throws DataException;
    public void carregarHistoricoAluno(Aluno aluno) throws DataException;
}
