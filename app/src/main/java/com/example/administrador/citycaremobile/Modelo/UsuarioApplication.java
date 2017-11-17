package com.example.administrador.citycaremobile.Modelo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrador.citycaremobile.Adapters.FeedDenunciaAdapter;
import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Fragments.FeedFragment;
import com.example.administrador.citycaremobile.Fragments.MapsFragment;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;
import com.example.administrador.citycaremobile.Services.Token;
import com.example.administrador.citycaremobile.Utils.ErrorUtils;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Administrador on 17/09/2017.
 */

public class UsuarioApplication extends Application {

    private Cidadao cidadao;
    private Empresa empresa;
    private static UsuarioApplication instance = null;
    private SharedPreferences preferences;
    private static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoicm9vdCIsInNlbmhhIjoiY2FyZWNpdHkiLCJpcCI6IjE3Ny4xMjkuNjAuMTA1In0.4gAD8--mlchHdjsSs-0lQlQZkxI6UbqHM0TYmfv2xdA";

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public static void setToken(String token) {
        UsuarioApplication.token = token;
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

    public void logout() {
        if (cidadao != null) {
            cidadao = null;
            preferences.edit().clear();
        } else if (empresa != null) {
            empresa = null;
            preferences.edit().clear();
        }
    }

    public static UsuarioApplication getInstance() {
        return instance;
    }

    public static String getToken() {
        return token;
    }

    private Object getUserPreferences(){
        if(preferences.getInt("idCidadao",0) != 0){
            return new Cidadao(preferences.getInt("idCidadao",0),
                    preferences.getString("nome", ""),
                    preferences.getString("sobrenome", ""),
                    preferences.getString("cidade", ""),
                    preferences.getString("estado", ""),
                    preferences.getString("dirFoto",""),
                    preferences.getString("sexo", ""),
                    new Login(preferences.getInt("idLogin", 0),
                            preferences.getString("email", ""),
                            preferences.getString("login",""),
                            null,
                            preferences.getBoolean("administrator", false),
                            preferences.getBoolean("statusAcc", false)));
        } else if(preferences.getInt("idEmpresa",0) != 0){
            return new Empresa(preferences.getInt("idEmpresa", 0),
                    preferences.getString("cnpj",""),
                    preferences.getString("razaoSocial", ""),
                    preferences.getString("nomeFantasia", ""),
                    preferences.getString("cidade", ""),
                    preferences.getString("estado", ""),
                    preferences.getString("dirFoto"," "),
                    new Login(preferences.getInt("idLogin", 0),
                    preferences.getString("email", ""),
                    preferences.getString("login",""),
                    null,
                    preferences.getBoolean("administrator", false),
                    preferences.getBoolean("statusAcc", false)));
        } else {
            return null;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        preferences = getApplicationContext().getSharedPreferences("acess", Context.MODE_PRIVATE);
        if(getUserPreferences() instanceof Cidadao){
            cidadao = (Cidadao) getUserPreferences();
        } else if(getUserPreferences() instanceof Empresa){
            empresa = (Empresa) getUserPreferences();
        }
    }
}

