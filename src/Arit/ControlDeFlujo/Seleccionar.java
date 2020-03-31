/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.ControlDeFlujo;

import Arit.AltaAbstraccion.Expresion;
import Arit.AltaAbstraccion.Instruccion;
import Arit.AltaAbstraccion.NodoAst;
import Arit.CambioFlujo.Break;
import Arit.CambioFlujo.Continue;
import Arit.CambioFlujo.Return;
import Arit.Entorno.Entorno;
import Arit.Estructuras.Vector;
import Arit.ValorImplicito.Operacion;
import Arit.ValorImplicito.Operacion.Operador;
import Arit.ValorImplicito.Primitivo;
import Error.ErrorAr;
import Informacion.Informacion;
import Reportes.NodoDot;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class Seleccionar extends Instruccion {

    LinkedList<Caso> casos;
    Expresion expresion;
    Caso defecto;

    public Seleccionar(Expresion expresion, LinkedList<Caso> casos, Caso defecto, int fila, int columna) {
        super(fila, columna);
        this.casos = casos;
        this.expresion = expresion;
        this.defecto = defecto;
    }

    @Override
    public Object ejecutar(Entorno en) {
        try {
            boolean entro = false;
            Object val = this.expresion.getValorImplicito(en);
            for (Caso caso : this.casos) {
                if (val instanceof Vector) {
                    Vector vec = (Vector) val;
                    Primitivo nuevoP = new Primitivo(vec.getValores().get(0).valor, vec.tipo, this.expresion.fila, this.expresion.columna);
                    Operacion operacion = new Operacion(nuevoP, caso.getExpresion(), Operador.IGUAL_IGUAL, fila, columna);
                    Object re = operacion.getValorImplicito(en);

                    if (entro || (boolean) ((Vector) re).valores.get(0).valor) {
                        entro = true;
                        for (NodoAst nodo : caso.getSentencias()) {
                            Entorno local = new Entorno(en);
                            if (nodo instanceof Instruccion) {
                                Instruccion ins = (Instruccion) nodo;
                                Object result = ins.ejecutar(local);
                                if (result instanceof Break) {
                                    return null;
                                } else if (result instanceof Continue) {
                                    return result;
                                } else if (result instanceof Return) {
                                    return result;
                                }
                            } else if (nodo instanceof Expresion) {
                                Expresion expr = (Expresion) nodo;
                                Object result = expr.getValorImplicito(local);
                                if (result instanceof Break) {
                                    return null;
                                } else if (result instanceof Continue) {
                                    return result;
                                } else if (result instanceof Return) {
                                    return result;
                                }
                            }
                        }
                    }
                }
            }
            if (!entro) {
                if (this.defecto != null) {
                    Caso caso = this.defecto;

                    for (NodoAst nodo : caso.getSentencias()) {
                        Entorno local = new Entorno(en);
                        if (nodo instanceof Instruccion) {
                            Instruccion ins = (Instruccion) nodo;
                            Object result = ins.ejecutar(local);
                            if (result instanceof Break) {
                                return null;
                            } else if (result instanceof Continue) {
                                return result;
                            } else if (result instanceof Return) {
                                return result;
                            }
                        } else if (nodo instanceof Expresion) {
                            Expresion expr = (Expresion) nodo;
                            Object result = expr.getValorImplicito(local);
                            if (result instanceof Break) {
                                return null;
                            } else if (result instanceof Continue) {
                                return result;
                            } else if (result instanceof Return) {
                                return result;
                            }

                        }

                    }
                }
            }
        } catch (Exception e) {
            Informacion.agregarError(new ErrorAr("Semantico", "Error al ejecutar switch", this.fila, this.columna));
        }
        return null;
    }

    @Override
    public NodoDot generarDot(int numero) {
        NodoDot valorNodo = this.expresion.generarDot(numero);

        String nodo = "node" + valorNodo.Numero;
        String cuerpo = nodo + "[label=\"Sentencia_Switch\"];\n";
        String nodoIdent = "node" + (valorNodo.Numero + 1);
        cuerpo += nodoIdent + "[label=\"Condicion\"];\n";
        cuerpo += nodo + "->" + nodoIdent + ";\n";
        cuerpo += nodoIdent + "->" + valorNodo.Nombre + ";\n";
        String NodoSentencias = "node" + (valorNodo.Numero + 2);
        cuerpo += NodoSentencias + "[label=\"Casos\"];\n";
        cuerpo += nodo + "->" + NodoSentencias + ";\n";
        numero = valorNodo.Numero + 3;

        for (Caso caso : this.casos) {
            String nodoCaso = "node" + numero;
            cuerpo += nodoCaso + "[label=\"Caso\"];\n";
            cuerpo += NodoSentencias + " -> " + nodoCaso + ";\n";
            NodoDot cas = caso.expresion.generarDot(numero + 1);

            numero = cas.Numero;
            String comparar = "node" + numero;
            cuerpo += cas.Cuerpo;
            cuerpo += comparar + "[label=\"Comparar\"];\n";
            cuerpo += nodoCaso + " -> " + comparar + ";\n";
            cuerpo += comparar + " -> " + cas.Nombre + ";\n";
            numero++;
            String sentencias = "node" + numero;
            cuerpo += cas.Cuerpo;
            cuerpo += sentencias + "[label=\"Sentencias\"];\n";
            cuerpo += nodoCaso + " -> " + sentencias + ";\n";
            numero++;
            for (NodoAst nod : caso.sentencias) {
                NodoDot nuevo = nod.generarDot(numero);
                numero = nuevo.Numero;
                cuerpo += nuevo.Cuerpo;
                cuerpo += sentencias + " -> " + nuevo.Nombre + ";\n";
            }
        }

        if (this.defecto != null) {
            String nodoCaso = "node" + numero;
            cuerpo += nodoCaso + "[label=\"Caso_Defecto\"];\n";
            cuerpo += NodoSentencias + " -> " + nodoCaso + ";\n";
            numero++;
            String sentencias = "node" + numero;
            cuerpo += sentencias + "[label=\"Sentencias\"];\n";
            cuerpo += nodoCaso + " -> " + sentencias + ";\n";
            numero++;
            for (NodoAst nod : this.defecto.sentencias) {
                NodoDot nuevo = nod.generarDot(numero);
                numero = nuevo.Numero;
                cuerpo += nuevo.Cuerpo;
                cuerpo += sentencias + " -> " + nuevo.Nombre + ";\n";
            }
        }

        NodoDot nuevo = new NodoDot(nodo, valorNodo.Cuerpo + cuerpo, numero + 1);
        return nuevo;
    }

}
