/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dados_instituicao.ColetorDadosFactory;
import excecoes.DataException;
import modelo.Aluno;
import dados_instituicao.ColetorDadosFacade;

/**
 *
 * @author rafao
 */
public class LoginService{

    private Aluno aluno;
    private static LoginService loginService = new LoginService();
    private ColetorDadosFacade coletor;
    
    private LoginService(){
        coletor = ColetorDadosFactory.getInstance().getColetorInstance();
        aluno = null;
    }

    public static LoginService getInstance(){
        return loginService;
    }
    
    public Aluno autenticar(String usuario, String senha) throws DataException{
        if (coletor.existeUsuario(usuario, senha)){
            aluno = new Aluno();
            aluno.setNumeroMatricula(usuario);
            //Carregar informaçoes do historico do aluno
            coletor.carregarHistoricoAluno(aluno);
        }
        else{
            aluno = null;
        }
        return aluno;        
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
    
}
