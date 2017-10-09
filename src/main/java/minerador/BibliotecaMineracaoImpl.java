package minerador;

import java.util.List;
import modelo.Curso;
import modelo.MatrizDisciplina;

public class BibliotecaMineracaoImpl implements BibliotecaMineracao {

    private GeradorCSV geradorCSV;
    private AssociadorWeka associador;
    
    public BibliotecaMineracaoImpl() {
        geradorCSV = GeradorCSV.getInstance();
        associador = AssociadorWeka.getInstance();
    }

    @Override
    public void gerarArquivoParaAssociarDisciplinas(Curso curso) {
        geradorCSV.gerarCSVBinarioDosPeriodosLetivos(curso);

    }

    @Override
    public void associarDisciplinasComunsAPeriodoLetivo(List<MatrizDisciplina> disciplinasDisponiveis) {
        associador.associarDisciplinasComunsAPeriodoLetivo(disciplinasDisponiveis);
    }
}
