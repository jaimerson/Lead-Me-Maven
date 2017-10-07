package service;

import excecoes.DataException;
import java.util.List;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.MatrizDisciplina;
import excecoes.AutenticacaoException;
import modelo.Curso;
import modelo.Turma;

public class ServiceFacadeImpl implements ServiceFacade {

    private LoginService loginService;
    private CursoService cursoService;
    private DisciplinaService disciplinaService;
    private SimulacaoService simulacaoService;
    
    public ServiceFacadeImpl() {
        loginService = LoginService.getInstance();
        cursoService = new CursoService();
        disciplinaService = new DisciplinaService();
        simulacaoService = SimulacaoService.getInstance();
    }

    @Override
    public Double coletarMediaAprovacao(Curso curso, Disciplina disciplina) throws DataException{
        return disciplinaService.coletarMediaAprovacao(disciplina);
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
    public List<MatrizDisciplina> carregarDisciplinasDisponiveis(Curso curso) {
        return cursoService.coletarDisciplinasDisponiveis(coletarAlunoLogado());
    }

    @Override
    public List<Disciplina> coletarDisciplinasMaisDificeis(Curso curso) throws DataException {
        return cursoService.coletarDisciplinasMaisDificeis(curso);
    }
    
    @Override
    public void carregarPesoMaximoParaAluno(Aluno aluno) {
        simulacaoService.carregarPesoMaximoParaAluno(aluno);
    }

    @Override
    public String coletarRecomendacaoSemestre(List<MatrizDisciplina> disciplinas) {
        return simulacaoService.coletarRecomendacaoSemestre(disciplinas);
    }

    @Override
    public List<Disciplina> carregarDisciplinasDoCurso(Curso curso) {
        return cursoService.carregarDisciplinasDoCurso(curso);
    }
}
