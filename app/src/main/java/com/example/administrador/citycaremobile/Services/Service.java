package com.example.administrador.citycaremobile.Services;


import com.example.administrador.citycaremobile.Modelo.Administrador;
import com.example.administrador.citycaremobile.Modelo.Agiliza;
import com.example.administrador.citycaremobile.Modelo.Categoria;
import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Comentario;
import com.example.administrador.citycaremobile.Modelo.Denuncia;
import com.example.administrador.citycaremobile.Modelo.Empresa;
import com.example.administrador.citycaremobile.Modelo.Login;
import com.example.administrador.citycaremobile.Modelo.Solucao;
import com.example.administrador.citycaremobile.Modelo.Usuario;

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

    //Call Services Login
    @FormUrlEncoded
    @DELETE("")
    Call<Boolean> deleteLogin(@Body Login login);

    @FormUrlEncoded
    @POST("")
    Call<Boolean> postLogin(@Body Login login);

    @FormUrlEncoded
    @PUT("")
    Call<Boolean> putLogin(@Body Login login);

    @FormUrlEncoded
    @GET("")
    Call<Boolean> getLogin(@Field("login") String login,
                           @Field("senha") String senha);

    @FormUrlEncoded
    @GET("")
    Call<Boolean> getLoginByEmail(@Field("email") String email,
                                  @Field("senha") String senha);


    //Call Services Usuario
    @FormUrlEncoded
    @DELETE("")
    Call<Usuario> deleteUsuario(@Body Usuario usuario);

    @FormUrlEncoded
    @POST("")
    Call<Usuario> postUsuario(@Body Usuario usuario);

    @FormUrlEncoded
    @PUT
    Call<Usuario> putUsuario(@Body Usuario usuario);

    @FormUrlEncoded
    @GET
    Call<Usuario> getUsuario(@Body Usuario usuario);


    //Call Services Administrador
    @FormUrlEncoded
    @DELETE("")
    Call<Administrador> deleteAdministrador(@Body Administrador administrador);

    @FormUrlEncoded
    @POST("")
    Call<Administrador> postAdministrador(@Body Administrador administrador);

    @FormUrlEncoded
    @PUT
    Call<Administrador> putAdministrador(@Body Administrador administrador);

    @FormUrlEncoded
    @GET
    Call<Administrador> getAdministrador(@Body Administrador administrador);


    //Call Services Empresa
    @FormUrlEncoded
    @DELETE("")
    Call<Empresa> deleteEmpresa(@Body Empresa empresa);

    @FormUrlEncoded
    @POST("")
    Call<Empresa> postEmpresa(@Body Empresa empresa);

    @FormUrlEncoded
    @PUT
    Call<Empresa> putEmpresa(@Body Empresa empresa);

    @FormUrlEncoded
    @GET
    Call<Empresa> getEmpresa(@Body Empresa empresa);

    //Call Services Cidadao
    @FormUrlEncoded
    @DELETE("")
    Call<Cidadao> deleteCidadao(@Body Cidadao cidadao);

    @FormUrlEncoded
    @POST("")
    Call<Cidadao> postCidadao(@Body Cidadao cidadao);

    @FormUrlEncoded
    @PUT
    Call<Cidadao> putCidadao(@Body Cidadao cidadao);

    @FormUrlEncoded
    @GET
    Call<Cidadao> getCidadao(@Body Cidadao cidadao);

    //Call Services Solucao
    @FormUrlEncoded
    @DELETE("")
    Call<Solucao> deleteSolucao(@Body Solucao solucao);

    @FormUrlEncoded
    @POST("")
    Call<Solucao> postSolucao(@Body Solucao solucao);

    @FormUrlEncoded
    @PUT
    Call<Solucao> putSolucao(@Body Solucao solucao);

    @FormUrlEncoded
    @GET
    Call<Solucao> getSolucao(@Body Solucao solucao);

    //Call Service Categoria
    @FormUrlEncoded
    @DELETE("")
    Call<Categoria> deleteCategoria(@Body Categoria categoria);

    @FormUrlEncoded
    @POST("")
    Call<Categoria> postCategoria(@Body Categoria categoria);

    @FormUrlEncoded
    @PUT
    Call<Categoria> putCategoria(@Body Categoria categoria);

    @FormUrlEncoded
    @GET
    Call<Categoria> getCategoria(@Body Categoria categoria);

    //Call Service Denuncia
    @FormUrlEncoded
    @DELETE("")
    Call<Denuncia> deleteDenuncia(@Body Denuncia denuncia);

    @FormUrlEncoded
    @POST("")
    Call<Denuncia> postDenuncia(@Body Denuncia denuncia);

    @FormUrlEncoded
    @PUT
    Call<Denuncia> putDenuncia(@Body Denuncia denuncia);

    @FormUrlEncoded
    @GET
    Call<Denuncia> getDenuncia(@Body Denuncia denuncia);

    //Call Services Comentario
    @FormUrlEncoded
    @DELETE("")
    Call<Comentario> deleteComentario(@Body Comentario comentario);

    @FormUrlEncoded
    @POST("")
    Call<Comentario> postComentario(@Body Comentario comentario);

    @FormUrlEncoded
    @PUT
    Call<Comentario> putComentario(@Body Comentario comentario);

    @FormUrlEncoded
    @GET
    Call<Comentario> getComentario(@Body Comentario comentario);

    //Call Services Agiliza
    @FormUrlEncoded
    @DELETE("")
    Call<Agiliza> deleteAgiliza(@Body Agiliza agiliza);

    @FormUrlEncoded
    @POST("")
    Call<Agiliza> postAgiliza(@Body Agiliza agiliza);

    @FormUrlEncoded
    @PUT
    Call<Agiliza> putAgiliza(@Body Agiliza agiliza);

    @FormUrlEncoded
    @GET
    Call<Agiliza> getAgiliza(@Body Agiliza agiliza);
}
