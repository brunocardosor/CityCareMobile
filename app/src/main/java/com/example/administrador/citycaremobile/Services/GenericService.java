package com.example.administrador.citycaremobile.Services;

import android.telecom.Call;

import com.example.administrador.citycaremobile.Modelo.Login;

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

public interface GenericService {

    @Headers("")

    //Call Services Login
    @FormUrlEncoded
    @DELETE("")
    Call deleteLogin();

    @FormUrlEncoded
    @POST("")
    Call postLogin();

    @FormUrlEncoded
    @PUT
    Call putLogin();

    @FormUrlEncoded
    @GET
    Call getLogin();


    //Call Services Usuario
    @FormUrlEncoded
    @DELETE("")
    Call deleteUsuario();

    @FormUrlEncoded
    @POST("")
    Call postUsuario();

    @FormUrlEncoded
    @PUT
    Call putUsuario();

    @FormUrlEncoded
    @GET
    Call getUsuario();


    //Call Services Administrador
    @FormUrlEncoded
    @DELETE("")
    Call deleteAdministrador();

    @FormUrlEncoded
    @POST("")
    Call postAdministrador();

    @FormUrlEncoded
    @PUT
    Call putAdministrador();

    @FormUrlEncoded
    @GET
    Call getAdministrador();


    //Call Services Empresa
    @FormUrlEncoded
    @DELETE("")
    Call deleteEmpresa();

    @FormUrlEncoded
    @POST("")
    Call postEmpresa();

    @FormUrlEncoded
    @PUT
    Call putEmpresa();

    @FormUrlEncoded
    @GET
    Call getEmpresa();

    //Call Services Cidadao
    @FormUrlEncoded
    @DELETE("")
    Call deleteCidadao();

    @FormUrlEncoded
    @POST("")
    Call postCidadao();

    @FormUrlEncoded
    @PUT
    Call putCidadao();

    @FormUrlEncoded
    @GET
    Call getCidadao();

    //Call Services Solucao
    @FormUrlEncoded
    @DELETE("")
    Call deleteSolucao();

    @FormUrlEncoded
    @POST("")
    Call postSolucao();

    @FormUrlEncoded
    @PUT
    Call putSolucao();

    @FormUrlEncoded
    @GET
    Call getSolucao();

    //Call Service Categoria
    @FormUrlEncoded
    @DELETE("")
    Call deleteCategoria();

    @FormUrlEncoded
    @POST("")
    Call postCategoria();

    @FormUrlEncoded
    @PUT
    Call putCategoria();

    @FormUrlEncoded
    @GET
    Call getCategoria();

    //Call Service Denuncia
    @FormUrlEncoded
    @DELETE("")
    Call deleteDenuncia();

    @FormUrlEncoded
    @POST("")
    Call postDenuncia();

    @FormUrlEncoded
    @PUT
    Call putDenuncia();

    @FormUrlEncoded
    @GET
    Call getDenuncia();

    //Call Services Comentario
    @FormUrlEncoded
    @DELETE("")
    Call deleteComentario();

    @FormUrlEncoded
    @POST("")
    Call postComentario();

    @FormUrlEncoded
    @PUT
    Call putComentario();

    @FormUrlEncoded
    @GET
    Call getComentario();

    //Call Services Agiliza
    @FormUrlEncoded
    @DELETE("")
    Call deleteAgiliza();

    @FormUrlEncoded
    @POST("")
    Call postAgiliza();

    @FormUrlEncoded
    @PUT
    Call putAgiliza();

    @FormUrlEncoded
    @GET
    Call getAgiliza();
}
