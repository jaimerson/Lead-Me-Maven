package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Matricula {

    private Turma turma;
    private Aluno aluno;
    private Integer numeroPresencas;

    private Double nota1;
    private Double nota2;
    private Double nota3;
    private Double notaRecuperacao;
    
    private Double media;
    private String situacao;

    public Matricula(Turma turma, Aluno aluno) {
        this.turma = turma;
        this.aluno = aluno;
        numeroPresencas = 0;
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

    public Double getNotaRecuperacao() {
        return notaRecuperacao;
    }

    public void setNotaRecuperacao(Double notaRecuperacao) {
        this.notaRecuperacao = notaRecuperacao;
    }
    public void setNotaRecuperacao() {
        this.notaRecuperacao = 0.0;
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
    
    public Integer getNumeroPresencas() {
        return numeroPresencas;
    }

    public void setNumeroPresencas(Integer numeroPresencas) {
        this.numeroPresencas = numeroPresencas;
    }
    
    //Assume-se que as notas estao carregadas
    //Sao matriculas ja fechadas, com recuperacoes feitas, entao essa funcao deve retornar o estado final (APR, APRN, REPF, REPNF, etc)
    public void calcularSituacao() {
        if (nota1 == null || nota2 == null || nota3 == null || numeroPresencas == null) {
            return;
        }
        double unidade1 = nota1, unidade2 = nota2, unidade3 = nota3;
        media = (unidade1 + unidade2 + unidade3) / 3;

        if (media < 7.0) {
            //Substituir a nota minima abaixo de 3 pela nota de recuperacao
            if (unidade1 < 3 && unidade1 <= unidade2 && unidade1 <= unidade3) {
                unidade1 = notaRecuperacao;
            } else if (unidade2 < 3 && unidade2 <= unidade1 && unidade2 <= unidade3) {
                unidade2 = notaRecuperacao;
            } else if (unidade3 < 3 && unidade3 <= unidade1 && unidade3 <= unidade2) {
                unidade3 = notaRecuperacao;
            }
            media = (unidade1 + unidade2 + unidade3) / 3;
        }
        
        boolean ehAssiduo = ehAssiduo();
        boolean passouPorMedia = media >= 7;
        boolean notasAcimaDeTres = unidade1 >= 3 && unidade2 >= 3 && unidade3 >= 3;
        boolean mediaAcimaDeCinco = media >= 5 && media < 7;

        definirSituacao(ehAssiduo, passouPorMedia, mediaAcimaDeCinco, notasAcimaDeTres);

    }
    
    private boolean ehAssiduo(){
        Double aulasAssistidas = numeroPresencas.doubleValue();
        Double numeroMinimoAulas = turma.getDisciplina().getNumeroMaximoPresencas().doubleValue()*0.75;
        return aulasAssistidas >= numeroMinimoAulas;
    }
    
    private void definirSituacao(boolean ehAssiduo, boolean passouPorMedia, boolean mediaAcimaDeCinco, boolean notasAcimaDeTres){
        //Nao reprova por falta, veremos por nota
        if(ehAssiduo){
            if (passouPorMedia){
                situacao = "APR";
            }
            else if(mediaAcimaDeCinco){
                if (notasAcimaDeTres){
                    situacao = "APRN";
                }
                else{
                    situacao = "REPN";
                }
            }
            else{
                situacao = "REP";
            }
        }
        //Reprovado por falta.. mas pode reprovar tambem por nota
        else{
            if(!passouPorMedia && !mediaAcimaDeCinco){
                situacao = "REPMF";
            }
            else if(mediaAcimaDeCinco && !notasAcimaDeTres){
                situacao = "REPNF";
            }
            else{
                situacao = "REPF";
            }
        }
    }
    
    public boolean foiAprovado(){
        return situacao.startsWith("APR");
    }
}
