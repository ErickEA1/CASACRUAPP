package com.casacru.casacruapp.Modelo;

public class ModelCostoVenta {
    String Tienda;
    String Venta;
    String Costo;
    float Diferencia;
    String DiferenciaProcentual;
String Diferenciaformato;
    public ModelCostoVenta(String venta, String costo, float diferencia,String diferenciaformato, String diferenciaProcentual, String tienda) {
        Tienda = tienda;
        Venta = venta;
        Costo = costo;
        Diferencia = diferencia;
        Diferenciaformato =diferenciaformato;
        DiferenciaProcentual = diferenciaProcentual;

    }

    public String getDiferenciaformato() {
        return Diferenciaformato;
    }

    public void setDiferenciaformato(String diferenciaformato) {
        Diferenciaformato = diferenciaformato;
    }

    public String getTienda() {
        return Tienda;
    }

    public void setTienda(String tienda) {
        Tienda = tienda;
    }

    public String getVenta() {
        return Venta;
    }

    public void setVenta(String venta) {
        Venta = venta;
    }

    public String getCosto() {
        return Costo;
    }

    public void setCosto(String costo) {
        Costo = costo;
    }

    public float getDiferencia() {
        return Diferencia;
    }

    public void setDiferencia(float diferencia) {
        Diferencia = diferencia;
    }

    public String getDiferenciaProcentual() {
        return DiferenciaProcentual;
    }

    public void setDiferenciaProcentual(String diferenciaProcentual) {
        DiferenciaProcentual = diferenciaProcentual;
    }
}
