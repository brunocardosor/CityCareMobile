package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Cidadao implements Parcelable {
    private int idCidadao;
    private String nome;
    private String sobrenome;
    private String sexo;
    private Login loginCidadao;

    protected Cidadao(Parcel in) {
        idCidadao = in.readInt();
        nome = in.readString();
        sobrenome = in.readString();
        sexo = in.readString();
        loginCidadao = in.readParcelable(Login.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idCidadao);
        dest.writeString(nome);
        dest.writeString(sobrenome);
        dest.writeString(sexo);
        dest.writeParcelable(loginCidadao, flags);
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Login getLoginCidadao() {
        return loginCidadao;
    }

    public void setLoginCidadao(Login loginCidadao) {
        this.loginCidadao = loginCidadao;
    }
}
