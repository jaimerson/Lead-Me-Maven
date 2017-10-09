package controller;

import excecoes.DataException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import minerador.BibliotecaMineracao;
import minerador.BibliotecaMineracaoFactory;
import modelo.Aluno;
import modelo.Curso;
import modelo.Disciplina;
import modelo.Matricula;
import modelo.MatrizDisciplina;
import modelo.Turma;
import service.ServiceFacade;
import service.ServiceFacadeFactory;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.AutoCompletionBinding.AutoCompletionEvent;
import org.controlsfx.control.textfield.TextFields;
import util.ProcessadorRequisitos;

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
    private BibliotecaMineracao bibliotecaMineracao;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        service = ServiceFacadeFactory.getInstance().getServiceInstance();
        bibliotecaMineracao = BibliotecaMineracaoFactory.getInstance().getBibliotecaMineracaoInstance();
        final Aluno alunoLogado = service.coletarAlunoLogado();
        disciplinas = service.carregarDisciplinasDoCurso(alunoLogado.getCurso());

        /*estatistica*/
        autoCompleteEstatistica = TextFields.bindAutoCompletion(txtDisciplina, disciplinas);
        autoCompleteEstatistica.setOnAutoCompleted(new EventHandler<AutoCompletionEvent<Disciplina>>() {

            @Override
            public void handle(AutoCompletionEvent<Disciplina> event) {
                disciplinaSelecionadaEstatistica = event.getCompletion();
                carregarGraficoAprovacoes(alunoLogado.getCurso());
                event.consume();
            }
        });
        
        //Tela inicial
        carregarInformacoesAluno(alunoLogado);
        //Tela de estatísticas
        carregarGraficoAprovacoes(alunoLogado.getCurso());
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

    private void carregarInformacoesAluno(Aluno alunoLogado) {
        txtBemVindo.setText("Bem vindo(a), " + alunoLogado.getNome());
        Double progresso = alunoLogado.getProgresso();
        progressBarCurso.setProgress(progresso);
        lbProgresso.setText("Progresso no curso: " + String.format("%.2f", progresso * 100) + "%");
        txtIEA.setText(alunoLogado.getIea().toString());
        txtMCN.setText(alunoLogado.getMcn().toString());
    }

    private void carregarGraficoAprovacoes(Curso curso) {
        Double aprovacoes;
        if (disciplinaSelecionadaEstatistica == null){
            return;
        }
        try {
            aprovacoes = service.coletarMediaAprovacao(curso, disciplinaSelecionadaEstatistica);
            ObservableList<PieChart.Data> dadosPieChart = FXCollections.observableArrayList(
                    new PieChart.Data("Aprovados: " + String.format("%.2f", aprovacoes) + "%", aprovacoes),
                    new PieChart.Data("Reprovados: " + String.format("%.2f", 100.0 - aprovacoes) + "%", 100.0 - aprovacoes));
            chartAprovacoes.setData(dadosPieChart);
            txtTituloPieChart.setText("Aprovações de " + disciplinaSelecionadaEstatistica.getNome());
            carregarListaTurmasSelecionavel(new ArrayList<>(disciplinaSelecionadaEstatistica.getTurmas().values()));

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

    private void carregarListaTurmasSelecionavel(List<Turma> turmasSelecionaveis) {
        ObservableList<Turma> listaObs = FXCollections.observableArrayList(turmasSelecionaveis);
        cbTurmas.setItems(listaObs);
        cbTurmas.getSelectionModel().select(0);
    }

    private void carregarSugestoes(Aluno alunoLogado) {
        service.carregarPesoMaximoParaAluno(alunoLogado);
        disciplinasDisponiveis = service.carregarDisciplinasDisponiveis(alunoLogado.getCurso());
        bibliotecaMineracao.associarDisciplinasComunsAPeriodoLetivo(disciplinasDisponiveis);
        
        List<MatrizDisciplina> disciplinasParaTabela = new ArrayList<>();
        disciplinasParaTabela.addAll(disciplinasDisponiveis);
        ObservableList<MatrizDisciplina> listaObs = FXCollections.observableList(disciplinasParaTabela);
        tableDisciplinasDisponiveis.setItems(listaObs);
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
                disciplinasDisponiveis.remove(selectedItem);
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
    void removerDisciplinaDaSimulacao(ActionEvent event) {
        MatrizDisciplina selectedItem = listDisciplinasSelecionadas.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            listDisciplinasSelecionadas.getItems().remove(selectedItem);
            disciplinasDisponiveis.add(selectedItem);
            Iterator<MatrizDisciplina> it = listDisciplinasSelecionadas.getItems().iterator();
            while(it.hasNext()){
                MatrizDisciplina disciplina = it.next();
                if (!ProcessadorRequisitos.cumpreCoRequisitos(service.coletarAlunoLogado(), disciplina, listDisciplinasSelecionadas.getItems())) {
                    it.remove();
                    disciplinasDisponiveis.add(disciplina);
                }
            }
            atualizarTabela(null);
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
    
      @FXML
    void atualizarTabela(KeyEvent event) {
        String busca = txtBuscarDisciplina.getText().toLowerCase();
        tableDisciplinasDisponiveis.getItems().clear();
        List<MatrizDisciplina> disciplinasPelaBusca = new ArrayList<>();
        for(MatrizDisciplina md : disciplinasDisponiveis){
            String codigo = md.getDisciplina().getCodigo().toLowerCase();
            String nomeDisciplina = md.getDisciplina().getNome().toLowerCase();
            
            if(codigo.contains(busca)|| nomeDisciplina.contains(busca) ){
                disciplinasPelaBusca.add(md);
            }
        }
        tableDisciplinasDisponiveis.setItems(FXCollections.observableArrayList(disciplinasPelaBusca));
        Collections.sort(tableDisciplinasDisponiveis.getItems());
    }
}
