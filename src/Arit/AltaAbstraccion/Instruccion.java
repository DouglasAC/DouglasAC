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
public abstract class Instruccion extends NodoAst {
    
    public Instruccion(int fila, int columna) {
        super(fila, columna);
    }
    
    public abstract Object ejecutar(Entorno en);
    
}
