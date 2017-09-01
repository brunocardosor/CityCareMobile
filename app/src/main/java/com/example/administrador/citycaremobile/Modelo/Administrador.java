package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Administrador implements Parcelable{

    private int idAdministrador;
    private String cpf;
    private String telefone;
    private String endereço;
    private Usuario usuarioAdministrador;
    private Login loginAdministrador;

    protected Administrador(Parcel in) {
        idAdministrador = in.readInt();
        cpf = in.readString();
        telefone = in.readString();
        endereço = in.readString();
        usuarioAdministrador = in.readParcelable(Usuario.class.getClassLoader());
        loginAdministrador = in.readParcelable(Login.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idAdministrador);
        dest.writeString(cpf);
        dest.writeString(telefone);
        dest.writeString(endereço);
        dest.writeParcelable(usuarioAdministrador, flags);
        dest.writeParcelable(loginAdministrador, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Administrador> CREATOR = new Creator<Administrador>() {
        @Override
        public Administrador createFromParcel(Parcel in) {
            return new Administrador(in);
        }

        @Override
        public Administrador[] newArray(int size) {
            return new Administrador[size];
        }
    };

    public int getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(int idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereço() {
        return endereço;
    }

    public void setEndereço(String endereço) {
        this.endereço = endereço;
    }

    public Usuario getUsuarioAdministrador() {
        return usuarioAdministrador;
    }

    public void setUsuarioAdministrador(Usuario usuarioAdministrador) {
        this.usuarioAdministrador = usuarioAdministrador;
    }

    public Login getLoginAdministrador() {
        return loginAdministrador;
    }

    public void setLoginAdministrador(Login loginAdministrador) {
        this.loginAdministrador = loginAdministrador;
    }
}
