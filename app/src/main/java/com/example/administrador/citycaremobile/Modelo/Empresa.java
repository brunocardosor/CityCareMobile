package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Empresa implements Parcelable {

    private int idEmpresa;
    private String cnpj;
    private String razaoSocial;
    private String nome_fantasia;
    private Login loginEmpresa;

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNome_fantasia() {
        return nome_fantasia;
    }

    public void setNome_fantasia(String nome_fantasia) {
        this.nome_fantasia = nome_fantasia;
    }

    public Login getLoginEmpresa() {
        return loginEmpresa;
    }

    public void setLoginEmpresa(Login loginEmpresa) {
        this.loginEmpresa = loginEmpresa;
    }

    public static Creator<Empresa> getCREATOR() {
        return CREATOR;
    }

    protected Empresa(Parcel in) {
        idEmpresa = in.readInt();
        cnpj = in.readString();
        razaoSocial = in.readString();
        nome_fantasia = in.readString();
        loginEmpresa = in.readParcelable(Login.class.getClassLoader());
    }

    public Empresa(int idEmpresa, String cnpj, String razaoSocial, String nome_fantasia, Login loginEmpresa) {
        this.idEmpresa = idEmpresa;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.nome_fantasia = nome_fantasia;
        this.loginEmpresa = loginEmpresa;
    }

    public Empresa() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idEmpresa);
        dest.writeString(cnpj);
        dest.writeString(razaoSocial);
        dest.writeString(nome_fantasia);
        dest.writeParcelable(loginEmpresa, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Empresa> CREATOR = new Creator<Empresa>() {
        @Override
        public Empresa createFromParcel(Parcel in) {
            return new Empresa(in);
        }

        @Override
        public Empresa[] newArray(int size) {
            return new Empresa[size];
        }
    };
}
