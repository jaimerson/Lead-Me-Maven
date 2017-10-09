/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minerador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import modelo.Disciplina;
import modelo.MatrizDisciplina;
import util.ProcessadorRequisitos;
import weka.associations.AssociationRule;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

/**
 *
 * @author rafao
 */
public class AssociadorWeka {
    
    public static final String ARQUIVO_ASSOCIACAO = "associacao.arff";
    public static final Integer NUMERO_REGRAS = 1000;
    private static AssociadorWeka instance = new AssociadorWeka();
    
    private AssociacaoStrategy associacaoStrategy;
    
    private AssociadorWeka(){
        associacaoStrategy = new FPGrowthStrategy();
    }
    
    public static AssociadorWeka getInstance(){
        return instance;
    }
    
    private void converterCsvParaArff(File arff) throws IOException {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(GeradorCSV.NOME_ARQUIVO_CSV_ASSOCIACAO_BINARIA));
        Instances dataCSV = loader.getDataSet();
        ArffSaver saver = new ArffSaver();
        saver.setInstances(dataCSV);
        saver.setFile(arff);
        saver.writeBatch();
    }

    private void removerAssociacoesIrrelevantes(List<AssociationRule> associacoes) {
        Iterator<AssociationRule> linhasIterator = associacoes.iterator();
        String[] divisaoRegraAssociacao;
        String regraDisciplinasPremissa;
        List<String> disciplinasPremissa;

        while (linhasIterator.hasNext()) {
            AssociationRule regra = linhasIterator.next();
            String regraString = regra.toString();
            System.out.println(regraString);
            divisaoRegraAssociacao = regraString.split("==>");
            regraDisciplinasPremissa = divisaoRegraAssociacao[0];
            disciplinasPremissa = ProcessadorRequisitos.coletarDisciplinasDaExpressao(regraDisciplinasPremissa);
            //Se tiver varias disciplinas do lado esquerdo, nao ajuda muito
            if (disciplinasPremissa.size() > 1){
                linhasIterator.remove();
            }
        }
    }

    private List<AssociationRule> coletarRegrasDeAssociacao() {
        List<AssociationRule> regras = null;
        try {
            File arff = new File(ARQUIVO_ASSOCIACAO);
            if (!arff.exists()) {
                converterCsvParaArff(arff);
            }
            regras = associacaoStrategy.rodarAssociacao();
            //filtra só os positivos
            removerAssociacoesIrrelevantes(regras);

        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } catch (Exception ex) {
        }
        return regras;
    }
    
    /**
     * Usa o algoritmo de associação de mineração de dados para verificar quais disciplinas
     * @param disciplinasDisponiveis 
     */
    public void associarDisciplinasComunsAPeriodoLetivo(List<MatrizDisciplina> disciplinasDisponiveis) {
        List<AssociationRule> associacoes = coletarRegrasDeAssociacao();
        String[] divisaoRegraAssociacao;
        String regraDisciplinasPremissa;
        String regraDisciplinasEnvolvidas;
        List<String> disciplinasEnvolvidas;
        
        //Para cada disciplina disponivel
        for (MatrizDisciplina materiaDisponivel : disciplinasDisponiveis){
            //Se for uma obrigatoria
            if (materiaDisponivel.getNaturezaDisciplina().equalsIgnoreCase("OBRIGATORIO")){
                Disciplina disciplina = materiaDisponivel.getDisciplina();
                String codigo = disciplina.getCodigo();
                
                //percorro cada regra buscando essa disciplina obrigatoria
                for (AssociationRule associacao: associacoes){
                    String associacaoString = associacao.toString();
                    divisaoRegraAssociacao = associacaoString.split("==>");
                    regraDisciplinasPremissa = divisaoRegraAssociacao[0];
                    //Se a premissa tiver o codigo da disciplina, entao podemos procurar uma optativa associada
                    if (regraDisciplinasPremissa.contains(codigo)){
                        regraDisciplinasEnvolvidas = divisaoRegraAssociacao[1];
                        disciplinasEnvolvidas = ProcessadorRequisitos.coletarDisciplinasDaExpressao(regraDisciplinasEnvolvidas);
                        //Para cada disciplina envolvida com a obrigatoria em questao
                        for (String codigoDisciplinaEnvolvida: disciplinasEnvolvidas){
                            //Coleto a disciplina envolvida 
                            MatrizDisciplina disciplinaEnvolvida = null;
                            for (MatrizDisciplina disciplinaM: disciplinasDisponiveis){
                                if (disciplinaM.getDisciplina().getCodigo().equals(codigoDisciplinaEnvolvida)){
                                    disciplinaEnvolvida = disciplinaM;
                                    break;
                                }
                            }
                            
                            //Verifico se eh optativa e nenhuma associaçao foi feita com ela
                            //Se for positivo, associo com a materia obrigatoria do laço externo
                            if (disciplinaEnvolvida != null && disciplinaEnvolvida.getNaturezaDisciplina().equalsIgnoreCase("OPTATIVO") && disciplinaEnvolvida.getSemestreIdeal().equals(Integer.MAX_VALUE)){
                                disciplinaEnvolvida.setSemestreIdeal(materiaDisponivel.getSemestreIdeal());
                            }
                        }
                    }
                }
            }
        }
        Collections.sort(disciplinasDisponiveis);
    }
}
