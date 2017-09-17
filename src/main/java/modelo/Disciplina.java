package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Disciplina {

    private String codigo;
    private String nome;
    private Integer cargaHoraria;
    private List<MatrizDisciplina> matrizesRelacionadas;
    
    private String preRequisitos;
//    List<PossibilidadePreRequisito> preRequisitos;
//    List<Disciplina> coRequisitos;
//    List<Disciplina> equivalentes;

    List<Turma> turmas;

    public Disciplina() {
        turmas = new ArrayList<>();
        matrizesRelacionadas = new ArrayList<>();
    }

    public Disciplina(String codigo, String nome, Integer cargaHoraria) {
        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        turmas = new ArrayList<>();
        matrizesRelacionadas = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Turma> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = turmas;
    }

    public List<MatrizDisciplina> getMatrizesRelacionadas() {
        return matrizesRelacionadas;
    }

    public void setMatrizesRelacionadas(List<MatrizDisciplina> matrizesRelacionadas) {
        this.matrizesRelacionadas = matrizesRelacionadas;
    }

    public String getPreRequisitos() {
        return preRequisitos;
    }

    public void setPreRequisitos(String preRequisitos) {
        this.preRequisitos = preRequisitos;
    }
    
    public void adicionarMatrizRelacionada(MatrizCurricular matriz, String natureza, Integer semestreIdeal) {
        MatrizDisciplina matrizRelacionada = new MatrizDisciplina(matriz, this);
        matrizRelacionada.setNaturezaDisciplina(natureza);
        matrizRelacionada.setSemestreIdeal(semestreIdeal);
        this.matrizesRelacionadas.add(matrizRelacionada);
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }
    
    public Curso getCurso(){
        return this.matrizesRelacionadas.get(0).getMatrizCurricular().getCurso();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        final Disciplina other = (Disciplina) obj;
        return this.codigo.equals(other.getCodigo());
    }

    @Override
    public String toString() {
        return nome + " - " + codigo;
    }

}
