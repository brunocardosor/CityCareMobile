package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Solucao implements Parcelable{

    private int idSolucao;
    private String descricaoSolucao;
    private String dirFotoSolucao;
    private Date dataSolucao;
    private Login loginSolucao;

    protected Solucao(Parcel in) {
        idSolucao = in.readInt();
        descricaoSolucao = in.readString();
        dirFotoSolucao = in.readString();
        loginSolucao = in.readParcelable(Login.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idSolucao);
        dest.writeString(descricaoSolucao);
        dest.writeString(dirFotoSolucao);
        dest.writeParcelable(loginSolucao, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Solucao> CREATOR = new Creator<Solucao>() {
        @Override
        public Solucao createFromParcel(Parcel in) {
            return new Solucao(in);
        }

        @Override
        public Solucao[] newArray(int size) {
            return new Solucao[size];
        }
    };

    public int getIdSolucao() {
        return idSolucao;
    }

    public void setIdSolucao(int idSolucao) {
        this.idSolucao = idSolucao;
    }

    public String getDescricaoSolucao() {
        return descricaoSolucao;
    }

    public void setDescricaoSolucao(String descricaoSolucao) {
        this.descricaoSolucao = descricaoSolucao;
    }

    public String getDirFotoSolucao() {
        return dirFotoSolucao;
    }

    public void setDirFotoSolucao(String dirFotoSolucao) {
        this.dirFotoSolucao = dirFotoSolucao;
    }

    public Date getDataSolucao() {
        return dataSolucao;
    }

    public void setDataSolucao(Date dataSolucao) {
        this.dataSolucao = dataSolucao;
    }

    public Login getLoginSolucao() {
        return loginSolucao;
    }

    public void setLoginSolucao(Login loginSolucao) {
        this.loginSolucao = loginSolucao;
    }
}
