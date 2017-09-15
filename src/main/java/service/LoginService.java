/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import base_dados.AlunoDAO;
import excecoes.DataException;
import modelo.Aluno;
import excecoes.AutenticacaoException;

/**
 *
 * @author rafao
 */
public class LoginService{

    private Aluno aluno;
    private static LoginService loginService = new LoginService();
    private CursoService cursoService;
    private AlunoDAO alunoDAO;
    
    private LoginService(){
        aluno = null;
        cursoService = CursoService.getInstance();
        alunoDAO = AlunoDAO.getInstance();
    }

    public static LoginService getInstance(){
        return loginService;
    }
    
    public Aluno autenticar(String usuario, String senha) throws DataException, AutenticacaoException{
        aluno = alunoDAO.carregarAluno(usuario, senha);
        return aluno;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
}
