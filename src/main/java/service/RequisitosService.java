/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import fabricas.Fabrica;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.Matricula;
import modelo.MatrizDisciplina;

/**
 *
 * @author rafao
 */
public abstract class RequisitosService {
    
    MatriculaService matriculaService;
    
    public RequisitosService(){
        matriculaService = new MatriculaService();
    }
    
    public boolean pagouMateria(Aluno aluno, Disciplina disciplina, boolean considerarEquivalentes){
        for(Matricula matricula: aluno.getMatriculas()){
            if (matricula.getTurma().getDisciplina().equals(disciplina) && matriculaService.situacaoAprovada(matricula)){
                return true;
            }
        }
        //Se nao pagou a disciplina em questao, verificamos se pagou a equivalencia
        return considerarEquivalentes && cumpreEquivalencia(aluno,disciplina);
    }
    
    public boolean cumpreEquivalencia(Aluno aluno, Disciplina disciplina){
        return satisfazRequisitos(aluno, disciplina.getEquivalencias(), false, false);
    }
    
    public boolean podePagar(Aluno aluno, Disciplina disciplina){
        return !pagouMateria(aluno,disciplina,true) && satisfazRequisitos(aluno, disciplina.getPreRequisitos(),true, true);
    }
    
    //Aqui pode ser aplicado um template method, pois o procedimento eh o mesmo, tirando o fato de ter padroes diferentes
    //dependendo da instancia do framework
    public List<String> coletarDisciplinasDaExpressao(String expressao) {
        List<String> allMatches = new ArrayList<>();
        Matcher m = Pattern.compile(coletarRegexCodigoDisciplina())
                .matcher(expressao);
        while (m.find()) {
            allMatches.add(m.group());
        }
        return allMatches;
    }
    
    //UFRN, estacio e outras instituicoes - "[A-Z]{3}[0-9]{4}"
    abstract public String coletarRegexCodigoDisciplina();
    
    //UFRN - requisitos.replace(" E ", "*").replace(" OU ", "+");
    abstract public String coletarExpressaoRequisitosComOperadores(String requisitos);
    
    
    //Aqui tambem pode ser aplicado o template method
    //Pois o tratamento da expressao depende da instituicao
    private String prepararExpressaoRequisitos(Aluno aluno, String requisitos, boolean explorarEquivalentes) {
        requisitos = coletarExpressaoRequisitosComOperadores(requisitos);
        //Se tiver as equivalentes na expressao de pre requisitos, esse trecho eh desnecessario
        List<String> codigosExpressao = coletarDisciplinasDaExpressao(requisitos);
        for (String codigo: codigosExpressao){
            Disciplina disciplina = aluno.getCurso().coletarDisciplina(codigo);
            if (disciplina == null || !pagouMateria(aluno,disciplina,explorarEquivalentes)){
                requisitos = requisitos.replace(codigo,"0");
            }
            else{
                requisitos = requisitos.replace(codigo, "1");
            }
        }
        return requisitos;
    }
    
    public boolean satisfazRequisitos(Aluno aluno, String requisitos, boolean cumpreAutomatico, boolean exploraEquivalentes) {
        if (requisitos == null || requisitos.isEmpty()) {
            return cumpreAutomatico;
        }
        requisitos = prepararExpressaoRequisitos(aluno, requisitos, exploraEquivalentes);
        return calcularExpressao(requisitos) > 0;
    }
    
    private int calcularExpressao(String expressao){
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        Integer resultado;
        try {
            // evaluate JavaScript code from String
            resultado = (Integer) engine.eval(expressao);
            return resultado;
        } catch (ScriptException ex) {
            return -1;
        }
    }
    
    public boolean cumpreCoRequisitos(Aluno aluno, MatrizDisciplina disciplinaAAdicionar, List<MatrizDisciplina> disciplinasM){
        String coRequisitos = disciplinaAAdicionar.getDisciplina().getCoRequisitos();
        if (coRequisitos == null || coRequisitos.isEmpty() || coRequisitos.equals(" ")){
            return true;
        }
        //Se ja pagou a disciplina do co requisito, ta de boa
        if (satisfazRequisitos(aluno, coRequisitos, true, false)){
            return true;
        }
        //Senao, precisa verificar se as disciplinas que estao na lista do semestre satisfazem
        for (MatrizDisciplina disciplinaM: disciplinasM){
            if(coRequisitos.contains(disciplinaM.getDisciplina().getCodigo())){
                coRequisitos = coRequisitos.replace(disciplinaM.getDisciplina().getCodigo(), "1");
            }
        }
        coRequisitos = coRequisitos.replaceAll(coletarRegexCodigoDisciplina(), "0");
        return calcularExpressao(coRequisitos) > 0;
    }
}
