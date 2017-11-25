/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_dados;

import modelo.MatrizCurricular;

/**
 *
 * @author rafao
 */
public class MatrizCurricularDAO extends AbstractDAO<MatrizCurricular,Integer>{
    
    private static MatrizCurricularDAO instance = new MatrizCurricularDAO();
    
    public MatrizCurricularDAO(){
        super(MatrizCurricular.class);
    }
    
    public static MatrizCurricularDAO getInstance(){
        return instance;
    }
}
