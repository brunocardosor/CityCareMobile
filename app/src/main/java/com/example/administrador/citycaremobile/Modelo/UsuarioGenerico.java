package com.example.administrador.citycaremobile.Modelo;

/**
 * Created by Administrador on 01/09/2017.
 */

public class UsuarioGenerico {

    private int idUsuario;
    private String nome;
    private String sobrenome;
    private String cidade;
    private String estado;
    private String dirFotoUsuario;

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
