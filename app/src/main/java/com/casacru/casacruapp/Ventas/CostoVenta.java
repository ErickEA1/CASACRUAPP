package com.casacru.casacruapp.Ventas;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.casacru.casacruapp.Dialog.DialogError;
import com.casacru.casacruapp.Interfaces.ICostoVenta;
import com.casacru.casacruapp.Modelo.ModelCostoVenta;
import com.casacru.casacruapp.R;
import com.casacru.casacruapp.TareasAsync.TareaCostoVenta;
import com.casacru.casacruapp.TareasAsync.TareaCostoVentaAsync;
import com.casacru.casacruapp.TareasAsync.TareaReporteVenta;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
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
import android.widget.Toast;

import com.casacru.casacruapp.Dialog.DialogError;
import com.casacru.casacruapp.R;
import com.casacru.casacruapp.SQLite.DatabaseHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CostoVenta extends AppCompatActivity implements OnChartValueSelectedListener, ICostoVenta {
        private PieChart pieChartView;
        private BarChart barchar, barchart2;
        private Button btnfech1, btnfech2;
        private int dia, mes, anio;
        private EditText txtfech1, txtfech2;
        private Context context;
        private String totalDengui, totalLagunas, total61, totalMatias, totalNopala, totalSantoDom;
        private String IP, PUERTO, USUARIO, CONTRASEÑA, BD, idAlma;
        private Button btnMostrarG;
        private String IPLagunas, PUERTOLagunas, USUARIOLagunas, CONTRASEÑALagunas, BDLagunas, idAlmaLagunas;
        private String IPSD, PUERTOSD, USUARIOSD, CONTRASEÑASD, BDSD, idAlmaSD;
        private String IP61, PUERTO61, USUARIO61, CONTRASEÑA61, BD61;
        private ArrayList<BarEntry> dataSet;
        private ArrayList<BarEntry> dataSet2;
        private ArrayList<BarEntry> dataSet3;
        private ArrayList<BarEntry> dataSet4;
        private ProgressBar progressBar4;
        private LinearLayout lyopciones,lypiechart,lybarchart,lybarchart2;
        private RadioButton rbBaras, rbPastel;
        private DecimalFormat formato;
        private float UtilidadDengui, UtilidadNopala,Utilidad61,UtilidadLagunas,UtilidadMR,UtilidadSD,UtilidadMina,UtilidadBloquera;
        private ArrayList<ModelCostoVenta> modelo=new ArrayList<>();
    private ArrayList<ModelCostoVenta> modelo2=new ArrayList<>();
        private int iterador;
        ArrayList<String> tiendas =new ArrayList<>();
    ArrayList<String> tiendas2 =new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_reporte_costo_venta);
            try {
                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                context = this;
                iterador=0;
                formato=new DecimalFormat("#,###.##");
                dataSet = new ArrayList<BarEntry>();
                dataSet2 = new ArrayList<BarEntry>();
                dataSet3 = new ArrayList<BarEntry>();
                dataSet4 = new ArrayList<BarEntry>();
                barchar = findViewById(R.id.chart);
                barchart2 = findViewById(R.id.chart2);
                lybarchart=(LinearLayout) findViewById(R.id.lybarchart);
                lybarchart2=(LinearLayout) findViewById(R.id.lybarchart2);
                lypiechart=(LinearLayout) findViewById(R.id.lypiechart);
                btnfech1 = findViewById(R.id.button8);
                btnfech2 = findViewById(R.id.button7);
                txtfech1 = findViewById(R.id.txtfech1);
                txtfech2 = findViewById(R.id.txtfech2);
                progressBar4=(ProgressBar) findViewById(R.id.progressBar4);
                btnMostrarG=(Button) findViewById(R.id.btnmostrarG);
                btnfech2.setOnClickListener(onclick);
                btnfech1.setOnClickListener(onclick);
                buscard();
                buscarLagunas();
                buscarSD();
                buscarel61();
                lyopciones=(LinearLayout) findViewById(R.id.lyopciones);
                rbBaras=(RadioButton) findViewById(R.id.rbbarchart);
                rbPastel=(RadioButton) findViewById(R.id.rbpiechart);
                pieChartView=(PieChart) findViewById(R.id.piechart);
                btnMostrarG.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dataSet.clear();
                        dataSet2.clear();
                        dataSet3.clear();
                        dataSet4.clear();
                        modelo.clear();
                        modelo2.clear();
                        tiendas.clear();
                        tiendas2.clear();

                        new TareaCostoVentaAsync(conexion2(), txtfech1.getText().toString(), txtfech2.getText().toString(), CostoVenta.this, "DENGUI").execute("3", "3056", "null", "null");
                    }
                });
            } catch (Exception e) {
            }
        }

            @Override
            public boolean onCreateOptionsMenu (Menu menu){
                getMenuInflater().inflate(R.menu.menu7, menu);
                return super.onCreateOptionsMenu(menu);
            }

            @Override
            public boolean onOptionsItemSelected (@NonNull MenuItem item){
                switch (item.getItemId()) {
                    case android.R.id.home:
                        finish();
                        break;
                    case R.id.item3:
                        if(lyopciones.getVisibility()==View.GONE){
                            lyopciones.setVisibility(View.VISIBLE);
                        }else{
                            lyopciones.setVisibility(View.GONE);
                        }
                }
                return super.onOptionsItemSelected(item);
            }


            public View.OnClickListener onclick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    switch (v.getId()) {
                        case R.id.button7:

                            dia = c.get(Calendar.DAY_OF_MONTH);
                            mes = c.get(Calendar.MONTH);
                            anio = c.get(Calendar.YEAR);

                            DatePickerDialog fecha = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    txtfech2.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                                }
                            }, dia, mes, anio);
                            fecha.show();


                            break;
                        case R.id.button8:
                            dia = c.get(Calendar.DAY_OF_MONTH);
                            mes = c.get(Calendar.MONTH);
                            anio = c.get(Calendar.YEAR);

                            DatePickerDialog fecha2 = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    txtfech1.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                                }
                            }, dia, mes, anio);
                            fecha2.show();
                            break;
                    }
                }
            };

            /**
             *Busca los parametros dee conexion el la base de datos SQLite del aplicativo
             */
            public void buscard () {
                try {
                    DatabaseHelper dbh = new DatabaseHelper(this, "Conexion", null, 1);
                    SQLiteDatabase database = dbh.getWritableDatabase();

                    Cursor fila = database.rawQuery
                            ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id=1", null);

                    if (fila.moveToFirst()) {
                        IP = (fila.getString(0));
                        PUERTO = (fila.getString(1));
                        USUARIO = (fila.getString(5));
                        CONTRASEÑA = (fila.getString(2));
                        BD = (fila.getString(3));
                        idAlma = (fila.getString(4));
                        database.close();
                    } else {
                        Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
                }
            }
            /**
             *Busca los parametros dee conexion el la base de datos SQLite del aplicativo
             */
            public void buscarLagunas () {
                try {
                    DatabaseHelper dbh = new DatabaseHelper(this, "Conexion", null, 1);
                    SQLiteDatabase database = dbh.getWritableDatabase();

                    Cursor fila = database.rawQuery
                            ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id=3", null);

                    if (fila.moveToFirst()) {
                        IPLagunas = (fila.getString(0));
                        PUERTOLagunas = (fila.getString(1));
                        USUARIOLagunas = (fila.getString(5));
                        CONTRASEÑALagunas = (fila.getString(2));
                        BDLagunas = (fila.getString(3));
                        idAlmaLagunas = (fila.getString(4));
                        database.close();
                    } else {
                        Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
                }
            }

            /**
             *Busca los parametros dee conexion el la base de datos SQLite del aplicativo
             */
            public void buscarSD () {
                try {
                    DatabaseHelper dbh = new DatabaseHelper(this, "Conexion", null, 1);
                    SQLiteDatabase database = dbh.getWritableDatabase();

                    Cursor fila = database.rawQuery
                            ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id=4", null);

                    if (fila.moveToFirst()) {
                        IPSD = (fila.getString(0));
                        PUERTOSD = (fila.getString(1));
                        USUARIOSD = (fila.getString(5));
                        CONTRASEÑASD = (fila.getString(2));
                        BDSD = (fila.getString(3));
                        idAlmaSD = (fila.getString(4));
                        database.close();
                    }
                    database.close();
                } catch (Exception ex) {
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
             * Realiza la conexion al servidor con la base de datos adDENGUI2019
             * @return
             */
            public Connection conexion2 () {
                Connection conexion2 = null;
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                    conexion2 = DriverManager.getConnection("jdbc:jtds:sqlserver://" + this.IP + ":" + this.PUERTO + ";databaseName=" + this.BD + ";user=" + this.USUARIO + ";password=" + this.CONTRASEÑA + ";");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return conexion2;
            }

            /**
             * Realiza la conexion al servidor con la base de datos adDENGIO 2019
             * @return
             */
            public Connection conexion61 () {
                Connection conexion2 = null;
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                    conexion2 = DriverManager.getConnection("jdbc:jtds:sqlserver://" + this.IP61 + ":" + this.PUERTO61 + ";databaseName="+BD61+";user=" + this.USUARIO61 + ";password=" + this.CONTRASEÑA61 + ";");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return conexion2;
            }

            /**
             * Realiza la conexion al servidor con la base de datos de lagunas
             * @return
             */
            public Connection conexionLagunas () {
                Connection conexion2 = null;
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                    conexion2 = DriverManager.getConnection("jdbc:jtds:sqlserver://" + this.IPLagunas + ":" + this.PUERTOLagunas + ";databaseName=" + BDLagunas + ";user=" + this.USUARIOLagunas + ";password=" + this.CONTRASEÑALagunas + ";");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return conexion2;
            }

            /**
             *Relaiza la conexion con la base de datos de santo domingo
             * @return
             */
            public Connection ConexionSD () {
                Connection conexion2 = null;
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                    conexion2 = DriverManager.getConnection("jdbc:jtds:sqlserver://" + this.IPSD + ":" + this.PUERTOSD + ";databaseName="+BDSD+";user=" + this.USUARIOSD + ";password=" + this.CONTRASEÑASD + ";");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

            public Connection Conexionbloquera () {
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
    public void proggresbarr(boolean status) {
        if(status){
            progressBar4.setVisibility(View.VISIBLE);
        }else{
            progressBar4.setVisibility(View.GONE);
        }
    }

    @Override
    public void lineChartDataSetDenguiVenta (float total, float totalCosto) {
                try {
                    dataSet.add(new BarEntry(1, total));
                    dataSet2.add(new BarEntry(1,totalCosto));
                    tiendas.add("DENGUI");
                    iterador++;
                    UtilidadDengui=total-totalCosto;
                    float porcentaje=(UtilidadDengui/total) * 100;
                    BigDecimal format=new BigDecimal(porcentaje);
                    format = format.setScale(2, RoundingMode.DOWN);
                    String v=String.valueOf(format);
                    String totalformato=formato.format(total);
                    String totalcostoformato=formato.format(totalCosto);
                    String diferenciaformato=formato.format(UtilidadDengui);
                    modelo.add(new ModelCostoVenta(totalformato,totalcostoformato,UtilidadDengui,diferenciaformato,v,"Tienda Dengui"));
                    new TareaCostoVentaAsync(conexion2(),txtfech1.getText().toString(),txtfech2.getText().toString(),CostoVenta.this,"NOPALA").execute("3140", "3142","null", "null");

                } catch (Exception e) {
                    String Mensaje = e.getMessage();
                 //   new DialogError(context,"Dengui");
                }
            }
    @Override
    public void lineChartDataSetNopalaVenta (float total, float totalCosto) {
                try {
                    dataSet.add(new BarEntry(2, total));
                    dataSet2.add(new BarEntry(2,totalCosto));
                    tiendas.add("NOPALA");
                    iterador++;
                    UtilidadNopala=total-totalCosto;
                    float porcentaje=(UtilidadNopala/total)*100;
                    BigDecimal format=new BigDecimal(porcentaje);
                    format = format.setScale(2, RoundingMode.DOWN);
                    String v=String.valueOf(format);
                    String totalformato=formato.format(total);
                    String totalcostoformato=formato.format(totalCosto);
                    String diferenciaformato=formato.format(UtilidadNopala);
                    modelo.add(new ModelCostoVenta(totalformato,totalcostoformato,UtilidadNopala,diferenciaformato,v,"TIENDA NOPALA"));
                    new TareaCostoVentaAsync(conexionMina(),txtfech1.getText().toString(),txtfech2.getText().toString(),CostoVenta.this,"MINA").execute("3", "3014","null", "null");

                } catch (Exception e) {
                    String Mensaje = e.getMessage();
                   // new DialogError(context,"Nopala");

                }
            }
    @Override
    public void lineChartDataSetMina (float total, float totalCosto) {
        try {
            dataSet3.add(new BarEntry(1, total));
            dataSet4.add(new BarEntry(1, totalCosto));
            tiendas2.add("MINA");
            iterador++;
            UtilidadMina=total-totalCosto;
            float porcentaje=(UtilidadMina/total)*100;
            BigDecimal format=new BigDecimal(porcentaje);
            format = format.setScale(2, RoundingMode.DOWN);
            String v=String.valueOf(format);
            String totalformato=formato.format(total);
            String totalcostoformato=formato.format(totalCosto);
            String diferenciaformato=formato.format(UtilidadMina);
            if(rbBaras.isChecked()){
                modelo2.add(new ModelCostoVenta(totalformato,totalcostoformato,UtilidadMina,diferenciaformato,v,"MINA"));
            }else{
                modelo.add(new ModelCostoVenta(totalformato,totalcostoformato,UtilidadMina,diferenciaformato,v,"MINA"));
            }
            new TareaCostoVentaAsync(conexionLagunas(),txtfech1.getText().toString(),txtfech2.getText().toString(),CostoVenta.this,"LAGUNAS").execute("3", "3050","null", "null");

        } catch (Exception e) {
            String Mensaje = e.getMessage();
            //new DialogError(context,"MINA");

        }
    }
    @Override
    public void lineChartDataSet61Venta (float total, float totalCosto) {
                try {
//                    Statement stm = conexion61().createStatement();
//                    String Consulta = "SELECT CNETO, CCOSTOESPECIFICO FROM admMovimientos " +
//                            "WHERE ((CIDDOCUMENTO) IN (SELECT CIDDOCUMENTO FROM admDocumentos " +
//                            "WHERE ((CFECHA>= '" + txtfech1.getText().toString() + "' and CFECHA<='" + txtfech2.getText().toString() + "') " +
//                            "AND (CCANCELADO)=0 AND (CIDCONCEPTODOCUMENTO=3043 OR CIDCONCEPTODOCUMENTO=3))))";
//                    ResultSet rs = stm.executeQuery(Consulta);
//                    float total = 0;
//                    float totalcosto=0;
//                    while (rs.next()) {
//                        total += Float.parseFloat(rs.getString(1));
//                        totalcosto += Float.parseFloat(rs.getString(2));
//                    }
                    dataSet.add(new BarEntry(6, total));
                    dataSet2.add(new BarEntry(6,totalCosto));
                    tiendas.add("EL 61");
                    iterador++;
                    Utilidad61=total-totalCosto;
                    float porcentaje=(Utilidad61/total)*100;
                    BigDecimal format=new BigDecimal(porcentaje);
                    format = format.setScale(2, RoundingMode.DOWN);
                    String v=String.valueOf(format);
                    String totalformato=formato.format(total);
                    String totalcostoformato=formato.format(totalCosto);
                    String diferenciaformato=formato.format(Utilidad61);
                    modelo.add(new ModelCostoVenta(totalformato,totalcostoformato,Utilidad61,diferenciaformato,v, "TIENDA 61"));
                    if(rbBaras.isChecked()){
                        try {
                            lypiechart.setVisibility(View.GONE);
                            lybarchart.setVisibility(View.VISIBLE);
                            lybarchart2.setVisibility(View.VISIBLE);
                            CustomGraphic();
                            CustomGraphic2();

                        }catch (Exception e){

                        }
                    }
                    else if(rbPastel.isChecked()){
                        try {
                            lybarchart.setVisibility(View.GONE);
                            lypiechart.setVisibility(View.VISIBLE);
                            lybarchart2.setVisibility(View.GONE);
                            CrearGrafico();
                        }catch (Exception e){

                        }
                    }
                    lyopciones.setVisibility(View.GONE);
                    Toast.makeText(context, "¡Listo!", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    String Mensaje = e.getMessage();
              //      new DialogError(context,"el 61");
                }
            }
    @Override
    public void lineChartDataSetLagunasVenta (float total, float totalCosto) {
                try {
                    dataSet.add(new BarEntry(3, total));
                    dataSet2.add(new BarEntry(3,totalCosto));
                    tiendas.add("LAGUNAS");
                    iterador++;
                    UtilidadLagunas=total-totalCosto;
                    float porcentaje=(UtilidadLagunas/total)*100;
                    BigDecimal format=new BigDecimal(porcentaje);
                    format = format.setScale(2, RoundingMode.DOWN);
                    String v=String.valueOf(format);
                    String totalformato=formato.format(total);
                    String totalcostoformato=formato.format(totalCosto);
                    String diferenciaformato=formato.format(UtilidadLagunas);
                    modelo.add(new ModelCostoVenta(totalformato,totalcostoformato,UtilidadLagunas,diferenciaformato,v,"TIENDA LAGUNAS"));
                    new TareaCostoVentaAsync(Conexionbloquera(),txtfech1.getText().toString(),txtfech2.getText().toString(),CostoVenta.this,"BLOQUERA").execute("3", "3014","3015","3016");
                } catch (Exception e) {
                    String Mensaje = e.getMessage();
                    //new DialogError(context, "Lagunas");
                }
            }
    @Override
    public void lineChartDataSetMatiasRVenta (float total, float totalCosto) {
                try {

                    dataSet.add(new BarEntry(4, total));
                    dataSet2.add(new BarEntry(4,totalCosto));
                    tiendas.add("MATIAS ROMERO");
                    iterador++;
                    UtilidadMR=total-totalCosto;
                    float porcentaje=(UtilidadMR/total)*100;
                    BigDecimal format=new BigDecimal(porcentaje);
                    format = format.setScale(2, RoundingMode.DOWN);
                    String v=String.valueOf(format);
                    String totalformato=formato.format(total);
                    String totalcostoformato=formato.format(totalCosto);
                    String diferenciaformato=formato.format(UtilidadMR);
                    modelo.add(new ModelCostoVenta(totalformato,totalcostoformato,UtilidadMR,diferenciaformato,v,"MATIAS ROMERO "));
                    new TareaCostoVentaAsync(ConexionSD(),txtfech1.getText().toString(),txtfech2.getText().toString(),CostoVenta.this,"SD").execute("3", "3050","null", "null");
                } catch (Exception e) {
                    String Mensaje = e.getMessage();
                   // new DialogError(context,"Matias Romero");

                }
            }
    @Override
    public void lineChartDataSetSDVenta (float total, float totalCosto) {
                try {

                    dataSet.add(new BarEntry(5, total));
                    dataSet2.add(new BarEntry(5, totalCosto));
                    tiendas.add("SANTO DOMINGO");
                    iterador++;
                    UtilidadSD=total-totalCosto;
                    float porcentaje=(UtilidadSD/total)*100;
                    BigDecimal format=new BigDecimal(porcentaje);
                    format = format.setScale(2, RoundingMode.DOWN);
                    String v=String.valueOf(format);
                    String totalformato=formato.format(total);
                    String totalcostoformato=formato.format(totalCosto);
                    String diferenciaformato=formato.format(UtilidadSD);
                    modelo.add(new ModelCostoVenta(totalformato,totalcostoformato,UtilidadSD,diferenciaformato,v,"SANTO DOMINGO"));
                    new TareaCostoVentaAsync(conexion61(),txtfech1.getText().toString(),txtfech2.getText().toString(),CostoVenta.this,"EL 61").execute("3", "3043","null", "null");
                } catch (Exception e) {
                    String Mensaje = e.getMessage();
                    //new DialogError(context,"Santo domingo");

                }
         }
    @Override
    public void lineChartDataSetBloquera (float total, float totalCosto) {
                try {

                    dataSet3.add(new BarEntry(2, total));
                    dataSet4.add(new BarEntry(2, totalCosto));
                    tiendas2.add("BLOQUERA");
                    iterador++;
                    UtilidadBloquera=total-totalCosto;
                    float porcentaje=(UtilidadBloquera/total)*100;
                    BigDecimal format=new BigDecimal(porcentaje);
                    format = format.setScale(2, RoundingMode.DOWN);
                    String v=String.valueOf(format);
                    String totalformato=formato.format(total);
                    String totalcostoformato=formato.format(totalCosto);
                    String diferenciaformato=formato.format(UtilidadBloquera);
                    if(rbBaras.isChecked()){
                        modelo2.add(new ModelCostoVenta(totalformato,totalcostoformato,UtilidadBloquera,diferenciaformato,v,"BLOQUERA"));
                    }else{
                        modelo.add(new ModelCostoVenta(totalformato,totalcostoformato,UtilidadBloquera,diferenciaformato,v,"BLOQUERA"));
                    }
                    new TareaCostoVentaAsync(conexionLagunas(),txtfech1.getText().toString(),txtfech2.getText().toString(),CostoVenta.this,"MATIAS").execute("3077", "3078","null", "null");
                    } catch (Exception e) {
                    String Mensaje = e.getMessage();
                    //new DialogError(context,"BLOQUERA");

                }
            }

            public void CustomGraphic() {
    try {
        // AJUSTE ESQUEMA VENTAS
        BarDataSet venta = new BarDataSet(dataSet, "VENTA");
        venta.setColor(Color.BLUE);
        venta.setValueTextSize(12f);

        // AJUSTE ESQUEMA COSTO
        BarDataSet costo = new BarDataSet(dataSet2, "COSTO");
        costo.setColor(Color.RED);
        costo.setValueTextSize(12f);


        BarData barData = new BarData(venta, costo);
        barchar.setData(barData);

        Legend l = barchar.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setEnabled(true);
        l.setTextSize(16f);
        l.setXEntrySpace(50);

        XAxis xAxis = barchar.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(tiendas));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        barchar.setDragEnabled(true);
        barchar.setVisibleXRangeMaximum(2);

        float barSpace = 0.05f;
        float groupSpace = 0.58f;
        barData.setBarWidth(0.16f);
        barchar.getXAxis().setAxisMinimum(0);
        barchar.getXAxis().setAxisMaximum(0 + barchar.getBarData().getGroupWidth(groupSpace, barSpace) * 7);
        barchar.getAxisLeft().setAxisMinimum(0);
        barchar.groupBars(0, groupSpace, barSpace);
        barchar.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                try {
                    float l = (float) Math.floor(e.getX());
                    int k = Math.round(l);
                    String Tienda = modelo.get(k).getTienda();
                    String venta = String.valueOf(modelo.get(k).getVenta());
                    String costo = String.valueOf(modelo.get(k).getCosto());
                    String diferencia = String.valueOf(modelo.get(k).getDiferenciaformato());
                    String porcentaje = String.valueOf(modelo.get(k).getDiferenciaProcentual());

                    AlertDialog.Builder builder = new AlertDialog.Builder(CostoVenta.this);
                    builder.setCancelable(true);
                    View view = LayoutInflater.from(CostoVenta.this).inflate(R.layout.datos_costo_venta, null);
                    TextView textViewtienda = view.findViewById(R.id.txtTienda);
                    TextView textViewcosto = view.findViewById(R.id.txtCosto);
                    TextView textViewventa = view.findViewById(R.id.txtVenta);
                    TextView textViewDiferencia = view.findViewById(R.id.txtdiferencia);
                    TextView textViewporcentaje = view.findViewById(R.id.txtdiferenciaporc);

                    textViewtienda.setText(Tienda);
                    textViewcosto.setText("Costo: $" + costo);
                    textViewventa.setText("Venta: $" + venta);
                    textViewDiferencia.setText("Utilidad bruta de la tienda: \n $" +diferencia);
                    textViewporcentaje.setText("Porcentaje de utilidad: " + porcentaje + "%");


                    builder.setView(view);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } catch (Exception ex) {
                    String Mensaje = ex.getMessage();
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        barchar.invalidate();
    }catch (Exception e){

    }
                }

            public void CustomGraphic2() {
        try {
            // AJUSTE ESQUEMA VENTAS
            BarDataSet venta = new BarDataSet(dataSet3, "VENTA");
            venta.setColor(Color.BLUE);
            venta.setValueTextSize(12f);

            // AJUSTE ESQUEMA COSTO
            BarDataSet costo = new BarDataSet(dataSet4, "COSTO");
            costo.setColor(Color.RED);
            costo.setValueTextSize(12f);


            BarData barData = new BarData(venta, costo);
            barchart2.setData(barData);

            Legend l = barchart2.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setEnabled(true);
            l.setTextSize(16f);
            l.setXEntrySpace(50);

            XAxis xAxis = barchart2.getXAxis();
            xAxis.setDrawGridLines(false);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(tiendas2));
            xAxis.setCenterAxisLabels(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1);
            xAxis.setGranularityEnabled(true);
            barchart2.setDragEnabled(true);
            barchart2.setVisibleXRangeMaximum(2);

            float barSpace = 0.05f;
            float groupSpace = 0.58f;
            barData.setBarWidth(0.16f);
            barchart2.getXAxis().setAxisMinimum(0);
            barchart2.getXAxis().setAxisMaximum(0 + barchart2.getBarData().getGroupWidth(groupSpace, barSpace) * 7);
            barchart2.getAxisLeft().setAxisMinimum(0);
            barchart2.groupBars(0, groupSpace, barSpace);
            barchart2.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    try {
                        float l = (float) Math.floor(e.getX());
                        int k = Math.round(l);
                        String Tienda = modelo2.get(k).getTienda();
                        String venta = String.valueOf(modelo2.get(k).getVenta());
                        String costo = String.valueOf(modelo2.get(k).getCosto());
                        String diferencia = String.valueOf(modelo2.get(k).getDiferenciaformato());
                        String porcentaje = String.valueOf(modelo2.get(k).getDiferenciaProcentual());

                        AlertDialog.Builder builder = new AlertDialog.Builder(CostoVenta.this);
                        builder.setCancelable(true);
                        View view = LayoutInflater.from(CostoVenta.this).inflate(R.layout.datos_costo_venta, null);
                        TextView textViewtienda = view.findViewById(R.id.txtTienda);
                        TextView textViewcosto = view.findViewById(R.id.txtCosto);
                        TextView textViewventa = view.findViewById(R.id.txtVenta);
                        TextView textViewDiferencia = view.findViewById(R.id.txtdiferencia);
                        TextView textViewporcentaje = view.findViewById(R.id.txtdiferenciaporc);

                        textViewtienda.setText(Tienda);
                        textViewcosto.setText("Costo: $" + costo);
                        textViewventa.setText("Venta: $" + venta);
                        textViewDiferencia.setText("Utilidad bruta de la tienda: \n $" +diferencia);
                        textViewporcentaje.setText("Porcentaje de utilidad: " + porcentaje + "%");


                        builder.setView(view);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } catch (Exception ex) {
                        String Mensaje = ex.getMessage();
                    }
                }

                @Override
                public void onNothingSelected() {

                }
            });
            barchart2.invalidate();

        }catch (Exception e){

        }
    }

            public void CrearGrafico() {
    try {
        ArrayList<PieEntry> visitor = new ArrayList<>();

        float t = 0;
        for (int i = 0; i < modelo.size(); i++) {
            t += modelo.get(i).getDiferencia();
        }

        for(int i = 0; i < modelo.size(); i++){
            String tienda = modelo.get(i).getTienda();
            float porcentaje= modelo.get(i).getDiferencia()/t;
            BigDecimal format=new BigDecimal(porcentaje);
            format = format.setScale(3, RoundingMode.DOWN);
            String v=String.valueOf(format);
            float f=Math.round(Float.parseFloat(v) * 100);
            visitor.add(new PieEntry(f, tienda));
        }

        int[] colors = {Color.rgb(255, 171, 0), Color.rgb(56, 142, 60),Color.rgb(218,101,18), Color.rgb(223, 28, 68), Color.rgb(25, 74, 141),
                Color.rgb(123, 31, 162), Color.rgb(109, 86, 27),Color.rgb(0, 200, 137)};
        PieDataSet pieDataSet = new PieDataSet(visitor, "TIENDAS");
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(18f);
        pieDataSet.setSliceSpace(2f);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(pieChartView));
        pieChartView.setData(pieData);
        pieChartView.setDrawSliceText(false);
        pieChartView.setEntryLabelTextSize(12f);
        pieChartView.getDescription().setEnabled(false);
        pieChartView.setDrawHoleEnabled(true);
        pieChartView.setUsePercentValues(true);
    pieChartView.invalidate();
        Legend l = pieChartView.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
        DecimalFormat formato=new DecimalFormat("#,###.00");
        String val=formato.format(t);
        pieChartView.setCenterText("UTILIDAD BRUTA: \n $" + val);
        pieChartView.setCenterTextSize(17f);
        pieChartView.animateY(1400, Easing.EaseInOutQuad);
        pieChartView.animate();

        pieChartView.setOnChartValueSelectedListener(this);
        pieChartView.setTouchEnabled(true);
    }catch (Exception e){
    String mensaje=e.getMessage();
    }
        }


             @Override
             public void onValueSelected(Entry e, Highlight h) {
            try {
                float f=h.getX();
                int k=Math.round(f);
                String Tienda = modelo.get(k).getTienda();
                String venta = String.valueOf(modelo.get(k).getVenta());
                String costo = String.valueOf(modelo.get(k).getCosto());
                String diferencia = String.valueOf(modelo.get(k).getDiferenciaformato());
                String Porcentaje= modelo.get(k).getDiferenciaProcentual();


                AlertDialog.Builder builder = new AlertDialog.Builder(CostoVenta.this);
                builder.setCancelable(true);
                View view = LayoutInflater.from(CostoVenta.this).inflate(R.layout.datos_costo_venta, null);
                TextView textViewtienda = view.findViewById(R.id.txtTienda);
                TextView textViewcosto = view.findViewById(R.id.txtCosto);
                TextView textViewventa = view.findViewById(R.id.txtVenta);
                TextView textViewDiferencia = view.findViewById(R.id.txtdiferencia);
                TextView textViewporcentaje=view.findViewById(R.id.txtdiferenciaporc);

                textViewtienda.setText(Tienda);
                textViewcosto.setText("Costo: $" + costo);
                textViewventa.setText("Venta: $" + venta);
                textViewDiferencia.setText("Utilidad bruta de la tienda: \n $" + diferencia);
                textViewporcentaje.setText("Porcentaje de utilidad: \n " + Porcentaje+ "%");


                builder.setView(view);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } catch (Exception ex) {
                String Mensaje = ex.getMessage();
            }
        }

        @Override
        public void onNothingSelected() {

        }
}
