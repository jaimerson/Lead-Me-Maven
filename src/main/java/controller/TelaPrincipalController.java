package controller;

import excecoes.DataException;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
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
import modelo.Matricula;
import modelo.MatrizCurricular;
import modelo.MatrizDisciplina;
import modelo.Turma;
import service.ServiceFacade;
import service.ServiceFacadeFactory;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.AutoCompletionBinding.AutoCompletionEvent;
import org.controlsfx.control.textfield.TextFields;
import service.ProcessadorRequisitos;

public class TelaPrincipalController implements Initializable {

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

    @FXML
    private TableView<Disciplina> tabelaDisciplinasDificeis;

    @FXML
    private ScatterChart<Integer, Double> graficoFrequenciaNotas;

    @FXML
    private ComboBox<String> cbTurmas;

    @FXML
    private Tab tabUmaDisciplina;

    @FXML
    private ComboBox<MatrizCurricular> cbGradeCurricular;

    @FXML
    private ListView<MatrizDisciplina> listDisciplinasSelecionadas;

    @FXML
    private Button btnAdicionarDisciplina;

    @FXML
    private Button btnRemoverDisciplina;

    @FXML
    private Label lbRecomendacao;

    @FXML
    private Button btnSair;

    private ServiceFacade service;
    //Possibilidades de resultado de busca de disciplina para consulta de estatisticas
    private String[] disciplinas;
    //Quando escolher a disciplina para verificar a taxa de aprovacao
    private Disciplina disciplinaSelecionadaEstatistica;
    private AutoCompletionBinding<String> autoCompleteSimulacao, autoCompleteEstatistica;
    private ControllerUtil util = new ControllerUtil();

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
                disciplinaSelecionadaEstatistica = service.carregarDisciplina(alunoLogado.getCurso(), event.getCompletion());
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
        disciplinaSelecionadaEstatistica = service.carregarDisciplina(alunoLogado.getCurso(), "IMD0040");
        txtDisciplina.setText(disciplinaSelecionadaEstatistica.toString());
        carregarGraficoAprovacoes();
        carregarScatterDaTurma();
        //Tela de sugestoes/simulacoes
        carregarSugestoes();
        carregarDisciplinasMaisDificeis(alunoLogado.getCurso());
    }

    @FXML
    void carregarGraficosTurma(ActionEvent event) {
        carregarScatterDaTurma();
    }

    public void carregarScatterDaTurma() {
        String opcaoSelecionada = cbTurmas.getSelectionModel().getSelectedItem();
        if (opcaoSelecionada == null) {
            return;
        }
        String periodoLetivo = opcaoSelecionada;
        Turma turmaSelecionada = service.coletarTurma(disciplinaSelecionadaEstatistica, periodoLetivo);
        if (turmaSelecionada == null) {
            System.err.println("Turma selecionada eh nula!");
            return;
        }
        XYChart.Series<Integer, Double> series = new XYChart.Series<>();
        for (Matricula matricula : turmaSelecionada.getMatriculas()) {
            series.getData().add(new XYChart.Data(matricula.getPorcentagemFrequencia(), matricula.getMedia()));
        }
        graficoFrequenciaNotas.getData().setAll(series);
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
            aprovacoes = service.coletarMediaAprovacao(alunoLogado.getCurso(), disciplinaSelecionadaEstatistica);
            ObservableList<PieChart.Data> dadosPieChart = FXCollections.observableArrayList(
                    new PieChart.Data("Aprovados: " + String.format("%.2f", aprovacoes) + "%", aprovacoes),
                    new PieChart.Data("Reprovados: " + String.format("%.2f", 100.0 - aprovacoes) + "%", 100.0 - aprovacoes));
            chartAprovacoes.setData(dadosPieChart);
            txtTituloPieChart.setText("Aprovações de " + disciplinaSelecionadaEstatistica.getNome());
            carregarListaTurmasSelecionavel(disciplinaSelecionadaEstatistica.coletarTurmasPeriodoToString());

        } catch (DataException ex) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao acessar os dados");
            alert.setContentText("Houve um problema ao coletar a média da aprovação");
            alert.showAndWait();
        } finally {
            txtDisciplina.clear();
        }
    }

    private void carregarListaTurmasSelecionavel(List<String> turmasSelecionaveis) {
        ObservableList<String> listaObs = FXCollections.observableArrayList(turmasSelecionaveis);
        cbTurmas.setItems(listaObs);
        cbTurmas.getSelectionModel().select(0);
    }

    private void carregarSugestoes() {
        Aluno alunoLogado = service.coletarAlunoLogado();
        service.carregarPesoMaximoParaAluno(alunoLogado);
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

        tableDisciplinasDisponiveis.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (event.getClickCount() == 2) {
                    adicionarDisciplinaParaSimulacao(null);
                }
            }

        });
        listDisciplinasSelecionadas.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (event.getClickCount() == 2) {
                    removerDisciplinaParaSimulacao(null);
                }
            }

        });
    }

    public void carregarDisciplinasMaisDificeis(Curso curso) {
        try {
            List<Disciplina> disciplinasMaisDificeis = service.coletarDisciplinasMaisDificeis(curso);
            ObservableList<Disciplina> listaDisciplinasTabela = FXCollections.observableList(disciplinasMaisDificeis);
            tabelaDisciplinasDificeis.setItems(listaDisciplinasTabela);
            TableColumn<Disciplina, String> colunaNomeDisciplina = new TableColumn<>("Nome");
            colunaNomeDisciplina.setSortable(false);
            colunaNomeDisciplina.setEditable(false);
            TableColumn<Disciplina, String> colunaAprovacoes = new TableColumn<>("% aprovados");
            colunaAprovacoes.setSortable(false);
            colunaAprovacoes.setEditable(false);
            colunaNomeDisciplina.setCellValueFactory(new PropertyValueFactory("nome"));
            colunaAprovacoes.setCellValueFactory(new Callback<CellDataFeatures<Disciplina, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<Disciplina, String> c) {
                    return new SimpleStringProperty(String.format("%.1f", c.getValue().coletarMediaAprovacao()));
                }

            });
            tabelaDisciplinasDificeis.getColumns().setAll(colunaNomeDisciplina, colunaAprovacoes);

        } catch (DataException ex) {
            Logger.getLogger(TelaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void adicionarDisciplinaParaSimulacao(ActionEvent event) {
        MatrizDisciplina selectedItem = tableDisciplinasDisponiveis.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (ProcessadorRequisitos.cumpreCoRequisitos(service.coletarAlunoLogado(), selectedItem, listDisciplinasSelecionadas.getItems())) {
                listDisciplinasSelecionadas.getItems().add(selectedItem);
                tableDisciplinasDisponiveis.getItems().remove(selectedItem);
                lbRecomendacao.setText(service.coletarRecomendacaoSemestre(listDisciplinasSelecionadas.getItems()));
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Aviso");
                alert.setHeaderText("Você não atendeu os co-requisitos");
                alert.setContentText("Para inserir a disciplina " + selectedItem.getDisciplina().getCodigo() + ", deverá cumprir os co-requisitos: " + selectedItem.getDisciplina().getCoRequisitos());
                alert.showAndWait();
            }
        }
    }

    @FXML
    void removerDisciplinaParaSimulacao(ActionEvent event) {
        MatrizDisciplina selectedItem = listDisciplinasSelecionadas.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            listDisciplinasSelecionadas.getItems().remove(selectedItem);
            tableDisciplinasDisponiveis.getItems().add(selectedItem);
            Iterator<MatrizDisciplina> it = listDisciplinasSelecionadas.getItems().iterator();
            while(it.hasNext()){
                MatrizDisciplina disciplina = it.next();
                if (!ProcessadorRequisitos.cumpreCoRequisitos(service.coletarAlunoLogado(), disciplina, listDisciplinasSelecionadas.getItems())) {
                    it.remove();
                    tableDisciplinasDisponiveis.getItems().add(disciplina);
                }
            }
            Collections.sort(tableDisciplinasDisponiveis.getItems());
            if (!listDisciplinasSelecionadas.getItems().isEmpty()) {
                lbRecomendacao.setText(service.coletarRecomendacaoSemestre(listDisciplinasSelecionadas.getItems()));
            }   
            else{
                lbRecomendacao.setText("Selecione as disciplinas para simulação");
            }
        }
    }

    @FXML
    void sair(ActionEvent event) {
        ((Stage) btnSair.getScene().getWindow()).close();
    }
}
