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
import Arit.Estructuras.Vector;
import Error.ErrorAr;
import java.util.LinkedList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

/**
 *
 * @author ddani
 */
public class Histograma extends Funcion {

    public Histograma(String identificador, LinkedList<Parametro> parametros, LinkedList<NodoAst> sentencias, int fila, int columna) {
        super(identificador, parametros, sentencias, fila, columna);
    }

    @Override
    public Object ejecutar(Entorno en) {
        Simbolo sim_v = en.getSimbolo("parametro&&v&&histo01210");
        Simbolo sim_xlab = en.getSimbolo("parametro&&xlabels&&histo01210");
        Simbolo sim_main = en.getSimbolo("parametro&&main&&histo01210");

        Object val_v = sim_v.getValor();
        if (val_v instanceof Vector) {
            Object val_xlabs = sim_xlab.getValor();
            if (val_xlabs instanceof Vector) {
                Object val_main = sim_main.getValor();
                if (val_main instanceof Vector) {
                    Vector vec = ((Vector) val_v);
                    double vval[] = new double[vec.getValores().size()];

                    for (int x = 0; x < vec.getValores().size(); x++) {
                        Object val = vec.valores.get(x).valor;
                        if (val instanceof Integer) {
                            vval[x] = (double) ((int) val);
                        } else if (val instanceof Double) {
                            vval[x] = (double) val;
                        } else {
                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Los valores del vector V deben de ser Numericos", this.fila, this.columna));
                            return null;
                        }
                    }
                    String xval = "";
                    Vector xvec = (Vector) val_xlabs;
                    if (xvec.valores.get(0).valor instanceof String) {
                        xval = (String) xvec.valores.get(0).valor;
                    } else {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Main en la grafica de pie debe ser String", this.fila, this.columna));
                        return null;
                    }
                    String mval = "";
                    Vector mvec = (Vector) val_main;
                    if (mvec.valores.get(0).valor instanceof String) {
                        mval = (String) mvec.valores.get(0).valor;
                    } else {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Main en la grafica de pie debe ser String", this.fila, this.columna));
                        return null;
                    }

                    HistogramDataset dataset = new HistogramDataset();
                    ///dataset.setType(HistogramType.SCALE_AREA_TO_1);
                    double val_mayor = 0;
                    for (int x = 0; x < vec.valores.size(); x++) {
                        if (vval[x] > val_mayor) {
                            val_mayor = vval[x];
                        }
                    }

                    double val_menor = val_mayor;
                    for (int x = 0; x < vec.valores.size(); x++) {
                        if (vval[x] < val_menor) {
                            val_menor = vval[x];
                        }
                    }

                    double k = Math.sqrt(vec.valores.size());
                    double rango = val_mayor - val_menor;
                    double frec = rango / k;
                    
                    System.out.println((int)(frec+0.5));
                    
                    dataset.addSeries(xval, vval,(int)(k+0.5));
                    

                    JFreeChart chart = ChartFactory.createHistogram(mval, xval, null, dataset, PlotOrientation.VERTICAL, true, true, false);
                    ChartFrame frame = new ChartFrame(mval, chart);
                    frame.pack();
                    frame.setVisible(true);

                } else {
                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Main en el Histogramas debe ser String", this.fila, this.columna));
                    return null;
                }
            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Xlab en els histograma debe ser un String", this.fila, this.columna));
            }
        } else {
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro V en el histograma debe ser un vector", this.fila, this.columna));
        }

        return null;
    }

}
