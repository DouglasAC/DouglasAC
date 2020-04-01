/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.Estructuras;

import Error.ErrorAr;
import java.util.ArrayList;

/**
 *
 * @author ddani
 */
public class Lista implements Cloneable {

    public ArrayList<Nodo> valores;

    public Lista() {
        this.valores = new ArrayList<>();
    }

    public Lista(ArrayList<Nodo> valores) {
        this.valores = valores;
    }

    public void agregarFinal(Nodo nuevo) {
        this.valores.add(nuevo);
    }

    public int tamaño() {
        return this.valores.size();
    }

    public void insertarPosicion(int pos, Object val, int fila, int columna) {
        if (pos >= 0) {
            if (pos > this.tamaño() - 1) {
                llenarHasta(pos);
            }
            this.valores.get(pos).valor = val;
        } else {
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La posicion debe ser mayor a 0", fila, columna));
        }
    }

    public void llenarHasta(int pos) {
        for (int x = this.tamaño(); x <= pos; x++) {
            Vector nulo = new Vector("String");
            nulo.agregarFinal(new Nodo("null"));
            this.valores.add(new Nodo(nulo));

        }
    }

    public void setValores(ArrayList<Nodo> valores) {
        this.valores = valores;
    }

    public Nodo getPosicion(int pos) throws CloneNotSupportedException {
        Lista l = (Lista) this.clone();
        if ( pos >= 0) {
            if (pos > this.tamaño() - 1) {
                llenarHasta(pos);
            }
            return l.valores.get(pos);
        } else {
            return null;
        }
    }

    public Nodo getPosicionSinClon(int pos) {
        if (pos >= 0) {
            if (pos > this.tamaño() - 1) {
                llenarHasta(pos);
            }
            return this.valores.get(pos);
        } else {
            return null;
        }
    }

    public Object getPosicionSegundoAcceeso(int pos) throws CloneNotSupportedException {
        Lista l = (Lista) this.clone();
        if (pos >= 0) {
            if (pos > this.tamaño() - 1) {
                llenarHasta(pos);
            }
            Lista nueva = new Lista();
            nueva.agregarFinal(l.valores.get(pos));
            return nueva;
        } else {
            return null;
        }
    }

    public Object getPosicionSegundoAcceesoSinClon(int pos) {
        if (pos >= 0) {
            if (pos > this.tamaño() - 1) {
                llenarHasta(pos);
            }
            Lista nueva = new Lista();
            nueva.agregarFinal(this.valores.get(pos));
            return nueva;
        } else {
            return null;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Lista clone = null;
        try {

            clone = (Lista) super.clone();

            //Copy new date object to cloned method
            //clone.setDob((Date) this.getDob().clone());
            ArrayList<Nodo> objetos = new ArrayList<Nodo>();
            for (Nodo o : this.valores) {
                objetos.add((Nodo) o.clone());
            }
            clone.setValores(objetos);
            /*clone = (Lista) super.clone();
            ArrayList<Object> cloneLista = new ArrayList<>();*/

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }

    @Override // this must return String
    public String toString() {
        String cuerpo = "";
        for (int x = 0; x < this.valores.size(); x++) {
            Object ov = this.valores.get(x);
            cuerpo += "[[" + (x + 1) + "]]\n";
            cuerpo += "" + ov.toString() + "\n";
        }
        return cuerpo;
    }

}
