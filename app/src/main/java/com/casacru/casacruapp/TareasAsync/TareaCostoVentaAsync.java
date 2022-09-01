package com.casacru.casacruapp.TareasAsync;

import android.os.AsyncTask;

import com.casacru.casacruapp.Interfaces.ICostoVenta;
import com.casacru.casacruapp.Modelo.ModeloPrecios;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class TareaCostoVentaAsync extends AsyncTask<Object,Void,ArrayList<Float>> {
    private Connection con;
    private float total=0;
    private float totalCosto=0;
    private String fecha1, fecha2;
    private ICostoVenta iCostoVenta;
    private String Tienda;

    public TareaCostoVentaAsync(Connection con, String fecha1, String fecha2, ICostoVenta iCostoVenta, String tienda) {
        this.con = con;
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
        this.iCostoVenta = iCostoVenta;
        Tienda = tienda;
    }

    @Override
    protected void onPreExecute() {
        iCostoVenta.proggresbarr(true);
    }

    @Override
    protected ArrayList<Float> doInBackground(Object... objects) {
        String doc1=(String) objects[0];
        String doc2=(String) objects[1];
        String doc3=(String) objects[2];
        String doc4=(String) objects[3];
        ArrayList<Float> array=new ArrayList<Float>();
        try {
            Statement stm = con.createStatement();
            String Consulta;
            if(doc3=="null") {
                Consulta = "SELECT CNETO, CCOSTOESPECIFICO FROM admMovimientos " +
                        "WHERE ((CIDDOCUMENTO) IN (SELECT CIDDOCUMENTO FROM admDocumentos " +
                        "WHERE ((CFECHA>= '" + fecha1 + "' and CFECHA<='" + fecha2 + "') " +
                        "AND CCANCELADO=0 AND (CIDCONCEPTODOCUMENTO='" + doc1 + "' OR CIDCONCEPTODOCUMENTO='" + doc2 + "'))))";
            }else{
                Consulta = "SELECT CNETO, CCOSTOESPECIFICO FROM admMovimientos " +
                        "WHERE ((CIDDOCUMENTO) IN (SELECT CIDDOCUMENTO FROM admDocumentos " +
                        "WHERE ((CFECHA>= '" + fecha1 + "' and CFECHA<='" + fecha2 + "') " +
                        "AND CCANCELADO=0 AND (CIDCONCEPTODOCUMENTO='" + doc1 + "' OR CIDCONCEPTODOCUMENTO='" + doc2 + "' OR CIDCONCEPTODOCUMENTO='" + doc3 + "' OR CIDCONCEPTODOCUMENTO='" + doc4 + "'))))";
            }
            ResultSet rs = stm.executeQuery(Consulta);
            while (rs.next()) {
                total += Float.parseFloat(rs.getString(1));
                totalCosto += Float.parseFloat(rs.getString(2));
            }
            array.add(total);
            array.add(totalCosto);

        }catch (Exception e){

        }
        return array;
    }

    @Override
    protected void onPostExecute(ArrayList<Float> s) {
        if(Tienda.equals("DENGUI")) {
            iCostoVenta.lineChartDataSetDenguiVenta(s.get(0), s.get(1));
        }else if(Tienda.equals("NOPALA")) {
            iCostoVenta.lineChartDataSetNopalaVenta(s.get(0), s.get(1));
        }else if(Tienda.equals("EL 61")) {
            iCostoVenta.lineChartDataSet61Venta(s.get(0), s.get(1));
        }else if(Tienda.equals("LAGUNAS")) {
            iCostoVenta.lineChartDataSetLagunasVenta(s.get(0), s.get(1));
        }else if(Tienda.equals("MATIAS")) {
            iCostoVenta.lineChartDataSetMatiasRVenta(s.get(0), s.get(1));
        }else if(Tienda.equals("MINA")) {
            iCostoVenta.lineChartDataSetMina(s.get(0), s.get(1));
        }else if(Tienda.equals("BLOQUERA")) {
            iCostoVenta.lineChartDataSetBloquera(s.get(0), s.get(1));
        }else if(Tienda.equals("SD")) {
            iCostoVenta.lineChartDataSetSDVenta(s.get(0), s.get(1));
            iCostoVenta.proggresbarr(false);
        }

    }

}
