package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Cidadao implements Parcelable {
    private int idCidadao;
    private String sexo;
    private Usuario usuarioCidadao;

    protected Cidadao(Parcel in) {
        idCidadao = in.readInt();
        usuarioCidadao = in.readParcelable(Usuario.class.getClassLoader());
        sexo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idCidadao);
        dest.writeParcelable(usuarioCidadao, flags);
        dest.writeString(sexo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Cidadao> CREATOR = new Creator<Cidadao>() {
        @Override
        public Cidadao createFromParcel(Parcel in) {
            return new Cidadao(in);
        }

        @Override
        public Cidadao[] newArray(int size) {
            return new Cidadao[size];
        }
    };

    public int getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(int idCidadao) {
        this.idCidadao = idCidadao;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Usuario getUsuarioCidadao() {
        return usuarioCidadao;
    }

    public void setUsuarioCidadao(Usuario usuarioCidadao) {
        this.usuarioCidadao = usuarioCidadao;
    }
}
