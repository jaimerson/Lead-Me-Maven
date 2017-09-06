package service;

import dados_instituicao.ColetorDados;
import dados_instituicao.ColetorDadosFactory;
import excecoes.DataException;
import modelo.Aluno;

public class ServiceFacadeImpl implements ServiceFacade {

    private ColetorDados coletorDados;
    private LoginService loginService;

    public ServiceFacadeImpl() {
        coletorDados = ColetorDadosFactory.getInstance().getColetorInstance();
        loginService = LoginService.getInstance();
    }

    @Override
    public Double getMediaAprovacao(String nomeCurso, String disciplina) throws DataException{
        return coletorDados.getMediaAprovacao(nomeCurso, disciplina);
    }

    @Override
    public Aluno autenticar(String usuario, String senha) throws DataException{
        return loginService.autenticar(usuario, senha);
    }

    @Override
    public Aluno getAlunoLogado() {
        return loginService.getAluno();
    }

}
