package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Curso {

    private String nome;
    private Map<String,MatrizCurricular> matrizesCurricular;
    private Map<String,Aluno> alunos;
    private Integer cargaHoraria;

    public Curso(String nome) {
        this.nome = nome;
        this.matrizesCurricular = new HashMap<>();
        this.alunos = new HashMap<>();
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
        this.matrizesCurricular.put(matriz.getNomeMatriz(),matriz);
    }

    public Map<String,Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(Map<String,Aluno> alunos) {
        this.alunos = alunos;
    }

    //Synchronized pelo fato de existir várias threads carregando os alunos e adicionando-os no curso
    public synchronized void adicionarAluno(Aluno aluno){
        if(!this.alunos.containsKey(aluno.getNumeroMatricula())){
            this.alunos.put(aluno.getNumeroMatricula(),aluno);
        }
    }
    
    public MatrizDisciplina getDisciplina(String nomeMatriz, String codigoDisciplina){
        MatrizCurricular matriz = this.matrizesCurricular.get(nomeMatriz);
        return matriz.getDisciplina(codigoDisciplina);
    }
    
    public Disciplina getDisciplina(String codigoDisciplina){
        Disciplina[] disciplinas = getDisciplinas();
        for (Disciplina disc: disciplinas){
            if (disc.getCodigo().equals(codigoDisciplina)){
                return disc;
            }
        }
        return null;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }
    
    public Disciplina[] getDisciplinas(){
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
        return disciplinas.toArray(new Disciplina[disciplinas.size()]);
    }
    
    public String[] getDisciplinasToString(){
        List<String> disciplinas = new ArrayList<>();
        Set<String> chaves = matrizesCurricular.keySet();
        MatrizCurricular matriz;
        for (String chave: chaves){
            matriz = matrizesCurricular.get(chave);
            Map<String, MatrizDisciplina> disciplinasNaMatriz = matriz.getDisciplinasNaMatriz();
            Set<String> chavesDisciplinas = disciplinasNaMatriz.keySet();
            MatrizDisciplina matrizDisciplina;
            for (String chaveDisciplina: chavesDisciplinas){
                matrizDisciplina = disciplinasNaMatriz.get(chaveDisciplina);
                if (!disciplinas.contains(matrizDisciplina.getDisciplina().toString())){
                    disciplinas.add(matrizDisciplina.getDisciplina().toString());
                }
            }
        }
        return disciplinas.toArray(new String[disciplinas.size()]);
    }
}
