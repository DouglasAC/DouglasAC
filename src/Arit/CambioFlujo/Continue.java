/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.CambioFlujo;

import Arit.AltaAbstraccion.Expresion;
import Arit.Entorno.Entorno;
import Reportes.NodoDot;

/**
 *
 * @author ddani
 */
public class Continue extends Expresion {

    public Continue(int fila, int columna) {
        super(fila, columna);
    }

    @Override
    public Object getValorImplicito(Entorno en) {
        return this;
    }

    @Override
    public NodoDot generarDot(int numero) {
        String nodo = "node" + numero;
        String cuerpo = nodo + "[label=\"Continue\"];\n";
        NodoDot nuevo = new NodoDot(nodo, cuerpo, numero + 1);
        return nuevo;
    }

}
