package com.casacru.casacruapp.TareasAsync;

import android.os.AsyncTask;
import android.widget.Toast;

import com.casacru.casacruapp.Interfaces.IReporteDeVenta;
import com.casacru.casacruapp.Modelo.ModeloTablaMaxmin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TareaReporteVenta extends AsyncTask<Object,Void,String> {
private Connection con;
private String fecha1, fecha2, concepto1, concepto2,tienda;
private IReporteDeVenta reporteDeVenta;

    public TareaReporteVenta(Connection con, String fecha1, String fecha2, String tienda, IReporteDeVenta reporteDeVenta) {
        this.con = con;
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
        this.tienda = tienda;
        this.reporteDeVenta = reporteDeVenta;
    }

    @Override
    protected void onPreExecute() {
        reporteDeVenta.proggresbar(true);
    }

    @Override
    protected String doInBackground(Object... objects) {
        this.concepto1 =(String) objects[0];
        this.concepto2 = (String) objects[1];
        String total="0";
        try {
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT SUM(CTOTAL) FROM admDocumentos where (CFECHA>= '"+fecha1+"' and CFECHA<='"+fecha2+"') and CCANCELADO=0 AND (CIDCONCEPTODOCUMENTO='"+concepto1+"' OR CIDCONCEPTODOCUMENTO='"+concepto2+"')");
            if (rs.next()) {
                 total=rs.getString(1);
            }
            return total;
        } catch (Exception e) {
            return total;
        }
    }


    @Override
    protected void onPostExecute(String total) {
        reporteDeVenta.Resultados(total, tienda);
    }
}
