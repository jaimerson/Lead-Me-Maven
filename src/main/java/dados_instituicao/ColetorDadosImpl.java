package dados_instituicao;

import excecoes.DataException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Curso;

public class ColetorDadosImpl implements ColetorDados {

    public ColetorDadosImpl() {
    }

    @Override
    public Curso getCurso(String nomeCurso) throws DataException{
        Curso curso = null;
        try {
            curso = LeitorArquivos.carregarCursoComGrades(nomeCurso);
        } catch (IOException ex) {
            Logger.getLogger(ColetorDadosImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return curso;
    }

}
