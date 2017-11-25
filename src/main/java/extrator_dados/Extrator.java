/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extrator_dados;

/**
 *
 * @author rafao
 */
public abstract class Extrator {

    abstract public void atualizarListaDeCursos();
    abstract public void atualizarListaDeMatrizesCurriculares();
    abstract public void atualizarListaDeDisciplinas();
    abstract public void atualizarListaDeTurmas();
    abstract public void atualizarListaDeAlunos();
    abstract public void atualizarListaDeComponentesDasMatrizes();
    abstract public void atualizarListaDeMatriculas();
    
    public void atualizarBaseDeDados(){
        atualizarListaDeCursos();
        atualizarListaDeMatrizesCurriculares();
        atualizarListaDeDisciplinas();
        atualizarListaDeTurmas();
        atualizarListaDeAlunos();
        atualizarListaDeComponentesDasMatrizes();
        atualizarListaDeMatriculas();
    }
    
}
