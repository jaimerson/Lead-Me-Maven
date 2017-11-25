/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extrator_dados;

import base_dados.DisciplinaDAO;
import base_dados.MatrizCurricularDAO;
import base_dados.MatrizDisciplinaDAO;
import static extrator_dados.ExtratorUFRN.camposValidos;
import javax.persistence.PersistenceException;
import modelo.Disciplina;
import modelo.MatrizCurricular;
import modelo.MatrizDisciplina;
import static extrator_dados.ExtratorUFRN.prepararLinha;

/**
 *
 * @author rafao
 */
public class MatrizDisciplinaParser implements Runnable {

    private String[] dados;

    private MatrizCurricularDAO matrizCurricularDAO;
    private DisciplinaDAO disciplinaDAO;
    private MatrizDisciplinaDAO matrizDisciplinaDAO;

    public MatrizDisciplinaParser(String linha) {
        linha = prepararLinha(linha);
        dados = linha.split(ExtratorUFRN.SEPARADOR_CSV);
        matrizCurricularDAO = MatrizCurricularDAO.getInstance();
        disciplinaDAO = DisciplinaDAO.getInstance();
        matrizDisciplinaDAO = MatrizDisciplinaDAO.getInstance();
    }

    @Override
    public void run() {
        MatrizDisciplina md;
        MatrizCurricular curriculo;
        Disciplina disciplina;
        if (!camposValidos(dados, 0, 1, 2, 3, 4)) {
            return;
        }
        md = new MatrizDisciplina();
        md.setId(Integer.parseInt(dados[0]));
        curriculo = matrizCurricularDAO.encontrar(Integer.parseInt(dados[1]));
        disciplina = disciplinaDAO.encontrar(Integer.parseInt(dados[2]));
        if (curriculo == null || disciplina == null) {
//            fecharConexoes();
            System.out.println("Matriz ou disciplina nula");
            return;
        }
        md.setMatrizCurricular(curriculo);
        md.setDisciplina(disciplina);
        md.setSemestreIdeal(Integer.parseInt(dados[3]));
        md.setNaturezaDisciplina(dados[4]);
        try{
            matrizDisciplinaDAO.salvar(md);
        }catch(PersistenceException e){
            System.out.println(e.getMessage());
        }
//        fecharConexoes();
    }

    private void fecharConexoes() {
        matrizCurricularDAO.fecharConexao();
        matrizDisciplinaDAO.fecharConexao();
        disciplinaDAO.fecharConexao();
    }

}
