/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.Ciclos;

import Arit.AltaAbstraccion.Expresion;
import Arit.AltaAbstraccion.Instruccion;
import Arit.AltaAbstraccion.NodoAst;
import Arit.CambioFlujo.Break;
import Arit.CambioFlujo.Continue;
import Arit.CambioFlujo.Return;
import Arit.Entorno.Entorno;
import Arit.Entorno.Simbolo;
import Arit.Estructuras.Arreglo;
import Arit.Estructuras.Lista;
import Arit.Estructuras.Matris;
import Arit.Estructuras.Nodo;
import Arit.Estructuras.Vector;
import Reportes.NodoDot;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class For extends Instruccion {

    String identificador;
    Expresion estructura;
    LinkedList<NodoAst> sentencias;

    public For(String identificador, Expresion estructura, LinkedList<NodoAst> sentencias, int fila, int columna) {
        super(fila, columna);
        this.identificador = identificador;
        this.estructura = estructura;
        this.sentencias = sentencias;
    }

    @Override
    public Object ejecutar(Entorno en) {
        try {
            Object val = this.estructura.getValorImplicito(en);
            if (val instanceof Vector) {
                Vector vec = (Vector) val;
                for (int x = 0; x < vec.valores.size(); x++) {
                    Vector nuevo = new Vector();
                    nuevo.agregarFinal(vec.valores.get(x));
                    nuevo.ponerTipoNuevo();
                    Entorno local = new Entorno(en);
                    if (en.existe(identificador)) {
                        Simbolo sim = en.getSimbolo(identificador);
                        sim.setValor(nuevo);
                        en.reemplazar(identificador, sim);
                    } else {
                        Simbolo sim = new Simbolo(identificador, nuevo);
                        local.agregar(identificador, sim);
                    }
                    Object h = realizarFor(local);
                    if (h instanceof Return) {
                        return h;
                    } else if (h instanceof Break) {
                        return null;
                    }
                }
            } else if (val instanceof Matris) {
                Matris mat = (Matris) val;
                for (int x = 0; x < mat.getColumna(); x++) {
                    for (int y = 0; y < mat.getFila(); y++) {
                        Vector nuevo = new Vector();
                        nuevo.agregarFinal(new Nodo(mat.valores[y][x]));// revisasr 
                        nuevo.ponerTipoNuevo();
                        Entorno local = new Entorno(en);
                        if (en.existe(identificador)) {
                            Simbolo sim = en.getSimbolo(identificador);
                            sim.setValor(nuevo);
                            en.reemplazar(identificador, sim);
                        } else {
                            Simbolo sim = new Simbolo(identificador, nuevo);
                            local.agregar(identificador, sim);
                        }
                        Object h = realizarFor(local);
                        if (h instanceof Return) {
                            return h;
                        } else if (h instanceof Break) {
                            return null;
                        }
                    }
                }

            } else if (val instanceof Lista) {
                Lista l = (Lista) val;
                for (int x = 0; x < l.valores.size(); x++) {
                    Entorno local = new Entorno(en);
                    if (en.existe(identificador)) {
                        Simbolo sim = en.getSimbolo(identificador);
                        sim.setValor(l.valores.get(x).valor);
                        en.reemplazar(identificador, sim);
                    } else {
                        Simbolo sim = new Simbolo(identificador, l.valores.get(x).valor);
                        local.agregar(identificador, sim);
                    }
                    Object h = realizarFor(local);
                    if (h instanceof Return) {
                        return h;
                    } else if (h instanceof Break) {
                        return null;
                    }
                }
            } else if (val instanceof Arreglo) {
                Arreglo ar = (Arreglo) val;
                for (int x = 0; x < ar.getValores().size(); x++) {
                    Entorno local = new Entorno(en);
                    if (en.existe(identificador)) {
                        Simbolo sim = en.getSimbolo(identificador);
                        sim.setValor(ar.getValores().get(x).valor);
                        en.reemplazar(identificador, sim);
                    } else {
                        Simbolo sim = new Simbolo(identificador, ar.getValores().get(x).valor);
                        local.agregar(identificador, sim);
                    }
                    Object h = realizarFor(local);
                    if (h instanceof Return) {
                        return h;
                    } else if (h instanceof Break) {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public Object realizarFor(Entorno local) {
        for (NodoAst nodo : this.sentencias) {
            if (nodo instanceof Instruccion) {
                Instruccion ins = (Instruccion) nodo;
                Object result = ins.ejecutar(local);
                if (result != null) {
                    if (result instanceof Break) {
                        return result;
                    } else if (result instanceof Continue) {
                        break;
                    } else if (result instanceof Return) {
                        return result;
                    }

                }
            } else if (nodo instanceof Expresion) {
                Expresion expr = (Expresion) nodo;
                Object ret = expr.getValorImplicito(local);
                if (ret instanceof Break) {
                    return ret;
                } else if (ret instanceof Continue) {
                    break;
                } else if (ret instanceof Return) {
                    return ret;
                }
            }
        }
        return null;
    }

    @Override
    public NodoDot generarDot(int numero) {
        NodoDot valorNodo = this.estructura.generarDot(numero);

        String nodo = "node" + valorNodo.Numero;
        String cuerpo = nodo + "[label=\"Sentencia_For\"];\n";
        String nodoIdent = "node" + (valorNodo.Numero + 1);
        cuerpo += nodoIdent + "[label=\"Estructura\"];\n";
        cuerpo += nodo + "->" + nodoIdent + ";\n";
        cuerpo += nodoIdent + "->" + valorNodo.Nombre + ";\n";
        String NodoVariable = "node" + (valorNodo.Numero + 2);
        cuerpo += NodoVariable + "[label=\"Variable: " + this.identificador + "\"];\n";
        cuerpo += nodo + "->" + NodoVariable + ";\n";
        String NodoSentencias = "node" + (valorNodo.Numero + 3);
        cuerpo += NodoSentencias + "[label=\"Sentencias\"];\n";
        cuerpo += nodo + "->" + NodoSentencias + ";\n";
        numero = valorNodo.Numero + 4;
        for (NodoAst nod : this.sentencias) {
            NodoDot nuevo = nod.generarDot(numero);
            numero = nuevo.Numero;
            cuerpo += nuevo.Cuerpo;
            cuerpo += NodoSentencias + " -> " + nuevo.Nombre + ";\n";
        }

        NodoDot nuevo = new NodoDot(nodo, valorNodo.Cuerpo + cuerpo, numero + 1);
        return nuevo;
    }

}
