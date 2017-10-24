package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Cidadao implements Parcelable {

    @SerializedName("id_cidadao")
    private Integer idCidadao;
    @SerializedName("nome")
    private String nome;
    @SerializedName("sobrenome")
    private String sobrenome;
    @SerializedName("cidade")
    private String cidade;
    @SerializedName("estado")
    private String estado;
    @SerializedName("dir_foto_usuario")
    private String dirFotoUsuario;
    @SerializedName("sexo")
    private String sexo;
    @SerializedName("fk_login_cidadao")
    private Login loginCidadao;


    public Cidadao(Integer idCidadao, String nome, String sobrenome,
                   String cidade, String estado, String dir_foto_usuario,
                   String sexo, Login loginCidadao) {
        this.idCidadao = idCidadao;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cidade = cidade;
        this.estado = estado;
        this.dirFotoUsuario = dir_foto_usuario;
        this.sexo = sexo;
        this.loginCidadao = loginCidadao;
    }

    public Cidadao(){
    }


    protected Cidadao(Parcel in) {
        nome = in.readString();
        sobrenome = in.readString();
        cidade = in.readString();
        estado = in.readString();
        dirFotoUsuario = in.readString();
        sexo = in.readString();
        loginCidadao = in.readParcelable(Login.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(sobrenome);
        dest.writeString(cidade);
        dest.writeString(estado);
        dest.writeString(dirFotoUsuario);
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

    public Integer getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(Integer idCidadao) {
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

    public String getDirFotoUsuario() {
        return dirFotoUsuario;
    }

    public void setDirFotoUsuario(String dir_foto_usuario) {
        this.dirFotoUsuario = dir_foto_usuario;
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
