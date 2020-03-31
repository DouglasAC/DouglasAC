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
import Arit.Estructuras.Nodo;
import Arit.Estructuras.Vector;
import Error.ErrorAr;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class Media extends Funcion {

    public Media(String identificador, LinkedList<Parametro> parametros, LinkedList<NodoAst> sentencias, int fila, int columna) {
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
                boolean trim = this.valores.size() == 2;
                Object vector = null;
                Object otrim = null;
                if (trim) {
                    vector = this.valores.get(0).getValorImplicito(en);
                    otrim = this.valores.get(1).getValorImplicito(en);
                } else {
                    if (this.valores.size() == 1) {
                        vector = this.valores.get(0).getValorImplicito(en);
                    } else {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "En la funcion mean hay mas parametros ", this.fila, this.columna));
                        return null;
                    }
                }
                Vector vec = null;
                if (vector instanceof Vector) {
                    vec = (Vector) vector;
                } else {
                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "En la funcion mean el promer parametro no es un vector", this.fila, this.columna));
                    return null;
                }
                ArrayList<Object> numeros = new ArrayList<>();
                for (Nodo ob : vec.getValores()) {
                    if (ob.valor instanceof Integer) {
                        numeros.add(ob.valor);
                    } else if (ob.valor instanceof Double) {
                        numeros.add(ob.valor);
                    } else {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "En la funcion mean el vector debe tener valores numericos", this.fila, this.columna));
                        return null;
                    }
                }
                if (trim) {
                    Vector tm = null;
                    if (otrim instanceof Vector) {
                        tm = (Vector) otrim;
                    } else {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "En la funcion mean trim debe ser numerico ", this.fila, this.columna));
                        return null;
                    }
                    Object tval = null;
                    if (tm.valores.size() == 1) {
                        if (tm.valores.get(0).valor instanceof Integer || tm.valores.get(0).valor instanceof Double) {
                            tval = tm.getValores().get(0).valor;
                        } else {
                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "En la funcion mean trim debe ser numerico ", this.fila, this.columna));
                            return null;
                        }
                    } else {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "En la funcion mean trim debe ser numerico ", this.fila, this.columna));
                        return null;
                    }
                    double valormedio = media(numeros, true, tval);
                    Vector nuevo = new Vector();
                    nuevo.agregarFinal(new Nodo(valormedio));
                    nuevo.ponerTipoNuevo();
                    return nuevo;
                } else {
                    double valormedio = media(numeros, false, null);
                    Vector nuevo = new Vector();
                    nuevo.agregarFinal(new Nodo(valormedio));
                    nuevo.ponerTipoNuevo();
                    return nuevo;
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public double media(ArrayList<Object> numeros, boolean trim, Object tval) {
        double val = 0.0;
        double vtrim = 0.0;
        int tam = numeros.size();
        if (trim) {
            if (tval instanceof Integer) {
                vtrim = (double) ((int) tval);
            } else if (tval instanceof Double) {
                vtrim = (double) tval;
            }
        }
        for (Object o : numeros) {
            double comp = 0.0;
            if (o instanceof Integer) {
                comp = (int) o;
            } else if (o instanceof Double) {
                comp = (double) o;
            }
            if (trim) {
                if (vtrim <= comp) {
                    val += comp;
                } else {
                    tam--;
                }
            } else {
                val += comp;
            }
        }

        if (tam == 0) {
            val = 0.0;
            for (Object o : numeros) {
                double comp = 0.0;
                if (o instanceof Integer) {
                    comp = (int) o;
                } else if (o instanceof Double) {
                    comp = (double) o;
                }

                val += comp;

            }
            val /= numeros.size();
        } else {
            val /= tam;
        }

        return val;
    }
}
