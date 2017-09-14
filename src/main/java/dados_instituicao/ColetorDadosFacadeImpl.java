package dados_instituicao;

import excecoes.DataException;
import modelo.Aluno;
import modelo.Disciplina;

public class ColetorDadosFacadeImpl implements ColetorDadosFacade {

    public static final String DIRETORIO_RECURSOS = "src/main/resources/";
    
    private ColetorDados coletor;
    
    public ColetorDadosFacadeImpl() {
        coletor = new ColetorDados();
    }
    
    @Override
    public Double getMediaAprovacao(Disciplina disciplina) throws DataException {
        return coletor.getMediaAprovacao(disciplina);
    }

    @Override
    public boolean existeUsuario(String usuario, String senha) throws DataException {
        return coletor.existeUsuario(usuario, senha);
    }

    @Override
    public void carregarHistoricoAluno(Aluno aluno) throws DataException {
        coletor.carregarHistoricoAluno(aluno);
    }
}
