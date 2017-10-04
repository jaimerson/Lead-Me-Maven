/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_dados;

import static base_dados.AbstractDAO.DIRETORIO_RECURSOS;
import excecoes.DataException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import modelo.Curso;
import modelo.Disciplina;
import modelo.MatrizCurricular;

/**
 *
 * @author rafao
 */
public class CursoDAO extends AbstractDAO{
    private static CursoDAO instance = new CursoDAO();
    private DisciplinaDAO disciplinaDAO;
    private CursoDAO(){
        disciplinaDAO = DisciplinaDAO.getInstance();
    }
    
    public static CursoDAO getInstance(){
        return instance;
    }
    public Curso carregarCurso(String nomeCurso) throws IOException {
        Map<String, Disciplina> disciplinasDoCurso = new HashMap<>();
        Curso curso = new Curso(nomeCurso);
        System.out.println(System.getProperty("user.dir"));
        String[] arquivosGrade = getArquivosMatrizesCurricularesDoCurso(nomeCurso);
        for (String arquivoGrade : arquivosGrade) {
            MatrizCurricular matriz = new MatrizCurricular(arquivoGrade.split(" - ")[2].replace(".txt", ""));
            matriz.setCurso(curso);
            BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream(DIRETORIO_RECURSOS + "grades/" + arquivoGrade), "UTF-8"));
            String linha;
            String codigo, nomeDisciplina, naturezaDisciplina;
            Integer cargaHoraria, semestreIdeal;
            String[] dadosLinha;
            Disciplina disciplina;

            matriz.setCargaHorariaObrigatoria(Integer.parseInt(lerArq.readLine()));
            matriz.setCargaHorariaOptativa(Integer.parseInt(lerArq.readLine()));
            while ((linha = lerArq.readLine()) != null) {
                linha = linha.replace("\n", "");
                dadosLinha = linha.split(";");
                codigo = dadosLinha[0];
                nomeDisciplina = dadosLinha[1];
                cargaHoraria = Integer.parseInt(dadosLinha[2]);
                naturezaDisciplina = dadosLinha[3];
                semestreIdeal = Integer.parseInt(dadosLinha[4]);
                if (!disciplinasDoCurso.containsKey(codigo)) {
                    disciplina = new Disciplina(codigo, nomeDisciplina, cargaHoraria);
                    disciplinasDoCurso.put(codigo, disciplina);
                } else {
                    disciplina = disciplinasDoCurso.get(codigo);
                }
                matriz.adicionarDisciplina(disciplina, naturezaDisciplina, semestreIdeal);
                disciplinaDAO.carregarTurmasDaDisciplina(disciplina);
            }
            lerArq.close();
            curso.adicionarMatrizCurricular(matriz);
        }
        carregarPreRequisitos(nomeCurso, disciplinasDoCurso);
        return curso;
    }

    private void carregarPreRequisitos(String nomeCurso, Map<String, Disciplina> disciplinas) throws IOException {
        BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream(DIRETORIO_RECURSOS + "grades/" + nomeCurso + " - pre requisitos.txt"), "UTF-8"));
        String linha;
        String codigoDisciplina, preRequisitos;
        Disciplina disciplina;
        //para cada disciplina
        while ((linha = lerArq.readLine()) != null) {
            linha = linha.replace("\n", "");
            codigoDisciplina = linha.split(": ")[0];
            //Pego a referencia no hashmap
            disciplina = disciplinas.get(codigoDisciplina);
            if (disciplina == null) {
                continue;
            }
            preRequisitos = linha.split(": ")[1];
            disciplina.setPreRequisitos(preRequisitos);
        }
        lerArq.close();
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
