package com.example.administrador.citycaremobile.Services;

import com.example.administrador.citycaremobile.Modelo.Denuncia;
import com.example.administrador.citycaremobile.Modelo.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

   @Headers("")


   //Serviços de Usuário
   @FormUrlEncoded
   @POST("")
   Call<Integer> postUsuario(@Body Usuario usuario);

    @FormUrlEncoded
    @PUT("")
    Call<Integer> putUsuario(@Body Usuario usuario);

    @FormUrlEncoded
    @GET("")
    Call<Usuario> getUsuarioByLogin(@Field("login") String login,
                                    @Field("senha") String senha);

    @FormUrlEncoded
    @GET("")
    Call<Usuario> getUsuarioByEmail(@Field("email") String email,
                                    @Field("senha") String senha);

    @GET("")
    Call<Usuario> getUsuarioById(@Field("id_usuario") String idUsuario);



    //Serviços de denúncia
    @FormUrlEncoded
    @GET("")
    Call<List<Denuncia>> getDenuncias();

    Call<Denuncia> getDenuncia(@Field("id_denuncia") int idDenuncia);


}
