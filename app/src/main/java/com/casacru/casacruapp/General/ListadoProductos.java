package com.casacru.casacruapp.General;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.casacru.casacruapp.Adaptador.ListadoAdapter;
import com.casacru.casacruapp.Almacen.Existencias;
import com.casacru.casacruapp.Almacen.Inventarios;
import com.casacru.casacruapp.Almacen.Ubicaciones;
import com.casacru.casacruapp.Almacen.VendidosMensual;
import com.casacru.casacruapp.Compras.Solicitudes;
import com.casacru.casacruapp.Modelo.ModeloProductos;
import com.casacru.casacruapp.R;
import com.casacru.casacruapp.SQLite.DatabaseHelper;
import com.casacru.casacruapp.SQLite.DatabaseOpenHelper;
import com.casacru.casacruapp.Almacen.ProductosVendidosMensual;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ListadoProductos extends AppCompatActivity implements SearchView.OnQueryTextListener {
    SearchView txtBuscar;
    ArrayList<ModeloProductos> listaproductos = new ArrayList<>();
    RecyclerView listaP;
    ListadoAdapter adapter;
    String IP, PUERTO,BD, CONTRASENA, IDAlmac, USUARIO="sa";
    Context context;
    String Param;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_productos);
       try {
           Intent val=getIntent();
           Param=val.getStringExtra("Param");
           buscar();
           context=this;
           listaP = (RecyclerView) findViewById(R.id.listaproductos);
           txtBuscar = (SearchView) findViewById(R.id.txtBuscar);
           listaP.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
           buscard();
           txtBuscar.setOnQueryTextListener(this);
       }catch (Exception ex){
           Toast.makeText(this, "error: "+ ex.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }


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

        }else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
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

    public void buscard() {
        try {
            DatabaseOpenHelper dbh = new DatabaseOpenHelper(this, "Listado", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();

            Cursor fila = database.rawQuery
                    ("select codigo,nombre from productoslistado", null);


            listaP.removeAllViewsInLayout();
            listaproductos.clear();
            if (fila.moveToFirst()) {

                do{
                    listaproductos.add(new ModeloProductos(fila.getString(0), fila.getString(1)));

                    database.close();
                }while (fila.moveToNext());
            }else{
                ObtenerProductos();
            }
            adapter=new ListadoAdapter(listaproductos, this, new ListadoAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ModeloProductos item) {
                    switch (Param){
                        case "2" :
                            Intent intent=new Intent(context, Existencias.class);
                            intent.putExtra("Elemento", item);
                            startActivity(intent);
                            finish();
                            break;

                        case "1":
                            Intent d=new Intent(context, Ubicaciones.class);
                            d.putExtra("Elemento", item);
                            startActivity(d);
                            finish();
                            break;
                        case "3":
                            Intent l=new Intent(context, Solicitudes.class);
                            l.putExtra("Elemento", item);
                            startActivity(l);
                            finish();
                            break;
                        case "4":
                            Intent i=new Intent(context, Inventarios.class);
                            i.putExtra("Elemento", item);
                            startActivity(i);
                            finish();
                            break;
                        case "5":
                            Intent p=new Intent(context, VendidosMensual.class);
                            p.putExtra("Elemento", item);
                            startActivity(p);
                            finish();
                            break;

                    }

                }
            });

            listaP.setAdapter(adapter);

        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void ObtenerProductos()
    {
        try {
            listaP.removeAllViewsInLayout();
            listaproductos.clear();

            String Consulta="SELECT CCODIGOPRODUCTO,CNOMBREPRODUCTO FROM admProductos";
            Statement stm3 = conexion2().createStatement();
            ResultSet rs = stm3.executeQuery(Consulta);

            while (rs.next()) {
                listaproductos.add(new ModeloProductos(rs.getString(1), rs.getString(2)));
            }

           adapter=new ListadoAdapter(listaproductos, this, new ListadoAdapter.OnItemClickListener() {
               @Override
               public void onItemClick(ModeloProductos item) {
                   switch (Param){
                       case "2" :
                           Intent intent=new Intent(context, Existencias.class);
                           intent.putExtra("Elemento", item);
                           startActivity(intent);
                           finish();
                           break;

                       case "1":
                           Intent d=new Intent(context, Ubicaciones.class);
                           d.putExtra("Elemento", item);
                           startActivity(d);
                           finish();
                           break;
                       case "3":
                           Intent l=new Intent(context, Solicitudes.class);
                           l.putExtra("Elemento", item);
                           startActivity(l);
                           finish();
                           break;
                       case "4":
                           Intent i=new Intent(context, Inventarios.class);
                           i.putExtra("Elemento", item);
                           startActivity(i);
                           finish();
                           break;
                       case "5":
                           Intent p=new Intent(context, VendidosMensual.class);
                           p.putExtra("Elemento", item);
                           startActivity(p);
                           finish();
                           break;

                   }

                   }
           });

            listaP.setAdapter(adapter);
            Toast.makeText(getApplicationContext(),"LISTO",Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filtrado(newText);

        return false;

    }

}