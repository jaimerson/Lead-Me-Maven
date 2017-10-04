/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_dados;

import excecoes.DataException;
import excecoes.AutenticacaoException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import modelo.MatrizDisciplina;
import modelo.Turma;

/**
 *
 * @author rafao
 */
public class AlunoDAO extends AbstractDAO {

    private static AlunoDAO instance = new AlunoDAO();
    private CursoDAO cursoDAO;

    private AlunoDAO() {
        cursoDAO = CursoDAO.getInstance();
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

    public String carregarNomeCurso(String matriculaAluno) {
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
            Logger.getLogger(AlunoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AlunoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AlunoDAO.class.getName()).log(Level.SEVERE, null, ex);
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

//    public Aluno carregarAluno(String usuario, String senha) throws DataException, AutenticacaoException {
//        if (!existeUsuario(usuario, senha)) {
//            throw new AutenticacaoException("Usuário e/ou senha inválidos");
//        }
//        Aluno aluno = new Aluno();
//        aluno.setNumeroMatricula(usuario);
//        carregarHistoricoAluno(aluno);
//        return aluno;
//    }
    
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
                
                //Contando as horas cumpridas  
                if (!matricula.getSituacao().contains("REP")) {
                    if (matrizDisciplina.getNaturezaDisciplina().equals("OBRIGATORIO")) {
                        aluno.setCargaObrigatoriaCumprida(aluno.getCargaObrigatoriaCumprida() + matrizDisciplina.getDisciplina().getCargaHoraria());
                    } else {
                        aluno.setCargaOptativaCumprida(aluno.getCargaOptativaCumprida() + matrizDisciplina.getDisciplina().getCargaHoraria());
                    }
                }
            }
            //Metodo synchronized
            curso.adicionarAluno(aluno);
            lerArq.close();
        } catch (FileNotFoundException ex) {
            System.err.println("FileNotFoundException");
        } catch (IOException ex) {
            System.err.println("IOException");
        }
    }

    private void carregarHistoricoAluno(Aluno aluno) throws DataException {
        try {
            BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream(DIRETORIO_RECURSOS + "historicos/" + aluno.getNumeroMatricula() + "-historico.txt"), "UTF-8"));
            aluno.setNome(lerArq.readLine());
            lerArq.readLine(); //ignorando a matricula
            String cursoComMatriz = lerArq.readLine();
//            String nomeCurso = cursoComMatriz.split(" - ")[0];
            String matrizCurricular = cursoComMatriz.split(" - ")[1];
//            Curso curso = cursoDAO.carregarCurso(nomeCurso);
//            aluno.setCurso(curso);
//            curso.adicionarAluno(aluno);
            aluno.setMatrizCurricular(matrizCurricular);
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
                matricula.setSituacao(dadosDisciplina[5]);
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
}
