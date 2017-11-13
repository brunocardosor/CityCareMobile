package com.example.administrador.citycaremobile.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;
import com.example.administrador.citycaremobile.Services.Token;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrador on 23/09/2017.
 */

public final class SystemUtils {

    public boolean verificaConexao(Context context) {
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }


    public void authenticateToken(final Context context) {
        Service service = CallService.createService(Service.class, "root", "carecity");
        Call<Token> getToken = service.autentication();
        getToken.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    UsuarioApplication.setToken(response.body().getToken());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Log.e("getToken", error.getMessage());
                    Toasty.error(context, "Erro na conexão", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.e("getToken", t.getMessage());
                Toasty.error(context, "Erro na conexão", Toast.LENGTH_LONG).show();
            }
        });
    }
}
