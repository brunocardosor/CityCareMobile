package com.example.administrador.citycaremobile.Services;

import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Denuncia;

import java.util.List;

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
    Call<Cidadao> postCidadao(@Body Cidadao cidadao);

    @Headers("Content-Type:application/json")
    @PUT("/cidadao/put")
    Call<Boolean> putUsuario(@Body Cidadao cidadao);

    @Headers("Content-Type:application/json")
    @FormUrlEncoded
    @GET("/cidadao/get-by-login")
    Call<Cidadao> getCidadaoByLogin(@Field("login") String login,
                                    @Field("senha") String senha);
    @Headers("Content-Type:application/json")
    @FormUrlEncoded
    @GET("/cidadao/get-by-email")
    Call<Cidadao> getCidadaoByEmail(@Field("email") String email,
                                    @Field("senha") String senha);
    @Headers("Content-Type:application/json")
    @FormUrlEncoded
    @GET("/cidadao/get-by-id")
    Call<Cidadao> getCidadaoById(@Field("id_usuario") String idUsuario);


    //Serviços de denúncia
    @FormUrlEncoded
    @GET("")
    Call<List<Denuncia>> getDenuncias();

    Call<Denuncia> getDenuncia(@Field("id_denuncia") int idDenuncia);
}
