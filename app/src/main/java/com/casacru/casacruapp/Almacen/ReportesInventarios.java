package com.casacru.casacruapp.Almacen;

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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.casacru.casacruapp.Adaptador.AdaptadorFolios;
import com.casacru.casacruapp.Adaptador.ListadoAdapter;
import com.casacru.casacruapp.Compras.SolicitudDeCompra;
import com.casacru.casacruapp.Compras.Solicitudes;
import com.casacru.casacruapp.Dialog.DialogSelectAlmacen;
import com.casacru.casacruapp.Dialog.DialogSelectArchivo;
import com.casacru.casacruapp.Modelo.ModeloFolios;
import com.casacru.casacruapp.Modelo.ModeloProductos;
import com.casacru.casacruapp.R;
import com.casacru.casacruapp.SQLite.DatabaseOpenHelper;
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

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReportesInventarios extends AppCompatActivity implements DialogSelectArchivo.Finalizo  {
    private Context context;
    ArrayList<ModeloFolios> listaProductos= new ArrayList<ModeloFolios>();
    private RecyclerView folios;
    private String Almacen, Folio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes_inventarios);
        try{
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            context=this;
            folios=(RecyclerView) findViewById(R.id.folios);
            folios.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            ListarFolios();
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



    public void ListarFolios() {
        try {
            DatabaseOpenHelper dbh = new DatabaseOpenHelper(this, "Folios", null, 1);
            SQLiteDatabase database = dbh.getWritableDatabase();
            listaProductos.clear();

            Cursor fila = database.rawQuery("select Folio, Almacen from folios", null);
            if (fila.moveToFirst()) {
                do {
                    listaProductos.add(new ModeloFolios(fila.getString(0), fila.getString(1)));
                } while (fila.moveToNext());
            }
            database.close();
            AdaptadorFolios adapter = new AdaptadorFolios(listaProductos, context, new AdaptadorFolios.OnItemClickListenerr() {
                @Override
                public void onItemClick(ModeloFolios item) {
                    new DialogSelectArchivo(context, ReportesInventarios.this);
                   Almacen=item.getAlmacen();
                   Folio=item.getFolio();
                }
            });
        folios.setAdapter(adapter);
        }catch (Exception ex) {
            Toast.makeText(context, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

            /**
             * Archivo pdf de inventario general (Todos los productos que se hallan registrado)
             * @param Almacen
             * @param folio
             */

            public void crearArchivoPDFGeneral(String Almacen, String folio){
                        try {

                            String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                            File archivo = new File(pdfPath, "Inventario " + Almacen + "-" + folio + ".pdf");
                            OutputStream outputStream = new FileOutputStream(archivo);

                            PdfWriter pdfWriter = new PdfWriter(String.valueOf(archivo));
                            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                            Document document = new Document(pdfDocument);

                            float[] width = {100f, 200f, 80f, 100f, 100f,80f};
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
                            table.addCell(new Cell(1, 2).add(new Paragraph("Inventario " + Almacen).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
                            table.addCell(new Cell().setBorder(Border.NO_BORDER));
                            table.addCell(new Cell(1, 2).add(new Paragraph("Fecha: \n" +folio)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

                            table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                            table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                            table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                            table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                            table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                            table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));


                            table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("CODIGO").setTextAlignment(TextAlignment.CENTER)));
                            table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("DESCRIPCION").setTextAlignment(TextAlignment.CENTER)));
                            table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("UNIDAD DE MEDIDA").setTextAlignment(TextAlignment.CENTER)));
                            table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("EXISTENCIA ACTUAL").setTextAlignment(TextAlignment.CENTER)));
                            table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("CONTADO").setTextAlignment(TextAlignment.CENTER)));
                            table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("DIFERENCIA").setTextAlignment(TextAlignment.CENTER)));


                            DatabaseOpenHelper dbh = new DatabaseOpenHelper(context, "Inventario", null, 1);
                            SQLiteDatabase database = dbh.getWritableDatabase();

                            Cursor fila = database.rawQuery
                                    ("select codigo,nombre,unidad,actual,contado,costo from productos where Folio='" + folio + "' and almacen='"+Almacen+"'", null);

                            if (fila.moveToFirst()) {
                                do {

                                    table.addCell(new Cell().add(new Paragraph(fila.getString(0))));
                                    table.addCell(new Cell().add(new Paragraph(fila.getString(1))));
                                    table.addCell(new Cell().add(new Paragraph(fila.getString(2)).setTextAlignment(TextAlignment.CENTER)));
                                    table.addCell(new Cell().add(new Paragraph(fila.getString(3)).setTextAlignment(TextAlignment.CENTER)));
                                    table.addCell(new Cell().add(new Paragraph(fila.getString(4)).setTextAlignment(TextAlignment.CENTER)));
                                    String diferencia="0";
                                    if(Double.parseDouble(fila.getString(3)) < Double.parseDouble(fila.getString(4))){
                                        diferencia= String.valueOf(Double.parseDouble(fila.getString(4)) - Double.parseDouble(fila.getString(3)));
                                    }else if(Double.parseDouble(fila.getString(3)) > Double.parseDouble(fila.getString(4))){
                                        diferencia= String.valueOf(Double.parseDouble(fila.getString(3)) - Double.parseDouble(fila.getString(4)));
                                    }
                                    table.addCell(new Cell().add(new Paragraph(diferencia).setTextAlignment(TextAlignment.CENTER)));

                                } while (fila.moveToNext());

                            }
                            database.close();
                            document.add(table);
                            document.close();
                            Toast.makeText(context, "Documento creado", Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, folio, Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            Toast.makeText(context, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

            /**
             * Archivo pdf de inventario Sobrantes (Todos los productos que se hallan registrado)
             * @param Almacen
             * @param folio
             */
            public void crearArchivoPDFSobrantes(String Almacen, String folio){
                try {

                    String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                    File archivo = new File(pdfPath, "Inventario Sobrantes " + Almacen + "-" + folio + ".pdf");
                    OutputStream outputStream = new FileOutputStream(archivo);

                    PdfWriter pdfWriter = new PdfWriter(String.valueOf(archivo));
                    PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                    Document document = new Document(pdfDocument);

                    float[] width = {100f, 200f, 80f, 100f, 100f,80f};
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
                    table.addCell(new Cell(1, 2).add(new Paragraph("Inventario Existencias Sobrantes " + Almacen).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().setBorder(Border.NO_BORDER));
                    table.addCell(new Cell(1, 2).add(new Paragraph("Fecha: \n" +folio)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

                    table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));

                    table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("CODIGO").setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("DESCRIPCION").setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("UNIDAD DE MEDIDA").setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("EXISTENCIA ACTUAL").setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("CONTADO").setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("DIFERENCIA").setTextAlignment(TextAlignment.CENTER)));


                    DatabaseOpenHelper dbh = new DatabaseOpenHelper(context, "Inventario", null, 1);
                    SQLiteDatabase database = dbh.getWritableDatabase();

                    Cursor fila = database.rawQuery
                            ("select codigo,nombre,unidad,actual,contado,costo from productos where Folio='" + folio + "' and almacen='"+Almacen+"'", null);

                    if (fila.moveToFirst()) {
                        do {
                            if (Double.parseDouble(fila.getString(3)) < Double.parseDouble(fila.getString(4))) {
                                table.addCell(new Cell().add(new Paragraph(fila.getString(0))));
                                table.addCell(new Cell().add(new Paragraph(fila.getString(1))));
                                table.addCell(new Cell().add(new Paragraph(fila.getString(2)).setTextAlignment(TextAlignment.CENTER)));
                                table.addCell(new Cell().add(new Paragraph(fila.getString(3)).setTextAlignment(TextAlignment.CENTER)));
                                table.addCell(new Cell().add(new Paragraph(fila.getString(4)).setTextAlignment(TextAlignment.CENTER)));
                                table.addCell(new Cell().add(new Paragraph(String.valueOf(Double.parseDouble(fila.getString(4)) - Double.parseDouble(fila.getString(3)))).setTextAlignment(TextAlignment.CENTER)));

                            }
                        } while (fila.moveToNext());

                    }
                    database.close();
                    document.add(table);
                    document.close();
                    Toast.makeText(context, "Documento creado", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, folio, Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Toast.makeText(context, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            /**
             * Archivo pdf de inventario Faltantes(Todos los productos que se hallan registrado)
             * @param Almacen
             * @param folio
             */
            public void crearArchivoPDFFaltantes(String Almacen, String folio){
                try {

                    String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                    File archivo = new File(pdfPath, "Inventario Faltante " + Almacen + "-" + folio + ".pdf");
                    OutputStream outputStream = new FileOutputStream(archivo);

                    PdfWriter pdfWriter = new PdfWriter(String.valueOf(archivo));
                    PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                    Document document = new Document(pdfDocument);

                    float[] width = {100f, 200f, 80f, 100f, 100f, 80f};
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
                    table.addCell(new Cell(1, 3).add(new Paragraph("Inventario Existencias Faltantes " + Almacen).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
                    table.addCell(new Cell(1, 2).add(new Paragraph("Fecha: \n" +folio)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));


                    table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));

                    table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("CODIGO").setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("DESCRIPCION").setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("UNIDAD DE MEDIDA").setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("EXISTENCIA ACTUAL").setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("CONTADO").setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(46, 134, 193)).add(new Paragraph("DIFERENCIA").setTextAlignment(TextAlignment.CENTER)));


                    DatabaseOpenHelper dbh = new DatabaseOpenHelper(context, "Inventario", null, 1);
                    SQLiteDatabase database = dbh.getWritableDatabase();

                    Cursor fila = database.rawQuery
                            ("select codigo,nombre,unidad,actual,contado,costo from productos where Folio='" + folio + "' and almacen='"+Almacen+"'", null);

                    if (fila.moveToFirst()) {
                        do {
                            if (Double.parseDouble(fila.getString(3)) > Double.parseDouble(fila.getString(4))) {
                                table.addCell(new Cell().add(new Paragraph(fila.getString(0))));
                                table.addCell(new Cell().add(new Paragraph(fila.getString(1))));
                                table.addCell(new Cell().add(new Paragraph(fila.getString(2)).setTextAlignment(TextAlignment.CENTER)));
                                table.addCell(new Cell().add(new Paragraph(fila.getString(3)).setTextAlignment(TextAlignment.CENTER)));
                                table.addCell(new Cell().add(new Paragraph(fila.getString(4)).setTextAlignment(TextAlignment.CENTER)));
                                table.addCell(new Cell().add(new Paragraph(String.valueOf(Double.parseDouble(fila.getString(3)) - Double.parseDouble(fila.getString(4)))).setTextAlignment(TextAlignment.CENTER)));

                            }
                        } while (fila.moveToNext());

                    }
                    database.close();
                    document.add(table);
                    document.close();
                    Toast.makeText(context, "Documento creado", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, folio, Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Toast.makeText(context, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            public void CrearArchivoExcelSobrantes(String Almacen, String folio){
                try {
                    Workbook workbook = new HSSFWorkbook();
                    org.apache.poi.ss.usermodel.Cell celda = null;
                    org.apache.poi.ss.usermodel.Cell celda1 = null;
                    org.apache.poi.ss.usermodel.Cell celda2 = null;
                    CellStyle cellStyle = workbook.createCellStyle();
                    cellStyle.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
                    cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

                    Sheet sheet = null;
                    Sheet sheet1 = null;
                    Sheet sheet2 = null;
                    sheet = workbook.createSheet("Existencias FALTANTES");
                    sheet1 = workbook.createSheet("Existencias SOBRANTES");
                    sheet2 = workbook.createSheet("INVENTARIO GENERAL");

                    Row row = null;
                    Row row1 = null;
                    Row row2 = null;

                    row = sheet.createRow(0);
                    row1 = sheet1.createRow(0);
                    row2 = sheet2.createRow(0);


                    /**
                     *   Archivo Existencias FALTANTES
                     */

                    celda = row.createCell(0);
                    celda.setCellValue("CODIGO");

                    celda = row.createCell(1);
                    celda.setCellValue("DESCRIPCION");

                    celda = row.createCell(2);
                    celda.setCellValue("UNIDAD DE MEDIDA");

                    celda = row.createCell(3);
                    celda.setCellValue("EXISTENCIA ACTUAL");

                    celda = row.createCell(4);
                    celda.setCellValue("EXISTENCIA CONTADA");

                    celda = row.createCell(5);
                    celda.setCellValue("DIFERENCIA");

                    celda = row.createCell(6);
                    celda.setCellValue("COSTO UNITARIO");

                    celda = row.createCell(7);
                    celda.setCellValue("COSTO");


                    /**
                     *    Archivo existencias SOBRANTES
                     */

                    celda1 = row1.createCell(0);
                    celda1.setCellValue("CODIGO");

                    celda1 = row1.createCell(1);
                    celda1.setCellValue("DESCRIPCION");

                    celda1 = row1.createCell(2);
                    celda1.setCellValue("UNIDAD DE MEDIDA");

                    celda1 = row1.createCell(3);
                    celda1.setCellValue("EXISTENCIA ACTUAL");

                    celda1 = row1.createCell(4);
                    celda1.setCellValue("EXISTENCIA CONTADA");

                    celda1 = row1.createCell(5);
                    celda1.setCellValue("DIFERENCIA");

                    celda1 = row1.createCell(6);
                    celda1.setCellValue("COSTO UNITARIO");

                    celda1 = row1.createCell(7);
                    celda1.setCellValue("COSTO");

                    /**
                     *  Archivo Inventario General
                     */

                    celda2 = row2.createCell(0);
                    celda2.setCellValue("CODIGO");

                    celda2 = row2.createCell(1);
                    celda2.setCellValue("DESCRIPCION");

                    celda2 = row2.createCell(2);
                    celda2.setCellValue("UNIDAD DE MEDIDA");

                    celda2 = row2.createCell(3);
                    celda2.setCellValue("EXISTENCIA ACTUAL");

                    celda2 = row2.createCell(4);
                    celda2.setCellValue("EXISTENCIA CONTADA");

                    celda2 = row2.createCell(5);
                    celda2.setCellValue("DIFERENCIA");

                    celda2 = row2.createCell(6);
                    celda2.setCellValue("COSTO UNITARIO");

                    celda2 = row2.createCell(7);
                    celda2.setCellValue("COSTO");


                    /**
                     * Consulta a la base de datos SQLITE
                     */
                    DatabaseOpenHelper dbh = new DatabaseOpenHelper(context, "Inventario", null, 1);
                    SQLiteDatabase database = dbh.getWritableDatabase();

                    Cursor fila = database.rawQuery
                            ("select codigo,nombre,unidad,actual,contado,costo from productos where Folio='" + folio + "' and almacen='" + Almacen + "'", null);
                    double TInventarioFaltante = 0;
                    double TInventarioSobrante = 0;
                    double TinventarioGeneral = 0;
                    int filaSobrantes = 0;
                    int filaFaltantes = 0;
                    int filaGeneral = 0;
                    if (fila.moveToFirst()) {
                        int l = 1;
                        do {

                            if (Double.parseDouble(fila.getString(3)) > Double.parseDouble(fila.getString(4))) {
                                int i = 0;
                                row = sheet.createRow(l);

                                celda = row.createCell(i);
                                celda.setCellValue(fila.getString(0));

                                i++;
                                celda = row.createCell(i);
                                celda.setCellValue(fila.getString(1));

                                i++;
                                celda = row.createCell(i);
                                celda.setCellValue(fila.getString(2));

                                i++;
                                celda = row.createCell(i);
                                celda.setCellValue(fila.getString(3));

                                i++;
                                celda = row.createCell(i);
                                celda.setCellValue(fila.getString(4));

                                i++;
                                String diferencia = String.valueOf(Double.parseDouble(fila.getString(3)) - Double.parseDouble(fila.getString(4)));
                                celda = row.createCell(i);
                                celda.setCellValue(diferencia);

                                i++;
                                celda = row.createCell(i);
                                celda.setCellValue(fila.getString(5));

                                i++;
                                double Total = (Double.parseDouble(diferencia) * Double.parseDouble(fila.getString(5)));
                                celda = row.createCell(i);
                                celda.setCellValue(String.valueOf(Total));

                                TInventarioFaltante -= Total;
                                l++;
                                filaFaltantes = l;
                            }


                        } while (fila.moveToNext());

                    }

                    if (fila.moveToFirst()) {
                        int l = 1;
                        do {
                            if (Double.parseDouble(fila.getString(3)) < Double.parseDouble(fila.getString(4))) {
                                int i = 0;
                                row1 = sheet1.createRow(l);

                                celda1 = row1.createCell(i);
                                celda1.setCellValue(fila.getString(0));

                                i++;
                                celda1 = row1.createCell(i);
                                celda1.setCellValue(fila.getString(1));

                                i++;
                                celda1 = row1.createCell(i);
                                celda1.setCellValue(fila.getString(2));

                                i++;
                                celda1 = row1.createCell(i);
                                celda1.setCellValue(fila.getString(3));

                                i++;
                                celda1 = row1.createCell(i);
                                celda1.setCellValue(fila.getString(4));

                                String diferencia = String.valueOf(Double.parseDouble(fila.getString(4)) - Double.parseDouble(fila.getString(3)));
                                i++;
                                celda1 = row1.createCell(i);
                                celda1.setCellValue(diferencia);

                                i++;
                                celda1 = row1.createCell(i);
                                celda1.setCellValue(fila.getString(5));

                                i++;
                                double Total = (Double.parseDouble(diferencia) * Double.parseDouble(fila.getString(5)));
                                celda1 = row1.createCell(i);
                                celda1.setCellValue(String.valueOf(Total));

                                TInventarioSobrante += Total;
                                l++;
                                filaSobrantes = l;
                            }

                        } while (fila.moveToNext());

                    }

                    if (fila.moveToFirst()) {
                        int l = 1;
                        do {
                            int i = 0;

                            row2 = sheet2.createRow(l);

                            celda2 = row2.createCell(i);
                            celda2.setCellValue(fila.getString(0));
                            i++;

                            celda2 = row2.createCell(i);
                            celda2.setCellValue(fila.getString(1));
                            i++;


                            celda2 = row2.createCell(i);
                            celda2.setCellValue(fila.getString(2));
                            i++;

                            celda2 = row2.createCell(i);
                            celda2.setCellValue(fila.getString(3));
                            i++;

                            celda2 = row2.createCell(i);
                            celda2.setCellValue(fila.getString(4));

                            i++;
                            String diferencia = "0";
                            if (Double.parseDouble(fila.getString(3)) < Double.parseDouble(fila.getString(4))) {
                                diferencia = String.valueOf(Double.parseDouble(fila.getString(4)) - Double.parseDouble(fila.getString(3)));
                                double Total = ((Double.parseDouble(diferencia)) * (Double.parseDouble(fila.getString(5))));
                                TinventarioGeneral += Total;

                            } else if (Double.parseDouble(fila.getString(3)) > Double.parseDouble(fila.getString(4))) {
                                diferencia = String.valueOf(Double.parseDouble(fila.getString(3)) - Double.parseDouble(fila.getString(4)));
                                double Total = ((Double.parseDouble(diferencia)) * (Double.parseDouble(fila.getString(5))));
                                TinventarioGeneral -= Total;
                            }
                            celda2 = row2.createCell(i);
                            celda2.setCellValue(diferencia);

                            i++;
                            celda2 = row2.createCell(i);
                            celda2.setCellValue(fila.getString(5));

                            i++;
                            double Total = (Double.parseDouble(diferencia)) * (Double.parseDouble(fila.getString(5)));
                            celda2 = row2.createCell(i);
                            celda2.setCellValue(String.valueOf(Total));


                            l++;
                            filaGeneral = l;
                        } while (fila.moveToNext());

                    }
                    row = sheet.createRow(filaFaltantes + 1);
                    celda = row.createCell(6);
                    celda.setCellValue("TOTAL INVENTARIO:");

                    celda = row.createCell(7);
                    celda.setCellValue("$ " + TInventarioFaltante);

                    row1 = sheet1.createRow(filaSobrantes + 1);
                    celda1 = row1.createCell(6);
                    celda1.setCellValue("TOTAL INVENTARIO:");

                    celda1 = row1.createCell(7);
                    celda1.setCellValue("$ " + TInventarioSobrante);

                    row2 = sheet2.createRow(filaGeneral + 1);
                    celda2 = row2.createCell(6);
                    celda2.setCellValue("TOTAL INVENTARIO:");

                    celda2 = row2.createCell(7);
                    celda2.setCellValue("$ " + TinventarioGeneral);


                    database.close();
                    String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                    File file2 = new File(pdfPath, "Inventario " + Almacen + "-" + folio + ".xls");
                    FileOutputStream outputStream = null;

                    try {
                        outputStream = new FileOutputStream(file2);
                        workbook.write(outputStream);
                        Toast.makeText(context, "Documento creado", Toast.LENGTH_SHORT).show();

                    } catch (Exception ex) {
                        Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

    @Override
    public void Resultado(String Id) {

        if(Id=="1"){
            crearArchivoPDFSobrantes(Almacen,Folio);
        } else if(Id=="2"){
            crearArchivoPDFFaltantes(Almacen,Folio);
        }
        else if(Id=="3"){
            crearArchivoPDFGeneral(Almacen,Folio);
        }
        else if(Id=="4"){
            CrearArchivoExcelSobrantes(Almacen,Folio);
        }

    }
}