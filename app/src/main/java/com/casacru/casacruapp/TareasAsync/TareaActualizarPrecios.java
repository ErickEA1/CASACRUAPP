package com.casacru.casacruapp.TareasAsync;

import android.content.Context;
import android.os.AsyncTask;

import com.casacru.casacruapp.Interfaces.Comunicacion;
import com.casacru.casacruapp.Interfaces.IActualizarPrecios;
import com.casacru.casacruapp.Modelo.ModeloPrecios;
import com.casacru.casacruapp.Modelo.ModeloTablaMaxmin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TareaActualizarPrecios extends AsyncTask <Object,Void,ArrayList<ModeloPrecios>> {
private IActualizarPrecios iActualizarPrecios;
private String IDAlmac;
private Connection connection;



    public TareaActualizarPrecios(Connection con, IActualizarPrecios comunicacion,String iDAlmac) {
        this.iActualizarPrecios=comunicacion;
        connection=con;
        IDAlmac=iDAlmac;
    }

    @Override
    protected void onPreExecute() {
        iActualizarPrecios.proggresbarr(true);


    }

    @Override
    protected ArrayList<ModeloPrecios> doInBackground(Object... objects) {
    String Consulta=(String) objects[0];
    String utilidad=(String) objects[1];
        ArrayList<ModeloPrecios> datos=new ArrayList<>();

            try {
                Statement stm3 = connection.createStatement();
                ResultSet rs = stm3.executeQuery(Consulta);

                while (rs.next()) {

                    String UltimoCosto = "";
                    String nompr=rs.getString(1);
                    String prec=rs.getString(2);
                    String idproducto = rs.getString(3);
                    double porcentaje = 0.0;
                    double costocalculado = 0.0;
                    double costosiniva=0.0;


                    Statement stm4 = connection.createStatement();
                    String consultaCC = "SELECT TOP 1 CULTIMOCOSTOH FROM admCostosHistoricos WHERE CIDPRODUCTO='" + idproducto + "' and CIDALMACEN='"+IDAlmac+"' ORDER BY CIDCOSTOH DESC";
                    ResultSet rsCC = stm4.executeQuery(consultaCC);
                    if (rsCC.next()) {
                        UltimoCosto = rsCC.getString(1);
                        double margenUtilidad = Double.parseDouble(utilidad);
                        porcentaje = (double) ((margenUtilidad * Double.parseDouble(UltimoCosto)) / 100);
                        costosiniva=Math.ceil((Double.parseDouble(UltimoCosto) + porcentaje));
                        costocalculado = Math.ceil((Double.parseDouble(UltimoCosto) + porcentaje) * 1.16);

                    }
                    if (Double.parseDouble(rs.getString(2)) < costocalculado) {
                        datos.add(new ModeloPrecios(rs.getString(1), rs.getString(2), rs.getString(3), String.valueOf(costocalculado),String.valueOf(costosiniva)));
                    }
                }

            }catch (Exception e){
                String M=e.getMessage();
            }
            return datos;
    }

    @Override
    protected void onPostExecute(ArrayList<ModeloPrecios> arrayList) {
        iActualizarPrecios.proggresbarr(false);
        iActualizarPrecios.ObtenerProductos(arrayList);

    }


}
