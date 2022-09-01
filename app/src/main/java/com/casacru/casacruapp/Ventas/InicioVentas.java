package com.casacru.casacruapp.Ventas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.casacru.casacruapp.Almacen.ProductosVendidosMensual;
import com.casacru.casacruapp.Dialog.DialogSelectAlmacen;
import com.casacru.casacruapp.R;

public class InicioVentas extends AppCompatActivity implements DialogSelectAlmacen.Finalizo {
ImageButton button3, button4,button5, button6;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_ventas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=this;
        button3=(ImageButton) findViewById(R.id.button3);
        button4=(ImageButton) findViewById(R.id.button4);
        button5=(ImageButton) findViewById(R.id.button5);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, ReporteDeVenta.class);
                startActivity(i);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, ReporteDeVentasFechas.class);
                startActivity(i);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, CostoVenta.class);
                startActivity(i);
            }
        });


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

    @Override
    public void Resultado(String Id) {
        if(Id=="TIENDA DENGUI"){
            final SharedPreferences sharedPreferences=getSharedPreferences("Datos2", context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Almacen","1");
            editor.putString("nomAlmacen", "11");
            editor.apply();
            Intent cambiar = new Intent(this, ProductosVendidosMensual.class);
            startActivity(cambiar);
        }else if(Id=="TIENDA NOPALA"){
            final SharedPreferences sharedPreferences=getSharedPreferences("Datos2", context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Almacen","1");
            editor.putString("nomAlmacen", "9");
            editor.apply();
            Intent cambiar = new Intent(this, ProductosVendidosMensual.class);
            startActivity(cambiar);
        }else if(Id=="ALMACEN DENGUI"){
            final SharedPreferences sharedPreferences=getSharedPreferences("Datos2", context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Almacen","1");
            editor.putString("nomAlmacen", "1");
            editor.apply();
            Intent cambiar = new Intent(this, ProductosVendidosMensual.class);
            startActivity(cambiar);
        }else if(Id=="TIENDA 61"){
            final SharedPreferences sharedPreferences=getSharedPreferences("Datos2", context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Almacen","2");
            editor.putString("nomAlmacen", "1");
            editor.apply();
            Intent cambiar = new Intent(this, ProductosVendidosMensual.class);
            startActivity(cambiar);
        }else if(Id=="LAGUNAS OAXACA"){
            final SharedPreferences sharedPreferences=getSharedPreferences("Datos2", context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Almacen","3");
            editor.putString("nomAlmacen", "4");
            editor.apply();
            Intent cambiar = new Intent(this, ProductosVendidosMensual.class);
            startActivity(cambiar);
        }else if(Id=="SANTO DOMINGO"){
            final SharedPreferences sharedPreferences=getSharedPreferences("Datos2", context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Almacen","4");
            editor.putString("nomAlmacen", "4");
            editor.apply();
            Intent cambiar = new Intent(this, ProductosVendidosMensual.class);
            startActivity(cambiar);
        }else if(Id=="MATIAS ROMERO"){
            final SharedPreferences sharedPreferences=getSharedPreferences("Datos2", context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Almacen","3");
            editor.putString("nomAlmacen", "10");
            editor.apply();
            Intent cambiar = new Intent(this, ProductosVendidosMensual.class);
            startActivity(cambiar);
        }
    }
    }
