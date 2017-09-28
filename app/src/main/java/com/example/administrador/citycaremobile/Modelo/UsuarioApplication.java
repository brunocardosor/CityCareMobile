package com.example.administrador.citycaremobile.Modelo;

import android.app.Application;
import android.os.AsyncTask;

import com.example.administrador.citycaremobile.Services.WebClient;


/**
 * Created by Administrador on 17/09/2017.
 */

public class UsuarioApplication extends Application {

    private static Cidadao cidadao;
    private static Empresa empresa;
    private static UsuarioApplication instance = null;
    private static String usuario = "root";
    private static String senha = "carecity";
    private static String token = null;


    public Object getUsuario(){
        if(cidadao != null && empresa == null){
            return cidadao;
        } else if(empresa != null && cidadao == null) {
            return empresa;
        } else {
            return null;
        }
    }

    public void setUsuario(Cidadao cidadao){
        if(empresa == null)
        this.cidadao = cidadao;
        else
            new Exception("Não é possivel instânciar dois usuários");
    }

    public void setUsuario(Empresa empresa){
        if(cidadao == null)
        this.empresa = empresa;
        else
            new Exception("Não é possivel instânciar dois usuários");
    }

    public static UsuarioApplication getInstance(){
        return instance;
    }

    public static String getToken(){
        return token;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AsyncTask<Void,Void,String> asyncTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                WebClient cliente = new WebClient();
                return cliente.auth();

            }

            @Override
            protected void onPostExecute(String s) {
                token = s;
            }
        };
    }
}
