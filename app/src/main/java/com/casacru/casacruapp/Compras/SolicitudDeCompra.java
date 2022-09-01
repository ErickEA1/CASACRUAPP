package com.casacru.casacruapp.Compras;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.casacru.casacruapp.Adaptador.AdaptadorProductos;
import com.casacru.casacruapp.Dialog.DialogSelectAlmacen;
import com.casacru.casacruapp.Modelo.DtoProductos;
import com.casacru.casacruapp.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.colors.ColorConstants;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SolicitudDeCompra extends AppCompatActivity implements DialogSelectAlmacen.Finalizo {
     Context context;
     ArrayList<DtoProductos> lista;
    private  String NomAlmacen, NomAlmacen2;
    private  RecyclerView listp;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private Button btnsave, btnFiltro;
    private RadioButton radioButtonTDengui,radioButtonMR,radioButtonAlDengui,radioButtonTNopala,radioButtonLagunas,radioButtonSD, radioButton61;
    private LinearLayout txtRg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes);
        try {
            context=this;
            radioButton61=(RadioButton)findViewById(R.id.tbtxt61);
            radioButtonAlDengui=(RadioButton)findViewById(R.id.rbtxtAlDengui);
            radioButtonTDengui=(RadioButton)findViewById(R.id.rbtxtDengui);
            radioButtonTNopala=(RadioButton)findViewById(R.id.rbtxtNopala);
            radioButtonLagunas=(RadioButton)findViewById(R.id.rbtxtlagunas);
            radioButtonSD=(RadioButton)findViewById(R.id.rbtxtsd);
            radioButtonMR=(RadioButton)findViewById(R.id.rbtxtmr);
            txtRg=(LinearLayout) findViewById(R.id.RGAlmacenes);
            btnFiltro=(Button) findViewById(R.id.btnfiltro);
            btnFiltro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtRg.setVisibility(View.GONE);
                    if(radioButtonTDengui.isChecked()){
                        ListarDatos("TIENDA DENGUI");
                            NomAlmacen2="TIENDA DENGUI";
                    }else if(radioButtonTNopala.isChecked()){
                        ListarDatos("TIENDA NOPALA");
                        NomAlmacen2="TIENDA NOPALA";
                    }else if(radioButtonAlDengui.isChecked()){
                        ListarDatos("ALMACEN DENGUI");
                        NomAlmacen2="ALMACEN DENGUI";
                    }else if(radioButton61.isChecked()){
                        ListarDatos("TIENDA 61");
                        NomAlmacen2="TIENDA 61";
                    }else if(radioButtonLagunas.isChecked()){
                        ListarDatos("LAGUNAS OAXACA");
                        NomAlmacen2="LAGUNAS OAXACA";
                    }else if(radioButtonSD.isChecked()){
                        ListarDatos("SANTO DOMINGO");
                        NomAlmacen2="SANTO DOMINGO";
                    }else if(radioButtonMR.isChecked()){
                        ListarDatos("MATIAS ROMERO");
                        NomAlmacen2="MATIAS ROMERO";
                    }

                }
            });
            lista= new ArrayList<DtoProductos>();
            listp=(RecyclerView) findViewById(R.id.listaprod);
            listp.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            new DialogSelectAlmacen(context,SolicitudDeCompra.this);
            btnsave=(Button)findViewById(R.id.btnSave);
            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Actualizar();
                }
            });
            inicializarFirebase();
               }catch (Exception ex){
            Toast.makeText(this, "Error: "+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu4, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item2:
                GuardarSharePreferens(NomAlmacen);
                Intent intent = new Intent(this, Solicitudes.class);
                intent.putExtra("Almacen", NomAlmacen );
                startActivity(intent);
                break;
            case R.id.item1:
         try{
             String fecha=Fecha();
                String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
               File archivo=new File(pdfPath, "Solicitud "+NomAlmacen+"-"+fecha+".pdf");
                OutputStream outputStream=new FileOutputStream(archivo);

             PdfWriter pdfWriter=new PdfWriter(String.valueOf(archivo));
                PdfDocument pdfDocument=new PdfDocument(pdfWriter);
                Document document=new Document(pdfDocument);

                float[] width={100f, 200f,100f,100f};
                Table table=new Table(width);
                table.setHorizontalAlignment(HorizontalAlignment.CENTER);

                Drawable d1 = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    d1 = getDrawable(R.drawable.logo_casacru);
                }
                Bitmap bitmap=((BitmapDrawable)d1).getBitmap();
                ByteArrayOutputStream stream1 =new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream1);
                byte[] bitmapStream=stream1.toByteArray();

                ImageData imageData= ImageDataFactory.create(bitmapStream);
                Image image1=new Image(imageData);
                image1.setWidth(100f);

                table.addCell(new Cell().add(image1).setBorder(Border.NO_BORDER));
                table.addCell(new Cell(1,2).add(new Paragraph("Solicitud de material para "+NomAlmacen).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
                table.addCell(new Cell().add(new Paragraph("Fecha: \n"+fecha)).setBorder(Border.NO_BORDER));

                table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));

                table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("CODIGO").setTextAlignment(TextAlignment.CENTER)));
                table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193) ).add(new Paragraph("DESCRIPCION").setTextAlignment(TextAlignment.CENTER)));
                table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193) ).add(new Paragraph("CANTIDAD").setTextAlignment(TextAlignment.CENTER)));
                table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("UNIDAD DE MEDIDA").setTextAlignment(TextAlignment.CENTER)));

                for (int i=0;i<=lista.size()-1;i++){
                    table.addCell(new Cell().add(new Paragraph(lista.get(i).getCodigoProducto())));
                    table.addCell(new Cell().add(new Paragraph(lista.get(i).getNombreProducto())));
                    table.addCell(new Cell().add(new Paragraph(lista.get(i).getCantidad()).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph(lista.get(i).getUnidad()).setTextAlignment(TextAlignment.CENTER)));
                }

                document.add(table);
                document.close();
                Toast.makeText(context, "Documento creado", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, NomAlmacen, Toast.LENGTH_SHORT).show();
        }catch (Exception e){

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
                break;
            case R.id.item3:
                if(NomAlmacen=="ALMACEN DENGUI") {
                    if (txtRg.getVisibility() == View.GONE) {
                        txtRg.setVisibility(View.VISIBLE);
                    } else {
                        txtRg.setVisibility(View.GONE);
                    }
                }

        }
        return super.onOptionsItemSelected(item);
    }

    public void inicializarFirebase(){
        try{
            FirebaseApp.initializeApp(context);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference=firebaseDatabase.getReference();
        }catch(Exception e){
            Toast.makeText(context, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Metodo eredado de a interfaz de la clase DialogSelectAlmacen.
     * @param Id
     */
    @Override
    public void Resultado(String Id) {
        try {
            NomAlmacen = Id;

            if (Id != "ALMACEN DENGUI") {
                ListarDatos(Id);
                btnsave.setVisibility(View.GONE);

            }
        }catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Llena la lista con los objetos dentro de la base de datos de firebase
     *
     * @param Almacen
     */
    public void ListarDatos(String Almacen){
        try {
            databaseReference.child(Almacen).orderByChild("surtido").equalTo(false).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    listp.removeAllViews();
                    lista.clear();
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        DtoProductos p = objSnapshot.getValue(DtoProductos.class);
                        lista.add(p);
                    }
                    AdaptadorProductos productos = new AdaptadorProductos(lista, NomAlmacen, context);
                    listp.setAdapter(productos);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch(Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }

    /**
     * Realiza una iteracion de la lista de productos y los que esten seleccionados y tengan un
     * valor true en el campo "Surtido" seran actualizados en la base de datos de firebase con el valor correspondiente
     */
    public void Actualizar(){
        try {
                DtoProductos p = new DtoProductos();
                for (int i = 0; i <= lista.size() - 1; i++) {
                    if (lista.get(i).getSurtido()) {
                        databaseReference.child(NomAlmacen2).child(lista.get(i).getId()).removeValue();
                    }
                }
            }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }

    /**
     * Metodo para optener la fecha actual
     * @return
     */
    public String Fecha() {
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

    public void GuardarSharePreferens(String Almacen){
        final Context context=this;
        final SharedPreferences sharedPreferences=getSharedPreferences("Mis_preferences", context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("Almacen",Almacen);
        editor.apply();
    }

}