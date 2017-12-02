/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demandas_disciplinas;

import excecoes.DataException;
import modelo.Curso;
import service.CursoService;

/**
 *
 * @author rafael
 */
public class DemandasDisciplinas {
    
    
    //Executar por fora apenas para gerar o JSON para colocar no HTML de demandas
    public static void main(String[] args) throws DataException{
        CursoService cursoService = new CursoService();
        Curso curso = cursoService.carregarCurso(92127264);
        String jsonDemandas = cursoService.coletarJSONDemandasDisciplinas(curso);
        System.out.println(jsonDemandas);
    }
}
