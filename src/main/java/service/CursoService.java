/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import modelo.Curso;
import modelo.Disciplina;

/**
 *
 * @author rafao
 */
public class CursoService {
    
    private static CursoService service = new CursoService();
    
    private Curso curso;
    private CursoService(){
        
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
    
    
}
