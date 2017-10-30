package controller;

import excecoes.AutenticacaoException;
import excecoes.DataException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
            util.criarAlerta("Erro ao acessar as informações", "Houve um problema para acessar os dados", 
                    "Detalhes: " + ex.getLocalizedMessage());
            return;
        } catch (AutenticacaoException ex) {
            util.criarAlerta("Usuário/Senha inválida", "Usuário/senha inválida", "Verifique os dados e tente novamente");
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