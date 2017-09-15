/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_dados;

import excecoes.DataException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import modelo.Disciplina;

/**
 *
 * @author rafao
 */
public class DisciplinaDAO extends AbstractDAO{
    
    private static DisciplinaDAO instance = new DisciplinaDAO();
    
    private DisciplinaDAO(){
        
    }
    
    public static DisciplinaDAO getInstance(){
        return instance;
    }
    
    public Double coletarMediaAprovacao(Disciplina disciplina) throws DataException {
        String nomeCurso = disciplina.getCurso().getNome();
        String codigoDisciplina = disciplina.getCodigo();
        BufferedReader lerArq;
        Double resultado = null;
        try {
            lerArq = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(DIRETORIO_RECURSOS + "turmas/" + nomeCurso + " - turmas - " + codigoDisciplina + ".txt"), "UTF-8"));
            String linha; //2017.1:95
            Double soma = 0.0;
            Integer qtdeTurmas = 0;
            while ((linha = lerArq.readLine()) != null) {
                soma += Double.parseDouble(linha.split(":")[1]);
                qtdeTurmas++;
            }

            lerArq.close();
            resultado = soma / qtdeTurmas;

        } catch (UnsupportedEncodingException ex) {
            throw new DataException(ex.getMessage());
        } catch (FileNotFoundException ex) {
            throw new DataException(ex.getMessage());
        } catch (IOException ex) {
            throw new DataException(ex.getMessage());
        }
        return resultado;
    }
}
