/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

/**
 *
 * @author f200976
 */
public class ControllerUtil {
    
    public Stage carregarTela(String caminhoTela, String titulo){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoTela));
            Parent root = loader.load();
            Scene scene = new Scene(new ScrollPane(root));
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.show();
            return stage;
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
