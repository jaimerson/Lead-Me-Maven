/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.Comparator;
import modelo.Disciplina;

/**
 *
 * @author rafao
 */
public abstract class ComparadorDisciplinaDificil implements Comparator<Disciplina>{

    protected DisciplinaService disciplinaService;
    
    public ComparadorDisciplinaDificil(){
        this.disciplinaService = new DisciplinaService();
    }
    
    abstract public int compare(Disciplina d1, Disciplina d2);
    
}
