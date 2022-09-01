package com.casacru.casacruapp.Ventas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.casacru.casacruapp.Dialog.DialogError;
import com.casacru.casacruapp.Interfaces.IReporteDeVenta;
import com.casacru.casacruapp.R;
import com.casacru.casacruapp.SQLite.DatabaseHelper;
import com.casacru.casacruapp.SQLite.DatabaseOpenHelper;
import com.casacru.casacruapp.TareasAsync.TareaReporteVenta;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class ReporteDeVenta extends AppCompatActivity implements IReporteDeVenta {
    private PieChart pieChartView;
    private Button btnfech1, btnfech2, btnmostrar;
    private int dia, mes, anio;
    private EditText txtfech1, txtfech2;
    private Context context;
    private String totalDengui, totalLagunas, total61, totalMatias, totalNopala, totalSantoDom,totalBloquera,totalMina;
    private String IP, PUERTO, USUARIO, CONTRASEÑA, BD, idAlma;
    private String IPLagunas, PUERTOLagunas, USUARIOLagunas, CONTRASEÑALagunas, BDLagunas, idAlmaLagunas;
    private String IPSD, PUERTOSD, USUARIOSD, CONTRASEÑASD, BDSD, idAlmaSD;
    private String IP61, PUERTO61, USUARIO61, CONTRASEÑA61, BD61;
    private ProgressBar progressBar3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_de_venta);
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            context = this;
            pieChartView = findViewById(R.id.chart);
            btnfech1 = findViewById(R.id.button8);
            btnfech2 = findViewById(R.id.button7);
            txtfech1 = findViewById(R.id.txtfech1);
            txtfech2 = findViewById(R.id.txtfech2);
            btnmostrar = findViewById(R.id.btnMostrar);
            btnfech2.setOnClickListener(onclick);
            btnfech1.setOnClickListener(onclick);
            progressBar3=(ProgressBar) findViewById(R.id.progressBar3);
            buscard();
            buscarLagunas();
            buscarSD();
            buscarel61();
            btnmostrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        new TareaReporteVenta(conexion2(),txtfech1.getText().toString(),txtfech2.getText().toString(),"DENGUI",ReporteDeVenta.this).execute("3", "3056");
                        new TareaReporteVenta(conexion2(),txtfech1.getText().toString(),txtfech2.getText().toString(),"NOPALA",ReporteDeVenta.this).execute("3140", "3142");
                        new TareaReporteVenta(conexion61(),txtfech1.getText().toString(),txtfech2.getText().toString(),"EL61",ReporteDeVenta.this).execute("3", "3043");
                        new TareaReporteVenta(conexionLagunas(),txtfech1.getText().toString(),txtfech2.getText().toString(),"LAGUNAS",ReporteDeVenta.this).execute("3", "3050");
                        new TareaReporteVenta(conexionLagunas(),txtfech1.getText().toString(),txtfech2.getText().toString(),"MATIAS",ReporteDeVenta.this).execute("3077", "3078");
                        new TareaReporteVenta(conexionMina(),txtfech1.getText().toString(),txtfech2.getText().toString(),"MINA",ReporteDeVenta.this).execute("3", "3014");
                        new TareaReporteVenta(Conexionbloquera(),txtfech1.getText().toString(),txtfech2.getText().toString(),"BLOQUERA",ReporteDeVenta.this).execute("3", "3014","3015","3016");
                        new TareaReporteVenta(ConexionSD(),txtfech1.getText().toString(),txtfech2.getText().toString(),"SD",ReporteDeVenta.this).execute("3", "3050");

                        Toast.makeText(context, "¡Listo!", Toast.LENGTH_SHORT).show();
                    }catch(Exception ex){
                        new DialogError(context,"");
                    }
                }
            });

        } catch (Exception e) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu6, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item1:

                Workbook workbook = new HSSFWorkbook();
                org.apache.poi.ss.usermodel.Cell celda = null;
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

                Sheet sheet = null;

                sheet = workbook.createSheet("Ventas totales");


                Row row = null;

                row = sheet.createRow(0);

                celda = row.createCell(0);
                celda.setCellValue("TIENDA");
                celda = row.createCell(1);
                celda.setCellValue("TOTAL SUMADO DE VENTAS");

                row = sheet.createRow(1);

                celda = row.createCell(0);
                celda.setCellValue("TIENDA DENGUI");
                celda = row.createCell(1);
                celda.setCellValue(totalDengui);

                row = sheet.createRow(2);

                celda = row.createCell(0);
                celda.setCellValue("TIENDA NOPALA");
                celda = row.createCell(1);
                celda.setCellValue(totalNopala);

                row = sheet.createRow(3);

                celda = row.createCell(0);
                celda.setCellValue("TIENDA EL 61");
                celda = row.createCell(1);
                celda.setCellValue(total61);

                row = sheet.createRow(4);

                celda = row.createCell(0);
                celda.setCellValue("TIENDA SANTO DOMINGO");
                celda = row.createCell(1);
                celda.setCellValue(totalSantoDom);

                row = sheet.createRow(5);

                celda = row.createCell(0);
                celda.setCellValue("TIENDA LAGUNAS");
                celda = row.createCell(1);
                celda.setCellValue(totalLagunas);

                row = sheet.createRow(6);

                celda = row.createCell(0);
                celda.setCellValue("TIENDA MATIAS ROMERO");
                celda = row.createCell(1);
                celda.setCellValue(totalMatias);

                row = sheet.createRow(7);

                celda = row.createCell(0);
                celda.setCellValue("");
                celda = row.createCell(1);
                celda.setCellValue("");

                row = sheet.createRow(8);

                celda = row.createCell(0);
                celda.setCellValue("RANGO DE FECHAS:");
                celda = row.createCell(1);
                celda.setCellValue("del "+txtfech1.getText().toString()+" al "+txtfech2.getText().toString());


                String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                File file2 = new File(pdfPath, "Ventas por tienda.xls");
                FileOutputStream outputStream = null;

                try {
                    outputStream = new FileOutputStream(file2);
                    workbook.write(outputStream);
                    Toast.makeText(context, "Documento creado", Toast.LENGTH_SHORT).show();

                } catch (Exception ex) {
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
               break;
        } return super.onOptionsItemSelected(item);
    }

    public void CrearGrafico() {
        float dengui=0;
        float nopala=0;
        float lagunas=0;
        float sd=0;
        float tienda61=0;
        float matiasR=0;
        float mina=0;
        float Bloquera=0;
        if(totalDengui != null ) {
             dengui = Float.parseFloat(totalDengui);
        }
        if(totalNopala != null) {
            nopala = Float.parseFloat(totalNopala);
        }
        if(totalLagunas != null) {
            lagunas = Float.parseFloat(totalLagunas);
        }
        if(totalSantoDom != null) {
            sd = Float.parseFloat(totalSantoDom);
        }
        if(total61 != null) {
            tienda61 = Float.parseFloat(total61);
        }
        if(totalMatias != null) {
            matiasR = Float.parseFloat(totalMatias);
        }
        if(totalMina != null) {
            mina = Float.parseFloat(totalMina);
        }
        if(totalBloquera != null) {
            Bloquera = Float.parseFloat(totalBloquera);
        }
        ArrayList<PieEntry> visitor = new ArrayList<>();
        visitor.add(new PieEntry(dengui, "TIENDA DENGUI"));
        visitor.add(new PieEntry(nopala, "TIENDA NOPALA"));
        visitor.add(new PieEntry(mina, "MINA"));
        visitor.add(new PieEntry(lagunas, "TIENDA LAGUNAS"));
        visitor.add(new PieEntry(Bloquera, "BLOQUERA"));
        visitor.add(new PieEntry(matiasR, "TIENDA MATIAS ROMERO"));
        visitor.add(new PieEntry(sd, "TIENDA SANTO DOMINGO"));
        visitor.add(new PieEntry(tienda61, "TIENDA EL 61"));





        int[] colors = {Color.rgb(255, 171, 0), Color.rgb(56, 142, 60), Color.rgb(218,101,18), Color.rgb(223, 28, 68),Color.rgb(0, 200, 137), Color.rgb(25, 74, 141),
                Color.rgb(123, 31, 162), Color.rgb(109, 86, 27) };
        PieDataSet pieDataSet = new PieDataSet(visitor, "TIENDAS");
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(18f);
        pieDataSet.setSliceSpace(2f);

        PieData pieData = new PieData(pieDataSet);
        pieChartView.setData(pieData);
        pieChartView.setDrawSliceText(false);
        pieChartView.setEntryLabelTextSize(12f);
        pieChartView.getDescription().setEnabled(false);
        pieChartView.setDrawHoleEnabled(true);

        Legend l=pieChartView.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
        float totalvendido=dengui+nopala+tienda61+lagunas+matiasR+sd+mina+Bloquera;
        DecimalFormat formato=new DecimalFormat("#,###.00");
        String val=formato.format(totalvendido);
        pieChartView.setCenterText("VENTAS TOTALES POR TIENDA \n TOTAL: $"+val);
        pieChartView.animateY(1400, Easing.EaseInOutQuad);
        pieChartView.animate();

    }

    public View.OnClickListener onclick= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Calendar c= Calendar.getInstance();
            switch (v.getId()){
                case R.id.button7:

                    dia=c.get(Calendar.DAY_OF_MONTH);
                    mes=c.get(Calendar.MONTH);
                    anio=c.get(Calendar.YEAR);

                    DatePickerDialog fecha=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            txtfech2.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                        }
                    },dia,mes,anio);
                    fecha.show();


                break;
                case R.id.button8:
                    dia=c.get(Calendar.DAY_OF_MONTH);
                    mes=c.get(Calendar.MONTH);
                    anio=c.get(Calendar.YEAR);

                    DatePickerDialog fecha2=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            txtfech1.setText( (month + 1) + "/" +dayOfMonth+ "/" + year);
                        }
                    },dia,mes,anio);
                    fecha2.show();
                    break;
            }
        }
    };

    /**
     *Busca los parametros dee conexion el la base de datos SQLite del aplicativo
     */
    public void buscard() {
        try {
            DatabaseHelper dbh = new DatabaseHelper(this, "Conexion", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();

            Cursor fila = database.rawQuery
                    ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id=1", null);

            if (fila.moveToFirst()) {
                IP=(fila.getString(0));
                PUERTO=(fila.getString(1));
                USUARIO=(fila.getString(5));
                CONTRASEÑA=(fila.getString(2));
                BD=(fila.getString(3));
                idAlma=(fila.getString(4));
                database.close();
            } else {
                Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){
            Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
        }
    }

    public void buscarel61() {
        try {
            DatabaseHelper dbh = new DatabaseHelper(this, "Conexion", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();

            Cursor fila = database.rawQuery
                    ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id=2", null);

            if (fila.moveToFirst()) {
                IP61 = (fila.getString(0));
                PUERTO61 = (fila.getString(1));
                USUARIO61 = (fila.getString(5));
                CONTRASEÑA61 = (fila.getString(2));
                BD61 = (fila.getString(3));
            } else{
                Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
                IP61 = IP;
                PUERTO61 = PUERTO;
                USUARIO61 = USUARIO;
                CONTRASEÑA61 = CONTRASEÑA;
                BD61 = "adCASACRU61";
            }
            if(IP61=="" || PUERTO61==""){
                IP61 = IP;
                PUERTO61 = PUERTO;
                USUARIO61 = USUARIO;
                CONTRASEÑA61 = CONTRASEÑA;
                BD61 = "adCASACRU61";
            }
            database.close();
        } catch (Exception ex) {
            Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *Busca los parametros dee conexion el la base de datos SQLite del aplicativo
     */
    public void buscarLagunas() {
        try {
            DatabaseHelper dbh = new DatabaseHelper(this, "Conexion", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();

            Cursor fila = database.rawQuery
                    ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id=3", null);

            if (fila.moveToFirst()) {
                IPLagunas=(fila.getString(0));
                PUERTOLagunas=(fila.getString(1));
                USUARIOLagunas=(fila.getString(5));
                CONTRASEÑALagunas=(fila.getString(2));
                BDLagunas=(fila.getString(3));
                idAlmaLagunas=(fila.getString(4));
                database.close();
            } else {
                Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){
            Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *Busca los parametros dee conexion el la base de datos SQLite del aplicativo
     */
    public void buscarSD() {
        try {
            DatabaseHelper dbh = new DatabaseHelper(this, "Conexion", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();

            Cursor fila = database.rawQuery
                    ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id=4", null);

            if (fila.moveToFirst()) {
                IPSD=(fila.getString(0));
                PUERTOSD=(fila.getString(1));
                USUARIOSD=(fila.getString(5));
                CONTRASEÑASD=(fila.getString(2));
                BDSD=(fila.getString(3));
                idAlmaSD=(fila.getString(4));
                database.close();
            }
        }catch (Exception ex){
            Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Realiza la conexion al servidor con la base de datos adDENGUI2019
     * @return
     */
    public Connection conexion2() {
        Connection conexion2 = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion2 = DriverManager.getConnection("jdbc:jtds:sqlserver://" + this.IP + ":" + this.PUERTO + ";databaseName=" + this.BD + ";user="+this.USUARIO+";password=" + this.CONTRASEÑA + ";");
        } catch (Exception e) {
            new DialogError(context,"Dengui");
        }
        return conexion2;
    }

    /**
     * Realiza la conexion al servidor con la base de datos adDENGIO 2019
     * @return
     */
    public Connection conexion61() {
        Connection conexion2 = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion2 = DriverManager.getConnection("jdbc:jtds:sqlserver://" + this.IP61 + ":" + this.PUERTO61 + ";databaseName="+BD61+";user="+this.USUARIO61+";password=" + this.CONTRASEÑA61 + ";");
        } catch (Exception e) {
            new DialogError(context,"el 61");
        }
        return conexion2;
    }

    /**
     * Realiza la conexion al servidor con la base de datos de lagunas
     * @return
     */
    public Connection conexionLagunas() {
        Connection conexion2 = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion2 = DriverManager.getConnection("jdbc:jtds:sqlserver://" + this.IPLagunas + ":" + this.PUERTOLagunas + ";databaseName="+BDLagunas+";user="+this.USUARIOLagunas+";password=" + this.CONTRASEÑALagunas + ";");
        } catch (Exception e) {
            new DialogError(context,"Lagunas");
        }
        return conexion2;
    }

    /**
     *Relaiza la conexion con la base de datos de santo domingo
     * @return
     */
    public Connection ConexionSD() {
        Connection conexion2 = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion2 = DriverManager.getConnection("jdbc:jtds:sqlserver://" + this.IPSD + ":" + this.PUERTOSD + ";databaseName="+BDSD+";user="+this.USUARIOSD+";password=" + this.CONTRASEÑASD + ";");
        } catch (Exception e) {
            new DialogError(context,"Santo Domingo");
        }
        return conexion2;
    }

    public Connection conexionMina() {
        Connection conexion2 = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion2 = DriverManager.getConnection("jdbc:jtds:sqlserver://" + this.IP + ":" + this.PUERTO + ";databaseName=adCASACRUMINA;user="+this.USUARIO+";password=" + this.CONTRASEÑA + ";");
        } catch (Exception e) {
            new DialogError(context,"Dengui");
        }
        return conexion2;
    }

    public Connection Conexionbloquera ()
    {
        Connection conexion2 = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion2 = DriverManager.getConnection("jdbc:jtds:sqlserver://" + this.IP + ":" + this.PUERTO + ";databaseName=adBLOQUERA;user=" + this.USUARIO + ";password=" + this.CONTRASEÑA + ";");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return conexion2;
    }


    @Override
    public void proggresbar(boolean status) {
        if(status){
            progressBar3.setVisibility(View.VISIBLE);
        }else{
            progressBar3.setVisibility(View.GONE);
        }
    }

    @Override
    public void Resultados(String total, String tienda) {
        if(tienda=="DENGUI"){
        totalDengui=total;
        }else if(tienda=="NOPALA") {
        totalNopala=total;
        }else if(tienda=="EL61"){
        total61=total;
        }else if(tienda=="LAGUNAS"){
        totalLagunas=total;
        }else if(tienda=="MATIAS"){
            totalMatias=total;
        }else if(tienda=="MINA"){
            totalMina=total;
        }else if(tienda=="BLOQUERA"){
            totalBloquera=total;
        }else if(tienda=="SD"){
        totalSantoDom=total;
            proggresbar(false);
            CrearGrafico();
        }
    }
}