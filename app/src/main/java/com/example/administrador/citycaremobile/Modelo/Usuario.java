package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Usuario implements Parcelable {
    private int idUsuario;
    private String nome;
    private String sobrenome;
    private String cidade;
    private String estado;
    private String dirFotoUsuario;


    protected Usuario(Parcel in) {
        idUsuario = in.readInt();
        nome = in.readString();
        sobrenome = in.readString();
        cidade = in.readString();
        estado = in.readString();
        dirFotoUsuario = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idUsuario);
        dest.writeString(nome);
        dest.writeString(sobrenome);
        dest.writeString(cidade);
        dest.writeString(estado);
        dest.writeString(dirFotoUsuario);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDirFotoUsuario() {
        return dirFotoUsuario;
    }

    public void setDirFotoUsuario(String dirFotoUsuario) {
        this.dirFotoUsuario = dirFotoUsuario;
    }
}
