package com.example.administrador.citycaremobile.Services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Administrador on 28/09/2017.
 */

public class WebClient {


    public String auth(){
        try {
            URL url = new URL("http://servico.projetocitycare.com.br/auth");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-type","application/json");
            connection.setRequestProperty("Accept", "application/json");

            connection.connect();

            connection.setDoInput(true);
            Scanner scanner = new Scanner(connection.getInputStream());
            String token = scanner.next();
            return token;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
