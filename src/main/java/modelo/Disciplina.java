package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

@Entity
public class Disciplina implements Serializable{

    @Id
    private Integer id;
    
    private String codigo;
    private String nome;
    private Integer cargaHoraria;
    
    @Lob
    @Column
    private String preRequisitos;
    
    @Lob
    @Column
    private String equivalencias;
    
    @Lob
    @Column
    private String coRequisitos;
    
    @OneToMany(mappedBy = "disciplina")
    private List<MatrizDisciplina> matrizesRelacionadas;

    @OneToMany(mappedBy = "disciplina")
    List<Turma> turmas;
    
    public Disciplina(){
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    
    //equivalente ao usar synchronized, porem com politica justa (fifo)
    public Turma coletarTurma(String periodoLetivo) {
        for (Turma turma: this.turmas){
            if(turma.getPeriodoLetivo().equals(periodoLetivo)){
                return turma;
            }
        }
        return null;
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
