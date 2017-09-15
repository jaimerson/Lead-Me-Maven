/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_dados;

import excecoes.DataException;
import excecoes.AutenticacaoException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Aluno;
import modelo.Curso;
import modelo.Disciplina;
import modelo.Matricula;
import modelo.MatrizCurricular;
import modelo.MatrizDisciplina;
import modelo.PossibilidadePreRequisito;
import modelo.Turma;
//import service.CursoService;

/**
 *
 * @author rafao
 */
public class AlunoDAO extends AbstractDAO{
    
    private static AlunoDAO instance = new AlunoDAO();
    
    private AlunoDAO(){
        
    }
    
    public static AlunoDAO getInstance(){
        return instance;
    }
    
    private boolean existeUsuario(String usuario, String senha) throws DataException {
        boolean existeUsuario = false;
        try {
            Map<String, String> logins = getLoginsESenhas();
            if (logins.containsKey(usuario) && logins.get(usuario).equals(senha)) {
                existeUsuario = true;
            }
        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(ColetorDadosImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataException(ex.getMessage());
        } catch (IOException ex) {
//            Logger.getLogger(ColetorDadosImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataException(ex.getMessage());
        }
        return existeUsuario;
    }

    private Map<String, String> getLoginsESenhas() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        Map<String, String> logins = new HashMap<String, String>();
        BufferedReader lerArq;
        String linha;
        String[] dadosLinha;
        lerArq = new BufferedReader(new InputStreamReader(new FileInputStream(DIRETORIO_RECURSOS + "logins/logins.txt"), "UTF-8"));
        while ((linha = lerArq.readLine()) != null) {
            dadosLinha = linha.split(";");
            logins.put(dadosLinha[0], dadosLinha[1]);
        }
        lerArq.close();
        return logins;
    }
    public Aluno carregarAluno(String usuario, String senha) throws DataException, AutenticacaoException{
        Aluno aluno = new Aluno();
        if (!existeUsuario(usuario,senha)){
            throw new AutenticacaoException("Usuário e/ou senha inválidos");
        }
        aluno.setNumeroMatricula(usuario);
        carregarHistoricoAluno(aluno);
        return aluno;
    }
    
    public void carregarHistoricoAluno(Aluno aluno) throws DataException {
        try {
            BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream(DIRETORIO_RECURSOS + "historicos/" + aluno.getNumeroMatricula() + "-historico.txt"), "UTF-8"));
            aluno.setNome(lerArq.readLine());
            lerArq.readLine(); //ignorando a matricula
            String cursoComMatriz = lerArq.readLine();
            String nomeCurso = cursoComMatriz.split(" - ")[0];
            String matrizCurricular = cursoComMatriz.split(" - ")[1];
            Curso curso = carregarCurso(nomeCurso, matrizCurricular);
            aluno.setCurso(curso);
            aluno.setMatrizCurricular(matrizCurricular);
//            CursoService cursoService = CursoService.getInstance();
//            cursoService.setCurso(curso);
            aluno.setIea(Double.parseDouble(lerArq.readLine()));
            aluno.setMcn(Double.parseDouble(lerArq.readLine()));
            String linha;
            //O aluno tem uma lista de matriculas
            Matricula matricula;
            MatrizDisciplina matrizDisciplina;
            Turma turma;
            String[] dadosDisciplina;
            while ((linha = lerArq.readLine()) != null) {
                dadosDisciplina = linha.split(";");
                matrizDisciplina = aluno.getCurso().getDisciplina(matrizCurricular, dadosDisciplina[1]);
                if (matrizDisciplina == null) {
                    continue;
                }
                turma = new Turma(dadosDisciplina[0], matrizDisciplina.getDisciplina());
                matricula = new Matricula(turma, aluno);
                matricula.setMedia(Double.parseDouble(dadosDisciplina[3]));
                matricula.setSituacao(dadosDisciplina[4]);
                //Contando as horas cumpridas  
                if (matricula.getSituacao().contains("APR")) {
                    if (matrizDisciplina.getNaturezaDisciplina().equals("OBRIGATORIO")) {
                        aluno.setCargaObrigatoriaCumprida(aluno.getCargaObrigatoriaCumprida() + matrizDisciplina.getDisciplina().getCargaHoraria());
                    } else {
                        aluno.setCargaOptativaCumprida(aluno.getCargaOptativaCumprida() + matrizDisciplina.getDisciplina().getCargaHoraria());
                    }
                }
                aluno.adicionarMatricula(matricula);
            }

            lerArq.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    private Curso carregarCurso(String nomeCurso, String matrizCurricular) throws IOException {
        Map<String, Disciplina> disciplinasDoCurso = new HashMap<String, Disciplina>();
        Curso curso = new Curso(nomeCurso);
        System.out.println(System.getProperty("user.dir"));
        String[] arquivosGrade = getArquivosMatrizesCurricularesDoCurso(nomeCurso);
        if (matrizCurricular != null) {
            arquivosGrade = new String[]{"Grade - " + nomeCurso + " - " + matrizCurricular + ".txt"};
        }
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