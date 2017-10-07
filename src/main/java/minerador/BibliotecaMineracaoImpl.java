package minerador;

import modelo.Curso;

public class BibliotecaMineracaoImpl implements BibliotecaMineracao {

    public GeradorCSV geradorCSV;
    
    public BibliotecaMineracaoImpl() {
        geradorCSV = GeradorCSV.getInstance();
    }

    @Override
    public void gerarArquivoParaAssociarDisciplinas(Curso curso) {
        geradorCSV.gerarCSVBinarioDosPeriodosLetivos(curso);
    }
}
