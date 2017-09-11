package com.example.administrador.citycaremobile.Modelo;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrador on 01/09/2017.
 */

public class Usuario implements Parcelable {
    private Tipo tipo;
    private Login login

    public Usuario(Cidadao cidadao, Login usrGeneric){
        this.tipo = new Tipo(cidadao);
        this.login = usrGeneric;
    }

    public Usuario(Empresa empresa, Login usrGeneric){
        this.tipo = new Tipo(empresa);
        this.login = usrGeneric;
    }


    protected Usuario(Parcel in) {
        tipo = in.readParcelable(Object.class.getClassLoader());
        login = in.readParcelable(Login.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(tipo,flags);
        dest.writeParcelable(login, flags);
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public Object getTipo() {
        if(tipo.getEmpresa() != null){
            return tipo.getEmpresa();
        } else if(tipo.getCidadao() != null){
            return tipo.getCidadao();
        } else {
            return null;
        }
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    private class Tipo implements Parcelable {

        private Cidadao cidadao;
        private Empresa empresa;

        protected Tipo(Parcel in) {
            cidadao = in.readParcelable(Cidadao.class.getClassLoader());
            empresa = in.readParcelable(Empresa.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(cidadao, flags);
            dest.writeParcelable(empresa, flags);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public final Creator<Tipo> CREATOR = new Creator<Tipo>() {
            @Override
            public Tipo createFromParcel(Parcel in) {
                return new Tipo(in);
            }

            @Override
            public Tipo[] newArray(int size) {
                return new Tipo[size];
            }
        };

        private Cidadao getCidadao() {
            return cidadao;
        }

        private Empresa getEmpresa() {
            return empresa;
        }

        private Tipo(Cidadao cidadao){
            this.cidadao = cidadao;
        }

        private Tipo(Empresa empresa){
            this.empresa = empresa;
        }
    }
}
