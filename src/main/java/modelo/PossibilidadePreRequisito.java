/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rafao
 */
public class PossibilidadePreRequisito {
    
    private List<Disciplina> preRequisitos;
    
    public PossibilidadePreRequisito(){
        preRequisitos = new ArrayList<>();
    }

    public List<Disciplina> getPreRequisitos() {
        return preRequisitos;
    }

    public void setPreRequisitos(List<Disciplina> preRequisitos) {
        this.preRequisitos = preRequisitos;
    }
    
    public void adicionarPreRequisitoNaPossibilidade(Disciplina disciplina){
        preRequisitos.add(disciplina);
    }
}
