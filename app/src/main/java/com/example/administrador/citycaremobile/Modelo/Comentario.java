package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Comentario implements Parcelable {

    private int idComentario;
    private String descricaoComentario;
    private Usuario usuarioComentario;
    private Denuncia denunciaComentario;

    protected Comentario(Parcel in) {
        idComentario = in.readInt();
        descricaoComentario = in.readString();
        usuarioComentario = in.readParcelable(Usuario.class.getClassLoader());
        denunciaComentario = in.readParcelable(Denuncia.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idComentario);
        dest.writeString(descricaoComentario);
        dest.writeParcelable(usuarioComentario, flags);
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

    public Usuario getUsuarioComentario() {
        return usuarioComentario;
    }

    public void setUsuarioComentario(Usuario usuarioComentario) {
        this.usuarioComentario = usuarioComentario;
    }

    public Denuncia getDenunciaComentario() {
        return denunciaComentario;
    }

    public void setDenunciaComentario(Denuncia denunciaComentario) {
        this.denunciaComentario = denunciaComentario;
    }
}
