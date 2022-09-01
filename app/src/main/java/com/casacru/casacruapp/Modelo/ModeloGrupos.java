package com.casacru.casacruapp.Modelo;

public class ModeloGrupos {

    private String codGrupo;
    private String IdGrupo;
    private String NomGrupo;

    public ModeloGrupos(String idGrupo, String codGrupo, String nomGrupo) {
        this.codGrupo = codGrupo;
        IdGrupo = idGrupo;
        NomGrupo = nomGrupo;
    }

    public String getCodGrupo() {
        return codGrupo;
    }

    public void setCodGrupo(String codGrupo) {
        this.codGrupo = codGrupo;
    }

    public String getIdGrupo() {
        return IdGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        IdGrupo = idGrupo;
    }

    public String getNomGrupo() {
        return NomGrupo;
    }

    public void setNomGrupo(String nomGrupo) {
        NomGrupo = nomGrupo;
    }
}
