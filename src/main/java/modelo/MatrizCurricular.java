package modelo;

import java.util.HashMap;
import java.util.Map;

public class MatrizCurricular {

    private String nomeMatriz;
    private Curso curso;
    private Map<String,MatrizDisciplina> disciplinasNaMatriz;
    
    public MatrizCurricular(String nomeMatriz) {
        this.nomeMatriz = nomeMatriz;
        this.disciplinasNaMatriz = new HashMap<String,MatrizDisciplina>();
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

    public Map<String,MatrizDisciplina> getDisciplinasNaMatriz() {
        return disciplinasNaMatriz;
    }

    public void setDisciplinasNaMatriz(Map<String,MatrizDisciplina> disciplinasNaMatriz) {
        this.disciplinasNaMatriz = disciplinasNaMatriz;
    }

    public void adicionarDisciplina(Disciplina disciplina, String natureza, Integer semestreIdeal) {
        MatrizDisciplina disciplinaRelacionada = new MatrizDisciplina(this, disciplina);
        disciplinaRelacionada.setNaturezaDisciplina(natureza);
        disciplinaRelacionada.setSemestreIdeal(semestreIdeal);
        this.disciplinasNaMatriz.put(disciplinaRelacionada.getDisciplina().getCodigo(),disciplinaRelacionada);
        disciplina.adicionarMatrizRelacionada(disciplinaRelacionada);
    }
    
    public MatrizDisciplina getDisciplina(String codigo){
        return disciplinasNaMatriz.get(codigo);
    }
}
