package com.casacru.casacruapp.Interfaces;

public interface ICostoVenta {
    void proggresbarr(boolean status);
    void lineChartDataSetDenguiVenta(float total, float totalCosto);
    void lineChartDataSetNopalaVenta(float total, float totalCosto);
    void lineChartDataSetMina(float total, float totalCosto);
    void lineChartDataSet61Venta(float total, float totalCosto);
    void lineChartDataSetLagunasVenta(float total, float totalCosto);
    void lineChartDataSetMatiasRVenta(float total, float totalCosto);
    void lineChartDataSetSDVenta(float total, float totalCosto);
    void lineChartDataSetBloquera(float total, float totalCosto);
}
