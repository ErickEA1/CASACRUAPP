package com.casacru.casacruapp.Modelo;

import java.io.Serializable;

public class DtoProductos implements Serializable {
    private String Id;
    private String Almacen;
    private String CodigoProducto;
    private String NombreProducto;
    private String Cantidad;
    private String Unidad;
    private boolean Surtido;



    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getAlmacen() {
        return Almacen;
    }

    public void setAlmacen(String almacen) {
        Almacen = almacen;
    }

    public String getCodigoProducto() {
        return CodigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        CodigoProducto = codigoProducto;
    }

    public String getNombreProducto() {
        return NombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        NombreProducto = nombreProducto;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    public String getUnidad() {
        return Unidad;
    }

    public void setUnidad(String unidad) {
        Unidad = unidad;
    }

    public boolean getSurtido() {
        return Surtido;
    }

    public void setSurtido(boolean surtido) {
        Surtido = surtido;
    }
}
