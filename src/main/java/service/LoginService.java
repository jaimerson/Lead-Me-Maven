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
import modelo.Curso;

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
        cursoService = new CursoService();
        alunoDAO = AlunoDAO.getInstance();
    }

    public static LoginService getInstance(){
        return loginService;
    }
    
    public Aluno autenticar(String matricula, String senha) throws DataException, AutenticacaoException{
        //Se existe esse usuario
        if (alunoDAO.existeUsuario(matricula, senha)){
            //Carregue o curso desse usuario
            String nomeCurso = alunoDAO.carregarNomeCurso(matricula);
            Curso curso = cursoService.carregarCurso(nomeCurso);
            //E depois colete o aluno com essa matricula
            aluno = curso.coletarAluno(matricula);
            if (aluno == null){
                System.out.println("ALUNO NULO!!!");
            }
            return aluno;
        }
        else{
            throw new AutenticacaoException("Usuário e/ou senha inválidos");
        }
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
}
