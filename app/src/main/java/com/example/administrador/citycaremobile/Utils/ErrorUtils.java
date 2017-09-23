package com.example.administrador.citycaremobile.Utils;

import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Administrador on 21/09/2017.
 */

public class ErrorUtils {

    public static APIError parseError(Response<?> response){
        Converter<ResponseBody, APIError> converter = CallService.getRetrofit().responseBodyConverter(APIError.class, new Annotation[0]);
        APIError error;
        try{
            error = converter.convert(response.errorBody());
        } catch (IOException e){
            return new APIError();
        }

        return error;
    }
}
