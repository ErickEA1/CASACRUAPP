package com.casacru.casacruapp.Modelo;

public class ModeloSegmentos {
    private String CodGrupo;
    private String NomGrupo;
    private String SegContble;

    public ModeloSegmentos(String codGrupo, String nomGrupo, String segContble) {
        CodGrupo = codGrupo;
        NomGrupo = nomGrupo;
        SegContble = segContble;
    }

    public String getCodGrupo() {
        return CodGrupo;
    }

    public void setCodGrupo(String codGrupo) {
        CodGrupo = codGrupo;
    }

    public String getNomGrupo() {
        return NomGrupo;
    }

    public void setNomGrupo(String nomGrupo) {
        NomGrupo = nomGrupo;
    }

    public String getSegContble() {
        return SegContble;
    }

    public void setSegContble(String segContble) {
        SegContble = segContble;
    }
}
