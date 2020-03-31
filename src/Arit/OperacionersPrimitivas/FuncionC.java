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
import Arit.Estructuras.Vector;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class FuncionC extends Funcion {

    public FuncionC(String identificador, LinkedList<Parametro> parametros, LinkedList<NodoAst> sentencias, int fila, int columna) {
        super(identificador, parametros, sentencias, fila, columna);
    }

    LinkedList<Expresion> valores;

    public void setValores(LinkedList<Expresion> valores) {
        this.valores = valores;
    }

    @Override
    public Object ejecutar(Entorno en) {
        try {
            if (this.valores != null) {
                LinkedList<Object> nuevos = new LinkedList<>();
                boolean hayLista = false;
                for (Expresion exp : this.valores) {
                    Object val = exp.getValorImplicito(en);
                    if (val != null) {
                        nuevos.add(val);
                    } else {
                        return null;
                    }
                    if (val instanceof Lista) {
                        hayLista = true;
                    }
                }

                if (!hayLista) {
                    Vector nuevo = new Vector();
                    for (Object v : nuevos) {
                        if (v instanceof Vector) {
                            Vector vec = (Vector) v;
                            for (int x = 0; x < vec.valores.size(); x++) {
                                nuevo.agregarFinal((Nodo) vec.valores.get(x).clone());
                            }
                        }
                    }
                    nuevo.ponerTipoNuevo();
                    return nuevo;
                } else {
                    Lista nueva = new Lista();
                    for (Object o : nuevos) {
                        if (o instanceof Vector) {
                            Vector vec = (Vector) o;
                            nueva.agregarFinal(new Nodo((Vector) vec.clone()));
                        } else if (o instanceof Lista) {
                            Lista lis = (Lista) o;
                            Lista l = (Lista) lis.clone();
                            for (int x = 0; x < l.valores.size(); x++) {
                                nueva.agregarFinal(l.valores.get(x));
                            }
                        }
                    }
                    return nueva;
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
