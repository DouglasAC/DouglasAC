/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.OperacionersPrimitivas;

import Arit.AltaAbstraccion.Expresion;
import Arit.AltaAbstraccion.NodoAst;
import Arit.Entorno.Entorno;
import Arit.Entorno.Funcion;
import Arit.Entorno.Parametro;
import Arit.Estructuras.Lista;
import Arit.Estructuras.Nodo;
import Error.ErrorAr;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class List extends Funcion {

    public LinkedList<Expresion> valores;

    public List(String identificador, LinkedList<Parametro> parametros, LinkedList<NodoAst> sentencias, int fila, int columna) {
        super(identificador, parametros, sentencias, fila, columna);
        this.valores = null;
    }

    public void serValores(LinkedList<Expresion> valores) {
        this.valores = valores;
    }

    @Override
    public Object ejecutar(Entorno en) {
        try {
            if (this.valores != null) {
                ArrayList<Nodo> val_exp = new ArrayList<>();
                for (Expresion exp : this.valores) {
                    Object val = exp.getValorImplicito(en);
                    if (val != null) {
                        val_exp.add(new Nodo(val));
                    } else {
                        return null;
                    }
                }
                Lista nueva = new Lista(val_exp);
                System.out.println(nueva);
                return nueva;
            }
        } catch (Exception e) {
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Error al ejecutar la funcion List", this.fila, this.columna));
        }

        return null;
    }
}
