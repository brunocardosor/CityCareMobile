package com.example.administrador.citycaremobile.Services;

import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Denuncia;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Administrador on 01/09/2017.
 */

public interface Service {

    //Serviços de Cidadao
    @Headers("Content-Type: application/json")
    @POST("cidadao/cadastrar")
    Call<Void> postCidadao(@Body Cidadao cidadao);

    @Headers("Content-Type:application/json")
    @PUT("/cidadao/put")
    Call<Boolean> putUsuario(@Body Cidadao cidadao);

    //Login
    @Headers("Content-Type:application/json")
    @FormUrlEncoded
    @POST("/login")
    Call<Object> getAcessByLogin(@Field("login") String login,
                                    @Field("senha") String senha);

}
