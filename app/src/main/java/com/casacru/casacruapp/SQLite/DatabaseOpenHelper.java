package com.casacru.casacruapp.SQLite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    String tabla="create table productos(Folio text, almacen text, codigo text, nombre text, contado text, actual text, unidad text,costo text)";
    String tabla2="create table folios(Folio text, Almacen text)";
    String tabla3="create table segmentoscontables(CodSegmento text, CodGrupo text PRIMARY KEY, NomGrupo text)";
    String tabla4="create table listado(codigo text PRIMARY KEY,nombre text)";
    String tabla5="create table productoslistado(codigo text primary key, nombre text, unidad text, existencia text,ultimocosto text)";



    public DatabaseOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public DatabaseOpenHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(tabla);
        db.execSQL(tabla2);
        db.execSQL(tabla3);
        db.execSQL(tabla4);
        db.execSQL(tabla5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {

    }
    public void borrarRegistros(SQLiteDatabase db) {
        db.execSQL("DELETE FROM productos");

}
}

