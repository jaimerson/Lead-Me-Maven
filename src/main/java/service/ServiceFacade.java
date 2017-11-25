package service;

//Facade para ser usada pela camada de controller (camada de apresenta��o em geral)

import excecoes.AutenticacaoException;
import excecoes.DataException;
import java.util.List;
import modelo.Aluno;
import modelo.Curso;
import modelo.Disciplina;
import modelo.MatrizCurricular;
import modelo.MatrizDisciplina;

public interface ServiceFacade {
    public Aluno autenticar(String usuario, String senha) throws DataException, AutenticacaoException;
    public Aluno coletarAlunoLogado();
    public List<Disciplina> coletarDisciplinasDoCurso(Curso curso);
    public Double coletarMediaAprovacao(Disciplina disciplina);
    public List<MatrizDisciplina> carregarDisciplinasDisponiveis(Curso curso, MatrizCurricular matriz); 
    public List<Disciplina> coletarDisciplinasMaisDificeis(Curso curso);
    public String coletarRecomendacaoSemestre(Aluno aluno, List<MatrizDisciplina> disciplinas);
    public boolean cumpreCoRequisitos(Aluno aluno, MatrizDisciplina disciplinaAAdicionar, List<MatrizDisciplina> disciplinasM);
    public void ordenarDisciplinas(List<MatrizDisciplina> disciplinas);
    public void atualizarBaseDeDados();
}
