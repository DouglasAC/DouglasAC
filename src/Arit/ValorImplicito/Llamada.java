/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.ValorImplicito;

import Arit.AltaAbstraccion.Expresion;
import Arit.Entorno.Entorno;
import Arit.Entorno.Funcion;
import Arit.Entorno.Parametro;
import Arit.Entorno.Simbolo;
import Arit.OperacionersPrimitivas.FuncionC;
import Arit.OperacionersPrimitivas.List;
import Arit.OperacionersPrimitivas.Media;
import Arit.OperacionersPrimitivas.Mediana;
import Arit.OperacionersPrimitivas.Moda;
import Error.ErrorAr;
import Reportes.NodoDot;
import Reportes.NodoTabla;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class Llamada extends Expresion {
    
    String identificador;
    LinkedList<Expresion> valores;
    
    public Llamada(String identificador, LinkedList<Expresion> valores, int fila, int columna) {
        super(fila, columna);
        this.identificador = identificador.toLowerCase();
        this.valores = valores;
    }
    
    @Override
    public Object getValorImplicito(Entorno en) {
        try {
            Entorno glob = en.getGlobal();
            if (glob.existeFuncion(identificador)) {
                Funcion fun = glob.getFuncion(identificador);
                if (fun instanceof List) {
                    List l = (List) fun;
                    l.fila = this.fila;
                    l.columna = this.columna;
                    l.serValores(valores);
                    return l.ejecutar(en);
                } else if (fun instanceof FuncionC) {
                    FuncionC c = (FuncionC) fun;
                    c.fila = this.fila;
                    c.columna = this.columna;
                    c.setValores(valores);
                    return c.ejecutar(en);
                } else if (fun instanceof Media) {
                    Media c = (Media) fun;
                    c.fila = this.fila;
                    c.columna = this.columna;
                    c.setValores(valores);
                    return c.ejecutar(en);
                } else if (fun instanceof Mediana) {
                    Mediana c = (Mediana) fun;
                    c.fila = this.fila;
                    c.columna = this.columna;
                    c.setValores(valores);
                    return c.ejecutar(en);
                } else if (fun instanceof Moda) {
                    Moda c = (Moda) fun;
                    c.fila = this.fila;
                    c.columna = this.columna;
                    c.setValores(valores);
                    return c.ejecutar(en);
                } else if (valores.size() == fun.getParametros().size()) {
                    Entorno local = new Entorno(en.getGlobal());
                    local.SetNombre(fun.identificador);
                    for (int x = 0; x < valores.size(); x++) {
                        Object val = valores.get(x).getValorImplicito(en);
                        Parametro par = fun.getParametros().get(x);
                        if (val instanceof Defecto) {
                            
                            if (par.getDefecto() != null) {
                                val = par.getDefecto().getValorImplicito(en);
                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro: " + par.getIdentificador() + " no tiene valor por defecto de la funcion: " + this.identificador, this.fila, this.columna));
                                return null;
                            }
                            
                        }
                        if (!local.existeactual(par.getIdentificador())) {
                            Simbolo sim = new Simbolo(par.getIdentificador(), val);
                            local.agregar(par.getIdentificador(), sim);
                            if (!fun.isIngresados()) {
                                Informacion.Informacion.agregarSimbolo(new NodoTabla(sim, local.GetNombre(), "Parametro"));  
                            }
                        } else {
                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro: " + par.getIdentificador() + " ya existe en la funcion: " + this.identificador, this.fila, this.columna));
                            return null;
                        }
                    }
                    fun.setIngresados(true);
                    return fun.ejecutar(local);
                } else {
                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La cantidad de parametros de la llamada no es igual a la cantidad de parametros de la funcion: " + this.identificador, this.fila, this.columna));
                }
                
            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "No existe la funcion: " + this.identificador, this.fila, this.columna));
            }
        } catch (Exception e) {
            System.out.print(e);
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Error al ejecutar la llamada: " + this.identificador, this.fila, this.columna));
        }
        
        return null;
    }

    /*
    String identificador;
    LinkedList<Expresion> valores;
     */
    @Override
    public NodoDot generarDot(int numero) {
        String cuerpo = "";
        String principal = "node" + numero++;
        cuerpo += principal + "[label=\"Llamada\"];\n";
        String ide = "node" + numero++;
        cuerpo += ide + "[label=\"Identificador: " + this.identificador + "\"];\n";
        cuerpo += principal + " -> " + ide + ";\n";
        
        String parametros = "node" + numero++;
        cuerpo += parametros + "[label=\"Expresiones\"];\n";
        cuerpo += principal + " -> " + parametros + ";\n";
        for (Expresion par : this.valores) {
            String parametro = "node" + numero++;
            cuerpo += parametro + "[label=\"Expresion\"];\n";
            cuerpo += parametros + " -> " + parametro + ";\n";
            
            NodoDot e = par.generarDot(numero + 1);
            numero = e.Numero;
            cuerpo += e.Cuerpo;
            cuerpo += parametro + " -> " + e.Nombre + ";\n";
            
        }
        return new NodoDot(principal, cuerpo, numero + 1);
    }
    
}
