package com.casacru.casacruapp.Almacen;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.casacru.casacruapp.Dialog.DialogError;
import com.casacru.casacruapp.Dialog.DialogSelectAlmacen;
import com.casacru.casacruapp.General.ListadoProductos;
import com.casacru.casacruapp.Modelo.ModeloProductos;
import com.casacru.casacruapp.R;
import com.casacru.casacruapp.SQLite.DatabaseHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Ubicaciones extends AppCompatActivity {
    EditText txtcodigoU,txtzona1,txtPasillo1,txtAnaquel1,txtminimo,txtmaximo,txtClass3,txtClass4,txtClass6;
    String IP, PUERTO, USUARIO, CONTRASEÑA, BD, IDAlmac,idBusqueda;
    TextView txtnom1,txtID;
    Button busca,btn22, btnclass, btnMaxMin;
    String idProveedor,class1, class2, class3,class4,class6;
    String Proveedor,grupo,subgrupo,nomClasificacion3,nomClasificacion4,nomClasificacion6;
    Spinner spClass1,spClass2,spProvee,spClass3,spClass4,spClass6;
    ArrayList<String> listaClass1 = new ArrayList<String>();
    ArrayList<String> listaClass2 = new ArrayList<String>();
    ArrayList<String> listaClass3 = new ArrayList<String>();
    ArrayList<String> listaClass4 = new ArrayList<String>();
    ArrayList<String> listProvee = new ArrayList<String>();
    ArrayList<String> listaClass6 = new ArrayList<String>();
    Context context;
    ArrayAdapter<CharSequence> adaptadorclass1;
    ArrayAdapter<CharSequence> adaptadorclass2;
    ArrayAdapter<CharSequence> adaptadorclass5;
    ArrayAdapter<CharSequence> adaptadorclass3;
    ArrayAdapter<CharSequence> adaptadorclass4;
    ArrayAdapter<CharSequence> adaptadorclass6;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicaciones);
try {
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    spClass1 = (Spinner) findViewById(R.id.SpinnerClass1);
    spClass2 = (Spinner) findViewById(R.id.SpinnerClass2);
    spProvee = (Spinner) findViewById(R.id.SpinnerProvee);
    spClass3 = (Spinner) findViewById(R.id.txtClasificacion3);
    spClass4 = (Spinner) findViewById(R.id.txtClasificacion4);
    spClass6 = (Spinner) findViewById(R.id.txtClasificacion6);
    context=this;
    busca = (Button) findViewById(R.id.button4);
    btn22 = (Button) findViewById(R.id.btnEsc);
    txtcodigoU = (EditText) findViewById(R.id.txtCodigo);
    txtnom1 = (TextView) findViewById(R.id.txt_nombrep);
    txtzona1 = (EditText) findViewById(R.id.txt_zona);
    txtPasillo1 = (EditText) findViewById(R.id.txt_pasillo);
    txtAnaquel1 = (EditText) findViewById(R.id.txt_anaquel);
    txtmaximo = (EditText) findViewById(R.id.txtMaximo);
    txtminimo = (EditText) findViewById(R.id.txtMinimo);
    txtID = (TextView) findViewById(R.id.TXTIDP);

    btn22.setOnClickListener(nOnClickListener);
    busca.setOnClickListener(nOnClickListener);
    btnclass = (Button) findViewById(R.id.btnclass);
    btnMaxMin = (Button) findViewById(R.id.btnMaxMin);
    btnclass.setOnClickListener(nOnClickListener);
    btnMaxMin.setOnClickListener(nOnClickListener);
    ObtenerSharePreferences();
    buscar();
    correccion();
    llenarSpinner();
    llenarSpinnerclass2();
    llenarSpinnerProvee();
    llenarSpinnerclass3();
    llenarSpinnerclass4();
    llenarSpinnerclass6();
    adaptadorclass1 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, this.listaClass1);
    adaptadorclass2 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, this.listaClass2);
    adaptadorclass5 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, this.listProvee);
    adaptadorclass3 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, this.listaClass3);
    adaptadorclass4 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, this.listaClass4);
    adaptadorclass6 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, this.listaClass6);

    spClass1.setAdapter(adaptadorclass1);
    spClass2.setAdapter(adaptadorclass2);
    spProvee.setAdapter(adaptadorclass5);
    spClass3.setAdapter(adaptadorclass3);
    spClass4.setAdapter(adaptadorclass4);
    spClass6.setAdapter(adaptadorclass6);

    spClass1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            Recuperar();
            //txtClass1.setText(parent.getItemAtPosition(position).toString());
            grupo=parent.getItemAtPosition(position).toString();
            Toast.makeText(context, grupo, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });
    spClass2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          //  Recuperar();
            //txtClass2.setText(parent.getItemAtPosition(position).toString());
        subgrupo=parent.getItemAtPosition(position).toString();
            Toast.makeText(context, subgrupo, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });
    spProvee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
               // Recuperar();
                //txtProveedor.setText(parent.getItemAtPosition(position).toString());
                Proveedor=parent.getItemAtPosition(position).toString();
                Toast.makeText(context, Proveedor, Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });
    spClass3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            nomClasificacion3=parent.getItemAtPosition(position).toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });
    spClass4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            nomClasificacion4=parent.getItemAtPosition(position).toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });
    spClass6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                nomClasificacion6=parent.getItemAtPosition(position).toString();
            } catch (Exception ex) {

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });

    Recuperar();


    Toast.makeText(context, IDAlmac, Toast.LENGTH_SHORT).show();
}catch (Exception ex){

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

    /**
     * Conexion Dinamica con SQLite
     * @return
     * Regresa con=conexion
     */
    public Connection conexion2() {
        Connection conexion2 = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

            conexion2 = DriverManager.getConnection("jdbc:jtds:sqlserver://" + this.IP + ":" + this.PUERTO + ";databaseName=" + this.BD + ";user="+this.USUARIO+";password=" + this.CONTRASEÑA + ";");

        } catch (Exception e) {
            new DialogError(context,"");
        }
        return conexion2;
    }

    /**
     * Escaneo de codigo de barras
     * @param requestCode1
     * @param resultCode1
     * @param data1
     * Realiza la consulta del producto y su ubicacion
     */

    @Override
    protected void onActivityResult(int requestCode1, int resultCode1, @Nullable Intent data1) {
        super.onActivityResult(requestCode1, resultCode1, data1);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode1, resultCode1, data1);
        if (result != null)
            if (result.getContents() != null) {
                txtcodigoU.setText("" + result.getContents());

                try{
                    consultarIdProducto();
                    consultarUbicacion();
                    consultarClasificacion();
                    consultarClasificacion1();
                    consultarClasificacion2();
                    consultarClasificacion3();
                    consultarClasificacion4();
                    consultarClasificacion6();

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

    }

   /* public void cargarSpinners(){
        try {
             adaptadorclass1 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, this.listaClass1);
             adaptadorclass2 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, this.listaClass2);
             adaptadorclass5 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, this.listProvee);

//            spClass1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, this.listaClass1));
//            spClass2.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, this.listaClass2));
//            spProvee.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, this.listProvee));
            spClass1.setAdapter(adaptadorclass1);
            spClass2.setAdapter(adaptadorclass2);
            spProvee.setAdapter(adaptadorclass5);

            spClass1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Recuperar();

                    txtClass1.setText(parent.getItemAtPosition(position).toString());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spClass2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Recuperar();
                    txtClass2.setText(parent.getItemAtPosition(position).toString());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spProvee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Recuperar();
                        txtProveedor.setText(parent.getItemAtPosition(position).toString());

                    } catch (Exception ex) {

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }catch (Exception ex){

        }
    }*/

    /**
     * Acciones para el boton de escaneo y de busqueda de producto
     */

    private View.OnClickListener nOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnEsc:
                    new IntentIntegrator(Ubicaciones.this).initiateScan();
                    limpiar();
                    spClass1.setVisibility(View.VISIBLE);
                    spClass2.setVisibility(View.VISIBLE);
                    break;
                case R.id.button4:
                    try{
                        Intent intent=new Intent(context, ListadoProductos.class);
                        intent.putExtra("Param","1");
                        startActivity(intent);
                        finish();

                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnMaxMin:
                    if(txtminimo.getVisibility()==GONE){
                        txtminimo.setVisibility(View.VISIBLE);
                        txtmaximo.setVisibility(View.VISIBLE);

                    }else{
                        txtminimo.setVisibility(View.GONE);
                        txtmaximo.setVisibility(View.GONE);
                    }
                    break;
                case R.id.btnclass:
                    if (spClass3.getVisibility()==GONE){
                        spClass3.setVisibility(View.VISIBLE);
                        spClass4.setVisibility(View.VISIBLE);
                        spClass6.setVisibility(View.VISIBLE);


                    }else{
                        spClass3.setVisibility(View.GONE);
                        spClass4.setVisibility(View.GONE);
                        spClass6.setVisibility(View.GONE);
                    }
                    break;

            }


        }
    };

    /**
     *Consultar Id, nombre y valor de la clasificacion5(proveedor) del producto
      */
    public void consultarIdProducto() {
        try {
            Statement stm = conexion2().createStatement();
            ResultSet rs = stm.executeQuery("SELECT CIDPRODUCTO, CNOMBREPRODUCTO,CIDVALORCLASIFICACION5,CIDVALORCLASIFICACION6,CIDVALORCLASIFICACION4, " +
                    "CIDVALORCLASIFICACION3, CIDVALORCLASIFICACION2, CIDVALORCLASIFICACION1 FROM admProductos where CCODIGOPRODUCTO='" + txtcodigoU.getText().toString() + "'");

            if (rs.next()) {
                txtnom1.setVisibility(View.VISIBLE);
                txtID.setText(rs.getString(1));
                txtnom1.setText(rs.getString(2));
                idProveedor=rs.getString(3);
                class6=(rs.getString(4));
                class4=(rs.getString(5));
                class3=(rs.getString(6));
                class2=rs.getString(7);
                class1=rs.getString(8);

            }


        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Consultar la clasificacion 1,2,5
     */
    public void consultarClasificacion() {
        try {
            Statement stm = conexion2().createStatement();
            String consulta="SELECT CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDVALORCLASIFICACION='"+this.idProveedor+"'";
            ResultSet rs = stm.executeQuery(consulta);

            if (rs.next()) {
                int Spinnerposition=adaptadorclass5.getPosition(rs.getString(1));
                spProvee.setSelection(Spinnerposition);
              //  txtProveedor.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void consultarClasificacion1() {
        try {
            Statement stm = conexion2().createStatement();
            String consulta="SELECT CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDVALORCLASIFICACION='"+this.class1+"'";
            ResultSet rs = stm.executeQuery(consulta);

            if (rs.next()) {
              //  txtClass1.setText(rs.getString(1));
                int Spinnerposition=adaptadorclass1.getPosition(rs.getString(1));
                spClass1.setSelection(Spinnerposition);
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void consultarClasificacion2() {
        try {
            Statement stm = conexion2().createStatement();
            String consulta="SELECT CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDVALORCLASIFICACION='"+this.class2+"'";
            ResultSet rs = stm.executeQuery(consulta);
            if (rs.next()) {
                //txtClass2.setText(rs.getString(1));
                int Spinnerposition=adaptadorclass2.getPosition(rs.getString(1));
                spClass2.setSelection(Spinnerposition);
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void consultarClasificacion3() {
        try {
            Statement stm = conexion2().createStatement();
            String consulta="SELECT CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDVALORCLASIFICACION='"+this.class3+"'";
            ResultSet rs = stm.executeQuery(consulta);
            if (rs.next()) {
                //txtClass2.setText(rs.getString(1));
                int Spinnerposition=adaptadorclass3.getPosition(rs.getString(1));
                spClass3.setSelection(Spinnerposition);
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void consultarClasificacion4() {
        try {
            Statement stm = conexion2().createStatement();
            String consulta="SELECT CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDVALORCLASIFICACION='"+this.class4+"'";
            ResultSet rs = stm.executeQuery(consulta);
            if (rs.next()) {
                //txtClass2.setText(rs.getString(1));
                int Spinnerposition=adaptadorclass4.getPosition(rs.getString(1));
                spClass4.setSelection(Spinnerposition);
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void consultarClasificacion6() {
        try {
            Statement stm = conexion2().createStatement();
            String consulta="SELECT CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDVALORCLASIFICACION='"+this.class6+"'";
            ResultSet rs = stm.executeQuery(consulta);
            if (rs.next()) {
                //txtClass2.setText(rs.getString(1));
                int Spinnerposition=adaptadorclass6.getPosition(rs.getString(1));
                spClass6.setSelection(Spinnerposition);
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Consultar Ubicacion (ZONA, PASILLO, ANAQUEL), Maximos y minimos del producto
     */
    public void consultarUbicacion() {

        try {
            Statement stm = conexion2().createStatement();
            ResultSet rs = stm.executeQuery("SELECT CZONA, CPASILLO, CANAQUEL, CEXISTENCIAMINBASE, CEXISTENCIAMAXBASE FROM admMaximosMinimos where CIDALMACEN='" + IDAlmac + "' and CIDPRODUCTO='" + txtID.getText().toString() + "';");

            rs.wasNull();
            if (rs.next()) {


                txtzona1.setText(rs.getString(1));
                txtPasillo1.setText(rs.getString(2));
                txtAnaquel1.setText(rs.getString(3));
                txtminimo.setText(rs.getString(4));
                txtmaximo.setText(rs.getString(5));

            }


        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Realiza la insercion o actualizacion
     * @param v
     */
    public void ActualizarInformacion(View v) {
       try{
           ConsultarIdProv();
           ConsultarIdClas1();
           ConsultarIdClas2();
           ConsultarIdClas3();
           ConsultarIdClas4();
           ConsultarIdClas6();
           insertar();
           actualizar4();
           InsertarClasificaciones();
           ActualizarClasificaciones();
           Toast.makeText(getApplicationContext(), "LISTO!! ACTUALIZADO", Toast.LENGTH_SHORT).show();
       }catch (Exception e){
           Toast.makeText(getApplicationContext(), "Ups. Hubo un problema", Toast.LENGTH_SHORT).show();
       }

    }

    /**
     * Actualiza ubicacion, maximos y minimos
     */
    public void actualizar4() {

        try {

            PreparedStatement rs = conexion2().prepareStatement("UPDATE admMaximosMinimos SET CEXISTENCIAMINBASE='" +
                    txtminimo.getText().toString() + "', CEXISTENCIAMAXBASE='"+ txtmaximo.getText().toString() + "',CPASILLO='" +
                    txtPasillo1.getText().toString() + "', CZONA='" + txtzona1.getText().toString() +
                    "', CANAQUEL='" +txtAnaquel1.getText().toString() +
                    "' where CIDPRODUCTO='" + txtID.getText().toString() +
                    "' AND CIDALMACEN='" + IDAlmac+"';");
            rs.executeUpdate();

        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Realiza Insercion de Ubicacion
     */
    public void insertar() {

        try {

            PreparedStatement rs = conexion2().prepareStatement("INSERT INTO admMaximosMinimos (CIDALMACEN, CIDPRODUCTO, CPASILLO, CZONA, CANAQUEL,CEXISTENCIAMINBASE, CEXISTENCIAMAXBASE) VALUES('" + IDAlmac + "','" + txtID.getText().toString() + "','" + txtPasillo1.getText().toString() + "','" + txtzona1.getText().toString() + "','" + txtAnaquel1.getText().toString() + "','"+ txtminimo.getText().toString() + "','" + txtmaximo.getText().toString() +"')");
            rs.executeUpdate();

        } catch (SQLException e) {

        }
    }

    /**
     * Insertar Clasificacion5
     */
    public void InsertarClasificaciones(){
    try {
        if(Proveedor=="*PROVEEDORES*"){
            idProveedor="0";
        }
        if(grupo=="*GRUPOS*"){
            class1="0";
        }
        if(subgrupo=="*SUBGRUPOS*"){
            class2="0";
        }
        if(subgrupo=="*CLASIFICACION 1*"){
            class2="0";
        }
        if(nomClasificacion3=="*CLASIFICACION 3*"){
            class3="0";
        }
        if(nomClasificacion4=="*CLASIFICACION 4*"){
            class4="0";
        }
        if(nomClasificacion6=="*CLASIFICACION 6*"){
            class6="0";
        }
            String consulta="INSERT INTO admProductos(CIDVALORCLASIFICACION1,CIDVALORCLASIFICACION2,CIDVALORCLASIFICACION5,CIDVALORCLASIFICACION3,CIDVALORCLASIFICACION4,CIDVALORCLASIFICACION6) values('"+this.class1+"','"+this.class2+"','"+this.idProveedor+"','"+this.class3+"'," +
                    "'"+this.class4+"','"+this.class6+"') where CIDPRODUCTO='"+txtID.getText().toString()+"'";
            PreparedStatement rs = conexion2().prepareStatement(consulta);
            rs.executeUpdate();

    } catch (SQLException e) {

    }

}

    /**
     * Metodo para actualizar la informacion de las clasificaciones.
     */
    public void ActualizarClasificaciones(){
        try{
            if(Proveedor=="*PROVEEDORES*"){
             idProveedor="0";
            }
            if(grupo=="*GRUPOS*"){
                class1="0";
            }
            if(subgrupo=="*SUBGRUPOS*"){
                class2="0";
            }
            if(nomClasificacion3=="*CLASIFICACION 3*"){
                class3="0";
            }
            if(nomClasificacion4=="*CLASIFICACION 4*"){
                class4="0";
            }
            if(nomClasificacion6=="*CLASIFICACION 6*"){
                class6="0";
            }
            PreparedStatement rs = conexion2().prepareStatement("UPDATE admProductos SET CIDVALORCLASIFICACION5='"+idProveedor+"', CIDVALORCLASIFICACION1='"+class1+"', " +
                    "CIDVALORCLASIFICACION2='"+class2+"', CIDVALORCLASIFICACION3='"+class3+"', CIDVALORCLASIFICACION4='"+class4+"', CIDVALORCLASIFICACION6='"+class6+"'" +
                    " WHERE CIDPRODUCTO='" + txtID.getText().toString() +"'");
            rs.executeUpdate();
        }catch(Exception ex){

        }

    }

    /**
     * Metodo para realizar la consulta del id de tres caracteres del proveedor
     */
    public void ConsultarIdProv(){
    try {

        Statement stm = conexion2().createStatement();
        String consulta="SELECT CIDVALORCLASIFICACION FROM admClasificacionesValores where CIDCLASIFICACION='"+29+"' and CVALORCLASIFICACION='"+Proveedor+"'";
        ResultSet rs = stm.executeQuery(consulta);


        if(rs.next()){
            idProveedor=rs.getString(1);
        }


    } catch (SQLException e) {

    }
}

    public void ConsultarIdClas1(){
        try {

            Statement stm = conexion2().createStatement();
            String consulta="SELECT CIDVALORCLASIFICACION FROM admClasificacionesValores where CIDCLASIFICACION='"+25+"' and CVALORCLASIFICACION='"+grupo+"'";
            ResultSet rs = stm.executeQuery(consulta);


            if(rs.next()){
                class1=rs.getString(1);
            }


        } catch (SQLException e) {

        }
    }

    public void ConsultarIdClas2(){
        try {

            Statement stm = conexion2().createStatement();
            String consulta="SELECT CIDVALORCLASIFICACION FROM admClasificacionesValores where CIDCLASIFICACION='"+26+"' and CVALORCLASIFICACION='"+subgrupo+"'";
            ResultSet rs = stm.executeQuery(consulta);


            if(rs.next()){
                class2=rs.getString(1);
            }


        } catch (SQLException e) {

        }
    }

    public void ConsultarIdClas3(){
        try {

            Statement stm = conexion2().createStatement();
            String consulta="SELECT CIDVALORCLASIFICACION FROM admClasificacionesValores where CIDCLASIFICACION='"+27+"' and CVALORCLASIFICACION='"+nomClasificacion3+"'";
            ResultSet rs = stm.executeQuery(consulta);


            if(rs.next()){
                class3=rs.getString(1);
            }


        } catch (SQLException e) {

        }
    }

    public void ConsultarIdClas4(){
        try {

            Statement stm = conexion2().createStatement();
            String consulta="SELECT CIDVALORCLASIFICACION FROM admClasificacionesValores where CIDCLASIFICACION='"+28+"' and CVALORCLASIFICACION='"+nomClasificacion4+"'";
            ResultSet rs = stm.executeQuery(consulta);


            if(rs.next()){
                class4=rs.getString(1);
            }


        } catch (SQLException e) {

        }
    }

    public void ConsultarIdClas6(){
        try {
            Statement stm = conexion2().createStatement();
            String consulta="SELECT CIDVALORCLASIFICACION FROM admClasificacionesValores where CIDCLASIFICACION='"+30+"' and CVALORCLASIFICACION='"+nomClasificacion6+"'";
            ResultSet rs = stm.executeQuery(consulta);

            if(rs.next()){
                class6=rs.getString(1);
            }

        } catch (SQLException e) {

        }
    }

    public void llenarSpinner(){
        try {
            Statement stm = conexion2().createStatement();

            String consulta="SELECT CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDCLASIFICACION='"+25+"'";

            ResultSet rs = stm.executeQuery(consulta);
            listaClass1.add("*GRUPOS*");
            while (rs.next()) {
                listaClass1.add(rs.getString(1));

            }

        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void llenarSpinnerclass2(){
        try {
            Statement stm = conexion2().createStatement();

            String consulta="SELECT CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDCLASIFICACION='"+26+"'";

            ResultSet rs = stm.executeQuery(consulta);
            listaClass2.add("*SUBGRUPOS*");
            while (rs.next()) {
                listaClass2.add(rs.getString(1));

            }

        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void llenarSpinnerclass3(){
        try {
            Statement stm = conexion2().createStatement();

            String consulta="SELECT CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDCLASIFICACION='"+27+"'";

            ResultSet rs = stm.executeQuery(consulta);
            listaClass3.add("*CLASIFICACION 3*");
            while (rs.next()) {
                listaClass3.add(rs.getString(1));

            }

        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void llenarSpinnerclass4(){
        try {
            Statement stm = conexion2().createStatement();

            String consulta="SELECT CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDCLASIFICACION='"+28+"'";

            ResultSet rs = stm.executeQuery(consulta);
            listaClass4.add("*CLASIFICACION 4*");
            while (rs.next()) {
                listaClass4.add(rs.getString(1));

            }

        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void llenarSpinnerclass6(){
        try {
            Statement stm = conexion2().createStatement();

            String consulta="SELECT CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDCLASIFICACION='"+30+"'";

            ResultSet rs = stm.executeQuery(consulta);
            listaClass6.add("*CLASIFICACION 6*");
            while (rs.next()) {
                listaClass6.add(rs.getString(1));

            }

        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void llenarSpinnerProvee(){
        try {
            Statement stm = conexion2().createStatement();

            String consulta="SELECT CVALORCLASIFICACION FROM admClasificacionesValores WHERE CIDCLASIFICACION='"+29+"'";

            ResultSet rs = stm.executeQuery(consulta);
            listProvee.add("*PROVEEDORES*");
            while (rs.next()) {
                listProvee.add(rs.getString(1));

            }

        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Consulta la informacion de bd sqlite
     */
    public void buscar() {
        try {
            DatabaseHelper dbh = new DatabaseHelper(this, "Conexion", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();


            Cursor fila = database.rawQuery
                    ("select ip, puerto, contraseña, bd, idAlmacen,usuario from parametros where Id='"+idBusqueda+"'", null);

            if (fila.moveToFirst()) {
                this.IP = (fila.getString(0));
                this.PUERTO = (fila.getString(1));
                this.CONTRASEÑA = (fila.getString(2));
                this.BD = (fila.getString(3));
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
     * Limpiar todos los campos
     */
    private void limpiar() {
        txtcodigoU.setText("");
        txtID.setText("");
        txtnom1.setText("");
        txtPasillo1.setText("");
        txtAnaquel1.setText("");
        txtzona1.setText("");
        txtmaximo.setText("");
        txtminimo.setText("");
        txtnom1.setVisibility(GONE);
    }

    /**
     * Redirige a la pantalla de inicio
     * @param view
     */
    public void inicio(View view) {
        Intent inicio = new Intent(this, Inicio.class);
        startActivity(inicio);
    }

    public void Recuperar() {

try {
    ModeloProductos model = (ModeloProductos) getIntent().getSerializableExtra("Elemento");
    if(model != null) {
        limpiar();
        txtcodigoU.setText(model.getCodProducto());
        consultarIdProducto();
        consultarUbicacion();
        consultarClasificacion();
        consultarClasificacion1();
        consultarClasificacion2();
        consultarClasificacion3();
        consultarClasificacion4();
        consultarClasificacion6();
    }
}catch (Exception ex){
    String mensaje = ex.getMessage();
}
    }

    public void ObtenerSharePreferences(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("Datos2",this.MODE_PRIVATE);
        idBusqueda=sharedPreferences.getString("Almacen","null");
        IDAlmac=sharedPreferences.getString("nomAlmacen","null");

    }

    public void correccion(){
        if(IP==null || IP.equals("")){
            if(idBusqueda=="2"){
                idBusqueda="1";
                buscar();
                BD="adCASACRU61";

            }
        }

    }
}

