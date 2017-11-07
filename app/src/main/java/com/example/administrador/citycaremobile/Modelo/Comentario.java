package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Comentario implements Parcelable {

    @SerializedName("id_comentario")
    private int idComentario;
    @SerializedName("descricao_comentario")
    private String descricaoComentario;
    @SerializedName("fk_login_comentario")
    private Login login;
    @SerializedName("denuncia_comentario")
    private Denuncia denunciaComentario;

    protected Comentario(Parcel in) {
        idComentario = in.readInt();
        descricaoComentario = in.readString();
        login = in.readParcelable(Login.class.getClassLoader());
        denunciaComentario = in.readParcelable(Denuncia.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idComentario);
        dest.writeString(descricaoComentario);
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

    public int getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(int idComentario) {
        this.idComentario = idComentario;
    }

    public String getDescricaoComentario() {
        return descricaoComentario;
    }

    public void setDescricaoComentario(String descricaoComentario) {
        this.descricaoComentario = descricaoComentario;
    }

    public Login getUsuarioComentario() {
        return login;
    }

    public void setUsuarioComentario(Login login) {
        this.login = login;
    }

    public Denuncia getDenunciaComentario() {
        return denunciaComentario;
    }

    public void setDenunciaComentario(Denuncia denunciaComentario) {
        this.denunciaComentario = denunciaComentario;
    }
}
