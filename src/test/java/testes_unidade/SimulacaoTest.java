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
import modelo.MatrizDisciplina;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import service.SimulacaoService;

/**
 *
 * @author rafao
 */
public class SimulacaoTest {

    private static Aluno aluno;
    private static AlunoDAO alunoDAO;
    private static SimulacaoService simulacaoService;
    
    public SimulacaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws DataException, AutenticacaoException {
        aluno = new Aluno();
        alunoDAO = AlunoDAO.getInstance();
        aluno = alunoDAO.carregarAluno("201602345", "ciclano");
        simulacaoService = SimulacaoService.getInstance();
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
    public void testCargaHorariaPonderada(){
        MatrizDisciplina matrizDisciplina = aluno.getCurso().getDisciplina(aluno.getMatrizCurricular(), "IMD0038");
        Double pesoPonderado = simulacaoService.coletarPesoDisciplina(matrizDisciplina.getDisciplina());
        System.out.println(pesoPonderado);
        assertTrue(pesoPonderado > matrizDisciplina.getDisciplina().getCargaHoraria());
    }
}
