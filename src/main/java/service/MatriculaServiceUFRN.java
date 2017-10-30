/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import modelo.Matricula;

/**
 *
 * @author rafao
 */
public class MatriculaServiceUFRN extends MatriculaService{

    @Override
    public boolean situacaoAprovada(Matricula matricula){
        return matricula.getSituacao().startsWith("APR");
    }
    
}
