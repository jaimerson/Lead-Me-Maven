/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_dados;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author rafao
 */
public class FabricaEM {
    
    private static EntityManagerFactory factory;
    
    public static EntityManagerFactory getEntityManagerFactory(){
        if (factory == null || !factory.isOpen()){
            factory = Persistence.createEntityManagerFactory("LeadMePU");
        }
        return factory;
    }
}
