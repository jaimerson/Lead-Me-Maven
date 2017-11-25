/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_dados;

import modelo.Matricula;

/**
 *
 * @author rafao
 */
public class MatriculaDAO extends AbstractDAO<Matricula,Integer>{
    private static MatriculaDAO instance = new MatriculaDAO();
    
    private MatriculaDAO(){
        super(Matricula.class);
    }
    
    public static MatriculaDAO getInstance(){
        return instance;
    }
}
