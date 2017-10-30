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
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Curso;
import modelo.Disciplina;
import modelo.MatrizCurricular;

/**
 *
 * @author rafao
 */
public class CarregadorMatrizCurricular extends Thread{
    private Curso curso;
    private String nomeMatriz;
    private String nomeArquivoMatrizCurricular;
    private Map<String,Disciplina> disciplinasDoCurso;
    
    public static final String NATUREZA_OPTATIVA = "OPTATIVO";
    public static final String NATUREZA_OBRIGATORIA = "OBRIGATORIO";
    
    public CarregadorMatrizCurricular(Curso curso, String nomeMatriz, String nomeArquivoMatrizCurricular, Map<String,Disciplina> disciplinasDoCurso){
        this.curso = curso;
        this.nomeMatriz = nomeMatriz;
        this.nomeArquivoMatrizCurricular = nomeArquivoMatrizCurricular;
        this.disciplinasDoCurso = disciplinasDoCurso;
    }
    
    @Override
    public void run(){
        MatrizCurricular matriz = new MatrizCurricular(nomeMatriz);
//            matriz.setCurso(curso);
            BufferedReader lerArq;
        try {
            lerArq = new BufferedReader(new InputStreamReader(new FileInputStream(this.nomeArquivoMatrizCurricular), "UTF-8"));
            String linha;
            String codigo, nomeDisciplina, naturezaDisciplina;
            Integer cargaHoraria, semestreIdeal;
            String[] dadosLinha;
            Disciplina disciplina;

            lerArq.readLine();
            lerArq.readLine();
            while ((linha = lerArq.readLine()) != null) {
                linha = linha.replace("\n", "");
                dadosLinha = linha.split(";");
                codigo = dadosLinha[0];
                nomeDisciplina = dadosLinha[1];
                cargaHoraria = Integer.parseInt(dadosLinha[2]);
                naturezaDisciplina = dadosLinha[3];
                semestreIdeal = Integer.parseInt(dadosLinha[4]);
                if (naturezaDisciplina.equals(NATUREZA_OPTATIVA)){
                    semestreIdeal = Integer.MAX_VALUE;
                }
                synchronized (disciplinasDoCurso){
                    if(!disciplinasDoCurso.containsKey(codigo)){
                        disciplina = new Disciplina(codigo, nomeDisciplina, cargaHoraria);
                        disciplinasDoCurso.put(codigo, disciplina);
                    }
                    else{
                        disciplina = disciplinasDoCurso.get(codigo);
                    }
                }
                matriz.adicionarDisciplina(disciplina, naturezaDisciplina, semestreIdeal);
            }
            lerArq.close();
            curso.adicionarMatrizCurricular(matriz);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CarregadorMatrizCurricular.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CarregadorMatrizCurricular.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CarregadorMatrizCurricular.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
}
