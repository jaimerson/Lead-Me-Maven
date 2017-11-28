package main;

import controller.ControllerUtil;
import fabricas.Fabrica;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;


public class MainApp extends Application {

    ControllerUtil util = new ControllerUtil();
    
    @Override
    public void start(Stage stage) throws Exception {
        CarregadorTelaLogin carregadorTelaLogin = Fabrica.getInstance().getFactory().createCarregadorTelaLogin();
        util.carregarTela("/fxml/"+carregadorTelaLogin.coletarNomeTelaLogin(), "Lead Me - Login");
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
