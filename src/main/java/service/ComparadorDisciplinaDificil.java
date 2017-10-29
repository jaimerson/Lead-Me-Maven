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
public class ComparadorDisciplinaDificil implements Comparator<Disciplina>{

    private DisciplinaService disciplinaService;
    
    public ComparadorDisciplinaDificil(){
        this.disciplinaService = new DisciplinaService();
    }
    
    @Override
    public int compare(Disciplina d1, Disciplina d2) {
        Double mediaAprovacoes = disciplinaService.coletarMediaAprovacao(d1);
        Double mediaAprovacoesOther = disciplinaService.coletarMediaAprovacao(d2);
        return mediaAprovacoes.compareTo(mediaAprovacoesOther);
    }
    
}
