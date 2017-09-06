/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dados_instituicao.ColetorDadosFacade;
import dados_instituicao.ColetorDadosFactory;
import excecoes.DataException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import modelo.Aluno;
import modelo.Curso;
import modelo.Disciplina;
import modelo.MatrizCurricular;
import modelo.MatrizDisciplina;

/**
 *
 * @author rafao
 */
public class CursoService {
    
    private static CursoService service = new CursoService();
    private ColetorDadosFacade coletor;
    
    private Curso curso;
    private CursoService(){
        coletor = ColetorDadosFactory.getInstance().getColetorInstance();
    }
    
    public static CursoService getInstance(){
        return service;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    
    public Disciplina[] getDisciplinasDoCurso(){
        return curso.getDisciplinas();
    }
    
    public String[] getDisciplinasDoCursoToString(){
        return curso.getDisciplinasToString();
    }
    
    public Disciplina getDisciplina(String disciplinaAutoComplete){
        for (Disciplina disciplina: getDisciplinasDoCurso()){
            if (disciplina.toString().equals(disciplinaAutoComplete)){
                return disciplina;
            }
        }
        return null;
    }
    
    public Disciplina getDisciplinaByCodigo(String codigo){
        Disciplina[] disciplinas = getDisciplinasDoCurso();
        for (Disciplina disciplina: disciplinas){
            if (disciplina.getCodigo().equals(codigo)){
                return disciplina;
            }
        }
        return null;
    }
    
    public Double getMediaAprovacao(Disciplina disciplina) throws DataException {
        return coletor.getMediaAprovacao(disciplina);
    }
    
    public List<MatrizDisciplina> getDisciplinasDisponiveis(Aluno aluno){
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
            if (aluno.podePagar(disciplinaNaMatriz)){
                disciplinasDisponiveis.add(disciplinaNaMatriz);
            }
        }
        //Ordena pela ordem de prioridade das disciplinas a serem pagas
        Collections.sort(disciplinasDisponiveis);
        return disciplinasDisponiveis;
    }
    
}
