package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrador on 01/09/2017.
 */

public class UsuarioGenerico implements Parcelable {

    private int idUsuario;
    private String nome;
    private String sobrenome;
    private String cidade;
    private String estado;
    private String dirFotoUsuario;
    private String email;
    private String login;
    private String senha;
    private boolean status_login;
    private boolean administrador;

    protected UsuarioGenerico(Parcel in) {
        idUsuario = in.readInt();
        nome = in.readString();
        sobrenome = in.readString();
        cidade = in.readString();
        estado = in.readString();
        dirFotoUsuario = in.readString();
        email = in.readString();
        login = in.readString();
        senha = in.readString();
        status_login = in.readByte() != 0;
        administrador = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idUsuario);
        dest.writeString(nome);
        dest.writeString(sobrenome);
        dest.writeString(cidade);
        dest.writeString(estado);
        dest.writeString(dirFotoUsuario);
        dest.writeString(email);
        dest.writeString(login);
        dest.writeString(senha);
        dest.writeByte((byte) (status_login ? 1 : 0));
        dest.writeByte((byte) (administrador ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UsuarioGenerico> CREATOR = new Creator<UsuarioGenerico>() {
        @Override
        public UsuarioGenerico createFromParcel(Parcel in) {
            return new UsuarioGenerico(in);
        }

        @Override
        public UsuarioGenerico[] newArray(int size) {
            return new UsuarioGenerico[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isStatus_login() {
        return status_login;
    }

    public void setStatus_login(boolean status_login) {
        this.status_login = status_login;
    }

    public boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    public void setDirFotoUsuario(String dirFotoUsuario) {
        this.dirFotoUsuario = dirFotoUsuario;
    }
}
