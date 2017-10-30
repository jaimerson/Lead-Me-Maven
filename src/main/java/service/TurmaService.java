/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import modelo.Matricula;
import modelo.Turma;

/**
 *
 * @author rafao
 */
public class TurmaService {
    
    private MatriculaService matriculaService;
    
    public TurmaService(){
        matriculaService = new MatriculaServiceUFRN();
    }
    public Double coletarMediaAprovacao(Turma turma){
        Double aprovados = 0.0;
        List<Matricula> matriculas = turma.getMatriculas();
        for(Matricula matricula: matriculas){
            if (matriculaService.situacaoAprovada(matricula)){
                aprovados += 1;
            }
        }
        return 100*(aprovados/matriculas.size());
    }
}
