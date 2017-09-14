package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Aluno extends Pessoa {

    private String numeroMatricula;
    private Curso curso;
    private String matrizCurricular;
    private List<Matricula> matriculas;
    private Integer cargaObrigatoriaCumprida;
    private Integer cargaOptativaCumprida;
    private Double iea;
    private Double mcn;
    
    public Aluno() {
        curso = null;
        matriculas = new ArrayList<>();
        cargaObrigatoriaCumprida = 0;
        cargaOptativaCumprida = 0;
    }

    public String getNumeroMatricula() {
        return numeroMatricula;
    }

    public void setNumeroMatricula(String numeroMatricula) {
        this.numeroMatricula = numeroMatricula;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public String getMatrizCurricular() {
        return matrizCurricular;
    }

    public void setMatrizCurricular(String matrizCurricular) {
        this.matrizCurricular = matrizCurricular;
    }
    
    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }
    
    public void adicionarMatricula(Matricula matricula){
        this.matriculas.add(matricula);
    }

    public Integer getCargaObrigatoriaCumprida() {
        return cargaObrigatoriaCumprida;
    }

    public void setCargaObrigatoriaCumprida(Integer cargaObrigatoriaCumprida) {
        this.cargaObrigatoriaCumprida = cargaObrigatoriaCumprida;
    }

    public Integer getCargaOptativaCumprida() {
        return cargaOptativaCumprida;
    }

    public void setCargaOptativaCumprida(Integer cargaOptativaCumprida) {
        this.cargaOptativaCumprida = cargaOptativaCumprida;
    }
    
    public Integer getCargaTotalCumprida(){
        return this.cargaObrigatoriaCumprida + this.cargaOptativaCumprida;
    }

    public Double getIea() {
        return iea;
    }

    public void setIea(Double iea) {
        this.iea = iea;
    }

    public Double getMcn() {
        return mcn;
    }

    public void setMcn(Double mcn) {
        this.mcn = mcn;
    }
    
    public Double getProgresso(){
        Integer cargaHorariaCumprida = getCargaTotalCumprida();
        Set<String> keys = this.curso.getMatrizesCurricular().keySet();
        MatrizCurricular matriz = null;
        for (String key: keys){
            matriz = this.curso.getMatrizesCurricular().get(key);
        }
        if(matriz != null){    
            Integer cargaHorariaTotal = matriz.getCargaTotal();
            return cargaHorariaCumprida.doubleValue()/cargaHorariaTotal;
        }
        else{
            return 0.0;
        }
    }
    
    public boolean pagouMateria(Disciplina disciplina){
        for(Matricula matricula: this.matriculas){
            if (matricula.getTurma().getDisciplina().equals(disciplina) && matricula.getSituacao().contains("APR")){
                return true;
            }
        }
        return false;
    }
    
    public boolean podePagar(Disciplina disciplina){
        //Primeiro, devemos verificar se a disciplina já n foi paga
        for(Matricula matricula: this.matriculas){
            if (matricula.getTurma().getDisciplina().equals(disciplina)){
                return false;
            }
        }
        List<PossibilidadePreRequisito> preRequisitos = disciplina.getPreRequisitos();
        //Para cada possibilidade de preRequisito
        boolean podePagar = true;
        boolean cumpriuPossibilidade;
        for(PossibilidadePreRequisito possibilidade: preRequisitos){
            podePagar = false;
            cumpriuPossibilidade = true;
            List<Disciplina> disciplinasPossibilidade = possibilidade.getPreRequisitos();
            //Para cada disciplina da possibilidade
            for (Disciplina disc: disciplinasPossibilidade){
                //Se o nao aluno pagou a materia, não satisfaz a possibilidade de pre requisito
                if (!this.pagouMateria(disc)){
                    cumpriuPossibilidade = false;
                    break;
                }
            }
            if(cumpriuPossibilidade){
                return true;
            }
        }
        return podePagar;
    }

}
