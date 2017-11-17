package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Solucao implements Parcelable{

    @SerializedName("id_solucao")
    private int idSolucao;
    @SerializedName("descricao_solucao")
    private String descricaoSolucao;
    @SerializedName("dir_foto_solucao")
    private String dirFotoSolucao;
    @SerializedName("data_solucao")
    private String dataSolucao;
    @SerializedName("fk_login_solucao")
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

    public String getDataSolucao() {
        return dataSolucao;
    }

    public void setDataSolucao(String dataSolucao) {
        this.dataSolucao = dataSolucao;
    }

    public Login getLoginSolucao() {
        return loginSolucao;
    }

    public void setLoginSolucao(Login loginSolucao) {
        this.loginSolucao = loginSolucao;
    }
}
