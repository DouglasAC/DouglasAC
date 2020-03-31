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
import Arit.Estructuras.Matris;
import Arit.Estructuras.Nodo;
import Arit.Estructuras.Vector;
import Error.ErrorAr;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class Nrow extends Funcion {

    public Nrow(String identificador, LinkedList<Parametro> parametros, LinkedList<NodoAst> sentencias, int fila, int columna) {
        super(identificador, parametros, sentencias, fila, columna);
    }

    @Override
    public Object ejecutar(Entorno en) {
        try {
            Simbolo sim = en.getSimbolo("parametro&&expresion&&nrow01210");
            Object val = sim.getValor();
            if (val instanceof Matris) {
                Matris mat = (Matris) val;
                int col = mat.getFila();
                Vector nuevo = new Vector();
                nuevo.agregarFinal(new Nodo(col));
                nuevo.ponerTipoNuevo();
                return nuevo;
            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La expresion en la funcion nRow no es una matris", this.fila, this.columna));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
