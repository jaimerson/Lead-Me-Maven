/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package estatistica;

import java.util.List;

/**
 *
 * @author rafael
 */
public class EstatisticaMediaProfessor {
    private Integer idDocente;
    private List<Double> mediaAprovacoes;

    public Integer getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(Integer idDocente) {
        this.idDocente = idDocente;
    }

    public List<Double> getMediaAprovacoes() {
        return mediaAprovacoes;
    }

    public void setMediaAprovacao(List<Double> mediaAprovacoes) {
        this.mediaAprovacoes = mediaAprovacoes;
    }
    
    public void incrementarMedia(Double media){
        this.mediaAprovacoes.add(media);
    }
    
    public Double coletarMediaFinalDoProfessor(){
        int qtdeMedias = this.mediaAprovacoes.size();
        Double somaMedias = 0.0;
        
        for (Double media: this.mediaAprovacoes){
            somaMedias += media;
        }
        return somaMedias / qtdeMedias;
    }

    @Override
    public String toString() {
        return idDocente.toString();
    }
    
    
}
