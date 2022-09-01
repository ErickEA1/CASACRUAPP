package com.casacru.casacruapp.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.casacru.casacruapp.Modelo.DtoProductos;
import com.casacru.casacruapp.R;


import java.util.ArrayList;

public class AdaptadorProductos extends RecyclerView.Adapter<AdaptadorProductos.ViewHolderDatos> {
    ArrayList<DtoProductos> ListDatos;
    String Almacen;
    Context context;

    public AdaptadorProductos(ArrayList<DtoProductos> listDatos, String Almacen, Context context) {
        ListDatos = listDatos;
        this.Almacen=Almacen;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modal, parent,false);

        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.txtCod.setText(ListDatos.get(position).getCodigoProducto());
        holder.txtnom.setText(ListDatos.get(position).getNombreProducto());
        holder.txtcant.setText(ListDatos.get(position).getCantidad());
        holder.txtuni.setText(ListDatos.get(position).getUnidad());
        holder.chSurtido.setChecked(ListDatos.get(position).getSurtido());
     if(Almacen=="ALMACEN DENGUI"){
         holder.chSurtido.setOnCheckedChangeListener((buttonView, isChecked) -> {
             if(buttonView.isShown()) {
                 if (buttonView.isChecked()) {
                     ListDatos.get(position).setSurtido(true);

                 } else {
                     ListDatos.get(position).setSurtido(false);

                 }

             }
         });
         holder.chSurtido.setChecked(ListDatos.get(position).getSurtido());
     }else{
         holder.chSurtido.setEnabled(false);

     }
    }

    @Override
    public int getItemCount() {
        return ListDatos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView txtCod,txtnom,txtcant,txtuni;
        CheckBox chSurtido;
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);

        chSurtido=itemView.findViewById(R.id.checkBoxSurtido);
        txtCod=itemView.findViewById(R.id.textViewCod);
        txtnom=itemView.findViewById(R.id.textViewNom);
        txtcant=itemView.findViewById(R.id.textViewCant);
        txtuni=itemView.findViewById(R.id.textViewUnidad);

        }
    }
}
