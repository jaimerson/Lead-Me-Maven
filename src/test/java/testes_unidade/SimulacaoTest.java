/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testes_unidade;

import base_dados.AlunoDAO;
import excecoes.AutenticacaoException;
import excecoes.DataException;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.MatrizDisciplina;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import service.LoginService;
import service.SimulacaoService;

/**
 *
 * @author rafao
 */
public class SimulacaoTest {

    private static Aluno aluno;
    private static SimulacaoService simulacaoService;
    private static LoginService loginService;
    private static Disciplina disciplina;
    public SimulacaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws DataException, AutenticacaoException {
        aluno = new Aluno();
      //  alunoDAO = AlunoDAO.getInstance();
        simulacaoService = SimulacaoService.getInstance();
        loginService = LoginService.getInstance();
         aluno = loginService.autenticar("201602345", "ciclano");
         
        
      
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
      @Test 
    public void testColetarPesoDisciplina(){
        disciplina = new Disciplina("IMD0028", "FUNDAMENTOS MATEM�TICOS DA COMPUTAÇÃO II", 90 );
        
         Double pesoDisciplina = simulacaoService.coletarPesoDisciplina(disciplina);
         
        assertTrue(90 <= pesoDisciplina);
    }
    
   @Test
    public void testCargaHorariaPonderada(){
        MatrizDisciplina matrizDisciplina = aluno.getCurso().getDisciplina(aluno.getMatrizCurricular(), "IMD0038");
        Double pesoPonderado = simulacaoService.coletarPesoDisciplina(matrizDisciplina.getDisciplina());
        System.out.println(pesoPonderado);
        assertTrue(pesoPonderado > matrizDisciplina.getDisciplina().getCargaHoraria());
    }
    
  
 
}
