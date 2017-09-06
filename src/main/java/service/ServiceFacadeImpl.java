package service;

import dados_instituicao.ColetorDados;
import dados_instituicao.ColetorDadosFactory;
import excecoes.DataException;
import modelo.Aluno;
import modelo.Disciplina;

public class ServiceFacadeImpl implements ServiceFacade {

    private ColetorDados coletorDados;
    private LoginService loginService;
    private CursoService cursoService;
    
    public ServiceFacadeImpl() {
        coletorDados = ColetorDadosFactory.getInstance().getColetorInstance();
        loginService = LoginService.getInstance();
        cursoService = CursoService.getInstance();
    }

    @Override
    public Double getMediaAprovacao(Disciplina disciplina) throws DataException{
        String nomeCurso = disciplina.getCurso().getNome();
        return coletorDados.getMediaAprovacao(nomeCurso, disciplina.getCodigo());
    }

    @Override
    public Aluno autenticar(String usuario, String senha) throws DataException{
        return loginService.autenticar(usuario, senha);
    }

    @Override
    public Aluno getAlunoLogado() {
        return loginService.getAluno();
    }

    @Override
    public String[] getDisciplinasDoCursoToString() {
        return cursoService.getDisciplinasDoCursoToString();
    }

    @Override
    public Disciplina getDisciplinaByCodigo(String codigo) {
        return cursoService.getDisciplinaByCodigo(codigo);
    }

    @Override
    public Disciplina getDisciplina(String toString) {
        return cursoService.getDisciplina(toString);
    }

}
