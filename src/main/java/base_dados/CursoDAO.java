/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_dados;

import static base_dados.AbstractDAO.DIRETORIO_RECURSOS;
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
import modelo.PossibilidadePreRequisito;

/**
 *
 * @author rafao
 */
public class CursoDAO extends AbstractDAO{
    private static CursoDAO instance = new CursoDAO();
    
    private CursoDAO(){
        
    }
    
    public static CursoDAO getInstance(){
        return instance;
    }
    public Curso carregarCurso(String nomeCurso) throws IOException {
        Map<String, Disciplina> disciplinasDoCurso = new HashMap<String, Disciplina>();
        Curso curso = new Curso(nomeCurso);
        System.out.println(System.getProperty("user.dir"));
        String[] arquivosGrade = getArquivosMatrizesCurricularesDoCurso(nomeCurso);
        for (String arquivoGrade : arquivosGrade) {
            MatrizCurricular matriz = new MatrizCurricular(arquivoGrade.split(" - ")[2].replace(".txt", ""));
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
        String[] possibilidades, disciplinasPossibilidade;
        //para cada disciplina
        while ((linha = lerArq.readLine()) != null) {
            codigoDisciplina = linha.split(": ")[0];
            //Pego a referencia no hashmap
            disciplina = disciplinas.get(codigoDisciplina);
            if (disciplina == null) {
                continue;
            }
            preRequisitos = linha.split(": ")[1];
            //Separo as possibilidades de pre requisito para adicioná-las na disciplina
            possibilidades = preRequisitos.split(" OU ");
            //Para cada possibilidade, eu tenho um conjunto de matérias que o aluno deve cumprir (uma ou mais)
            for (String possibilidade : possibilidades) {
                PossibilidadePreRequisito possibilidadePreRequisito = new PossibilidadePreRequisito();
                disciplinasPossibilidade = possibilidade.split(" E ");
                //Para cada matéria da possibilidade, adicionar no objeto possibilidade
                for (String disciplinaPossibilidade : disciplinasPossibilidade) {
                    possibilidadePreRequisito.adicionarPreRequisitoNaPossibilidade(disciplinas.get(disciplinaPossibilidade));
                }
                //Para cada possibilidade, adicionar na disciplina
                disciplina.adicionarPossibilidadePreRequisito(possibilidadePreRequisito);
            }
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
