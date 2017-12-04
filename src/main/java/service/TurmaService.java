/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import base_dados.DisciplinaDAO;
import fabricas.Fabrica;
import java.util.List;
import modelo.Disciplina;
import modelo.Matricula;
import modelo.Turma;

/**
 *
 * @author rafao
 */
public class TurmaService {
    
    DisciplinaDAO disciplinaDAO;
    
    public TurmaService(){
        disciplinaDAO = DisciplinaDAO.getInstance();
    }
    
    public Double coletarMediaAprovacao(Turma turma){
        Integer totalAlunos = turma.getNumeroAprovados() + turma.getNumeroReprovados();
        return 100.0*(turma.getNumeroAprovados().doubleValue()/totalAlunos);
    }
    
    //Só no caso do Hibernate der ruim com postgres
    public Disciplina coletarDisciplinaDaTurma(Turma turma){
        return disciplinaDAO.encontrarDisciplinaDaTurma(turma);
    }
}
