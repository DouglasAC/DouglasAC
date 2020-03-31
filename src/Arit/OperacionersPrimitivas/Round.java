/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.OperacionersPrimitivas;

import Arit.AltaAbstraccion.NodoAst;
import Arit.Entorno.Entorno;
import Arit.Entorno.Funcion;
import Arit.Entorno.Parametro;
import Arit.Entorno.Simbolo;
import Arit.Estructuras.Nodo;
import Arit.Estructuras.Vector;
import Error.ErrorAr;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class Round extends Funcion {

    public Round(String identificador, LinkedList<Parametro> parametros, LinkedList<NodoAst> sentencias, int fila, int columna) {
        super(identificador, parametros, sentencias, fila, columna);
    }

    @Override
    public Object ejecutar(Entorno en) {
        try {
            Simbolo sim = en.getSimbolo("parametro&&expresion&&round01210");
            Object val = sim.getValor();
            if (val instanceof Vector) {
                Vector vec = (Vector) val;
                if (vec.tamaño() == 1) {
                    if (vec.valores.get(0).valor instanceof Double) {
                        double dou = (double) vec.valores.get(0).valor;
                        int trun = (int) (dou + 0.5);
                        Vector nuevo = new Vector();
                        nuevo.agregarFinal(new Nodo(trun));
                        nuevo.ponerTipoNuevo();
                        return nuevo;

                    } else {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La expresion en la funcion Round no es Numeric ", this.fila, this.columna));
                    }
                } else {
                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La expresion en la funcion Round es un vector con mas de una posicion", this.fila, this.columna));
                }

            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La expresion en la funcion Round no es Numeric ni Vector de Numeric", this.fila, this.columna));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
