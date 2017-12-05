/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_dados;

import java.io.Serializable;
import modelo.Docente;

/**
 *
 * @author rafael
 */
public class DocenteDAO extends AbstractDAO<Docente, Integer>{
    
    private static DocenteDAO instance = new DocenteDAO();
    
    private DocenteDAO(){
        super(Docente.class);
    }
    
    public static DocenteDAO getInstance(){
        return instance;
    }
}
