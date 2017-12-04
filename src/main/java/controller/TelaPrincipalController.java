package controller;

import estatistica.EstatisticaAprovacoesSemestre;
import estatistica.EstatisticaProfessor;
import estatistica.EstatisticasProfessores;
import estatistica.EstatisticasSemestres;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import modelo.Aluno;
import modelo.Curso;
import modelo.Disciplina;
import modelo.MatrizCurricular;
import modelo.MatrizDisciplina;
import modelo.Turma;
import service.ServiceFacade;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.AutoCompletionBinding.AutoCompletionEvent;
import org.controlsfx.control.textfield.TextFields;
import service.CursoService;
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
    private TableView<MatrizDisciplina> tableDisciplinasDisponiveis;

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

    @FXML
    private ComboBox<MatrizCurricular> cbMatrizCurricular;

    @FXML
    private WebView webViewSequencia;

    @FXML
    private WebView webViewDemandas;
    
    @FXML
    private StackedBarChart<String, Number> barAprovacoesSemestres;

    @FXML
    private StackedBarChart<String, Number> barAprovacoesPorDocente;

    @FXML
    private ImageView imagemGrafo;

    private ServiceFacade service;
    CursoService cursoService;
    //Possibilidades de resultado de busca de disciplina para consulta de estatisticas
    private List<Disciplina> disciplinas;
    //Quando escolher a disciplina para verificar a taxa de aprovacao
    private Disciplina disciplinaSelecionadaEstatistica;
    private EstatisticasProfessores estatisticasProfessores;
    private EstatisticaProfessor estatisticaProfSelecionado;
    
    private AutoCompletionBinding<Disciplina> autoCompleteEstatistica;
    private ControllerUtil util = new ControllerUtil();
    private List<MatrizDisciplina> disciplinasDisponiveis;
    private MatrizCurricular matrizCurricularSelecionada;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        service = new ServiceFacadeImpl();
        cursoService = new CursoService();
        Aluno alunoLogado = service.coletarAlunoLogado();
        txtBemVindo.setText("Bem vindo, " + alunoLogado.getNome());
        disciplinas = service.coletarDisciplinasDoCurso(alunoLogado.getCurso());

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

        
        carregarWebView(webViewDemandas, "bubble/index.html");
        carregarGrafo();
        //Tela de estatísticas
        carregarGraficoAprovacoes();
        //Tela de sugestoes/simulacoes
        carregarMatrizesDoCurso(alunoLogado.getCurso());
        carregarSugestoes(alunoLogado);
        carregarEstatisticasSemestres();
    }
    
    
    private void carregarWebView(WebView webView, String localArquivoHTML){
        WebEngine engine = webView.getEngine();
        URL url = getClass().getClassLoader().getResource(localArquivoHTML);
        engine.load(url.toString());
    }
    
    private void carregarGrafo(){
        URL url = getClass().getClassLoader().getResource("graph");
        imagemGrafo.setImage(new Image(url.toString()));
    }
    
    private void carregarEstatisticasSemestres() {
        EstatisticasSemestres estatisticas = cursoService.coletarEstatisticasDosSemestres();
        List<EstatisticaAprovacoesSemestre> aprovacoesSemestre = new ArrayList<>(estatisticas.getEstatisticasSemestres().values());

        XYChart.Series<String, Number> serieAprovados = new XYChart.Series<>();
        XYChart.Series<String, Number> serieReprovados = new XYChart.Series<>();
        CategoryAxis periodosLetivosGrafico = (CategoryAxis) barAprovacoesSemestres.getXAxis();
        ObservableList<String> observableArrayList = FXCollections.<String>observableArrayList(estatisticas.getEstatisticasSemestres().keySet());
        Collections.sort(observableArrayList);
        periodosLetivosGrafico.setCategories(observableArrayList);
        serieAprovados.setName("Aprovados");
        serieReprovados.setName("Reprovados");
        
        for(EstatisticaAprovacoesSemestre estatistica : aprovacoesSemestre){
             serieAprovados.getData().add(new XYChart.Data<>(estatistica.getPeriodoLetivo(), estatistica.getNumeroAprovados()));
             serieReprovados.getData().add(new XYChart.Data<>(estatistica.getPeriodoLetivo(), estatistica.getNumeroReprovados()));
        }
        barAprovacoesSemestres.getData().addAll(serieAprovados,serieReprovados);
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
        chartAprovacoes.setTitle("Média de aprovaçoes de " + disciplinaSelecionadaEstatistica.getCodigo());
        txtDisciplina.clear();
        carregarEstatisticasPorProfessor(disciplinaSelecionadaEstatistica);
    }
    
    private void carregarEstatisticasPorProfessor(Disciplina disciplina){
        List<Turma> turmas = disciplina.getTurmas();
        estatisticasProfessores = new EstatisticasProfessores();
        for(Turma turma : turmas){
            estatisticasProfessores.adicionarEstatisticasDaTurma(turma);
        }
        carregarGraficoPorProfessor();
    }
    
    
    public void carregarGraficoPorProfessor(){
        List<EstatisticaProfessor> estatisticasPorProfessor = new ArrayList<>(estatisticasProfessores.getEstatisticasProfessores().values());
        XYChart.Series<String, Number> serieAprovados = new XYChart.Series<>();
        XYChart.Series<String, Number> serieReprovados = new XYChart.Series<>();
        CategoryAxis eixoDocentes = (CategoryAxis) barAprovacoesSemestres.getXAxis();
        eixoDocentes.setCategories(FXCollections.<String>observableArrayList(estatisticasProfessores.getEstatisticasProfessores().keySet()));
        serieAprovados.setName("Aprovados");
        serieReprovados.setName("Reprovados");
        
        for(EstatisticaProfessor estatistica : estatisticasPorProfessor){
            System.out.println("Id docente: "+ estatistica.getIdDocente());
             serieAprovados.getData().add(new XYChart.Data<>(estatistica.getIdDocente().toString(), estatistica.getNumeroAprovados()));
             serieReprovados.getData().add(new XYChart.Data<>(estatistica.getIdDocente().toString(), estatistica.getNumeroReprovados()));
        }
        barAprovacoesPorDocente.getData().setAll(serieAprovados,serieReprovados);
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
        disciplinasDisponiveis = service.carregarDisciplinasDisponiveis(alunoLogado.getCurso(), matrizCurricularSelecionada);
        List<MatrizDisciplina> disciplinasParaTabela = new ArrayList<>();
        disciplinasParaTabela.addAll(disciplinasDisponiveis);
        ObservableList<MatrizDisciplina> listaObs = FXCollections.observableList(disciplinasParaTabela);
        tableDisciplinasDisponiveis.setItems(listaObs);
    }

    private void carregarMatrizesDoCurso(Curso curso) {
        ObservableList<MatrizCurricular> listaObs = FXCollections.observableArrayList(curso.getMatrizesCurricular());
        cbMatrizCurricular.setItems(listaObs);
        cbMatrizCurricular.getSelectionModel().select(0);
        matrizCurricularSelecionada = cbMatrizCurricular.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void adicionarDisciplinaParaSimulacao(ActionEvent event) {
        MatrizDisciplina selectedItem = tableDisciplinasDisponiveis.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (service.cumpreCoRequisitos(service.coletarAlunoLogado(), selectedItem, listDisciplinasSelecionadas.getItems())) {
                listDisciplinasSelecionadas.getItems().add(selectedItem);
                tableDisciplinasDisponiveis.getItems().remove(selectedItem);
                disciplinasDisponiveis.remove(selectedItem);
                lbRecomendacao.setText(service.coletarRecomendacaoSemestre(service.coletarAlunoLogado(), listDisciplinasSelecionadas.getItems()));
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
                lbRecomendacao.setText(service.coletarRecomendacaoSemestre(service.coletarAlunoLogado(), listDisciplinasSelecionadas.getItems()));
            } else {
                lbRecomendacao.setText("Selecione as disciplinas para simulação");
            }
        }
    }

    @FXML
    void sair(ActionEvent event) {
        System.exit(0);
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

    @FXML
    void carregarSugestoesDaMatriz(ActionEvent event) {
        matrizCurricularSelecionada = cbMatrizCurricular.getSelectionModel().getSelectedItem();
        listDisciplinasSelecionadas.getItems().clear();
        lbRecomendacao.setText("Selecione as disciplinas para simulação");
        carregarSugestoes(service.coletarAlunoLogado());
    }
}
