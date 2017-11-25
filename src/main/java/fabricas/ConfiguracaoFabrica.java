/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fabricas;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author rafao
 */
public class ConfiguracaoFabrica {

    public static final String NOME_ARQUIVO = "instancia.txt";

    public static String coletarInstanciaSistema() throws FileNotFoundException, UnsupportedEncodingException, IOException {
//        BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream(NOME_ARQUIVO), "UTF-8"));
//        String instancia = lerArq.readLine();
//        lerArq.close();
//        return instancia.replace("\n","");
        return "UFRN";
    }

    public static void salvarInstanciaSistema(String instancia) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(NOME_ARQUIVO), "UTF-8"));
        pw.write(instancia);
        pw.close();
    }
}
