package com.casacru.casacruapp.Interfaces;

import java.sql.Connection;
import java.util.ArrayList;

public interface IProductosMensual {

void Progressbar(boolean b);
void RecibirMeses(float e, float f, float m, float a, float ma, float j, float ju, float ag,float s, float o,float n, float d);
    void Consulta1(Connection con, String IdProducto, String idAlma, String Doc1, String Doc2);
    void Consulta2(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2);
    void Consulta3(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2);
    void Consulta4(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2);
    void Consulta5(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2);
    void Consulta6(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2);
    void Consulta7(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2);
    void Consulta8(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2);
    void Consulta9(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2);
    void Consulta10(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2);
    void Consulta11(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2);
    void Consulta12(Connection con, String IdProducto, String idAlma, String Doc1,String Doc2);


}
