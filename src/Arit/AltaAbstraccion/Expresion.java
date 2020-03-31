/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.AltaAbstraccion;

import Arit.Entorno.Entorno;

/**
 *
 * @author ddani
 */
public abstract class Expresion extends NodoAst {

    public Expresion(int fila, int columna) {
        super(fila, columna);
    }

    public abstract Object getValorImplicito(Entorno en);

}
