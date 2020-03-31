/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Error;

/**
 *
 * @author ddani
 */
public class ErrorAr {
    
    String tipo;
    String descripcion;
    int fila;
    int columna;

    public ErrorAr(String tipo, String descripcion, int fila, int columna) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fila = fila;
        this.columna = columna;
    }

}
