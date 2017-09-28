package com.example.administrador.citycaremobile.Services;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrador on 27/09/2017.
 */

public class AutenticadorInteceptor implements Interceptor {

    private String authToken;

    public AutenticadorInteceptor(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder().header("Authorization", authToken).addHeader("Content-Type","application/json");

        Request request = builder.build();
        return chain.proceed(request);
    }
}
