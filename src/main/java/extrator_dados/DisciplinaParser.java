/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extrator_dados;

import base_dados.DisciplinaDAO;
import static extrator_dados.ExtratorUFRN.camposValidos;
import javax.persistence.PersistenceException;
import modelo.Disciplina;
import static extrator_dados.ExtratorUFRN.prepararLinha;

/**
 *
 * @author rafao
 */
public class DisciplinaParser implements Runnable {

    private String[] dados;
    private DisciplinaDAO disciplinaDAO;
    
    public DisciplinaParser(String linha) {
        linha = prepararLinha(linha);
        dados = linha.split(ExtratorUFRN.SEPARADOR_CSV);
        disciplinaDAO = DisciplinaDAO.getInstance();
    }

    @Override
    public void run() {
        Disciplina disciplina;
        if (dados.length <= 19) {
            return;
        }
        if (!camposValidos(dados, 0, 2, 3, 4, 9)) {
            return;
        }

        //So interessa as disciplinas de graduacao
        if (!dados[3].toUpperCase().equals("G")) {
            return;
        }
        disciplina = new Disciplina();
        disciplina.setId(Integer.parseInt(dados[0]));
        disciplina.setCodigo(dados[2]);
        disciplina.setNome(dados[4]);
        disciplina.setCargaHoraria(Integer.parseInt(dados[9]));
        disciplina.setEquivalencias(dados[16] == null ? "" : dados[16]);
        disciplina.setPreRequisitos(dados[17] == null ? "" : dados[17]);
        disciplina.setCoRequisitos(dados[18] == null ? "" : dados[18]);
        try{
            disciplinaDAO.salvar(disciplina);
            System.out.println("Salvou a disciplina");
        }catch(PersistenceException e){
            System.out.println("NAO SALVOU A DISCIPLINA");
        }
//        disciplinaDAO.fecharConexao();
    }

}
