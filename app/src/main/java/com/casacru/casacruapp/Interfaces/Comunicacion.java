package com.casacru.casacruapp.Interfaces;

import com.casacru.casacruapp.Modelo.ModeloTablaMaxmin;

import java.util.ArrayList;

public interface Comunicacion {

void proggresbar(boolean status);
void consultarIdProducto(ArrayList<ModeloTablaMaxmin> model);
void consultarIdProducto2(ArrayList<ModeloTablaMaxmin> model);
}
