/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.Matricula;

/**
 *
 * @author rafao
 */
public class AlunoService {
    
    public Map<String,List<Disciplina>> coletarMatriculasAgrupadasPorPeriodoLetivo(Aluno aluno, boolean apenasAprovados){
        Map<String,List<Disciplina>> disciplinasAgrupadas = new HashMap<>();
        List<Disciplina> disciplinasDoPeriodo;
        String periodoLetivo;
        List<Matricula> matriculas = aluno.getMatriculas();
        for (Matricula matricula: matriculas){
            if (apenasAprovados && !matricula.situacaoAprovada()){
                continue;
            }
            periodoLetivo = matricula.getTurma().getPeriodoLetivo();
            if (!disciplinasAgrupadas.containsKey(periodoLetivo)){
                disciplinasAgrupadas.put(periodoLetivo, new ArrayList<Disciplina>());
            }
            disciplinasDoPeriodo = disciplinasAgrupadas.get(periodoLetivo);
            disciplinasDoPeriodo.add(matricula.getTurma().getDisciplina());
            disciplinasAgrupadas.put(periodoLetivo, disciplinasDoPeriodo);
        }
        return disciplinasAgrupadas;
    }
}
