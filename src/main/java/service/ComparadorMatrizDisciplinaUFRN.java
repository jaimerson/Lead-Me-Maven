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
public class ComparadorMatrizDisciplinaUFRN extends ComparadorMatrizDisciplina {

    public ComparadorMatrizDisciplinaUFRN() {

    }

    @Override
    public int compare(MatrizDisciplina md1, MatrizDisciplina md2) {

        int comparacaoPeriodo = md1.getSemestreIdeal().compareTo(md2.getSemestreIdeal());
        if (comparacaoPeriodo != 0) {
            if (md1.getSemestreIdeal().equals(0)) {
                return 1;
            } else if (md2.getSemestreIdeal().equals(0)) {
                return -1;
            } else {
                return comparacaoPeriodo;
            }
        } else {
            //OBRIGATORIO < OPTATIVO, ainda bem, pois facilita a comparacao
            return md1.getNaturezaDisciplina().compareTo(md2.getNaturezaDisciplina());
        }
    }

}
