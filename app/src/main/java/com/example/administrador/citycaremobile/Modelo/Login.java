package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Login implements Parcelable {

    private int idLogin;
    private String cidade;
    private String estado;
    private String dirFotoUsuario;
    private String email;
    private String login;
    private String senha;
    private boolean status_login;
    private boolean asAdministrador;

    protected Login(Parcel in) {
        idLogin = in.readInt();
        cidade = in.readString();
        estado = in.readString();
        dirFotoUsuario = in.readString();
        email = in.readString();
        login = in.readString();
        senha = in.readString();
        status_login = in.readByte() != 0;
        asAdministrador = in.readByte() != 0;
    }

    public Login(){

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idLogin);
        dest.writeString(cidade);
        dest.writeString(estado);
        dest.writeString(dirFotoUsuario);
        dest.writeString(email);
        dest.writeString(login);
        dest.writeString(senha);
        dest.writeByte((byte) (status_login ? 1 : 0));
        dest.writeByte((byte) (asAdministrador ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Login> CREATOR = new Creator<Login>() {
        @Override
        public Login createFromParcel(Parcel in) {
            return new Login(in);
        }

        @Override
        public Login[] newArray(int size) {
            return new Login[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isStatus_login() {
        return status_login;
    }

    public void setStatus_login(boolean status_login) {
        this.status_login = status_login;
    }

    public boolean isAdministrador() {
        return asAdministrador;
    }

    public void setAdministrador(boolean administrador) {
        this.asAdministrador = administrador;
    }

    public int getIdLogin() {
        return idLogin;
    }

    public void setIdLogin(int idLogin) {
        this.idLogin = idLogin;
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
