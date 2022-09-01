package com.casacru.casacruapp.Almacen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.casacru.casacruapp.Adaptador.AdaptadorSegmentos;
import com.casacru.casacruapp.Dialog.DialogError;
import com.casacru.casacruapp.Modelo.ModeloGrupos;
import com.casacru.casacruapp.Modelo.ModeloSegmentos;
import com.casacru.casacruapp.R;
import com.casacru.casacruapp.SQLite.DatabaseHelper;
import com.casacru.casacruapp.SQLite.DatabaseOpenHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Segmentos_contables extends AppCompatActivity {
    Spinner SPGrupo;
    String Grupo, CodGrupo;
    String IP, PUERTO,BD, CONTRASENA, IDAlmac, USUARIO;
    ArrayList<ModeloGrupos> listaClass1 = new ArrayList<ModeloGrupos>();
    ArrayList<String> listagrupos=new ArrayList<>();
    ArrayList<ModeloSegmentos> listaSegmentos= new ArrayList<ModeloSegmentos>();
    TextInputEditText txtSegContable;
    Button btnSaveSeg, btnCargar;
    RecyclerView RvSeg;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segmentos_contables);

    try {
        context=this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buscar();
        SPGrupo = (Spinner) findViewById(R.id.SpGrupo);
        txtSegContable = (TextInputEditText) findViewById(R.id.txt_SegContable);
        btnSaveSeg=(Button) findViewById(R.id.btnGuardarSeg);
        RvSeg=(RecyclerView)findViewById(R.id.RVSeg);
        btnCargar=(Button)findViewById(R.id.btnCargar);
        RvSeg.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        llenarSpinner();
        ListarSeg();
        btnCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarArray();
                ListarSeg();
            }
        });
        ArrayAdapter<CharSequence> adaptadorGrupos = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listagrupos);
        SPGrupo.setAdapter(adaptadorGrupos);
        SPGrupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtSegContable.setText("");
                Grupo = listaClass1.get(position).getNomGrupo();
                CodGrupo=listaClass1.get(position).getCodGrupo();
                buscarSeg();
                Toast.makeText(getApplicationContext(), listaClass1.get(position).getNomGrupo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSaveSeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuardarSegmento();
                ActualizarSeg();
                ListarSeg();
            }
        });
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
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void buscar() {
        DatabaseHelper dbh = new DatabaseHelper(this, "Conexion", null, 1);
        SQLiteDatabase database = dbh.getWritableDatabase();



            Cursor fila = database.rawQuery
                    ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id=1", null);

            if (fila.moveToFirst()) {
                this.IP=(fila.getString(0));
                this.PUERTO=(fila.getString(1));
                this.CONTRASENA=(fila.getString(2));
                this.BD=(fila.getString(3));
                this.IDAlmac=(fila.getString(4));
                this.USUARIO=fila.getString(5);
                database.close();
                Toast.makeText(this, "Conectado a: "+IP+","+PUERTO, Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public Connection conexion2() {
        Connection conexion2 = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

            conexion2 = DriverManager.getConnection("jdbc:jtds:sqlserver://" + this.IP + ":" + this.PUERTO + ";databaseName=" + this.BD + ";user="+this.USUARIO+";password=" + this.CONTRASENA + ";");

        } catch (Exception e) {
            new DialogError(context,"");
        }
        return conexion2;
    }

    public void llenarSpinner(){
        try {
            listaClass1.add(new ModeloGrupos("000","*","SELECCIONA UN GRUPO"));

            Statement stm = conexion2().createStatement();
            String consulta="SELECT CIDVALORCLASIFICACION,CCODIGOVALORCLASIFICACION,CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDCLASIFICACION='"+25+"'";
            ResultSet rs = stm.executeQuery(consulta);

            while (rs.next()) {
                listaClass1.add(new ModeloGrupos(String.valueOf(rs.getString(1)),rs.getString(2), rs.getString(3)));
            }
            for(int i=0;i<listaClass1.size();i++){
                listagrupos.add(listaClass1.get(i).getCodGrupo()+" - "+listaClass1.get(i).getNomGrupo());
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(),"Error spinner Grupos", Toast.LENGTH_SHORT).show();
        }
    }

    public void GuardarSegmento(){
        try {
            DatabaseOpenHelper dbh = new DatabaseOpenHelper(this, "Segmentos", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();

            ContentValues registro = new ContentValues();
            registro.put("CodSegmento", txtSegContable.getText().toString());
            registro.put("CodGrupo", CodGrupo);
            registro.put("NomGrupo", Grupo);

            database.insert("segmentoscontables", null, registro);
            database.close();
            Toast.makeText(this, "¡Listo!", Toast.LENGTH_SHORT).show();

        }catch(Exception ex){
            Toast.makeText(this, "Error: "+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        }

    public void buscarSeg() {
        DatabaseOpenHelper dbh = new DatabaseOpenHelper(this, "Segmentos", null, 1);
        SQLiteDatabase database = dbh.getWritableDatabase();

        Cursor fila = database.rawQuery
                ("select CodSegmento from segmentoscontables where CodGrupo='"+CodGrupo+"' and NomGrupo='"+Grupo+"'", null);

        if (fila.moveToFirst()) {
            txtSegContable.setText(fila.getString(0));
            database.close();

        } else {
            Toast.makeText(this, "No existe segmento contable", Toast.LENGTH_SHORT).show();
        }
    }

    public void ActualizarSeg(){
        DatabaseOpenHelper dbh = new DatabaseOpenHelper(this, "Segmentos", null, 1);
        SQLiteDatabase database = dbh.getWritableDatabase();

        database.execSQL("UPDATE segmentoscontables SET CodSegmento='"+txtSegContable.getText().toString()+"' where CodGrupo='"+CodGrupo+"' and NomGrupo='"+Grupo+"'");
        database.close();
    }

    public void ListarSeg() {
        try {
            DatabaseOpenHelper dbh = new DatabaseOpenHelper(this, "Segmentos", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();
            listaSegmentos.clear();

            Cursor fila = database.rawQuery("select CodGrupo,NomGrupo,CodSegmento from segmentoscontables", null);
            if(fila.moveToFirst()) {
                do {
                    listaSegmentos.add(new ModeloSegmentos(fila.getString(0), fila.getString(1),fila.getString(2)));
                } while (fila.moveToNext());
            }
            database.close();
            AdaptadorSegmentos adapter = new AdaptadorSegmentos(listaSegmentos);
            RvSeg.setAdapter(adapter);

        }catch (Exception ex){
            Toast.makeText(this, "Error: "+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void CargarArray(){
        try {
            DatabaseOpenHelper dbh = new DatabaseOpenHelper(this, "Segmentos", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();

            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0001','AB','ACEROS')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0002','AC','TINACOS Y CISTERNAS')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0003','AD','ADHESIVOS Y COLORANTES PARA CONCRETOS')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0004','AE','CEMENTANTES')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0026','AF','GALVANIZADO')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0005','AG','DECORATIVOS')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0006','AH','HERRAMIENTAS')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0007','AI','ACCESORIOS DE BAÑO')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0008','AJ','MUEBLES DE BAÑO')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0009','AK','VIBROCOMPRIMIDOS')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0010','AL','LAMINAS')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0011','AM','CERRAJERIA')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0012','AN','MOLDURAS Y MADERAS')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0013','AO','MATERIAL ELECTRICO')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0014','AP','PERFILES')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0015','AQ','BROCAS Y DISCOS')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0016','AR','PINTURAS, SOLVENTES Y DERIVADOS')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0017','AS','PLOMERIA')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0018','AT','MATERIAL PVC')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0028','ATC','MATERIAL CPVC')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0019','AU','TORNILLERIA')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0020','AV','MATERIAL DE LIMPIEZA')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0021','AW','MATERIAL DE SEGURIDAD INDUSTRIAL')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0022','AX','TUBO PLUS')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0023','AY','PETREOS')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0024','AZ','MATERIAL DE EXHIBICION')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0025','RMA','RENTA DE MAQUINARIA Y FLETES')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0030','VID','VIDEOVIGILANCIA')");
            database.execSQL("INSERT INTO segmentoscontables(CodSegmento,CodGrupo,NomGrupo) values('0029','VMA','VENTA DE MAQUINARIA')");

            database.close();
            Toast.makeText(this, "¡Listo!", Toast.LENGTH_SHORT).show();

        }catch(Exception ex){
            Toast.makeText(this, "Error: "+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}