/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fabricas;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rafao
 */
public class Fabrica {

    private static Fabrica instance = new Fabrica();
    private AbstractFactory factory;

    private Fabrica() {
        factory = null;
    }

    public static Fabrica getInstance() {
        return instance;
    }

    private AbstractFactory criarFactory() {
        try {
            String instancia = ConfiguracaoFabrica.coletarInstanciaSistema();
            if (instancia.equalsIgnoreCase("UFRN")) {
                factory = new UFRNFactory();
            } else if (instancia.equalsIgnoreCase("ESTACIO")) {
//                factory = new EstacioFactory();
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Fabrica.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Fabrica.class.getName()).log(Level.SEVERE, null, ex);
        }
        return factory;
    }

    public AbstractFactory getFactory() {
        if (factory == null) {
            factory = criarFactory();
        }
        return factory;
    }
}
