package com.casacru.casacruapp.Almacen;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.casacru.casacruapp.Dialog.DialogError;
import com.casacru.casacruapp.Dialog.DialogSelectAlmacen;
import com.casacru.casacruapp.Interfaces.Comunicacion;
import com.casacru.casacruapp.Modelo.ModeloGrupos;
import com.casacru.casacruapp.Modelo.ModeloTablaMaxmin;
import com.casacru.casacruapp.R;
import com.casacru.casacruapp.SQLite.DatabaseHelper;
import com.casacru.casacruapp.SQLite.DatabaseOpenHelper;
import com.casacru.casacruapp.TareasAsync.TareaReporteMaxMin;
import com.google.android.material.textfield.TextInputEditText;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReporteMaxMin extends AppCompatActivity implements Comunicacion {
    private String IP, PUERTO, USUARIO, CONTRASEÑA, BD, IDAlmac,NomAlmacen,idBusqueda;
    private String mes,anio,idalma2,bd,IdProducto,CCODProd,CnomProd, Max, Grupo,nomgrup, proveedor, SubGrupo,Class3,Class4,Class6;
    private Context context;
    private Button btnMostrar,btnActualizar, btnFiltros,btnGFiltros,btnreport;
    private Spinner SPGrupo, SPProvee,SPSubGrupo,SPClass3,SPClass4,SPClass6;
    private String PartClass1,PartClass2,PartClass3;
    private ProgressBar progressBar2;
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
    CheckBox ChGrupo,ChSubGrupo,ChClass3,ChClass4,ChProveedor,ChClass6,Cbop1,Cbop2;
    LinearLayout lyFiltro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_max_min);
        try {
            context = this;
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ObtenerSharePreferences();
            buscard();
            Fecha();
            lyFiltro = (LinearLayout) findViewById(R.id.LyFiltros);
            btnMostrar = (Button) findViewById(R.id.btnMostrar);
            progressBar2=(ProgressBar) findViewById(R.id.progressBar2);
            SPGrupo = (Spinner) findViewById(R.id.spinnerGrupo);
            SPProvee = (Spinner) findViewById(R.id.spinnerProveedor);
            SPSubGrupo = (Spinner) findViewById(R.id.spinnerSubGrupo);
            SPClass3 = (Spinner) findViewById(R.id.spinnerClass3);
            SPClass4 = (Spinner) findViewById(R.id.spinnerClass4);
            SPClass6 = (Spinner) findViewById(R.id.spinnerClass6);
            ChGrupo = (CheckBox) findViewById(R.id.CbGrupo);
            ChSubGrupo = (CheckBox) findViewById(R.id.CbSubGrupo);
            ChClass3 = (CheckBox) findViewById(R.id.CbClass3);
            ChProveedor = (CheckBox) findViewById(R.id.CbProveedor);
            Cbop1=(CheckBox) findViewById(R.id.Cbop1);
            Cbop2=(CheckBox) findViewById(R.id.Cbop2);
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

                    try{
                        if(Cbop1.isChecked()) {
                            Toast.makeText(context, "Cargando...", Toast.LENGTH_SHORT).show();
                            //consultarIdProducto(IDAlmac);
                            new TareaReporteMaxMin(context,conexion2(),ReporteMaxMin.this).execute(Filtro(),IDAlmac,"<",3000);

                        }else if(Cbop2.isChecked()){
                            Toast.makeText(context, "Cargando...", Toast.LENGTH_SHORT).show();
                          //  consultarIdProducto2(IDAlmac);
                            new TareaReporteMaxMin(context,conexion2(),ReporteMaxMin.this).execute(Filtro(),IDAlmac,">",3000);
                        }
                    }catch (Exception e){
                        new DialogError(context,"Error de conexión");
                    }
                }
            });

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
            SPGrupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Grupo = listaClass1.get(position).getIdGrupo();
                    nomgrup= listaClass1.get(position).getNomGrupo();
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
        }catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu7, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
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

    public void AccionesCheckbox(){
        ListaFiltro.clear();

    if (ChProveedor.isChecked()) {
        SPProvee.setVisibility(VISIBLE);
        ListaFiltro.add("CIDVALORCLASIFICACION5");
    } else {
        SPProvee.setVisibility(GONE);
    }

    if (ChClass3.isChecked()) {
        SPClass3.setVisibility(VISIBLE);
        ListaFiltro.add("CIDVALORCLASIFICACION3");
    } else {
        SPClass3.setVisibility(GONE);
    }

    if (ChClass4.isChecked()) {
        SPClass4.setVisibility(VISIBLE);
        ListaFiltro.add("CIDVALORCLASIFICACION4");
    } else {
        SPClass4.setVisibility(GONE);
    }

    if (ChClass6.isChecked()) {
        SPClass6.setVisibility(VISIBLE);
        ListaFiltro.add("CIDVALORCLASIFICACION6");
    } else {
        SPClass6.setVisibility(GONE);
    }

    if (ChSubGrupo.isChecked()) {
        SPSubGrupo.setVisibility(VISIBLE);
        ListaFiltro.add("CIDVALORCLASIFICACION2");
    } else {
        SPSubGrupo.setVisibility(GONE);
    }

    if (ChGrupo.isChecked()) {
        SPGrupo.setVisibility(VISIBLE);
        ListaFiltro.add("CIDVALORCLASIFICACION1");
    } else {
        SPGrupo.setVisibility(GONE);
    }

    }

    public String Filtro() {
        try {
            String Consulta = "";

            if (ListaFiltro.size() == 1) {

                PartClass1 = "SELECT CIDPRODUCTO,CCODIGOPRODUCTO,CNOMBREPRODUCTO FROM admProductos where " + ListaFiltro.get(0) + "=";

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

                PartClass1 = "SELECT CIDPRODUCTO,CCODIGOPRODUCTO,CNOMBREPRODUCTO FROM admProductos where " + ListaFiltro.get(0) + "=";
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
                PartClass1 = "SELECT CIDPRODUCTO,CCODIGOPRODUCTO,CNOMBREPRODUCTO FROM admProductos where " + ListaFiltro.get(0) + "=";
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

                if (ListaFiltro.get(2) == "CIDVALORCLASIFICACION1") {
                    Consulta += PartClass3 + "'" + Grupo + "'";
                } else if (ListaFiltro.get(2) == "CIDVALORCLASIFICACION2") {
                    Consulta += PartClass3 + "'" + SubGrupo + "'";
                } else if (ListaFiltro.get(2) == "CIDVALORCLASIFICACION3") {
                    Consulta += PartClass3 + "'" + Class3 + "'";
                } else if (ListaFiltro.get(2) == "CIDVALORCLASIFICACION4") {
                    Consulta += PartClass3 + "'" + Class4 + "'";
                } else if (ListaFiltro.get(2) == "CIDVALORCLASIFICACION5") {
                    Consulta += PartClass3 + "'" + proveedor + "'";
                } else if (ListaFiltro.get(2) == "CIDVALORCLASIFICACION6") {
                    Consulta += PartClass3 + "'" + Class6 + "'";
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
     * Consultar el id y nombre del producto escaneado del almacen dengui, tienda dengui, tienda nopala
     */
    @Override
    public void consultarIdProducto(ArrayList<ModeloTablaMaxmin> model) {
        try {
            String fecha = Fech();
            String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            File archivo = new File(pdfPath, "Maximos y minimos -" + fecha + ".pdf");
            OutputStream outputStream = new FileOutputStream(archivo);

            PdfWriter pdfWriter = new PdfWriter(String.valueOf(archivo));
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            float[] width = {100f, 200f, 100f, 100f, 100f};
            Table table = new Table(width);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            Drawable d1 = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                d1 = getDrawable(R.drawable.logo_casacru);
            }
            Bitmap bitmap = ((BitmapDrawable) d1).getBitmap();
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream1);
            byte[] bitmapStream = stream1.toByteArray();

            ImageData imageData = ImageDataFactory.create(bitmapStream);
            Image image1 = new Image(imageData);
            image1.setWidth(100f);

            table.addCell(new Cell().add(image1).setBorder(Border.NO_BORDER));
            table.addCell(new Cell(1, 2).add(new Paragraph("Reporte de Máximos y Mínimos " + NomAlmacen).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
            table.addCell(new Cell(1,2).add(new Paragraph("Fecha: \n" + fecha)).setBorder(Border.NO_BORDER));

            table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));

            table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("CODIGO").setTextAlignment(TextAlignment.CENTER)));
            table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("DESCRIPCION").setTextAlignment(TextAlignment.CENTER)));
            table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("EXISTENCIA ACTUAL").setTextAlignment(TextAlignment.CENTER)));
            table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("MÁXIMO").setTextAlignment(TextAlignment.CENTER)));
            table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("MÍNIMO").setTextAlignment(TextAlignment.CENTER)));

            try {
                Toast.makeText(context, "Cargando...", Toast.LENGTH_SHORT).show();
                for(int i=0;i<model.size();i++){
                    table.addCell(new Cell().add(new Paragraph(model.get(i).getCodProd())));
                    table.addCell(new Cell().add(new Paragraph(model.get(i).getNomProd())));
                    table.addCell(new Cell().add(new Paragraph(model.get(i).getExistenciaActual()).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph(model.get(i).getMaximo()).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph(model.get(i).getMinimo()).setTextAlignment(TextAlignment.CENTER)));
                }
                document.add(table);
                document.close();
                Toast.makeText(context, "Documento creado", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, NomAlmacen, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }
        } catch (Exception e) {

        }
    }



@Override
    public void consultarIdProducto2(ArrayList<ModeloTablaMaxmin> model)
    {
        try {
            String fecha = Fech();
            String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            File archivo = new File(pdfPath, "Maximos y minimos -" + fecha + ".pdf");
            OutputStream outputStream = new FileOutputStream(archivo);

            PdfWriter pdfWriter = new PdfWriter(String.valueOf(archivo));
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            float[] width = {100f, 200f, 100f, 100f, 100f};
            Table table = new Table(width);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            Drawable d1 = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                d1 = getDrawable(R.drawable.logo_casacru);
            }
            Bitmap bitmap = ((BitmapDrawable) d1).getBitmap();
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream1);
            byte[] bitmapStream = stream1.toByteArray();

            ImageData imageData = ImageDataFactory.create(bitmapStream);
            Image image1 = new Image(imageData);
            image1.setWidth(100f);

            table.addCell(new Cell().add(image1).setBorder(Border.NO_BORDER));
            table.addCell(new Cell(1, 2).add(new Paragraph("Reporte de Máximos y Mínimos " + NomAlmacen).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
            table.addCell(new Cell(1,2).add(new Paragraph("Fecha: \n" + fecha)).setBorder(Border.NO_BORDER));

            table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));

            table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("CODIGO").setTextAlignment(TextAlignment.CENTER)));
            table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("DESCRIPCION").setTextAlignment(TextAlignment.CENTER)));
            table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("EXISTENCIA ACTUAL").setTextAlignment(TextAlignment.CENTER)));
            table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("MÁXIMO").setTextAlignment(TextAlignment.CENTER)));
            table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("MÍNIMO").setTextAlignment(TextAlignment.CENTER)));

            try {
                Toast.makeText(context, "Cargando...", Toast.LENGTH_SHORT).show();
                for(int i=0;i<model.size();i++){
                    table.addCell(new Cell().add(new Paragraph(model.get(i).getCodProd())));
                    table.addCell(new Cell().add(new Paragraph(model.get(i).getNomProd())));
                    table.addCell(new Cell().add(new Paragraph(model.get(i).getExistenciaActual()).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph(model.get(i).getMaximo()).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph(model.get(i).getMinimo()).setTextAlignment(TextAlignment.CENTER)));
                }
                document.add(table);
                document.close();
                Toast.makeText(context, "Documento creado", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, NomAlmacen, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }
        } catch (Exception e) {

        }
    }

    /**
     *Busca los parametros dee conexion el la base de datos SQLite del aplicativo
     */
    public void buscard() {
        try {
            DatabaseHelper dbh = new DatabaseHelper(this, "Conexion", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();

            Cursor fila = database.rawQuery
                    ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id='"+idBusqueda+"'", null);

            if (fila.moveToFirst()) {
                IP=(fila.getString(0));
                PUERTO=(fila.getString(1));
                USUARIO=(fila.getString(5));
                CONTRASEÑA=(fila.getString(2));
                BD=(fila.getString(3));
                database.close();
            } else {
                Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){
            Toast.makeText(this, "No existen parametros", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Realiza la conexion al servidor con la base de datos adDENGUI2019
     * @return
     */

    public Connection conexion2() {
        Connection conexion2 = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion2 = DriverManager.getConnection("jdbc:jtds:sqlserver://" + this.IP + ":" + this.PUERTO + ";databaseName=" + BD + ";user="+this.USUARIO+";password=" + this.CONTRASEÑA + ";");
        } catch (Exception e) {
            new DialogError(context,"");
        }
        return conexion2;
    }


    /**
     * obtiene los valores de la fecha por separado (dia, mes, año)
     */

    public void Fecha() {
        try {
            SimpleDateFormat dtf=new SimpleDateFormat("yyyy/MM/dd");
            Calendar calendar=Calendar.getInstance();
            Date date=calendar.getTime();
            String fetch=dtf.format(date);

            Date dte=new SimpleDateFormat("yyyy/MM/dd").parse(fetch);
            calendar.setTime(dte);

            anio = String.valueOf(calendar.get(Calendar.YEAR));
            int month = (calendar.get(Calendar.MONTH))+1;
            mes=String.valueOf(month);
        }catch (Exception ex){

        }
    }

    /**
     * Consulta el ejercicio al que corresponde la fecha actual
     * @return
     */
    public String getEjercicio(){
        String ejercicio="";
        try {
            Statement stm = conexion2().createStatement();
            ResultSet rs = stm.executeQuery("SELECT CIDEJERCICIO FROM admEjercicios where CEJERCICIO='" + anio + "'");

            if (rs.next()) {
                ejercicio=rs.getString(1);
            }
            return ejercicio;
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return ejercicio;
    }

    /**
     * Metodo para optener la fecha actual
     * @return
     */
    public String Fech() {
        try {
            SimpleDateFormat dtf=new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar=Calendar.getInstance();
            Date date=calendar.getTime();
            String fetch=dtf.format(date);
            return fetch;
        }catch (Exception ex){
            return "0";
        }
    }

    public void ObtenerSharePreferences(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("Datos2",this.MODE_PRIVATE);
        idBusqueda=sharedPreferences.getString("Almacen","null");
        IDAlmac=sharedPreferences.getString("nomAlmacen","null");
    }



    @Override
    public void proggresbar(boolean status) {
        if(status){
            btnMostrar.setEnabled(false);
            progressBar2.setVisibility(VISIBLE);
        }else{
            btnMostrar.setEnabled(true);
            progressBar2.setVisibility(GONE);
        }
    }

}