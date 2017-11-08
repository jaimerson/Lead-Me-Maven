/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import api.ConsumidorAPI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import org.json.JSONException;
import service.ServiceFacade;
import service.ServiceFacadeImpl;

/**
 *
 * @author rafao
 */
public class LoginWebViewController {

    @FXML
    private WebView webViewLogin;
    
    private final static String CLIENT_ID = "integra-ti-id";
    private final static String CLIENT_SECRET = "segredo";
    private final static String X_PI_KEY = "ABXsOnkQx7juN3GYNXGR6lu3CthmHnAuzGIxqEv6";
    private final static String URL_ENCAMINHA = "http://www.google.com.br";
    public static String URL_AUTENTICACAO_SINFO = "https://apitestes.info.ufrn.br//authz-server/oauth/authorize?client_id="+ CLIENT_ID +"&response_type=code&redirect_uri="+ URL_ENCAMINHA;
    
    private ServiceFacade service;
    private ControllerUtil util = new ControllerUtil();
    private ConsumidorAPI consumidor = new ConsumidorAPI();
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        service = new ServiceFacadeImpl();
        WebEngine engine = webViewLogin.getEngine();
        engine.load(URL_AUTENTICACAO_SINFO);
        
        engine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
            public void handle(WebEvent<String> event) {
                if (event.getSource() instanceof WebEngine) {
                    WebEngine we = (WebEngine) event.getSource();
                    String location = we.getLocation();
                     if (location.startsWith(URL_ENCAMINHA) && location.contains("code")) {
                        try {
                            String accessToken = consumidor.carregarAccessToken(location);
                            System.out.println(consumidor.coletarInfoTeste());
//                            util.carregarTela("/fxml/TelaPrincipal.fxml", "Lead Me");
//                            ((Stage) webViewLogin.getScene().getWindow()).close();
                        } catch (JSONException ex) {
                            Logger.getLogger(LoginWebViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
    }
}
