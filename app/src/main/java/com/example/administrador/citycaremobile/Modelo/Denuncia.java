package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Denuncia implements Parcelable {

    @SerializedName("id_denuncia")
    private int idDenuncia;
    @SerializedName("descricao_denuncia")
    private String descricaoDenuncia;
    @SerializedName("dir_foto_denuncia")
    private String dirFotoDenuncia;
    @SerializedName("latitude_denuncia")
    private double latitude;
    @SerializedName("longitude_denuncia")
    private double longitude;
    @SerializedName("cidade")
    private String cidade;
    @SerializedName("estado")
    private String estado;
    @SerializedName("data_denuncia")
    private String dataDenuncia;
    @SerializedName("status_denuncia")
    private Integer statusDenuncia;
    @SerializedName("fk_solucao_denuncia")
    private Solucao solucaoDenuncia;
    @SerializedName("fk_categoria_denuncia")
    private Categoria categoriaDenuncia;
    @SerializedName("fk_login_denuncia")
    private Login login;

    public Denuncia() {
    }

    public Denuncia(Integer idDenuncia){
        this.idDenuncia = idDenuncia;
    }

    public Denuncia(int idDenuncia, String descricaoDenuncia, String dirFotoDenuncia, double latitude, double longitude, String cidade, String estado, String dataDenuncia, Integer statusDenuncia, Solucao solucaoDenuncia, Categoria categoriaDenuncia, Login login) {
        this.idDenuncia = idDenuncia;
        this.descricaoDenuncia = descricaoDenuncia;
        this.dirFotoDenuncia = dirFotoDenuncia;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cidade = cidade;
        this.estado = estado;
        this.dataDenuncia = dataDenuncia;
        this.statusDenuncia = statusDenuncia;
        this.solucaoDenuncia = solucaoDenuncia;
        this.categoriaDenuncia = categoriaDenuncia;
        this.login = login;
    }

    protected Denuncia(Parcel in) {
        idDenuncia = in.readInt();
        descricaoDenuncia = in.readString();
        dirFotoDenuncia = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        cidade = in.readString();
        estado = in.readString();
        dataDenuncia = in.readString();
        statusDenuncia = in.readInt();
        solucaoDenuncia = in.readParcelable(Solucao.class.getClassLoader());
        categoriaDenuncia = in.readParcelable(Categoria.class.getClassLoader());
        login = in.readParcelable(Login.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idDenuncia);
        dest.writeString(descricaoDenuncia);
        dest.writeString(dirFotoDenuncia);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(cidade);
        dest.writeString(estado);
        dest.writeString(dataDenuncia);
        dest.writeInt(statusDenuncia);
        dest.writeParcelable(solucaoDenuncia, flags);
        dest.writeParcelable(categoriaDenuncia, flags);
        dest.writeParcelable(login, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Denuncia> CREATOR = new Creator<Denuncia>() {
        @Override
        public Denuncia createFromParcel(Parcel in) {
            return new Denuncia(in);
        }

        @Override
        public Denuncia[] newArray(int size) {
            return new Denuncia[size];
        }
    };

    public int getIdDenuncia() {
        return idDenuncia;
    }

    public void setIdDenuncia(int idDenuncia) {
        this.idDenuncia = idDenuncia;
    }

    public String getDescricaoDenuncia() {
        return descricaoDenuncia;
    }

    public void setDescricaoDenuncia(String descricaoDenuncia) {
        this.descricaoDenuncia = descricaoDenuncia;
    }

    public String getDirFotoDenuncia() {
        return dirFotoDenuncia;
    }

    public void setDirFotoDenuncia(String dirFotoDenuncia) {
        this.dirFotoDenuncia = dirFotoDenuncia;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public String getDataDenuncia() {
        return dataDenuncia;
    }

    public void setDataDenuncia(String dataDenuncia) {
        this.dataDenuncia = dataDenuncia;
    }

    public Integer getStatusDenuncia() {
        return statusDenuncia;
    }

    public void setStatusDenuncia(Integer statusDenuncia) {
        this.statusDenuncia = statusDenuncia;
    }

    public Solucao getSolucaoDenuncia() {
        return solucaoDenuncia;
    }

    public void setSolucaoDenuncia(Solucao solucaoDenuncia) {
        this.solucaoDenuncia = solucaoDenuncia;
    }

    public Categoria getCategoriaDenuncia() {
        return categoriaDenuncia;
    }

    public void setCategoriaDenuncia(Categoria categoriaDenuncia) {
        this.categoriaDenuncia = categoriaDenuncia;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
}
