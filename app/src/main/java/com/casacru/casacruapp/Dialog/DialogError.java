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
import android.widget.TextView;

import com.casacru.casacruapp.R;

public class DialogError {

    public DialogError(Context context, String Conexion) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_dialog_error);

        TextView txterror=(TextView) dialog.findViewById(R.id.txterror);
        Button btnaceptar=(Button) dialog.findViewById(R.id.btnAceptar);
        txterror.setText("Error de conexión "+Conexion);
        btnaceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    }