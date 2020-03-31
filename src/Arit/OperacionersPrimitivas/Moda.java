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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author ddani
 */
public class Moda extends Funcion {

    public Moda(String identificador, LinkedList<Parametro> parametros, LinkedList<NodoAst> sentencias, int fila, int columna) {
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
                    double valormedio = mediana(numeros, true, tval);
                    if (vec.tipo.equalsIgnoreCase("integer")) {
                        if (!(valormedio % 1 > 0)) {
                            Vector nuevo = new Vector();
                            nuevo.agregarFinal(new Nodo((int) valormedio));
                            nuevo.ponerTipoNuevo();
                            return nuevo;
                        }
                    }
                    Vector nuevo = new Vector();
                    nuevo.agregarFinal(new Nodo(valormedio));
                    nuevo.ponerTipoNuevo();
                    return nuevo;
                } else {
                    double valormedio = mediana(numeros, false, null);
                    if (vec.tipo.equalsIgnoreCase("integer")) {
                        if (!(valormedio % 1 > 0)) {
                            Vector nuevo = new Vector();
                            nuevo.agregarFinal(new Nodo((int) valormedio));
                            nuevo.ponerTipoNuevo();
                            return nuevo;
                        }
                    }
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

    public double mediana(ArrayList<Object> numeros, boolean trim, Object tval) {
        double val = 0.0;
        double vtrim = 0.0;

        if (trim) {
            if (tval instanceof Integer) {
                vtrim = (double) ((int) tval);
            } else if (tval instanceof Double) {
                vtrim = (double) tval;
            }
        }
        LinkedList<Double> nuevos = new LinkedList<>();
        for (Object o : numeros) {
            double comp = 0.0;
            if (o instanceof Integer) {
                comp = (int) o;
            } else if (o instanceof Double) {
                comp = (double) o;
            }
            if (trim) {
                if (vtrim <= comp) {
                    nuevos.add(comp);
                } else {

                }
            } else {
                nuevos.add(comp);
            }
        }
        if (nuevos.size() > 0) {
            for (int i = 0; i < nuevos.size() - 1; i++) {
                for (int j = 0; j < nuevos.size() - 1; j++) {
                    if (nuevos.get(j) > nuevos.get(j + 1)) {
                        double tmp = nuevos.get(j + 1);
                        nuevos.set(j + 1, nuevos.get(j));
                        nuevos.set(j, tmp);
                    }
                }
            }

            Map m = new HashMap();

            for (Object elemento : nuevos) {
                if (m.containsKey(elemento)) {
                    m.put(elemento, (int) m.get(elemento) + 1);
                } else {
                    m.put(elemento, 1);
                }
            }

            int repeticiones = 0;
            ArrayList moda = new ArrayList();
            Iterator iter = m.entrySet().iterator();
            while (iter.hasNext()) {
                Entry e = (Entry) iter.next();

                if ((int) e.getValue() > repeticiones) {
                    moda.clear();
                    moda.add(e.getKey());
                    repeticiones = (int) e.getValue();
                } else if ((int) e.getValue() == repeticiones) {
                    moda.add(e.getKey());
                }

            }
            val = (double) moda.get(0);
        } else {
            nuevos = new LinkedList<>();
            for (Object o : numeros) {
                double comp = 0.0;
                if (o instanceof Integer) {
                    comp = (int) o;
                } else if (o instanceof Double) {
                    comp = (double) o;
                }
                nuevos.add(comp);
            }

            for (int i = 0; i < nuevos.size() - 1; i++) {
                for (int j = 0; j < nuevos.size() - 1; j++) {
                    if (nuevos.get(j) > nuevos.get(j + 1)) {
                        double tmp = nuevos.get(j + 1);
                        nuevos.set(j + 1, nuevos.get(j));
                        nuevos.set(j, tmp);
                    }
                }
            }

            Map m = new HashMap();

            for (Object elemento : nuevos) {
                if (m.containsKey(elemento)) {
                    m.put(elemento, (int) m.get(elemento) + 1);
                } else {
                    m.put(elemento, 1);
                }
            }

            int repeticiones = 0;
            ArrayList moda = new ArrayList();
            Iterator iter = m.entrySet().iterator();
            while (iter.hasNext()) {
                Entry e = (Entry) iter.next();

                if ((int) e.getValue() > repeticiones) {
                    moda.clear();
                    moda.add(e.getKey());
                    repeticiones = (int) e.getValue();
                } else if ((int) e.getValue() == repeticiones) {
                    moda.add(e.getKey());
                }

            }
            val = (double) moda.get(0);
        }

        return val;
    }

}
