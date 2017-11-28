/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import conexao_api_sinfo.ConsumidorAPI;
import static conexao_api_sinfo.ConsumidorAPI.URL_AUTENTICACAO_SINFO;
import static conexao_api_sinfo.ConsumidorAPI.URL_ENCAMINHA;
import dto.DiscenteUfrnDTO;
import dto.UsuarioUfrnDTO;
import excecoes.DataException;
import fabricas.Fabrica;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import modelo.Aluno;
import modelo.Curso;
import org.json.JSONException;
import service.AlunoService;
import service.CursoService;
import service.LoginService;
import service.ServiceFacade;
import service.ServiceFacadeImpl;

/**
 *
 * @author rafao
 */
public class LoginUFRNController {

    @FXML
    private WebView webViewLogin;

    private ServiceFacade service;
    private ControllerUtil util = new ControllerUtil();
    private ConsumidorAPI consumidor;
    private boolean clicouEmLogar = false;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        service = new ServiceFacadeImpl();
        consumidor = ConsumidorAPI.getInstance();
        WebEngine engine = webViewLogin.getEngine();
        engine.load(URL_AUTENTICACAO_SINFO);
        engine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
            public void handle(WebEvent<String> event) {
                if (event.getSource() instanceof WebEngine) {
                    WebEngine we = (WebEngine) event.getSource();
                    String location = we.getLocation();
                    if (location.startsWith(URL_ENCAMINHA) && location.contains("code") && !clicouEmLogar) {
                        try {
                            consumidor.carregarAccessToken(location);
                            carregarAlunoParaService();
                            clicouEmLogar = true;
                            util.carregarTela("/fxml/TelaPrincipal.fxml", "Lead Me");
                            
                            ((Stage) webViewLogin.getScene().getWindow()).close();
                        } catch (JSONException ex) {
                            Logger.getLogger(LoginUFRNController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(LoginUFRNController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (DataException ex) {
                            Logger.getLogger(LoginUFRNController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
    }

    private void carregarAlunoParaService() throws DataException {
        UsuarioUfrnDTO usuarioLogado = consumidor.coletarUsuarioLogado();
        DiscenteUfrnDTO ultimoVinculo = consumidor.carregarUltimoVinculo(usuarioLogado.getCpfCnpj().toString());
        Integer id = ultimoVinculo.getIdDiscente();
        Integer idCurso = ultimoVinculo.getIdCurso();
        CursoService cursoService = new CursoService();
        Curso curso = cursoService.carregarCurso(idCurso);
        Aluno aluno = new Aluno();
        aluno.setId(id.toString());
        aluno.setNome(usuarioLogado.getNomePessoa());
        aluno.setCurso(curso);
        LoginService loginService = LoginService.getInstance();
        AlunoService alunoService = Fabrica.getInstance().getFactory().createAlunoService();
        alunoService.carregarMatriculasDoAluno(aluno);
        System.out.println("Carregou as matriculas do aluno");
        loginService.setAluno(aluno);
    }
}
