package com.casacru.casacruapp.Adaptador;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.casacru.casacruapp.Modelo.ModeloProductos;
import com.casacru.casacruapp.R;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ListadoAdapter extends RecyclerView.Adapter<ListadoAdapter.ViewHolderDatos>{
    private ArrayList<ModeloProductos> ListDatos;
    private ArrayList<ModeloProductos> Listaoriginal;
    private Context context;
    final OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(ModeloProductos item);
    }

    public ListadoAdapter(ArrayList<ModeloProductos> listDatos, Context context, OnItemClickListener listen) {
        ListDatos = listDatos;
        Listaoriginal=new ArrayList<>();
        Listaoriginal.addAll(ListDatos);
        this.context=context;
        this.listener = listen;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_listadoproductos,null,false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.bindData(ListDatos.get(position));
    }

    public void filtrado(String txtbuscar){
        int longitud=txtbuscar.length();
        if(longitud==0){
            ListDatos.clear();
            ListDatos.addAll(Listaoriginal);
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List<ModeloProductos> collection=ListDatos.stream().filter(i -> i.getDescripcion().toLowerCase()
                        .contains(txtbuscar.toLowerCase())).collect(Collectors.toList());
                ListDatos.clear();
                ListDatos.addAll(collection);
            }else{
                for (ModeloProductos m: Listaoriginal){
                    if (m.getDescripcion().toLowerCase().contains(txtbuscar.toLowerCase())){
                        ListDatos.add(m);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return ListDatos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView txtcod, txtnom;
        String txtId;
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            txtcod = itemView.findViewById(R.id.txtCodProd);
            txtnom = itemView.findViewById(R.id.txtNomProd);
        }

            void bindData(final ModeloProductos item){
                txtcod.setText(item.getCodProducto());
                txtnom.setText(item.getDescripcion());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(item);
                    }
                });
            }
        }


}
