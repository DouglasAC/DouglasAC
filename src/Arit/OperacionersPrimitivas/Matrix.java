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
import Arit.Estructuras.Lista;
import Arit.Estructuras.Matris;
import Arit.Estructuras.Nodo;
import Arit.Estructuras.Vector;
import Error.ErrorAr;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class Matrix extends Funcion {

    public Matrix(String identificador, LinkedList<Parametro> parametros, LinkedList<NodoAst> sentencias, int fila, int columna) {
        super(identificador, parametros, sentencias, fila, columna);
    }

    @Override
    public Object ejecutar(Entorno en) {

        Simbolo sim_data = en.getSimbolo("parametro&&data&&matris01210");
        Simbolo sim_filas = en.getSimbolo("parametro&&fila&&matris01210");
        Simbolo sim_columnas = en.getSimbolo("parametro&&columna&&matris01210");

        int vfila = 0;
        int vcolumna = 0;

        if (sim_filas.getValor() instanceof Vector) {
            Vector v = (Vector) sim_filas.getValor();
            Object vf = v.getValores().get(0).valor;
            if (vf instanceof Integer) {
                vfila = (int) vf;
            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La fila no es de tipo integer", this.fila, this.columna));
                return null;
            }
        } else {
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La fila no es de tipo integer", this.fila, this.columna));
            return null;
        }

        if (sim_columnas.getValor() instanceof Vector) {
            Vector v = (Vector) sim_columnas.getValor();
            Object vf = v.getValores().get(0).valor;
            if (vf instanceof Integer) {
                vcolumna = (int) vf;
            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La Columna no es de tipo integer", this.fila, this.columna));
                return null;
            }
        } else {
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La Columna no es de tipo integer", this.fila, this.columna));
            return null;
        }

        if (vfila > 0 && vcolumna > 0) {
            Matris nueva = new Matris(vfila, vcolumna);
            ArrayList<Nodo> valor = null;
            Object val = sim_data.getValor();
            boolean esLista = false;
            if (val instanceof Vector) {
                valor = ((Vector) val).getValores();
            } else if (val instanceof Lista) {
                valor = ((Lista) val).valores;
                esLista = true;
            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La data de la matris debe ser un Vector o una Lista con primitivos", this.fila, this.columna));
                return null;
            }
            if (valor != null) {
                int actualLista = 0;
                for (int x = 0; x < vcolumna; x++) {
                    for (int y = 0; y < vfila; y++) {
                        if (actualLista == valor.size()) {
                            actualLista = 0;
                            valor = (ArrayList) valor.clone();
                        }
                        Nodo val_insertar = valor.get(actualLista);
                        if (!esLista) {
                            nueva.insertar(y, x, val_insertar.valor);
                            actualLista++;
                        } else {
                            if (val_insertar.valor instanceof Vector) {
                                Vector v = (Vector) val_insertar.valor;
                                if (v.tamaño() == 1) {
                                    nueva.insertar(y, x, v.getValores().get(0).valor);
                                    actualLista++;
                                } else {
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La matris solo almacena primitivos", this.fila, this.columna));
                                    return null;
                                }

                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La matris solo almacena primitivos", this.fila, this.columna));
                                return null;
                            }
                        }
                    }
                }
            }
            nueva.ponerTipo();
            return nueva;
        } else {
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La fila y columna de la matriz debe ser mayor a 0", this.fila, this.columna));
        }

        return null;
    }

}
