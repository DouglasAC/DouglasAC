/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.AltaAbstraccion;

import Reportes.NodoDot;

/**
 *
 * @author ddani
 */
public abstract class NodoAst {

    public int fila;
    public int columna;

    public NodoAst(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public abstract NodoDot generarDot(int numero);

}
