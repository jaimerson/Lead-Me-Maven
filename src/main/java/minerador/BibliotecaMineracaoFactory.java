/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minerador;

/**
 *
 * @author rafao
 */
public class BibliotecaMineracaoFactory {
    private BibliotecaMineracao biblioteca = new BibliotecaMineracaoImpl();
    private static BibliotecaMineracaoFactory instance = new BibliotecaMineracaoFactory();

    public static BibliotecaMineracaoFactory getInstance(){
        return instance;
    }
    
    private BibliotecaMineracaoFactory(){
        
    }
    
    public BibliotecaMineracao getBibliotecaMineracaoInstance(){
        return biblioteca;
    }
}
