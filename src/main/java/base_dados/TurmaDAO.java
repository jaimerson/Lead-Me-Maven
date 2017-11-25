/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_dados;

import modelo.Turma;

/**
 *
 * @author rafao
 */
public class TurmaDAO extends AbstractDAO<Turma,Integer>{
    
    private static TurmaDAO instance = new TurmaDAO();
    
    public TurmaDAO(){
        super(Turma.class);
    }
    
    public static TurmaDAO getInstance(){
        return instance;
    }
}
