/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author f200976
 */
public class ControllerUtil {

    public Stage carregarTela(String caminhoTela, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoTela));
            Parent root = loader.load();
            Scene scene = new Scene(new ScrollPane(root));
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                    System.exit(0);
                }
            });
            stage.show();
            return stage;
        } catch (IOException ex) {
            return null;
        }
    }

    public void criarAlerta(String titulo, String header, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }
}
