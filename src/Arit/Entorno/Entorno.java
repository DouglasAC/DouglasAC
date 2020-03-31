/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.Entorno;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author ddani
 */
public class Entorno {

    Hashtable<String, Simbolo> tabla;
    Hashtable<String, Funcion> funciones;
    String nombre;
    Entorno anterior;

    public Entorno(Entorno anterior) {
        tabla = new Hashtable<String, Simbolo>();
        funciones = new Hashtable<String, Funcion>();
        this.anterior = anterior;
        this.nombre = "";
    }

    public void SetNombre(String nombre) {
        this.nombre = nombre;
    }

    public String GetNombre() {
        return this.nombre;
    }

    public void agregar(String id, Simbolo sim) {
        tabla.put(id.toLowerCase(), sim);
    }

    public boolean existe(String id) {
        for (Entorno e = this; e != null; e = e.anterior) {
            if (e.tabla.containsKey(id.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public boolean existeactual(String id) {
        boolean encontrado = this.tabla.containsKey(id.toLowerCase());
        return encontrado;
    }

    public Simbolo getSimbolo(String id) {
        for (Entorno e = this; e != null; e = e.anterior) {
            if (e.tabla.containsKey(id.toLowerCase())) {
                return e.tabla.get(id.toLowerCase());
            }
        }
        System.out.println("El simbolo " + id + " no ha sido declarado en ningun entorno");
        return null;
    }

    public void setSimbolo(String id, Simbolo nuevo) {
        for (Entorno e = this; e != null; e = e.anterior) {
            if (e.tabla.containsKey(id.toLowerCase())) {
                e.tabla.replace(id.toLowerCase(), nuevo);
            }
        }
        System.out.println("El simbolo " + id + " no ha sido declarado en ningun entorno");
    }

    public Hashtable<String, Simbolo> getTabla() {
        return tabla;
    }

    public void setTabla(Hashtable<String, Simbolo> tabla) {
        this.tabla = tabla;
    }

    public Entorno getAnterior() {
        return anterior;
    }

    public void setAnterior(Entorno anterior) {
        this.anterior = anterior;
    }

    public void imprimirEntorno() {
        Enumeration e = tabla.keys();
        Object clave;
        while (e.hasMoreElements()) {
            clave = e.nextElement();
            System.out.println("Clave : " + clave);
        }
    }

    public void reemplazar(String id, Simbolo nuevoValor) {
        for (Entorno e = this; e != null; e = e.getAnterior()) {
            Simbolo encontrado = (Simbolo) (e.getSimbolo(id.toLowerCase()));
            if (encontrado != null) {
                e.tabla.replace(id.toLowerCase(), nuevoValor);
                return;
            }
        }
        System.out.println("El simbolo \"" + id + "\" no ha sido declarado en el entorno actual ni en alguno externo");
    }

    public Entorno getGlobal() {
        Entorno e = this;
        while (e.anterior != null) {
            e = e.anterior;
        }
        return e;
    }

    public void agregarFuncion(String id, Funcion fun) {
        this.funciones.put(id.toLowerCase(), fun);
    }

    public boolean existeFuncion(String id) {
        for (Entorno e = this; e != null; e = e.anterior) {
            if (e.funciones.containsKey(id.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public Funcion getFuncion(String id) {
        for (Entorno e = this; e != null; e = e.anterior) {
            if (e.funciones.containsKey(id.toLowerCase())) {
                return e.funciones.get(id.toLowerCase());
            }
        }
        System.out.println("La funcion " + id + " no ha sido declarado");
        return null;
    }

}
