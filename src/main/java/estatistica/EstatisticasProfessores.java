/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package estatistica;

import java.util.HashMap;
import java.util.Map;
import modelo.Turma;
import service.TurmaService;

/**
 *
 * @author rafael
 */
public class EstatisticasProfessores {
    private Map<String,EstatisticaProfessor> estatisticasProfessores;
    
    public EstatisticasProfessores(){
        estatisticasProfessores = new HashMap<>();
    }

    public Map<String, EstatisticaProfessor> getEstatisticasProfessores() {
        return estatisticasProfessores;
    }
    
    public void setEstatisticasProfessores(Map<String, EstatisticaProfessor> estatisticasProfessores) {
        this.estatisticasProfessores = estatisticasProfessores;
    }
    
    public EstatisticaProfessor coletarEstatisticaDoDocente(Integer idDocente){
        return this.estatisticasProfessores.get(idDocente);
    }
    
    public void adicionarEstatisticasDaTurma(Turma turma){
        EstatisticaProfessor mediaProfessor;
        if(estatisticasProfessores.containsKey(turma.getIdDocente().toString())){
            mediaProfessor = estatisticasProfessores.get(turma.getIdDocente().toString());
        }
        else{
            mediaProfessor = new EstatisticaProfessor();
            mediaProfessor.setIdDocente(turma.getIdDocente());
            estatisticasProfessores.put(turma.getIdDocente().toString(), mediaProfessor);
        }
        mediaProfessor.incrementarNumeroAprovados(turma.getNumeroAprovados());
        mediaProfessor.incrementarNumeroReprovados(turma.getNumeroReprovados());
    }
}
