package com.casacru.casacruapp.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.casacru.casacruapp.Modelo.ModeloFolios;
import com.casacru.casacruapp.Modelo.ModeloPrecios;
import com.casacru.casacruapp.Modelo.ModeloProductos;
import com.casacru.casacruapp.R;

import java.util.ArrayList;

public class AdaptadorFolios extends RecyclerView.Adapter<AdaptadorFolios.ViewHolderDatos>{
    ArrayList<ModeloFolios> ListDatos;
    Context context;
    final OnItemClickListenerr listenerr;

    public interface OnItemClickListenerr{
        void onItemClick(ModeloFolios item);
    }

    public AdaptadorFolios(ArrayList<ModeloFolios> listDatos, Context context, AdaptadorFolios.OnItemClickListenerr listen) {
        ListDatos = listDatos;
        this.context = context;
        listenerr= (OnItemClickListenerr) listen;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_folios,parent,false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.bindData(ListDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return ListDatos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView txtFolio,txtAlmacen;
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);

            txtFolio=(TextView) itemView.findViewById(R.id.txtFolio);
            txtAlmacen=(TextView) itemView.findViewById(R.id.txtalmacen);
        }
        void bindData(final ModeloFolios item){
            txtFolio.setText(item.getFolio());
            txtAlmacen.setText(item.getAlmacen());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenerr.onItemClick(item);
                }
            });
        }
    }
}
