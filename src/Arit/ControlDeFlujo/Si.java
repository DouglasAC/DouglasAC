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
import Error.ErrorAr;
import Reportes.NodoDot;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class Si extends Instruccion {

    Expresion condicion;
    LinkedList<NodoAst> sentencias;
    LinkedList<NodoAst> sentenciasElse;

    public Si(Expresion condicion, LinkedList<NodoAst> sentencias, LinkedList<NodoAst> sentenciasElse, int fila, int columna) {
        super(fila, columna);
        this.condicion = condicion;
        this.sentencias = sentencias;
        this.sentenciasElse = sentenciasElse;
    }

    @Override
    public Object ejecutar(Entorno en) {
        try {
            Object val = this.condicion.getValorImplicito(en);
            if (val instanceof Vector) {
                Vector cond = (Vector) val;
                Object val_cond = cond.valores.get(0).valor;
                if (val_cond instanceof Boolean) {
                    if ((boolean) val_cond) {
                        Entorno nuevo = new Entorno(en);
                        for (NodoAst nodo : this.sentencias) {
                            if (nodo instanceof Instruccion) {
                                Object val_nodo = ((Instruccion) nodo).ejecutar(nuevo);
                                if (val_nodo instanceof Break) {
                                    return val_nodo;
                                } else if (val_nodo instanceof Continue) {
                                    return val_nodo;
                                } else if (val_nodo instanceof Return) {
                                    return val_nodo;
                                }
                            } else if (nodo instanceof Expresion) {
                                Object val_nodo = ((Expresion) nodo).getValorImplicito(nuevo);
                                if (val_nodo instanceof Break) {
                                    return val_nodo;
                                } else if (val_nodo instanceof Continue) {
                                    return val_nodo;
                                } else if (val_nodo instanceof Return) {
                                    return val_nodo;
                                }
                            }
                        }
                    } else if (this.sentenciasElse != null) {
                        Entorno nuevo = new Entorno(en);
                        for (NodoAst nodo : this.sentenciasElse) {
                            if (nodo instanceof Instruccion) {
                                Object val_nodo = ((Instruccion) nodo).ejecutar(nuevo);
                                if (val_nodo instanceof Break) {
                                    return val_nodo;
                                } else if (val_nodo instanceof Continue) {
                                    return val_nodo;
                                } else if (val_nodo instanceof Return) {
                                    return val_nodo;
                                }
                            } else if (nodo instanceof Expresion) {
                                Object val_nodo = ((Expresion) nodo).getValorImplicito(nuevo);
                                if (val_nodo instanceof Break) {
                                    return val_nodo;
                                } else if (val_nodo instanceof Continue) {
                                    return val_nodo;
                                } else if (val_nodo instanceof Return) {
                                    return val_nodo;
                                }
                            }
                        }
                    }
                } else {
                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La condicion debe de ser boolean", this.columna, this.fila));
                }
            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La condicion debe de ser boolean", this.columna, this.fila));
            }

        } catch (Exception e) {
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Error al ejecutar sentencia Si", this.columna, this.fila));
        }
        return null;
    }

    @Override
    public NodoDot generarDot(int numero) {
        NodoDot valorNodo = this.condicion.generarDot(numero);

        String nodo = "node" + valorNodo.Numero;
        String cuerpo = nodo + "[label=\"Sentencia_Si\"];\n";
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
        if (this.sentenciasElse != null) {
            String NodoSentenciasElse = "node" + numero++;
            cuerpo += NodoSentenciasElse + "[label=\"Sentencias_Else\"];\n";
            cuerpo += nodo + "->" + NodoSentenciasElse + ";\n";
            for (NodoAst nod : this.sentenciasElse) {
                NodoDot nuevo = nod.generarDot(numero);
                numero = nuevo.Numero;
                cuerpo += nuevo.Cuerpo;
                cuerpo += NodoSentenciasElse + " -> " + nuevo.Nombre + ";\n";
            }
        }

        NodoDot nuevo = new NodoDot(nodo, valorNodo.Cuerpo + cuerpo, numero + 1);
        return nuevo;
    }

}
