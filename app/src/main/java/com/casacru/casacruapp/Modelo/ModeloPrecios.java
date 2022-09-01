package com.casacru.casacruapp.Modelo;

public class ModeloPrecios {
    private String Descripcion;
    private String Precio2;
    private String Idproducto;
    private String PrecioCalculado;
    private String PrecioSinIva;
    private Boolean isSelected;



    public ModeloPrecios(String descripcion, String precio2, String idproducto, String precioCalculado, String Preciosiniva) {
        Descripcion = descripcion;
        Precio2 = precio2;
        Idproducto=idproducto;
        PrecioCalculado=precioCalculado;
        PrecioSinIva=Preciosiniva;
        isSelected=true;
    }

    public String getIdproducto() {
        return Idproducto;
    }

    public void setIdproducto(String idproducto) {
        this.Idproducto = idproducto;
    }
    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getPrecio2() {
        return Precio2;
    }

    public void setPrecio2(String precio2) {
        Precio2 = precio2;
    }

    public String getPrecioCalculado() {
        return PrecioCalculado;
    }

    public void setPrecioCalculado(String precioCalculado) {
        PrecioCalculado = precioCalculado;
    }

    public String getPrecioSinIva() {
        return PrecioSinIva;
    }

    public void setPrecioSinIva(String precioSinIva) {
        PrecioSinIva = precioSinIva;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
