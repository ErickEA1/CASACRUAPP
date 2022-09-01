package com.casacru.casacruapp.Dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;

import com.casacru.casacruapp.R;

public class DialogSelectArchivo {
    public interface Finalizo {
        void Resultado(String Id);
    }

    private DialogSelectArchivo.Finalizo interfaz;

    public DialogSelectArchivo(Context context, DialogSelectArchivo.Finalizo actividad) {
        interfaz = actividad;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_dialog_select_archivo);

        RadioButton pdfsobrantes = (RadioButton) dialog.findViewById(R.id.pdfsobrantes);
        RadioButton pdffaltantes = (RadioButton) dialog.findViewById(R.id.pdffaltantes);
        RadioButton pdfGeneral = (RadioButton) dialog.findViewById(R.id.xlssobrantes);
        RadioButton xlsArchivo = (RadioButton) dialog.findViewById(R.id.rbTxlsFaltantes);
        Button btnAceptar = (Button) dialog.findViewById(R.id.btnAceptar);


        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pdfsobrantes.isChecked()) {
                    interfaz.Resultado("1");
                    dialog.dismiss();
                } else if (pdffaltantes.isChecked()) {
                    interfaz.Resultado("2");
                    dialog.dismiss();
                } else if (pdfGeneral.isChecked()) {
                    interfaz.Resultado("3");
                    dialog.dismiss();
                } else if (xlsArchivo.isChecked()) {
                    interfaz.Resultado("4");
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
}