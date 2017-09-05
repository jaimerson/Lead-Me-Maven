package modelo;

import java.util.ArrayList;
import java.util.List;

public class Curso {

    private String nome;
    private List<MatrizCurricular> matrizesCurricular;

    public Curso(String nome) {
        this.nome = nome;
        this.matrizesCurricular = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<MatrizCurricular> getMatrizesCurricular() {
        return matrizesCurricular;
    }

    public void setMatrizesCurricular(List<MatrizCurricular> matrizesCurricular) {
        this.matrizesCurricular = matrizesCurricular;
    }

    public void adicionarMatrizCurricular(MatrizCurricular matriz) {
        matriz.setCurso(this);
        this.matrizesCurricular.add(matriz);
    }

}
