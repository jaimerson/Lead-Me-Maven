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
    private SimulacaoService simulacaoService;
    
    public ServiceFacadeImpl() {
        loginService = LoginService.getInstance();
        cursoService = CursoService.getInstance();
        simulacaoService = SimulacaoService.getInstance();
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
    public List<Disciplina> coletarDisciplinasMaisDificeis(Curso curso) throws DataException {
        return cursoService.coletarDisciplinasMaisDificeis(curso);
    }
    
    @Override
    public Turma coletarTurma(Disciplina disciplina, String periodoLetivo){
        return cursoService.coletarTurma(disciplina, periodoLetivo);
    }

    @Override
    public void carregarPesoMaximoParaAluno(Aluno aluno) {
        simulacaoService.carregarPesoMaximoParaAluno(aluno);
    }

    @Override
    public String coletarRecomendacaoSemestre(List<MatrizDisciplina> disciplinas) {
        return simulacaoService.coletarRecomendacaoSemestre(disciplinas);
    }

}
