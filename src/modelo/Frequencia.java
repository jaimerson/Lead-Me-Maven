package modelo;

import java.util.Date;
import java.util.GregorianCalendar;

public class Frequencia {
	private Date dataAula;
	private int qtdeFaltas;
	
	public Frequencia() {
		dataAula = new Date();
		qtdeFaltas = 0;
	}

	public Date getDataAula() {
		return dataAula;
	}

	public void setDataAula(Date dataAula) {
		this.dataAula = dataAula;
	}

	public int getQtdeFaltas() {
		return qtdeFaltas;
	}

	public void setQtdeFaltas(int qtdeFaltas) {
		this.qtdeFaltas = qtdeFaltas;
	}
	
}
