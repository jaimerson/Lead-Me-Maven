package modelo;

import java.util.ArrayList;
import java.util.List;

public class Matricula {

    private Turma turma;
    private Aluno aluno;
    private List<Frequencia> historicoFrequencia;

    private Double nota1;
    private Double nota2;
    private Double nota3;
    
    private Double media;

    public Matricula(Turma turma, Aluno aluno) {
        this.turma = turma;
        this.aluno = aluno;
        historicoFrequencia = new ArrayList<>();
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Double getNota1() {
        return nota1;
    }

    public void setNota1(Double nota1) {
        this.nota1 = nota1;
    }

    public Double getNota2() {
        return nota2;
    }

    public void setNota2(Double nota2) {
        this.nota2 = nota2;
    }

    public Double getNota3() {
        return nota3;
    }

    public void setNota3(Double nota3) {
        this.nota3 = nota3;
    }

    public List<Frequencia> getHistoricoFrequencia() {
        return historicoFrequencia;
    }

    public void setHistoricoFrequencia(List<Frequencia> historicoFrequencia) {
        this.historicoFrequencia = historicoFrequencia;
    }

    public Double getMedia() {
        return media;
    }

    public void setMedia(Double media) {
        this.media = media;
    }

}
