package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Agiliza implements Parcelable {

    private Usuario usuarioAgiliza;
    private Denuncia usuarioDenuncia;
    private boolean interacao;

    protected Agiliza(Parcel in) {
        usuarioAgiliza = in.readParcelable(Usuario.class.getClassLoader());
        usuarioDenuncia = in.readParcelable(Denuncia.class.getClassLoader());
        interacao = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(usuarioAgiliza, flags);
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

    public Usuario getUsuarioAgiliza() {
        return usuarioAgiliza;
    }

    public void setUsuarioAgiliza(Usuario usuarioAgiliza) {
        this.usuarioAgiliza = usuarioAgiliza;
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
}
