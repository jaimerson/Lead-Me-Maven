package modelo;

import java.util.ArrayList;
import java.util.List;

public class Professor extends Pessoa {

	private String siape;
	private List<Turma> turmas;
	
 	public Professor() {
 		turmas = new ArrayList<>();
	}

	public String getSiape() {
		return siape;
	}

	public void setSiape(String siape) {
		this.siape = siape;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}
	
}