package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class MatrizCurricular implements Serializable {

    @Id
    private Integer id;
    
    private String nomeMatriz;
    
    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;
    
    @OneToMany(mappedBy = "matrizCurricular")
    private List<MatrizDisciplina> disciplinasNaMatriz;

    public MatrizCurricular() {
    }
    
    public MatrizCurricular(String nomeMatriz) {
        this.nomeMatriz = nomeMatriz;
        this.disciplinasNaMatriz = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeMatriz() {
        return nomeMatriz;
    }

    public void setNomeMatriz(String nomeMatriz) {
        this.nomeMatriz = nomeMatriz;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<MatrizDisciplina> getDisciplinasNaMatriz() {
        return disciplinasNaMatriz;
    }

    public void setDisciplinasNaMatriz(List<MatrizDisciplina> disciplinasNaMatriz) {
        this.disciplinasNaMatriz = disciplinasNaMatriz;
    }

    public String toString(){
        return this.id + " " + this.nomeMatriz;
    }
}
