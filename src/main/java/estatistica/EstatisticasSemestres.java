/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package estatistica;

import java.util.HashMap;
import java.util.Map;
import modelo.Turma;

/**
 *
 * @author rafael
 */
public class EstatisticasSemestres {
    
    private Map<String,EstatisticaAprovacoesSemestre> estatisticasSemestres;
    
    public EstatisticasSemestres(){
        estatisticasSemestres = new HashMap<>();
    }

    public Map<String, EstatisticaAprovacoesSemestre> getEstatisticasSemestres() {
        return estatisticasSemestres;
    }

    public void setEstatisticasSemestres(Map<String, EstatisticaAprovacoesSemestre> estatisticasSemestres) {
        this.estatisticasSemestres = estatisticasSemestres;
    }
    
    public void adicionarEstatisticasDaTurma(Turma turma){
        EstatisticaAprovacoesSemestre estatistica;
        if(estatisticasSemestres.containsKey(turma.getPeriodoLetivo())){
            estatistica = estatisticasSemestres.get(turma.getPeriodoLetivo());
        }
        else{
            estatistica = new EstatisticaAprovacoesSemestre();
            estatistica.setPeriodoLetivo(turma.getPeriodoLetivo());
            estatisticasSemestres.put(turma.getPeriodoLetivo(), estatistica);
        }
        estatistica.incrementarNumeroAprovados(turma.getNumeroAprovados());
        estatistica.incrementarNumeroReprovados(turma.getNumeroReprovados());
    }
}
