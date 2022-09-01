package com.casacru.casacruapp.General;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.casacru.casacruapp.Ajustes.menuAjustes;
import com.casacru.casacruapp.Almacen.Inicio;
import com.casacru.casacruapp.Compras.InicioCompras;
import com.casacru.casacruapp.R;
import com.casacru.casacruapp.Ventas.InicioVentas;


public class Home extends AppCompatActivity {
int requestcode=200;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        verificarPermisos();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Intent inicio = new Intent(this, menuAjustes.class);
                startActivity(inicio);

        }
        return super.onOptionsItemSelected(item);
    }

    public void CambiarAlmacen(View view) {
        Intent cambiar = new Intent(this, Inicio.class);
        startActivity(cambiar);
    }
    public void CambiarCompras(View view) {
        Intent cambiar = new Intent(this, InicioCompras.class);
        startActivity(cambiar);
    }
    public void CambiarVentas(View v){
        Intent i=new Intent(this, InicioVentas.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verificarPermisos(){
        int perm=ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(perm==PackageManager.PERMISSION_GRANTED){

        }else{
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},requestcode);
        }
    }
}