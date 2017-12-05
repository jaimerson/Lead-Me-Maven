package main;

import base_dados.DisciplinaDAO;
import base_dados.DocenteDAO;
import controller.ControllerUtil;
import fabricas.Fabrica;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import modelo.Disciplina;
import modelo.Docente;


public class MainApp extends Application {

    ControllerUtil util = new ControllerUtil();
    
    @Override
    public void start(Stage stage) throws Exception {
        CarregadorTelaLogin carregadorTelaLogin = Fabrica.getInstance().getFactory().createCarregadorTelaLogin();
        util.carregarTela("/fxml/"+carregadorTelaLogin.coletarNomeTelaLogin(), "Lead Me - Login");
        DocenteDAO docenteDAO = new DocenteDAO();
        Docente docente = docenteDAO.encontrar(5752204);
        System.out.println("Docente: " + docente.getNome());
//        DisciplinaDAO disciplinaDAO = DisciplinaDAO.getInstance();
//        Disciplina disciplina = disciplinaDAO.encontrar(18715);
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
