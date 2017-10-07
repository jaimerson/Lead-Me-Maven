/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import base_dados.CursoDAO;
import excecoes.DataException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import minerador.BibliotecaMineracao;
import minerador.BibliotecaMineracaoFactory;
import modelo.Aluno;
import modelo.Curso;
import modelo.Disciplina;
import modelo.MatrizCurricular;
import modelo.MatrizDisciplina;

public class CursoService {
    
    public static int NUMERO_DISCIPLINAS_DIFICEIS = 10;
    
    private CursoDAO cursoDAO;
    private BibliotecaMineracao bibliotecaMineracao;
    
    public CursoService(){
        cursoDAO = CursoDAO.getInstance();
        bibliotecaMineracao = BibliotecaMineracaoFactory.getInstance().getBibliotecaMineracaoInstance();
    }
    
    
    public Curso carregarCurso(String nomeCurso) throws DataException{
        Curso curso = cursoDAO.carregarCurso(nomeCurso);
        bibliotecaMineracao.gerarArquivoParaAssociarDisciplinas(curso);
        return curso;
    }
    
    public List<Disciplina> carregarDisciplinasDoCurso(Curso curso){
        return curso.getDisciplinas();
    }
    
    /**
     * Retorna uma lista de disciplinas que podem ser pagas pelo aluno.
     * Leva em consideração os pré requisitos, co requisitos e equivalencias para dizer se o aluno pode ou não pagar a disciplina.
     * A lista é do tipo MatrizDisciplina por ter informações de natureza (obrigatória ou optativa) e semestre ideal (qual período pagar,  caso seja obrigatória)
     * @param aluno aluno interessado em receber as sugestões de disciplinas
     * @return Lista de disciplinas que pode pagar, ordenada pela relevância (obrigatório e dos primeiros períodos)
     */
    public List<MatrizDisciplina> coletarDisciplinasDisponiveis(Aluno aluno){
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
        List<Disciplina> disciplinasDificeis = curso.getDisciplinas();
        //As mais dificeis primeiro
        Collections.sort(disciplinasDificeis);
        //Só interessa o número de disciplinas para a tabela
        if (disciplinasDificeis.size() > NUMERO_DISCIPLINAS_DIFICEIS){
            disciplinasDificeis = disciplinasDificeis.subList(0, NUMERO_DISCIPLINAS_DIFICEIS);
        }
        return disciplinasDificeis;
    }
}
