/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
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

    private final static String BASE_URL = "https://apitestes.info.ufrn.br";
    private final static String CLIENT_ID = "integra-ti-id";
    private final static String CLIENT_SECRET = "segredo";
    private final static String X_PI_KEY = "ABXsOnkQx7juN3GYNXGR6lu3CthmHnAuzGIxqEv6";
    private final static String URL_ENCAMINHA = "http://www.google.com.br";
    private static String URL_AUTENTICACAO_SINFO = BASE_URL + "/authz-server/oauth/authorize?client_id=" + CLIENT_ID + "&response_type=code&redirect_uri=" + URL_ENCAMINHA;
    private static String URL_GET_ACCESS_TOKEN = BASE_URL + "/authz-server/oauth/token?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&redirect_uri=" + URL_ENCAMINHA + "&grant_type=authorization_code&code=#code#";
    private static final String POST = "POST";
    private static final String GET = "GET";

    private String accessToken;

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

    public String carregarAccessToken(String location) throws JSONException {
        String codigo = coletarCodigo(location);
        String urlAccessToken = URL_GET_ACCESS_TOKEN.replace("#code#", codigo);
        try {
            String resultadoAccessToken = fazerRequisicao(urlAccessToken, POST, null, null);
            JSONObject resultJson = new JSONObject(resultadoAccessToken);
            String token = resultJson.getString("access_token");
            accessToken = token;
            return token;
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConsumidorAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConsumidorAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String fazerRequisicao(String location, String metodo, String token, String xApiKey) throws ProtocolException, MalformedURLException, IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(location);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(metodo.toUpperCase());
        conn.setRequestProperty("content-type", "application/json");
        conn.setRequestProperty("charset", "UTF-8");
        if (token != null && xApiKey != null) {
            conn.setRequestProperty("Authorization", "bearer " + token);
            conn.setRequestProperty("x-api-key", xApiKey);
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

    public String coletarInfoTeste() {
        try {
            return fazerRequisicao(BASE_URL + "/v0.1/usuarios/info", GET, accessToken, X_PI_KEY);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConsumidorAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConsumidorAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
