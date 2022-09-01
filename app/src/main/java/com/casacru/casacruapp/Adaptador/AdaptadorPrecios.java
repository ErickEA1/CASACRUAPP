package com.casacru.casacruapp.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.casacru.casacruapp.Modelo.ModeloPrecios;
import com.casacru.casacruapp.R;


import java.util.ArrayList;

public class AdaptadorPrecios extends RecyclerView.Adapter<AdaptadorPrecios.ViewHolderDatos> {

    ArrayList<ModeloPrecios> ListDatos;
    Context context;


    public AdaptadorPrecios(ArrayList<ModeloPrecios> listDatos) {
        this.ListDatos = listDatos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.Descripcion.setText(ListDatos.get(position).getDescripcion());
        holder.Precio2.setText(String.valueOf(ListDatos.get(position).getPrecio2()));
        holder.IdProducto=String.valueOf(ListDatos.get(position).getIdproducto());
        holder.PrecioCalculado.setText(String.valueOf(ListDatos.get(position).getPrecioCalculado()));
        holder.precioSinIva=String.valueOf(ListDatos.get(position).getPrecioSinIva());
        holder.ChActualizar.setChecked(ListDatos.get(position).getSelected());
        holder.ChActualizar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isShown()) {
                if (buttonView.isChecked()) {
                    ListDatos.get(position).setSelected(true);

                } else {
                    ListDatos.get(position).setSelected(false);

                } holder.ChActualizar.setChecked(ListDatos.get(position).getSelected());
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListDatos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView Descripcion, Precio2, PrecioCalculado;
        String IdProducto, precioSinIva;
        CheckBox ChActualizar;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            Descripcion=(TextView) itemView.findViewById(R.id.idDesc);
            Precio2=(TextView) itemView.findViewById(R.id.Precio);
            PrecioCalculado=(TextView) itemView.findViewById(R.id.PrecioCalculado);
            ChActualizar=(CheckBox) itemView.findViewById(R.id.chMarcador);
            precioSinIva="";
            IdProducto="";
        }


    }

}