package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Usuario implements Parcelable {

    private Login login;
    private Tipo tipo;
    private UsuarioGenerico usuarioGeneric;

    public Usuario(Login login, Cidadao cidadao, UsuarioGenerico usrGeneric){
        this.login = login;
        this.tipo = new Tipo(cidadao);
        this.usuarioGeneric = usrGeneric;
    }

    public Usuario(Login login, Empresa empresa, UsuarioGenerico usrGeneric){
        this.login = login;
        this.tipo = new Tipo(empresa);
        this.usuarioGeneric = usrGeneric;
    }

    public Usuario(Login login, Administrador administrador, UsuarioGenerico usrGeneric){
        this.login = login;
        this.tipo = new Tipo(administrador);
        this.usuarioGeneric = usrGeneric;
    }

    protected Usuario(Parcel in) {
        login = in.readParcelable(Login.class.getClassLoader());
        tipo = in.readParcelable(Object.class.getClassLoader());
        usuarioGeneric = in.readParcelable(UsuarioGenerico.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(login, flags);
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

    public Login getLogin(){
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Object getTipo() {
        if (tipo.getAdministrador() != null){
            return tipo.getAdministrador();
        } else if(tipo.getEmpresa() != null){
            return tipo.getEmpresa();
        } else if(tipo.getCidadao() != null){
            return tipo.getCidadao();
        } else {
            return null;
        }
    }

    public UsuarioGenerico getUsuarioGeneric() {
        return usuarioGeneric;
    }

    public void setUsuarioGeneric(UsuarioGenerico usuarioGenerico) {
        this.usuarioGeneric = usuarioGenerico;
    }

    private class Tipo {

        private Administrador administrador;
        private Cidadao cidadao;
        private Empresa empresa;

        public Administrador getAdministrador() {
            return administrador;
        }

        private Cidadao getCidadao() {
            return cidadao;
        }

        private Empresa getEmpresa() {
            return empresa;
        }

        private Tipo(Administrador administrador){
            this.administrador = administrador;
        }

        private Tipo(Cidadao cidadao){
            this.cidadao = cidadao;
        }

        private Tipo(Empresa empresa){
            this.empresa = empresa;
        }
    }
}
