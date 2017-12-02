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
    private Map<Integer,EstatisticaMediaProfessor> estatisticasProfessores;
    
    public EstatisticasProfessores(){
        estatisticasProfessores = new HashMap<>();
    }

    public Map<Integer, EstatisticaMediaProfessor> getEstatisticasProfessores() {
        return estatisticasProfessores;
    }
    
    public void setEstatisticasProfessores(Map<Integer, EstatisticaMediaProfessor> estatisticasProfessores) {
        this.estatisticasProfessores = estatisticasProfessores;
    }
    
    public EstatisticaMediaProfessor coletarEstatisticaDoDocente(Integer idDocente){
        return this.estatisticasProfessores.get(idDocente);
    }
    
    public void adicionarEstatisticasDaTurma(Turma turma){
        TurmaService turmaService = new TurmaService();
        EstatisticaMediaProfessor mediaProfessor;
        if(estatisticasProfessores.containsKey(turma.getIdDocente())){
            mediaProfessor = estatisticasProfessores.get(turma.getIdDocente());
        }
        else{
            mediaProfessor = new EstatisticaMediaProfessor();
            mediaProfessor.setIdDocente(turma.getIdDocente());
            estatisticasProfessores.put(turma.getIdDocente(), mediaProfessor);
        }
        mediaProfessor.incrementarMedia(turmaService.coletarMediaAprovacao(turma));
    }
}
