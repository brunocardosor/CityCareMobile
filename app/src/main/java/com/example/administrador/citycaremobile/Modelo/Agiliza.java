package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Agiliza implements Parcelable, Comparable<Agiliza> {

    @SerializedName("fk_login_agiliza")
    private Login login;
    @SerializedName("fk_denuncia_agiliza")
    private Denuncia usuarioDenuncia;
    @SerializedName("interacao")
    private boolean interacao;

    public Agiliza() {
    }

    public Agiliza(Login login, Denuncia usuarioDenuncia, boolean interacao) {
        this.login = login;
        this.usuarioDenuncia = usuarioDenuncia;
        this.interacao = interacao;
    }

    protected Agiliza(Parcel in) {
        login = in.readParcelable(Login.class.getClassLoader());
        usuarioDenuncia = in.readParcelable(Denuncia.class.getClassLoader());
        interacao = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(login, flags);
        dest.writeParcelable(usuarioDenuncia, flags);
        dest.writeByte((byte) (interacao ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Agiliza> CREATOR = new Creator<Agiliza>() {
        @Override
        public Agiliza createFromParcel(Parcel in) {
            return new Agiliza(in);
        }

        @Override
        public Agiliza[] newArray(int size) {
            return new Agiliza[size];
        }
    };

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Denuncia getUsuarioDenuncia() {
        return usuarioDenuncia;
    }

    public void setUsuarioDenuncia(Denuncia usuarioDenuncia) {
        this.usuarioDenuncia = usuarioDenuncia;
    }

    public boolean isInteracao() {
        return interacao;
    }

    public void setInteracao(boolean interacao) {
        this.interacao = interacao;
    }

    @Override
    public int compareTo(@NonNull Agiliza o) {
        if (login.getIdLogin() == o.getLogin().getIdLogin() &&
                usuarioDenuncia.getIdDenuncia() == o.getUsuarioDenuncia().getIdDenuncia()){
            return 1;
        } else {
            return 0;
        }
    }
}
