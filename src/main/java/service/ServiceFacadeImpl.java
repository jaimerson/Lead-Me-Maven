package service;

import dados_instituicao.ColetorDadosFactory;
import excecoes.DataException;
import java.util.List;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.MatrizDisciplina;
import dados_instituicao.ColetorDadosFacade;

public class ServiceFacadeImpl implements ServiceFacade {

    private LoginService loginService;
    private CursoService cursoService;
    
    public ServiceFacadeImpl() {
        loginService = LoginService.getInstance();
        cursoService = CursoService.getInstance();
    }

    @Override
    public Double getMediaAprovacao(Disciplina disciplina) throws DataException{
        return cursoService.getMediaAprovacao(disciplina);
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

    @Override
    public List<MatrizDisciplina> getDisciplinasDisponiveis() {
        return cursoService.getDisciplinasDisponiveis(getAlunoLogado());
    }

}
