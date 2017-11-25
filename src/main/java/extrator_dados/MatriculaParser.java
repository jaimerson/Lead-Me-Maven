/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extrator_dados;

import base_dados.AlunoDAO;
import base_dados.CursoDAO;
//import base_dados.MatriculaDAO;
import base_dados.TurmaDAO;
import static extrator_dados.ExtratorUFRN.prepararLinha;
import javax.persistence.PersistenceException;
import modelo.Aluno;
import modelo.Curso;
import modelo.Matricula;
import modelo.Turma;

/**
 *
 * @author rafao
 */
public class MatriculaParser implements Runnable {

    String[] dadosMatricula;

    private TurmaDAO turmaDAO;
    private CursoDAO cursoDAO;
    private AlunoDAO alunoDAO;
//    private MatriculaDAO matriculaDAO;

    public MatriculaParser(String linha) {
        linha = prepararLinha(linha);
        this.dadosMatricula = linha.split(ExtratorUFRN.SEPARADOR_CSV);
        turmaDAO = TurmaDAO.getInstance();
        cursoDAO = CursoDAO.getInstance();
        alunoDAO = AlunoDAO.getInstance();
//        matriculaDAO = MatriculaDAO.getInstance();
    }

    @Override
    public void run() {
        Aluno aluno;
        Matricula matricula;
        Turma turma;
        Curso curso;
        if (dadosMatricula.length < 10 || !ExtratorUFRN.camposValidos(dadosMatricula, 0, 1, 2, 4, 6, 7, 8, 9)) {
            return;
        }
        if (!dadosMatricula[9].contains("APROVADO") && !dadosMatricula[9].contains("REPROVADO")) {
            return;
        }
        aluno = new Aluno();
        matricula = new Matricula();
        aluno.setId(dadosMatricula[1]);
        turma = turmaDAO.encontrar(Integer.parseInt(dadosMatricula[0]));
        curso = cursoDAO.encontrar(Integer.parseInt(dadosMatricula[2]));
        if (turma == null) {
            System.out.println("Turma nula");
            return;
        }
        if(curso == null){
            System.out.println("Curso nulo");
            return;
        }
        aluno.setCurso(curso);
        try{
            aluno = alunoDAO.salvar(aluno);
            System.out.println("Salvou aluno");
        }catch(PersistenceException e){
            System.out.println(e.getMessage());
            System.err.println("NAO SALVOU O ALUNO");
            aluno = alunoDAO.encontrar(aluno.getId());
            if(aluno == null){
                System.err.println(" >>>>> Nao achou o aluno que tbm nao conseguiu inserir!! <<<<<<");
                return;
            }
        }
        matricula.setAluno(aluno);
        matricula.setTurma(turma);
        matricula.setMedia(Double.parseDouble(dadosMatricula[7]));
        matricula.setNumeroFaltas(Double.parseDouble(dadosMatricula[8]));
        matricula.setSituacao(dadosMatricula[9]);
        try{
//            matriculaDAO.salvar(matricula);
            System.out.println("Salvou matricula");
        }catch(PersistenceException e){
            System.err.println("NAO SALVOU A MATRICULA");
        }
//        fecharConexoes();
    }
    
    private void fecharConexoes(){
        alunoDAO.fecharConexao();
//        matriculaDAO.fecharConexao();
        cursoDAO.fecharConexao();
        turmaDAO.fecharConexao();
    }

}
