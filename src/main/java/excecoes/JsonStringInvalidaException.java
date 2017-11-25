/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excecoes;

/**
 *
 * @author rafao
 */
public class JsonStringInvalidaException extends ManipulacaoDosDadosException{
    
    public JsonStringInvalidaException(){
        super();
    }
    
    public JsonStringInvalidaException(String msg){
        super(msg);
    }
}
