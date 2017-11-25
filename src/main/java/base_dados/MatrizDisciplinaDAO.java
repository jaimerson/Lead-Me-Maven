/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_dados;

import modelo.MatrizDisciplina;

/**
 *
 * @author rafao
 */
public class MatrizDisciplinaDAO extends AbstractDAO<MatrizDisciplina,Integer>{
    
    private static MatrizDisciplinaDAO instance = new MatrizDisciplinaDAO();
    
    public static MatrizDisciplinaDAO getInstance(){
        return instance;
    }
    
    private MatrizDisciplinaDAO() {
        super(MatrizDisciplina.class);
    }
    
}
