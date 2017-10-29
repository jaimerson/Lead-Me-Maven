package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Aluno{

    public static final Double MEDIA_APROVACAO = 5.0;
    
    private String nome;
    private String numeroMatricula;
    private Curso curso;
    private String matrizCurricular;
    private List<Matricula> matriculas;
    
    public Aluno() {
        curso = null;
        matriculas = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getMatrizCurricular() {
        return matrizCurricular;
    }

    public void setMatrizCurricular(String matrizCurricular) {
        this.matrizCurricular = matrizCurricular;
    }
    
    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }
    
    public void adicionarMatricula(Matricula matricula){
        this.matriculas.add(matricula);
    }
    
    public Map<String,List<Disciplina>> coletarMatriculasAgrupadasPorPeriodoLetivo(boolean apenasAprovados){
        Map<String,List<Disciplina>> disciplinasAgrupadas = new HashMap<>();
        List<Disciplina> disciplinasDoPeriodo;
        String periodoLetivo;
        for (Matricula matricula: matriculas){
            if (apenasAprovados && !matricula.foiAprovado()){
                continue;
            }
            periodoLetivo = matricula.getTurma().getPeriodoLetivo();
            if (!disciplinasAgrupadas.containsKey(periodoLetivo)){
                disciplinasAgrupadas.put(periodoLetivo, new ArrayList<Disciplina>());
            }
            disciplinasDoPeriodo = disciplinasAgrupadas.get(periodoLetivo);
            disciplinasDoPeriodo.add(matricula.getTurma().getDisciplina());
            disciplinasAgrupadas.put(periodoLetivo, disciplinasDoPeriodo);
        }
        return disciplinasAgrupadas;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.numeroMatricula);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Aluno other = (Aluno) obj;
        return this.numeroMatricula.equals(other.numeroMatricula);
    }
    
    

}
