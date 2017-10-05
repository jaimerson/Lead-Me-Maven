package controller;

import excecoes.AutenticacaoException;
import excecoes.DataException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.ServiceFacade;
import service.ServiceFacadeFactory;

public class LoginController {

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private Button btnEntrar;

    private ServiceFacade serviceFacade;
    private ControllerUtil util = new ControllerUtil();
    
    @FXML
    void entrar(ActionEvent event) {
        serviceFacade = ServiceFacadeFactory.getInstance().getServiceInstance();
        try {
            serviceFacade.autenticar(txtLogin.getText(), txtSenha.getText());
        } catch (DataException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (AutenticacaoException ex) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Usuário/Senha inválida");
            alert.setHeaderText("Usuário/senha inválida");
            alert.setContentText("Verifique os dados e tente novamente");
            alert.showAndWait();
            return;
        }
        util.carregarTela("/fxml/TelaPrincipal.fxml","Lead Me");
    }

//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        serviceFacade = ServiceFacadeFactory.getInstance().getServiceInstance();
////        util.carregarTela("/fxml/TelaLogin.fxml");
//    }
}