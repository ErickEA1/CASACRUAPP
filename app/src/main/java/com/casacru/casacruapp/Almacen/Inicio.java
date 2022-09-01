package com.casacru.casacruapp.Almacen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.casacru.casacruapp.Dialog.DialogSelectAlmacen;
import com.casacru.casacruapp.R;
import com.casacru.casacruapp.SQLite.DatabaseOpenHelper;
import com.casacru.casacruapp.Ventas.InicioVentas;


public class Inicio extends AppCompatActivity implements DialogSelectAlmacen.Finalizo {
    Context context;
ImageButton button6;
String lugar="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            DatabaseOpenHelper baseHelper = new DatabaseOpenHelper(this, "PRODUCTOS", null, 1);
            SQLiteDatabase db = baseHelper.getReadableDatabase();
            baseHelper.borrarRegistros(db);
            context = this;
            button6=(ImageButton) findViewById(R.id.button6);
            button6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent cambiar = new Intent(context, VendidosMensual.class);
                    startActivity(cambiar);

                }
            });
        }catch (Exception e){

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
        }
        return super.onOptionsItemSelected(item);
    }

    public void ubicaciones(View view) {
        lugar="2";
        new DialogSelectAlmacen(context, Inicio.this);
    }

    public void Existencias(View view) {
        Intent Existencias = new Intent(this, Existencias.class);
        startActivity(Existencias);
    }

    public void CambiarPrecios(View view) {
        Intent cambiar = new Intent(this, ActualizarPrecios.class);
        startActivity(cambiar);
    }

    public void cambiarInventario(View v){
        Intent cambiar = new Intent(this, InicioInventario.class);
        startActivity(cambiar);
    }

    public void cambiarreporte(View v){
        lugar="3";
        new DialogSelectAlmacen(context, Inicio.this);

    }

    @Override
    public void Resultado(String Id) {
        if(Id=="TIENDA DENGUI"){
            final SharedPreferences sharedPreferences=getSharedPreferences("Datos2", context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Almacen","1");
            editor.putString("nomAlmacen", "11");
            editor.apply();
            if(lugar=="1"){
                Intent cambiar = new Intent(this, ProductosVendidosMensual.class);
                startActivity(cambiar);
            }else if(lugar=="2"){
                Intent cambiar = new Intent(this, Ubicaciones.class);
                startActivity(cambiar);
            }else if(lugar=="3"){
                Intent cambiar = new Intent(this, ReporteMaxMin.class);
                startActivity(cambiar);
            }

        }else if(Id=="TIENDA NOPALA"){
            final SharedPreferences sharedPreferences=getSharedPreferences("Datos2", context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Almacen","1");
            editor.putString("nomAlmacen", "12");
            editor.apply();
            if(lugar=="1"){
                Intent cambiar = new Intent(this, ProductosVendidosMensual.class);
                startActivity(cambiar);
            }else if(lugar=="2"){
                Intent cambiar = new Intent(this, Ubicaciones.class);
                startActivity(cambiar);
            }else if(lugar=="3"){
                Intent cambiar = new Intent(this, ReporteMaxMin.class);
                startActivity(cambiar);
            }
        }else if(Id=="ALMACEN DENGUI"){
            final SharedPreferences sharedPreferences=getSharedPreferences("Datos2", context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Almacen","1");
            editor.putString("nomAlmacen", "1");
            editor.apply();
            if(lugar=="1"){
                Intent cambiar = new Intent(this, ProductosVendidosMensual.class);
                startActivity(cambiar);
            }else if(lugar=="2"){
                Intent cambiar = new Intent(this, Ubicaciones.class);
                startActivity(cambiar);
            }else if(lugar=="3"){
                Intent cambiar = new Intent(this, ReporteMaxMin.class);
                startActivity(cambiar);
            }
        }else if(Id=="TIENDA 61"){
            final SharedPreferences sharedPreferences=getSharedPreferences("Datos2", context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Almacen","2");
            editor.putString("nomAlmacen", "1");
            editor.apply();
            if(lugar=="1"){
                Intent cambiar = new Intent(this, ProductosVendidosMensual.class);
                startActivity(cambiar);
            }else if(lugar=="2"){
                Intent cambiar = new Intent(this, Ubicaciones.class);
                startActivity(cambiar);
            }else if(lugar=="3"){
                Intent cambiar = new Intent(this, ReporteMaxMin.class);
                startActivity(cambiar);
            }
        }else if(Id=="LAGUNAS OAXACA"){
            final SharedPreferences sharedPreferences=getSharedPreferences("Datos2", context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Almacen","3");
            editor.putString("nomAlmacen", "4");
            editor.apply();
            if(lugar=="1"){
                Intent cambiar = new Intent(this, ProductosVendidosMensual.class);
                startActivity(cambiar);
            }else if(lugar=="2"){
                Intent cambiar = new Intent(this, Ubicaciones.class);
                startActivity(cambiar);
            }else if(lugar=="3"){
                Intent cambiar = new Intent(this, ReporteMaxMin.class);
                startActivity(cambiar);
            }
        }else if(Id=="SANTO DOMINGO"){
            final SharedPreferences sharedPreferences=getSharedPreferences("Datos2", context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Almacen","4");
            editor.putString("nomAlmacen", "4");
            editor.apply();
            if(lugar=="1"){
                Intent cambiar = new Intent(this, ProductosVendidosMensual.class);
                startActivity(cambiar);
            }else if(lugar=="2"){
                Intent cambiar = new Intent(this, Ubicaciones.class);
                startActivity(cambiar);
            }else if(lugar=="3"){
                Intent cambiar = new Intent(this, ReporteMaxMin.class);
                startActivity(cambiar);
            }
        }else if(Id=="MATIAS ROMERO"){
            final SharedPreferences sharedPreferences=getSharedPreferences("Datos2", context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Almacen","3");
            editor.putString("nomAlmacen", "10");
            editor.apply();
            if(lugar=="1"){
                Intent cambiar = new Intent(this, ProductosVendidosMensual.class);
                startActivity(cambiar);
            }else if(lugar=="2"){
                Intent cambiar = new Intent(this, Ubicaciones.class);
                startActivity(cambiar);
            }else if(lugar=="3"){
                Intent cambiar = new Intent(this, ReporteMaxMin.class);
                startActivity(cambiar);
            }
        }

    }
}