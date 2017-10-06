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

/**
 *
 * @author rafao
 */
public class GeradorCSV {

    public static final String NOME_ARQUIVO_CSV_ASSOCIACAO = "associacao.csv";

    public static void gerarCSVDosPeriodosLetivos(Curso curso) {
        try {
            FileWriter writer = new FileWriter(NOME_ARQUIVO_CSV_ASSOCIACAO);
            Map<String, Aluno> alunos = curso.getAlunos();
            Map<String, List<Disciplina>> matriculasAgrupadasPorPeriodoLetivo;
            String linhaPeriodo;
            for (Aluno aluno : new ArrayList<>(alunos.values())) {
                System.out.println(aluno.getNome());
                matriculasAgrupadasPorPeriodoLetivo = aluno.coletarMatriculasAgrupadasPorPeriodoLetivo(true);
                for (List<Disciplina> disciplinasDoPeriodo : new ArrayList<>(matriculasAgrupadasPorPeriodoLetivo.values())) {
                    linhaPeriodo = "";
                    for (Disciplina disciplina : disciplinasDoPeriodo) {
                        linhaPeriodo += disciplina.getCodigo() + ";";
                    }
                    writer.append(linhaPeriodo.substring(0, linhaPeriodo.length())+"\n");
                }
            }
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(GeradorCSV.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
