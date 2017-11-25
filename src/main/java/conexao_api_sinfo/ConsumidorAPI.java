/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexao_api_sinfo;

import dto.DiscenteUfrnDTO;
import dto.MatriculaComponenteUfrnDTO;
import dto.SituacaoMatriculaUfrnDTO;
import dto.UsuarioUfrnDTO;
import excecoes.JsonStringInvalidaException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author rafao
 */
public class ConsumidorAPI {

    public final static String BASE_URL = "https://apitestes.info.ufrn.br/";
    public final static String CLIENT_ID = "integra-ti-id";
    public final static String CLIENT_SECRET = "segredo";
    public final static String X_PI_KEY = "ABXsOnkQx7juN3GYNXGR6lu3CthmHnAuzGIxqEv6";
    public final static String URL_ENCAMINHA = "https://api.ufrn.br";
    
    public static String URL_AUTENTICACAO_SINFO = BASE_URL + "authz-server/oauth/authorize?client_id=" + CLIENT_ID + "&response_type=code&redirect_uri=" + URL_ENCAMINHA;
    public static String URL_GET_ACCESS_TOKEN = BASE_URL + "authz-server/oauth/token?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&redirect_uri=" + URL_ENCAMINHA + "&grant_type=authorization_code&code=#code#";
    public static final String URL_INFO_USUARIO = BASE_URL + "usuario/v0.1/usuarios/info";
    public static String URL_DISCENTE = BASE_URL + "discente/v0.1/discentes?cpf-cnpj=#cpf#";
    public static String URL_MATRICULAS = BASE_URL + "matricula/v0.1/matriculas-componentes?id-discente=#id-discente#";
    public static String URL_SITUACOES_MATRICULA = BASE_URL + "matricula/v0.1/situacoes-matriculas?ativo=false";
    
    private static final String POST = "POST";
    private static final String GET = "GET";

    private static ConsumidorAPI instance = new ConsumidorAPI();
    private String accessToken;
    private List<SituacaoMatriculaUfrnDTO> situacoesMatricula;

    private ConsumidorAPI() {
        situacoesMatricula = null;
    }

    public static ConsumidorAPI getInstance() {
        return instance;
    }

    public String coletarCodigo(String location) {
        URL url;
        try {
            url = new URL(location);
            String[] params = url.getQuery().split("&");
            Map<String, String> map = new HashMap<>();
            for (String param : params) {
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                map.put(name, value);
            }
            System.out.println("The code: " + map.get("code"));
            return map.get("code");
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    public String carregarAccessToken(String location) throws JSONException, IOException {
        String codigo = coletarCodigo(location);
        if (codigo != null) {
            String urlAccessToken = URL_GET_ACCESS_TOKEN.replace("#code#", codigo);
            try {
                String resultadoAccessToken = fazerRequisicao(urlAccessToken, POST);
                JSONObject resultJson = new JSONObject(resultadoAccessToken);
                String token = resultJson.getString("access_token");
                accessToken = token;
                return token;
            } catch (MalformedURLException ex) {
                Logger.getLogger(ConsumidorAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public String fazerRequisicao(String location, String metodo) throws ProtocolException, MalformedURLException, IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(location);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(metodo.toUpperCase());
        conn.setRequestProperty("content-type", "application/json");
        conn.setRequestProperty("charset", "UTF-8");
        if (accessToken != null) {
            System.out.println("Token >>>>> " + accessToken);
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("X-Api-Key", X_PI_KEY);
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

    public UsuarioUfrnDTO coletarUsuarioLogado() {
        try {
            String resultado = fazerRequisicao(URL_INFO_USUARIO, GET);
            return JsonToObject.toUsuario(resultado);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConsumidorAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConsumidorAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JsonStringInvalidaException ex) {
            Logger.getLogger(ConsumidorAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    public List<DiscenteUfrnDTO> coletarVinculos(String cpf){
        String urlIdDiscente = URL_DISCENTE.replace("#cpf#", cpf);
        List<DiscenteUfrnDTO> vinculos;
        try {
            String resultado = fazerRequisicao(urlIdDiscente, GET);
            vinculos = JsonToObject.toDiscentes(resultado);
            return vinculos;
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConsumidorAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConsumidorAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JsonStringInvalidaException ex) {
            Logger.getLogger(ConsumidorAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public DiscenteUfrnDTO carregarUltimoVinculo(String cpf){
        List<DiscenteUfrnDTO> vinculos = coletarVinculos(cpf);
        return vinculos.get(vinculos.size() - 1);
    }
    
    public List<MatriculaComponenteUfrnDTO> coletarMatriculas(Integer idDiscente){
        String urlMatriculas = URL_MATRICULAS.replace("#id-discente#", idDiscente.toString());
        List<MatriculaComponenteUfrnDTO> matriculas;
        try {
            String resultado = fazerRequisicao(urlMatriculas, GET);
            System.out.println("MATRICULAS: " + resultado);
            matriculas = JsonToObject.paraMatriculas(resultado);
            return matriculas;
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConsumidorAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConsumidorAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JsonStringInvalidaException ex) {
            Logger.getLogger(ConsumidorAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
    
    public String coletarSituacaoMatricula(Integer idSituacaoMatricula){
        System.out.println("Coletando situação da matrícula");
        try {
            if(situacoesMatricula == null){
                System.out.println("Nao fez a requisicao das situacoes ainda");
                String resultado = fazerRequisicao(URL_SITUACOES_MATRICULA, GET);
                situacoesMatricula = JsonToObject.paraSituacoes(resultado);
            }
            for(SituacaoMatriculaUfrnDTO situacao: situacoesMatricula){
                if(situacao.getIdSituacaoMatricula().equals(idSituacaoMatricula)){
                    System.out.println("Achou a situação: " + situacao.getDescricao());
                    return situacao.getDescricao();
                }
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConsumidorAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConsumidorAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
