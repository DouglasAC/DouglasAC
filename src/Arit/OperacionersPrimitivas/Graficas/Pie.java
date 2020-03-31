/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.OperacionersPrimitivas.Graficas;

import Arit.AltaAbstraccion.NodoAst;
import Arit.Entorno.Entorno;
import Arit.Entorno.Funcion;
import Arit.Entorno.Parametro;
import Arit.Entorno.Simbolo;
import Arit.Estructuras.Nodo;
import Arit.Estructuras.Vector;
import Error.ErrorAr;
import java.util.LinkedList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author ddani
 */
public class Pie extends Funcion {

    public Pie(String identificador, LinkedList<Parametro> parametros, LinkedList<NodoAst> sentencias, int fila, int columna) {
        super(identificador, parametros, sentencias, fila, columna);
    }

    @Override
    public Object ejecutar(Entorno en) {
        Simbolo sim_x = en.getSimbolo("parametro&&x&&matris01210");
        Simbolo sim_labs = en.getSimbolo("parametro&&labels&&matris01210");
        Simbolo sim_main = en.getSimbolo("parametro&&main&&matris01210");

        Object val_x = sim_x.getValor();
        if (val_x instanceof Vector) {
            Object val_labs = sim_labs.getValor();
            if (val_labs instanceof Vector) {
                Object val_main = sim_main.getValor();
                if (val_main instanceof Vector) {
                    LinkedList<Object> Xval = new LinkedList<>();
                    for (Nodo ob : ((Vector) val_x).getValores()) {
                        if (ob.valor instanceof Integer || ob.valor instanceof Double) {
                            Xval.add(ob.valor);
                        } else {
                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Los valores del vector X deben de ser Numericos", this.fila, this.columna));
                            return null;
                        }
                    }
                    LinkedList<String> Lval = new LinkedList<>();
                    for (Nodo ob : ((Vector) val_labs).getValores()) {
                        if (ob.valor instanceof String) {
                            Lval.add((String) ob.valor);
                        } else {
                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Los valores del vector Labs deben de ser String", this.fila, this.columna));
                            return null;
                        }
                    }
                    String mval = "";
                    Vector mvec = (Vector) val_main;
                    if (mvec.valores.get(0).valor instanceof String) {
                        mval = (String) mvec.valores.get(0).valor;
                    } else {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Main en la grafica de pie debe ser String", this.fila, this.columna));
                        return null;
                    }

                    if (Xval.size() > Lval.size()) {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La cantidad de labels es menor a la cantidad de valores", this.fila, this.columna));
                        int dif = Xval.size() - Lval.size();
                        for (int x = 0; x < dif; x++) {
                            Lval.add("Desconocido " + (x + 1));
                        }
                    }

                    DefaultPieDataset data = new DefaultPieDataset();
                    for (int x = 0; x < Xval.size(); x++) {
                        Object val = Xval.get(x);
                        if (val instanceof Integer) {
                            data.setValue(Lval.get(x), (int) val);
                        } else if (val instanceof Double) {
                            data.setValue(Lval.get(x), (double) val);
                        }
                    }
                    JFreeChart chart = ChartFactory.createPieChart(mval, data, true, true, false);
                    ChartFrame frame = new ChartFrame(mval, chart);
                    frame.pack();
                    frame.setVisible(true);

                } else {
                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Main en la grafica de pie debe ser String", this.fila, this.columna));
                    return null;
                }
            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Labels en la grafica de pie debe ser un vector", this.fila, this.columna));
            }
        } else {
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro X en la grafica de pie debe ser un vector", this.fila, this.columna));
        }

        return null;
    }

}
