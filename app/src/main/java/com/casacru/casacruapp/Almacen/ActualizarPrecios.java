package com.casacru.casacruapp.Almacen;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.casacru.casacruapp.Adaptador.AdaptadorPrecios;
import com.casacru.casacruapp.Dialog.DialogError;
import com.casacru.casacruapp.Dialog.DialogSelectAlmacen;
import com.casacru.casacruapp.Interfaces.IActualizarPrecios;
import com.casacru.casacruapp.Modelo.ModeloGrupos;
import com.casacru.casacruapp.Modelo.ModeloPrecios;
import com.casacru.casacruapp.R;
import com.casacru.casacruapp.SQLite.DatabaseHelper;
import com.casacru.casacruapp.SQLite.DatabaseOpenHelper;
import com.casacru.casacruapp.TareasAsync.TareaActualizarPrecios;
import com.casacru.casacruapp.TareasAsync.TareaReporteMaxMin;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ActualizarPrecios extends AppCompatActivity implements IActualizarPrecios, DialogSelectAlmacen.Finalizo
    {

    Spinner SPGrupo, SPProvee,SPSubGrupo,SPClass3,SPClass4,SPClass6;
    TextInputEditText txtUtilidad;
    Button btnMostrar,btnActualizar, btnFiltros,btnGFiltros;
    String IP, PUERTO,BD, CONTRASENA, IDAlmac, USUARIO, Grupo,nomgrup, proveedor, SubGrupo,Class3,Class4,Class6, idalmacen;
    String PartClass1,PartClass2,PartClass3, SegContable;
    ArrayList<ModeloPrecios> listaProductos = new ArrayList<>();
    ArrayList<String> listagrupos=new ArrayList<>();
    ArrayList<String> listaClass2=new ArrayList<>();
    ArrayList<String> listaClass3=new ArrayList<>();
    ArrayList<String> listaClass4=new ArrayList<>();
    ArrayList<String> listaClass6=new ArrayList<>();
    ArrayList<ModeloGrupos> listaSubgrupos=new ArrayList<ModeloGrupos>();
    ArrayList<ModeloGrupos> listaClass1 = new ArrayList<ModeloGrupos>();
    ArrayList<ModeloGrupos> listaC3 = new ArrayList<ModeloGrupos>();
    ArrayList<ModeloGrupos> listaC4 = new ArrayList<ModeloGrupos>();
    ArrayList<ModeloGrupos> listaC5 = new ArrayList<ModeloGrupos>();
    ArrayList<ModeloGrupos> listaC6 = new ArrayList<ModeloGrupos>();
    ArrayList<String> ListaFiltro=new ArrayList<>();
    ArrayList<String> listProvee = new ArrayList<String>();
    RecyclerView listView;
    CheckBox ChGrupo,ChSubGrupo,ChClass3,ChClass4,ChProveedor,ChClass6;
    LinearLayout lyFiltro;
    ProgressBar pbCarga;
        Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_actualizar_precios);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
          context=this;
            new DialogSelectAlmacen(context, ActualizarPrecios.this);
    lyFiltro = (LinearLayout) findViewById(R.id.LyFiltros);
    btnActualizar = (Button) findViewById(R.id.btnActualizar);
    txtUtilidad = (TextInputEditText) findViewById(R.id.txtutilidad);
    listView = (RecyclerView) findViewById(R.id.Listaproduc);
    listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    btnMostrar = (Button) findViewById(R.id.btnMostrar);
    SPGrupo = (Spinner) findViewById(R.id.spinnerGrupo);
    SPProvee = (Spinner) findViewById(R.id.spinnerProveedor);
    SPSubGrupo = (Spinner) findViewById(R.id.spinnerSubGrupo);
    SPClass3 = (Spinner) findViewById(R.id.spinnerClass3);
    SPClass4 = (Spinner) findViewById(R.id.spinnerClass4);
    SPClass6 = (Spinner) findViewById(R.id.spinnerClass6);
    pbCarga=(ProgressBar)findViewById(R.id.PBCarga);
    ChGrupo = (CheckBox) findViewById(R.id.CbGrupo);
    ChSubGrupo = (CheckBox) findViewById(R.id.CbSubGrupo);
    ChClass3 = (CheckBox) findViewById(R.id.CbClass3);
    ChProveedor = (CheckBox) findViewById(R.id.CbProveedor);
    ChClass4 = (CheckBox) findViewById(R.id.CbClass4);
    ChClass6 = (CheckBox) findViewById(R.id.CbClass6);
    btnFiltros = (Button) findViewById(R.id.btnFiltros);
    btnGFiltros = (Button) findViewById(R.id.btnGuardFiltros);
    btnGFiltros.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (lyFiltro.getVisibility() == VISIBLE) {
                lyFiltro.setVisibility(GONE);
                AccionesCheckbox();
            }
        }
    });
    btnFiltros.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (lyFiltro.getVisibility() == GONE) {
                lyFiltro.setVisibility(View.VISIBLE);
                btnFiltros.setVisibility(GONE);
            }
        }
    });
    btnMostrar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            listView.removeAllViewsInLayout();
            listaProductos.clear();
            new TareaActualizarPrecios(conexion2(),ActualizarPrecios.this,IDAlmac).execute(Filtro(),txtUtilidad.getText().toString());
        }
    });
    btnActualizar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            pbCarga.setVisibility(VISIBLE);
            ActualizarPrecios2();
            pbCarga.setVisibility(GONE);
        }

    });
            SPGrupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Grupo = listaClass1.get(position).getIdGrupo();
                    nomgrup= listaClass1.get(position).getNomGrupo();
                    if(Grupo!="000"){
                        buscarSeg();
                    }
                    Toast.makeText(getApplicationContext(), listaClass1.get(position).getNomGrupo(), Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            SPProvee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    proveedor = listaC5.get(position).getIdGrupo();
                    Toast.makeText(getApplicationContext(), listaC5.get(position).getNomGrupo(), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            SPSubGrupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    SubGrupo = listaSubgrupos.get(position).getIdGrupo();
                    Toast.makeText(getApplicationContext(), listaSubgrupos.get(position).getNomGrupo(), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            SPClass3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Class3 = listaC3.get(position).getIdGrupo();
                    Toast.makeText(getApplicationContext(), listaC3.get(position).getNomGrupo(), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            SPClass4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Class4 = listaC4.get(position).getIdGrupo();
                    Toast.makeText(getApplicationContext(), listaC4.get(position).getNomGrupo(), Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            SPClass6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Class6 = listaC6.get(position).getIdGrupo();
                    Toast.makeText(getApplicationContext(), listaC6.get(position).getNomGrupo(), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
}catch (Exception ex){
            new DialogError(context,"");
}}

        /**
         * Metodo para cargar la barra de menu dependiendo de una plantilla asignada
         * @param menu
         * @return
         */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu3, menu);

        return super.onCreateOptionsMenu(menu);
    }

        /**
         * Este metodo se encarga de las acciones que se realizaran al dar clic en alguna opcion
         * dentro de la barra de menu
         * @param item
         * @return
         */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            case R.id.item2:
                Intent intent=new Intent(this, Segmentos_contables.class);
                startActivity(intent);
                break;

            case R.id.item3:
                if (lyFiltro.getVisibility() == GONE) {
                    lyFiltro.setVisibility(View.VISIBLE);
                }else{
                    lyFiltro.setVisibility(GONE);
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

        /**
         * Este metodo se encarga de buscar los parametros de conexion para DENGUI
         * @param id
         */
    public void buscar(String id) {
        try {
            DatabaseHelper dbh = new DatabaseHelper(this, "Conexion", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();

            Cursor fila = database.rawQuery
                    ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id='"+id+"'", null);

            if (fila.moveToFirst()) {
                this.IP = (fila.getString(0));
                this.PUERTO = (fila.getString(1));
                this.CONTRASENA = (fila.getString(2));
                this.BD = (fila.getString(3));
                this.IDAlmac = (fila.getString(4));
                this.USUARIO = fila.getString(5);
                database.close();
                Toast.makeText(this, "Conectado a: " + IP + "," + PUERTO, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }
    }

        /**
         * Este metodo se encarga de realizar la conexion
         * @return la conexion al servidor
         */
    public Connection conexion2() {
        Connection conexion2 = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

            conexion2 = DriverManager.getConnection("jdbc:jtds:sqlserver://" + this.IP + ":" + this.PUERTO + ";databaseName=" + this.BD + ";user="+this.USUARIO+";password=" + this.CONTRASENA + ";");

        } catch (Exception e) {
            new DialogError(context,"");
        }
        return conexion2;
    }

        /**
         * Este metodo se encarga de cargar la lista de los productos que necesitan actualizar precio
         *
         * @param array que proviene de la tarea async llamada TareaActualizarPrecio
         */
        @Override
    public void ObtenerProductos(ArrayList<ModeloPrecios> array){
    try {
        for(int i=0; i<array.size();i++){
            listaProductos.add(new ModeloPrecios(array.get(i).getDescripcion(),array.get(i).getPrecio2(),array.get(i).getIdproducto(),array.get(i).getPrecioCalculado(),array.get(i).getPrecioSinIva()));
        }

       AdaptadorPrecios adapter=new AdaptadorPrecios(listaProductos);
        listView.setAdapter(adapter);
        Toast.makeText(getApplicationContext(),"LISTO",Toast.LENGTH_SHORT).show();
    } catch (Exception e) {
        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

    }
}

        /**
         * Este metodo se encarga de mostrar y esconder el progressbar
         * @param status del proceso TRUE O FALSE
         */
        @Override
    public void proggresbarr(boolean status) {
            if(status){
                btnMostrar.setEnabled(false);
                pbCarga.setVisibility(VISIBLE);
            }else{
                btnMostrar.setEnabled(true);
                pbCarga.setVisibility(GONE);
            }
        }

        /**
         * Este metodo se encarga de llenar la lista desplegable o Spinner
         * con los datos de la clasificacion 1
         */
    public void llenarSpinner(){
        try {
            listaClass1.add(new ModeloGrupos("000","*","SELECCIONA UN GRUPO"));

            Statement stm = conexion2().createStatement();

            String consulta="SELECT CIDVALORCLASIFICACION,CCODIGOVALORCLASIFICACION,CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDCLASIFICACION='"+25+"'";

            ResultSet rs = stm.executeQuery(consulta);

            while (rs.next()) {

                listaClass1.add(new ModeloGrupos(String.valueOf(rs.getString(1)),rs.getString(2), rs.getString(3)));

            }


            for(int i=0;i<listaClass1.size();i++){
                listagrupos.add(listaClass1.get(i).getCodGrupo()+" - "+listaClass1.get(i).getNomGrupo());
            }

        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(),"Error spinner Grupos", Toast.LENGTH_SHORT).show();
        }
    }

        /**
         * Este metodo se encarga de llenar la lista desplegable o Spinner
         * con los datos de la clasificacion 2
         */
    public void llenarSubGrupo(){
        try {
            listaSubgrupos.add(new ModeloGrupos("000","*","SELECCIONA UN SUBGRUPO"));

            Statement stm = conexion2().createStatement();
            String consulta="SELECT CIDVALORCLASIFICACION,CCODIGOVALORCLASIFICACION,CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDCLASIFICACION='"+26+"'";
            ResultSet rs = stm.executeQuery(consulta);

            while (rs.next()) {
                listaSubgrupos.add(new ModeloGrupos(String.valueOf(rs.getString(1)),rs.getString(2), rs.getString(3)));
            }

            for(int i=0;i<listaSubgrupos.size();i++){
                listaClass2.add(listaSubgrupos.get(i).getCodGrupo()+" - "+listaSubgrupos.get(i).getNomGrupo());
            }

        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "Error spinner Sub Grupos", Toast.LENGTH_SHORT).show();
        }
    }

        /**
         * Este metodo se encarga de llenar la lista desplegable o Spinner
         * con los datos de la clasificacion 3
         */
    public void llenarClass3(){
        try {
            listaC3.add(new ModeloGrupos("000","sss","SELECCIONA UNA OPCIÓN"));

            Statement stm = conexion2().createStatement();
            String consulta="SELECT CIDVALORCLASIFICACION,CCODIGOVALORCLASIFICACION,CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDCLASIFICACION='"+27+"'";
            ResultSet rs = stm.executeQuery(consulta);

            while (rs.next()) {
                listaC3.add(new ModeloGrupos(String.valueOf(rs.getString(1)),rs.getString(2), rs.getString(3)));
            }

            for(int i=0;i<listaC3.size();i++){
                listaClass3.add(listaC3.get(i).getCodGrupo()+" - "+listaC3.get(i).getNomGrupo());
            }

        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "Error spinner class3", Toast.LENGTH_SHORT).show();
        }
    }

        /**
         * Este metodo se encarga de llenar la lista desplegable o Spinner
         * con los datos de la clasificacion 4
         */
    public void llenarClass4(){
        try {
            listaC4.add(new ModeloGrupos("000","sss","SELECCIONA UNA OPCIÓN"));

            Statement stm = conexion2().createStatement();
            String consulta="SELECT CIDVALORCLASIFICACION,CCODIGOVALORCLASIFICACION,CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDCLASIFICACION='"+28+"'";
            ResultSet rs = stm.executeQuery(consulta);

            while (rs.next()) {
                listaC4.add(new ModeloGrupos(String.valueOf(rs.getString(1)),rs.getString(2), rs.getString(3)));
            }

            for(int i=0;i<listaC4.size();i++){
                listaClass4.add(listaC4.get(i).getCodGrupo()+" - "+listaC4.get(i).getNomGrupo());
            }

        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "Error spinner class 4", Toast.LENGTH_SHORT).show();
        }
    }

        /**
         * Este metodo se encarga de llenar la lista desplegable o Spinner
         * con los datos de la clasificacion 5
         */
    public void llenarSpinnerProvee(){
        try {
            listaC5.add(new ModeloGrupos("000","sss","SELECCIONA UN PROVEEDOR"));

            Statement stm = conexion2().createStatement();
            String consulta="SELECT CIDVALORCLASIFICACION,CCODIGOVALORCLASIFICACION,CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDCLASIFICACION='"+29+"'";
            ResultSet rs = stm.executeQuery(consulta);

            while (rs.next()) {
                listaC5.add(new ModeloGrupos(String.valueOf(rs.getString(1)),rs.getString(2), rs.getString(3)));
            }

            for(int i=0;i<listaC5.size();i++){
                listProvee.add(listaC5.get(i).getNomGrupo());
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(),"error en spinner proveedor", Toast.LENGTH_SHORT).show();
        }
    }

        /**
         * Este metodo se encarga de llenar la lista desplegable o Spinner
         * con los datos de la clasificacion 6
         */
    public void llenarClass6(){
        try {
            listaC6.add(new ModeloGrupos("000","sss","SELECCIONA UNA OPCIÓN"));

            Statement stm = conexion2().createStatement();
            String consulta="SELECT CIDVALORCLASIFICACION,CCODIGOVALORCLASIFICACION,CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDCLASIFICACION='"+30+"'";
            ResultSet rs = stm.executeQuery(consulta);

            while (rs.next()) {
                listaC6.add(new ModeloGrupos(String.valueOf(rs.getString(1)),rs.getString(2), rs.getString(3)));
            }

            for(int i=0;i<listaC6.size();i++){
                listaClass6.add(listaC6.get(i).getCodGrupo()+" - "+listaC6.get(i).getNomGrupo());
            }

        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

        /**
         * Este metodo se encrarga de realizar la actualizacion de los precios
         * mediante el recorrido de la lista.
         */
    public void ActualizarPrecios2(){
        pbCarga.setVisibility(VISIBLE);
        try{
            for(int i=0;i<=listaProductos.size()-1;i++){

                boolean Res= false;
                Res=listaProductos.get(i).getSelected();

                if(Res){
                    if(SegContable!="null"){
                        String consultaUpdate="UPDATE admProductos SET CSEGCONTPRODUCTO1='"+SegContable+"', CPRECIO2='"+listaProductos.get(i).getPrecioCalculado()+"', CPRECIO1='"+listaProductos.get(i).getPrecioSinIva()+"' WHERE CIDPRODUCTO='"+listaProductos.get(i).getIdproducto()+"'";
                        PreparedStatement rsk = conexion2().prepareStatement(consultaUpdate);
                        rsk.executeUpdate();
                    }else{
                        String consultaUpdate="UPDATE admProductos SET CPRECIO2='"+listaProductos.get(i).getPrecioCalculado()+"', CPRECIO1='"+listaProductos.get(i).getPrecioSinIva()+"' WHERE CIDPRODUCTO='"+listaProductos.get(i).getIdproducto()+"'";
                        PreparedStatement rsk = conexion2().prepareStatement(consultaUpdate);
                        rsk.executeUpdate();
                    }


                }

            }
            pbCarga.setVisibility(GONE);
            Toast.makeText(getApplicationContext(), "!!Listo precios actualizados¡¡",Toast.LENGTH_SHORT).show();

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
            pbCarga.setVisibility(GONE);
        }

    }

        /**
         * Este metodo muestra o esconde los Spinners o listas desplegables dependiendo de
         * que checknox hayas marcado.
         */
    public void AccionesCheckbox(){
        ListaFiltro.clear();

        if(ChProveedor.isChecked()){
            SPProvee.setVisibility(VISIBLE);
        ListaFiltro.add("CIDVALORCLASIFICACION5");
        }else{
            SPProvee.setVisibility(GONE);
        }

        if(ChClass3.isChecked()){
            SPClass3.setVisibility(VISIBLE);
            ListaFiltro.add("CIDVALORCLASIFICACION3");
        }else{
            SPClass3.setVisibility(GONE);
        }

        if(ChClass4.isChecked()){
            SPClass4.setVisibility(VISIBLE);
            ListaFiltro.add("CIDVALORCLASIFICACION4");
        }else{
            SPClass4.setVisibility(GONE);
        }

        if(ChClass6.isChecked()){
            SPClass6.setVisibility(VISIBLE);
            ListaFiltro.add("CIDVALORCLASIFICACION6");
        }else{
            SPClass6.setVisibility(GONE);
        }

        if(ChSubGrupo.isChecked()){
            SPSubGrupo.setVisibility(VISIBLE);
            ListaFiltro.add("CIDVALORCLASIFICACION2");
        }else{
            SPSubGrupo.setVisibility(GONE);
        }

        if(ChGrupo.isChecked()){
            SPGrupo.setVisibility(VISIBLE);
            ListaFiltro.add("CIDVALORCLASIFICACION1");
        }else{
            SPGrupo.setVisibility(GONE);
        }
    }

        /**
         * Este metodo crea la cadena de consulta dependiendo de cuantos y cuales clasificaciones se hayan
         * seleccionado para el filtrado
         * @return La cadena de consulta
         */
    public String Filtro() {
try {
    String Consulta = "";

    if (ListaFiltro.size() == 1) {

        PartClass1 = "SELECT CNOMBREPRODUCTO,CPRECIO2,CIDPRODUCTO FROM admProductos WHERE " + ListaFiltro.get(0) + "=";

        if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION1") {
            Consulta = PartClass1 + "'" + Grupo + "'";
        } else if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION2") {
            Consulta = PartClass1 + "'" + SubGrupo + "'";
        } else if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION3") {
            Consulta = PartClass1 + "'" + Class3 + "'";
        } else if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION4") {
            Consulta = PartClass1 + "'" + Class4 + "'";
        } else if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION5") {
            Consulta = PartClass1 + "'" + proveedor + "'";
        } else if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION6") {
            Consulta = PartClass1 + "'" + Class6 + "'";
        }
        return Consulta;
    }

    if (ListaFiltro.size() == 2) {

        PartClass1 = "SELECT CNOMBREPRODUCTO,CPRECIO2,CIDPRODUCTO FROM admProductos WHERE " + ListaFiltro.get(0) + "=";
        PartClass2 = " AND " + ListaFiltro.get(1) + "=";

        if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION1") {
            Consulta = PartClass1 + "'" + Grupo + "'";
        } else if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION2") {
            Consulta = PartClass1 + "'" + SubGrupo + "'";
        } else if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION3") {
            Consulta = PartClass1 + "'" + Class3 + "'";
        } else if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION4") {
            Consulta = PartClass1 + "'" + Class4 + "'";
        } else if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION5") {
            Consulta = PartClass1 + "'" + proveedor + "'";
        } else if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION6") {
            Consulta = PartClass1 + "'" + Class6 + "'";
        }

        if (ListaFiltro.get(1) == "CIDVALORCLASIFICACION1") {
            Consulta += PartClass2 + "'" + Grupo + "'";
        } else if (ListaFiltro.get(1) == "CIDVALORCLASIFICACION2") {
            Consulta += PartClass2 + "'" + SubGrupo + "'";
        } else if (ListaFiltro.get(1) == "CIDVALORCLASIFICACION3") {
            Consulta += PartClass2 + "'" + Class3 + "'";
        } else if (ListaFiltro.get(1) == "CIDVALORCLASIFICACION4") {
            Consulta += PartClass2 + "'" + Class4 + "'";
        } else if (ListaFiltro.get(1) == "CIDVALORCLASIFICACION5") {
            Consulta += PartClass2 + "'" + proveedor + "'";
        } else if (ListaFiltro.get(1) == "CIDVALORCLASIFICACION6") {
            Consulta += PartClass2 + "'" + Class6 + "'";
        }
        return Consulta;
    }


    if (ListaFiltro.size() == 3) {
        PartClass1 = "SELECT CNOMBREPRODUCTO,CPRECIO2,CIDPRODUCTO FROM admProductos WHERE " + ListaFiltro.get(0) + "=";
        PartClass2 = " AND " + ListaFiltro.get(1) + "=";
        PartClass3 = " AND " + ListaFiltro.get(2) + "=";

        if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION1") {
            Consulta = PartClass1 + "'" + Grupo + "'";
        } else if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION2") {
            Consulta = PartClass1 + "'" + SubGrupo + "'";
        } else if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION3") {
            Consulta = PartClass1 + "'" + Class3 + "'";
        } else if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION4") {
            Consulta = PartClass1 + "'" + Class4 + "'";
        } else if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION5") {
            Consulta = PartClass1 + "'" + proveedor + "'";
        } else if (ListaFiltro.get(0) == "CIDVALORCLASIFICACION6") {
            Consulta = PartClass1 + "'" + Class6 + "'";
        }

        if (ListaFiltro.get(1) == "CIDVALORCLASIFICACION1") {
            Consulta +=PartClass2 + "'" + Grupo + "'";
        } else if (ListaFiltro.get(1) == "CIDVALORCLASIFICACION2") {
            Consulta +=  PartClass2 + "'" + SubGrupo + "'";
        } else if (ListaFiltro.get(1) == "CIDVALORCLASIFICACION3") {
            Consulta +=PartClass2 + "'" + Class3 + "'";
        } else if (ListaFiltro.get(1) == "CIDVALORCLASIFICACION4") {
            Consulta += PartClass2 + "'" + Class4 + "'";
        } else if (ListaFiltro.get(1) == "CIDVALORCLASIFICACION5") {
            Consulta += PartClass2 + "'" + proveedor + "'";
        } else if (ListaFiltro.get(1) == "CIDVALORCLASIFICACION6") {
            Consulta += PartClass2 + "'" + Class6 + "'";
        }

        if (ListaFiltro.get(2) == "CIDVALORCLASIFICACION1") {
            Consulta +=  PartClass3 + "'" + Grupo + "'";
        } else if (ListaFiltro.get(2) == "CIDVALORCLASIFICACION2") {
            Consulta +=  PartClass3 + "'" + SubGrupo + "'";
        } else if (ListaFiltro.get(2) == "CIDVALORCLASIFICACION3") {
            Consulta +=  PartClass3 + "'" + Class3 + "'";
        } else if (ListaFiltro.get(2) == "CIDVALORCLASIFICACION4") {
            Consulta +=   PartClass3 + "'" + Class4 + "'";
        } else if (ListaFiltro.get(2) == "CIDVALORCLASIFICACION5") {
            Consulta +=  PartClass3 + "'" + proveedor + "'";
        } else if (ListaFiltro.get(2) == "CIDVALORCLASIFICACION6") {
            Consulta +=   PartClass3 + "'" + Class6 + "'";
        }
        return Consulta;
    }
    return Consulta;
}catch (Exception ex){
    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
    return "ERROR";

}
}

        /**
         * Este metodo busca la clave de segmento contable con la que esten registradas cada una de las
         * clasificaciones seleccionadas.
         */
    public void buscarSeg() {
       try {
           DatabaseOpenHelper dbh = new DatabaseOpenHelper(this, "Segmentos", null, 1);
           SQLiteDatabase database = dbh.getWritableDatabase();

           Cursor fila = database.rawQuery
                   ("select CodSegmento from segmentoscontables where NomGrupo='" + nomgrup + "'", null);

           if (fila.moveToFirst()) {
               SegContable = fila.getString(0);
               database.close();
               Toast.makeText(this, SegContable, Toast.LENGTH_SHORT).show();
           } else {
               SegContable="null";

           }
       }catch (Exception ex){
           Toast.makeText(this, "error: "+ex.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }

        /**
         * Este metodo Realiza una correccion con los datos de conexion
         * @param Id
         */
        @Override
    public void Resultado(String Id) {
            if (Id.equals("TIENDA DENGUI")) {
                buscar("1");
                IDAlmac = "11";
            } else if (Id.equals("TIENDA NOPALA")) {
                buscar("1");
                IDAlmac = "12";
            } else if (Id.equals("ALMACEN DENGUI")) {
                buscar("1");
                IDAlmac = "1";
            } else if (Id.equals("TIENDA 61")) {
                buscar("2");
                IDAlmac = "1";
                BD = "adCASACRU61";
            } else if (Id.equals("LAGUNAS OAXACA")) {
                buscar("3");
                IDAlmac = "4";
            } else if (Id.equals("SANTO DOMINGO")) {
                buscar("4");
                IDAlmac = "4";
                BD = "adSANTODOMINGO";
            } else if (Id.equals("MATIAS ROMERO")) {
                buscar("3");
                IDAlmac = "10";
            }
            try {
                llenarSpinner();
                llenarSpinnerProvee();
                llenarSubGrupo();
                llenarClass3();
                llenarClass4();
                llenarClass6();
                ArrayAdapter<CharSequence> adaptadorGrupos = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listagrupos);
                SPGrupo.setAdapter(adaptadorGrupos);
                ArrayAdapter<CharSequence> adaptadorSubGrupos = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaClass2);
                SPSubGrupo.setAdapter(adaptadorSubGrupos);
                ArrayAdapter<CharSequence> adaptadorClass3 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaClass3);
                SPClass3.setAdapter(adaptadorClass3);
                ArrayAdapter<CharSequence> adaptadorClass4 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaClass4);
                SPClass4.setAdapter(adaptadorClass4);
                ArrayAdapter<CharSequence> adaptadorClass6 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaClass6);
                SPClass6.setAdapter(adaptadorClass6);
                ArrayAdapter<CharSequence> adaptadorProveedor = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listProvee);
                SPProvee.setAdapter(adaptadorProveedor);
            }catch (Exception e){
                new DialogError(context,"");
            }
        }
    }
