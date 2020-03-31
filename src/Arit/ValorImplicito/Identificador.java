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

/**
 *
 * @author ddani
 */
public class Identificador extends Expresion {

    public String Identificador;

    public Identificador(String Identificador, int fila, int columna) {
        super(fila, columna);
        this.Identificador = Identificador.toLowerCase();
    }

    @Override
    public Object getValorImplicito(Entorno en) {
        try {
            if (en.existe(Identificador)) {
                Simbolo sim = en.getSimbolo(Identificador);
                Object val = sim.valor;
                if (val instanceof Vector) {
                    Vector v = (Vector) val;
                    return v;
                } else if (val instanceof Lista) {
                    Lista l = (Lista) val;
                    return l;
                } else if (val instanceof Matris) {
                    Matris m = (Matris) val;
                    return m;
                } else if (val instanceof Arreglo) {
                    Arreglo ar = (Arreglo) val;
                    return ar;
                }
            }else
            {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La variable: "+this.Identificador+" no existe", this.fila, this.columna));
            }
        } catch (Exception e) {
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Error al ejecutar Identificador", this.fila, this.columna));
        }
        return null;
    }

    @Override
    public NodoDot generarDot(int numero) {
        String nodo = "node" + numero;
        String cuerpo = nodo + "[label=\"Identificador: " + this.Identificador + "\"];\n";
        NodoDot nuevo = new NodoDot(nodo, cuerpo, numero + 1);
        return nuevo;
    }

}
