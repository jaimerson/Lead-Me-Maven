/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

/**
 *
 * @author rafao
 */
public class RequisitosServiceUFRN extends RequisitosService{

    @Override
    public String coletarRegexCodigoDisciplina() {
        return "[A-Z]{3}[0-9]{4}";
    }

    @Override
    public String coletarExpressaoRequisitosComOperadores(String requisitos) {
        return requisitos.replace(" E ", "*").replace(" OU ", "+");
    }
    
}
