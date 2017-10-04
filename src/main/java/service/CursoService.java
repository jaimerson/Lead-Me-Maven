/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import base_dados.CursoDAO;
import base_dados.DisciplinaDAO;
import excecoes.DataException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Aluno;
import modelo.Curso;
import modelo.Disciplina;
import modelo.MatrizCurricular;
import modelo.MatrizDisciplina;
import modelo.Turma;

public class CursoService {
    
    public static int NUMERO_DISCIPLINAS_DIFICEIS = 10;
    
    private static CursoService service = new CursoService();
    private CursoDAO cursoDAO;
    private DisciplinaDAO disciplinaDAO;
    
    private CursoService(){
        cursoDAO = CursoDAO.getInstance();
        disciplinaDAO = DisciplinaDAO.getInstance();
    }
    
    public static CursoService getInstance(){
        return service;
    }
    
    public Curso carregarCurso(String nomeCurso){
        Curso curso = null;
        try{
            curso = cursoDAO.carregarCurso(nomeCurso);
        }catch(IOException e){
            System.out.println("Erro ao carregar o curso");
        }
        return curso;
    }
    
    public Disciplina[] carregarDisciplinasDoCurso(Curso curso){
        return curso.getDisciplinas();
    }
    
    public String[] carregarDisciplinasDoCursoToString(Curso curso){
        return curso.getDisciplinasToString();
    }
    
    public Disciplina carregarDisciplina(Curso curso, String disciplinaAutoComplete){
        for (Disciplina disciplina: carregarDisciplinasDoCurso(curso)){
            if (disciplina.toString().contains(disciplinaAutoComplete)){
                return disciplina;
            }
        }
        return null;
    }
    
    /**
     * Retorna a média de aprovações das turmas da disciplina. Serve para carregar o gráfico pizza com as aprovações.
     * @param disciplina disciplina que o usuário escolheu para ver as estatísticas
     * @return média de aprovações dessa disciplina
     */
    public Double coletarMediaAprovacao(Disciplina disciplina){
        return disciplina.coletarMediaAprovacao();
    }
    
    /**
     * Retorna a turma escolhida pelo usuário para ver estatísticas específicas para essa turma
     * @param disciplina disciplina escolhida na tela de estatísticas
     * @param periodoLetivo semestre da turma escolhido na tela de esstatísticas
     * @param numeroTurma também escolhido na tela de estatísticas. (ex: 2017.1-T01)
     * @return a instância da turma do período letivo e número da turma escolhidos
     */
    public Turma coletarTurma(Disciplina disciplina, String periodoLetivo){
        return disciplina.coletarTurma(periodoLetivo);
    }
    
    /**
     * Retorna uma lista de disciplinas que podem ser pagas pelo aluno.
     * Leva em consideração os pré requisitos, co requisitos e equivalencias para dizer se o aluno pode ou não pagar a disciplina.
     * A lista é do tipo MatrizDisciplina por ter informações de natureza (obrigatória ou optativa) e semestre ideal (qual período pagar,  caso seja obrigatória)
     * @param aluno aluno interessado em receber as sugestões de disciplinas
     * @return Lista de disciplinas que pode pagar, ordenada pela relevância (obrigatório e dos primeiros períodos)
     */
    public List<MatrizDisciplina> carregarDisciplinasDisponiveis(Aluno aluno){
        Curso curso = aluno.getCurso();
        //Crio a lista com as disciplinas disponiveis
        List<MatrizDisciplina> disciplinasDisponiveis = new ArrayList<>();
        //Consulto a grade do aluno para considerar as sugestoes
        MatrizCurricular matriz = curso.getMatrizesCurricular().get(aluno.getMatrizCurricular());
        //E para cada disciplina da matriz, eu verifico se o aluno pode pagar
        Map<String, MatrizDisciplina> disciplinasNaMatriz = matriz.getDisciplinasNaMatriz();
        Set<String> codigoDisciplinas = disciplinasNaMatriz.keySet();
        MatrizDisciplina disciplinaNaMatriz;
        for (String codigoDisciplina: codigoDisciplinas){
            disciplinaNaMatriz = disciplinasNaMatriz.get(codigoDisciplina);
            if (aluno.podePagar(disciplinaNaMatriz.getDisciplina())){
                disciplinasDisponiveis.add(disciplinaNaMatriz);
            }
        }
        //Ordena pela ordem de prioridade das disciplinas a serem pagas
        Collections.sort(disciplinasDisponiveis);
        return disciplinasDisponiveis;
    }
    
    /**
     * Carrega a lista das disciplinas com menores índices de aprovação, considerando as turmas na base de dados.
     * Essa lista deve ser usada para carregar a tabela de disciplinas mais difíceis
     * @param curso
     * @return 
     */
    public List<Disciplina> coletarDisciplinasMaisDificeis(Curso curso){
        Disciplina[] todasDisciplinas = curso.getDisciplinas();
        List<Disciplina> disciplinasDificeis = new ArrayList<>();
        disciplinasDificeis.addAll(Arrays.asList(todasDisciplinas));
        //As mais dificeis primeiro
        Collections.sort(disciplinasDificeis);
        //Só interessa o número de disciplinas para a tabela
        if (disciplinasDificeis.size() > NUMERO_DISCIPLINAS_DIFICEIS){
            disciplinasDificeis = disciplinasDificeis.subList(0, NUMERO_DISCIPLINAS_DIFICEIS);
        }
        return disciplinasDificeis;
    }
    
}
