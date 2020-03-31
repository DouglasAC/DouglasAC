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
import Arit.Estructuras.Arreglo;
import Arit.Estructuras.Lista;
import Arit.Estructuras.Matris;
import Arit.Estructuras.Nodo;
import Arit.Estructuras.Vector;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class TypeOf extends Funcion {

    public TypeOf(String identificador, LinkedList<Parametro> parametros, LinkedList<NodoAst> sentencias, int fila, int columna) {
        super(identificador, parametros, sentencias, fila, columna);
    }

    @Override
    public Object ejecutar(Entorno en) {
        try {
            Simbolo sim = en.getSimbolo("parametro&&expresion&&typeof01210");
            Object val = sim.getValor();
            if (val instanceof Vector) {
                Vector v = (Vector) val;
                v.ponerTipoNuevo();
                String tipo = v.getTipo();
                Vector nuevo = new Vector();
                nuevo.agregarFinal(new Nodo(tipo));
                nuevo.ponerTipoNuevo();
                return nuevo;
            } else if (val instanceof Lista) {
                Vector nuevo = new Vector();
                nuevo.agregarFinal(new Nodo("list"));
                nuevo.ponerTipoNuevo();
                return nuevo;
            } else if (val instanceof Matris) {
                Matris mat = (Matris) val;
                mat.ponerTipo();
                String tipo = mat.getTipo();
                Vector nuevo = new Vector();
                nuevo.agregarFinal(new Nodo(tipo));
                nuevo.ponerTipoNuevo();
                return nuevo;
            } else if (val instanceof Arreglo) {
                Arreglo ar = (Arreglo) val;
                String tipo = ar.getTipo();
                Vector nuevo = new Vector();
                nuevo.agregarFinal(new Nodo(tipo));
                nuevo.ponerTipoNuevo();
                return nuevo;
            } else {
                String tipo = "null";
                Vector nuevo = new Vector();
                nuevo.agregarFinal(new Nodo(tipo));
                nuevo.ponerTipoNuevo();
                return nuevo;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
