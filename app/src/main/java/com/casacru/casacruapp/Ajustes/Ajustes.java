package com.casacru.casacruapp.Ajustes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.casacru.casacruapp.R;
import com.casacru.casacruapp.SQLite.DatabaseHelper;


public class Ajustes extends AppCompatActivity {
    private EditText IP, PUERTO, USUARIO, CONTRASEÑA, BD, idAlma;
    private RadioButton rbDengui,rbLagunas,rb61,rbSD;
    private Button btnGuardar;
    private Ajustes context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
try {
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    context=this;
    btnGuardar=findViewById(R.id.btnNew);
    rbDengui=(RadioButton) findViewById(R.id.radioButtonDengui);
    rb61=(RadioButton) findViewById(R.id.radioButton61);
    rbLagunas=(RadioButton) findViewById(R.id.radioButtonLagunas);
    rbSD=(RadioButton) findViewById(R.id.radioButtonMatiasR);

    IP = (EditText) findViewById(R.id.txtipn);
    PUERTO = (EditText) findViewById(R.id.txtpuerto1);
    USUARIO = (EditText) findViewById(R.id.txtUsuario);
    CONTRASEÑA = (EditText) findViewById(R.id.txtContraseña);
    BD = (EditText) findViewById(R.id.txtDB);
    idAlma = (EditText) findViewById(R.id.idAlmacen);

    btnGuardar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatabaseHelper dbh = new DatabaseHelper(context, "Conexion", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();
            if(rbDengui.isChecked()) {

                Cursor fila = database.rawQuery
                        ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id=1", null);

                if (fila.moveToFirst()) {
                    actualizarbd("1");
                    Toast.makeText(context, "Listo", Toast.LENGTH_SHORT).show();
                } else {
                    insert(1);
                }
            }else if(rb61.isChecked()) {

                Cursor fila = database.rawQuery
                        ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id=2", null);

                if (fila.moveToFirst()) {
                    actualizarbd("2");
                    Toast.makeText(context, "Listo", Toast.LENGTH_SHORT).show();
                } else {
                    insert(2);
                }
            }else if(rbLagunas.isChecked()) {

                Cursor fila = database.rawQuery
                        ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id=3", null);

                if (fila.moveToFirst()) {
                    actualizarbd("3");
                    Toast.makeText(context, "Listo", Toast.LENGTH_SHORT).show();
                } else {
                    insert(3);
                }
            }else if(rbSD.isChecked()) {

                Cursor fila = database.rawQuery
                        ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id=4", null);

                if (fila.moveToFirst()) {
                    actualizarbd("4");
                    Toast.makeText(context, "Listo", Toast.LENGTH_SHORT).show();
                } else {
                    insert(4);
                }
            }



        }
    });

    rb61.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Limpiar();
            buscard("2");
        }
    });
    rbDengui.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Limpiar();
            buscard("1");
        }
    });
    rbLagunas.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Limpiar();
            buscard("3");
        }
    });
    rbSD.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Limpiar();
            buscard("4");
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




    public void actualizarbd(String id){
        DatabaseHelper dbh = new DatabaseHelper(this, "Conexion", null, 1);
        SQLiteDatabase database = dbh.getWritableDatabase();
        String ip=IP.getText().toString();
        String puerto=PUERTO.getText().toString();
        String usuario=USUARIO.getText().toString();
        String contraseña=CONTRASEÑA.getText().toString();
        String bd=BD.getText().toString();
        String idalma=idAlma.getText().toString();
        if(database!=null){
            database.execSQL("UPDATE parametros SET ip='"+ip+"' , puerto='"+puerto+"', usuario='"+usuario+"', " +
                    "contraseña='"+contraseña+"', bd='"+bd+"', idAlmacen='"+idalma+"' where Id='"+id+"'");
            database.close();
        }
    }
    public void insert(int id){
        DatabaseHelper dbh = new DatabaseHelper(this, "Conexion", null, 1);
        SQLiteDatabase database = dbh.getWritableDatabase();

        String ip=IP.getText().toString();
        String puerto=PUERTO.getText().toString();
        String usuario=USUARIO.getText().toString();
        String contraseña=CONTRASEÑA.getText().toString();
        String bd=BD.getText().toString();
        String idalma=idAlma.getText().toString();

        ContentValues registro=new ContentValues();
        registro.put("Id",id);
        registro.put("ip",ip);
        registro.put("puerto",puerto);
        registro.put("usuario",usuario);
        registro.put("contraseña",contraseña);
        registro.put("bd",bd);
        registro.put("idAlmacen",idalma);

        database.insert("parametros",null, registro);
        database.close();
        Toast.makeText(this,"Se inserto",Toast.LENGTH_SHORT).show();
    }

    public void buscard(String id) {
      try {
          DatabaseHelper dbh = new DatabaseHelper(this, "Conexion", null, 1);
          SQLiteDatabase database = dbh.getWritableDatabase();

          Cursor fila = database.rawQuery
                  ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id='"+id+"'", null);

          if (fila.moveToFirst()) {
              IP.setText(fila.getString(0));
              PUERTO.setText(fila.getString(1));
              USUARIO.setText(fila.getString(5));
              CONTRASEÑA.setText(fila.getString(2));
              BD.setText(fila.getString(3));
              idAlma.setText(fila.getString(4));
              database.close();
          } else {
              Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
          }
      }catch (Exception ex){
          Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
      }
    }

public void Limpiar(){
        IP.setText("");
        PUERTO.setText("");
        USUARIO.setText("");
        CONTRASEÑA.setText("");
        BD.setText("");
        idAlma.setText("");
}
}