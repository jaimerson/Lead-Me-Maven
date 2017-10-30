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
    private DisciplinaService disciplinaService;
    private SimulacaoService simulacaoService;
    private RequisitosService requisitosService;
    
    public ServiceFacadeImpl() {
        loginService = LoginService.getInstance();
        cursoService = new CursoService();
        disciplinaService = new DisciplinaService();
        simulacaoService = SimulacaoService.getInstance();
        requisitosService = new RequisitosServiceUFRN();
    }

    @Override
    public Double coletarMediaAprovacao(Disciplina disciplina){
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
    public List<Disciplina> coletarDisciplinasMaisDificeis(Curso curso){
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

    @Override
    public boolean cumpreCoRequisitos(Aluno aluno, MatrizDisciplina disciplinaAAdicionar, List<MatrizDisciplina> disciplinasM) {
        return requisitosService.cumpreCoRequisitos(aluno, disciplinaAAdicionar, disciplinasM);
    }

    @Override
    public void ordenarDisciplinas(List<MatrizDisciplina> disciplinas) {
        disciplinaService.ordenarDisciplinas(disciplinas);
    }
}
