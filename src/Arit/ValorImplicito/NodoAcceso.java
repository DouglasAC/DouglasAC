/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.ValorImplicito;

import Arit.AltaAbstraccion.Expresion;

/**
 *
 * @author ddani
 */
public class NodoAcceso {

    public Expresion posicion;
    public Expresion posicionY;
    public Boolean tipo;//tipo []
    public Boolean matriz;
    
    public NodoAcceso(Expresion posicion, Boolean tipo) {
        this.posicion = posicion;
        this.posicionY = null;
        this.tipo = tipo;
        this.matriz = false;
    }

    public NodoAcceso(Expresion posicion, Expresion posiciony, Boolean tipo, Boolean matriz) {
        this.posicion = posicion;
        this.posicionY = posiciony;
        this.tipo = tipo;
        this.matriz = matriz;
    }

    public Expresion getPosicion() {
        return posicion;
    }

    public void setPosicion(Expresion posicion) {
        this.posicion = posicion;
    }

    public Boolean getTipo() {
        return tipo;
    }

    public void setTipo(Boolean tipo) {
        this.tipo = tipo;
    }

    public Expresion getPosicionY() {
        return posicionY;
    }

    public void setPosicionY(Expresion posicionY) {
        this.posicionY = posicionY;
    }

    public Boolean getMatriz() {
        return matriz;
    }

    public void setMatriz(Boolean matriz) {
        this.matriz = matriz;
    }

    
    
}
