/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.ValorImplicito;

import Arit.AltaAbstraccion.Expresion;
import Arit.Entorno.Entorno;
import Arit.Entorno.Simbolo;
import Arit.Estructuras.Arreglo;
import Arit.Estructuras.Lista;
import Arit.Estructuras.Matris;
import Arit.Estructuras.Vector;
import Error.ErrorAr;
import Reportes.NodoDot;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class Acceso extends Expresion {

    String identificador;
    LinkedList<NodoAcceso> accesos;

    public Acceso(String identificador, LinkedList<NodoAcceso> accesos, int fila, int columna) {
        super(fila, columna);
        this.identificador = identificador.toLowerCase();
        this.accesos = accesos;
    }

    @Override
    public Object getValorImplicito(Entorno en) {
        try {
            if (en.existe(identificador)) {
                Simbolo sim = en.getSimbolo(identificador);
                Object estructura = sim.getValor();

                for (int x = 0; x < this.accesos.size(); x++) {
                    NodoAcceso a = this.accesos.get(x);
                    if (estructura instanceof Vector) {
                        if (a.tipo && !a.matriz) {
                            Object posV = a.posicion.getValorImplicito(en);
                            if (posV instanceof Vector) {
                                Vector pos = (Vector) posV;
                                if (pos.valores.get(0).valor instanceof Integer) {
                                    Vector vec = (Vector) estructura;
                                    int val_pos = (int) pos.valores.get(0).valor;
                                    Object nuevo = vec.getPosicion(val_pos - 1);
                                    if (nuevo != null) {
                                        estructura = nuevo;
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
                        } else if (a.matriz) {
                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tipo de acceso solo es permitido para matrices", this.fila, this.columna));
                            return null;
                        } else {
                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tipo 2 solo es posible en las listas", this.fila, this.columna));
                            return null;
                        }
                    } else if (estructura instanceof Lista) {
                        if (a.tipo && !a.matriz) {
                            Object posV = a.posicion.getValorImplicito(en);
                            if (posV instanceof Vector) {
                                Vector pos = (Vector) posV;
                                if (pos.valores.get(0).valor instanceof Integer) {
                                    Lista vec = (Lista) estructura;
                                    int val_pos = (int) pos.valores.get(0).valor;
                                    Object nuevo = vec.getPosicionSegundoAcceesoSinClon(val_pos - 1);
                                    if (nuevo != null) {
                                        estructura = nuevo;
                                    } else {
                                        return null;
                                    }
                                }
                            }
                        } else if (a.matriz) {
                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tipo de acceso solo es permitido para matrices", this.fila, this.columna));
                            return null;
                        } else {
                            Object posV = a.posicion.getValorImplicito(en);
                            if (posV instanceof Vector) {
                                Vector pos = (Vector) posV;
                                if (pos.valores.get(0).valor instanceof Integer) {
                                    Lista vec = (Lista) estructura;
                                    int val_pos = (int) pos.valores.get(0).valor;
                                    Object nuevo = vec.getPosicionSinClon(val_pos - 1).valor;
                                    if (nuevo != null) {
                                        estructura = nuevo;
                                    } else {
                                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Acceso Incorrecto en la lsita", this.fila, this.columna));
                                        return null;

                                    }
                                }
                            }
                        }
                    } else if (estructura instanceof Matris) {
                        if (a.tipo) {
                            Object posV = a.posicion.getValorImplicito(en);
                            if (posV instanceof Vector) {
                                Vector pos = (Vector) posV;
                                if (pos.valores.get(0).valor instanceof Integer) {
                                    Matris vec = (Matris) estructura;
                                    int val_pos = (int) pos.valores.get(0).valor;
                                    Object nuevo = vec.ObtenerPosicion(val_pos - 1);
                                    if (nuevo != null) {
                                        estructura = nuevo;
                                    } else {
                                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Acceso Incorrecto en la Matris", this.fila, this.columna));
                                        return null;

                                    }
                                }
                            }
                        } else if (a.matriz) {
                            if (a.posicion != null && a.posicionY != null) {
                                Object posV = a.posicion.getValorImplicito(en);
                                Object posY = a.posicionY.getValorImplicito(en);
                                if (posV instanceof Vector && posY instanceof Vector) {
                                    Vector pos = (Vector) posV;
                                    Vector pos2 = (Vector) posY;
                                    if (pos.valores.get(0).valor instanceof Integer && pos2.valores.get(0).valor instanceof Integer) {
                                        Matris vec = (Matris) estructura;
                                        int val_pos = (int) pos.valores.get(0).valor;
                                        int val_posY = (int) pos2.valores.get(0).valor;
                                        Object nuevo = vec.ObtenerPosicion(val_pos - 1, val_posY - 1);
                                        if (nuevo != null) {
                                            estructura = nuevo;
                                        } else {
                                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Acceso Incorrecto en la Matris", this.fila, this.columna));
                                            return null;
                                        }
                                    }
                                }
                            } else if (a.posicion != null && a.posicionY == null) {

                                Object posV = a.posicion.getValorImplicito(en);
                                if (posV instanceof Vector) {
                                    Vector pos = (Vector) posV;
                                    if (pos.valores.get(0).valor instanceof Integer) {
                                        Matris vec = (Matris) estructura;
                                        int val_pos = (int) pos.valores.get(0).valor;
                                        Object nuevo = vec.ObtenerFila(val_pos - 1);
                                        if (nuevo != null) {
                                            estructura = nuevo;
                                        } else {
                                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Acceso Incorrecto en la Matris", this.fila, this.columna));
                                            return null;

                                        }
                                    }
                                }
                            } else if (a.posicion == null && a.posicionY != null) {

                                Object posV = a.posicionY.getValorImplicito(en);
                                if (posV instanceof Vector) {
                                    Vector pos = (Vector) posV;
                                    if (pos.valores.get(0).valor instanceof Integer) {
                                        Matris vec = (Matris) estructura;
                                        int val_pos = (int) pos.valores.get(0).valor;
                                        Object nuevo = vec.ObtenerColumna(val_pos - 1);
                                        if (nuevo != null) {
                                            estructura = nuevo;
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
                    } else if (estructura instanceof Arreglo) {
                        Arreglo ar = (Arreglo) estructura;
                        int dims = ar.getDimensiones().size();
                        LinkedList<Integer> posicion = new LinkedList<>();
                        if ((x + dims - 1) < this.accesos.size()) {
                            for (int d = 0; d < dims; d++) {
                                a = this.accesos.get(x + d);
                                if (a.tipo && !a.matriz) {
                                    Object posV = a.posicion.getValorImplicito(en);
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
                                } else if (a.matriz) {
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tipo de acceso solo es permitido para matrices", this.fila, this.columna));
                                    return null;
                                } else {
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El tipo 2 solo es posible en las listas", this.fila, this.columna));
                                    return null;
                                }
                            }
                            estructura = ar.Obetener(posicion);
                            if (estructura == null) {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Los indices estan fuera del rango en el arreglo", this.fila, this.columna));
                                return null;
                            }
                            x = x + dims - 1;
                        } else {
                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "No se esta accediendo a todas las dimensiones del arreglo", this.fila, this.columna));
                            return null;
                        }

                    }
                }
                return estructura;
            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La variable " + this.identificador + " no existe", this.fila, this.columna));
            }
        } catch (Exception e) {
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Error al ejecutar acceso a: " + this.identificador, this.fila, this.columna));
        }
        return null;
    }

    @Override
    public NodoDot generarDot(int numero) {
        String cuerpo = "";
        String principal = "node" + numero++;
        cuerpo += principal + "[label=\"Sentencia_Acceso_Estructura\"];\n";
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
        return new NodoDot(principal, cuerpo, numero + 1);
    }

}
