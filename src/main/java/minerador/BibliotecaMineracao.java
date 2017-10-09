package minerador;

import java.util.List;
import modelo.Curso;
import modelo.MatrizDisciplina;

public interface BibliotecaMineracao {
    public void gerarArquivoParaAssociarDisciplinas(Curso curso);
    public void associarDisciplinasComunsAPeriodoLetivo(List<MatrizDisciplina> disciplinasDisponiveis);
}
