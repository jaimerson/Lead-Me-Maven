/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package estatistica;

/**
 *
 * @author rafael
 */
public class EstatisticaAprovacoesSemestre {
    private String periodoLetivo;
    private Integer numeroAprovados;
    private Integer numeroReprovados;

    public EstatisticaAprovacoesSemestre(){
        this.numeroAprovados = 0;
        this.numeroReprovados = 0;
    }
    
    public String getPeriodoLetivo() {
        return periodoLetivo;
    }

    public void setPeriodoLetivo(String periodoLetivo) {
        this.periodoLetivo = periodoLetivo;
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
    
    public Integer getNumeroTotalDeMatriculas(){
        return this.numeroAprovados + this.numeroReprovados;
    }
            
}
