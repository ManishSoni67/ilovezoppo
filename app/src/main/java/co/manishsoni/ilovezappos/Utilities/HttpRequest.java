package co.manishsoni.ilovezappos.Utilities;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by manis on 1/29/2017.
 */

public class HttpRequest {

    public String getData(String strURL)
    {
        HttpURLConnection connection = null;
        try {
            String response = "";
            URL url = new URL(strURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(45000);
            connection.setConnectTimeout(30000);
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                response = Utils.getResponse(connection.getInputStream());
            } else {
            }
            return response;

        } catch (Exception e) {
            return  e.getMessage();
        }

    }



}
