/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import base_dados.CursoDAO;
import excecoes.DataException;
import fabricas.Fabrica;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modelo.Aluno;
import modelo.Curso;
import modelo.Disciplina;
import modelo.MatrizCurricular;
import modelo.MatrizDisciplina;

public class CursoService {
    
    public static int NUMERO_DISCIPLINAS_DIFICEIS = 10;
    
    private CursoDAO cursoDAO;
    private RequisitosService requisitosService;
    private DisciplinaService disciplinaService;
    public CursoService(){
        cursoDAO = CursoDAO.getInstance();
        requisitosService = Fabrica.getInstance().getFactory().createRequisitosService();
        disciplinaService = new DisciplinaService();
    }
    
    
    public Curso carregarCurso(Integer id) throws DataException{
        Curso curso = cursoDAO.encontrar(id);
//        bibliotecaMineracao.gerarArquivoParaAssociarDisciplinas(curso);
        System.out.println("ENCONTROU O CURSO");
        return curso;
    }
    
    public List<Disciplina> coletarDisciplinasDoCurso(Curso curso){
        return curso.coletarDisciplinas();
    }
    
    /**
     * Retorna uma lista de disciplinas que podem ser pagas pelo aluno.
     * Leva em consideração os pré requisitos, co requisitos e equivalencias para dizer se o aluno pode ou não pagar a disciplina.
     * A lista é do tipo MatrizDisciplina por ter informações de natureza (obrigatória ou optativa) e semestre ideal (qual período pagar,  caso seja obrigatória)
     * @param aluno aluno interessado em receber as sugestões de disciplinas
     * @return Lista de disciplinas que pode pagar, ordenada pela relevância (obrigatório e dos primeiros períodos)
     */
    public List<MatrizDisciplina> coletarDisciplinasDisponiveis(Aluno aluno, MatrizCurricular matriz){
        Curso curso = aluno.getCurso();
        //Crio a lista com as disciplinas disponiveis
        List<MatrizDisciplina> disciplinasDisponiveis = new ArrayList<>();
        //TODO fornecer a lista de matrizes para aluno escolher qual fazer as simulacoes
        //E para cada disciplina da matriz, eu verifico se o aluno pode pagar
        List<MatrizDisciplina> disciplinasNaMatriz = matriz.getDisciplinasNaMatriz();
        for (MatrizDisciplina disciplinaNaMatriz: disciplinasNaMatriz){
            if (requisitosService.podePagar(aluno, disciplinaNaMatriz.getDisciplina())){
                disciplinasDisponiveis.add(disciplinaNaMatriz);
            }
        }
//        bibliotecaMineracao.associarDisciplinasComunsAPeriodoLetivo(disciplinasDisponiveis);
        //Ordena pela ordem de prioridade das disciplinas a serem pagas
        disciplinaService.ordenarDisciplinas(disciplinasDisponiveis);
        return disciplinasDisponiveis;
    }
    
    /**
     * Carrega a lista das disciplinas com menores índices de aprovação, considerando as turmas na base de dados.
     * Essa lista deve ser usada para carregar a tabela de disciplinas mais difíceis
     * @param curso
     * @return 
     */
    public List<Disciplina> coletarDisciplinasMaisDificeis(Curso curso){
        List<Disciplina> disciplinasDificeis = curso.coletarDisciplinas();
        ComparadorDisciplinaDificil comparador = new ComparadorDisciplinaDificil();
        //As mais dificeis primeiro
        Collections.sort(disciplinasDificeis,comparador);
        //Só interessa o número de disciplinas para a tabela
        if (disciplinasDificeis.size() > NUMERO_DISCIPLINAS_DIFICEIS){
            disciplinasDificeis = disciplinasDificeis.subList(0, NUMERO_DISCIPLINAS_DIFICEIS);
        }
        return disciplinasDificeis;
    }
}
