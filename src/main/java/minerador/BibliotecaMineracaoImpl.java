package minerador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Curso;
import modelo.Disciplina;
import modelo.MatrizDisciplina;
import weka.associations.FPGrowth;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class BibliotecaMineracaoImpl implements BibliotecaMineracao {

    public GeradorCSV geradorCSV;
    
    public BibliotecaMineracaoImpl() {
        geradorCSV = GeradorCSV.getInstance();
    }

    @Override
    public void gerarArquivoParaAssociarDisciplinas(Curso curso) {
        geradorCSV.gerarCSVBinarioDosPeriodosLetivos(curso);
    
    }
    private ArrayList<String> minerarComWeka(){
        ArrayList<String> linhas = new ArrayList<>();
        try {            
            File arff = new File("teste.arff");
            if(!arff.exists()) { 
                //só é usado pra converter csv em arff
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File(GeradorCSV.NOME_ARQUIVO_CSV_ASSOCIACAO_BINARIA));
            Instances dataCSV = loader.getDataSet();

            ArffSaver saver = new ArffSaver();
            saver.setInstances(dataCSV);
            saver.setFile(arff);
            saver.writeBatch();           
            }
            
            BufferedReader reader;
            reader = new BufferedReader(new FileReader("teste.arff"));
            Instances data = new Instances(reader);
            reader.close();
            
            FPGrowth growth = new FPGrowth();
            growth.setMinMetric(0.7);
            growth.setNumRulesToFind(1700);
            growth.buildAssociations(data);
                      
            String resultado = growth.toString();
            linhas.addAll(Arrays.asList(resultado.split(System.getProperty("line.separator"))));
            //filtra só os positivos
            List<Object> toRemove = new ArrayList<>();
            String oldStr="";
            for(int i = 0; i < linhas.size(); i++){
            String obj = linhas.get(i);
                if(obj.contains("=n") || obj.contains("FPGrowth")){
                toRemove.add(obj);
                }else{
                 if (obj.length() > 52)linhas.set(i,obj.substring(4, obj.length()-48));//remove caracteres desnecessário
                 oldStr = linhas.get(i).replace(": 30 ", "");
                 oldStr = oldStr.replace("=y", "");
                 oldStr = oldStr.replace("]", "");
                 oldStr = oldStr.replace("[", "");
                 oldStr = oldStr.replace("==>", ",");
                 oldStr = oldStr.replace(" ", "");
                 linhas.set(i,oldStr);
                } 
            }
            linhas.removeAll(toRemove);
            //
            //print 
            for(int i = 0; i < linhas.size(); i++){
              System.out.println(linhas.get(i));         
            } 
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BibliotecaMineracaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BibliotecaMineracaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }catch (Exception ex) {
            Logger.getLogger(BibliotecaMineracaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return linhas;
    }
    
    /*public ArrayList<Disciplina> getAssociadas(Disciplina disciplina, List<MatrizDisciplina> disponiveis){
        ArrayList<Disciplina> resultado = new ArrayList<>();
        ArrayList<String> linhas = minerarComWeka();
        if(!linhas.isEmpty()){
            for(int i = 0; i < linhas.size(); i++){
              String temp = linhas.get(i);
              if(temp.contains(disciplina.getCodigo())){// se a disciplina está na associção weka ,
                  String[] split = temp.split(","); //pego a linha que a disciplina está ,
                  for (String split_ : split) { //comparo todas disciplinas da associação ,
                      for(int j = 0;j < disponiveis.size(); j++){ //com todas disponiveis .
                          if(split_.equals(disponiveis.get(j).getDisciplina().getCodigo())&&
                                  resultado.contains(disponiveis.get(j).getDisciplina()))
                            //se o código das disciplinas forem iguais e se ela já não estiver no array resultado ,
                              resultado.add(disponiveis.get(j).getDisciplina());//adiciono a disciplina ao array de associações.
                      }
                  }
              }
            }            
        }        
        return resultado;
    }*/
    public void associar(List<MatrizDisciplina> disponiveis){
        /*Esta função atribui o semestre ideal as disciplinas */
        ArrayList<String> linhas = minerarComWeka();
        for(int i = 0;i < disponiveis.size();i++){
            if("OBRIGATORIO".equals(disponiveis.get(i).getNaturezaDisciplina())){
                Disciplina disciplina = disponiveis.get(i).getDisciplina();
                String codigo = disciplina.getCodigo();
            
                for(int j = 0; j < linhas.size(); j++){
                    String temp = linhas.get(j);
                    if(temp.contains(codigo)){
                        String[] split = temp.split(","); //pego a linha que a disciplina está ,
                        
                        for (String split_ : split) { //comparo todas disciplinas da associação ,
                            if(!split_.equals(codigo)){
                                
                                for(int k = 0;k < disponiveis.size(); k++){ //com todas disponiveis .
                                    if(split_.equals(disponiveis.get(k).getDisciplina().getCodigo()))
                                        disponiveis.get(k).setSemestreIdeal(disponiveis.get(i).getSemestreIdeal());
                                        //se o código das disciplinas forem iguais e se ela já não estiver no array resultado ,
                                }
                            }
                        }    
                    }            
                }
            }
        }
    }
}
