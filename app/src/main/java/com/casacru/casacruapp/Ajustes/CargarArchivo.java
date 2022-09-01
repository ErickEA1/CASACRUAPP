package com.casacru.casacruapp.Ajustes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.casacru.casacruapp.SQLite.DatabaseOpenHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import androidx.appcompat.app.AppCompatActivity;

public class CargarArchivo extends AppCompatActivity {


    public void inicializar(Context context,Uri dato){
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


    public void Eliminar_tabla(Context context){
        DatabaseOpenHelper baseHelper = new DatabaseOpenHelper(context, "PRODUCTOS", null, 1);
        SQLiteDatabase db = baseHelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM productos");
        }catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
    }

    public void Eliminar_tabla2(Context context){
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



