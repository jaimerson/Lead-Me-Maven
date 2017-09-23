package controller;

import excecoes.DataException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import modelo.Aluno;
import modelo.Curso;
import modelo.Disciplina;
import modelo.MatrizDisciplina;
import service.ServiceFacade;
import service.ServiceFacadeFactory;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.AutoCompletionBinding.AutoCompletionEvent;
import org.controlsfx.control.textfield.TextFields;

public class TelaPrincipalController extends Application implements Initializable {

    @FXML
    private Label lbProgresso;

    @FXML
    private TextField txtBuscarDisciplina;

    @FXML
    private ProgressBar progressBarCurso;

    @FXML
    private Label txtIEA;

    @FXML
    private TextField txtDisciplina;

    @FXML
    private PieChart chartAprovacoes;

    @FXML
    private Label txtMCN;

    @FXML
    private Label txtBemVindo;

    @FXML
    private Label txtTituloPieChart;

    @FXML
    private TableView<MatrizDisciplina> tableDisciplinasDisponiveis;

    private ServiceFacade service;
    //Possibilidades de resultado de busca de disciplina para consulta de estatisticas
    private String[] disciplinas;
    //Quando escolher a disciplina para verificar a taxa de aprovacao
    private Disciplina disciplinaSelecionada;
    private AutoCompletionBinding<String> autoCompleteSimulacao, autoCompleteEstatistica;
    private ControllerUtil util = new ControllerUtil();
    
    @Override
    public void start(Stage stage) throws Exception {
        util.carregarTela("/fxml/TelaPrincipal.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        service = ServiceFacadeFactory.getInstance().getServiceInstance();
        final Aluno alunoLogado = service.coletarAlunoLogado();
        disciplinas = service.carregarDisciplinasDoCursoToString(alunoLogado.getCurso());

        /*estatistica*/
        autoCompleteEstatistica = TextFields.bindAutoCompletion(txtDisciplina, disciplinas);
        autoCompleteEstatistica.setOnAutoCompleted(new EventHandler<AutoCompletionEvent<String>>() {

            @Override
            public void handle(AutoCompletionEvent<String> event) {
                disciplinaSelecionada = service.carregarDisciplina(alunoLogado.getCurso(),event.getCompletion());
                carregarGraficoAprovacoes();
                event.consume();
            }
        });
        /*simulação*/
        final ObservableList<String> items = FXCollections.observableArrayList();
        autoCompleteSimulacao = TextFields.bindAutoCompletion(txtBuscarDisciplina, disciplinas);
        autoCompleteSimulacao.setOnAutoCompleted(new EventHandler<AutoCompletionEvent<String>>() {

            @Override
            public void handle(AutoCompletionEvent<String> event) {
                items.add(event.getCompletion());
//                listDisciplinas.setItems(items);
                txtBuscarDisciplina.clear();
                event.consume();
            }
        });
        //Tela inicial
        carregarInformacoesAluno(alunoLogado);
        //Tela de estatísticas
        disciplinaSelecionada = service.carregarDisciplina(alunoLogado.getCurso(),"IMD0040");
        txtDisciplina.setText(disciplinaSelecionada.toString());
        carregarGraficoAprovacoes();
        //Tela de sugestoes/simulacoes
        carregarSugestoes();
    }

    private void carregarInformacoesAluno(Aluno alunoLogado) {
        txtBemVindo.setText("Bem vindo(a), " + alunoLogado.getNome());
        Double progresso = alunoLogado.getProgresso();
        progressBarCurso.setProgress(progresso);
        lbProgresso.setText("Progresso no curso: " + String.format("%.2f", progresso * 100) + "%");
        txtIEA.setText(alunoLogado.getIea().toString());
        txtMCN.setText(alunoLogado.getMcn().toString());
    }

    private void carregarGraficoAprovacoes() {
        Double aprovacoes;
        Aluno alunoLogado = service.coletarAlunoLogado();
        try {
            aprovacoes = service.coletarMediaAprovacao(alunoLogado.getCurso(),disciplinaSelecionada);
            ObservableList<PieChart.Data> dadosPieChart = FXCollections.observableArrayList(
                    new PieChart.Data("Aprovados: " + String.format("%.2f", aprovacoes) + "%", aprovacoes),
                    new PieChart.Data("Reprovados: " + String.format("%.2f", 100.0 - aprovacoes) + "%", 100.0 - aprovacoes));
            chartAprovacoes.setData(dadosPieChart);
            txtTituloPieChart.setText("Aprovações de " + disciplinaSelecionada.getNome());
            
            
        } catch (DataException ex) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao acessar os dados");
            alert.setContentText("Houve um problema ao coletar a média da aprovação");
            alert.showAndWait();
        }
        finally{
            txtDisciplina.clear();
        }
    }

    private void carregarSugestoes() {
        Aluno alunoLogado = service.coletarAlunoLogado();
        List<MatrizDisciplina> disciplinasDisponiveis = service.carregarDisciplinasDisponiveis(alunoLogado.getCurso());
        ObservableList<MatrizDisciplina> listaObs = FXCollections.observableList(disciplinasDisponiveis);
        tableDisciplinasDisponiveis.setItems(listaObs);
        TableColumn<MatrizDisciplina, String> codigoTabela = new TableColumn<MatrizDisciplina, String>("Código");
        TableColumn<MatrizDisciplina, String> nomeTabela = new TableColumn<MatrizDisciplina, String>("Nome");
        TableColumn<MatrizDisciplina, String> naturezaTabela = new TableColumn<MatrizDisciplina, String>("Natureza");
        TableColumn<MatrizDisciplina, Integer> semestreTabela = new TableColumn<MatrizDisciplina, Integer>("Semestre");
        codigoTabela.setCellValueFactory(new Callback<CellDataFeatures<MatrizDisciplina, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<MatrizDisciplina, String> c) {
                return new SimpleStringProperty(c.getValue().getDisciplina().getCodigo());
            }
        });
        nomeTabela.setCellValueFactory(new Callback<CellDataFeatures<MatrizDisciplina, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<MatrizDisciplina, String> c) {
                return new SimpleStringProperty(c.getValue().getDisciplina().getNome());
            }
        });
        naturezaTabela.setCellValueFactory(new PropertyValueFactory("naturezaDisciplina"));
        semestreTabela.setCellValueFactory(new PropertyValueFactory("semestreIdeal"));
        tableDisciplinasDisponiveis.getColumns().setAll(codigoTabela, nomeTabela, naturezaTabela, semestreTabela);
    }
    
    public void carregarDisciplinasMaisDificeis(Curso curso){
        try {
            List<Disciplina> disciplinasMaisDificeis = service.coletarDisciplinasMaisDificeis(curso);
            //Colocar em uma tabela
            
        } catch (DataException ex) {
            Logger.getLogger(TelaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
