package com.casacru.casacruapp.Almacen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.casacru.casacruapp.Dialog.DialogSelectAlmacen;
import com.casacru.casacruapp.R;


public class InicioInventario extends AppCompatActivity {
    private ImageButton btnInventario, btnReportes;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_inventario);
        context=this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnInventario=(ImageButton) findViewById(R.id.button5);
        btnReportes=(ImageButton) findViewById(R.id.button6);
        btnInventario.setOnClickListener(Onclick);
        btnReportes.setOnClickListener(Onclick);
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

    private View.OnClickListener Onclick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button5:
                    Intent i=new Intent(context, ListadoInventario.class);
                    startActivity(i);
                    break;
                case R.id.button6:
                    Intent l=new Intent(context,ReportesInventarios.class);
                    startActivity(l);
            }
        }
    };

}