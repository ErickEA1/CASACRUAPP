package com.casacru.casacruapp.Ventas;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.casacru.casacruapp.Dialog.DialogError;
import com.casacru.casacruapp.R;
import com.casacru.casacruapp.SQLite.DatabaseHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReporteDeVentasFechas extends AppCompatActivity {
    private CardView cv;
    LineChart lineChart;
    private LineData lineData;
    private LineDataSet lineDataSetDengui;
    private ArrayList lineEntries;
    private Button btnfech1, btnfech2, btnmostrar,btnsave;
    private int dia, mes, anio;
    private EditText txtfech1, txtfech2;
    private Context context;
    private String totalDengui, totalLagunas, total61, totalMatias, totalNopala, totalSantoDom;
    private String IP, PUERTO, USUARIO, CONTRASEÑA, BD, idAlma;
    private String IPSD, PUERTOSD, USUARIOSD, CONTRASEÑASD, BDSD, idAlmaSD;
    private String IP61, PUERTO61, USUARIO61, CONTRASEÑA61, BD61;
    private String IPLagunas, PUERTOLagunas, USUARIOLagunas, CONTRASEÑALagunas, BDLagunas, idAlmaLagunas;
    private CheckBox chdengui,chNopala, ch61,chLagunas,chSD,chMR,chMina,chBloquera;
    private LinearLayout lyfiltro;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_de_ventas_fechas);
        try {
            buscard();
            buscarLagunas();
            buscarSD();
            buscarel61();
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            context = this;
            chdengui=(CheckBox) findViewById(R.id.chDengui);
            chNopala=(CheckBox) findViewById(R.id.chNopala);
            ch61=(CheckBox) findViewById(R.id.ch61);
            chLagunas=(CheckBox) findViewById(R.id.chLagunas);
            chMR=(CheckBox) findViewById(R.id.chMR);
            chSD=(CheckBox) findViewById(R.id.chSD);
            chMina=(CheckBox) findViewById(R.id.chMina);
            chBloquera=(CheckBox) findViewById(R.id.chBloquera);
            cv=(CardView) findViewById(R.id.cv2);
            lyfiltro=(LinearLayout) findViewById(R.id.lytiendas);
            btnsave=(Button) findViewById(R.id.btnsave);
            btnfech1 = findViewById(R.id.button8);
            btnfech2 = findViewById(R.id.button7);
            txtfech1 = findViewById(R.id.txtfech1);
            txtfech2 = findViewById(R.id.txtfech2);
            btnmostrar = findViewById(R.id.btnMostrar);
            btnfech2.setOnClickListener(onclick);
            btnfech1.setOnClickListener(onclick);
            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lyfiltro.setVisibility(GONE);
                }
            });
            btnmostrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(chdengui.isChecked() || ch61.isChecked() || chLagunas.isChecked() || chMR.isChecked() || chNopala.isChecked() || chSD.isChecked() || chMina.isChecked() || chBloquera.isChecked())
                    {
                        Toast.makeText(context, "Cargando...", Toast.LENGTH_SHORT).show();
                        CustomGraphic();
                        cv.setVisibility(GONE);
                        Toast.makeText(context, "¡LISTO!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "SELECCIONA ALGUNA TIENDA", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            lineChart = (LineChart) findViewById(R.id.chart);



        }catch (Exception e){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu7, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item3:
                if(lyfiltro.getVisibility()==GONE){
                    lyfiltro.setVisibility(View.VISIBLE);
                    cv.setVisibility(View.VISIBLE);
                }else{
                    lyfiltro.setVisibility(GONE);
                    cv.setVisibility(GONE);
                }
                break;
        } return super.onOptionsItemSelected(item);
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
            } else {
                Toast.makeText(this, "No existen parametros de santo domingo", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "No existen parametros del 61", Toast.LENGTH_SHORT).show();
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
            new DialogError(context,"El 61");
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

    private ArrayList<Entry> lineChartDataSetDengui(){

        ArrayList<Entry> dataSet = new ArrayList<>();
        try {
            Statement stm = conexion2().createStatement();
            String Consulta="SELECT CFECHA, SUM(CTOTAL) FROM admDocumentos where CCANCELADO=0 AND (CIDCONCEPTODOCUMENTO=3056 OR CIDCONCEPTODOCUMENTO=3)" +
                    " GROUP BY CFECHA" +
                    " HAVING (CFECHA>= '"+txtfech1.getText().toString()+"' and CFECHA<='"+txtfech2.getText().toString()+"')";
            ResultSet rs = stm.executeQuery(Consulta);

            int i=0;
            while(rs.next()) {

                dataSet.add(new Entry(i, rs.getInt(2)));
                i++;
            }
            dataSet.add(new Entry());
            return dataSet;
        }catch (Exception e){

            return dataSet;
        }
    }
    private ArrayList<Entry> lineChartDataSetNopala(){

        ArrayList<Entry> dataSet = new ArrayList<>();
        try {
            Statement stm = conexion2().createStatement();
            String Consulta="SELECT CFECHA, SUM(CTOTAL) FROM admDocumentos where CCANCELADO=0 AND (CIDCONCEPTODOCUMENTO=3140 OR CIDCONCEPTODOCUMENTO=3142)" +
                    " GROUP BY CFECHA" +
                    " HAVING (CFECHA>= '"+txtfech1.getText().toString()+"' and CFECHA<='"+txtfech2.getText().toString()+"')";
            ResultSet rs = stm.executeQuery(Consulta);

            int i=0;
            while(rs.next()) {

                dataSet.add(new Entry(i, rs.getInt(2)));
                i++;
            }
            dataSet.add(new Entry());
            return dataSet;
        }catch (Exception e){
            return dataSet;
        }
    }
    private ArrayList<Entry> lineChartDataSet61(){

        ArrayList<Entry> dataSet = new ArrayList<>();
        try {
            Statement stm = conexion61().createStatement();
            String Consulta="SELECT CFECHA, SUM(CTOTAL) FROM admDocumentos where CCANCELADO=0 AND (CIDCONCEPTODOCUMENTO=3043 OR CIDCONCEPTODOCUMENTO=3)" +
                    " GROUP BY CFECHA" +
                    " HAVING (CFECHA>= '"+txtfech1.getText().toString()+"' and CFECHA<='"+txtfech2.getText().toString()+"')";
            ResultSet rs = stm.executeQuery(Consulta);

            int i=0;
            while(rs.next()) {

                dataSet.add(new Entry(i, rs.getInt(2)));
                i++;
            }
            dataSet.add(new Entry());
            return dataSet;
        }catch (Exception e){

            return dataSet;
        }
    }
    private ArrayList<Entry> lineChartDataSetLagunas(){

        ArrayList<Entry> dataSet = new ArrayList<>();
        try {
            Statement stm = conexionLagunas().createStatement();
            String Consulta="SELECT CFECHA, SUM(CTOTAL) FROM admDocumentos where CCANCELADO=0 AND (CIDCONCEPTODOCUMENTO=3050 OR CIDCONCEPTODOCUMENTO=3)" +
                    " GROUP BY CFECHA" +
                    " HAVING (CFECHA>= '"+txtfech1.getText().toString()+"' and CFECHA<='"+txtfech2.getText().toString()+"')";
            ResultSet rs = stm.executeQuery(Consulta);

            int i=0;
            while(rs.next()) {

                dataSet.add(new Entry(i, rs.getInt(2)));
                i++;
            }
            dataSet.add(new Entry());
            return dataSet;
        }catch (Exception e){

            return dataSet;
        }
    }
    private ArrayList<Entry> lineChartDataSetMatiasR(){

        ArrayList<Entry> dataSet = new ArrayList<>();
        try {
            Statement stm = conexionLagunas().createStatement();
            String Consulta="SELECT CFECHA, SUM(CTOTAL) FROM admDocumentos where CCANCELADO=0 AND (CIDCONCEPTODOCUMENTO=3077 OR CIDCONCEPTODOCUMENTO=3078)" +
                    " GROUP BY CFECHA" +
                    " HAVING (CFECHA>= '"+txtfech1.getText().toString()+"' and CFECHA<='"+txtfech2.getText().toString()+"')";
            ResultSet rs = stm.executeQuery(Consulta);

            int i=0;
            while(rs.next()) {

                dataSet.add(new Entry(i, rs.getInt(2)));
                i++;
            }
            dataSet.add(new Entry());
            return dataSet;
        }catch (Exception e){

            return dataSet;
        }
    }
    private ArrayList<Entry> lineChartDataSetSD(){

        ArrayList<Entry> dataSet = new ArrayList<>();
        try {
            Statement stm = ConexionSD().createStatement();
            String Consulta="SELECT CFECHA, SUM(CTOTAL) FROM admDocumentos where CCANCELADO=0 AND (CIDCONCEPTODOCUMENTO=3050 OR CIDCONCEPTODOCUMENTO=3)" +
                    " GROUP BY CFECHA" +
                    " HAVING (CFECHA>= '"+txtfech1.getText().toString()+"' and CFECHA<='"+txtfech2.getText().toString()+"')";
            ResultSet rs = stm.executeQuery(Consulta);

            int i=0;
            while(rs.next()) {

                dataSet.add(new Entry(i, rs.getInt(2)));
                i++;
            }
            dataSet.add(new Entry());
            return dataSet;
        }catch (Exception e){

            return dataSet;
        }
    }
    private ArrayList<Entry> lineChartDataSetMina(){

        ArrayList<Entry> dataSet = new ArrayList<>();
        try {
            Statement stm = conexionMina().createStatement();
            String Consulta="SELECT CFECHA, SUM(CTOTAL) FROM admDocumentos where CCANCELADO=0 AND (CIDCONCEPTODOCUMENTO=3014 OR CIDCONCEPTODOCUMENTO=3)" +
                    " GROUP BY CFECHA" +
                    " HAVING (CFECHA>= '"+txtfech1.getText().toString()+"' and CFECHA<='"+txtfech2.getText().toString()+"')";
            ResultSet rs = stm.executeQuery(Consulta);

            int i=0;
            while(rs.next()) {

                dataSet.add(new Entry(i, rs.getInt(2)));
                i++;
            }
            dataSet.add(new Entry());
            return dataSet;
        }catch (Exception e){

            return dataSet;
        }
    }
    private ArrayList<Entry> lineChartDataSetBloquera(){

        ArrayList<Entry> dataSet = new ArrayList<>();
        try {
            Statement stm = Conexionbloquera().createStatement();
            String Consulta="SELECT CFECHA, SUM(CTOTAL) FROM admDocumentos where CCANCELADO=0 AND (CIDCONCEPTODOCUMENTO=3014 OR CIDCONCEPTODOCUMENTO=3 OR CIDCONCEPTODOCUMENTO=3015 OR CIDCONCEPTODOCUMENTO=3016)" +
                    " GROUP BY CFECHA" +
                    " HAVING (CFECHA>= '"+txtfech1.getText().toString()+"' and CFECHA<='"+txtfech2.getText().toString()+"')";
            ResultSet rs = stm.executeQuery(Consulta);

            int i=0;
            while(rs.next()) {

                dataSet.add(new Entry(i, rs.getInt(2)));
                i++;
            }
            dataSet.add(new Entry());
            return dataSet;
        }catch (Exception e){

            return dataSet;
        }
    }



    public void CustomGraphic(){
        List<ILineDataSet> list=new ArrayList<ILineDataSet>();
if(chdengui.isChecked()) {
    // AJUSTE ESQUEMA DENGUI
    lineDataSetDengui = new LineDataSet(lineChartDataSetDengui(), "Dengui");
    lineDataSetDengui.setColors(Color.YELLOW);
    lineDataSetDengui.setValueTextColor(Color.BLACK);
    lineDataSetDengui.setCircleRadius(5f);
    lineDataSetDengui.setLineWidth(5);
    lineDataSetDengui.setValueTextSize(12f);
    list.add(lineDataSetDengui);
}
if(chNopala.isChecked()) {
    // AJUSTE ESQUEMA NOPALA
    LineDataSet lineDataSetNopala = new LineDataSet(lineChartDataSetNopala(), "Nopala");
    lineDataSetNopala.setColors(Color.RED);
    lineDataSetNopala.setValueTextColor(Color.BLACK);
    lineDataSetNopala.setCircleRadius(5f);
    lineDataSetNopala.setLineWidth(5);
    lineDataSetNopala.setValueTextSize(12f);
    list.add(lineDataSetNopala);
}
if(ch61.isChecked()) {
    //AJUSTE ESQUEMA EL 61
    LineDataSet lineDataSet61 = new LineDataSet(lineChartDataSet61(), "EL 61");
    lineDataSet61.setColors(Color.BLUE);
    lineDataSet61.setValueTextColor(Color.BLACK);
    lineDataSet61.setCircleRadius(5f);
    lineDataSet61.setLineWidth(5);
    lineDataSet61.setValueTextSize(12f);
    list.add(lineDataSet61);
}
if(chLagunas.isChecked()) {
    // AJUSTE ESQUEMA LAGUNAS
    LineDataSet lineDataSetLagunas = new LineDataSet(lineChartDataSetLagunas(), "Lagunas Oax.");
    lineDataSetLagunas.setColors(Color.BLACK);
    lineDataSetLagunas.setValueTextColor(Color.BLACK);
    lineDataSetLagunas.setCircleRadius(5f);
    lineDataSetLagunas.setLineWidth(5);
    lineDataSetLagunas.setValueTextSize(12f);
    list.add(lineDataSetLagunas);
}
if(chMR.isChecked()) {
    // AJUSTE ESQUEMA MATIAS ROMERO
    LineDataSet lineDataSetMatiasR = new LineDataSet(lineChartDataSetMatiasR(), "Matias Romero");
    lineDataSetMatiasR.setColors(Color.GREEN);
    lineDataSetMatiasR.setValueTextColor(Color.BLACK);
    lineDataSetMatiasR.setCircleRadius(5f);
    lineDataSetMatiasR.setLineWidth(5);
    lineDataSetMatiasR.setValueTextSize(12f);
    list.add(lineDataSetMatiasR);
}
if(chSD.isChecked()) {
    //AJSUTE ESQUEMA SANTO DIMINGO
    LineDataSet lineDataSetSD = new LineDataSet(lineChartDataSetSD(), "Santo Domingo");
    lineDataSetSD.setColors(Color.MAGENTA);
    lineDataSetSD.setValueTextColor(Color.BLACK);
    lineDataSetSD.setCircleRadius(5f);
    lineDataSetSD.setLineWidth(5);
    lineDataSetSD.setValueTextSize(12f);
    list.add(lineDataSetSD);
}
if(chMina.isChecked()) {
    //AJSUTE ESQUEMA MINA
    LineDataSet lineDataSetMina = new LineDataSet(lineChartDataSetMina(), "Mina");
    lineDataSetMina.setColors(Color.BLACK);
    lineDataSetMina.setValueTextColor(Color.BLACK);
    lineDataSetMina.setCircleRadius(5f);
    lineDataSetMina.setLineWidth(5);
    lineDataSetMina.setValueTextSize(12f);
    list.add(lineDataSetMina);
}
if(chBloquera.isChecked()) {
    //AJSUTE ESQUEMA BLOQUERA
    LineDataSet lineDataSetBloquera = new LineDataSet(lineChartDataSetBloquera(), "Bloquera");
    lineDataSetBloquera.setColors(Color.RED);
    lineDataSetBloquera.setValueTextColor(Color.BLACK);
    lineDataSetBloquera.setCircleRadius(5f);
    lineDataSetBloquera.setLineWidth(5);
    lineDataSetBloquera.setValueTextSize(12f);
    list.add(lineDataSetBloquera);
        }


        lineData=new LineData(list);
        lineChart.setData(lineData);
        lineChart.invalidate();
        lineChart.animateY(1400, Easing.EaseInOutElastic);
        lineChart.animate();

        Legend l=lineChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setEnabled(true);
        l.setTextSize(12f);
        l.setXEntrySpace(50);

        XAxis xAxis= lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getAreaCount()));



    }

    public ArrayList<String> getAreaCount(){
        ArrayList<String> labels=new ArrayList<>();
        try {
            Statement stm = conexion2().createStatement();
            String Consulta = "SELECT CFECHA FROM admDocumentos where CCANCELADO=0 AND (CIDCONCEPTODOCUMENTO=3056 OR CIDCONCEPTODOCUMENTO=3)" +
                    " GROUP BY CFECHA" +
                    " HAVING (CFECHA>= '"+txtfech1.getText().toString()+"' and CFECHA<='"+txtfech2.getText().toString()+"')";
            ResultSet rs = stm.executeQuery(Consulta);


            while (rs.next()) {
                String labelfech= rs.getString(1).substring(0,11);
               labels.add(labelfech);
            }
            return labels;
        }catch (Exception e){
            return labels;
        }
        }
}