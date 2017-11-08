package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import modelo.Aluno;
import modelo.Curso;
import modelo.Disciplina;
import modelo.Matricula;
import modelo.MatrizDisciplina;
import modelo.Turma;
import service.ServiceFacade;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.AutoCompletionBinding.AutoCompletionEvent;
import org.controlsfx.control.textfield.TextFields;
import service.ServiceFacadeImpl;

public class TelaPrincipalController implements Initializable {

    @FXML
    private TextField txtBuscarDisciplina;

    @FXML
    private TextField txtDisciplina;

    @FXML
    private PieChart chartAprovacoes;

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
    private ComboBox<Turma> cbTurmas;

    @FXML
    private Tab tabUmaDisciplina;

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
    private List<Disciplina> disciplinas;
    //Quando escolher a disciplina para verificar a taxa de aprovacao
    private Disciplina disciplinaSelecionadaEstatistica;
    private AutoCompletionBinding<Disciplina> autoCompleteEstatistica;
    private ControllerUtil util = new ControllerUtil();
    private List<MatrizDisciplina> disciplinasDisponiveis;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        service = new ServiceFacadeImpl();
        Aluno alunoLogado = service.coletarAlunoLogado();
        disciplinas = service.carregarDisciplinasDoCurso(alunoLogado.getCurso());

        /*estatistica*/
        autoCompleteEstatistica = TextFields.bindAutoCompletion(txtDisciplina, disciplinas);
        autoCompleteEstatistica.setOnAutoCompleted(new EventHandler<AutoCompletionEvent<Disciplina>>() {

            @Override
            public void handle(AutoCompletionEvent<Disciplina> event) {
                disciplinaSelecionadaEstatistica = event.getCompletion();
                carregarGraficoAprovacoes();
                event.consume();
            }
        });

        //Tela de estatísticas
        carregarGraficoAprovacoes();
        carregarScatterDaTurma();
        //Tela de sugestoes/simulacoes
        carregarSugestoes(alunoLogado);
        carregarDisciplinasMaisDificeis(alunoLogado.getCurso());
    }

    @FXML
    void carregarGraficosTurma(ActionEvent event) {
        carregarScatterDaTurma();
    }

    public void carregarScatterDaTurma() {
        Turma turmaSelecionada = cbTurmas.getSelectionModel().getSelectedItem();
        if (turmaSelecionada == null || disciplinaSelecionadaEstatistica == null) {
            return;
        }
        XYChart.Series<Integer, Double> series = new XYChart.Series<>();
        for (Matricula matricula : turmaSelecionada.getMatriculas()) {
            series.getData().add(new XYChart.Data(matricula.getPorcentagemFrequencia(), matricula.getMedia()));
        }
        graficoFrequenciaNotas.getData().setAll(series);
    }

    private void carregarGraficoAprovacoes() {
        Double aprovacoes;
        if (disciplinaSelecionadaEstatistica == null) {
            return;
        }
        aprovacoes = service.coletarMediaAprovacao(disciplinaSelecionadaEstatistica);
        ObservableList<PieChart.Data> dadosPieChart = FXCollections.observableArrayList(
                new PieChart.Data("Aprovados: " + String.format("%.2f", aprovacoes) + "%", aprovacoes),
                new PieChart.Data("Reprovados: " + String.format("%.2f", 100.0 - aprovacoes) + "%", 100.0 - aprovacoes));
        chartAprovacoes.setData(dadosPieChart);
        txtTituloPieChart.setText("Aprovações de " + disciplinaSelecionadaEstatistica.getNome());
        carregarListaTurmasSelecionavel(new ArrayList<>(disciplinaSelecionadaEstatistica.getTurmas().values()));
        txtDisciplina.clear();
    }

    private void carregarListaTurmasSelecionavel(List<Turma> turmasSelecionaveis) {
        ObservableList<Turma> listaObs = FXCollections.observableArrayList(turmasSelecionaveis);
        cbTurmas.setItems(listaObs);
        cbTurmas.getSelectionModel().select(0);
    }

    private void criarTabelaDeSugestoes() {
        TableColumn<MatrizDisciplina, String> codigoTabela = new TableColumn<>("Código");
        TableColumn<MatrizDisciplina, String> nomeTabela = new TableColumn<>("Nome");
        TableColumn<MatrizDisciplina, String> naturezaTabela = new TableColumn<>("Natureza");
        TableColumn<MatrizDisciplina, String> semestreTabela = new TableColumn<>("Semestre");
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
        semestreTabela.setCellValueFactory(new Callback<CellDataFeatures<MatrizDisciplina, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<MatrizDisciplina, String> c) {
                //Ele exibirá "-" caso a optativa nao essteja associada com nenhum periodo letivo e tiver MAX_VALUE como valor
                return new SimpleStringProperty(c.getValue().getSemestreIdeal() == Integer.MAX_VALUE ? "-" : c.getValue().getSemestreIdeal().toString());
            }
        });
        tableDisciplinasDisponiveis.getColumns().setAll(codigoTabela, nomeTabela, naturezaTabela, semestreTabela);
        adicionarEventosCliqueTabela();
    }

    private void adicionarEventosCliqueTabela() {
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
                    removerDisciplinaDaSimulacao(null);
                }
            }
        });
    }

    private void carregarSugestoes(Aluno alunoLogado) {
        criarTabelaDeSugestoes();
        service.carregarPesoMaximoParaAluno(alunoLogado);
        disciplinasDisponiveis = service.carregarDisciplinasDisponiveis(alunoLogado.getCurso());
        List<MatrizDisciplina> disciplinasParaTabela = new ArrayList<>();
        disciplinasParaTabela.addAll(disciplinasDisponiveis);
        ObservableList<MatrizDisciplina> listaObs = FXCollections.observableList(disciplinasParaTabela);
        tableDisciplinasDisponiveis.setItems(listaObs);
    }

    private void criarTabelaRanking() {
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
                return new SimpleStringProperty(String.format("%.1f", service.coletarMediaAprovacao(c.getValue())));
            }
        });
        tabelaDisciplinasDificeis.getColumns().setAll(colunaNomeDisciplina, colunaAprovacoes);
    }

    public void carregarDisciplinasMaisDificeis(Curso curso) {
        criarTabelaRanking();
        List<Disciplina> disciplinasMaisDificeis = service.coletarDisciplinasMaisDificeis(curso);
        ObservableList<Disciplina> listaDisciplinasTabela = FXCollections.observableList(disciplinasMaisDificeis);
        tabelaDisciplinasDificeis.setItems(listaDisciplinasTabela);
    }

    @FXML
    public void adicionarDisciplinaParaSimulacao(ActionEvent event) {
        MatrizDisciplina selectedItem = tableDisciplinasDisponiveis.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (service.cumpreCoRequisitos(service.coletarAlunoLogado(), selectedItem, listDisciplinasSelecionadas.getItems())) {
                listDisciplinasSelecionadas.getItems().add(selectedItem);
                tableDisciplinasDisponiveis.getItems().remove(selectedItem);
                disciplinasDisponiveis.remove(selectedItem);
                lbRecomendacao.setText(service.coletarRecomendacaoSemestre(listDisciplinasSelecionadas.getItems()));
            } else {
                util.criarAlerta("Aviso", "Você não atendeu os co-requisitos", "Para inserir a disciplina " + selectedItem.getDisciplina().getCodigo() + ", deverá cumprir os co-requisitos: " + selectedItem.getDisciplina().getCoRequisitos());
            }
        }
    }

    @FXML
    void removerDisciplinaDaSimulacao(ActionEvent event) {
        MatrizDisciplina selectedItem = listDisciplinasSelecionadas.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            listDisciplinasSelecionadas.getItems().remove(selectedItem);
            disciplinasDisponiveis.add(selectedItem);
            Iterator<MatrizDisciplina> it = listDisciplinasSelecionadas.getItems().iterator();
            while (it.hasNext()) {
                MatrizDisciplina disciplina = it.next();
                if (!service.cumpreCoRequisitos(service.coletarAlunoLogado(), disciplina, listDisciplinasSelecionadas.getItems())) {
                    it.remove();
                    disciplinasDisponiveis.add(disciplina);
                }
            }
            service.ordenarDisciplinas(disciplinasDisponiveis);
            atualizarTabela(null);
            if (!listDisciplinasSelecionadas.getItems().isEmpty()) {
                lbRecomendacao.setText(service.coletarRecomendacaoSemestre(listDisciplinasSelecionadas.getItems()));
            } else {
                lbRecomendacao.setText("Selecione as disciplinas para simulação");
            }
        }
    }

    @FXML
    void sair(ActionEvent event) {
        ((Stage) btnSair.getScene().getWindow()).close();
    }

    @FXML
    void atualizarTabela(KeyEvent event) {
        String busca = txtBuscarDisciplina.getText().toLowerCase();
        tableDisciplinasDisponiveis.getItems().clear();
        List<MatrizDisciplina> disciplinasPelaBusca = new ArrayList<>();
        for (MatrizDisciplina md : disciplinasDisponiveis) {
            String codigo = md.getDisciplina().getCodigo().toLowerCase();
            String nomeDisciplina = md.getDisciplina().getNome().toLowerCase();

            if (codigo.contains(busca) || nomeDisciplina.contains(busca)) {
                disciplinasPelaBusca.add(md);
            }
        }
        tableDisciplinasDisponiveis.setItems(FXCollections.observableArrayList(disciplinasPelaBusca));
        service.ordenarDisciplinas(disciplinasPelaBusca);
    }
}
