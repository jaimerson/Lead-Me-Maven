/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    private static String prepararExpressaoRequisitos(Aluno aluno, String requisitos){
        requisitos = requisitos.replace(" E ", "*").replace(" OU ", "+");
        List<Disciplina> disciplinasPagas = aluno.carregarDisciplinasPagas();
        for (Disciplina disciplina: disciplinasPagas){
            requisitos = requisitos.replace(disciplina.getCodigo(), "1");
        }
        //( ( ( 1 ) * ( EST0091 ) ) + ( ( 1 ) * ( EST0091 ) ) + ( ( EST0036 ) * ( EST0039 ) ) )
        requisitos = requisitos.replaceAll("[A-Z]{3}[0-9]{4}", "0");
        //( ( ( 1 ) * ( 0 ) ) + ( ( 1 ) * ( 0 ) ) + ( ( 0 ) * ( 0 ) ) )
        return requisitos;
    }
    
    //Para essa implementação, assumimos que a disciplina ja tem os requisitos carregados
    public static boolean satisfazRequisitos(Aluno aluno, String requisitos){
        if (requisitos == null){
            return true;
        }
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        //( ( ( EST0061 ) E ( EST0091 ) ) OU ( ( EST0114 ) E ( EST0091 ) ) OU ( ( EST0036 ) E ( EST0039 ) ) ) 
        requisitos = prepararExpressaoRequisitos(aluno, requisitos);
        //( ( ( 1 ) * ( 0 ) ) + ( ( 1 ) * ( 0 ) ) + ( ( 0 ) * ( 0 ) ) )
        Integer resultado;
        try {
            // evaluate JavaScript code from String
            resultado = (Integer) engine.eval(requisitos);
            return resultado != 0;
        } catch (ScriptException ex) {
            Logger.getLogger(ProcessadorRequisitos.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
