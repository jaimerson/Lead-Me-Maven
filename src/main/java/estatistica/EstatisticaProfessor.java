/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package estatistica;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rafael
 */
public class EstatisticaProfessor {
    private Integer idDocente;
    private Integer numeroAprovados;
    private Integer numeroReprovados;
    
    public EstatisticaProfessor(){
        numeroAprovados = 0;
        numeroReprovados = 0;
    }
    
    public Integer getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(Integer idDocente) {
        this.idDocente = idDocente;
    }

    public Integer getNumeroAprovados() {
        return numeroAprovados;
    }

    public void setNumeroAprovados(Integer numeroAprovados) {
        this.numeroAprovados = numeroAprovados;
    }

    public Integer getNumeroReprovados() {
        return numeroReprovados;
    }

    public void setNumeroReprovados(Integer numeroReprovados) {
        this.numeroReprovados = numeroReprovados;
    }
    
    public void incrementarNumeroAprovados(Integer numero){
        this.numeroAprovados += numero;
    }
    
    public void incrementarNumeroReprovados(Integer numero){
        this.numeroReprovados += numero;
    }
    
    public String coletarPorcentagem(){
        return String.format("%.2f", 100.0*(this.numeroAprovados.doubleValue() / (this.numeroAprovados + this.numeroReprovados)));
    }

    @Override
    public String toString() {
        return idDocente.toString();
    }
}
