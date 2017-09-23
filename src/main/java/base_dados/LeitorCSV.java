/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_dados;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Disciplina;

/**
 *
 * @author f200976
 */
public class LeitorCSV {
    private static final String NOME_ARQUIVO_CSV = "componentes.csv";
    
    public static Map<String,Disciplina> carregarDisciplinasDoCSV(){
        Map<String,Disciplina> disciplinas = new HashMap<>();
        String linha;
        String[] dadosDisciplina;
        Disciplina disciplina;
        try {
            BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream(NOME_ARQUIVO_CSV), "UTF-8"));
            lerArq.readLine();
            while ((linha = lerArq.readLine()) != null){
                dadosDisciplina = linha.split(";");
                disciplina = new Disciplina();
                dadosDisciplina[1] = dadosDisciplina[1].replace(" ","");
                if (!codigoValido(dadosDisciplina[1])){
                    System.out.println("Codigo Inválido: "+ dadosDisciplina[1]);
                    continue;
                }
                
                if (dadosDisciplina.length <= 7){
                    System.out.println("Divisão da linha está errada. Divisão: "+ Arrays.toString(dadosDisciplina));
                    
                    continue;
                }
                dadosDisciplina[4] = dadosDisciplina[4].replace(" ","");
                if (!dadosDisciplina[4].matches("[0-9]+")){
                    System.out.println("Carga horária está inválida : "+ dadosDisciplina[4] + " Código: " + dadosDisciplina[1]);
                    continue;
                }
                
                disciplina.setCodigo(dadosDisciplina[1]);
                System.out.println(disciplina.getCodigo());
                disciplina.setNome(dadosDisciplina[2]);
                disciplina.setCargaHoraria(Integer.parseInt(dadosDisciplina[4]));
                disciplina.setEquivalencias(dadosDisciplina[5]);
                disciplina.setPreRequisitos(dadosDisciplina[6]);
                disciplina.setCoRequisitos(dadosDisciplina[7]);
                if(disciplinas.containsKey(disciplina.getCodigo())){
                    disciplinas.put(disciplina.getCodigo(), disciplina);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LeitorCSV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LeitorCSV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LeitorCSV.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return disciplinas;
    }
    
    public static boolean codigoValido(String codigo){
        return codigo.matches("[A-Z]{3}[0-9]{4}");
    }

}
