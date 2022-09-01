package com.casacru.casacruapp.Almacen;

import static android.view.View.GONE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.casacru.casacruapp.Dialog.DialogError;
import com.casacru.casacruapp.General.ListadoProductos;
import com.casacru.casacruapp.Interfaces.IProductosMensual;
import com.casacru.casacruapp.Modelo.ModeloProductos;
import com.casacru.casacruapp.R;
import com.casacru.casacruapp.SQLite.DatabaseHelper;
import com.casacru.casacruapp.TareasAsync.TareaProductosMensual;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class VendidosMensual extends AppCompatActivity implements IProductosMensual {
    private String IP, PUERTO, USUARIO, CONTRASEÑA, BD, idAlma,idBusqueda;
    Context context;
    private EditText txtCod;
    private Button btnBusca2, button4,btnsave;
    private String anio, IdProducto, aniofech,mesfech;
    private Spinner spinner2;
    private boolean chDenguiV,chTiendaNopalaV,chTLAGUNAS,chTiendaMatias,chTiendaSD,chTienda61V;
    private TextView txtnom1,txtpromedio;
    private ArrayList<Entry> dataSet = new ArrayList<>();
    private ArrayList<String> meses = new ArrayList<>();
    private LineChart lineChart;
    CheckBox chdengui2,chNopala2, ch612,chLagunas2,chSD2,chMR2;
    private LinearLayout lyfiltro;
    private String IPSD, PUERTOSD, USUARIOSD, CONTRASEÑASD, BDSD, idAlmaSD;
    private String IP61, PUERTO61, USUARIO61, CONTRASEÑA61, BD61;
    private String IPLagunas, PUERTOLagunas, USUARIOLagunas, CONTRASEÑALagunas, BDLagunas, idAlmaLagunas;
    String[] anios;
    private ProgressBar progressBar5;
    private float ene,feb,mar,abr,may,jun,jul,ago,sep,oct,nov,dec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_vendidos_mensual);
        try {
            chDenguiV=false;chTiendaNopalaV=false;chTLAGUNAS=false;chTiendaMatias=false;chTiendaSD=false;chTienda61V=false;
            context = this;
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            anios = new String[]{"*SELECCIONA UN AÑO*", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024",
                    "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038",
                    "2039", "2040"};
            spinner2 = (Spinner) findViewById(R.id.spinner2);
            txtCod = (EditText) findViewById(R.id.txtCod);
            txtpromedio=(TextView) findViewById(R.id.txtPromedio);
            lineChart=(LineChart) findViewById(R.id.linechart);
            chdengui2=(CheckBox) findViewById(R.id.chDengui1);
            chNopala2=(CheckBox) findViewById(R.id.chNopala1);
            ch612=(CheckBox) findViewById(R.id.ch611);
            chLagunas2=(CheckBox) findViewById(R.id.chLagunas1);
            chMR2=(CheckBox) findViewById(R.id.chMR1);
            chSD2=(CheckBox) findViewById(R.id.chSD1);
            lyfiltro=(LinearLayout) findViewById(R.id.lytiendas);
            btnsave=(Button) findViewById(R.id.btnsave);
            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lyfiltro.setVisibility(GONE);
                }
            });
            btnBusca2 = (Button) findViewById(R.id.btnBusca2);
            button4=(Button) findViewById(R.id.btnEscanear);
            btnBusca2.setOnClickListener(nOnClickListener);
            button4.setOnClickListener(nOnClickListener);
            txtnom1 = (TextView) findViewById(R.id.txtNombre);
            progressBar5=(ProgressBar) findViewById(R.id.progressBar5);
            ArrayAdapter<CharSequence> adaptadorAnios = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, anios);
            spinner2.setAdapter(adaptadorAnios);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    anio = (String) spinner2.getSelectedItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            lista();
            ObtenerSharePreferences();

            if(anio!=null){
                int Spinnerposition=adaptadorAnios.getPosition(anio);
                spinner2.setSelection(Spinnerposition);
            }
            Inicializar();
        }catch (Exception e){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu7, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.item3:
                if(lyfiltro.getVisibility()==GONE){
                    lyfiltro.setVisibility(View.VISIBLE);

                }else{
                    lyfiltro.setVisibility(GONE);

                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    /**
     *Busca los parametros dee conexion el la base de datos SQLite del aplicativo
     */
    public void buscard(String Id) {
        try {
            DatabaseHelper dbh = new DatabaseHelper(this, "Conexion", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();

            if(Id=="1") {
                Cursor fila = database.rawQuery
                        ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id='" + Id + "'", null);

                if (fila.moveToFirst()) {
                    IP = (fila.getString(0));
                    PUERTO = (fila.getString(1));
                    USUARIO = (fila.getString(5));
                    CONTRASEÑA = (fila.getString(2));
                    BD = (fila.getString(3));
                    database.close();
                } else {
                    //Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
                }
            }else if(Id=="2"){
                Cursor fila = database.rawQuery
                        ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id='" + Id + "'", null);

                if (fila.moveToFirst()) {
                    IP61 = (fila.getString(0));
                    PUERTO61 = (fila.getString(1));
                    USUARIO61 = (fila.getString(5));
                    CONTRASEÑA61 = (fila.getString(2));
                    BD61 = (fila.getString(3));
                    database.close();
                } else {
                    //Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
                }
            }else if(Id=="3"){
                Cursor fila = database.rawQuery
                        ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id='" + Id + "'", null);

                if (fila.moveToFirst()) {
                    IPLagunas = (fila.getString(0));
                    PUERTOLagunas = (fila.getString(1));
                    USUARIOLagunas = (fila.getString(5));
                    CONTRASEÑALagunas = (fila.getString(2));
                    BDLagunas = (fila.getString(3));
                    database.close();
                } else {
                    //Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
                }
            }else if(Id=="4"){
                Cursor fila = database.rawQuery
                        ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id='" + Id + "'", null);

                if (fila.moveToFirst()) {
                    IPSD = (fila.getString(0));
                    PUERTOSD = (fila.getString(1));
                    USUARIOSD = (fila.getString(5));
                    CONTRASEÑASD = (fila.getString(2));
                    BDSD = (fila.getString(3));
                    database.close();
                } else {
                    //Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
                }
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
            new DialogError(context,"");
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

    /**
     *Consultar Id, nombre y valor de la clasificacion5(proveedor) del producto
     */
    public String consultarIdProducto(Connection con) {
        try {
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT CIDPRODUCTO, CNOMBREPRODUCTO FROM admProductos where CCODIGOPRODUCTO='" + txtCod.getText().toString() + "'");

            if (rs.next()) {
                txtnom1.setVisibility(View.VISIBLE);
                IdProducto=rs.getString(1);
                txtnom1.setText(rs.getString(2));
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return IdProducto;
    }

    /**
     * Metodo onClick para los diferentes botones de la actividad
     */
    private View.OnClickListener nOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnEscanear:
                    ene=0;feb=0;mar=0;abr=0;may=0;jun=0;jul=0;ago=0;sep=0;oct=0;nov=0;dec=0;
                    if(anio != null) {
                        dataSet.clear();
                        if(anio!="*SELECCIONA UN AÑO*"){
                            if(chdengui2.isChecked() || chNopala2.isChecked() || chSD2.isChecked() || chMR2.isChecked() || chLagunas2.isChecked() || ch612.isChecked()){
                                new IntentIntegrator(VendidosMensual.this).initiateScan();
                            }else{
                                Toast.makeText(context, "Selecciona alguna tienda", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(context, "Selecciona un año", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(context, "SELECCIONE UN AÑO", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnBusca2:
                    try{
                        if(anio != "*SELECCIONA UN AÑO*") {
                            if(chdengui2.isChecked() || chNopala2.isChecked() || chSD2.isChecked() || chMR2.isChecked() || chLagunas2.isChecked() || ch612.isChecked()) {
                                chDenguiV=chdengui2.isChecked();
                                chTiendaNopalaV=chNopala2.isChecked();
                                chTienda61V=ch612.isChecked();
                                chTiendaMatias=chMR2.isChecked();
                                chTLAGUNAS=chLagunas2.isChecked();
                                chTiendaSD=chSD2.isChecked();
                                final SharedPreferences sharedPreferences = getSharedPreferences("FECH", context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("anio", anio);
                                editor.apply();
                                final SharedPreferences sharedPreferences2 = getSharedPreferences("selecciones", context.MODE_PRIVATE);
                                SharedPreferences.Editor editor2 = sharedPreferences2.edit();

                                editor2.putBoolean("dengui",chDenguiV);
                                editor2.putBoolean("nopala",chTiendaNopalaV);
                                editor2.putBoolean("el61",chTienda61V);
                                editor2.putBoolean("lagunas",chTLAGUNAS);
                                editor2.putBoolean("matias",chTiendaMatias);
                                editor2.putBoolean("sd",chTiendaSD);
                                editor2.apply();
                                Intent intent = new Intent(context, ListadoProductos.class);
                                intent.putExtra("Param", "5");
                                startActivity(intent);
                                finish();
                            }
                        }else{
                            Toast.makeText(context, "SELECCIONE UN AÑO", Toast.LENGTH_SHORT).show();
                        }


                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    /**
     * Escaneo de codigo de barras
     * @param requestCode1
     * @param resultCode1
     * @param data1
     * Realiza la consulta del producto y su ubicacion
     */
    @Override
    protected void onActivityResult(int requestCode1, int resultCode1, @Nullable Intent data1) {
        super.onActivityResult(requestCode1, resultCode1, data1);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode1, resultCode1, data1);
        if (result != null)
            if (result.getContents() != null) {
                txtCod.setText("" + result.getContents());
                try{
                    new TareaProductosMensual(ene, feb, mar, abr, may, jun, jul, ago, sep, oct, nov, dec, conexion2(),
                            VendidosMensual.this).execute("idDengui","3","33","1",anio);
                    new Handler().postDelayed(new Runnable(){
                        public void run(){

                            //----------------------------
                            String idDengui = consultarIdProducto(conexion2());
                            String idLagunas = consultarIdProducto(conexionLagunas());
                            String idSD=consultarIdProducto(ConexionSD());
                            String id61=consultarIdProducto(conexion61());
                            if(chdengui2.isChecked()){
                                Consulta1(conexion2(), idDengui,"1", "3","3");
                                Consulta2(conexion2(), idDengui,"1", "3","3");
                                Consulta3(conexion2(), idDengui,"1", "3","3");
                                Consulta4(conexion2(), idDengui,"1", "3","3");
                                Consulta4(conexion2(), idDengui,"1", "3","3");
                                Consulta5(conexion2(), idDengui,"1", "3","3");
                                Consulta6(conexion2(), idDengui,"1", "3","3");
                                Consulta7(conexion2(), idDengui,"1", "3","3");
                                Consulta8(conexion2(), idDengui,"1", "3","3");
                                Consulta9(conexion2(), idDengui,"1", "3","3");
                                Consulta10(conexion2(), idDengui,"1", "3","3");
                                Consulta11(conexion2(), idDengui,"1", "3","3");
                                Consulta11(conexion2(), idDengui,"1", "3","3");

                                Consulta1(conexion2(), idDengui,"11", "3","3");
                                Consulta2(conexion2(), idDengui,"11", "3","3");
                                Consulta3(conexion2(), idDengui,"11", "3","3");
                                Consulta4(conexion2(), idDengui,"11", "3","3");
                                Consulta4(conexion2(), idDengui,"11", "3","3");
                                Consulta5(conexion2(), idDengui,"11", "3","3");
                                Consulta6(conexion2(), idDengui,"11", "3","3");
                                Consulta7(conexion2(), idDengui,"11", "3","3");
                                Consulta8(conexion2(), idDengui,"11", "3","3");
                                Consulta9(conexion2(), idDengui,"11", "3","3");
                                Consulta10(conexion2(), idDengui,"11", "3","3");
                                Consulta11(conexion2(), idDengui,"11", "3","3");
                                Consulta11(conexion2(), idDengui,"11", "3","3");

                            }
                            if(chNopala2.isChecked()){
                                Consulta1(conexion2(), idDengui,"12", "3","3");
                                Consulta2(conexion2(), idDengui,"12", "3","3");
                                Consulta3(conexion2(), idDengui,"12", "3","3");
                                Consulta4(conexion2(), idDengui,"12", "3","3");
                                Consulta4(conexion2(), idDengui,"12", "3","3");
                                Consulta5(conexion2(), idDengui,"12", "3","3");
                                Consulta6(conexion2(), idDengui,"12", "3","3");
                                Consulta7(conexion2(), idDengui,"12", "3","3");
                                Consulta8(conexion2(), idDengui,"12", "3","3");
                                Consulta9(conexion2(), idDengui,"12", "3","3");
                                Consulta10(conexion2(), idDengui,"12", "3","3");
                                Consulta11(conexion2(), idDengui,"12", "3","3");
                                Consulta11(conexion2(), idDengui,"12", "3","3");
                            }
                            if(chLagunas2.isChecked()){
                                Consulta1(conexionLagunas(), idLagunas,"4", "3","3");
                                Consulta2(conexionLagunas(), idLagunas,"4", "3","3");
                                Consulta3(conexionLagunas(), idLagunas,"4", "3","3");
                                Consulta4(conexionLagunas(), idLagunas,"4", "3","3");
                                Consulta4(conexionLagunas(), idLagunas,"4", "3","3");
                                Consulta5(conexionLagunas(), idLagunas,"4", "3","3");
                                Consulta6(conexionLagunas(), idLagunas,"4", "3","3");
                                Consulta7(conexionLagunas(), idLagunas,"4", "3","3");
                                Consulta8(conexionLagunas(), idLagunas,"4", "3","3");
                                Consulta9(conexionLagunas(), idLagunas,"4", "3","3");
                                Consulta10(conexionLagunas(), idLagunas,"4", "3","3");
                                Consulta11(conexionLagunas(), idLagunas,"4", "3","3");
                                Consulta11(conexionLagunas(), idLagunas,"4", "3","3");
                            }
                            if(chMR2.isChecked()){
                                Consulta1(conexionLagunas(), idLagunas,"10", "3","3");
                                Consulta2(conexionLagunas(), idLagunas,"10", "3","3");
                                Consulta3(conexionLagunas(), idLagunas,"10", "3","3");
                                Consulta4(conexionLagunas(), idLagunas,"10", "3","3");
                                Consulta4(conexionLagunas(), idLagunas,"10", "3","3");
                                Consulta5(conexionLagunas(), idLagunas,"10", "3","3");
                                Consulta6(conexionLagunas(), idLagunas,"10", "3","3");
                                Consulta7(conexionLagunas(), idLagunas,"10", "3","3");
                                Consulta8(conexionLagunas(), idLagunas,"10", "3","3");
                                Consulta9(conexionLagunas(), idLagunas,"10", "3","3");
                                Consulta10(conexionLagunas(), idLagunas,"10", "3","3");
                                Consulta11(conexionLagunas(), idLagunas,"10", "3","3");
                                Consulta11(conexionLagunas(), idLagunas,"10", "3","3");

                            }
                            if(ch612.isChecked()){
                                Consulta1(conexion61(), id61,"1", "3","3");
                                Consulta2(conexion61(), id61,"1", "3","3");
                                Consulta3(conexion61(), id61,"1", "3","3");
                                Consulta4(conexion61(), id61,"1", "3","3");
                                Consulta4(conexion61(), id61,"1", "3","3");
                                Consulta5(conexion61(), id61,"1", "3","3");
                                Consulta6(conexion61(), id61,"1", "3","3");
                                Consulta7(conexion61(), id61,"1", "3","3");
                                Consulta8(conexion61(), id61,"1", "3","3");
                                Consulta9(conexion61(), id61,"1", "3","3");
                                Consulta10(conexion61(), id61,"1", "3","3");
                                Consulta11(conexion61(), id61,"1", "3","3");
                                Consulta11(conexion61(), id61,"1", "3","3");

                            }
                            if (chSD2.isChecked()){
                                Consulta1(ConexionSD(), idSD,"4", "3","3");
                                Consulta2(ConexionSD(), idSD,"4", "3","3");
                                Consulta3(ConexionSD(), idSD,"4", "3","3");
                                Consulta4(ConexionSD(), idSD,"4", "3","3");
                                Consulta4(ConexionSD(), idSD,"4", "3","3");
                                Consulta5(ConexionSD(), idSD,"4", "3","3");
                                Consulta6(ConexionSD(), idSD,"4", "3","3");
                                Consulta7(ConexionSD(), idSD,"4", "3","3");
                                Consulta8(ConexionSD(), idSD,"4", "3","3");
                                Consulta9(ConexionSD(), idSD,"4", "3","3");
                                Consulta10(ConexionSD(), idSD,"4", "3","3");
                                Consulta11(ConexionSD(), idSD,"4", "3","3");
                                Consulta11(ConexionSD(), idSD,"4", "3","3");
                            }
                            Datasets();
                            Promedio();
                            progressBar5.setVisibility(GONE);
                            CustomGraphic();
                            //----------------------------

                        }
                    }, 2000); //5000 millisegundos = 5 segundos.


                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

    }

    /**
     * Este metodo recupera el año seleccionado antes de buscar el producto.
     */
    public void ObtenerSharePreferences(){
        try {
            buscard("1");
            buscard("2");
            buscard("3");
            buscard("4");
            correccion();
            SharedPreferences sharedPreferences = this.getSharedPreferences("FECH", this.MODE_PRIVATE);
            anio = sharedPreferences.getString("anio", "null");
            SharedPreferences sharedPreferences2 = this.getSharedPreferences("selecciones", this.MODE_PRIVATE);
            chdengui2.setChecked(sharedPreferences2.getBoolean("dengui", false));
            chNopala2.setChecked(sharedPreferences2.getBoolean("nopala", false));
            ch612.setChecked(sharedPreferences2.getBoolean("el61", false));
            chLagunas2.setChecked(sharedPreferences2.getBoolean("lagunas", false));
            chMR2.setChecked(sharedPreferences2.getBoolean("matias", false));
            chSD2.setChecked(sharedPreferences2.getBoolean("sd", false));

        }catch (Exception e){
            String message=e.getMessage();
        }
    }

    /**
     * Este metodo recupera la informacion del producto y realiza las consultas para obtener
     * la informacion de ventas del producto.
     */
    public void Inicializar() {
        dataSet.clear();
        ModeloProductos model = (ModeloProductos) getIntent().getSerializableExtra("Elemento");
        if (model != null) {
            try {
                txtCod.setText(model.getCodProducto());
                new TareaProductosMensual(ene, feb, mar, abr, may, jun, jul, ago, sep, oct, nov, dec, conexion2(),
                        VendidosMensual.this).execute("idDengui","3","33","1",anio);
                new Handler().postDelayed(new Runnable(){
                    public void run(){

                        //----------------------------
                        String idDengui = consultarIdProducto(conexion2());
                        String idLagunas = consultarIdProducto(conexionLagunas());
                        String idSD=consultarIdProducto(ConexionSD());
                        String id61=consultarIdProducto(conexion61());
                        if(chdengui2.isChecked()){
                            Consulta1(conexion2(), idDengui,"1", "3","3");
                            Consulta2(conexion2(), idDengui,"1", "3","3");
                            Consulta3(conexion2(), idDengui,"1", "3","3");
                            Consulta4(conexion2(), idDengui,"1", "3","3");
                            Consulta4(conexion2(), idDengui,"1", "3","3");
                            Consulta5(conexion2(), idDengui,"1", "3","3");
                            Consulta6(conexion2(), idDengui,"1", "3","3");
                            Consulta7(conexion2(), idDengui,"1", "3","3");
                            Consulta8(conexion2(), idDengui,"1", "3","3");
                            Consulta9(conexion2(), idDengui,"1", "3","3");
                            Consulta10(conexion2(), idDengui,"1", "3","3");
                            Consulta11(conexion2(), idDengui,"1", "3","3");
                            Consulta11(conexion2(), idDengui,"1", "3","3");

                        }
                        if(chNopala2.isChecked()){
                            Consulta1(conexion2(), idDengui,"12", "3","3");
                            Consulta2(conexion2(), idDengui,"12", "3","3");
                            Consulta3(conexion2(), idDengui,"12", "3","3");
                            Consulta4(conexion2(), idDengui,"12", "3","3");
                            Consulta4(conexion2(), idDengui,"12", "3","3");
                            Consulta5(conexion2(), idDengui,"12", "3","3");
                            Consulta6(conexion2(), idDengui,"12", "3","3");
                            Consulta7(conexion2(), idDengui,"12", "3","3");
                            Consulta8(conexion2(), idDengui,"12", "3","3");
                            Consulta9(conexion2(), idDengui,"12", "3","3");
                            Consulta10(conexion2(), idDengui,"12", "3","3");
                            Consulta11(conexion2(), idDengui,"12", "3","3");
                            Consulta11(conexion2(), idDengui,"12", "3","3");
                        }
                        if(chLagunas2.isChecked()){
                            Consulta1(conexionLagunas(), idLagunas,"4", "3","3");
                            Consulta2(conexionLagunas(), idLagunas,"4", "3","3");
                            Consulta3(conexionLagunas(), idLagunas,"4", "3","3");
                            Consulta4(conexionLagunas(), idLagunas,"4", "3","3");
                            Consulta4(conexionLagunas(), idLagunas,"4", "3","3");
                            Consulta5(conexionLagunas(), idLagunas,"4", "3","3");
                            Consulta6(conexionLagunas(), idLagunas,"4", "3","3");
                            Consulta7(conexionLagunas(), idLagunas,"4", "3","3");
                            Consulta8(conexionLagunas(), idLagunas,"4", "3","3");
                            Consulta9(conexionLagunas(), idLagunas,"4", "3","3");
                            Consulta10(conexionLagunas(), idLagunas,"4", "3","3");
                            Consulta11(conexionLagunas(), idLagunas,"4", "3","3");
                            Consulta11(conexionLagunas(), idLagunas,"4", "3","3");
                        }
                        if(chMR2.isChecked()){
                            Consulta1(conexionLagunas(), idLagunas,"10", "3","3");
                            Consulta2(conexionLagunas(), idLagunas,"10", "3","3");
                            Consulta3(conexionLagunas(), idLagunas,"10", "3","3");
                            Consulta4(conexionLagunas(), idLagunas,"10", "3","3");
                            Consulta4(conexionLagunas(), idLagunas,"10", "3","3");
                            Consulta5(conexionLagunas(), idLagunas,"10", "3","3");
                            Consulta6(conexionLagunas(), idLagunas,"10", "3","3");
                            Consulta7(conexionLagunas(), idLagunas,"10", "3","3");
                            Consulta8(conexionLagunas(), idLagunas,"10", "3","3");
                            Consulta9(conexionLagunas(), idLagunas,"10", "3","3");
                            Consulta10(conexionLagunas(), idLagunas,"10", "3","3");
                            Consulta11(conexionLagunas(), idLagunas,"10", "3","3");
                            Consulta11(conexionLagunas(), idLagunas,"10", "3","3");

                        }
                        if(ch612.isChecked()){
                            Consulta1(conexion61(), id61,"1", "3","3");
                            Consulta2(conexion61(), id61,"1", "3","3");
                            Consulta3(conexion61(), id61,"1", "3","3");
                            Consulta4(conexion61(), id61,"1", "3","3");
                            Consulta4(conexion61(), id61,"1", "3","3");
                            Consulta5(conexion61(), id61,"1", "3","3");
                            Consulta6(conexion61(), id61,"1", "3","3");
                            Consulta7(conexion61(), id61,"1", "3","3");
                            Consulta8(conexion61(), id61,"1", "3","3");
                            Consulta9(conexion61(), id61,"1", "3","3");
                            Consulta10(conexion61(), id61,"1", "3","3");
                            Consulta11(conexion61(), id61,"1", "3","3");
                            Consulta11(conexion61(), id61,"1", "3","3");

                        }
                        if (chSD2.isChecked()){
                            Consulta1(ConexionSD(), idSD,"4", "3","3");
                            Consulta2(ConexionSD(), idSD,"4", "3","3");
                            Consulta3(ConexionSD(), idSD,"4", "3","3");
                            Consulta4(ConexionSD(), idSD,"4", "3","3");
                            Consulta4(ConexionSD(), idSD,"4", "3","3");
                            Consulta5(ConexionSD(), idSD,"4", "3","3");
                            Consulta6(ConexionSD(), idSD,"4", "3","3");
                            Consulta7(ConexionSD(), idSD,"4", "3","3");
                            Consulta8(ConexionSD(), idSD,"4", "3","3");
                            Consulta9(ConexionSD(), idSD,"4", "3","3");
                            Consulta10(ConexionSD(), idSD,"4", "3","3");
                            Consulta11(ConexionSD(), idSD,"4", "3","3");
                            Consulta11(ConexionSD(), idSD,"4", "3","3");
                        }
                        Datasets();
                        Promedio();
                        progressBar5.setVisibility(GONE);
                        CustomGraphic();
                        //----------------------------

                    }
                }, 2000); //5000 millisegundos = 5 segundos.

            } catch (Exception e) {

            }
        }
    }

    public void Datasets(){
        dataSet.add(new Entry(1, ene));
        dataSet.add(new Entry(2, feb));
        dataSet.add(new Entry(3, mar));
        dataSet.add(new Entry(4, abr));
        dataSet.add(new Entry(5, may));
        dataSet.add(new Entry(6, jun));
        dataSet.add(new Entry(7, jul));
        dataSet.add(new Entry(8, ago));
        dataSet.add(new Entry(9, sep));
        dataSet.add(new Entry(10, oct));
        dataSet.add(new Entry(11, nov));
        dataSet.add(new Entry(12, dec));

    }

    /**
     * Este metodo realiza la creacion de productos
     */
    public void CustomGraphic(){
        try {
            LineData lineData;
            LineDataSet lineDataSetDengui;
            // AJUSTE ESQUEMA DENGUI
            lineDataSetDengui = new LineDataSet(dataSet, "Almacenes");
            lineDataSetDengui.setColors(Color.RED);
            lineDataSetDengui.setValueTextColor(Color.BLACK);
            lineDataSetDengui.setCircleRadius(5f);
            lineDataSetDengui.setLineWidth(5);
            lineDataSetDengui.setValueTextSize(12f);
            lineData = new LineData(lineDataSetDengui);
            lineChart.setData(lineData);
            lineChart.invalidate();
            lineChart.animateY(1400, Easing.EaseInOutElastic);
            lineChart.animate();

            Legend l = lineChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setEnabled(true);
            l.setTextSize(12f);


            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(meses));

        }catch (Exception e){

        }
    }

    public void lista(){
        meses.add("Meses");
        meses.add("Enero");
        meses.add("Febrero");
        meses.add("Marzo");
        meses.add("Abril");
        meses.add("Mayo");
        meses.add("Junio");
        meses.add("Julio");
        meses.add("Agosto");
        meses.add("Septiembre");
        meses.add("Octubre");
        meses.add("Noviembre");
        meses.add("Diciembre");
    }

    public void Promedio() {
        try {
            Fecha();
            txtpromedio.setVisibility(View.VISIBLE);
            float promedio = (ene + feb + mar + abr + may + jun + jul + ago + sep + oct + nov + dec) / Integer.parseInt(mesfech);
            txtpromedio.setText("Promedio: " + promedio);
        }catch (Exception e){

        }
    }



    public void correccion(){
        if(IP61 == null || IP61.equals("")){
            IP61=IP;
            PUERTO61=PUERTO;
            USUARIO61=USUARIO;
            CONTRASEÑA61=CONTRASEÑA;
            BD61="adCASACRU61";
        }
    }
    /**
     * obtiene los valores de la fecha por separado (dia, mes, año)
     */
    public void Fecha() {
        try {
            SimpleDateFormat dtf=new SimpleDateFormat("yyyy/MM/dd");
            Calendar calendar=Calendar.getInstance();
            Date date=calendar.getTime();
            String fetch=dtf.format(date);


            Date dte=new SimpleDateFormat("yyyy/MM/dd").parse(fetch);


            calendar.setTime(dte);
            aniofech = String.valueOf(calendar.get(Calendar.YEAR));
            int month = (calendar.get(Calendar.MONTH))+1;
            mesfech=String.valueOf(month);

        }catch (Exception ex){

        }
    }

    @Override
    public void Progressbar(boolean b) {
    if(b){
        progressBar5.setVisibility(View.VISIBLE);
    }else{
        progressBar5.setVisibility(GONE);
    }
    }

    @Override
    public void RecibirMeses(float e, float f, float m, float a, float ma, float j, float ju, float ag, float s, float o, float n, float d) {
        ene=e;
        feb=f;
        mar=m;
        abr=a;
        mar=ma;
        jun=j;
        jul=ju;
        ago=ag;
        sep=s;
        oct=o;
        nov=n;
        dec=d;

    }

    // Este metodo realiza la consulta de la cantidad vendida del producto
    // escaneado o buscado en el mes de enero
    @Override
    public void Consulta1(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2) {
        try {
            Statement stm = con.createStatement();
            String Consulta="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='1/1/"+anio+"' and CFECHA<='1/31/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs = stm.executeQuery(Consulta);
            while (rs.next()) {
                ene+=Double.parseDouble(rs.getString(1));
            }


        } catch (SQLException e) {
            String mensaje=e.getMessage();
        }

    }

    // Este metodo realiza la consulta de la cantidad vendida del producto
    // escaneado o buscado en el mes de febrero
    @Override
    public void Consulta2(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2) {
        try {
            Statement stm = con.createStatement();
            String Consulta="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='2/1/"+anio+"' and CFECHA<='2/28/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs = stm.executeQuery(Consulta);
            while (rs.next()) {
                feb+=Double.parseDouble(rs.getString(1));
            }

        } catch (SQLException e) {
            String mensaje=e.getMessage();
        }
    }

    // Este metodo realiza la consulta de la cantidad vendida del producto
    // escaneado o buscado en el mes de marzo
    @Override
    public void Consulta3(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2) {

        try {
            Statement stm = con.createStatement();
            String Consulta="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='3/1/"+anio+"' and CFECHA<='3/31/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs = stm.executeQuery(Consulta);
            while (rs.next()) {
                mar+=Double.parseDouble(rs.getString(1));
            }

        } catch (SQLException e) {
            String mensaje = e.getMessage();
        }
    }

    // Este metodo realiza la consulta de la cantidad vendida del producto
    // escaneado o buscado en el mes de abril
    @Override
    public void Consulta4(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2) {

        try {
            Statement stm = con.createStatement();
            String Consulta="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='4/1/"+anio+"' and CFECHA<='4/30/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs = stm.executeQuery(Consulta);
            while (rs.next()) {
                abr+=Double.parseDouble(rs.getString(1));
            }

        } catch (SQLException e) {
            String mensaje=e.getMessage();
        }

    }

    // Este metodo realiza la consulta de la cantidad vendida del producto
    // escaneado o buscado en el mes de mayo
    @Override
    public void Consulta5(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2) {
        try {
            Statement stm = con.createStatement();
            String Consulta="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='5/1/"+anio+"' and CFECHA<='5/31/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs = stm.executeQuery(Consulta);
            while (rs.next()) {
                may+=Double.parseDouble(rs.getString(1));
            }

        } catch (SQLException e) {
            String mensaje=e.getMessage();
        }

    }

    // Este metodo realiza la consulta de la cantidad vendida del producto
    // escaneado o buscado en el mes de junio
    @Override
    public void Consulta6(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2) {
        try {
            Statement stm = con.createStatement();
            String Consulta="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='6/1/"+anio+"' and CFECHA<='6/30/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs = stm.executeQuery(Consulta);
            while (rs.next()) {
                jun+=Double.parseDouble(rs.getString(1));
            }

        } catch (SQLException e) {
            String mensaje=e.getMessage();
        }
    }

    // Este metodo realiza la consulta de la cantidad vendida del producto
    // escaneado o buscado en el mes de julio
    @Override
    public void Consulta7(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2) {
        try {
            Statement stm = con.createStatement();
            String Consulta="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='7/1/"+anio+"' and CFECHA<='7/31/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs = stm.executeQuery(Consulta);
            while (rs.next()) {
                jul+=Double.parseDouble(rs.getString(1));
            }

        } catch (SQLException e) {
            String mensaje=e.getMessage();
        }

    }

    // Este metodo realiza la consulta de la cantidad vendida del producto
    // escaneado o buscado en el mes de agosto
    @Override
    public void Consulta8(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2) {

        try {
            Statement stm = con.createStatement();
            String Consulta="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='8/1/"+anio+"' and CFECHA<='8/31/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs = stm.executeQuery(Consulta);
            while (rs.next()) {
                ago+=Double.parseDouble(rs.getString(1));
            }

        } catch (SQLException e) {
            String mensaje=e.getMessage();
        }

    }

    // Este metodo realiza la consulta de la cantidad vendida del producto
    // escaneado o buscado en el mes de septiembre
    @Override
    public void Consulta9(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2) {
        try {
            Statement stm = con.createStatement();
            String Consulta="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='9/1/"+anio+"' and CFECHA<='9/30/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs = stm.executeQuery(Consulta);
            while (rs.next()) {
                sep+=Double.parseDouble(rs.getString(1));
            }

        } catch (SQLException e) {
            String mensaje=e.getMessage();
        }
    }

    // Este metodo realiza la consulta de la cantidad vendida del producto
    // escaneado o buscado en el mes de octubre
    @Override
    public void Consulta10(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2) {
        try {
            Statement stm = con.createStatement();
            String Consulta="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='10/1/"+anio+"' and CFECHA<='10/31/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs = stm.executeQuery(Consulta);
            while (rs.next()) {
                oct+=Double.parseDouble(rs.getString(1));
            }

        } catch (SQLException e) {
            String mensaje=e.getMessage();
        }

    }

    // Este metodo realiza la consulta de la cantidad vendida del producto
    // escaneado o buscado en el mes de noviembre
    @Override
    public void Consulta11(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2) {

        try {
            Statement stm = con.createStatement();
            String Consulta="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='11/1/"+anio+"' and CFECHA<='11/30/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs = stm.executeQuery(Consulta);
            while (rs.next()) {
                nov+=Double.parseDouble(rs.getString(1));
            }

        } catch (SQLException e) {
            String mensaje=e.getMessage();
        }
    }

    // Este metodo realiza la consulta de la cantidad vendida del producto
    // escaneado o buscado en el mes de Diciembre
    @Override
    public void Consulta12(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2) {

        try {
            Statement stm = con.createStatement();
            String Consulta="SELECT CUNIDADESCAPTURADAS, CFECHA " +
                    "FROM admMovimientos " +
                    "WHERE CIDPRODUCTO='"+IdProducto+"' AND (CFECHA>='12/1/"+anio+"' and CFECHA<='12/31/"+anio+"') " +
                    "AND (CIDDOCUMENTODE='"+Doc1+"' OR CIDDOCUMENTODE='"+Doc2+"') and CIDALMACEN='"+idAlma+"' " +
                    "AND CIDDOCUMENTO!='0'";
            ResultSet rs = stm.executeQuery(Consulta);
            while (rs.next()) {
                dec+=Double.parseDouble(rs.getString(1));
            }

        } catch (SQLException e) {
            String mensaje=e.getMessage();
        }
    }


}
