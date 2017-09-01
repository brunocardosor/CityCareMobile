package com.example.administrador.citycaremobile.Controller;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.example.administrador.citycaremobile.Modelo.Login;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Administrador on 01/09/2017.
 */

public class LoginController extends GenericController<Login> {

    Service service = CallService.createService(Service.class);

    public LoginController(Context context) {
        super(context);
    }


    @Override
    boolean[] delete(Login login, final Context context) {
        final boolean[] autenticacao = new boolean[]{false};
        Call<Boolean> call = service.deleteLogin(login);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Boolean bool = response.body();
                    if(bool.booleanValue() == true){
                        autenticacao[0] = true;
                        Toast.makeText(context,"Usuário desativado com sucesso", Toast.LENGTH_LONG).show();
                    }

                    else{
                        autenticacao[0] = false;
                        Toast.makeText(context,"Erro ao executar a solicitação", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                    autenticacao[0] = false;
                    Toast.makeText(context,"Falha na Conexão", Toast.LENGTH_LONG).show();
            }
        });
        return autenticacao;
    }

    @Override
    boolean[] post(Login login, Context context) {
        return new boolean[0];
    }

    @Override
    boolean[] put(Login login, Context context) {
        return new boolean[0];
    }

    @Override
    List<Login> get(Login login, Context context) {
        return null;
    }

    @Override
    List<Login> listar(Cursor cursor, Context context) {
        return null;
    }
}
