package test;

import java.io.IOException;
import java.util.List;

import dados_instituicao.LeitorArquivos;
import modelo.Curso;
import modelo.MatrizCurricular;

public class TestLeituraDados {

    public static void main(String[] args) {

        try {
            Curso curso = LeitorArquivos.carregarCursoComGrades("BTI");
            System.out.println();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
