package com.example.administrador.citycaremobile.Modelo;

import android.app.Application;
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

    public static void setToken(String token) {
        UsuarioApplication.token = token;
    }

    private static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoicm9vdCIsInNlbmhhIjoiY2FyZWNpdHkiLCJpcCI6IjE3Ny4xMjkuNjAuMTA1In0.4gAD8--mlchHdjsSs-0lQlQZkxI6UbqHM0TYmfv2xdA";

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
        } else if (empresa != null) {
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
        instance = this;;
    }
}

