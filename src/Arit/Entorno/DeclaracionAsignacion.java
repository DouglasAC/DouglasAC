/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.Entorno;

import Arit.AltaAbstraccion.Expresion;
import Arit.AltaAbstraccion.Instruccion;
import Arit.Estructuras.*;
import Error.ErrorAr;
import Reportes.NodoDot;
import Reportes.NodoTabla;

/**
 *
 * @author ddani
 */
public class DeclaracionAsignacion extends Instruccion {

    String identificador;
    Expresion valor;

    public DeclaracionAsignacion(String identificador, Expresion valor, int fila, int columna) {
        super(fila, columna);
        this.identificador = identificador.toLowerCase();
        this.valor = valor;
    }

    @Override
    public Object ejecutar(Entorno en) {
        try {
            if (!en.existe(identificador)) {
                Object val = this.valor.getValorImplicito(en);
                if (val instanceof Vector) {
                    Vector vec = (Vector) val;
                    val = vec.clone();
                } else if (val instanceof Lista) {
                    Lista l = (Lista) val;
                    val = l.clone();
                } else if (val instanceof Matris) {
                    Matris m = (Matris) val;
                    val = m.clone();
                } else if (val instanceof Arreglo) {
                    Arreglo ar = (Arreglo) val;
                    val = ar.clone();
                }
                if (val != null) {
                    Simbolo nuevo = new Simbolo(this.identificador, val);
                    en.agregar(identificador, nuevo);
                    Informacion.Informacion.agregarSimbolo(new NodoTabla(nuevo, en.GetNombre(), "Variable"));
                }
            } else {
                Object val = this.valor.getValorImplicito(en);
                if (val instanceof Vector) {
                    Vector vec = (Vector) val;
                    val = vec.clone();
                } else if (val instanceof Lista) {
                    Lista l = (Lista) val;
                    val = l.clone();
                } else if (val instanceof Matris) {
                    Matris m = (Matris) val;
                    val = m.clone();
                } else if (val instanceof Arreglo) {
                    Arreglo ar = (Arreglo) val;
                    val = ar.clone();
                }
                Simbolo sim = en.getSimbolo(identificador);
                sim.valor = val;
                en.reemplazar(identificador, sim);
            }
        } catch (Exception e) {
            System.out.println(e);
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Error al declarar o asignar: " + this.identificador, this.fila, this.columna));
        }
        return null;
    }

    @Override
    public NodoDot generarDot(int numero) {
        NodoDot valorNodo = this.valor.generarDot(numero);
        String nodo = "node" + valorNodo.Numero;
        String cuerpo = nodo + "[label=\"Declaracion / Asignacion\"];\n";
        String nodoIdent = "node" + (valorNodo.Numero + 1);
        cuerpo += nodoIdent + "[label=\"Identificador: " + this.identificador + "\"];\n";
        cuerpo += nodo + " -> " + nodoIdent + ";\n";
        cuerpo += nodo + " -> " + valorNodo.Nombre + ";\n";
        NodoDot nuevo = new NodoDot(nodo, valorNodo.Cuerpo + cuerpo, valorNodo.Numero + 2);
        return nuevo;
    }

}
