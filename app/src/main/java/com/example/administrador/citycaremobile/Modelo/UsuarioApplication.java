package com.example.administrador.citycaremobile.Modelo;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;
import com.example.administrador.citycaremobile.Services.Token;
import com.example.administrador.citycaremobile.Utils.ErrorUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

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
    private static String usuario = "root";
    private static String senha = "carecity";
    private Token token = null;
    private static DateTimeZone timeZone;



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

    public Token getToken() {
        return token;
    }

    public void setToken(Token token){
        this.token = token;
    }

    public DateTimeZone getTimeZone(){
        return timeZone;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        timeZone = DateTimeZone.getDefault();
        Log.i("TimeZone","Time Zone: " + timeZone);

        Service service = CallService.createService(Service.class,usuario,senha);
        Call<Token> call = service.autentication();
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    Token token = response.body();
                    UsuarioApplication.getInstance().setToken(token);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Log.e("AuthError", error.getMessage());
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.e("ConnectionError", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

