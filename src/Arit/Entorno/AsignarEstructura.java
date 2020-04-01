/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.Entorno;

import Arit.AltaAbstraccion.Expresion;
import Arit.AltaAbstraccion.Instruccion;
import Arit.Estructuras.Arreglo;
import Arit.Estructuras.Lista;
import Arit.Estructuras.Matris;
import Arit.Estructuras.Nodo;
import Arit.Estructuras.Vector;
import Arit.ValorImplicito.NodoAcceso;
import Error.ErrorAr;
import Reportes.NodoDot;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class AsignarEstructura extends Instruccion {

    String identificador;
    LinkedList<NodoAcceso> accesos;
    Expresion expresion;

    public AsignarEstructura(String identificador, LinkedList<NodoAcceso> accesos, Expresion expresion, int fila, int columna) {
        super(fila, columna);
        this.identificador = identificador.toLowerCase();
        this.accesos = accesos;
        this.expresion = expresion;
    }

    @Override
    public Object ejecutar(Entorno en) {
        try {
            if (en.existe(identificador)) {
                Simbolo sim = en.getSimbolo(identificador);
                Object val = sim.getValor();
                if (this.accesos.size() == 1) {
                    if (val instanceof Arreglo) {
                        NodoAcceso a = this.accesos.get(0);
                        Object val_ac = a.getPosicion().getValorImplicito(en);
                        if (val_ac instanceof Vector) {
                            Vector pos = (Vector) val_ac;
                            if (pos.valores.get(0).valor instanceof Integer) {
                                if (a.getTipo()) {

                                    LinkedList<Integer> posIns = new LinkedList<>();
                                    posIns.add((int) pos.valores.get(0).valor);
                                    this.InsertarEnArreglo((Arreglo) val, posIns, en);
                                } else {
                                    if (val instanceof Vector) {
                                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Este tipo de acceso solo es permitido en Listas", a.getPosicion().fila, a.getPosicion().columna));
                                        return null;
                                    }
                                }
                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La posicion a la que se desea acceder debe ser de tipo Integer", a.getPosicion().fila, a.getPosicion().columna));
                                return null;
                            }

                        }
                    } else {
                        this.insertarEstructura(this.accesos.get(0), val, en);
                    }

                } else {
                    for (int x = 0; x < this.accesos.size() - 1; x++) {
                        NodoAcceso acceso = this.accesos.get(x);
                        if (val instanceof Vector) {
                            if (acceso.tipo && !acceso.matriz) {
                                Object posV = acceso.posicion.getValorImplicito(en);
                                if (posV instanceof Vector) {
                                    Vector pos = (Vector) posV;
                                    if (pos.valores.get(0).valor instanceof Integer) {
                                        Vector vec = (Vector) val;
                                        int val_pos = (int) pos.valores.get(0).valor;
                                        Object nuevo = vec.getPosicion(val_pos - 1);
                                        if (nuevo != null) {
                                            val = nuevo;
                                        } else {
                                            return null;
                                        }
                                    } else {
                                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La posicion debe ser Integer", this.fila, this.columna));
                                        return null;
                                    }
                                } else {
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La posicion debe ser Integer", this.fila, this.columna));
                                    return null;
                                }
                            } else if (acceso.matriz) {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tipo de acceso solo es permitido para matrices", this.fila, this.columna));
                                return null;
                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tipo 2 solo es posible en las listas", this.fila, this.columna));
                                return null;
                            }
                        } else if (val instanceof Lista) {
                            if (acceso.tipo && !acceso.matriz) {
                                Object posV = acceso.posicion.getValorImplicito(en);
                                if (posV instanceof Vector) {
                                    Vector pos = (Vector) posV;
                                    if (pos.valores.get(0).valor instanceof Integer) {
                                        Lista vec = (Lista) val;
                                        int val_pos = (int) pos.valores.get(0).valor;
                                        Object nuevo = vec.getPosicionSegundoAcceesoSinClon(val_pos - 1);
                                        if (nuevo != null) {
                                            val = nuevo;
                                        } else {
                                            return null;
                                        }
                                    }
                                }
                            } else if (acceso.matriz) {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tipo de acceso solo es permitido para matrices", this.fila, this.columna));
                                return null;
                            } else {
                                Object posV = acceso.posicion.getValorImplicito(en);
                                if (posV instanceof Vector) {
                                    Vector pos = (Vector) posV;
                                    if (pos.valores.get(0).valor instanceof Integer) {
                                        Lista vec = (Lista) val;
                                        int val_pos = (int) pos.valores.get(0).valor;
                                        Object nuevo = vec.getPosicionSinClon(val_pos - 1).valor;
                                        if (nuevo != null) {
                                            val = nuevo;
                                        } else {
                                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Acceso Incorrecto en la lsita", this.fila, this.columna));
                                            return null;

                                        }
                                    }
                                }
                            }
                        } else if (val instanceof Matris) {
                            if (acceso.tipo) {
                                Object posV = acceso.posicion.getValorImplicito(en);
                                if (posV instanceof Vector) {
                                    Vector pos = (Vector) posV;
                                    if (pos.valores.get(0).valor instanceof Integer) {
                                        Matris vec = (Matris) val;
                                        int val_pos = (int) pos.valores.get(0).valor;
                                        Object nuevo = vec.ObtenerPosicion(val_pos - 1);
                                        if (nuevo != null) {
                                            val = nuevo;
                                        } else {
                                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Acceso Incorrecto en la Matris", this.fila, this.columna));
                                            return null;

                                        }
                                    }
                                }
                            } else if (acceso.matriz) {
                                if (acceso.posicion != null && acceso.posicionY != null) {
                                    Object posV = acceso.posicion.getValorImplicito(en);
                                    Object posY = acceso.posicionY.getValorImplicito(en);
                                    if (posV instanceof Vector && posY instanceof Vector) {
                                        Vector pos = (Vector) posV;
                                        Vector pos2 = (Vector) posY;
                                        if (pos.valores.get(0).valor instanceof Integer && pos2.valores.get(0).valor instanceof Integer) {
                                            Matris vec = (Matris) val;
                                            int val_pos = (int) pos.valores.get(0).valor;
                                            int val_posY = (int) pos2.valores.get(0).valor;
                                            Object nuevo = vec.ObtenerPosicion(val_pos - 1, val_posY - 1);
                                            if (nuevo != null) {
                                                val = nuevo;
                                            } else {
                                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Acceso Incorrecto en la Matris", this.fila, this.columna));
                                                return null;
                                            }
                                        }
                                    }
                                } else if (acceso.posicion != null && acceso.posicionY == null) {

                                    Object posV = acceso.posicion.getValorImplicito(en);
                                    if (posV instanceof Vector) {
                                        Vector pos = (Vector) posV;
                                        if (pos.valores.get(0).valor instanceof Integer) {
                                            Matris vec = (Matris) val;
                                            int val_pos = (int) pos.valores.get(0).valor;
                                            Object nuevo = vec.ObtenerFila(val_pos - 1);
                                            if (nuevo != null) {
                                                val = nuevo;
                                            } else {
                                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Acceso Incorrecto en la Matris", this.fila, this.columna));
                                                return null;

                                            }
                                        }
                                    }
                                } else if (acceso.posicion == null && acceso.posicionY != null) {

                                    Object posV = acceso.posicionY.getValorImplicito(en);
                                    if (posV instanceof Vector) {
                                        Vector pos = (Vector) posV;
                                        if (pos.valores.get(0).valor instanceof Integer) {
                                            Matris vec = (Matris) val;
                                            int val_pos = (int) pos.valores.get(0).valor;
                                            Object nuevo = vec.ObtenerColumna(val_pos - 1);
                                            if (nuevo != null) {
                                                val = nuevo;
                                            } else {
                                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Acceso Incorrecto en la Matris", this.fila, this.columna));
                                                return null;

                                            }
                                        }
                                    }
                                }

                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tipo 2 solo es posible en las listas", this.fila, this.columna));
                                return null;
                            }
                        } else if (val instanceof Arreglo) {
                            Arreglo ar = (Arreglo) val;
                            int dims = ar.getDimensiones().size();
                            LinkedList<Integer> posicion = new LinkedList<>();
                            if ((x + dims - 1) < this.accesos.size()) {
                                for (int d = 0; d < dims; d++) {
                                    acceso = this.accesos.get(x + d);
                                    if (acceso.tipo && !acceso.matriz) {
                                        Object posV = acceso.posicion.getValorImplicito(en);
                                        if (posV instanceof Vector) {
                                            Vector pos = (Vector) posV;
                                            if (pos.valores.get(0).valor instanceof Integer) {
                                                posicion.add((int) pos.valores.get(0).valor);
                                            } else {
                                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La posicion debe ser Integer", this.fila, this.columna));
                                                return null;
                                            }
                                        } else {
                                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La posicion debe ser Integer", this.fila, this.columna));
                                            return null;
                                        }
                                    } else if (acceso.matriz) {
                                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tipo de acceso solo es permitido para matrices", this.fila, this.columna));
                                        return null;
                                    } else {
                                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tipo 2 solo es posible en las listas", this.fila, this.columna));
                                        return null;
                                    }
                                }
                                if (x + dims == this.accesos.size()) {
                                    this.InsertarEnArreglo(ar, posicion, en);
                                    return null;
                                } else {
                                    val = ar.ObtenerSinClon(posicion);
                                    if (val == null) {
                                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Los indices estan fuera del rango en el arreglo", this.fila, this.columna));
                                        return null;
                                    }
                                    x = x + dims -1;
                                }
                                
                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "No se esta accediendo a todas las dimensiones del arreglo", this.fila, this.columna));
                                return null;
                            }

                        }

                    }
                    this.insertarEstructura(this.accesos.getLast(), val, en);
                }

                val = sim.getValor();
                if(val instanceof Vector)
                {
                    Vector v = (Vector)val;
                    v.ponerTipoNuevo();
                }else if(val instanceof Matris)
                {
                    Matris  m = (Matris)val;
                    m.ponerTipo();
                }else if(val instanceof Arreglo)
                {
                    Arreglo a = (Arreglo)val;
                    a.getTipo();
                }
                
            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La variable " + this.identificador + " no existe", this.fila, this.columna));
            }
        } catch (Exception e) {
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Error al asignar ", this.fila, this.columna));
            System.out.println(e);
        }
        return null;
    }

    public void insertarEstructura(NodoAcceso acceso, Object estructura, Entorno en) throws CloneNotSupportedException {
        if (estructura instanceof Vector) {
            NodoAcceso pos = acceso;
            if (pos.getTipo()) {
                Object pos_val = pos.getPosicion().getValorImplicito(en);
                if (pos_val instanceof Vector) {
                    Vector pos_vec = (Vector) pos_val;
                    if (pos_vec.valores.get(0).valor instanceof Integer) {
                        Vector vec = (Vector) estructura;
                        Object valor_exp = this.expresion.getValorImplicito(en);
                        if (valor_exp instanceof Vector) {
                            Vector val_exp = (Vector) valor_exp;
                            if (val_exp.tamaño() == 1) {
                                Vector clon = (Vector) val_exp.clone();
                                vec.insertarPosicion((int) pos_vec.valores.get(0).valor - 1, clon.valores.get(0).valor, fila, columna);
                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tamaño del vector debe de ser de 1 para poder asignar", this.fila, this.columna));
                            }
                        }
                        //
                    } else {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La posicion debe de ser integer", this.fila, this.columna));
                    }
                }
            } else if (pos.getMatriz()) {

                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El acceso de este tipo solo es valido para matrices", this.fila, this.columna));

            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El acceso de este tipo solo es valido para listas", this.fila, this.columna));
            }
        } else if (estructura instanceof Lista) {
            NodoAcceso pos = acceso;
            if (pos.getTipo()) {
                Object pos_val = pos.getPosicion().getValorImplicito(en);
                if (pos_val instanceof Vector) {
                    Vector pos_vec = (Vector) pos_val;
                    if (pos_vec.getValores().get(0).valor instanceof Integer) {
                        Lista l = (Lista) estructura;
                        Object valor_exp = this.expresion.getValorImplicito(en);
                        if (valor_exp instanceof Vector) {
                            Vector val_exp = (Vector) valor_exp;
                            if (val_exp.tamaño() == 1) {
                                l.insertarPosicion((int) pos_vec.getValores().get(0).valor - 1, (Vector) val_exp.clone(), fila, columna);
                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tamaño del vector debe de ser de 1 para poder asignar", this.fila, this.columna));
                            }
                        } else if (valor_exp instanceof Lista) {
                            Lista val_exp = (Lista) valor_exp;
                            if (val_exp.tamaño() == 1) {
                                l.insertarPosicion((int) pos_vec.getValores().get(0).valor - 1, val_exp.valores.get(0), fila, columna);
                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tamaño de la lista debe de ser de 1 para poder asignar", this.fila, this.columna));
                            }
                        }
                        //
                    } else {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La posicion debe de ser integer", this.fila, this.columna));
                    }
                }
            } else if (!pos.getMatriz()) {
                Object pos_val = pos.getPosicion().getValorImplicito(en);
                if (pos_val instanceof Vector) {
                    Vector pos_vec = (Vector) pos_val;
                    if (pos_vec.getValores().get(0).valor instanceof Integer) {
                        Lista l = (Lista) estructura;
                        Object valor_exp = this.expresion.getValorImplicito(en);
                        if (valor_exp != null) {
                            l.insertarPosicion((int) pos_vec.getValores().get(0).valor - 1, valor_exp, fila, columna);
                        }
                    } else {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La posicion debe de ser integer", this.fila, this.columna));
                    }
                }
            }
        } else if (estructura instanceof Matris) {
            NodoAcceso pos = acceso;
            if (pos.getTipo()) {
                Object pos_val = pos.getPosicion().getValorImplicito(en);
                if (pos_val instanceof Vector) {
                    Vector pos_vec = (Vector) pos_val;
                    if (pos_vec.getValores().get(0).valor instanceof Integer) {
                        Matris l = (Matris) estructura;
                        Object valor_exp = this.expresion.getValorImplicito(en);
                        if (valor_exp instanceof Vector) {
                            Vector val_exp = (Vector) valor_exp;
                            if (val_exp.tamaño() == 1) {
                                boolean ins = l.InsertarPosicion((int) pos_vec.getValores().get(0).valor - 1, val_exp.valores.get(0));
                                if (!ins) {
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La posicion " + (int) pos_vec.getValores().get(0).valor + " no existe en la matris", this.fila, this.columna));
                                }
                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tamaño del vector debe de ser de 1 para poder asignar", this.fila, this.columna));
                            }
                        } else {
                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Matris solo almacena primitivos", this.fila, this.columna));
                        }
                        //
                    } else {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La posicion debe de ser integer", this.fila, this.columna));
                    }
                }
            } else if (pos.getMatriz()) {
                if (pos.getPosicion() != null && pos.getPosicionY() != null) {
                    Object posV = pos.getPosicion().getValorImplicito(en);
                    Object posY = pos.getPosicionY().getValorImplicito(en);
                    if (posV instanceof Vector && posY instanceof Vector) {
                        Vector posx = (Vector) posV;
                        Vector pos2 = (Vector) posY;
                        if (posx.valores.get(0).valor instanceof Integer && pos2.valores.get(0).valor instanceof Integer) {
                            int val_pos = (int) posx.valores.get(0).valor;
                            int val_posY = (int) pos2.valores.get(0).valor;
                            Matris mat = (Matris) estructura;
                            Object valor_exp = this.expresion.getValorImplicito(en);
                            if (valor_exp instanceof Vector) {
                                Vector val_exp = (Vector) valor_exp;
                                if (val_exp.tamaño() == 1) {
                                    boolean ins = mat.insertar(val_pos - 1, val_posY - 1, val_exp.valores.get(0));
                                    if (!ins) {
                                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La matris no contine las siguientes cooredenadas: x = " + val_pos + " y = " + val_posY, this.fila, this.columna));
                                    }
                                } else {
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tamaño del vector debe de ser de 1 para poder asignar", this.fila, this.columna));
                                }
                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Matris solo almacena primitivos", this.fila, this.columna));
                            }

                        }
                    }
                } else if (pos.getPosicion() != null && pos.getPosicionY() == null) {
                    Object posV = pos.getPosicion().getValorImplicito(en);
                    if (posV instanceof Vector) {
                        Vector posx = (Vector) posV;
                        if (posx.valores.get(0).valor instanceof Integer) {
                            int val_pos = (int) posx.valores.get(0).valor;
                            Matris mat = (Matris) estructura;
                            Object valor_exp = this.expresion.getValorImplicito(en);
                            if (valor_exp instanceof Vector) {
                                Vector val_exp = (Vector) valor_exp;
                                if (val_exp.tamaño() == 1) {
                                    boolean ins = mat.insertarFila(val_pos - 1, val_exp.valores.get(0));
                                    if (!ins) {
                                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La matris no contine las siguientes fila: x = " + val_pos, this.fila, this.columna));
                                    }
                                } else if (val_exp.tamaño() == mat.getColumna()) {
                                    for (int x = 0; x < mat.getColumna(); x++) {
                                        boolean ins = mat.insertar(val_pos - 1, x, val_exp.valores.get(x));
                                        if (!ins) {
                                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La matris no contine las siguientes fila: x = " + val_pos, this.fila, this.columna));
                                            break;
                                        }
                                    }
                                } else {
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tamaño del vector no es 1 o no tiene el mismo numero de valores que columnas", this.fila, this.columna));
                                }
                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Matris solo almacena primitivos", this.fila, this.columna));
                            }

                        }
                    }
                } else if (pos.getPosicion() == null && pos.getPosicionY() != null) {
                    Object posV = pos.getPosicionY().getValorImplicito(en);
                    if (posV instanceof Vector) {
                        Vector posx = (Vector) posV;
                        if (posx.valores.get(0).valor instanceof Integer) {
                            int val_pos = (int) posx.valores.get(0).valor;
                            Matris mat = (Matris) estructura;
                            Object valor_exp = this.expresion.getValorImplicito(en);
                            if (valor_exp instanceof Vector) {
                                Vector val_exp = (Vector) valor_exp;
                                if (val_exp.tamaño() == 1) {
                                    boolean ins = mat.insertarColumna(val_pos - 1, val_exp.valores.get(0));
                                    if (!ins) {
                                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La matris no contine las siguiente columna: " + val_pos, this.fila, this.columna));
                                    }
                                } else if (val_exp.tamaño() == mat.getFila()) {
                                    for (int x = 0; x < mat.getFila(); x++) {
                                        boolean ins = mat.insertar(x, val_pos - 1, val_exp.valores.get(x));
                                        if (!ins) {
                                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La matris no contine las siguientes columna:  " + val_pos, this.fila, this.columna));
                                            break;
                                        }
                                    }
                                } else {
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tamaño del vector debe de ser de 1 o del mismo numero de filas", this.fila, this.columna));
                                }
                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Matris solo almacena primitivos", this.fila, this.columna));
                            }

                        }
                    }
                }
            }
        }

    }

    public void InsertarEnArreglo(Arreglo ar, LinkedList<Integer> posicion, Entorno en) throws CloneNotSupportedException {
        Object valor = this.expresion.getValorImplicito(en);
        if (valor instanceof Vector) {
            Vector nvec = (Vector) valor;
            if (nvec.valores.size() == 1) {
                if (ar.isVector()) {
                    boolean inserto = ar.insertar(posicion, (Vector) nvec.clone());
                    if (!inserto) {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La posicion del arreglo es incorrecta", this.fila, this.columna));
                    } else {
                        ar.getTipo();
                    }
                } else {
                    Lista nueva = new Lista();
                    nueva.agregarFinal(new Nodo((Vector) nvec.clone()));
                    boolean inserto = ar.insertar(posicion, nueva);
                    if (!inserto) {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La posicion del arreglo es incorrecta", this.fila, this.columna));
                    }
                }
            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El vector debe de ser de tamaño uno para poder Asignarlo en el arreglo", this.fila, this.columna));
            }
        } else if (valor instanceof Lista) {
            Lista nvec = (Lista) valor;
            if (nvec.valores.size() == 1) {
                if (ar.isVector()) {
                    boolean inserto = ar.insertar(posicion, (Lista) nvec.clone());

                    if (!inserto) {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La posicion del arreglo es incorrecta", this.fila, this.columna));
                    }
                    ar.setVector(false);
                    ar.llenarLista();
                } else {

                    boolean inserto = ar.insertar(posicion, (Lista) nvec.clone());
                    if (!inserto) {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La posicion del arreglo es incorrecta", this.fila, this.columna));
                    }
                }
            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El vector debe de ser de tamaño uno para poder Asignarlo en el arreglo", this.fila, this.columna));
            }
        }
    }

    @Override
    public NodoDot generarDot(int numero) {
        String cuerpo = "";
        String principal = "node" + numero++;
        cuerpo += principal + "[label=\"Sentencia_Asignar_Estructura\"];\n";
        String id = "node" + numero++;
        cuerpo += id + "[label=\"Identificador: " + this.identificador + "\"];\n";
        cuerpo += principal + " -> " + id + ";\n";
        String acs = "node" + numero++;
        cuerpo += acs + "[label=\"Accesos\"];\n";
        cuerpo += principal + " -> " + acs + ";\n";
        for (NodoAcceso a : accesos) {
            if (a.getTipo() && !a.getMatriz()) {
                String normal = "node" + numero++;
                cuerpo += normal + "[label=\"Acceso_Normal\"];\n";
                cuerpo += acs + " -> " + normal + ";\n";
                NodoDot nodo = a.getPosicion().generarDot(numero + 1);
                numero = nodo.Numero;
                cuerpo += nodo.Cuerpo;
                cuerpo += normal + " -> " + nodo.Nombre + ";\n";
            } else if (a.getMatriz()) {
                if (a.getPosicion() != null && a.getPosicionY() != null) {
                    String normal = "node" + numero++;
                    cuerpo += normal + "[label=\"Acceso_Matris [x,y]\"];\n";
                    cuerpo += acs + " -> " + normal + "\n";
                    NodoDot nodo = a.getPosicion().generarDot(numero + 1);
                    numero = nodo.Numero;
                    cuerpo += nodo.Cuerpo;
                    cuerpo += normal + " -> " + nodo.Nombre + ";\n";
                    NodoDot nodoY = a.getPosicionY().generarDot(numero + 1);
                    numero = nodoY.Numero;
                    cuerpo += nodoY.Cuerpo;
                    cuerpo += normal + " -> " + nodoY.Nombre + ";\n";
                } else if (a.getPosicion() != null && a.getPosicionY() == null) {
                    String normal = "node" + numero++;
                    cuerpo += normal + "[label=\"Acceso_Matris [x,]\"];\n";
                    cuerpo += acs + " -> " + normal + ";\n";
                    NodoDot nodo = a.getPosicion().generarDot(numero + 1);
                    numero = nodo.Numero;
                    cuerpo += nodo.Cuerpo;
                    cuerpo += normal + " -> " + nodo.Nombre + ";\n";
                } else if (a.getPosicion() == null && a.getPosicionY() != null) {
                    String normal = "node" + numero++;
                    cuerpo += normal + "[label=\"Acceso_Matris [,y]\"];\n";
                    cuerpo += acs + " -> " + normal + "\n";
                    NodoDot nodoY = a.getPosicionY().generarDot(numero + 1);
                    numero = nodoY.Numero;
                    cuerpo += nodoY.Cuerpo;
                    cuerpo += normal + " -> " + nodoY.Nombre + ";\n";
                }
            } else {
                String normal = "node" + numero++;
                cuerpo += normal + "[label=\"Acceso_Lista\"];\n";
                cuerpo += acs + " -> " + normal + ";\n";
                NodoDot nodo = a.getPosicion().generarDot(numero + 1);
                numero = nodo.Numero;
                cuerpo += nodo.Cuerpo;
                cuerpo += normal + " -> " + nodo.Nombre + ";\n";
            }
        }
        String exp = "node" + numero++;
        cuerpo += exp + "[label=\"Valor\"];\n";
        cuerpo += principal + " -> " + exp + ";\n";
        NodoDot e = this.expresion.generarDot(numero);
        cuerpo += e.Cuerpo;
        cuerpo += exp + " -> " + e.Nombre + ";\n";
        numero = e.Numero;

        return new NodoDot(principal, cuerpo, numero + 1);
    }

}
