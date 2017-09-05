package controller;

import dados_instituicao.ColetorDados;
import dados_instituicao.ColetorDadosFactory;
import dados_instituicao.LeitorArquivos;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.Curso;
import service.ServiceFacade;
import service.ServiceFacadeFactory;

public class TelaPrincipalController extends Application {

    @FXML
    private TextField txtBuscarDisciplina;

    @FXML
    private ProgressBar progressBarCurso;

    @FXML
    private Label txtIEA;

    @FXML
    private ComboBox<?> cbDisciplina;

    @FXML
    private PieChart chartAprovacoes;

    @FXML
    private Button btnSimularSemestre;

    @FXML
    private Label txtMCN;

    @FXML
    private Label txtBemVindo;

    @FXML
    private ListView<?> listDisciplinas;

    private ServiceFacade service;
    
    @FXML
    void carregarGrafico(ActionEvent event) {
        
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TelaPrincipal.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
            service = ServiceFacadeFactory.getInstance().getServiceInstance();
            service.carregarCurso("BTI");
//            cbDisciplina.setItems( FXCollections.observableArrayList( ));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
