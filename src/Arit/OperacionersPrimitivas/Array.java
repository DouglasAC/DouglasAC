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
import Arit.Estructuras.Nodo;
import Arit.Estructuras.Vector;
import Error.ErrorAr;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class Array extends Funcion {

    public Array(String identificador, LinkedList<Parametro> parametros, LinkedList<NodoAst> sentencias, int fila, int columna) {
        super(identificador, parametros, sentencias, fila, columna);
    }

    @Override
    public Object ejecutar(Entorno en) {

        Simbolo sim_data = en.getSimbolo("parametro&&data&&arreglo01210");
        Simbolo sim_dimensiones = en.getSimbolo("parametro&&vector&&arreglo01210");

        LinkedList<Integer> dim = new LinkedList<>();

        if (sim_dimensiones.valor instanceof Vector) {
            Vector vec = (Vector) sim_dimensiones.valor;
            for (Nodo val : vec.valores) {
                if (val.valor instanceof Integer) {
                    dim.add((int) val.valor);
                } else {
                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El vector debe de ser de tipo Integer", this.fila, this.columna));
                    return null;
                }
            }
        }
        Arreglo nuevo = new Arreglo(dim);

        ArrayList<Nodo> valores = new ArrayList<>();
        ArrayList<Nodo> nuevos = new ArrayList<>();/// clonar por la referencia
        boolean vec = false;
        if (sim_data.valor instanceof Vector) {
            valores = ((Vector) sim_data.valor).valores;
            vec = true;
            nuevo.setVector(true);
        } else if (sim_data.valor instanceof Lista) {
            valores = ((Lista) sim_data.valor).valores;
            vec = false;
            nuevo.setVector(false);
        } else {
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Los datos debes ser un vector o una Lista", this.fila, this.columna));
            return null;
        }
        int tam = nuevo.getTamaño();
        int pos = 0;
        for (int x = 0; x < tam; x++) {
            if (pos == valores.size()) {
                pos = 0;
            }
            if (vec) {
                Vector vnuevo = new Vector();
                vnuevo.agregarFinal(valores.get(pos));
                vnuevo.ponerTipoNuevo();
                nuevos.add(new Nodo(vnuevo));
            } else {
                Lista vnuevo = new Lista();
                vnuevo.agregarFinal(valores.get(pos));
                nuevos.add(new Nodo(vnuevo));
            }
            pos++;
        }

        nuevo.setValores(nuevos);

        // System.out.print(nuevo.toString());
        return nuevo;
    }

}
