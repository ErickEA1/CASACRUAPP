package com.casacru.casacruapp.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DatabaseHelper extends SQLiteOpenHelper{

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase conexion) {
conexion.execSQL("create table parametros(Id INTEGER PRIMARY KEY,ip TEXT, puerto TEXT, usuario text, contraseña TEXT, bd TEXT, idAlmacen text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase conexion, int versionAntigua, int versionNueva) {

    }
}