/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.CambioFlujo;

import Arit.AltaAbstraccion.Expresion;
import Arit.Entorno.Entorno;
import Error.ErrorAr;
import Informacion.Informacion;
import Reportes.NodoDot;

/**
 *
 * @author ddani
 */
public class Return extends Expresion {

    public Expresion retorno;
    public Object valor;

    public Return(Expresion retorno, int fila, int columna) {
        super(fila, columna);
        this.retorno = retorno;
    }

    @Override
    public Object getValorImplicito(Entorno en) {
        try {
            valor = retorno.getValorImplicito(en);
            return this;
        } catch (Exception e) {
            Informacion.agregarError(new ErrorAr("Semantico", "Erroral realizar retorno ", this.fila, this.columna));
        }
        return null;
    }

    @Override
    public NodoDot generarDot(int numero) {

        if (this.retorno != null) {
            String nodo = "node" + numero;
            String cuerpo = nodo + "[label=\"Return\"];\n";
            NodoDot val = this.retorno.generarDot(numero + 1);
            cuerpo += nodo + " -> " + val.Nombre + ";\n";
            NodoDot nuevo = new NodoDot(nodo, val.Cuerpo + cuerpo, val.Numero + 1);
            return nuevo;
        } else {
            String nodo = "node" + numero;
            String cuerpo = nodo + "[label=\"Return\"];\n";
            NodoDot nuevo = new NodoDot(nodo, cuerpo, numero + 1);
            return nuevo;
        }
    }

}
