package modelo;

public class MatrizDisciplina {

    private MatrizCurricular matrizCurricular;
    private Disciplina disciplina;
    private String naturezaDisciplina;
    private Integer semestreIdeal;

    public MatrizDisciplina() {

    }

    public MatrizDisciplina(MatrizCurricular matrizCurricular, Disciplina disciplina) {
        this.matrizCurricular = matrizCurricular;
        this.disciplina = disciplina;
    }

    public MatrizCurricular getMatrizCurricular() {
        return matrizCurricular;
    }

    public void setMatrizCurricular(MatrizCurricular matrizCurricular) {
        this.matrizCurricular = matrizCurricular;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public String getNaturezaDisciplina() {
        return naturezaDisciplina;
    }

    public void setNaturezaDisciplina(String naturezaDisciplina) {
        this.naturezaDisciplina = naturezaDisciplina;
    }

    public Integer getSemestreIdeal() {
        return semestreIdeal;
    }

    public void setSemestreIdeal(Integer semestreIdeal) {
        this.semestreIdeal = semestreIdeal;
    }

}
