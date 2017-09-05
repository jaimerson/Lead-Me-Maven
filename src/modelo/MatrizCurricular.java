package modelo;

import java.util.ArrayList;
import java.util.List;

public class MatrizCurricular {

	private String nomeMatriz;
	private Curso curso;
	private List<MatrizDisciplina> disciplinasNaMatriz;
	
	public MatrizCurricular(String nomeMatriz) {
		this.nomeMatriz = nomeMatriz;
		this.disciplinasNaMatriz = new ArrayList<>();
	}

	public String getNomeMatriz() {
		return nomeMatriz;
	}

	public void setNomeMatriz(String nomeMatriz) {
		this.nomeMatriz = nomeMatriz;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<MatrizDisciplina> getDisciplinasNaMatriz() {
		return disciplinasNaMatriz;
	}

	public void setDisciplinasNaMatriz(List<MatrizDisciplina> disciplinasNaMatriz) {
		this.disciplinasNaMatriz = disciplinasNaMatriz;
	}
	
	public void adicionarDisciplina(Disciplina disciplina, String natureza, Integer semestreIdeal) {
		MatrizDisciplina disciplinaRelacionada = new MatrizDisciplina(this, disciplina);
		disciplinaRelacionada.setNaturezaDisciplina(natureza);
		disciplinaRelacionada.setSemestreIdeal(semestreIdeal);
		this.disciplinasNaMatriz.add(disciplinaRelacionada);
		disciplina.adicionarMatrizRelacionada(this, natureza, semestreIdeal);
	}
	
}
