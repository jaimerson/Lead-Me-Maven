/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_dados;

import excecoes.DataException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import minerador.GeradorCSV;
import modelo.Curso;
import modelo.Disciplina;
import util.ThreadUtil;

/**
 *
 * @author rafao
 */
public class CursoDAO extends AbstractDAO{
    private static CursoDAO instance = new CursoDAO();
    private AlunoDAO alunoDAO;
    
    private CursoDAO(){
        alunoDAO = AlunoDAO.getInstance();
    }
    
    public static CursoDAO getInstance(){
        return instance;
    }
    
 
    public Curso carregarCurso(String nomeCurso) throws DataException {
        //Synchronized map pelo fato de muitas threads acessarem esse objeto
//        Map<String, Disciplina> disciplinasDoCurso = Collections.synchronizedMap(new HashMap<String,Disciplina>());
        Map<String, Disciplina> disciplinasDoCurso = new ConcurrentHashMap<>();
        Curso curso = new Curso(nomeCurso);
        String[] arquivosGrade = getArquivosMatrizesCurricularesDoCurso(nomeCurso);
        List<Thread> listaThreadsParaCarregar = new ArrayList<>();
        long tempoInicial = System.currentTimeMillis();
        for (String arquivoGrade :arquivosGrade) {
            Thread th = new CarregadorMatrizCurricular(curso, arquivoGrade.split(" - ")[2].replace(".txt", ""), DIRETORIO_RECURSOS + "grades/" + arquivoGrade, disciplinasDoCurso);
            th.start();
            listaThreadsParaCarregar.add(th);
        }
        ThreadUtil.esperarThreads(listaThreadsParaCarregar);
        try {
            carregarPreRequisitos(nomeCurso, disciplinasDoCurso);
        } catch (IOException ex) {
            throw new DataException(ex.getLocalizedMessage());
        }
        
        //Agora que carregamos as matrizes curriculares com suas respectivas disciplinas
        //Podemos carregar os alunos do curso
        carregarAlunosDoCurso(curso);
        long tempoFinal = System.currentTimeMillis();
        System.out.println("Tempo gasto para carregar:" + (tempoFinal - tempoInicial) + " milisegundos");
        return curso;
    }
    
    private void carregarAlunosDoCurso(Curso curso){
        String[] arquivosHistoricoAlunos = coletarArquivosHistoricoAlunos();
        List<Thread> threadsParaAlunos = new ArrayList<>();
        for (String arquivoHistoricoAluno:arquivosHistoricoAlunos){
            Thread th = new CarregadorAluno(arquivoHistoricoAluno, alunoDAO, DIRETORIO_RECURSOS + "historicos/" + arquivoHistoricoAluno, curso);
            th.start();
            threadsParaAlunos.add(th);
        }
        ThreadUtil.esperarThreads(threadsParaAlunos);
    }
    
    private void carregarPreRequisitos(String nomeCurso, Map<String, Disciplina> disciplinas) throws IOException {
        BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream(DIRETORIO_RECURSOS + "grades/" + nomeCurso + " - pre requisitos.txt"), "UTF-8"));
        String linha;
        String codigoDisciplina, preRequisitos, coRequisitos;
        Disciplina disciplina;
        String[] dadosLinha;
        //para cada disciplina
        while ((linha = lerArq.readLine()) != null) {
            linha = linha.replace("\n", "");
            dadosLinha = linha.split(":");
            codigoDisciplina = dadosLinha[0];
            //Pego a referencia no hashmap
            disciplina = disciplinas.get(codigoDisciplina);
            if (disciplina == null) {
                continue;
            }
            preRequisitos = dadosLinha[1];
            coRequisitos = dadosLinha[2];
            disciplina.setPreRequisitos(preRequisitos);
            disciplina.setCoRequisitos(coRequisitos);
        }
        lerArq.close();
    }

    private String[] coletarArquivosHistoricoAlunos() {
        File file = new File(DIRETORIO_RECURSOS + "historicos/");
        return file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith("-historico.txt");
            }
        });
    }
    
    private String[] getArquivosMatrizesCurricularesDoCurso(String nomeCurso) {
        File file = new File(DIRETORIO_RECURSOS + "grades/");
        final String nomeDoCurso = nomeCurso;
        return file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().startsWith("grade - " + nomeDoCurso.toLowerCase());
            }
        });
    }
}
