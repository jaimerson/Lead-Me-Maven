/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_dados;

import modelo.Disciplina;
import modelo.Turma;

/**
 *
 * @author rafao
 */
public class DisciplinaDAO extends AbstractDAO<Disciplina,Integer>{
    private static DisciplinaDAO instance = new DisciplinaDAO();
    
    public DisciplinaDAO(){
        super(Disciplina.class);
    }
    
    public static DisciplinaDAO getInstance(){
        return instance;
    }
    
    public Disciplina encontrarDisciplinaDaTurma(Turma turma){
        return null;
    }
}
