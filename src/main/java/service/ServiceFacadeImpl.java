package service;

import excecoes.DataException;
import java.util.List;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.MatrizDisciplina;
import excecoes.AutenticacaoException;

public class ServiceFacadeImpl implements ServiceFacade {

    private LoginService loginService;
    private CursoService cursoService;
    
    public ServiceFacadeImpl() {
        loginService = LoginService.getInstance();
        cursoService = CursoService.getInstance();
    }

    @Override
    public Double coletarMediaAprovacao(Disciplina disciplina) throws DataException{
        return cursoService.coletarMediaAprovacao(disciplina);
    }

    @Override
    public Aluno autenticar(String usuario, String senha) throws DataException, AutenticacaoException{
        return loginService.autenticar(usuario, senha);
    }

    @Override
    public Aluno coletarAlunoLogado() {
        return loginService.getAluno();
    }

    @Override
    public String[] carregarDisciplinasDoCursoToString() {
        return cursoService.carregarDisciplinasDoCursoToString();
    }

    @Override
    public Disciplina carregarDisciplinaByCodigo(String codigo) {
        return cursoService.carregarDisciplinaByCodigo(codigo);
    }

    @Override
    public Disciplina carregarDisciplinaByToString(String toString) {
        return cursoService.carregarDisciplina(toString);
    }

    @Override
    public List<MatrizDisciplina> carregarDisciplinasDisponiveis() {
        return cursoService.carregarDisciplinasDisponiveis(coletarAlunoLogado());
    }

}
