/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dados_instituicao.ColetorDados;
import dados_instituicao.ColetorDadosFactory;
import excecoes.DataException;
import modelo.Curso;

/**
 *
 * @author rafao
 */
public class CursoService {
    private static CursoService instance = new CursoService();
    
    private Curso curso; 
    
    private CursoService(){
        curso = null;
    }
    
    public static CursoService getInstance(){
        return instance;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    
    public Curso carregarCurso(String nomeCurso) throws DataException{
        ColetorDados coletor = ColetorDadosFactory.getInstance().getColetorInstance();
        this.curso = coletor.getCurso(nomeCurso);
        return curso;
    }
    
}
