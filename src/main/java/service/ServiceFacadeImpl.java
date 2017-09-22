package service;

import excecoes.DataException;
import java.util.List;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.MatrizDisciplina;
import excecoes.AutenticacaoException;
import modelo.Curso;

public class ServiceFacadeImpl implements ServiceFacade {

    private LoginService loginService;
    private CursoService cursoService;
    
    public ServiceFacadeImpl() {
        loginService = LoginService.getInstance();
        cursoService = CursoService.getInstance();
    }

    @Override
    public Double coletarMediaAprovacao(Curso curso, Disciplina disciplina) throws DataException{
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
    public String[] carregarDisciplinasDoCursoToString(Curso curso) {
        return cursoService.carregarDisciplinasDoCursoToString(curso);
    }

    @Override
    public Disciplina carregarDisciplina(Curso curso, String disciplina) {
        return cursoService.carregarDisciplina(curso,disciplina);
    }

    @Override
    public List<MatrizDisciplina> carregarDisciplinasDisponiveis(Curso curso) {
        return cursoService.carregarDisciplinasDisponiveis(coletarAlunoLogado());
    }

    @Override
    public List<Disciplina> coletarDisciplinasMaisDificeis() throws DataException {
        return cursoService.coletarDisciplinasMaisDificeis();
    }

}
