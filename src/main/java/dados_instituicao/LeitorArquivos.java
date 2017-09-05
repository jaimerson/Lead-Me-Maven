package dados_instituicao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import modelo.Curso;
import modelo.Disciplina;
import modelo.MatrizCurricular;
import modelo.PossibilidadePreRequisito;

public class LeitorArquivos {

    public static final String DIRETORIO_GRADES = "src/main/resources/grades/";
    public static final String DIRETORIO_TURMAS = "src/main/resources/turmas/";
    public static String[] getArquivosMatrizesCurricularesDoCurso(String nomeCurso) {
        File file = new File(DIRETORIO_GRADES);
        final String nomeDoCurso = nomeCurso;
        return file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().startsWith("grade - " + nomeDoCurso.toLowerCase());
            }
        });
    }

    public static Curso carregarCursoComGrades(String nomeCurso) throws IOException {
        Map<String, Disciplina> disciplinasDoCurso = new HashMap<String, Disciplina>();
        Curso curso = new Curso(nomeCurso);
        System.out.println(System.getProperty("user.dir"));
        String[] arquivosGrade = getArquivosMatrizesCurricularesDoCurso(nomeCurso);
        
        for (String arquivoGrade : arquivosGrade) {
            MatrizCurricular matriz = new MatrizCurricular(arquivoGrade.split(" - ")[2].replace(".txt", ""));
            BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream(DIRETORIO_GRADES + arquivoGrade), "UTF-8"));
            String linha;
            String codigo, nomeDisciplina, naturezaDisciplina;
            Integer cargaHoraria, semestreIdeal;
            String[] dadosLinha;
            Disciplina disciplina;

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

    public static void carregarPreRequisitos(String nomeCurso, Map<String, Disciplina> disciplinas) throws IOException {
        BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream(DIRETORIO_GRADES + nomeCurso + " - pre requisitos.txt"), "UTF-8"));
        String linha;
        String codigoDisciplina, preRequisitos;
        Disciplina disciplina;
        String[] possibilidades, disciplinasPossibilidade;
        //para cada disciplina
        while ((linha = lerArq.readLine()) != null){
            codigoDisciplina = linha.split(": ")[0];
            //Pego a referencia no hashmap
            disciplina = disciplinas.get(codigoDisciplina);
            preRequisitos = linha.split(": ")[1];
            //Separo as possibilidades de pre requisito para adicioná-las na disciplina
            possibilidades = preRequisitos.split(" OU ");
            //Para cada possibilidade, eu tenho um conjunto de matérias que o aluno deve cumprir (uma ou mais)
            for (String possibilidade: possibilidades){
                PossibilidadePreRequisito possibilidadePreRequisito = new PossibilidadePreRequisito();
                disciplinasPossibilidade = possibilidade.split(" E ");
                //Para cada matéria da possibilidade, adicionar no objeto possibilidade
                for (String disciplinaPossibilidade: disciplinasPossibilidade){
                    possibilidadePreRequisito.adicionarPreRequisitoNaPossibilidade(disciplinas.get(disciplinaPossibilidade));
                }
                //Para cada possibilidade, adicionar na disciplina
                disciplina.adicionarPossibilidadePreRequisito(possibilidadePreRequisito);
            }
        }
        lerArq.close();
    }
    
    public static Double getMediaAprovacao(String nomeCurso, String disciplina) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        BufferedReader lerArq = new BufferedReader(
            new InputStreamReader(
                new FileInputStream(DIRETORIO_TURMAS + nomeCurso + " - turmas "+ disciplina +".txt"), "UTF-8"));
        String linha; //2017.1:95
        Double soma = 0.0;
        Integer qtdeTurmas = 0;
        while( (linha = lerArq.readLine()) != null){
            soma += Double.parseDouble(linha.split(":")[1]);
            qtdeTurmas++;
        }
        
        lerArq.close();
        return soma/qtdeTurmas;
    }
}
