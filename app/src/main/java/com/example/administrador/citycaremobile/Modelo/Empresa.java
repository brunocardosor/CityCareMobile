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
    private Usuario usuarioEmpresa;

    protected Empresa(Parcel in) {
        idEmpresa = in.readInt();
        cnpj = in.readString();
        razaoSocial = in.readString();
        usuarioEmpresa = in.readParcelable(Usuario.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idEmpresa);
        dest.writeString(cnpj);
        dest.writeString(razaoSocial);
        dest.writeParcelable(usuarioEmpresa, flags);
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

    public Usuario getUsuarioEmpresa() {
        return usuarioEmpresa;
    }

    public void setUsuarioEmpresa(Usuario usuarioEmpresa) {
        this.usuarioEmpresa = usuarioEmpresa;
    }
}
