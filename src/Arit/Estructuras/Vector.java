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
public class Vector implements Cloneable {

    public String tipo;
    public ArrayList<Nodo> valores;

    public Vector(String tipo) {
        this.tipo = tipo;
        this.valores = new ArrayList<>();
    }

    public Vector() {
        this.tipo = "";
        this.valores = new ArrayList<>();
    }

    public void agregarFinal(Nodo nuevo) {
        this.valores.add(nuevo);
    }

    public void agregarPosicion(int posicion, Nodo nuevo) {
        this.valores.add(posicion, nuevo);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ArrayList<Nodo> getValores() {
        return valores;
    }

    public void setValores(ArrayList valores) {
        this.valores = valores;
    }

    public int tamaño() {
        return this.valores.size();
    }

    public Vector getPosicion(int pos) {
        Vector nuevo = new Vector(this.tipo);
        if (pos < this.tamaño() && pos >= 0) {
            nuevo.agregarFinal(this.valores.get(pos));
            return nuevo;
        } else {
            return null;
        }
    }

    public String ponerTipo() {
        Object val = this.valores.get(0).valor;
        if (val instanceof Integer) {
            return "integer";
        } else if (val instanceof Double) {
            return "numeric";
        } else if (val instanceof Boolean) {
            return "boolean";
        } else if (val instanceof String) {
            return "string";
        }
        return "null";
    }

    public void ponerTipoNuevo() {
        int prioridad = 0;
        for (int x = 0; x < this.valores.size(); x++) {

            int nueva = this.prioridad2(this.valores.get(x));
            if (nueva > prioridad) {
                prioridad = nueva;
            }

        }
        switch (prioridad) {
            case 1:
                this.tipo = "integer";
                break;
            case 2:
                this.tipo = "numeric";
                break;
            case 0:
                this.tipo = "boolean";
                break;
            case 3:
                this.tipo = "string";
                break;
            default:
                break;
        }
        this.CastearValores(prioridad);
    }

    public int ObtenerPrioridada() {
        int prioridad = 0;
        for (int x = 0; x < this.valores.size(); x++) {

            int nueva = this.prioridad2(this.valores.get(x));
            if (nueva > prioridad) {
                prioridad = nueva;
            }

        }
        return prioridad;
    }

    public void insertarPosicion(int pos, Object val, int fila, int columna) {
        if (pos >= 0) {
            if (pos > this.tamaño() - 1) {
                llenarHasta(pos);
            }
            this.valores.get(pos).valor = val;
            String tipo_nuevo = this.NuevoTipo(this.valores.get(pos));
            if (!tipo_nuevo.equals(this.tipo)) {
                int prio_actual = this.prioridad(tipo);
                int prio_nuevo = this.prioridad(tipo_nuevo);
                if (prio_nuevo > prio_actual) {
                    this.CastearValores(prio_nuevo);
                } else {
                    this.CastearValores(prio_actual);
                }
                String t = ponerTipo();
                this.tipo = t;
            }
        } else {
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La posicion debe ser mayor a 0", fila, columna));
        }
    }

    public int prioridad2(Nodo val) {
        if (val.valor instanceof Integer) {
            return 1;
        } else if (val.valor instanceof Double) {
            return 2;
        } else if (val.valor instanceof Boolean) {
            return 0;
        } else if (val.valor instanceof String) {
            return 3;
        }

        return 0;
    }

    public void llenarHasta(int pos) {
        for (int x = this.tamaño(); x <= pos; x++) {
            if (this.tipo.equalsIgnoreCase("integer")) {
                int val = 0;
                this.valores.add(new Nodo(val));
            } else if (this.tipo.equalsIgnoreCase("numeric")) {
                double val = 0.0;
                this.valores.add(new Nodo(val));
            } else if (this.tipo.equalsIgnoreCase("boolean")) {
                this.valores.add(new Nodo(false));
            } else if (this.tipo.equalsIgnoreCase("String")) {
                this.valores.add(new Nodo("null"));
            }
        }
    }

    public String NuevoTipo(Nodo val) {
        if (val.valor instanceof Integer) {
            return "integer";
        } else if (val.valor instanceof Double) {
            return "numeric";
        } else if (val.valor instanceof Boolean) {
            return "boolean";
        } else if (val.valor instanceof String) {
            return "string";
        }
        return "null";
    }

    public int prioridad(String tipo) {
        if (tipo.equalsIgnoreCase("integer")) {
            return 1;
        } else if (tipo.equalsIgnoreCase("numeric")) {
            return 2;
        } else if (tipo.equalsIgnoreCase("boolean")) {
            return 0;
        } else if (tipo.equalsIgnoreCase("String")) {
            return 3;
        }
        return 0;
    }

    public void CastearValores(int prioridad) {
        for (int x = 0; x < this.valores.size(); x++) {
            Object actual = this.valores.get(x).valor;
            if (prioridad == 1 && actual instanceof Boolean) {
                boolean val = (boolean) actual;
                int nuevo = val ? 1 : 0;
                this.valores.get(x).valor = nuevo;
            }
            if (prioridad == 2 && actual instanceof Boolean) {
                boolean val = (boolean) actual;
                double nuevo = val ? 1.0 : 0.0;
                this.valores.get(x).valor = nuevo;
            } else if (prioridad == 2 && actual instanceof Integer) {
                int val = (int) actual;
                double nuevo = val + 0.0;
                this.valores.get(x).valor = nuevo;
            }
            if (prioridad == 3 && actual instanceof Boolean) {
                boolean val = (boolean) actual;
                String nuevo = val ? "true" : "false";
                this.valores.get(x).valor = nuevo;
            } else if (prioridad == 3 && actual instanceof Integer) {
                int val = (int) actual;
                String nuevo = String.valueOf(val);
                this.valores.get(x).valor = nuevo;
            } else if (prioridad == 3 && actual instanceof Double) {
                double val = (double) actual;
                String nuevo = String.valueOf(val);
                this.valores.get(x).valor = nuevo;
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Vector clone = null;
        try {
            clone = (Vector) super.clone();
            ArrayList<Nodo> nuevo = new ArrayList<>();
            for(Nodo n: this.valores)
            {
                nuevo.add((Nodo)n.clone());
            }
            //Copy new date object to cloned method
            //clone.setDob((Date) this.getDob().clone());
            clone.setValores(nuevo);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }

    @Override // this must return String
    public String toString() {
        String cuerpo = "";
        for (Object ov : this.valores) {
            cuerpo += ov.toString() + "  ";
        }
        return cuerpo;
    }
}
