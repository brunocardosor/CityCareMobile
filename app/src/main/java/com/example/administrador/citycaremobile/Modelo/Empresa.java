package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Empresa implements Parcelable {

    @SerializedName("id_empresa")
    private int idEmpresa;
    @SerializedName("cnpj")
    private String cnpj;
    @SerializedName("razao_social")
    private String razaoSocial;
    @SerializedName("nome_fantasia")
    private String nomeFantasia;
    @SerializedName("cidadae")
    private String cidade;
    @SerializedName("estado")
    private String estado;
    @SerializedName("fk_empresa_login")
    private Login loginEmpresa;
    @SerializedName("dir_foto_usuario")
    private String dirFotoUsuario;

    public Empresa() {
    }

    public Empresa(int idEmpresa, String cnpj, String razaoSocial, String nomeFantasia, String cidade, String estado, String dirFotoUsuario, Login loginEmpresa) {
        this.idEmpresa = idEmpresa;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.cidade = cidade;
        this.estado = estado;
        this.loginEmpresa = loginEmpresa;
        this.dirFotoUsuario = dirFotoUsuario;
    }

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

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
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

    public Login getLoginEmpresa() {
        return loginEmpresa;
    }

    public void setLoginEmpresa(Login loginEmpresa) {
        this.loginEmpresa = loginEmpresa;
    }

    public String getDirFotoUsuario() {
        return dirFotoUsuario;
    }

    public void setDirFotoUsuario(String dirFotoUsuario) {
        this.dirFotoUsuario = dirFotoUsuario;
    }

    public static Creator<Empresa> getCREATOR() {
        return CREATOR;
    }

    protected Empresa(Parcel in) {
        idEmpresa = in.readInt();
        cnpj = in.readString();
        razaoSocial = in.readString();
        nomeFantasia = in.readString();
        cidade = in.readString();
        estado = in.readString();
        loginEmpresa = in.readParcelable(Login.class.getClassLoader());
        dirFotoUsuario = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idEmpresa);
        dest.writeString(cnpj);
        dest.writeString(razaoSocial);
        dest.writeString(nomeFantasia);
        dest.writeString(cidade);
        dest.writeString(estado);
        dest.writeParcelable(loginEmpresa, flags);
        dest.writeString(dirFotoUsuario);
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
