/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import modelo.Disciplina;

/**
 *
 * @author rafao
 */
public class DisciplinaService {
    
    public DisciplinaService(){
    }
    
    /**
     * Retorna a média de aprovações das turmas da disciplina. Serve para carregar o gráfico pizza com as aprovações.
     * @param disciplina disciplina que o usuário escolheu para ver as estatísticas
     * @return média de aprovações dessa disciplina
     */
    public Double coletarMediaAprovacao(Disciplina disciplina){
        return disciplina.coletarMediaAprovacao();
    }
}
