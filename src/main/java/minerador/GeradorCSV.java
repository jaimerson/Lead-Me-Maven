/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minerador;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Aluno;
import modelo.Curso;
import modelo.Disciplina;
import modelo.Matricula;

/**
 *
 * @author rafao
 */
public class GeradorCSV {

    public static final String NOME_ARQUIVO_CSV_ASSOCIACAO = "associacao.csv";
    public static final String NOME_ARQUIVO_CSV_ASSOCIACAO_BINARIA = "associacaoBinaria.csv";
    public static final String DELIMITADOR_CSV = ",";
    
    private static GeradorCSV instance = new GeradorCSV();
    
    public static GeradorCSV getInstance(){
        return instance;
    }
    
    private GeradorCSV(){
        
    }

    public void gerarCSVBinarioDosPeriodosLetivos(Curso curso) {
        List<Disciplina> disciplinas = new ArrayList<>();
        String cabecalho = "";
        try {
            FileWriter writer = new FileWriter(NOME_ARQUIVO_CSV_ASSOCIACAO_BINARIA);
            Map<String, List<Disciplina>> matriculasAgrupadasPorPeriodoLetivo;
            Map<String, Aluno> alunos = curso.getAlunos();
            
            for (Aluno aluno: new ArrayList<>(alunos.values())){
                for(Matricula m: aluno.getMatriculas()){
                    //Adiciono apenas a coluna para uma disciplina se ao menos um aluno a pagou
                    if (!disciplinas.contains(m.getTurma().getDisciplina())){
                        disciplinas.add(m.getTurma().getDisciplina());
                        cabecalho += m.getTurma().getDisciplina().getCodigo() + DELIMITADOR_CSV;
                    }
                }
            }
            //Escrevendo o cabecalho
            writer.append(cabecalho.substring(0, cabecalho.length()-1)+"\n");
            
            String linhaPeriodo;
            for (Aluno aluno : new ArrayList<>(alunos.values())) {
                matriculasAgrupadasPorPeriodoLetivo = aluno.coletarMatriculasAgrupadasPorPeriodoLetivo(true);
                for (List<Disciplina> disciplinasDoPeriodo : new ArrayList<>(matriculasAgrupadasPorPeriodoLetivo.values())) {
                    linhaPeriodo = "";
                    for(Disciplina disciplina: disciplinas){
                        linhaPeriodo += disciplinasDoPeriodo.contains(disciplina) ? "y" : "?";
                        linhaPeriodo += DELIMITADOR_CSV;
                    }
                    writer.append(linhaPeriodo.substring(0, linhaPeriodo.length()-1)+"\n");
                }
            }
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(GeradorCSV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
