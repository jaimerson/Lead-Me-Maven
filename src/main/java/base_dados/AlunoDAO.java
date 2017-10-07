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
import java.util.HashMap;
import java.util.Map;
import modelo.Aluno;
import modelo.Curso;
import modelo.Disciplina;
import modelo.Matricula;
import modelo.MatrizDisciplina;
import modelo.Turma;

/**
 *
 * @author rafao
 */
public class AlunoDAO extends AbstractDAO {

    private static AlunoDAO instance = new AlunoDAO();

    private AlunoDAO() {
    }

    public static AlunoDAO getInstance() {
        return instance;
    }

    public boolean existeUsuario(String usuario, String senha) throws DataException {
        boolean existeUsuario = false;
        try {
            Map<String, String> logins = getLoginsESenhas();
            if (logins.containsKey(usuario) && logins.get(usuario).equals(senha)) {
                existeUsuario = true;
            }
        } catch (UnsupportedEncodingException ex) {
            throw new DataException(ex.getMessage());
        } catch (IOException ex) {
            throw new DataException(ex.getMessage());
        }
        return existeUsuario;
    }

    public String carregarNomeCurso(String matriculaAluno) throws DataException{
        BufferedReader lerArq;
        String nomeCurso = null;
        try {
            lerArq = new BufferedReader(new InputStreamReader(new FileInputStream(DIRETORIO_RECURSOS + "historicos/" + matriculaAluno + "-historico.txt"), "UTF-8"));
            lerArq.readLine();
            lerArq.readLine(); //ignorando a matricula
            String cursoComMatriz = lerArq.readLine();
            nomeCurso = cursoComMatriz.split(" - ")[0];
            lerArq.close();
        } catch (FileNotFoundException ex) {
            throw new DataException(ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            throw new DataException(ex.getMessage());
        } catch (IOException ex) {
            throw new DataException(ex.getMessage());
        }
        return nomeCurso;
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

    //Método utilizado pela funcionalidade de carregar curso
    public void carregarAluno(String nomeArquivoHistorico, Curso curso) throws DataException{
        Aluno aluno = new Aluno();
        try {
            BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream(nomeArquivoHistorico), "UTF-8"));
            aluno.setNome(lerArq.readLine());
            aluno.setNumeroMatricula(lerArq.readLine());
            String cursoComMatriz = lerArq.readLine();
            String nomeCurso = cursoComMatriz.split(" - ")[0];
            //Se o nome do curso não for o mesmo.. nao temos pra que continuar a leitura do arquivo
            if (!nomeCurso.equalsIgnoreCase(curso.getNome())){
                lerArq.close();
                return;
            }
            aluno.setCurso(curso);
            
            String matrizCurricular = cursoComMatriz.split(" - ")[1];
            aluno.setMatrizCurricular(matrizCurricular);
            aluno.setIea(Double.parseDouble(lerArq.readLine()));
            aluno.setMcn(Double.parseDouble(lerArq.readLine()));
            String linha;
            //O aluno tem uma lista de matriculas
            Matricula matricula;
            MatrizDisciplina matrizDisciplina;
            Disciplina disciplina;
            Turma turma;
            String[] dadosDisciplina;
            while ((linha = lerArq.readLine()) != null) {
                dadosDisciplina = linha.split(";");
                matrizDisciplina = aluno.getCurso().getDisciplina(matrizCurricular, dadosDisciplina[1]);
                if (matrizDisciplina == null) {
                    System.out.println("Thread " + Thread.currentThread().getName()+": Matriz nula! Disciplina "+ dadosDisciplina[1]);
                    continue;
                }
                disciplina = matrizDisciplina.getDisciplina();
                synchronized(disciplina){
                    if (disciplina.coletarTurma(dadosDisciplina[0]) == null){
                        turma = new Turma(dadosDisciplina[0], disciplina);
                        disciplina.adicionarTurma(turma);
                    }
                    else{
                        turma = disciplina.coletarTurma(dadosDisciplina[0]);
                    }
                }
                //Metodo synchronized
                matricula = turma.adicionarAluno(aluno);
                
                matricula.setMedia(Double.parseDouble(dadosDisciplina[3]));
                matricula.setSituacao(dadosDisciplina[5]);
                matricula.setPorcentagemPresencas(Double.parseDouble(dadosDisciplina[4]));
                
                //Contando as horas cumpridas  
                if (!matricula.getSituacao().contains("REP")) {
                    if (matrizDisciplina.getNaturezaDisciplina().equals("OBRIGATORIO")) {
                        aluno.incrementarCargaObrigatoriaCumprida(matrizDisciplina.getDisciplina().getCargaHoraria());
                    } else {
                        aluno.incrementarCargaOptativaCumprida(matrizDisciplina.getDisciplina().getCargaHoraria());
                    }
                }
            }
            //Metodo synchronized
            curso.adicionarAluno(aluno);
            
            lerArq.close();
        } catch (FileNotFoundException ex) {
            throw new DataException(ex.getLocalizedMessage());
        } catch (IOException ex) {
            throw new DataException(ex.getLocalizedMessage());
        }
    }
}
