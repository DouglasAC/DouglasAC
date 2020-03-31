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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author ddani
 */
public class Barras extends Funcion {

    public Barras(String identificador, LinkedList<Parametro> parametros, LinkedList<NodoAst> sentencias, int fila, int columna) {
        super(identificador, parametros, sentencias, fila, columna);
    }

    @Override
    public Object ejecutar(Entorno en) {
        Simbolo sim_h = en.getSimbolo("parametro&&h&&barras01210");
        Simbolo sim_xlab = en.getSimbolo("parametro&&xlab&&barras01210");
        Simbolo sim_ylab = en.getSimbolo("parametro&&ylab&&barras01210");
        Simbolo sim_main = en.getSimbolo("parametro&&main&&barras01210");
        Simbolo sim_names = en.getSimbolo("parametro&&names&&barras01210");

        Object val_h = sim_h.getValor();
        if (val_h instanceof Vector) {
            Object val_xl = sim_xlab.getValor();
            if (val_xl instanceof Vector) {
                Object val_yl = sim_ylab.getValor();
                if (val_yl instanceof Vector) {
                    Object val_m = sim_main.getValor();
                    if (val_m instanceof Vector) {
                        Object val_n = sim_names.getValor();
                        if (val_n instanceof Vector) {

                            LinkedList<Object> hval = new LinkedList<Object>();
                            for (Nodo ob : ((Vector) val_h).getValores()) {
                                if (ob.valor instanceof Integer || ob.valor instanceof Double) {
                                    hval.add(ob.valor);
                                } else {
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Los valores del vector H deben de ser Numericos", this.fila, this.columna));
                                    return null;
                                }
                            }

                            String xval = "";
                            Vector xvec = (Vector) val_xl;
                            if (xvec.valores.get(0).valor instanceof String) {
                                xval = (String) xvec.valores.get(0).valor;
                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Xlabs en la grafica de barras debe ser String", this.fila, this.columna));
                                return null;
                            }
                            String yval = "";
                            Vector yvec = (Vector) val_yl;
                            if (yvec.valores.get(0).valor instanceof String) {
                                yval = (String) yvec.valores.get(0).valor;
                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Ylabs en la grafica de barras debe ser String", this.fila, this.columna));
                                return null;
                            }
                            String mval = "";
                            Vector mvec = (Vector) val_m;
                            if (mvec.valores.get(0).valor instanceof String) {
                                mval = (String) mvec.valores.get(0).valor;
                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Main en la grafica de barras debe ser String", this.fila, this.columna));
                                return null;
                            }

                            LinkedList<String> nval = new LinkedList<>();
                            for (Nodo ob : ((Vector) val_n).getValores()) {
                                if (ob.valor instanceof String) {
                                    nval.add((String) ob.valor);
                                } else {
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Los valores del vector Names.arg deben de ser String", this.fila, this.columna));
                                    return null;
                                }
                            }

                            if (hval.size() > nval.size()) {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "La cantidad de labels es menor a la cantidad de valores", this.fila, this.columna));
                                int dif = hval.size() - nval.size();
                                for (int x = 0; x < dif; x++) {
                                    nval.add("Desconocido " + (x + 1));
                                }
                            }

                            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                            for (int x = 0; x < hval.size(); x++) {
                                Object val = hval.get(x);
                                if (val instanceof Integer) {
                                    dataset.setValue((int) val, nval.get(x), nval.get(x));
                                } else if (val instanceof Double) {
                                    dataset.setValue((double) val, nval.get(x), nval.get(x));
                                }
                            }
                            JFreeChart chart = ChartFactory.createBarChart(mval, xval, yval, dataset, PlotOrientation.VERTICAL, true, true, false);
                            ChartFrame frame = new ChartFrame(mval, chart);
                           
                            frame.pack();
                            frame.setVisible(true);
                        } else {
                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Names.arg en la grafica de barras debe ser String", this.fila, this.columna));
                            return null;
                        }
                    } else {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Main en la grafica de barras debe ser String", this.fila, this.columna));
                        return null;
                    }
                } else {
                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Ylab en la grafica de barras debe ser String", this.fila, this.columna));
                    return null;
                }
            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Xlab en la grafica de barras debe ser String", this.fila, this.columna));
                return null;
            }
        } else {
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro H en la grafica de barras debe ser un vector", this.fila, this.columna));
            return null;
        }

        return null;
    }

}
