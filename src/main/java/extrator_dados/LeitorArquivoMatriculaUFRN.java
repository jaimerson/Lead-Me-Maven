/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extrator_dados;

import base_dados.AlunoDAO;
import base_dados.CursoDAO;
import base_dados.TurmaDAO;
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
public class LeitorArquivoMatriculaUFRN extends Thread {

    private static final int NUMERO_THREADS_PROCESSAMENTO_LINHAS = 15;
    private int ano;
    private int periodo;

    private CursoDAO cursoDAO;
    private TurmaDAO turmaDAO;
    private AlunoDAO alunoDAO;

    public LeitorArquivoMatriculaUFRN(int ano, int periodo) {
        this.ano = ano;
        this.periodo = periodo;
        cursoDAO = CursoDAO.getInstance();
        turmaDAO = TurmaDAO.getInstance();
        alunoDAO = AlunoDAO.getInstance();
    }

    public void run() {
        BufferedReader lerArq;
        try {
            lerArq = new BufferedReader(new InputStreamReader(new FileInputStream(PASTA_DADOS_ABERTOS + "\\matriculas\\matricula-componente-" + ano + "." + periodo + ".csv"), "UTF-8"));
            System.err.println("INICIANDO LEITURA DO ARQUIVO DE" + ano + "." + periodo);
            String linha = lerArq.readLine();
            ExecutorService executorService = Executors.newFixedThreadPool(NUMERO_THREADS_PROCESSAMENTO_LINHAS);
            Collection<Future> tasks = new LinkedList<>();
            Future submit;
            while ((linha = lerArq.readLine()) != null) {
                System.out.println("Lendo a linha do arquivo " + ano + "." + periodo);
                submit = executorService.submit(new MatriculaParser(linha));
                tasks.add(submit);
            }
            ThreadUtil.esperarThreads(tasks);
            lerArq.close();
            System.out.println("FINALIZOU A EXTRAÇÃO DAS MATRÍCULAS DO ARQUIVO " + ano + "." + periodo + "!!");
        } catch (IOException ioe) {
            System.out.println(ioe.getLocalizedMessage());
        }
    }
}
