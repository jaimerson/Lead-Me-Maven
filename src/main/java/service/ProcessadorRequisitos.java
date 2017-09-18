/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import modelo.Aluno;
import modelo.Disciplina;

/**
 *
 * @author rafao
 */
public class ProcessadorRequisitos {

    public static List<String> coletarDisciplinasDaExpressao(String expressao) {
        List<String> allMatches = new ArrayList<>();
        Matcher m = Pattern.compile("[A-Z]{3}[0-9]{4}")
                .matcher(expressao);
        while (m.find()) {
            allMatches.add(m.group());
        }
        return allMatches;
    }

    private static String prepararExpressaoRequisitos(Aluno aluno, String requisitos, boolean explorarEquivalentes) {
        requisitos = requisitos.replace(" E ", "*").replace(" OU ", "+");
        //Se tiver as equivalentes na expressao de pre requisitos, esse trecho eh desnecessario
        List<String> codigosExpressao = coletarDisciplinasDaExpressao(requisitos);
        for (String codigo: codigosExpressao){
            Disciplina disciplina = aluno.getCurso().getDisciplina(codigo);
            if (disciplina == null){
                System.out.println("DISCIPLINA NUUULAAAAAAAA");
                requisitos = requisitos.replace(codigo,"0");
            }
            else if (aluno.pagouMateria(disciplina,explorarEquivalentes)){
                requisitos = requisitos.replace(codigo, "1");
            }
            else{
                requisitos = requisitos.replace(codigo,"0");
            }
        }
        return requisitos;
    }

    //Para essa implementação, assumimos que a disciplina ja tem os requisitos carregados
    public static boolean satisfazRequisitos(Aluno aluno, String requisitos, boolean cumpreAutomatico, boolean exploraEquivalentes) {
        if (requisitos == null || requisitos.isEmpty()) {
            return cumpreAutomatico;
        }
        //( ( ( EST0061 ) E ( EST0091 ) ) OU ( ( EST0114 ) E ( EST0091 ) ) OU ( ( EST0036 ) E ( EST0039 ) ) ) 
        requisitos = prepararExpressaoRequisitos(aluno, requisitos, exploraEquivalentes);
        //( ( ( 1 ) * ( 0 ) ) + ( ( 1 ) * ( 0 ) ) + ( ( 0 ) * ( 0 ) ) )
        return calcularExpressao(requisitos) > 0;
    }
    
    //Nessa função, assume-se que a expressao possua apenas 1 e 0 com parenteses, * e +
    private static int calcularExpressao(String expressao){
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        //( ( ( EST0061 ) E ( EST0091 ) ) OU ( ( EST0114 ) E ( EST0091 ) ) OU ( ( EST0036 ) E ( EST0039 ) ) ) 
        //( ( ( 1 ) * ( 0 ) ) + ( ( 1 ) * ( 0 ) ) + ( ( 0 ) * ( 0 ) ) )
        Integer resultado;
        try {
            // evaluate JavaScript code from String
            resultado = (Integer) engine.eval(expressao);
            return resultado;
        } catch (ScriptException ex) {
            Logger.getLogger(ProcessadorRequisitos.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
    
    public static boolean cumpreCoRequisito(Aluno aluno, String codigoDisciplina, List<String> codigosSimulacao){
        Disciplina disciplina = aluno.getCurso().getDisciplina(codigoDisciplina);
        String coRequisitos = disciplina.getCoRequisitos();
        for (String codigoSimulacao: codigosSimulacao){
            if(!codigoSimulacao.equals(codigoDisciplina)){
                coRequisitos = coRequisitos.replace(codigoSimulacao, "1");
            }
        }
        coRequisitos = prepararExpressaoRequisitos(aluno, coRequisitos, false);
        return calcularExpressao(coRequisitos) > 0;
    }
}
