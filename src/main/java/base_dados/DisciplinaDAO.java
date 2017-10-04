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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.Matricula;
import modelo.Turma;

/**
 *
 * @author rafao
 */
public class DisciplinaDAO extends AbstractDAO{
    
    private static DisciplinaDAO instance = new DisciplinaDAO();
    private static final String SUFIXO_ARQUIVO_TURMAS = " - turmas.csv";
    
    private DisciplinaDAO(){
        
    }
    
    public static DisciplinaDAO getInstance(){
        return instance;
    }
    
    
//    public List<Turma> carregarTurmasDaDisciplina(Disciplina disciplina){
//        //Se por acaso já carregou, nao tem o que fazer
//        if (disciplina.getTurmas() != null && !disciplina.getTurmas().isEmpty()){
//            return new ArrayList<>(disciplina.getTurmas().values());
//        }
//        Map<String,Turma> mapTurmas = new HashMap<>();
//        Map<String,Turma> turmas = new HashMap<>();
//        try {
//            BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream( disciplina.getCurso().getNome() + SUFIXO_ARQUIVO_TURMAS ), "UTF-8"));
//            String linha;
//            String[] dados;
//            String periodoLetivo, numeroDaTurma, codigoDisciplina, matriculaAluno;
//            Double nota1, nota2, nota3, notaRecuperacao;
//            Integer numPresencas;
//            String chaveTurma;
//            Turma turma;
//            Aluno aluno;
//            Matricula matricula;
//            while ((linha = lerArq.readLine()) != null){
//                dados = linha.split(";");
//                periodoLetivo = dados[0];
//                numeroDaTurma = dados[1];
//                codigoDisciplina = dados[2];
//                if (!codigoDisciplina.equals(disciplina.getCodigo())){
//                    continue;
//                }
//                matriculaAluno = dados[3];
//                nota1 = Double.parseDouble(dados[4]);
//                nota2 = Double.parseDouble(dados[5]);
//                nota3 = Double.parseDouble(dados[6]);
//                notaRecuperacao = Double.parseDouble(dados[7]);
//                numPresencas = Integer.parseInt(dados[8]);
//                
//                chaveTurma = periodoLetivo + numeroDaTurma + codigoDisciplina;
//                if (!mapTurmas.containsKey(chaveTurma)){
//                    turma = new Turma(periodoLetivo, disciplina);
//                    mapTurmas.put(chaveTurma, turma);
//                    turmas.add(turma);
//                }
//                else{
//                    turma = mapTurmas.get(chaveTurma);
//                }
//                
//                aluno = new Aluno();
//                aluno.setNumeroMatricula(matriculaAluno);
//                matricula = turma.adicionarAluno(aluno);
//                matricula.setNota1(nota1);
//                matricula.setNota2(nota2);
//                matricula.setNota3(nota3);
//                matricula.setNotaRecuperacao(notaRecuperacao);
//                matricula.setNumeroPresencas(numPresencas);
//                matricula.calcularSituacao();
//            }
//            
//            lerArq.close();
//            disciplina.setTurmas(turmas);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(DisciplinaDAO.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(DisciplinaDAO.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) { 
//            Logger.getLogger(DisciplinaDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return turmas;
//    }
}
