package modelo;

public class Matricula {

    private Turma turma;
    private Aluno aluno;
    private Double porcentagemFrequencia;

    private Double media;
    private String situacao;

    public Matricula(Turma turma, Aluno aluno) {
        this.turma = turma;
        this.aluno = aluno;
        porcentagemFrequencia = 0.0;
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
    
    public Double getPorcentagemFrequencia() {
        return porcentagemFrequencia;
    }

    public void setPorcentagemPresencas(Double numeroPresencas) {
        this.porcentagemFrequencia = numeroPresencas;
    }
    
    public boolean foiAprovado(){
        return situacao.startsWith("APR");
    }
}
