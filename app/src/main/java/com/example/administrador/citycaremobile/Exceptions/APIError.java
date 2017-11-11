package com.example.administrador.citycaremobile.Exceptions;

/**
 * Created by Administrador on 21/09/2017.
 */

public class APIError {

    public APIError(){}

    private int errorCode;
    private String message;

    public int getCode(){
        return errorCode;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

}
