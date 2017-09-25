package com.example.administrador.citycaremobile.Modelo;

import android.app.Application;

/**
 * Created by Administrador on 17/09/2017.
 */

public class UsuarioApplication extends Application {

    private static Cidadao cidadao;
    private static Empresa empresa;
    private static UsuarioApplication instance = null;

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

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
