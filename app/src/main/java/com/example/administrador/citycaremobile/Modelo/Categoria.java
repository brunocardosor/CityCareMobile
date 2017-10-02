package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Categoria implements Parcelable {

    private int idCategoria;
    private String descricaoCategoria;

    protected Categoria(Parcel in) {
        idCategoria = in.readInt();
        descricaoCategoria = in.readString();
    }

    public Categoria(){

    }

    public Categoria(int idCategoria, String descricaoCategoria) {
        this.idCategoria = idCategoria;
        this.descricaoCategoria = descricaoCategoria;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idCategoria);
        dest.writeString(descricaoCategoria);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Categoria> CREATOR = new Creator<Categoria>() {
        @Override
        public Categoria createFromParcel(Parcel in) {
            return new Categoria(in);
        }

        @Override
        public Categoria[] newArray(int size) {
            return new Categoria[size];
        }
    };

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDescricaoCategoria() {
        return descricaoCategoria;
    }

    public void setDescricaoCategoria(String descricaoCategoria) {
        this.descricaoCategoria = descricaoCategoria;
    }

    @Override
    public String toString() {
        return descricaoCategoria;
    }
}
