/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testes_unidade;

import dados_instituicao.ColetorDadosFacade;
import dados_instituicao.ColetorDadosFactory;
import excecoes.DataException;
import modelo.Aluno;
import modelo.Disciplina;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rafao
 */
public class AlunoTest {
    
    private Aluno aluno;
    static private ColetorDadosFacade coletorFacade;
    @BeforeClass
    public static void setUpClass() {
        coletorFacade = ColetorDadosFactory.getInstance().getColetorInstance();
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws DataException {
        aluno = new Aluno();
        aluno.setNumeroMatricula("201602345");
        coletorFacade.carregarHistoricoAluno(aluno);
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void testDadosGeraisAluno(){
         assertEquals(aluno.getNome(),"FULANO OLIVEIRA COSTA");
         assertEquals(aluno.getIea(),8.1,0.0);
     }
     
     @Test
     public void testCargaHoraria(){
         assertEquals(aluno.getCargaTotalCumprida().intValue(),750);
     }
     
     @Test
     public void testDisciplinasPagas(){
         Disciplina disciplina = new Disciplina();
         disciplina.setCodigo("IMD0038");
         assertTrue(aluno.pagouMateria(disciplina));
         disciplina.setCodigo("IMD0040");
         assertFalse(aluno.pagouMateria(disciplina));
     }
     
     @Test
     public void testMatrizCurricular(){
         assertEquals(aluno.getMatrizCurricular(),"sig");
     }
     
     @Test
     public void testPreRequisito(){
         Disciplina disciplina = aluno.getCurso().getDisciplina(aluno.getMatrizCurricular(), "IMD0040").getDisciplina();
         assertTrue(aluno.podePagar(disciplina));
         disciplina = aluno.getCurso().getDisciplina(aluno.getMatrizCurricular(), "DIM0600").getDisciplina();
         assertFalse(aluno.podePagar(disciplina));
     }
}
