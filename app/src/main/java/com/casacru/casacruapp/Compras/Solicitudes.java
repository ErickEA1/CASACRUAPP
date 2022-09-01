package com.casacru.casacruapp.Compras;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.casacru.casacruapp.General.ListadoProductos;
import com.casacru.casacruapp.Modelo.DtoProductos;
import com.casacru.casacruapp.Modelo.ModeloProductos;
import com.casacru.casacruapp.R;
import com.casacru.casacruapp.SQLite.DatabaseHelper;
import com.casacru.casacruapp.SQLite.DatabaseOpenHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

public class Solicitudes extends AppCompatActivity  {
    private Button btnescan, btnSave, btnB;
    private String IdProd, IP, PUERTO, CONTRASEÑA, BD,USUARIO, IDAlmac,NomAlmacen;
    private TextView txtcodpro;
    private TextInputEditText CnomProd;
    private ArrayList<String> listaSolicitudes= new ArrayList<>();
    private ArrayList<DtoProductos> lista= new ArrayList<>();
    private  EditText TxtCantidad,Unidad;
    private LinearLayout lyData;
    private RecyclerView listp;
    private Context context;
    private FirebaseDatabase fb;
    private DatabaseReference dr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_de_compra);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            context=this;
            NomAlmacen=ObtenerSharePreferences();
            buscar();
            Toolbar toolbar1=(Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            CnomProd =(TextInputEditText) findViewById(R.id.textNomPro);
            txtcodpro = (TextView) findViewById(R.id.txtCod);
            btnescan=(Button) findViewById(R.id.btnEscanear);
            btnescan.setOnClickListener(nOnClickListener);
            TxtCantidad = (EditText) findViewById(R.id.txtCantidad);
            Unidad=(EditText) findViewById(R.id.txtunidadmedida);
            btnSave=(Button) findViewById(R.id.btnGuardar);
            btnSave.setOnClickListener(nOnClickListener);
            btnB=(Button) findViewById(R.id.btnBusca2);
            btnB.setOnClickListener(nOnClickListener);
            lyData = (LinearLayout) findViewById(R.id.LyDatos);
            listp=(RecyclerView) findViewById(R.id.listaprod);
            listp.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
            inicializarFirebase();
            Recuperar();
            Toast.makeText(context, NomAlmacen, Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Toast.makeText(this, "Error: "+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Realiza consulta a base de datos SQLite del aplicativo para recolectar los parametros de conexion
     * al servidor
     */
    public void buscar() {
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
                IDAlmac=(fila.getString(4));
                database.close();
            } else {
                Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){
            Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Metodo que realiza la conexion a la base de datos SQL SERVER mediante los parametros de conexion
     * @return Coneccion a base de datos
     */
    public Connection conexion2() {
        Connection conexion2 = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion2 = DriverManager.getConnection("jdbc:jtds:sqlserver://" + this.IP + ":" + this.PUERTO + ";databaseName=" + this.BD + ";user="+this.USUARIO+";password=" + this.CONTRASEÑA + ";");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return conexion2;
    }

    /**
     * Metodo para crear un Escaner y leer el codigo de barras
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
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

    }

    /**
     * metodos para botones al dar click
     */
    private View.OnClickListener nOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnEscanear:
                    new IntentIntegrator(Solicitudes.this).initiateScan();
                    limpiar();
                    break;
                case R.id.btnGuardar:
                    insertar();
                    break;
                case R.id.btnBusca2:
                    Intent intent=new Intent(context, ListadoProductos.class);
                    intent.putExtra("Param","3");
                    startActivity(intent);
                    finish();

                    break;
            }
        }
    };

    /**
     * Consultar el id y nombre del producto en el almacen dengui, tienda dengui,tienda nopala
     */
    public void consultarIdProducto(){
        try {
            Statement stm = conexion2().createStatement();
            ResultSet rs = stm.executeQuery("SELECT CIDPRODUCTO, CNOMBREPRODUCTO FROM admProductos where CCODIGOPRODUCTO='" + txtcodpro.getText().toString() + "'");

            if (rs.next()) {

                    IdProd = rs.getString(1);
                    CnomProd.setText(rs.getString(2));
                }
        } catch (SQLException e) {
          //  Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *Inicializar la conexion a la base de datos de firebase
     */
    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        fb = FirebaseDatabase.getInstance();
        dr=fb.getReference();
    }

    /**
     * Realiza la insercion del objeto creado mediante el llenado de los campos.
     */
    public void insertar() {
    try {
        String CodProducto = txtcodpro.getText().toString();
        String Nomproducto = CnomProd.getText().toString();
        String Cantidad = TxtCantidad.getText().toString();
        String unidad = Unidad.getText().toString();


        DtoProductos p = new DtoProductos();
        p.setId(UUID.randomUUID().toString());
        p.setAlmacen(NomAlmacen);
        p.setCodigoProducto(CodProducto);
        p.setNombreProducto(Nomproducto);
        p.setCantidad(Cantidad);
        p.setUnidad(unidad);
        p.setSurtido(false);

        dr.child(NomAlmacen).child(p.getId()).setValue(p);
        Toast.makeText(context, "Listo", Toast.LENGTH_SHORT).show();
    }catch (Exception e){
        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
        }

    /**
     * Realiza un limpiado de los campos espesificados.
     */
    public void limpiar(){
CnomProd.setText("");
TxtCantidad.setText("");
Unidad.setText("");
    }

    public void Recuperar(){
        try{
            ModeloProductos model = (ModeloProductos) getIntent().getSerializableExtra("Elemento");
            txtcodpro.setText(model.getCodProducto());
            CnomProd.setText(model.getDescripcion());

        }catch (Exception e){
            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public String ObtenerSharePreferences(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("Mis_preferences",this.MODE_PRIVATE);
        String Almacen=sharedPreferences.getString("Almacen","null");
        return Almacen;
    }

    public void existenciaAlmacen() {
        try {
            DatabaseOpenHelper dbh = new DatabaseOpenHelper(this, "Listado", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();

            Cursor fila = database.rawQuery
                    ("select nombre from productoslistado where codigo='"+txtcodpro.getText().toString()+"'", null);

            if (fila.moveToFirst()) {
                CnomProd.setText(fila.getString(0));
            }
            database.close();
        }catch (Exception ex){

        }
    }

}