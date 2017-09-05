package modelo;

import java.util.ArrayList;
import java.util.List;

public class Turma {

	private String periodoLetivo;
	private Disciplina disciplina;
	private List<Matricula> matriculas;
	private Professor professor;
	
	public Turma(String periodoLetivo, Disciplina disciplina) {
		this.periodoLetivo = periodoLetivo;
		this.disciplina = disciplina;
		this.matriculas = new ArrayList<>();
	}
	public String getPeriodoLetivo() {
		return periodoLetivo;
	}
	public void setPeriodoLetivo(String periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}
	public Disciplina getDisciplina() {
		return disciplina;
	}
	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}
	public List<Matricula> getMatriculas() {
		return matriculas;
	}
	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}
	public Professor getProfessor() {
		return professor;
	}
	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
}
