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
import modelo.Matricula;
import modelo.Turma;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author f200976
 */
public class MatriculaTest {
    private Turma turma;
    private Aluno aluno;
    private AlunoDAO alunoDAO;
    Matricula matricula;
    public MatriculaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws DataException, AutenticacaoException  {
        //Carregar alguma turma de alguma disciplina
//        aluno = new Aluno();
        alunoDAO = AlunoDAO.getInstance();
//        aluno = alunoDAO.carregarAluno("201602345", "ciclano");
//        turma = new Turma("2017.2",aluno.getCurso().getDisciplina(aluno.getMatrizCurricular(), "DIM0600").getDisciplina());
//        matricula = new Matricula(turma, aluno);
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Ignore @Test
    public void testSituacaoAprovada() {
        //Criar testes colocando as notas do aluno e verificando sua situacao apos chamar o metodo de calcularSituacao
        matricula.setNota1(7.0);
        matricula.setNota2(7.0);
        matricula.setNota3(0.0);
        matricula.setNotaRecuperacao(10.0); //so substitui se a media for menor que 7 e tiver alguma nota menor que 3
        matricula.setNumeroPresencas(48); //menor que 48 reprova por falta para matérias de 60
        matricula.calcularSituacao();
        assertTrue(matricula.foiAprovado());
        
        matricula.setNota1(7.0);
        matricula.setNota2(3.0);
        matricula.setNota3(0.0);
        matricula.setNotaRecuperacao(5.0); //so substitui se a media for menor que 7 e tiver alguma nota menor que 3
        matricula.setNumeroPresencas(48); //menor que 48 reprova por falta para matérias de 60
        matricula.calcularSituacao();
        assertTrue(matricula.foiAprovado());
        
        matricula.setNota1(0.0);
        matricula.setNota2(9.0);
        matricula.setNota3(2.0);
        matricula.setNotaRecuperacao(10.0); //so substitui se a media for menor que 7 e tiver alguma nota menor que 3
        matricula.setNumeroPresencas(48); //menor que 48 reprova por falta para matérias de 60
        matricula.calcularSituacao();
        assertTrue(matricula.foiAprovado());
        
        matricula.setNota1(0.0);
        matricula.setNota2(0.0);
        matricula.setNota3(0.0);
        matricula.setNotaRecuperacao(10.0); //so substitui se a media for menor que 7 e tiver alguma nota menor que 3
        matricula.setNumeroPresencas(48); //menor que 48 reprova por falta para matérias de 60
        matricula.calcularSituacao();
        assertFalse(matricula.foiAprovado());
        
        matricula.setNota1(10.0);
        matricula.setNota2(10.0);
        matricula.setNota3(10.0);
        matricula.setNotaRecuperacao(); //so substitui se a media for menor que 7 e tiver alguma nota menor que 3
        matricula.setNumeroPresencas(47); //menor que 48 reprova por falta para matérias de 60
        matricula.calcularSituacao();
        assertFalse(matricula.foiAprovado());
        
        matricula.setNota1(0.0);
        matricula.setNota2(7.0);
        matricula.setNota3(0.0);
        matricula.setNotaRecuperacao(10.0); //so substitui se a media for menor que 7 e tiver alguma nota menor que 3
        matricula.setNumeroPresencas(48); //menor que 48 reprova por falta para matérias de 60
        matricula.calcularSituacao();
        assertFalse(matricula.foiAprovado());
    }
}
