package modelo;

import java.io.Serializable;

public class Matricula implements Serializable{

    private Integer id;
    
    private Turma turma;
    
    private Aluno aluno;
    
    private Double numeroFaltas;

    private Double media;
    private String situacao;

    public Matricula() {
    }

    public Matricula(Turma turma, Aluno aluno) {
        this.turma = turma;
        this.aluno = aluno;
        numeroFaltas = 0.0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Double getMedia() {
        return media;
    }

    public void setMedia(Double media) {
        this.media = media;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
    
    public Double getNumeroFaltas() {
        return numeroFaltas;
    }

    public void setNumeroFaltas(Double numeroPresencas) {
        this.numeroFaltas = numeroPresencas;
    }
    
    //TODO remover metodo e usar somente o metodo do service
    public boolean situacaoAprovada(){
        return situacao.startsWith("APR");
    }
}
