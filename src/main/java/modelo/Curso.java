package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import sincronizacao.RecursoCompartilhado;

public class Curso {

    private String nome;
    private Map<String,MatrizCurricular> matrizesCurricular;
    private Map<String,Aluno> alunos;
    private RecursoCompartilhado recurso;

    public Curso(String nome) {
        this.nome = nome;
        this.matrizesCurricular = new HashMap<>();
        this.alunos = new HashMap<>();
        recurso = new RecursoCompartilhado();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Map<String,MatrizCurricular> getMatrizesCurricular() {
        return matrizesCurricular;
    }

    public void setMatrizesCurricular(Map<String,MatrizCurricular> matrizesCurricular) {
        this.matrizesCurricular = matrizesCurricular;
    }

    public void adicionarMatrizCurricular(MatrizCurricular matriz) {
        matriz.setCurso(this);
        recurso.requisitarAcesso();
        this.matrizesCurricular.put(matriz.getNomeMatriz(),matriz);
        recurso.liberarAcesso();
    }

    public Map<String,Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(Map<String,Aluno> alunos) {
        this.alunos = alunos;
    }

    //Equivalente a metodo synchronized
    public void adicionarAluno(Aluno aluno){
        recurso.requisitarAcesso(); //Exclusao mutua
        if(!this.alunos.containsKey(aluno.getNumeroMatricula())){
            this.alunos.put(aluno.getNumeroMatricula(),aluno);
        }
        recurso.liberarAcesso();
    }
    
    public Aluno coletarAluno(String matricula){
        return this.alunos.getOrDefault(matricula, null);
    }
    
    public MatrizDisciplina getDisciplina(String nomeMatriz, String codigoDisciplina){
        MatrizCurricular matriz = this.matrizesCurricular.get(nomeMatriz);
        return matriz.getDisciplina(codigoDisciplina);
    }
    
    public Disciplina getDisciplina(String codigoDisciplina){
        for (Disciplina disc: getDisciplinas()){
            if (disc.getCodigo().equals(codigoDisciplina)){
                return disc;
            }
        }
        return null;
    }

    public List<Disciplina> getDisciplinas(){
        List<Disciplina> disciplinas = new ArrayList<>();
        Set<String> chaves = matrizesCurricular.keySet();
        MatrizCurricular matriz;
        for (String chave: chaves){
            matriz = matrizesCurricular.get(chave);
            Map<String, MatrizDisciplina> disciplinasNaMatriz = matriz.getDisciplinasNaMatriz();
            Set<String> chavesDisciplinas = disciplinasNaMatriz.keySet();
            MatrizDisciplina matrizDisciplina;
            for (String chaveDisciplina: chavesDisciplinas){
                matrizDisciplina = disciplinasNaMatriz.get(chaveDisciplina);
                if (!disciplinas.contains(matrizDisciplina.getDisciplina())){
                    disciplinas.add(matrizDisciplina.getDisciplina());
                }
            }
        }
        return disciplinas;
    }
}
