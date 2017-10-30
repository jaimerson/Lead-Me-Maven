/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import extrator_dados.Extrator;

/**
 *
 * @author rafao
 */
public class DadosService {
    
    private Extrator extrator;
    
    public void atualizarBaseDeDados(){
        extrator.atualizarBaseDeDados();
    }
}
