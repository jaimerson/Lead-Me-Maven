package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sincronizacao.RecursoCompartilhado;

public class Disciplina{

    private String codigo;
    private String nome;
    private Integer cargaHoraria;
    private List<MatrizDisciplina> matrizesRelacionadas;

    private String preRequisitos;
    private String equivalencias;
    private String coRequisitos;

    Map<String,Turma> turmas;
    
    private RecursoCompartilhado recurso;

    public Disciplina(){
        turmas = new HashMap<>();
        matrizesRelacionadas = new ArrayList<>();
        recurso = new RecursoCompartilhado();
    }

    public Disciplina(String codigo, String nome, Integer cargaHoraria) {
        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        turmas = new HashMap<>();
        matrizesRelacionadas = new ArrayList<>();
        recurso = new RecursoCompartilhado();
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

    public Map<String,Turma> getTurmas() {
        return turmas;
    }

    public void setTurmas(Map<String,Turma> turmas) {
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

    public void adicionarMatrizRelacionada(MatrizDisciplina matrizRelacionada) {
        this.matrizesRelacionadas.add(matrizRelacionada);
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Curso getCurso() {
        return this.matrizesRelacionadas.get(0).getMatrizCurricular().getCurso();
    }

    //proporcao de 60h para 64 presencas
    public Integer getNumeroMaximoPresencas() {
        return (16 * cargaHoraria) / 15;
    }
    
    public void adicionarTurma(Turma turma){
        this.turmas.put(turma.getPeriodoLetivo(), turma);
    }

    //equivalente ao usar synchronized, porem com politica justa (fifo)
    public Turma coletarOuCriarTurma(String periodoLetivo) {
        recurso.requisitarAcesso();
        Turma turma = this.turmas.getOrDefault(periodoLetivo,null);
        if (turma == null){
            turma = new Turma(periodoLetivo,this);
            adicionarTurma(turma);
        }
        recurso.liberarAcesso();
        return turma;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
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
