package com.casacru.casacruapp.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.casacru.casacruapp.R;


public class DialogSelectAlmacen {
public interface Finalizo{
    void Resultado(String Id);
}
    private Finalizo interfaz;
    public DialogSelectAlmacen(Context context, Finalizo actividad){
        interfaz=actividad;

        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_dialog_select_almacen);

        RadioButton rbTDengui=(RadioButton) dialog.findViewById(R.id.rbTDengui);
        RadioButton rbTNopala=(RadioButton) dialog.findViewById(R.id.rbTnopala);
        RadioButton rbT61=(RadioButton) dialog.findViewById(R.id.rbT61);
        RadioButton rbAlm1=(RadioButton) dialog.findViewById(R.id.rbAlmDengui);
        RadioButton rbLAGUNAS=(RadioButton) dialog.findViewById(R.id.rbLagunas);
        RadioButton rbSD=(RadioButton) dialog.findViewById(R.id.rbTSd);
        Button btnAceptar=(Button) dialog.findViewById(R.id.btnAceptar);
        RadioButton rbTmr=(RadioButton) dialog.findViewById(R.id.rbTmr);
        ImageButton mgClose=(ImageButton) dialog.findViewById(R.id.imgClose);

        mgClose.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        dialog.dismiss();
    }
});
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rbTDengui.isChecked()){
                    interfaz.Resultado("TIENDA DENGUI");
                    dialog.dismiss();
                }else if(rbTNopala.isChecked()){
                    interfaz.Resultado("TIENDA NOPALA");
                    dialog.dismiss();
                }else if(rbAlm1.isChecked()){
                    interfaz.Resultado("ALMACEN DENGUI");
                    dialog.dismiss();
                }else if(rbT61.isChecked()){
                    interfaz.Resultado("TIENDA 61");
                    dialog.dismiss();
                }else if(rbLAGUNAS.isChecked()){
                    interfaz.Resultado("LAGUNAS OAXACA");
                    dialog.dismiss();
                }else if(rbSD.isChecked()){
                    interfaz.Resultado("SANTO DOMINGO");
                    dialog.dismiss();
                }else if(rbTmr.isChecked()) {
                    interfaz.Resultado("MATIAS ROMERO");
                    dialog.dismiss();
                }
            }
        });
        dialog.show();




    }
}