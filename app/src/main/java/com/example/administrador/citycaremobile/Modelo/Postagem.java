package com.example.administrador.citycaremobile.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrador on 30/09/2017.
 */

public class Postagem implements Parcelable {

    private Cidadao cidadao;
    private Denuncia denuncia;
    private List<Agiliza> agilizas;
    private List<Comentario> comentarios;

    public Postagem(Denuncia denuncia, List<Agiliza> agilizas, List<Comentario> comentarios, Cidadao cidadao) {
        this.denuncia = denuncia;
        this.agilizas = agilizas;
        this.comentarios = comentarios;
        this.cidadao = cidadao;
    }

    public Postagem() {
    }

    protected Postagem(Parcel in) {
        denuncia = in.readParcelable(Denuncia.class.getClassLoader());
        agilizas = in.createTypedArrayList(Agiliza.CREATOR);
        comentarios = in.createTypedArrayList(Comentario.CREATOR);
        cidadao = in.readParcelable(Cidadao.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(denuncia, flags);
        dest.writeTypedList(agilizas);
        dest.writeTypedList(comentarios);
        dest.writeParcelable(cidadao,flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Postagem> CREATOR = new Creator<Postagem>() {
        @Override
        public Postagem createFromParcel(Parcel in) {
            return new Postagem(in);
        }

        @Override
        public Postagem[] newArray(int size) {
            return new Postagem[size];
        }
    };

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

    public List<Agiliza> getAgilizas() {
        return agilizas;
    }

    public void setAgilizas(List<Agiliza> agilizas) {
        this.agilizas = agilizas;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public Cidadao getCidadao(){
        return cidadao;
    }

    public void setCidadao(Cidadao cidadao){
        this.cidadao = cidadao;
    }
}
