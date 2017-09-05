package dados_instituicao;

import modelo.Curso;

//Facade para coleta de dados da instituição
public interface ColetorDados {
	public Curso getCurso(String nomeCurso);
}
