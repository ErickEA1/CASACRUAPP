package com.casacru.casacruapp.Modelo;

import java.io.Serializable;

public class ModeloProductos implements Serializable {
    private String CodProducto;
    private String Descripcion;

    public ModeloProductos( String codProducto, String descripcion) {

        this.CodProducto = codProducto;
        this.Descripcion = descripcion;
    }


    public String getCodProducto() {
        return CodProducto;
    }

    public void setCodProducto(String codProducto) {
        CodProducto = codProducto;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}
