/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import base_dados.TurmaDAO;
import conexao_api_sinfo.ConsumidorAPI;
import dto.MatriculaComponenteUfrnDTO;
import java.util.ArrayList;
import java.util.List;
import modelo.Aluno;
import modelo.Matricula;
import modelo.Turma;

/**
 *
 * @author rafao
 */
public class AlunoServiceUFRN extends AlunoService{
    
    private ConsumidorAPI consumidor;
    private TurmaDAO turmaDAO;
    
    public AlunoServiceUFRN(){
        consumidor = ConsumidorAPI.getInstance();
        turmaDAO = TurmaDAO.getInstance();
    }
    
    @Override
    public void carregarMatriculasDoAluno(Aluno aluno){
        List<MatriculaComponenteUfrnDTO> matriculasDTO = consumidor.coletarMatriculas(Integer.parseInt(aluno.getId()));
        List<Matricula> matriculas = new ArrayList<>();
        Matricula matricula;
        Turma turma;
        for(MatriculaComponenteUfrnDTO matriculaDTO: matriculasDTO){
            System.out.println("Carregando a matricula");
            matricula = new Matricula();
            matricula.setAluno(aluno);
            turma = turmaDAO.encontrar(matriculaDTO.getIdTurma());
            if (turma == null){
                System.err.println("Não encontrou a turma para a matricula recebida da API");
                continue;
            }
            matricula.setTurma(turma);
            if(matriculaDTO.getConceito()){
                System.err.println("Conceito true, entao a nota n eh double");
                continue;
            }
//            matricula.setMedia((Double) matriculaDTO.getMediaFinal());
            String situacao = consumidor.coletarSituacaoMatricula(matriculaDTO.getIdSituacaoMatricula());
            matricula.setSituacao(situacao);
            matricula.setNumeroFaltas(matriculaDTO.getFaltas().doubleValue());
            matriculas.add(matricula);
        }
        aluno.setMatriculas(matriculas);
    }
}
