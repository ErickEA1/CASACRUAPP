package com.casacru.casacruapp.Modelo;

public class ModeloTablaMaxmin {
    private String CodProd;
    private String NomProd;
    private String ExistenciaActual;
    private String Maximo;
    private String Minimo;

    public ModeloTablaMaxmin(String codProd, String nomProd, String existenciaActual, String maximo, String minimo) {
        CodProd = codProd;
        NomProd = nomProd;
        ExistenciaActual = existenciaActual;
        Maximo = maximo;
        Minimo = minimo;
    }

    public String getCodProd() {
        return CodProd;
    }

    public void setCodProd(String codProd) {
        CodProd = codProd;
    }

    public String getNomProd() {
        return NomProd;
    }

    public void setNomProd(String nomProd) {
        NomProd = nomProd;
    }

    public String getExistenciaActual() {
        return ExistenciaActual;
    }

    public void setExistenciaActual(String existenciaActual) {
        ExistenciaActual = existenciaActual;
    }

    public String getMaximo() {
        return Maximo;
    }

    public void setMaximo(String maximo) {
        Maximo = maximo;
    }

    public String getMinimo() {
        return Minimo;
    }

    public void setMinimo(String minimo) {
        Minimo = minimo;
    }
}
