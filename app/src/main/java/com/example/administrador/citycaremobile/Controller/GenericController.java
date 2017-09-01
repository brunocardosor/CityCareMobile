package com.example.administrador.citycaremobile.Controller;

import android.database.Cursor;

import java.util.List;

/**
 * Created by Administrador on 01/09/2017.
 */

public interface GenericController<T> {

    void delete(T t);
    void post(T t);
    void put(T t);
    void get(T t);
    List<T> listar(Cursor cursor);
}
