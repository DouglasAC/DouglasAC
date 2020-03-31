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
import Arit.Estructuras.Vector;
import Error.ErrorAr;
import Reportes.NodoDot;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class While extends Instruccion {

    Expresion condicion;
    LinkedList<NodoAst> sentencias;

    public While(Expresion condicion, LinkedList<NodoAst> sentencias, int fila, int columna) {
        super(fila, columna);
        this.condicion = condicion;
        this.sentencias = sentencias;
    }

    @Override
    public Object ejecutar(Entorno en) {
        try {
            Object condicion_while = this.condicion.getValorImplicito(en);
            if (condicion_while instanceof Vector) {

                while ((boolean) ((Vector) this.condicion.getValorImplicito(en)).valores.get(0).valor) {
                    Entorno local = new Entorno(en);
                    for (NodoAst nodo : this.sentencias) {
                        if (nodo instanceof Instruccion) {
                            Instruccion ins = (Instruccion) nodo;
                            Object result = ins.ejecutar(local);
                            if (result != null) {
                                if (result instanceof Break) {
                                    return null;
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
                                return null;
                            } else if (ret instanceof Continue) {
                                break;
                            } else if (ret instanceof Return) {
                                return ret;
                            }
                        }
                    }
                }
            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La expresion del While no es booleana ", fila, columna));
            }
        } catch (Exception e) {
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La expresion del While no es booleana ", fila, columna));
        }

        return null;
    }

    @Override
    public NodoDot generarDot(int numero) {
        NodoDot valorNodo = this.condicion.generarDot(numero);

        String nodo = "node" + valorNodo.Numero;
        String cuerpo = nodo + "[label=\"Sentencia_While\"];\n";
        String nodoIdent = "node" + (valorNodo.Numero + 1);
        cuerpo += nodoIdent + "[label=\"Condicion\"];\n";
        cuerpo += nodo + "->" + nodoIdent + ";\n";
        cuerpo += nodoIdent + "->" + valorNodo.Nombre + ";\n";
        String NodoSentencias = "node" + (valorNodo.Numero + 2);
        cuerpo += NodoSentencias + "[label=\"Sentencias\"];\n";
        cuerpo += nodo + "->" + NodoSentencias + ";\n";
        numero = valorNodo.Numero + 3;
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
