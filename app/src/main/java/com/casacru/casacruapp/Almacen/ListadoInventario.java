package com.casacru.casacruapp.Almacen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.casacru.casacruapp.Adaptador.AdaptadorFolios;
import com.casacru.casacruapp.Adaptador.AdaptadorSegmentos;
import com.casacru.casacruapp.Adaptador.ListadoAdapter;
import com.casacru.casacruapp.Compras.SolicitudDeCompra;
import com.casacru.casacruapp.Dialog.DialogSelectAlmacen;
import com.casacru.casacruapp.Modelo.DtoProductos;
import com.casacru.casacruapp.Modelo.ModeloFolios;
import com.casacru.casacruapp.Modelo.ModeloProductos;
import com.casacru.casacruapp.Modelo.ModeloSegmentos;
import com.casacru.casacruapp.R;
import com.casacru.casacruapp.SQLite.DatabaseOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListadoInventario extends AppCompatActivity implements DialogSelectAlmacen.Finalizo {

    private String Fecha,NomAlmacen;
    private Context context;
    ArrayList<ModeloFolios> listaProductos= new ArrayList<ModeloFolios>();
    private RecyclerView folios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_inventario);
        try{
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            context=this;
            folios=(RecyclerView) findViewById(R.id.folios);
            folios.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

            ListarFolios();
        }catch (Exception ex){

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
                Fecha=Fecha();
                new DialogSelectAlmacen(context, ListadoInventario.this);

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Metodo eredado de a interfaz de la clase DialogSelectAlmacen.
     * @param Id
     */
    @Override
    public void Resultado(String Id) {
        try {
           insert(Id);
            ListarFolios();
        }catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Este metodo realiza la insercion de un registro que servira como documento para
     * la creacion de inventarios
     * @param Almacen nombre del almacen que se seleccione
     */
    public void insert(String Almacen) {
        DatabaseOpenHelper dbh = new DatabaseOpenHelper(this, "Folios", null, 1);
        SQLiteDatabase database = dbh.getWritableDatabase();
        Fecha=Fecha();
        ContentValues registro=new ContentValues();
        registro.put("Folio",Fecha);
        registro.put("Almacen", Almacen);

        database.insert("folios",null, registro);
        database.close();
        Toast.makeText(this,"Inventario creado",Toast.LENGTH_SHORT).show();
    }

    /**
     * Este metodo obtiene la fecha actual
     * @return
     */
    public String Fecha() {
        String fetch="";
        try {
            SimpleDateFormat dtf=new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar=Calendar.getInstance();
            Date date=calendar.getTime();
            fetch=dtf.format(date);
            return fetch;
        }catch (Exception ex){
            return fetch;
        }
    }

    /**
     * Este metodo realiza el listado del catralogo de productos
     */
    public void ListarFolios() {
        try {
            DatabaseOpenHelper dbh = new DatabaseOpenHelper(this, "Folios", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();
            listaProductos.clear();

            Cursor fila = database.rawQuery("select Folio, Almacen from folios", null);
            if(fila.moveToFirst()) {
                do {
                    listaProductos.add(new ModeloFolios(fila.getString(0), fila.getString(1)));
                } while (fila.moveToNext());
            }
            database.close();
            AdaptadorFolios adapter = new AdaptadorFolios(listaProductos, context, new AdaptadorFolios.OnItemClickListenerr() {
                @Override
                public void onItemClick(ModeloFolios item) {
                    final SharedPreferences sharedPreferences=getSharedPreferences("Datos", context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("Almacen",item.getAlmacen());
                    editor.putString("Folio",item.getFolio());
                    editor.apply();
                    Intent i=new Intent(context, Inventarios.class);
                    startActivity(i);
                }
            });

            folios.setAdapter(adapter);

        }catch (Exception ex){
            Toast.makeText(this, "Error: "+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}