package com.casacru.casacruapp.Ajustes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.casacru.casacruapp.R;
import com.casacru.casacruapp.SQLite.DatabaseHelper;
import com.casacru.casacruapp.SQLite.DatabaseOpenHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class menuAjustes extends AppCompatActivity  {
private Button btn1, btn2,btn3;
private Context context;
private String IP, PUERTO,BD, CONTRASENA, IDAlmac, USUARIO,ruta;
private ProgressBar pb;
private TextView txtruta;
    private File f;
    Uri rut;
    Intent myFileIntent;
    CargarArchivo cargarArchivo=new CargarArchivo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = this;
            setContentView(R.layout.activity_menu_ajustes);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            buscar();
            txtruta = (TextView) findViewById(R.id.txtruta);
            pb = (ProgressBar) findViewById(R.id.progressBar);
            btn1 = (Button) findViewById(R.id.btnAjuste);
            btn2 = (Button) findViewById(R.id.bntCargaLista);
            btn3 = (Button) findViewById(R.id.btnBuscarArchivo);
            btn1.setOnClickListener(onclick);
            btn2.setOnClickListener(onclick);
            isExternalStorageReadable();
            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Eliminar_tabla2();
                    myFileIntent = new Intent();
                    myFileIntent.setType("*/*");
                    myFileIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(myFileIntent, 10);

                }
            });
        }catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
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

    /**
     * Metodo para poder leer el path de un archivo en la memoria externa
     * @param requestCode
     * @param resultCode
     * @param resultData
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        try {
            if (requestCode == 10
                    && resultCode == Activity.RESULT_OK) {
                // The result data contains a URI for the document or directory that
                // the user selected.
                Uri uri = null;
                if (resultData != null) {
                    uri = resultData.getData();

                    String path = uri.getPath();

                    txtruta.setText(path);
                    f = new File(path);
                    inicializar(uri);

                    // readTextFromUri(uri);
                    // Perform operations on the document using its URI.
                }
            }
        }catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * metodo para verificar los permisos de almacenamiento
     * @return
     * regresa un true o un false
     */
    private boolean isExternalStorageReadable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }


    private View.OnClickListener onclick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
      switch (v.getId()){
          case R.id.btnAjuste:
              Intent i= new Intent(context, Ajustes.class);
              startActivity(i);
              break;
          case R.id.bntCargaLista:
              Eliminar_tabla();
              pb.setVisibility(View.VISIBLE);
              try {
                  String Consulta = "SELECT CCODIGOPRODUCTO,CNOMBREPRODUCTO FROM admProductos";
                  Statement stm3 = conexion2().createStatement();
                  ResultSet rs = stm3.executeQuery(Consulta);

                  DatabaseOpenHelper dbh = new DatabaseOpenHelper(context, "Listado", null, 1);
                  SQLiteDatabase database = dbh.getWritableDatabase();
                  while (rs.next()) {
                      ContentValues registro=new ContentValues();
                      registro.put("codigo", rs.getString(1) );
                      registro.put("nombre", rs.getString(2) );
                      database.insert("listado",null, registro);

                  }
                  database.close();
                  Toast.makeText(context, "Lista cargada", Toast.LENGTH_SHORT).show();
              }catch (Exception e){
                    Toast.makeText(context,"Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
              }
              pb.setVisibility(View.GONE);
              break;
      }
        }
    };

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



        } else {
           // Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return conexion2;
    }

    public void inicializar(Uri dato){
        try {
            if (cantidadRegistros(context) == 0) {

                String[] texto = leerArchivo(dato);
                Toast.makeText(context, "tamaño " + texto.length, Toast.LENGTH_LONG).show();
                DatabaseOpenHelper baseHelper = new DatabaseOpenHelper(context, "Listado", null, 1);
                SQLiteDatabase db = baseHelper.getWritableDatabase();
                db.beginTransaction();
                try {
                    for (int i = 0; i < texto.length; i++) {

                        String[] linea = texto[i].split(";");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("codigo", linea[0]);
                        contentValues.put("nombre", linea[1]);
                        contentValues.put("unidad", linea[2]);
                        contentValues.put("existencia", linea[3]);
                        contentValues.put("ultimocosto", linea[4]);
                        db.insert("productoslistado", null, contentValues);
                        db.replace("productoslistado", null, contentValues);

                    }
                    Toast.makeText(context, "registros insertados " + texto.length, Toast.LENGTH_LONG).show();
                    db.setTransactionSuccessful();
                    db.endTransaction();
                }catch (Exception ex){
                    Toast.makeText(context, "Revisa tu archivo", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, "la tabla ya esta creada", Toast.LENGTH_LONG).show();

            }
        }catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    /**
     * metodo para contar el total de registros en la bd
     * @return
     */
    private long cantidadRegistros(Context context) {
        DatabaseOpenHelper baseHelper = new DatabaseOpenHelper(context, "Listado", null, 1);
        SQLiteDatabase db = baseHelper.getReadableDatabase();
        long cn = DatabaseUtils.queryNumEntries(db, "productoslistado");
        db.close();
        return cn;
    }


    /**
     * metodo para leer la informacion de un archivo de texto
     * @return
     * regresa un arreglo con la informacion capturada del documento
     */
    private String[] leerArchivo(Uri uri) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);

            try {
                int i = inputStreamReader.read();
                while (i != -1) {
                    byteArrayOutputStream.write(i);
                    i = inputStreamReader.read();
                }
                inputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }catch (Exception ex){

        }

        return byteArrayOutputStream.toString().split("\n");
    }


    public void Eliminar_tabla(){
        DatabaseOpenHelper baseHelper = new DatabaseOpenHelper(context, "Listado", null, 1);
        SQLiteDatabase db = baseHelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM listado");
        }catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
    }

    public void Eliminar_tabla2(){
        DatabaseOpenHelper baseHelper = new DatabaseOpenHelper(context, "Listado", null, 1);
        SQLiteDatabase db = baseHelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM productoslistado");
        }catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
    }

}