/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import fabricas.Fabrica;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import modelo.Disciplina;
import modelo.MatrizDisciplina;
import modelo.Turma;

/**
 *
 * @author rafao
 */
public class DisciplinaService {

    TurmaService turmaService;
    
    public DisciplinaService() {
        turmaService = new TurmaService();
    }

    /**
     * Retorna a média de aprovações das turmas da disciplina. Serve para
     * carregar o gráfico pizza com as aprovações.
     *
     * @param disciplina disciplina que o usuário escolheu para ver as
     * estatísticas
     * @return média de aprovações dessa disciplina
     */
    public Double coletarMediaAprovacao(Disciplina disciplina) {
        Collection<Turma> turmas = disciplina.getTurmas();
        //Se nenhuma turma foi adicionada, nao podemos falar q houve reprovacoes
        if (disciplina.getTurmas() == null || disciplina.getTurmas().isEmpty()) {
            return 100.0;
        }
        Double somaAprovacoes = 0.0;
        int turmasValidas = 0;
        
        for (Turma turma : turmas) {
            if(turma.getMatriculas().isEmpty()){
                continue;
            }
            turmasValidas++;
            somaAprovacoes += turmaService.coletarMediaAprovacao(turma);
        }
        return turmasValidas > 0 ? somaAprovacoes / turmasValidas : 100.0;
    }
    
    public Double coletarMediaReprovacao(Disciplina disciplina){
        return 100.0 - coletarMediaAprovacao(disciplina);
    }
    
    public void ordenarDisciplinas(List<MatrizDisciplina> disciplinas){
        ComparadorMatrizDisciplina comparador = Fabrica.getInstance().getFactory().createComparadorMatrizDisciplina();
        Collections.sort(disciplinas, comparador);
    }
    
   public Double coletarMediaDeMatriculas(Disciplina disciplina){
       Double somaMatriculas = 0.0;
       List<Turma> turmas = disciplina.getTurmas();
       for(Turma turma: turmas){
           somaMatriculas += turma.getNumeroAprovados();
           somaMatriculas += turma.getNumeroReprovados();
       }
       return somaMatriculas / turmas.size();
   }
}
