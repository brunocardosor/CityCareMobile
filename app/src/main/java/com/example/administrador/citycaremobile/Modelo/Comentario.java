package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Comentario implements Parcelable, Comparable<Login> {

    @SerializedName("id_comentario")
    private Integer idComentario;
    @SerializedName("descricao_comentario")
    private String descricao;
    @SerializedName("fk_login_comentario")
    private Login login;
    @SerializedName("fk_denuncia_comentario")
    private Denuncia denunciaComentario;

    public Comentario(){

    }

    public Comentario(Integer idComentario, String descricao, Login login, Denuncia denunciaComentario) {
        this.idComentario = idComentario;
        this.descricao = descricao;
        this.login = login;
        this.denunciaComentario = denunciaComentario;
    }

    protected Comentario(Parcel in) {
        if (in.readByte() == 0) {
            idComentario = null;
        } else {
            idComentario = in.readInt();
        }
        descricao = in.readString();
        login = in.readParcelable(Login.class.getClassLoader());
        denunciaComentario = in.readParcelable(Denuncia.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (idComentario == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idComentario);
        }
        dest.writeString(descricao);
        dest.writeParcelable(login, flags);
        dest.writeParcelable(denunciaComentario, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comentario> CREATOR = new Creator<Comentario>() {
        @Override
        public Comentario createFromParcel(Parcel in) {
            return new Comentario(in);
        }

        @Override
        public Comentario[] newArray(int size) {
            return new Comentario[size];
        }
    };

    public Integer getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(Integer idComentario) {
        this.idComentario = idComentario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Denuncia getDenunciaComentario() {
        return denunciaComentario;
    }

    public void setDenunciaComentario(Denuncia denunciaComentario) {
        this.denunciaComentario = denunciaComentario;
    }

    @Override
    public int compareTo(@NonNull Login o) {
        if(idComentario == o.getIdLogin()){
            return 1;
        } else {
            return 0;
        }
    }
}
