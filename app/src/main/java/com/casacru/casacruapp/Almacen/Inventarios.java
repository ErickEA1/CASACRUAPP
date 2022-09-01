package com.casacru.casacruapp.Almacen;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.casacru.casacruapp.SQLite.DatabaseOpenHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Inventarios extends AppCompatActivity {
    private String IDUnidadM, UnidadM, almacen,folio,UltimoCosto,ExistenciaActual;
    private TextView txtcodpro,CnomProd,txtExistenciaActua;
    private EditText txtExistenciaContada;
    private Button btnescan,btnbuscar,buttonGuardar;
    private Context context;
    private String IP, PUERTO, USUARIO, CONTRASEÑA, BD, idAlma, AlmacenB;
    private String mes,anio,IdProducto, IdProducto61;
    private String txtAlm1, txtAlm8, txtAlm2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventarios);
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ObtenerSharePreferences();
            Fecha();
            CnomProd = (TextView) findViewById(R.id.textNomPro);
            txtcodpro = (TextView) findViewById(R.id.txtCod);
            txtExistenciaActua = (TextView) findViewById(R.id.txtExistenciaActual);
            txtExistenciaContada = (EditText) findViewById(R.id.txtExistenciaContada);
            btnescan = (Button) findViewById(R.id.btnEscanear);
            btnescan.setOnClickListener(nOnClickListener);
            btnbuscar = (Button) findViewById(R.id.btnBusca2);
            btnbuscar.setOnClickListener(nOnClickListener);
            buttonGuardar = (Button) findViewById(R.id.buttonGuardar);
            buttonGuardar.setOnClickListener(nOnClickListener);
            context = this;

            Recuperar();


            Toast.makeText(context, almacen+", "+folio, Toast.LENGTH_SHORT).show();
        }catch (Exception ex){

        }

    }

    /**
     * Este metodo se encarga de llenar la barra de menu
     * @param menu plantilla del contenido de la barra de menu (xml)
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * En este metodo se asignan las acciones que se realizaran cuando se de clic
     * en alguna opcion dentro de la barra de menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Este metodo se escarga de las acciones onClick de cada boton existente dentro la la actividad
     */
    private View.OnClickListener nOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnEscanear:
                    new IntentIntegrator(Inventarios.this).initiateScan();
                    Limpiar();
                    if(almacen.equals("TIENDA DENGUI")){
                        buscard("1");
                        idAlma="8";
                    }else if(almacen.equals("TIENDA NOPALA")){
                        buscard("1");
                        idAlma="9";
                    }else if(almacen.equals("ALMACEN DENGUI")){
                        buscard("1");
                        idAlma="1";
                    }else if(almacen.equals("TIENDA 61")){
                        buscard("1");
                        idAlma="1";
                        BD="adCASACRU61";
                    }else if(almacen.equals("LAGUNAS OAXACA")){
                        buscard("3");
                        idAlma="3";
                    }else if(almacen.equals("SANTO DOMINGO")){
                        buscard("1");
                        BD="adSANTODOMINGO";
                    }else if(almacen.equals("MATIAS ROMERO")){
                        buscard("3");
                        idAlma="4";
                    }
                    break;
                case R.id.btnBusca2:
                    try{
                        Intent intent=new Intent(context, ListadoProductos.class);
                        intent.putExtra("Param","4");
                        startActivity(intent);
                        finish();

                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.buttonGuardar:
                    try {
                        DatabaseOpenHelper dbh = new DatabaseOpenHelper(context, "Inventario", null, 1);
                        SQLiteDatabase database = dbh.getWritableDatabase();

                        Cursor fila = database.rawQuery
                                ("select contado from productos where codigo='"+txtcodpro.getText().toString()+"' and Folio='"+folio+"' and almacen='"+almacen+"'", null);

                        if (fila.moveToFirst()) {
                            actualizarbd();
                        }else{
                            insert();
                        }
                        database.close();
                    }catch (Exception ex){

                    }


            }
        }
    };

    /**
     * Este metodo permite lanzar el escaner para los codigos de barras.
     * @param requestCode1
     * @param resultCode1
     * @param data1
     */
    protected void onActivityResult(int requestCode1, int resultCode1, @Nullable Intent data1) {
        super.onActivityResult(requestCode1, resultCode1, data1);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode1, resultCode1, data1);
        if (result != null)
            if (result.getContents() != null) {
                txtcodpro.setText("" + result.getContents());
                try{
                    existenciaAlmacen();
                    consultarIdProducto();
                    consultarUnidadMedida();
                    existenciaAlmacen1();

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

    }

    /**
     * Este metodo se encarga de realizar una consulta a una bd sqlite en caso de
     * cargar un archivo de productos.
     */
    public void existenciaAlmacen() {
        try {
            DatabaseOpenHelper dbh = new DatabaseOpenHelper(this, "Listado", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();

            Cursor fila = database.rawQuery
                    ("select nombre,existencia,unidad from productoslistado where codigo='"+txtcodpro.getText().toString()+"'", null);

            if (fila.moveToFirst()) {
                CnomProd.setText(fila.getString(0));
                txtExistenciaActua.setText(fila.getString(1));
                UnidadM=fila.getString(2);
                existenciContada();
            }
            database.close();
        }catch (Exception ex){

        }
    }

    /**
     * Este metodo realiza la consulta de la existencia de un producto que ya haya sido registrado previamente
     * en caso de no tener un producto registrado con el mismo codigo no traera nada.
     */
    public void existenciContada() {
        try {
            DatabaseOpenHelper dbh = new DatabaseOpenHelper(this, "Inventario", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();

            Cursor fila = database.rawQuery
                    ("select contado from productos where codigo='"+txtcodpro.getText().toString()+"' and Folio='"+folio+"' and almacen='"+almacen+"'", null);

            if (fila.moveToFirst()) {
                txtExistenciaContada.setText(fila.getString(0));

            }
            database.close();
        }catch (Exception ex){

        }
    }

    /**
     * Este metodo se encarga de realizar una actualizacion de la cantidad contada del producto escaneado
     * o buscado en caso de ya estar registrado en la base de datos de inventario
     */
    public void actualizarbd() {
        try {
            DatabaseOpenHelper dbh = new DatabaseOpenHelper(this, "Inventario", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();

            if (database != null) {
                database.execSQL("UPDATE productos SET contado='" + txtExistenciaContada.getText().toString() + "' where codigo='" + txtcodpro.getText().toString() + "' and Folio='"+folio+"' and almacen='"+almacen+"'");
                database.close();
            }
            Toast.makeText(context, "Actualizado", Toast.LENGTH_SHORT).show();
        }catch (Exception e){

        }
    }

    /**
     * Este metodo realiza la insercion de productos contabilizados o inventariados.
     */
    public void insert() {
        try {
            DatabaseOpenHelper dbh = new DatabaseOpenHelper(this, "Inventario", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();

            ContentValues registro = new ContentValues();
            registro.put("Folio", folio);
            registro.put("almacen", almacen);
            registro.put("nombre", CnomProd.getText().toString());
            registro.put("codigo", txtcodpro.getText().toString());
            registro.put("actual", ExistenciaActual);
            registro.put("unidad", UnidadM);
            registro.put("contado", txtExistenciaContada.getText().toString());
            registro.put("costo", UltimoCosto);

            database.insert("productos", null, registro);
            database.close();
            Toast.makeText(this, "Se inserto", Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Esyte metodo limpia los cuadros de texto de la actividad
     */
    public void Limpiar(){
        txtcodpro.setText("");
        CnomProd.setText("");
        txtExistenciaActua.setText("");
        txtExistenciaContada.setText("");

    }

    /**
     * Este metodo se encarga  de realizar cambios en el id del almacen  dependiendo del nombre de
     * almacen seleccionado
     */
    public void Recuperar(){
        try {
            if(almacen.equals("TIENDA DENGUI")){
                buscard("1");
                idAlma="8";
            }else if(almacen.equals("TIENDA NOPALA")){
                buscard("1");
                idAlma="9";
            }else if(almacen.equals("ALMACEN DENGUI")){
                buscard("1");
                idAlma="1";
            }else if(almacen.equals("TIENDA 61")){
                buscard("2");
                idAlma="1";
                BD="adCASACRU61";
            }else if(almacen.equals("LAGUNAS OAXACA")){
                buscard("3");
                idAlma="3";
            }else if(almacen.equals("SANTO DOMINGO")){
                buscard("4");
                idAlma="3";
            }else if(almacen.equals("MATIAS ROMERO")){
                buscard("3");
                idAlma="4";
            }
            Limpiar();
            ModeloProductos model = (ModeloProductos) getIntent().getSerializableExtra("Elemento");
            txtcodpro.setText(model.getCodProducto());
            if(txtcodpro.getText().toString() != ""){
                existenciaAlmacen();
                consultarIdProducto();
                consultarUnidadMedida();
                existenciaAlmacen1();

            }

        }catch (Exception ex){
            String mensaje=ex.getMessage();
        }
    }

    /**
     * Este metodo obtiene el folio y nombre del almacen seleccionado al inicio de la actividad
     */
    public void ObtenerSharePreferences(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("Datos",this.MODE_PRIVATE);
         almacen=sharedPreferences.getString("Almacen","null");
         folio=sharedPreferences.getString("Folio","null");

    }

    /**
     * Consultar el id y nombre del producto escaneado del almacen dengui, tienda dengui, tienda nopala
     */
    public void consultarIdProducto(){
        try {


            Statement stm = conexion2().createStatement();
            ResultSet rs = stm.executeQuery("SELECT CIDPRODUCTO, CNOMBREPRODUCTO,CIDUNIDADBASE FROM admProductos where CCODIGOPRODUCTO='" + txtcodpro.getText().toString() + "'");

            if (rs.next()) {
                IdProducto = rs.getString(1);
                CnomProd.setText(rs.getString(2));
                IDUnidadM= rs.getString(3);
            }

            Statement stm4 = conexion2().createStatement();
            String consultaCC = "SELECT TOP 1 CULTIMOCOSTOH FROM admCostosHistoricos WHERE CIDPRODUCTO='" + IdProducto + "' and CIDALMACEN='"+idAlma+"' ORDER BY CIDCOSTOH DESC";
            ResultSet rsCC = stm4.executeQuery(consultaCC);

            if (rsCC.next()) {
                UltimoCosto = rsCC.getString(1);
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "No existe", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Este metodo realiza una consulta al servidor para obtener la unidad de medida del producto.
     */
    public void consultarUnidadMedida(){
        try {
            Statement stm = conexion2().createStatement();
            ResultSet rs = stm.executeQuery("SELECT CABREVIATURA FROM admUnidadesMedidaPeso where CIDUNIDAD='" + IDUnidadM + "'");

            if (rs.next()) {
                UnidadM=rs.getString(1);
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "No existe", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *Busca los parametros dee conexion el la base de datos SQLite del aplicativo
     */
    public void buscard(String id) {
        try {
            DatabaseHelper dbh = new DatabaseHelper(this, "Conexion", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();

            Cursor fila = database.rawQuery
                    ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id='"+id+"'", null);

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
     *  Este metodo realiza una consulta para saber el id del alamcen seleccionado
     */
    public void ConsultaIdAlmacen() {
    try {
        Statement stm = conexion2().createStatement();
        ResultSet rs = stm.executeQuery("SELECT CIDALMACEN FROM admAlmacenes WHERE CCODIGOALMACEN='"+idAlma+"'");
        if (rs.next()) {
            AlmacenB=rs.getString(1);
        }
        Toast.makeText(context, AlmacenB, Toast.LENGTH_SHORT).show();

    }catch(Exception ex){
       // Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();

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
     * Este metodo realiza una consulta de la existencia actual del producto directamente al servidor
     */
    public void existenciaAlmacen1(){
        try {
            ConsultaIdAlmacen();
            String entradasp="",salidasp="",ejercicio="";
            ejercicio=getEjercicio();
            Statement stm = conexion2().createStatement();
            ResultSet rs = stm.executeQuery("SELECT CENTRADASPERIODO"+mes+", CSALIDASPERIODO"+mes+" from admExistenciaCosto " +
                    "WHERE CIDEJERCICIO='"+ejercicio+"' and CIDPRODUCTO='"+IdProducto+"' and CIDALMACEN='"+AlmacenB+"'");
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
            txtExistenciaActua.setText(Existencia+" "+UnidadM);
            ExistenciaActual=Existencia;
            existenciContada();

        }catch(Exception ex){

            txtExistenciaActua.setText("No existe");
        }
    }

    /**
     * Este metodo obtiene la fecha actual para realizar las consultas de existencias
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
        } catch (SQLException e) {
        //    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return ejercicio;
    }


}