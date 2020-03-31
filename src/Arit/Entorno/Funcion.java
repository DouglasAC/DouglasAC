/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.Entorno;

import Arit.AltaAbstraccion.Expresion;
import Arit.AltaAbstraccion.Instruccion;
import Arit.AltaAbstraccion.NodoAst;
import Arit.CambioFlujo.Break;
import Arit.CambioFlujo.Continue;
import Arit.CambioFlujo.Return;
import Error.ErrorAr;
import Reportes.NodoDot;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class Funcion extends Instruccion {

    public String identificador;
    LinkedList<Parametro> parametros;
    LinkedList<NodoAst> sentencias;
    boolean ingresados;

    public Funcion(String identificador, LinkedList<Parametro> parametros, LinkedList<NodoAst> sentencias, int fila, int columna) {
        super(fila, columna);
        this.identificador = identificador.toLowerCase();
        this.parametros = parametros;
        this.sentencias = sentencias;
        this.ingresados = false;
    }

    @Override
    public Object ejecutar(Entorno en) {
        try {
            for (NodoAst nodo : this.sentencias) {
                if (nodo instanceof Instruccion) {
                    Object val_nodo = ((Instruccion) nodo).ejecutar(en);
                    if (val_nodo instanceof Break) {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Break no esta en el lugar que le corresponde", this.columna, this.fila));
                    } else if (val_nodo instanceof Continue) {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Continue no esta en el lugar que le corresponde", this.columna, this.fila));
                    } else if (val_nodo instanceof Return) {
                        return ((Return) val_nodo).valor;
                    }
                } else if (nodo instanceof Expresion) {
                    Object val_nodo = ((Expresion) nodo).getValorImplicito(en);
                    if (val_nodo instanceof Break) {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Break no esta en el lugar que le corresponde", this.columna, this.fila));
                    } else if (val_nodo instanceof Continue) {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Continue no esta en el lugar que le corresponde", this.columna, this.fila));
                    } else if (val_nodo instanceof Return) {
                        return ((Return) val_nodo).valor;
                    }
                }
            }

        } catch (Exception e) {
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Error al ejecutar funcion: " + this.identificador, this.columna, this.fila));
        }
        return null;
    }

    /* public String identificador;
    LinkedList<Parametro> parametros;
    LinkedList<NodoAst> sentencias;*/
    @Override
    public NodoDot generarDot(int numero) {
        String cuerpo = "";
        String principal = "node" + numero++;
        cuerpo += principal + "[label=\"Sentencia_Funcion\"];\n";
        String idn = "node" + numero++;
        cuerpo += idn + "[label=\"Identificador: " + this.identificador + "\"];\n";
        cuerpo += principal + " -> " + idn + ";\n";

        String parametros2 = "node" + numero++;
        cuerpo += parametros2 + "[label=\"Parametros\"];\n";
        cuerpo += principal + " -> " + parametros2 + ";\n";
        for (Parametro par : this.parametros) {
            String parametro = "node" + numero++;
            cuerpo += parametro + "[label=\"Parametro\"];\n";
            cuerpo += parametros2 + " -> " + parametro + ";\n";
            String id = "node" + numero++;
            cuerpo += id + "[label=\"Identificador: " + par.getIdentificador() + "\"];\n";
            cuerpo += parametro + " -> " + id + ";\n";
            if (par.getDefecto() != null) {
                String def = "node" + numero++;
                cuerpo += def + "[label=\"Defecto\"];\n";
                cuerpo += parametro + " -> " + def + ";\n";
                NodoDot d = par.getDefecto().generarDot(numero + 1);
                numero = d.Numero;
                cuerpo += d.Cuerpo;
                cuerpo += def + " -> " + d.Nombre + ";\n";
            }
        }
        String NodoSentencias = "node" + numero++;
        cuerpo += NodoSentencias + "[label=\"Sentencias\"];\n";
        cuerpo += principal + "->" + NodoSentencias + ";\n";
        for (NodoAst nod : this.sentencias) {
            NodoDot nuevo = nod.generarDot(numero);
            numero = nuevo.Numero;
            cuerpo += nuevo.Cuerpo;
            cuerpo += NodoSentencias + " -> " + nuevo.Nombre + ";\n";
        }

        return new NodoDot(principal, cuerpo, numero + 1);
    }

    public LinkedList<Parametro> getParametros() {
        return parametros;
    }

    public LinkedList<NodoAst> getSentencias() {
        return sentencias;
    }

    public boolean isIngresados() {
        return ingresados;
    }

    public void setIngresados(boolean ingresados) {
        this.ingresados = ingresados;
    }

}
