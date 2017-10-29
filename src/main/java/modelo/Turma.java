package modelo;

import java.util.ArrayList;
import java.util.List;
import sincronizacao.RecursoCompartilhado;

public class Turma {

    private String periodoLetivo;
    private String numeroTurma;
    private Disciplina disciplina;
    private List<Matricula> matriculas;
    
    private RecursoCompartilhado recurso;
    
    public Turma(){
        this.matriculas = new ArrayList<>();
        this.numeroTurma = "T01";
        this.recurso = new RecursoCompartilhado();
    }

    public Turma(String periodoLetivo, Disciplina disciplina) {
        this.periodoLetivo = periodoLetivo;
        this.disciplina = disciplina;
        this.matriculas = new ArrayList<>();
        this.numeroTurma = "T01";
        this.recurso = new RecursoCompartilhado();
    }

    public String getPeriodoLetivo() {
        return periodoLetivo;
    }

    public void setPeriodoLetivo(String periodoLetivo) {
        this.periodoLetivo = periodoLetivo;
    }

    public String getNumeroTurma() {
        return numeroTurma;
    }

    public void setNumeroTurma(String numeroTurma) {
        this.numeroTurma = numeroTurma;
    }
    
    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }

    //Utiliza o lock para adicionar a matricula na lista
    public Matricula adicionarAluno(Aluno aluno){
        Matricula novaMatricula = new Matricula(this,aluno);
        recurso.requisitarAcesso();
        this.matriculas.add(novaMatricula);
        recurso.liberarAcesso();
        aluno.adicionarMatricula(novaMatricula);
        return novaMatricula;
    }
    
    //Assume-se que a turma ja tenha sido carregada com a lista de matriculas com suas situacoes definidas
    public Double coletarMediaAprovacao(){
        Double aprovados = 0.0;
        for(Matricula matricula: matriculas){
            if (matricula.foiAprovado()){
                aprovados += 1;
            }
        }
        return 100*(aprovados/matriculas.size());
    }
    
    public String toString(){
        return this.disciplina.getCodigo() + " - " + this.periodoLetivo;
    }
}
