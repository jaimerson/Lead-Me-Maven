/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import modelo.Aluno;
import modelo.Curso;
import modelo.Disciplina;
import modelo.Matricula;
import modelo.MatrizDisciplina;

/**
 *
 * @author rafao
 */
public class SimulacaoService {
    private static SimulacaoService instance = new SimulacaoService();
    
    private SimulacaoService(){
        
    }
    
    public static SimulacaoService getInstance(){
        return instance;
    }
            
    /**
     * Função responsável em retornar o peso de uma disciplina em um semestre.
     * A lógica usada leva em consideração a carga horária da disciplina e a média de reprovações
     * @param disciplina
     * @return peso da disciplina no semestre
     */
    public Double coletarPesoDisciplina(Disciplina disciplina){
        return disciplina.getCargaHoraria()*(1 + disciplina.coletarMediaReprovacao()/100);
    }
    
    public Double coletarPesoSemestre(List<Disciplina> disciplinas){
        Double pesoSemestre = 0.0;
        for (Disciplina disciplina: disciplinas){
            pesoSemestre += coletarPesoDisciplina(disciplina);
        }
        return pesoSemestre;
    }
    
    public Double coletarPesoDoSemestre(List<MatrizDisciplina> disciplinas){
        List<Disciplina> disciplinasParaCalculo = new ArrayList<>();
        for(MatrizDisciplina matrizDisciplina: disciplinas){
            disciplinasParaCalculo.add(matrizDisciplina.getDisciplina());
        }
        return coletarPesoSemestre(disciplinasParaCalculo);
    }
    
    public Double coletarPesoAproveitado(Aluno aluno, String periodoLetivo){
        Double pesoAproveitado = 0.0;
        List<Matricula> matriculasDoPeriodo = aluno.coletarMatriculasDoPeriodo(periodoLetivo);
        for (Matricula matricula: matriculasDoPeriodo){
            if (matricula.foiAprovado()){
                pesoAproveitado += coletarPesoDisciplina(matricula.getTurma().getDisciplina());
            }
        }
        return pesoAproveitado;
    }
    
    
    public Double coletarPesoMedioSuportado(Curso curso){
        int qtdeAlunosParaMedia = 0;
        Double pesoAcumulado = 0.0;
        
        List<Aluno> alunos = curso.getAlunos();
        for (Aluno aluno: alunos){
            List<Matricula> matriculas = aluno.getMatriculas();
            if (matriculas.isEmpty()){
                continue;
            }
            //Aqui eu espero facilmente coletar no máximo os dois ultimos semestres do aluno
            Map<String, List<Disciplina>> matriculasAgrupadasPorPeriodoLetivo = aluno.coletarMatriculasAgrupadasPorPeriodoLetivo(true);
            
        }
        return pesoAcumulado / qtdeAlunosParaMedia;
    }
    
    //Podemos considerar a média, ou o ultimo semestre, ou os dois ultimos, 
    //ou uma proporcao do peso do aluno com a media dos alunos
    public Double coletarPesoSuportado(Aluno aluno){
        List<Matricula> matriculas = aluno.getMatriculas();
        //Se tiver vazia, eh pq eh um aluno ingressante, entao devemos calcular a media dos outros
        if (matriculas.isEmpty()){
            
        }
        return 0.0;
    }
}
