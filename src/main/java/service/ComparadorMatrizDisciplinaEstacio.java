/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import modelo.MatrizDisciplina;

/**
 *
 * @author rafao
 */
public class ComparadorMatrizDisciplinaEstacio extends ComparadorMatrizDisciplina{

    //Todas as obrigatorias e depois todas as optativas
    @Override
    public int compare(MatrizDisciplina md1, MatrizDisciplina md2) {
        int comparacaoNatureza = md1.getNaturezaDisciplina().compareTo(md2.getNaturezaDisciplina());
        if (comparacaoNatureza != 0){
            return comparacaoNatureza;
        }
        else{
            return md1.getSemestreIdeal().compareTo(md2.getSemestreIdeal());
        }
    }
    
}
