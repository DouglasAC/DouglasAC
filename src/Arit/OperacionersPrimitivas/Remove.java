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
public class Remove extends Funcion {

    public Remove(String identificador, LinkedList<Parametro> parametros, LinkedList<NodoAst> sentencias, int fila, int columna) {
        super(identificador, parametros, sentencias, fila, columna);
    }

    @Override
    public Object ejecutar(Entorno en) {
        try {
            Simbolo sim_o = en.getSimbolo("parametro&&original&&remove01210");
            Simbolo sim_r = en.getSimbolo("parametro&&remove&&remove01210");

            Object oval = sim_o.getValor();
            Object rval = sim_r.getValor();

            if (oval instanceof Vector) {
                if (rval instanceof Vector) {
                    Vector ovec = (Vector) oval;
                    if (ovec.tamaño() == 1) {
                        Vector rvec = (Vector) rval;
                        if (rvec.tamaño() == 1) {
                            if (ovec.valores.get(0).valor instanceof String) {
                                if (rvec.valores.get(0).valor instanceof String) {
                                    String original = (String) ovec.valores.get(0).valor;
                                    String remove = (String) rvec.valores.get(0).valor;
                                    String nu = original.replace(remove, "");

                                    Vector nuevo = new Vector();
                                    nuevo.agregarFinal(new Nodo(nu));
                                    nuevo.ponerTipoNuevo();
                                    System.out.println("-------- " + original);
                                    System.out.println("-------- " + remove);
                                    System.out.println("-------- " + nu);
                                    return nuevo;
                                } else {
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El valor de la expresion 2 no es String", this.fila, this.columna));
                                }
                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El valor de la expresion 1 no es String", this.fila, this.columna));
                            }
                        } else {
                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El vector de expresion 2 en Remove tienen mas de un elemento : " + ovec.tamaño(), this.fila, this.columna));
                        }
                    } else {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El vector de expresion 1 en Remove tienen mas de un elemento : " + ovec.tamaño(), this.fila, this.columna));
                    }
                } else {
                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La expresion 2 en la funcion Remove no es String ni Vector de String", this.fila, this.columna));
                }
            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La expresion 1 en la funcion Remove no es String ni Vector de String", this.fila, this.columna));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
