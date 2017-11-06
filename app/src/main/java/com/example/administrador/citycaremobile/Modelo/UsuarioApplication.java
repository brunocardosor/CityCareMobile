package com.example.administrador.citycaremobile.Modelo;

import android.app.Application;

import com.example.administrador.citycaremobile.Adapters.FeedDenunciaAdapter;


/**
 * Created by Administrador on 17/09/2017.
 */

public class UsuarioApplication extends Application {

    private Cidadao cidadao;
    private Empresa empresa;
    private static UsuarioApplication instance = null;
    private static FeedDenunciaAdapter feedDenuncia;
    private static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoicm9vdCIsInNlbmhhIjoiY2FyZWNpdHkiLCJpcCI6IjE3Ny4xMjkuNjAuMTA1In0.4gAD8--mlchHdjsSs-0lQlQZkxI6UbqHM0TYmfv2xdA";


    public static FeedDenunciaAdapter getFeedDenuncia() {
        return feedDenuncia;
    }

    public static void setFeedDenuncia(FeedDenunciaAdapter feedDenuncia) {
        UsuarioApplication.feedDenuncia = feedDenuncia;
    }

    public Object getUsuario() {
        if (cidadao != null && empresa == null) {
            return cidadao;
        } else if (empresa != null && cidadao == null) {
            return empresa;
        } else {
            return null;
        }
    }

    public void setUsuario(Cidadao cidadao) throws Exception {
        if (empresa == null)
            this.cidadao = cidadao;
        else
            throw new Exception("Não é possivel instânciar dois usuários");
    }

    public void setUsuario(Empresa empresa) throws Exception {
        if (cidadao == null)
            this.empresa = empresa;
        else
            throw new Exception("Não é possivel instânciar dois usuários");
    }

    public void logout(){
        if(cidadao != null){
            cidadao = null;
        } else if (empresa != null){
            empresa = null;
        }
    }

    public static UsuarioApplication getInstance() {
        return instance;
    }

    public static String getToken() {
        return token;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        feedDenuncia = new FeedDenunciaAdapter(this);
    }
}

