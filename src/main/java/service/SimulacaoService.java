/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import excecoes.SemPeriodoLetivoException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import modelo.Aluno;
import modelo.Curso;
import modelo.Disciplina;
import modelo.Matricula;
import modelo.MatrizDisciplina;

/**
 *
 * @author rafao
 */
public class SimulacaoService {

    public static final String ABAIXO_RECOMENDADO = "Carga horária abaixo do recomendado";
    public static final String RECOMENDADO = "Carga horária recomendada";
    public static final String AVISO_EXCEDENTE = "Carga horária um pouco acima do normal";
    public static final String ACIMA_RECOMENDADO = "Carga horária acima do recomendado!";

    public static final double PROPORCAO_MINIMA_CH = 0.7;
    public static final double PROPORCAO_MAXIMA_IDEAL_CH = 1.1;
    public static final double PROPORCAO_MINIMA_NAO_RECOMENDADA_CH = 1.3;

    private static SimulacaoService instance = new SimulacaoService();
    private DisciplinaService disciplinaService;
    
    
    private Double pesoEstimadoAluno;

    private SimulacaoService() {
        disciplinaService = new DisciplinaService();
    }

    public static SimulacaoService getInstance() {
        return instance;
    }

    /**
     * Função responsável em retornar o peso de uma disciplina em um semestre. A
     * lógica usada leva em consideração a carga horária da disciplina e a média
     * de reprovações
     *
     * @param disciplina
     * @return peso da disciplina no semestre
     */
    public Double coletarPesoDisciplina(Disciplina disciplina) {
        return disciplina.getCargaHoraria() * (1 + disciplinaService.coletarMediaReprovacao(disciplina) / 100);
    }

    public Double coletarPesoSemestre(List<Disciplina> disciplinas) {
        Double pesoSemestre = 0.0;
        for (Disciplina disciplina : disciplinas) {
            pesoSemestre += coletarPesoDisciplina(disciplina);
        }
        return pesoSemestre;
    }

    public Double coletarPesoDoSemestre(List<MatrizDisciplina> disciplinas) {
        List<Disciplina> disciplinasParaCalculo = new ArrayList<>();
        for (MatrizDisciplina matrizDisciplina : disciplinas) {
            disciplinasParaCalculo.add(matrizDisciplina.getDisciplina());
        }
        return coletarPesoSemestre(disciplinasParaCalculo);
    }

    /**
     * Esse método serve simplesmente para fazer o cálculo com base no último ou
     * nos dois últimos semestres Assume-se que o aluno tenha ao menos um
     * período letivo cursado
     *
     * @param aluno
     * @return o peso médio do(s) último(s) período(s) letivo(s)
     */
    private Double calcularPesoMedioDoAluno(Aluno aluno) throws SemPeriodoLetivoException {
        Map<String, List<Disciplina>> matriculasAgrupadasPorPeriodoLetivo = aluno.coletarMatriculasAgrupadasPorPeriodoLetivo(true);
        List<String> periodosLetivosCursados = new ArrayList(matriculasAgrupadasPorPeriodoLetivo.keySet());
        //A partir desse momento, eu sei quais os últimos períodos
        Collections.sort(periodosLetivosCursados);
        List<Disciplina> disciplinasSemestre;
        Double dificuldade = 0.0;
        if (periodosLetivosCursados.isEmpty()) {
            throw new SemPeriodoLetivoException("O aluno é ingressante e deve ter seu peso calculado com base nos demais");
        }
        disciplinasSemestre = matriculasAgrupadasPorPeriodoLetivo.get(periodosLetivosCursados.get(periodosLetivosCursados.size() - 1));
        dificuldade = coletarPesoSemestre(disciplinasSemestre);
        //Tem pelo menos dois semestres cursados: pode fazer a media
        if (periodosLetivosCursados.size() > 1) {
            disciplinasSemestre = matriculasAgrupadasPorPeriodoLetivo.get(periodosLetivosCursados.get(periodosLetivosCursados.size() - 2));
            dificuldade += coletarPesoSemestre(disciplinasSemestre);
            dificuldade /= 2;
        }
        return dificuldade;
    }

    private Double coletarPesoMedioSuportado(Curso curso) {
        int qtdeAlunosParaMedia = 0;
        Double pesoAcumulado = 0.0;
        List<Aluno> alunos = curso.getAlunos();
        for (Aluno aluno : alunos) {
            try {
                pesoAcumulado += calcularPesoMedioDoAluno(aluno);
                qtdeAlunosParaMedia++;
            } catch (SemPeriodoLetivoException ex) {
                System.out.println("Aluno sem periodo letivo, e assim nao estará na conta do peso medio");
            }
        }
        return pesoAcumulado / qtdeAlunosParaMedia;
    }

    /**
     * Carrega o peso estimado para o aluno suportar no semestre Deve ser
     * chamado assim que a tela de sugestões for carregada
     *
     * @param aluno
     * @return a instancia do aluno
     */
    public Aluno carregarPesoMaximoParaAluno(Aluno aluno) {
        List<Matricula> matriculas = aluno.getMatriculas();
        //Se tiver vazia, eh pq eh um aluno ingressante, entao devemos calcular a media dos outros
        if (matriculas.isEmpty()) {
            this.pesoEstimadoAluno = coletarPesoMedioSuportado(aluno.getCurso());
        } else {
            try {
                this.pesoEstimadoAluno = calcularPesoMedioDoAluno(aluno);
            } catch (SemPeriodoLetivoException ex) {
                System.err.println("Houve erro ao calcular peso médio do aluno");
            }
        }
        return aluno;
    }

    /**
     * Retorna a recomendação do semestre em relação ao aluno Deve ser chamada
     * sempre que um aluno colocar ou retirar uma disciplina da simulação
     *
     * @param disciplinas
     * @return uma string com a recomendação do semestre para o aluno (pouca
     * matéria, ideal, um pouco acima ou muito acima)
     */
    public String coletarRecomendacaoSemestre(Aluno aluno, List<MatrizDisciplina> disciplinas) {
        if(this.pesoEstimadoAluno == null){
            carregarPesoMaximoParaAluno(aluno);
        }
        Double pesoSemestre = coletarPesoDoSemestre(disciplinas);
        if (pesoSemestre < PROPORCAO_MINIMA_CH * this.pesoEstimadoAluno) {
            return ABAIXO_RECOMENDADO;
        } else if (pesoSemestre <= PROPORCAO_MAXIMA_IDEAL_CH * this.pesoEstimadoAluno) {
            return RECOMENDADO;
        } else if (pesoSemestre < PROPORCAO_MINIMA_NAO_RECOMENDADA_CH * this.pesoEstimadoAluno) {
            return AVISO_EXCEDENTE;
        } else {
            return ACIMA_RECOMENDADO;
        }
    }
}
