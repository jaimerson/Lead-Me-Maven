package dados_instituicao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.Curso;
import modelo.Disciplina;
import modelo.MatrizCurricular;

public class LeitorArquivos {
	public static final String DIRETORIO_GRADES = "grades/";
	
	public static String[] getArquivosMatrizesCurricularesDoCurso(String nomeCurso) {
        File file = new File(DIRETORIO_GRADES);
        return file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().startsWith("grade - "+ nomeCurso.toLowerCase());
            }
        });
    }
	
	public static Curso carregarCursoComGrades(String nomeCurso) throws IOException {
		Map<String,Disciplina> disciplinasDoCurso = new HashMap<String,Disciplina>();
		Curso curso = new Curso(nomeCurso);
		String[] arquivosGrade = getArquivosMatrizesCurricularesDoCurso(nomeCurso);
		
		for (String arquivoGrade: arquivosGrade) {
			System.out.println(arquivoGrade);
			MatrizCurricular matriz = new MatrizCurricular(arquivoGrade.split(" - ")[2].replace(".txt", ""));
			FileReader arq = new FileReader(DIRETORIO_GRADES+arquivoGrade);
	        BufferedReader lerArq = new BufferedReader(arq);
	        String linha;
	        
	        String codigo, nomeDisciplina, naturezaDisciplina;
	        Integer cargaHoraria, semestreIdeal;
	        String[] dadosLinha;
	        Disciplina disciplina;
	        
	        while ( (linha = lerArq.readLine()) != null) {
	        	linha = linha.replace("\n", "");
	        	System.out.println(linha);
	        	dadosLinha = linha.split(";");
	        	codigo = dadosLinha[0];
	        	nomeDisciplina = dadosLinha[1];
	        	cargaHoraria = Integer.parseInt(dadosLinha[2]);
	        	naturezaDisciplina = dadosLinha[3];
	        	semestreIdeal = Integer.parseInt(dadosLinha[4]);
	        	if (!disciplinasDoCurso.containsKey(codigo)) {
	        		disciplina = new Disciplina(codigo,nomeDisciplina,cargaHoraria);
	        		disciplinasDoCurso.put(codigo, disciplina);	
	        	}
	        	else {
	        		disciplina = disciplinasDoCurso.get(codigo);
	        	}
	        	matriz.adicionarDisciplina(disciplina, naturezaDisciplina, semestreIdeal);
	        }
	        lerArq.close();
	        curso.adicionarMatrizCurricular(matriz);
		}
		
		return curso;
	}
}
