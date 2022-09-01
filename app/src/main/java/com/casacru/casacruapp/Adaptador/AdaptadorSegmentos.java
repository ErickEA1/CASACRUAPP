package com.casacru.casacruapp.Adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.casacru.casacruapp.Modelo.ModeloSegmentos;
import com.casacru.casacruapp.R;


import java.util.ArrayList;

public class AdaptadorSegmentos extends RecyclerView.Adapter<AdaptadorSegmentos.ViewHolderDatos> {
    ArrayList<ModeloSegmentos> ListDatos;

    public AdaptadorSegmentos(ArrayList<ModeloSegmentos> listDatos) {
        ListDatos = listDatos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_segcontable,null,false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
    holder.txtCod.setText(ListDatos.get(position).getCodGrupo());
    holder.txtNom.setText(ListDatos.get(position).getNomGrupo());
    holder.txtseg.setText(ListDatos.get(position).getSegContble());
    }

    @Override
    public int getItemCount() {
        return ListDatos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView txtCod,txtNom,txtseg;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);

            txtCod=itemView.findViewById(R.id.textViewcodigo);
            txtNom=itemView.findViewById(R.id.textViewgrupo);
            txtseg=itemView.findViewById(R.id.textViewsegmento);
        }
    }
}
