package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Denuncia implements Parcelable {

    private int idDenuncia;
    private String descricaoDenuncia;
    private String dirFotoDenuncia;
    private double latitude;
    private double longitude;
    private String cidade;
    private String estado;
    private Date dataDenuncia;
    private boolean statusDenuncia;
    private Solucao solucaoDenuncia;
    private Categoria categoriaDenuncia;
    private Login login;

    public Denuncia() {
    }

    public Denuncia(int idDenuncia, String descricaoDenuncia, String dirFotoDenuncia, double latitude, double longitude, String cidade, String estado, Date dataDenuncia, boolean statusDenuncia, Solucao solucaoDenuncia, Categoria categoriaDenuncia, Login login) {
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
        statusDenuncia = in.readByte() != 0;
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
        dest.writeByte((byte) (statusDenuncia ? 1 : 0));
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

    public Date getDataDenuncia() {
        return dataDenuncia;
    }

    public void setDataDenuncia(Date dataDenuncia) {
        this.dataDenuncia = dataDenuncia;
    }

    public boolean isStatusDenuncia() {
        return statusDenuncia;
    }

    public void setStatusDenuncia(boolean statusDenuncia) {
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
