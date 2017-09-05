package controller;

import excecoes.DataException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.Aluno;
import service.ServiceFacade;
import service.ServiceFacadeFactory;

public class LoginController extends Application {

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private Button btnEntrar;

    private ServiceFacade serviceFacade;

    @FXML
    void entrar(ActionEvent event) {
        Aluno aluno = null;
        serviceFacade = ServiceFacadeFactory.getInstance().getServiceInstance();
        try {
            aluno = serviceFacade.autenticar(txtLogin.getText(), txtSenha.getText());
        } catch (DataException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        if (aluno != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TelaPrincipal.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
//                    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (Exception ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        serviceFacade = ServiceFacadeFactory.getInstance().getServiceInstance();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TelaLogin.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
