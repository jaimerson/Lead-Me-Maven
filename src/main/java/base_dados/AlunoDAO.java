/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_dados;

import modelo.Aluno;

/**
 *
 * @author rafao
 */
public class AlunoDAO extends AbstractDAO<Aluno,String> {

    private static AlunoDAO instance = new AlunoDAO();

    public AlunoDAO() {
        super(Aluno.class);
    }

    public static AlunoDAO getInstance() {
        return instance;
    }
}
