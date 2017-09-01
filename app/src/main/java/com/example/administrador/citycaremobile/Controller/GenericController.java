package com.example.administrador.citycaremobile.Controller;

import android.content.Context;
import android.database.Cursor;

import java.util.List;

import retrofit2.http.GET;

/**
 * Created by Administrador on 01/09/2017.
 */

public abstract class GenericController<T> {

    Context context;

    public GenericController(Context context){
        this.context = context;
    }

    abstract boolean[] delete(T t, Context context);
    abstract boolean[] post(T t, Context context);
    abstract boolean[] put(T t, Context context);
    abstract List<T> get(T t, Context context);
    abstract List<T> listar(Cursor cursor, Context context);
}
