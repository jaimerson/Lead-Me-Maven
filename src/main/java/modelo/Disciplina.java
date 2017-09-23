package modelo;

import java.util.ArrayList;
import java.util.List;

public class Disciplina {

    private String codigo;
    private String nome;
    private Integer cargaHoraria;
    private List<MatrizDisciplina> matrizesRelacionadas;
    
    private String preRequisitos;
    private String equivalencias;
    private String coRequisitos;

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

    public String getEquivalencias() {
        return equivalencias;
    }

    public void setEquivalencias(String equivalencias) {
        this.equivalencias = equivalencias;
    }

    public String getCoRequisitos() {
        return coRequisitos;
    }

    public void setCoRequisitos(String coRequisitos) {
        this.coRequisitos = coRequisitos;
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
    
    //proporcao de 60h para 64 presencas
    public Integer getNumeroMaximoPresencas(){
        return (16*cargaHoraria)/15;
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
