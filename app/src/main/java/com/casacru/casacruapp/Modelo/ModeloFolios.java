package com.casacru.casacruapp.Modelo;

public class ModeloFolios {
    private String Folio;
    private String Almacen;

    public ModeloFolios(String folio, String almacen) {
        Folio = folio;
        Almacen = almacen;
    }

    public String getFolio() {
        return Folio;
    }

    public void setFolio(String folio) {
        Folio = folio;
    }

    public String getAlmacen() {
        return Almacen;
    }

    public void setAlmacen(String alamacen) {
        Almacen = alamacen;
    }
}
