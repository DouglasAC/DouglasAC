/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.Estructuras;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class Arreglo implements Cloneable {

    LinkedList<Integer> dimensiones;
    ArrayList<Nodo> valores;
    boolean vector;
    int tamaño;

    public Arreglo(LinkedList<Integer> dimensiones) {
        this.dimensiones = dimensiones;
        this.valores = new ArrayList<>();
        int tam = 1;
        for (int i : this.dimensiones) {
            tam *= i;
        }
        this.tamaño = tam;
        llenarNulos();
    }

    public void llenarLista() {
        for (int x = 0; x < this.valores.size(); x++) {
            if (!(this.valores.get(x).valor instanceof Lista)) {
                Lista nueva = new Lista();
                nueva.agregarFinal(this.valores.get(x));
                this.valores.set(x, new Nodo(nueva));
            }
        }

    }

    public LinkedList<Integer> getDimensiones() {
        return dimensiones;
    }

    public ArrayList<Nodo> getValores() {
        return valores;
    }

    public boolean isVector() {
        return vector;
    }

    public int getTamaño() {
        return tamaño;
    }

    public void setValores(ArrayList<Nodo> valores) {
        this.valores = valores;
    }

    public void setVector(boolean vector) {
        this.vector = vector;
    }

    public void llenarNulos() {
        for (int x = 0; x < this.tamaño; x++) {
            Vector nuevo = new Vector();
            nuevo.agregarFinal(new Nodo("null"));
            nuevo.ponerTipoNuevo();
            valores.add(new Nodo(nuevo));
        }
    }

    public String getTipo() {
        if (this.isVector()) {
            int prioridad = 0;
            for (Nodo o : this.valores) {
                if (o.valor instanceof Vector) {
                    Vector v = (Vector) o.valor;
                    int comp = v.ObtenerPrioridada();
                    if (comp > prioridad) {
                        prioridad = comp;
                    }
                }
            }

            for (Nodo o : this.valores) {
                if (o.valor instanceof Vector) {
                    Vector v = (Vector) o.valor;
                    v.CastearValores(prioridad);
                    v.ponerTipoNuevo();
                }
            }
            for (Nodo o : this.valores) {
                if (o.valor instanceof Vector) {
                    Vector v = (Vector) o.valor;
                    return v.getTipo();
                }
            }

        }
        return "list";
    }

    public Object Obetener(LinkedList<Integer> accesos) throws CloneNotSupportedException {
        if (accesos.size() == this.dimensiones.size()) {
            if (accesos.size() == 1) {
                int val = accesos.getFirst();
                int valComp = this.dimensiones.getFirst();
                if (val > 0 && val <= valComp) {
                    int pos = accesos.getFirst() - 1;
                    Arreglo clon = (Arreglo) this.clone();
                    return clon.valores.get(pos).valor;

                }
                return null;

            } else {
                int pos = accesos.getLast() - 1;
                for (int x = accesos.size() - 2; x >= 0; x--) {
                    int val = accesos.get(x);
                    int valComp = this.dimensiones.get(x);
                    if (val > 0 && val <= valComp) {
                        pos = pos * this.dimensiones.get(x);
                        pos = pos + accesos.get(x) - 1;
                    } else {
                        return null;
                    }
                }
                Arreglo clon = (Arreglo) this.clone();
                return clon.valores.get(pos).valor;

            }
        }
        return null;
    }

    public Object ObtenerSinClon(LinkedList<Integer> accesos) {
        if (accesos.size() == this.dimensiones.size()) {
            if (accesos.size() == 1) {
                int val = accesos.getFirst();
                int valComp = this.dimensiones.getFirst();
                if (val > 0 && val <= valComp) {
                    int pos = accesos.getFirst() - 1;
                    return this.valores.get(pos).valor;

                }
                return null;

            } else {
                int pos = accesos.getLast() - 1;
                for (int x = accesos.size() - 2; x >= 0; x--) {
                    int val = accesos.get(x);
                    int valComp = this.dimensiones.get(x);
                    if (val > 0 && val <= valComp) {
                        pos = pos * this.dimensiones.get(x);
                        pos = pos + accesos.get(x) - 1;
                    } else {
                        return null;
                    }
                }

                return this.valores.get(pos).valor;

            }
        }
        return null;
    }

    public boolean insertar(LinkedList<Integer> accesos, Object valor) {
        if (accesos.size() == this.dimensiones.size()) {
            if (accesos.size() == 1) {
                int val = accesos.getFirst();
                int valComp = this.dimensiones.getFirst();
                if (val > 0 && val <= valComp) {
                    int pos = accesos.getFirst() - 1;
                    this.valores.get(pos).valor = valor;
                    return true;
                }
                return false;

            } else {
                int pos = accesos.getLast() - 1;
                for (int x = accesos.size() - 2; x >= 0; x--) {
                    int val = accesos.get(x);
                    int valComp = this.dimensiones.get(x);
                    if (val > 0 && val <= valComp) {
                        pos = pos * this.dimensiones.get(x);
                        pos = pos + accesos.get(x) - 1;
                    } else {
                        return false;
                    }
                }
                this.valores.get(pos).valor = valor;
                return true;
            }
        }
        return false;
    }

    @Override // this must return String
    public String toString() {

        String cuerpo = "";
        if (this.dimensiones.size() == 1) {
            for (int x = 0; x < this.valores.size(); x++) {
                Object ov = this.valores.get(x);
                cuerpo += "[" + (x + 1) + "]\n";
                cuerpo += "" + ov.toString() + "\n";
            }
        } else if (this.dimensiones.size() == 2) {
            int filas = this.dimensiones.get(0);
            int columnas = this.dimensiones.get(1);
            cuerpo += "     ";
            for (int x = 0; x < columnas; x++) {
                cuerpo += "[," + (x + 1) + "]   ";
            }
            cuerpo += "\n";
            for (int x = 1; x <= filas; x++) {
                cuerpo += "[" + (x + 1) + ",]   ";
                for (int y = 1; y <= columnas; y++) {
                    LinkedList<Integer> posicion = new LinkedList<Integer>();
                    posicion.add(x);
                    posicion.add(y);
                    cuerpo += this.ObtenerSinClon(posicion).toString() + "    ";
                }
                cuerpo += "\n";
            }
        } else {
            LinkedList<Integer> valores = new LinkedList<>();
            for (int x = this.dimensiones.size() - 1; x >= 0; x--) {
                valores.add(this.dimensiones.get(x));
            }
            LinkedList<Integer> posicion = new LinkedList<Integer>();
            cuerpo += masDeDos(valores, "", posicion);
        }

        return cuerpo;
    }

    public String masDeDos(LinkedList<Integer> valores, String pos, LinkedList<Integer> posicion) {
        String cuerpo = "";
        if (valores.size() > 2) {
            int val = valores.getFirst();
            valores.removeFirst();
            for (int x = 1; x <= val; x++) {
                posicion.addFirst(x);
                cuerpo += masDeDos(valores, "," + x + pos, posicion);
                posicion.removeFirst();
            }
            valores.addFirst(val);
        } else {
            int filas = this.dimensiones.get(0);
            int columnas = this.dimensiones.get(1);
            cuerpo += "," + pos + "\n";
            cuerpo += "     ";
            for (int x = 0; x < columnas; x++) {
                cuerpo += "[," + (x + 1) + "]   ";
            }
            cuerpo += "\n";
            for (int x = 0; x < filas; x++) {
                cuerpo += "[" + (x + 1) + ",]   ";
                for (int y = 0; y < columnas; y++) {

                    posicion.addFirst(y + 1);
                    posicion.addFirst(x + 1);
                    cuerpo += this.ObtenerSinClon(posicion).toString() + "    ";
                    posicion.removeFirst();
                    posicion.removeFirst();
                }
                cuerpo += "\n";
            }
        }
        return cuerpo;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Arreglo clone = null;
        try {

            clone = (Arreglo) super.clone();

            //Copy new date object to cloned method
            //clone.setDob((Date) this.getDob().clone());
            ArrayList<Nodo> objetos = new ArrayList<>();
            for (Nodo o : this.valores) {
                objetos.add((Nodo) o.clone());
            }
            clone.valores = objetos;
            /*clone = (Lista) super.clone();
            ArrayList<Object> cloneLista = new ArrayList<>();*/

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }

}
