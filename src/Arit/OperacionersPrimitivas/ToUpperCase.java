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
public class ToUpperCase extends Funcion {

    public ToUpperCase(String identificador, LinkedList<Parametro> parametros, LinkedList<NodoAst> sentencias, int fila, int columna) {
        super(identificador, parametros, sentencias, fila, columna);
    }

    @Override
    public Object ejecutar(Entorno en) {
        try {
            Simbolo sim = en.getSimbolo("parametro&&expresion&&touppercase01210");
            Object val = sim.getValor();
            if (val instanceof Vector) {
                Vector vec = (Vector) val;
                if (vec.tamaño() == 1) {
                    if (vec.valores.get(0).valor instanceof String) {
                        String cad = (String) vec.valores.get(0).valor;
                        String upp = cad.toUpperCase();
                        Vector nuevo = new Vector();
                        nuevo.agregarFinal(new Nodo(upp));
                        nuevo.ponerTipoNuevo();
                        System.out.println("-------- " + upp);
                        return nuevo;

                    } else {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La expresion en la funcion ToUpperCase no es String", this.fila, this.columna));
                    }
                } else {
                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La expresion en la funcion ToUpperCase es un vector con mas de una posicion", this.fila, this.columna));
                }

            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La expresion en la funcion ToUpperCase no es String ni Vector de String", this.fila, this.columna));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
}
