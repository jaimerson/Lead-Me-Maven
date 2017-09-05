package modelo;

import java.util.ArrayList;
import java.util.List;

public class Aluno extends Pessoa {

    private String numeroMatricula;
    private Curso curso;
    private List<Matricula> matriculas;

    public Aluno() {
        curso = null;
        matriculas = new ArrayList<>();
    }

    public String getNumeroMatricula() {
        return numeroMatricula;
    }

    public void setNumeroMatricula(String numeroMatricula) {
        this.numeroMatricula = numeroMatricula;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }

}
