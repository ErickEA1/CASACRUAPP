package com.casacru.casacruapp.TareasAsync;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.view.Display;
import android.widget.Toast;

import com.casacru.casacruapp.Almacen.ReporteMaxMin;
import com.casacru.casacruapp.Interfaces.Comunicacion;
import com.casacru.casacruapp.Modelo.ModeloTablaMaxmin;
import com.casacru.casacruapp.R;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TareaReporteMaxMin extends AsyncTask <Object,Void,ArrayList<ModeloTablaMaxmin>> {
private Comunicacion comunicacion;
private Context context;
private String IDAlmac;
private Connection connection;



    public TareaReporteMaxMin(Context contexto, Connection con, Comunicacion comunicacion) {
        this.comunicacion=comunicacion;
        context=contexto;
        connection=con;
    }

    @Override
    protected void onPreExecute() {
        comunicacion.proggresbar(true);


    }

    @Override
    protected ArrayList<ModeloTablaMaxmin> doInBackground(Object... objects) {
String Consulta=(String) objects[0];
IDAlmac=(String) objects[1];
String condicion=(String) objects[2];
        ArrayList<ModeloTablaMaxmin> datos=new ArrayList<>();
        String ejercicio = "";
        String mes="",anio="";
            try {

                //fecha
                SimpleDateFormat dtf = new SimpleDateFormat("yyyy/MM/dd");
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                String fetch = dtf.format(date);

                Date dte = new SimpleDateFormat("yyyy/MM/dd").parse(fetch);
                calendar.setTime(dte);

                anio = String.valueOf(calendar.get(Calendar.YEAR));
                int month = (calendar.get(Calendar.MONTH)) + 1;
                mes = String.valueOf(month);
            }catch (Exception e) {
            }

                // ejercicio
try {

    Statement stm3 = connection.createStatement();
    ResultSet rs3 = stm3.executeQuery("SELECT CIDEJERCICIO FROM admEjercicios where CEJERCICIO='" + anio + "'");

    if (rs3.next()) {
        ejercicio = rs3.getString(1);
    }
}catch (Exception e) {
}

try{
                Statement stm = connection.createStatement();
                ResultSet rs = stm.executeQuery(Consulta);
                 while(rs.next()) {
                     String Minimo = "0";
                     String Max = "0";
                     String ExistenciaActual = "0";

                     // Maximo y minimo del producto
                     Statement stm1 = connection.createStatement();
                     ResultSet rs1 = stm1.executeQuery("SELECT CEXISTENCIAMINBASE, CEXISTENCIAMAXBASE FROM admMaximosMinimos where CIDALMACEN='" + IDAlmac + "' and CIDPRODUCTO='" + rs.getString(1) + "';");
                     rs1.wasNull();
                     if (rs1.next()) {
                         Minimo = rs1.getString(1);
                         Max = rs1.getString(2);
                     }


                     //Existencia Actual del producto

                     String entradasp = "", salidasp = "";

                     Statement stm2 = connection.createStatement();
                     ResultSet rs2 = stm2.executeQuery("SELECT CENTRADASPERIODO" + mes + ", CSALIDASPERIODO" + mes + " from admExistenciaCosto " +
                             "WHERE CIDEJERCICIO='" + ejercicio + "' and CIDPRODUCTO='" + rs.getString(1) + "' and CIDALMACEN='" + IDAlmac + "'");
                     if (rs2.next()) {

                         entradasp = rs2.getString(1);
                         salidasp = rs2.getString(2);
                     } else {
                         entradasp = "0.0";
                         salidasp = "0.0";
                     }
                     float entradas = Float.parseFloat(entradasp);
                     float salidas = Float.parseFloat(salidasp);
                     float total = (entradas) - (salidas);
                     ExistenciaActual = String.valueOf(total);

                     if ((Minimo != null) && (Max != null)) {
                         if (condicion == "<") {
                             if (Double.parseDouble(ExistenciaActual) <= Double.parseDouble(Minimo)) {
                                 datos.add(new ModeloTablaMaxmin(rs.getString(2), rs.getString(3), ExistenciaActual, Max, Minimo));
                             }
                         } else if (condicion == ">") {
                             if (Double.parseDouble(ExistenciaActual) >= Double.parseDouble(Minimo)) {
                                 datos.add(new ModeloTablaMaxmin(rs.getString(2), rs.getString(3), ExistenciaActual, Max, Minimo));
                             }
                         }
                     }
                 }
            }catch (Exception e){
                String M=e.getMessage();
            }
            return datos;
    }

    @Override
    protected void onPostExecute(ArrayList<ModeloTablaMaxmin> arrayList) {
        comunicacion.proggresbar(false);
        comunicacion.consultarIdProducto2(arrayList);

    }


}
