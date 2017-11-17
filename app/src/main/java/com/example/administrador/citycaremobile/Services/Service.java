package com.example.administrador.citycaremobile.Services;

import com.example.administrador.citycaremobile.Modelo.Agiliza;
import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Comentario;
import com.example.administrador.citycaremobile.Modelo.Denuncia;
import com.example.administrador.citycaremobile.Modelo.Login;
import com.example.administrador.citycaremobile.Modelo.Postagem;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
    Call<Denuncia> denunciar(@Header("X-Token") String token,
                         @Part MultipartBody.Part foto,
                         @Part("denuncia") RequestBody denuncia);

    @POST("verificar/email")
    @Headers("content-type:application/json")
    Call<Void> verificarEmail(@Header("X-Token") String token,
                              @Body Login email);

    @POST("verificar/login")
    @Headers("content-type:application/json")
    Call<Void> verificarLogin(@Header("X-Token") String token,
                              @Body Login login);
    @GET("denuncia/all")
    Call<ArrayList<Postagem>> getPostagens(@Header("X-Token") String token);

    @POST("retorno")
    @Headers("content-type:application/json")
    Call<Object> getUsuario(@Header("X-Token") String token,
                            @Body Login login);

    @POST("agiliza/cadastrar")
    @Headers("content-type:application/json")
    Call<Void> agilizar(@Header("X-Token") String token,
                        @Body Agiliza agiliza);

    @POST("comentario/cadastrar")
    @Headers("content-type:application/json")
    Call<Comentario> enviarComentario(@Header("X-Token") String token,
                                @Body Comentario comentario);

    @POST("comentario/deletar")
    @Headers("content-type:application/json")
    Call<Void> deletarComentario(@Header("X-Token") String token,
                                @Body Comentario comentario);

    @POST("denuncia/feedSecundario")
    @Headers("content-type:application/json")
    Call<ArrayList<Postagem>> carregarPostagem(@Header("X-Token") String token,
                                @Body Denuncia denuncia);
    @POST("denuncia/excluir")
    @Headers("content-type:application/json")
    Call<Void> excluirPostagem(@Header("X-Token") String token,
                               @Body Denuncia denuncia);
    @GET("denuncia/maps")
    @Headers("content-type:application/json")
    Call<ArrayList<Postagem>> mapsPostagens(@Header("X-Token") String token);

    @POST("denuncia/alterar")
    @Multipart
    Call<Denuncia> atualizarDenuncia(@Header("X-Token") String token,
                                     @Part MultipartBody.Part file,
                                     @Part ("denuncia") Denuncia denuncia);
    @POST("denuncia/excluir")
    @Headers("content-type:application/json")
    Call<Void> recuperarSenha(@Header("X-Token") String token,
                              @Body Login login);
}
