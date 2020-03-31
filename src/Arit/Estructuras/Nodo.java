/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.Estructuras;

/**
 *
 * @author ddani
 */
public class Nodo implements Cloneable {

    public Object valor;

    public Nodo(Object valor) {
        this.valor = valor;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Nodo clone = null;
        try {
            clone = (Nodo) super.clone();

            if (this.valor instanceof Vector) {
                Vector n = (Vector) this.valor;
                clone.valor = (Vector) n.clone();
            } else if (this.valor instanceof Lista) {
                Lista l = (Lista) this.valor;
                clone.valor = (Lista) l.clone();
            } else if (this.valor instanceof Matris) {
                Matris m = (Matris) this.valor;
                clone.valor = (Matris) m.clone();
            } else if (this.valor instanceof Arreglo) {
                Arreglo a = (Arreglo) this.valor;
                clone.valor = (Arreglo) a.clone();
            } else {
                clone.valor = this.valor;
            }
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }

    @Override // this must return String
    public String toString() {
        String cuerpo = this.valor.toString();
        return cuerpo;
    }

}
