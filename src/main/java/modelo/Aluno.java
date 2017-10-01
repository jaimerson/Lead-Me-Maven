package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import service.ProcessadorRequisitos;

public class Aluno extends Pessoa {

    public static final Double MEDIA_APROVACAO = 5.0;
    
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
    //CUMPRE EQUIVALENCIA DEVE CHAMAR A FUNÇÃO DE SATISFAZREQUISITOS SEM USAR AS EQUIVALENCIAS, PQ N EH UMA PROPRIEDADE TRANSITIVA NA UFRN
    public boolean cumpreEquivalencia(Disciplina disciplina){
        return ProcessadorRequisitos.satisfazRequisitos(this, disciplina.getEquivalencias(),false, false);
    }
    
    public boolean pagouMateria(Disciplina disciplina, boolean considerarEquivalentes){
        for(Matricula matricula: this.matriculas){
            if (matricula.getTurma().getDisciplina().equals(disciplina) && matricula.getSituacao().contains("APR")){
                return true;
            }
        }
        //Se nao pagou a disciplina em questao, verificamos se pagou a equivalencia
        return considerarEquivalentes && cumpreEquivalencia(disciplina);
    }
    
    public boolean podePagar(Disciplina disciplina){
        return !pagouMateria(disciplina,true) && ProcessadorRequisitos.satisfazRequisitos(this, disciplina.getPreRequisitos(),true, true);
    }
    
    public List<Disciplina> carregarDisciplinasPagas(){
        List<Disciplina> disciplinasPagas = new ArrayList<>();
        for(Matricula matricula: this.matriculas){
            if (matricula.getMedia() >= MEDIA_APROVACAO){
                disciplinasPagas.add(matricula.getTurma().getDisciplina());
            }
        }
        return disciplinasPagas;
    }
    
    public List<Matricula> coletarMatriculasDoPeriodo(String periodoLetivo){
        List<Matricula> matriculasDoPeriodo = new ArrayList<>();
        for (Matricula matricula: matriculas){
            if (matricula.getTurma().getPeriodoLetivo().equals(periodoLetivo)){
                matriculasDoPeriodo.add(matricula);
            }
        }
        return matriculasDoPeriodo;
    }
    
    public Map<String,List<Disciplina>> coletarMatriculasAgrupadasPorPeriodoLetivo(boolean apenasAprovados){
        Map<String,List<Disciplina>> disciplinasAgrupadas = new HashMap<>();
        
        List<Matricula> matriculas = new ArrayList<>();
        List<Disciplina> disciplinasDoPeriodo;
        String periodoLetivo;
        for (Matricula matricula: matriculas){
            if (apenasAprovados && !matricula.foiAprovado()){
                continue;
            }
            periodoLetivo = matricula.getTurma().getPeriodoLetivo();
            if (!disciplinasAgrupadas.containsKey(periodoLetivo)){
                disciplinasAgrupadas.put(periodoLetivo, new ArrayList<Disciplina>());
            }
            disciplinasDoPeriodo = disciplinasAgrupadas.get(periodoLetivo);
            disciplinasDoPeriodo.add(matricula.getTurma().getDisciplina());
            disciplinasAgrupadas.put(periodoLetivo, disciplinasDoPeriodo);
        }
        return disciplinasAgrupadas;
    }

}
