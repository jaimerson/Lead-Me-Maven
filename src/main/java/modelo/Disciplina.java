package modelo;

import java.util.ArrayList;
import java.util.List;

public class Disciplina {

    private String codigo;
    private String nome;
    private Integer cargaHoraria;
    private List<MatrizDisciplina> matrizesRelacionadas;

    List<PossibilidadePreRequisito> preRequisitos;
//    List<Disciplina> coRequisitos;
//    List<Disciplina> equivalentes;

    List<Turma> turmas;

    public Disciplina() {
        turmas = new ArrayList<>();
        matrizesRelacionadas = new ArrayList<>();
        preRequisitos = new ArrayList<>();
//        coRequisitos = new ArrayList<>();
//        equivalentes = new ArrayList<>();
    }

    public Disciplina(String codigo, String nome, Integer cargaHoraria) {
        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        turmas = new ArrayList<>();
        matrizesRelacionadas = new ArrayList<>();
        preRequisitos = new ArrayList<>();
//        coRequisitos = new ArrayList<>();
//        equivalentes = new ArrayList<>();
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

    public void adicionarMatrizRelacionada(MatrizCurricular matriz, String natureza, Integer semestreIdeal) {
        MatrizDisciplina matrizRelacionada = new MatrizDisciplina(matriz, this);
        matrizRelacionada.setNaturezaDisciplina(natureza);
        matrizRelacionada.setSemestreIdeal(semestreIdeal);
        this.matrizesRelacionadas.add(matrizRelacionada);
    }
    
    public void adicionarPossibilidadePreRequisito(PossibilidadePreRequisito possibilidade){
        this.preRequisitos.add(possibilidade);
    }

}
