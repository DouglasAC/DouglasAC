/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.OperacionersPrimitivas;

import Arit.AltaAbstraccion.Expresion;
import Arit.AltaAbstraccion.Instruccion;
import Arit.Entorno.Entorno;
import Error.ErrorAr;
import Reportes.NodoDot;

/**
 *
 * @author ddani
 */
public class Imprimir extends Instruccion {

    Expresion valor;

    public Imprimir(Expresion valor, int fila, int columna) {
        super(fila, columna);
        this.valor = valor;
    }

    @Override
    public Object ejecutar(Entorno en) {
        try {
            Object imprimir = valor.getValorImplicito(en);
            System.out.println(valor);
            Informacion.Informacion.ImprimeEnConsola(imprimir.toString());
        } catch (Exception e) {
            System.out.println(e);
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Error al ejecutar el Imprimir", this.fila, this.columna));
        }
        return null;
    }

    @Override
    public NodoDot generarDot(int numero) {
        String cuerpo = "";
        String principal = "node" + numero++;
        cuerpo += principal + "[label=\"Sentencia_Imprimir\"];\n";

        String parametro = "node" + numero++;
        cuerpo += parametro + "[label=\"Expresion\"];\n";
        cuerpo += principal + " -> " + parametro + ";\n";

        NodoDot e = this.valor.generarDot(numero + 1);
        numero = e.Numero;
        cuerpo += e.Cuerpo;
        cuerpo += parametro + " -> " + e.Nombre + ";\n";

        return new NodoDot(principal, cuerpo, numero + 1);
    }

}
