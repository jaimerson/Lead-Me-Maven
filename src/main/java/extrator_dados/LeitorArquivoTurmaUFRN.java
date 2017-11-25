/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extrator_dados;

import static extrator_dados.ExtratorUFRN.PASTA_DADOS_ABERTOS;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import util.ThreadUtil;

/**
 *
 * @author rafao
 */
public class LeitorArquivoTurmaUFRN extends Thread{
    
    private int ano;
    private int periodo;
    
    public static final int NUMERO_THREADS_PROCESSAMENTO_LINHAS = 15;
    
    public LeitorArquivoTurmaUFRN(int ano, int periodo){
        this.ano = ano;
        this.periodo = periodo;
    }
    
    public void run(){
        BufferedReader lerArq;
        try{
            lerArq = new BufferedReader(new InputStreamReader(new FileInputStream(PASTA_DADOS_ABERTOS + "\\turmas\\turmas-" + ano + "." + periodo + ".csv"), "UTF-8"));
            ExecutorService executorService = Executors.newFixedThreadPool(NUMERO_THREADS_PROCESSAMENTO_LINHAS);
            Collection<Future> tasks = new LinkedList<>();
            Future submit;
            String linha = lerArq.readLine();
            while((linha = lerArq.readLine()) != null){
                System.out.println("Lendo a linha do arquivo " + ano + "." + periodo);
                submit = executorService.submit(new TurmaParser(linha));
                tasks.add(submit);
            }
            ThreadUtil.esperarThreads(tasks);
            lerArq.close();
            executorService.shutdown();
            System.out.println("FINALIZOU A EXTRAÇÃO DAS TURMAS!!");
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        
    }
}
