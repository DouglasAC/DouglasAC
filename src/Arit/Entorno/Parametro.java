/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.Entorno;

import Arit.AltaAbstraccion.Expresion;

/**
 *
 * @author ddani
 */
public class Parametro {

    String identificador;
    Expresion defecto;

    public Parametro(String identificador, Expresion defecto) {
        this.identificador = identificador.toLowerCase();
        this.defecto = defecto;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Expresion getDefecto() {
        return defecto;
    }

    public void setDefecto(Expresion defecto) {
        this.defecto = defecto;
    }

}
