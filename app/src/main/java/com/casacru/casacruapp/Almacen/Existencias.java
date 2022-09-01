package com.casacru.casacruapp.Almacen;

import static android.view.View.GONE;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.casacru.casacruapp.Compras.SolicitudDeCompra;
import com.casacru.casacruapp.Dialog.DialogError;
import com.casacru.casacruapp.General.ListadoProductos;
import com.casacru.casacruapp.Modelo.ModeloProductos;
import com.casacru.casacruapp.R;
import com.casacru.casacruapp.SQLite.DatabaseHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.bouncycastle.pqc.crypto.xmss.BDS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Existencias extends AppCompatActivity {
    private String IP, PUERTO, USUARIO, CONTRASEÑA, BD, idAlma;
    private String mes,anio,IdProducto, IdProducto61,IdProductoLagunas,IdProductoSD,IdProductoMatR;
    private TextView txtAlm1, txtAlm8, txtAlm9,txtAlm2,txtAlm3,txtAlm4,txtmatR,txtcodpro,CnomProd;
    private Button btnescan,btnbuscar;
    private TableLayout tblExistencias;
    private Context context;
    private String IP61, PUERTO61, USUARIO61, CONTRASEÑA61, BD61;
    private String IPLagunas, PUERTOLagunas, USUARIOLagunas, CONTRASEÑALagunas, BDLagunas, idAlmaLagunas;
    private String IPSD, PUERTOSD, USUARIOSD, CONTRASEÑASD, BDSD, idAlmaSD;
    private ProgressBar progressBar6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existencias);
        context = this;
        txtAlm1 = (TextView) findViewById(R.id.textViewAlm1);
        txtAlm8 = (TextView) findViewById(R.id.textViewAlm8);
        txtAlm9 = (TextView) findViewById(R.id.textViewAlm9);
        txtAlm2 = (TextView) findViewById(R.id.textViewAlm2);
        txtAlm3 = (TextView) findViewById(R.id.textViewAlm3);
        txtAlm4 = (TextView) findViewById(R.id.textViewAlm4);
        txtmatR=(TextView) findViewById(R.id.textViewAlmMatR);
        CnomProd =(TextView) findViewById(R.id.textNomPro);
        txtcodpro = (TextView) findViewById(R.id.txtCod);
        btnescan = (Button) findViewById(R.id.btnEscanear);
        btnescan.setOnClickListener(nOnClickListener);
        btnbuscar = (Button) findViewById(R.id.btnBusca2);
        btnbuscar.setOnClickListener(nOnClickListener);
        tblExistencias = (TableLayout) findViewById(R.id.table);
        progressBar6=(ProgressBar) findViewById(R.id.progressBar6);
        try {

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            buscard();
            buscarLagunas();
            buscarSD();
            buscarel61();
            Fecha();
            Recuperar();
        }catch (Exception ex){
            Recuperar();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu5, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.item2:
                Intent i=new Intent(this, SolicitudDeCompra.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode1, int resultCode1, @Nullable Intent data1) {
        super.onActivityResult(requestCode1, resultCode1, data1);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode1, resultCode1, data1);
        if (result != null)
            if (result.getContents() != null) {
                txtcodpro.setText("" + result.getContents());
                try{
                    if(tblExistencias.getVisibility()==View.GONE){
                        tblExistencias.setVisibility(View.VISIBLE);
                    }
                    progressBar6.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable(){
                        public void run(){

                            //----------------------------
                            consultarIdProducto();
                            consultarIdProducto61();
                            consultarIdProductoLagunas();
                            consultarIdProductoSD();
                            existenciaAlmacenMatR();
                            existenciaAlmacen1();
                            existenciaAlmacen2();
                            existenciaAlmacen8();
                            existenciaAlmacen9();
                            existenciaAlmacen3();
                            existenciaAlmacen4();
                            progressBar6.setVisibility(GONE);
                            //----------------------------

                        }
                    }, 2000); //5000 millisegundos = 5 segundos.

                }catch (Exception e){
                    new DialogError(context,e.getMessage());
                }

            }

    }

    private View.OnClickListener nOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnEscanear:
                    new IntentIntegrator(Existencias.this).initiateScan();
                    Limpiar();
                    if(tblExistencias.getVisibility()==View.VISIBLE){
                        tblExistencias.setVisibility(View.GONE);
                    }
                    break;
                case R.id.btnBusca2:
                    try{
                        Intent intent=new Intent(context, ListadoProductos.class);
                        intent.putExtra("Param","2");
                        startActivity(intent);
                            finish();

                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            }
        }
    };

    /**
     * Consultar el id y nombre del producto escaneado del almacen dengui, tienda dengui, tienda nopala
     */
    public void consultarIdProducto(){

        try{
            try {
                Statement stm = conexion2().createStatement();
                ResultSet rs = stm.executeQuery("SELECT CIDPRODUCTO, CNOMBREPRODUCTO FROM admProductos where CCODIGOPRODUCTO='" + txtcodpro.getText().toString() + "'");

                if (rs.next()) {
                    IdProducto = rs.getString(1);
                    CnomProd.setText(rs.getString(2));
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "No existe en Dengui", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception ex){

        }
        }

    /**
     * Consultar Id del producto del almacen el 61
     */
    public void consultarIdProducto61(){
        try {
            try {
                Statement stm = conexion61().createStatement();
                ResultSet rs = stm.executeQuery("SELECT CIDPRODUCTO FROM admProductos where CCODIGOPRODUCTO='" + txtcodpro.getText().toString() + "'");

                if (rs.next()) {
                    IdProducto61 = rs.getString(1);

                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "No existe en el 61", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){

        }
    }

    /**
     * Consultar el id del producto del almacen lagunas
     */
    public void consultarIdProductoLagunas(){

            try {
                Statement stm = conexionLagunas().createStatement();
                ResultSet rs = stm.executeQuery("SELECT CIDPRODUCTO FROM admProductos where CCODIGOPRODUCTO='" + txtcodpro.getText().toString() + "'");

                if (rs.next()) {
                    IdProductoLagunas = rs.getString(1);
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "No existe en Lagunas", Toast.LENGTH_SHORT).show();
            }
    }

    /**
     * Consultar el id del producto en el almacen de Santo domingo
     */
    public void consultarIdProductoSD(){

        try {
            Statement stm = ConexionSD().createStatement();
            ResultSet rs = stm.executeQuery("SELECT CIDPRODUCTO FROM admProductos where CCODIGOPRODUCTO='" + txtcodpro.getText().toString() + "'");

            if (rs.next()) {
                IdProductoSD = rs.getString(1);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "No existe en Santo domingo", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     *Busca los parametros de conexion el la base de datos SQLite del aplicativo
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
                Toast.makeText(this, "No existen parametros de dengui", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "No existen parametros de lagunas", Toast.LENGTH_SHORT).show();
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
            conexion2 = DriverManager.getConnection("jdbc:jtds:sqlserver://" + this.IPSD + ":" + this.PUERTOSD + ";databaseName="+ BDSD +";user="+this.USUARIOSD+";password=" + this.CONTRASEÑASD + ";");
        } catch (Exception e) {
            }
        return conexion2;
    }

    /**
     * Calcula la existencia del producto en el almacen dengui dependiendo
     * de los valores consultados en la base de datos
     */
    public void existenciaAlmacen1(){
try {
    String entradasp="",salidasp="",ejercicio="";
    ejercicio=getEjercicio();
    Statement stm = conexion2().createStatement();
    ResultSet rs = stm.executeQuery("SELECT CENTRADASPERIODO"+mes+", CSALIDASPERIODO"+mes+" from admExistenciaCosto " +
            "WHERE CIDEJERCICIO='"+ejercicio+"' and CIDPRODUCTO='"+IdProducto+"' and CIDALMACEN='1'");
    if (rs.next()){

        entradasp=rs.getString(1);
        salidasp=rs.getString(2);
    }else{
        entradasp="0.0";
        salidasp="0.0";
    }
    float entradas=Float.parseFloat(entradasp);
    float salidas=Float.parseFloat(salidasp);
    float total= (entradas)-(salidas);
    String Existencia=String.valueOf(total);
    txtAlm1.setText(Existencia);

}catch(Exception ex){

    txtAlm1.setText("No existe");
}
    }

    /**
     * Calcula la existencia del producto en el Tienda dengui dependiendo
     * de los valores consultados en la base de datos
     */
    public void existenciaAlmacen8(){
        try {

            String entradasp="",salidasp="",ejercicio="";
            ejercicio=getEjercicio();

            Statement stm = conexion2().createStatement();
            ResultSet rs = stm.executeQuery("SELECT CENTRADASPERIODO"+mes+", CSALIDASPERIODO"+mes+" from admExistenciaCosto " +
                    "WHERE CIDEJERCICIO='"+ejercicio+"' and CIDPRODUCTO='"+IdProducto+"' and CIDALMACEN='11'");

            if (rs.next()){

                entradasp=rs.getString(1);
                salidasp=rs.getString(2);
            }else{
                entradasp="0.0";
                salidasp="0.0";
            }
            float entradas=Float.parseFloat(entradasp);
            float salidas=Float.parseFloat(salidasp);
            float total= (entradas)-(salidas);
            String Existencia=String.valueOf(total);

            txtAlm8.setText(Existencia);
        }catch(Exception ex){
            txtAlm8.setText("No existe");
            //Toast.makeText(this, "No existe", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Calcula la existencia del producto en tienda nopala dependiendo
     * de los valores consultados en la base de datos
     */
    public void existenciaAlmacen9(){
        try {

            String entradasp="",salidasp="",ejercicio="";
            ejercicio=getEjercicio();
            Statement stm = conexion2().createStatement();
            ResultSet rs = stm.executeQuery("SELECT CENTRADASPERIODO"+mes+", CSALIDASPERIODO"+mes+" from admExistenciaCosto " +
                    "WHERE CIDEJERCICIO='"+ejercicio+"' and CIDPRODUCTO='"+IdProducto+"' and CIDALMACEN='12'");

            if (rs.next()){

                entradasp=rs.getString(1);
                salidasp=rs.getString(2);
            }else{
                entradasp="0.0";
                salidasp="0.0";
            }
            float entradas=Float.parseFloat(entradasp);
            float salidas=Float.parseFloat(salidasp);
            float total= (entradas)-(salidas);
            String Existencia=String.valueOf(total);

            txtAlm9.setText(Existencia);
        }catch(Exception ex){

            txtAlm9.setText("No existe");
        }
    }

    /**
     * Calcula la existencia del producto en tienda el 61 dependiendo
     * de los valores consultados en la base de datos
     */
    public void existenciaAlmacen2(){
        try {
            String entradasp="",salidasp="",ejercicio="";
            ejercicio=getEjercicio61();
            Statement stm = conexion61().createStatement();
            ResultSet rs = stm.executeQuery("SELECT CENTRADASPERIODO"+mes+", CSALIDASPERIODO"+mes+" from admExistenciaCosto " +
                    "WHERE CIDEJERCICIO='"+ejercicio+"' and CIDPRODUCTO='"+IdProducto61+"' and CIDALMACEN='1'");
            if (rs.next()){

                entradasp=rs.getString(1);
                salidasp=rs.getString(2);
            }else{
                entradasp="0.0";
                salidasp="0.0";
            }
            float entradas=Float.parseFloat(entradasp);
            float salidas=Float.parseFloat(salidasp);
            float total= (entradas)-(salidas);
            String Existencia=String.valueOf(total);
            txtAlm2.setText(Existencia);

        }catch(Exception ex){

            txtAlm2.setText("No existe");        }
    }

    /**
     * Calcula la existencia del producto en Lagunas dependiendo
     * de los valores consultados en la base de datos
     */
    public void existenciaAlmacen3(){
        try {

            String entradasp="",salidasp="",ejercicio="";
            ejercicio=getEjercicioLagunas();

            Statement stm = conexionLagunas().createStatement();
            ResultSet rs = stm.executeQuery("SELECT CENTRADASPERIODO"+mes+", CSALIDASPERIODO"+mes+" from admExistenciaCosto " +
                    "WHERE CIDEJERCICIO='"+ejercicio+"' and CIDPRODUCTO='"+IdProductoLagunas+"' and CIDALMACEN='4'");

            if (rs.next()){

                entradasp=rs.getString(1);
                salidasp=rs.getString(2);
            }else{
                entradasp="0.0";
                salidasp="0.0";
            }
            float entradas=Float.parseFloat(entradasp);
            float salidas=Float.parseFloat(salidasp);
            float total= (entradas)-(salidas);
            String Existencia=String.valueOf(total);

            txtAlm3.setText(Existencia);
        }catch(Exception ex){

            txtAlm3.setText("No existe");        }
    }

    /**
     * Calcula la existencia del producto en Lagunas dependiendo
     * de los valores consultados en la base de datos
     */
    public void existenciaAlmacenMatR(){
        try {

            String entradasp="",salidasp="",ejercicio="";
            ejercicio=getEjercicioLagunas();

            Statement stm = conexionLagunas().createStatement();
            ResultSet rs = stm.executeQuery("SELECT CENTRADASPERIODO"+mes+", CSALIDASPERIODO"+mes+" from admExistenciaCosto " +
                    "WHERE CIDEJERCICIO='"+ejercicio+"' and CIDPRODUCTO='"+IdProductoLagunas+"' and CIDALMACEN='10'");

            if (rs.next()){

                entradasp=rs.getString(1);
                salidasp=rs.getString(2);
            }else{
                entradasp="0.0";
                salidasp="0.0";
            }
            float entradas=Float.parseFloat(entradasp);
            float salidas=Float.parseFloat(salidasp);
            float total= (entradas)-(salidas);
            String Existencia=String.valueOf(total);

            txtmatR.setText(Existencia);
        }catch(Exception ex){

            txtmatR.setText("No existe");        }
    }

    /**
     * Calcula la existencia del producto en santo domingo dependiendo
     * de los valores consultados en la base de datos
     */
    public void existenciaAlmacen4(){
        try {

            String entradasp="",salidasp="",ejercicio="";
            ejercicio=getEjercicioSantoDomingo();
            Statement stm = ConexionSD().createStatement();
            ResultSet rs = stm.executeQuery("SELECT CENTRADASPERIODO"+mes+", CSALIDASPERIODO"+mes+" from admExistenciaCosto " +
                    "WHERE CIDEJERCICIO='"+ejercicio+"' and CIDPRODUCTO='"+IdProductoSD+"' and CIDALMACEN='4'");

            if (rs.next()){

                entradasp=rs.getString(1);
                salidasp=rs.getString(2);
            }else{
                entradasp="0.0";
                salidasp="0.0";
            }
            float entradas=Float.parseFloat(entradasp);
            float salidas=Float.parseFloat(salidasp);
            float total= (entradas)-(salidas);
            String Existencia=String.valueOf(total);

            txtAlm4.setText(Existencia);
        }catch(Exception ex){
            txtAlm4.setText("No existe");
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

            anio = String.valueOf(calendar.get(Calendar.YEAR));
            int month = (calendar.get(Calendar.MONTH))+1;
            mes=String.valueOf(month);

        }catch (Exception ex){

        }
    }

    /**
     * Consulta el ejercicio al que corresponde la fecha actual
     * @return
     */
    public String getEjercicio(){
       String ejercicio="";

       try {
            Statement stm = conexion2().createStatement();
            ResultSet rs = stm.executeQuery("SELECT CIDEJERCICIO FROM admEjercicios where CEJERCICIO='" + anio + "'");

            if (rs.next()) {
                ejercicio=rs.getString(1);
            }
            return ejercicio;
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return ejercicio;
    }

    /**
     * Consulta el ejercicio al que corresponde la fecha actual
     * @return
     */
    public String getEjercicioLagunas(){
        String ejercicio="";
            try {
                Statement stm = conexionLagunas().createStatement();
                ResultSet rs = stm.executeQuery("SELECT CIDEJERCICIO FROM admEjercicios where CEJERCICIO='" + anio + "'");

                if (rs.next()) {
                    ejercicio = rs.getString(1);
                } else {
                    Statement stm1 = conexion2().createStatement();
                    ResultSet rs1 = stm1.executeQuery("SELECT CIDEJERCICIO FROM admEjercicios where CEJERCICIO='" + anio + "'");
                    if (rs1.next()) {
                        ejercicio = rs1.getString(1);
                    }
                }
                return ejercicio;
            } catch (Exception e) {
            //    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        return ejercicio;
    }

    /**
     * Consulta el ejercicio al que corresponde la fecha actual
     * @return
     */
    public String getEjercicioSantoDomingo(){
        String ejercicio="";
        try {
            Statement stm = ConexionSD().createStatement();
            ResultSet rs = stm.executeQuery("SELECT CIDEJERCICIO FROM admEjercicios where CEJERCICIO='" + anio + "'");

            if (rs.next()) {
                ejercicio=rs.getString(1);
            }
            return ejercicio;
        } catch (Exception e) {
           // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return ejercicio;
    }

    /**
     * Consulta el ejercicio al que corresponde la fecha actual
     * @return
     */
    public String getEjercicio61(){
        String ejercicio="";
        try {
            Statement stm = conexion61().createStatement();
            ResultSet rs = stm.executeQuery("SELECT CIDEJERCICIO FROM admEjercicios where CEJERCICIO='" + anio + "'");

            if (rs.next()) {
                ejercicio=rs.getString(1);
            }else{
                Statement stm1 = conexion2().createStatement();
                ResultSet rs1 = stm1.executeQuery("SELECT CIDEJERCICIO FROM admEjercicios where CEJERCICIO='" + anio + "'");
                if (rs1.next()) {
                    ejercicio = rs1.getString(1);
                }
            }
            return ejercicio;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return ejercicio;
    }

    /**
     * limpia los campos espesificados
     */
    public void Limpiar(){
        txtcodpro.setText("");
        CnomProd.setText("");
        txtAlm1.setText("");
        txtAlm8.setText("");
        txtAlm9.setText("");
        txtAlm2.setText("");
        txtAlm3.setText("");
        txtAlm4.setText("");
    }

    /**
     * tras la seleccion de un producto el la lista recupera el producto seleccionado para realizar las consultas de existencias
     * en los diferentes almacenes.
     */
    public void Recuperar(){
try {
    Limpiar();
    ModeloProductos model = (ModeloProductos) getIntent().getSerializableExtra("Elemento");
    txtcodpro.setText(model.getCodProducto());
    if (tblExistencias.getVisibility() == View.GONE) {
        tblExistencias.setVisibility(View.VISIBLE);
    }
    progressBar6.setVisibility(View.VISIBLE);
    new Handler().postDelayed(new Runnable(){
        public void run(){
try {
    //----------------------------
    consultarIdProducto();
    consultarIdProducto61();
    consultarIdProductoLagunas();
    consultarIdProductoSD();
    existenciaAlmacenMatR();
    existenciaAlmacen1();
    existenciaAlmacen8();
    existenciaAlmacen2();
    existenciaAlmacen9();
    existenciaAlmacen3();
    existenciaAlmacen4();
    progressBar6.setVisibility(GONE);
    //----------------------------
}catch (Exception e){

}
        }
    }, 2000); //5000 millisegundos = 5 segundos.

}catch (Exception ex){
  // String mensaje=ex.getMessage();
   //new DialogError(context,"");
}
    }
}