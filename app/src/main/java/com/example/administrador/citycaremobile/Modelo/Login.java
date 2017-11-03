package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by administrador on 01/09/2017.
 */

public class Login implements Parcelable {
    @SerializedName("id_login")
    private Integer idLogin;
    @SerializedName("email")
    private String email;
    @SerializedName("login")
    private String login;
    @SerializedName("senha")
    private String senha;
    @SerializedName("status_login")
    private boolean status_login;
    @SerializedName("administrador")
    private boolean administrador;

    public Login(){}

    public Login(Integer idLogin, String email, String login, String senha, boolean status_login, boolean administrador) {
        this.idLogin = idLogin;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.status_login = status_login;
        this.administrador = administrador;
    }

    public Login(Integer idLogin){
        this.idLogin = idLogin;
    }


    protected Login(Parcel in) {
        email = in.readString();
        login = in.readString();
        senha = in.readString();
        status_login = in.readByte() != 0;
        administrador = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(login);
        dest.writeString(senha);
        dest.writeByte((byte) (status_login ? 1 : 0));
        dest.writeByte((byte) (administrador ? 1 : 0));
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

    public Integer getIdLogin() {
        return idLogin;
    }

    public void setIdLogin(Integer idLogin) {
        this.idLogin = idLogin;
    }

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
        return administrador;
    }

    public void setAdministrador(boolean asAdministrador) {
        this.administrador = asAdministrador;
    }
}
