package service;

import dados_instituicao.ColetorDados;
import dados_instituicao.ColetorDadosFactory;
import excecoes.DataException;
import modelo.Curso;

public class ServiceFacadeImpl implements ServiceFacade {

    private ColetorDados coletorDados;
    private Curso curso;

    public ServiceFacadeImpl() {
        coletorDados = ColetorDadosFactory.getInstance().getColetorInstance();
    }

    @Override
    public void carregarCurso(String nomeCurso) throws DataException {
        curso = coletorDados.getCurso(nomeCurso);
    }

    @Override
    public Curso getCurso() {
        return this.curso;
    }

    @Override
    public Double getMediaAprovacao(String nomeCurso, String disciplina) throws DataException{
        return coletorDados.getMediaAprovacao(nomeCurso, disciplina);
    }

}
