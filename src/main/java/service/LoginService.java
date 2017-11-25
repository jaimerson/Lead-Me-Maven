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
public class LoginService {

    private Aluno aluno;
    private static LoginService loginService = new LoginService();
    private CursoService cursoService;
    private AlunoDAO alunoDAO;
    
    private LoginService() {
        aluno = null;
        cursoService = new CursoService();
        alunoDAO = AlunoDAO.getInstance();
    }

    public static LoginService getInstance() {
        return loginService;
    }

    //Nesse caso, a senha ta sendo null
    public Aluno autenticar(String id, String senha) throws DataException, AutenticacaoException {
        //Se existe esse usuario
        aluno = alunoDAO.encontrar(id);
        if (aluno != null) {
//            bibliotecaMineracao.gerarArquivoParaAssociarDisciplinas(aluno.getCurso());
            return aluno;
        }
        else{
            throw new AutenticacaoException("Nao existe aluno com esse id");
        }
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
}
