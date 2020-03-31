/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.Entorno;

/**
 *
 * @author ddani
 */
public class Simbolo {
    
    public String identificador;
    public Object valor;

    public Simbolo(String identificador) {
        this.identificador = identificador;
        this.valor = null;
    }

    public Simbolo(String identificador, Object valor) {
        this.identificador = identificador;
        this.valor = valor;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

}
