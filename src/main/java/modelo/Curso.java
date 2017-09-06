package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Curso {

    private String nome;
    private Map<String,MatrizCurricular> matrizesCurricular;
    private List<Aluno> alunos;
    private Integer cargaHoraria;

    public Curso(String nome) {
        this.nome = nome;
        this.matrizesCurricular = new HashMap<String,MatrizCurricular>();
        this.alunos = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Map<String,MatrizCurricular> getMatrizesCurricular() {
        return matrizesCurricular;
    }

    public void setMatrizesCurricular(Map<String,MatrizCurricular> matrizesCurricular) {
        this.matrizesCurricular = matrizesCurricular;
    }

    public void adicionarMatrizCurricular(MatrizCurricular matriz) {
        matriz.setCurso(this);
        this.matrizesCurricular.put(matriz.getNomeMatriz(),matriz);
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public void adicionarAluno(Aluno aluno){
        this.alunos.add(aluno);
    }
    
    public MatrizDisciplina getDisciplina(String nomeMatriz, String codigoDisciplina){
        MatrizCurricular matriz = this.matrizesCurricular.get(nomeMatriz);
        return matriz.getDisciplina(codigoDisciplina);
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }
}
