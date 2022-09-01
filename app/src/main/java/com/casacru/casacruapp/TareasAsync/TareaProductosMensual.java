package com.casacru.casacruapp.TareasAsync;

import android.os.AsyncTask;

import com.casacru.casacruapp.Interfaces.IProductosMensual;
import com.github.mikephil.charting.data.Entry;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TareaProductosMensual extends AsyncTask<Object,Void, ArrayList<Float>> {
    float ene, feb, mar, abr, may, jun, jul, ago, sep, oct, nov, dec;
    Connection con;
    IProductosMensual productosMensual;

    public TareaProductosMensual(float ene, float feb, float mar, float abr, float may, float jun, float jul, float ago, float sep, float oct, float nov, float dec, Connection con, IProductosMensual iProductosMensual) {
        this.ene = ene;
        this.feb = feb;
        this.mar = mar;
        this.abr = abr;
        this.may = may;
        this.jun = jun;
        this.jul = jul;
        this.ago = ago;
        this.sep = sep;
        this.oct = oct;
        this.nov = nov;
        this.dec = dec;
        this.con = con;
        productosMensual = iProductosMensual;
    }

    @Override
    protected void onPreExecute() {
        productosMensual.Progressbar(true);
    }


    @Override
    protected ArrayList<Float> doInBackground(Object... objects) {
        String IdProducto = (String) objects[0];
        String Doc1 = (String) objects[1];
        String Doc2 = (String) objects[2];
        String idAlma = (String) objects[3];
        String anio = (String) objects[4];
        ArrayList<Float> Meses = new ArrayList<>();


//        productosMensual.Consulta1(con,IdProducto,idAlma,Doc1,Doc2);
//        productosMensual.Consulta2(con,IdProducto,idAlma,Doc1,Doc2);
//        productosMensual.Consulta3(con,IdProducto,idAlma,Doc1,Doc2);
//        productosMensual.Consulta4(con,IdProducto,idAlma,Doc1,Doc2);
//        productosMensual.Consulta5(con,IdProducto,idAlma,Doc1,Doc2);
//        productosMensual.Consulta6(con,IdProducto,idAlma,Doc1,Doc2);
//        productosMensual.Consulta7(con,IdProducto,idAlma,Doc1,Doc2);
//        productosMensual.Consulta8(con,IdProducto,idAlma,Doc1,Doc2);
//        productosMensual.Consulta9(con,IdProducto,idAlma,Doc1,Doc2);
//        productosMensual.Consulta10(con,IdProducto,idAlma,Doc1,Doc2);
//        productosMensual.Consulta11(con,IdProducto,idAlma,Doc1,Doc2);
//        productosMensual.Consulta12(con,IdProducto,idAlma,Doc1,Doc2);

     /*   try {
            Statement stm = con.createStatement();
            String Consulta = "SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='" + IdProducto + "' AND (CFECHA>='1/1/" + anio + "' and CFECHA<='1/31/" + anio + "') " +
                    "AND (CIDDOCUMENTODE='" + Doc1 + "' OR CIDDOCUMENTODE='" + Doc2 + "') and CIDALMACEN='" + idAlma + "' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs = stm.executeQuery(Consulta);
            while (rs.next()) {
                ene += Double.parseDouble(rs.getString(1));
            }
            Meses.add(ene);


            String Consulta2 = "SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='" + IdProducto + "' AND (CFECHA>='2/1/" + anio + "' and CFECHA<='2/28/" + anio + "') " +
                    "AND (CIDDOCUMENTODE='" + Doc1 + "' OR CIDDOCUMENTODE='" + Doc2 + "') and CIDALMACEN='" + idAlma + "' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs2 = stm.executeQuery(Consulta2);
            while (rs2.next()) {
                feb += Double.parseDouble(rs2.getString(1));
            }
            Meses.add(feb);


            String Consulta3 = "SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='" + IdProducto + "' AND (CFECHA>='3/1/" + anio + "' and CFECHA<='3/31/" + anio + "') " +
                    "AND (CIDDOCUMENTODE='" + Doc1 + "' OR CIDDOCUMENTODE='" + Doc2 + "') and CIDALMACEN='" + idAlma + "' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs3 = stm.executeQuery(Consulta3);
            while (rs3.next()) {
                mar += Double.parseDouble(rs3.getString(1));
            }
            Meses.add(mar);

            String Consulta4="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='4/1/"+anio+"' and CFECHA<='4/30/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs4 = stm.executeQuery(Consulta4);
            while (rs4.next()) {
                abr+=Double.parseDouble(rs4.getString(1));
            }
            Meses.add(abr);

            String Consulta5="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='5/1/"+anio+"' and CFECHA<='5/31/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs5 = stm.executeQuery(Consulta5);
            while (rs5.next()) {
                may+=Double.parseDouble(rs5.getString(1));
            }
            Meses.add(may);

            String Consulta6="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='6/1/"+anio+"' and CFECHA<='6/30/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs6 = stm.executeQuery(Consulta6);
            while (rs6.next()) {
                jun+=Double.parseDouble(rs6.getString(1));
            }
            Meses.add(jun);


            String Consulta7="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='7/1/"+anio+"' and CFECHA<='7/31/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs7 = stm.executeQuery(Consulta7);
            while (rs7.next()) {
                jul+=Double.parseDouble(rs7.getString(1));
            }
            Meses.add(jul);

            String Consulta8="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='8/1/"+anio+"' and CFECHA<='8/31/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs8 = stm.executeQuery(Consulta8);
            while (rs8.next()) {
                ago+=Double.parseDouble(rs8.getString(1));
            }
            Meses.add(ago);
            String Consulta9="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='9/1/"+anio+"' and CFECHA<='9/30/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs9 = stm.executeQuery(Consulta9);
            while (rs9.next()) {
                sep+=Double.parseDouble(rs9.getString(1));
            }
            Meses.add(sep);
            String Consulta10="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='10/1/"+anio+"' and CFECHA<='10/31/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs10 = stm.executeQuery(Consulta10);
            while (rs10.next()) {
                oct+=Double.parseDouble(rs10.getString(1));
            }
            Meses.add(oct);
            String Consulta11="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='11/1/"+anio+"' and CFECHA<='11/30/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs11 = stm.executeQuery(Consulta11);
            while (rs11.next()) {
                nov+=Double.parseDouble(rs11.getString(1));
            }
            Meses.add(nov);

            String Consulta12="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='12/1/"+anio+"' and CFECHA<='12/31/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs12 = stm.executeQuery(Consulta12);
            while (rs12.next()) {
                dec+=Double.parseDouble(rs12.getString(1));
            }
            Meses.add(dec);
            productosMensual.RecibirMeses(Meses.get(0),Meses.get(1),Meses.get(2),Meses.get(3),Meses.get(4),Meses.get(5),Meses.get(6),Meses.get(7),Meses.get(8),Meses.get(9),Meses.get(10),Meses.get(11));

        }catch (Exception e){

        }


      */
        return Meses;
    }

    @Override
    protected void onPostExecute(ArrayList<Float> s) {
        //productosMensual.RecibirMeses(s.get(0), s.get(1), s.get(2), s.get(3), s.get(4), s.get(5), s.get(6), s.get(7), s.get(8), s.get(9), s.get(10), s.get(11));
    }
}
