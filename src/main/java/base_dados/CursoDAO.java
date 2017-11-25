/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_dados;

import modelo.Curso;

/**
 *
 * @author rafao
 */
public class CursoDAO extends AbstractDAO<Curso,Integer>{
    private static CursoDAO instance = new CursoDAO();
    private AlunoDAO alunoDAO;
    
    public CursoDAO(){
        super(Curso.class);
        alunoDAO = AlunoDAO.getInstance();
    }
    
    public static CursoDAO getInstance(){
        return instance;
    }
}
