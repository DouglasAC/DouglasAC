/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import Arit.Entorno.Simbolo;

/**
 *
 * @author ddani
 */
public class NodoTabla {
    
    Simbolo sim;
    String entorno;
    String rol;   

    public NodoTabla(Simbolo sim, String entorno, String rol) {
        this.sim = sim;
        this.entorno = entorno;
        this.rol = rol;
    }
    
}
