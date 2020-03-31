/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.ValorImplicito;

import Arit.AltaAbstraccion.Expresion;
import Arit.Entorno.Entorno;
import Arit.Estructuras.Nodo;
import Arit.Estructuras.Vector;
import Reportes.NodoDot;

/**
 *
 * @author ddani
 */
public class Primitivo extends Expresion {

    Object valor;
    String tipo;

    public Primitivo(Object valor, String tipo, int fila, int columna) {
        super(fila, columna);
        this.valor = valor;
        this.tipo = tipo;
    }

    @Override
    public NodoDot generarDot(int numero) {
        String nodo = "node" + numero;
        String cuerpo = nodo + "[label=\"Primitivo: " + valor + "\"];\n";
        NodoDot nuevo = new NodoDot(nodo, cuerpo, numero + 1);
        return nuevo;
    }

    @Override
    public Object getValorImplicito(Entorno en) {
        Vector nuevo = new Vector(this.tipo);
        nuevo.agregarFinal(new Nodo(valor));
        return nuevo;
    }

}
