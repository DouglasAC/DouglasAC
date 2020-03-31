/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.ValorImplicito;

import Arit.AltaAbstraccion.Expresion;
import Arit.Entorno.Entorno;
import Arit.Estructuras.Vector;
import Error.ErrorAr;
import Informacion.Informacion;
import Reportes.NodoDot;

/**
 *
 * @author ddani
 */
public class Ternario extends Expresion {

    Expresion condicion;
    Expresion verdadero;
    Expresion falso;

    public Ternario(Expresion condicion, Expresion verdadero, Expresion falso, int fila, int columna) {
        super(fila, columna);
        this.condicion = condicion;
        this.verdadero = verdadero;
        this.falso = falso;
    }

    @Override
    public Object getValorImplicito(Entorno en) {
        try {
            Object cond = this.condicion.getValorImplicito(en);
            if (cond instanceof Vector) {
                Vector vcond = (Vector) cond;
                if (vcond.valores.get(0).valor instanceof Boolean) {
                    if ((boolean) vcond.valores.get(0).valor) {
                        Object ver = this.verdadero.getValorImplicito(en);
                        return ver;
                    } else {
                        Object fal = this.falso.getValorImplicito(en);
                        return fal;
                    }
                } else {
                    Informacion.agregarError(new ErrorAr("Semantico", "La condicion del Operador Ternario debe ser de tipo boolean", this.fila, this.columna));
                }
            } else {
                Informacion.agregarError(new ErrorAr("Semantico", "La condicion del Operador Ternario debe ser de tipo boolean", this.fila, this.columna));
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    @Override
    public NodoDot generarDot(int numero) {
        String cuerpo = "";
        String principal = "node" + numero++;
        cuerpo += principal + "[label=\"Operador_Ternario\"];\n";

        String condicion = "node" + numero++;
        cuerpo += condicion + "[label=\"Condicion\"];\n";
        cuerpo += principal + " -> " + condicion + ";\n";
        NodoDot c = this.condicion.generarDot(numero + 1);
        numero = c.Numero;
        cuerpo += c.Cuerpo;
        cuerpo += condicion + " -> " + c.Nombre + ";\n";

        String verdadero = "node" + numero++;
        cuerpo += verdadero + "[label=\"Verdadero\"];\n";
        cuerpo += principal + " -> " + verdadero + ";\n";
        NodoDot v = this.verdadero.generarDot(numero + 1);
        numero = v.Numero;
        cuerpo += v.Cuerpo;
        cuerpo += verdadero + " -> " + v.Nombre + ";\n";

        String falso = "node" + numero++;
        cuerpo += falso + "[label=\"Falso\"];\n";
        cuerpo += principal + " -> " + falso + ";\n";
        NodoDot f = this.falso.generarDot(numero + 1);
        numero = f.Numero;
        cuerpo += f.Cuerpo;
        cuerpo += falso + " -> " + f.Nombre + ";\n";

        return new NodoDot(principal, cuerpo, numero + 1);
    }

}
