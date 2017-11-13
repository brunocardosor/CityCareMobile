package com.example.administrador.citycaremobile.Services;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrador on 01/09/2017.
 * Classe de configuração e chamada do serviço
 */

public class CallService {

    //Url base do webservice
    public static final String API_BASE_URL =("https://servico.projetocitycare.com.br/");

    static Retrofit retrofit;

    public static <S> S createService(Class<S> serviceClass){

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS);

        httpClient.addInterceptor(loggingInterceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, String username, String password){
        AutenticadorInteceptor autenticadorInteceptor = new AutenticadorInteceptor(username, password);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(autenticadorInteceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit.create(serviceClass);
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}