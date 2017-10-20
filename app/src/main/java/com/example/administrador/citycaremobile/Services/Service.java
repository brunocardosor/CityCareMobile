package com.example.administrador.citycaremobile.Services;

import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Denuncia;
import com.example.administrador.citycaremobile.Modelo.Login;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

/**
 * Created by Administrador on 01/09/2017.
 */

public interface Service {

    @POST("cidadao/cadastrar")
    @Multipart
    Call<Void> postCidadao(@Header("X-Token") String token,
                           @Part("cidadao") Cidadao cidadao,
                           @Part MultipartBody.Part foto);

    @PUT("cidadao/put")
    Call<Cidadao> putUsuario(@Header("X-Token") String token,
                             @Body Cidadao cidadao);
    @Headers("content-type:application/json")
    @DELETE
    Call<Void> deleteCidadao(@Header("X-Token") String token,
                             @Body Cidadao cidadao);

    @Headers("content-type:application/json")
    @GET("auth")
    Call<Token> autentication();

    //Login
    @POST("login")
    Call<Object> login(@Header("Content-type") String content,
                       @Header("X-Token") String token,
                       @Body Login login);


    @POST("denuncia/cadastrar")
    @Multipart
    Call<Void> denunciar(@Header("X-Token") String token,
                         @Part("denuncia") Denuncia denuncia,
                         @Part MultipartBody.Part foto);

}
