package controller;

import excecoes.AutenticacaoException;
import excecoes.DataException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import service.ServiceFacade;
import service.ServiceFacadeImpl;

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
        serviceFacade = new ServiceFacadeImpl();
        try {
            serviceFacade.autenticar(txtLogin.getText(), txtSenha.getText());
        } catch (DataException ex) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Erro ao acessar as informações");
            alert.setHeaderText("Houve um problema para acessar os dados");
            alert.setContentText("Detalhes: " + ex.getLocalizedMessage());
            alert.showAndWait();
            return;
        } catch (AutenticacaoException ex) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Usuário/Senha inválida");
            alert.setHeaderText("Usuário/senha inválida");
            alert.setContentText("Verifique os dados e tente novamente");
            alert.showAndWait();
            return;
        }
        Stage stage = util.carregarTela("/fxml/TelaPrincipal.fxml","Lead Me");
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                util.carregarTela("/fxml/TelaLogin.fxml", "Lead Me - Login");
            }
        });
        ((Stage) btnEntrar.getScene().getWindow()).close();
    }
}