/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extrator_dados;

import base_dados.DisciplinaDAO;
import base_dados.TurmaDAO;
import static extrator_dados.ExtratorUFRN.camposValidos;
import javax.persistence.PersistenceException;
import modelo.Disciplina;
import modelo.Turma;
import static extrator_dados.ExtratorUFRN.prepararLinha;

/**
 *
 * @author rafao
 */
public class TurmaParser implements Runnable {

    private String[] dadosTurma;
    
    private DisciplinaDAO disciplinaDAO;
    private TurmaDAO turmaDAO;
    
    public TurmaParser(String linha){
        linha = prepararLinha(linha);
        dadosTurma = linha.split(ExtratorUFRN.SEPARADOR_CSV);
        disciplinaDAO = DisciplinaDAO.getInstance();
        turmaDAO = TurmaDAO.getInstance();
    }
    
    @Override
    public void run() {
        Turma turma;
        Disciplina disciplina;
        if (dadosTurma.length <= 24 || !camposValidos(dadosTurma, 0, 1, 5, 6, 9, 10, 22, 24)) {
            return;
        }
        if (!dadosTurma[6].equals("GRADUAÇÃO") || !dadosTurma[22].equals("CONSOLIDADA") || !dadosTurma[24].equals("Presencial")) {
            return;
        }
        turma = new Turma();
        turma.setId(Integer.parseInt(dadosTurma[0]));
        turma.setCodigoTurma(dadosTurma[1]);
        disciplina = disciplinaDAO.encontrar(Integer.parseInt(dadosTurma[5]));
        if (disciplina == null) {
            System.out.println("Disciplina para a turma eh nula");
            return;
        }
        turma.setDisciplina(disciplina);
        turma.setPeriodoLetivo("" + dadosTurma[9] + "." + dadosTurma[10]);
        try{
            turmaDAO.salvar(turma);
            System.out.println("Salvou a turma");
        }catch(PersistenceException e){
            System.out.println("NAO SALVOU A TURMA");
        }
    }
}
