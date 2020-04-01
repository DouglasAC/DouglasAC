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
import Arit.Estructuras.Matris;
import Arit.Estructuras.Nodo;
import Arit.Estructuras.Vector;
import Error.ErrorAr;
import java.util.ArrayList;
import java.util.LinkedList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author ddani
 */
public class Lineas extends Funcion {

    public Lineas(String identificador, LinkedList<Parametro> parametros, LinkedList<NodoAst> sentencias, int fila, int columna) {
        super(identificador, parametros, sentencias, fila, columna);
    }

    @Override
    public Object ejecutar(Entorno en) {
        Simbolo sim_v = en.getSimbolo("parametro&&v&&linea01210");
        Simbolo sim_type = en.getSimbolo("parametro&&type&&linea01210");
        Simbolo sim_xlab = en.getSimbolo("parametro&&xlab&&linea01210");
        Simbolo sim_ylab = en.getSimbolo("parametro&&ylab&&liena01210");
        Simbolo sim_main = en.getSimbolo("parametro&&main&&linea01210");

        Object val_v = sim_v.getValor();
        if (val_v instanceof Vector || val_v instanceof Matris) {
            Object val_t = sim_type.getValor();
            if (val_t instanceof Vector) {
                Object val_x = sim_xlab.getValor();
                if (val_x instanceof Vector) {
                    Object val_y = sim_ylab.getValor();
                    if (val_y instanceof Vector) {
                        Object val_m = sim_main.getValor();
                        if (val_m instanceof Vector) {

                            String mval = "";
                            Vector mvec = (Vector) val_m;
                            boolean esInt = false;
                            Object ylim = 0;
                            if (mvec.valores.get(0).valor instanceof String) {
                                mval = (String) mvec.valores.get(0).valor;
                            } else if (mvec.valores.get(0).valor instanceof Integer) {
                                ylim = (int) mvec.valores.get(0).valor;
                                esInt = true;
                            } else if (mvec.valores.get(0).valor instanceof Double) {
                                ylim = (Double) mvec.valores.get(0).valor;
                                esInt = true;
                            } else {
                                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Main en la grafica de lineas debe ser String o del grafica de dispercion debe ser Integer", this.fila, this.columna));
                                return null;

                            }

                            ArrayList<Nodo> valores = new ArrayList<>();
                            if (val_v instanceof Vector) {
                                Vector vec = (Vector) val_v;
                                valores = vec.valores;
                            } else if (val_v instanceof Matris) {
                                Matris mat = (Matris) val_v;

                                for (int x = 0; x < mat.getColumna(); x++) {
                                    for (int y = 0; y < mat.getFila(); y++) {
                                        valores.add(mat.valores[y][x]);
                                    }
                                }
                            }
                            String tval = "";
                            Vector tvec = (Vector) val_t;
                            if (tvec.valores.get(0).valor instanceof String) {
                                tval = (String) tvec.valores.get(0).valor;
                            } else {
                                if (!esInt) {
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Type en la grafica de lineas debe ser String", this.fila, this.columna));
                                } else {
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Xlab en la grafica de dispercion debe ser String", this.fila, this.columna));
                                }
                                return null;
                            }

                            String xval = "";
                            Vector xvec = (Vector) val_x;
                            if (xvec.valores.get(0).valor instanceof String) {
                                xval = (String) xvec.valores.get(0).valor;
                            } else {
                                if (!esInt) {
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Xlab en la grafica de lineas  debe ser String", this.fila, this.columna));
                                } else {
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Ylab en la grafica de dispercion debe ser String", this.fila, this.columna));
                                }
                                return null;

                            }

                            String yval = "";
                            Vector yvec = (Vector) val_y;

                            if (yvec.valores.get(0).valor instanceof String) {
                                yval = (String) yvec.valores.get(0).valor;
                            } else {
                                if (!esInt) {
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Ylab en la grafica de lineas  debe ser String", this.fila, this.columna));
                                } else {
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Main en la grafica de dispercion debe ser String", this.fila, this.columna));
                                }
                                return null;

                            }
                            if (!esInt) {
                                XYSeries datos = new XYSeries("Datos");
                                for (int x = 0; x < valores.size(); x++) {
                                    Object val = valores.get(x).valor;
                                    if (val instanceof Integer) {
                                        datos.add((x + 1), (int) val);
                                    } else if (val instanceof Double) {
                                        datos.add((x + 1), (double) val);
                                    } else {
                                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Los valores de la grafica deben ser numericos", this.fila, this.columna));
                                        return null;
                                    }
                                }

                                XYSeriesCollection coleccion = new XYSeriesCollection();
                                coleccion.addSeries(datos);

                                JFreeChart chart = ChartFactory.createXYLineChart(mval, xval, yval, coleccion, PlotOrientation.VERTICAL, true, true, false);
                                XYPlot xyplot = (XYPlot) chart.getPlot();

                                XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
                                xylineandshaperenderer.setBaseShapesVisible(true);
                                XYItemLabelGenerator xy = new StandardXYItemLabelGenerator();
                                xylineandshaperenderer.setBaseItemLabelGenerator(xy);
                                xylineandshaperenderer.setBaseItemLabelsVisible(true);
                                if (tval.equalsIgnoreCase("i")) {
                                    xylineandshaperenderer.setBaseShapesVisible(false);
                                } else if (tval.equalsIgnoreCase("p")) {
                                    xylineandshaperenderer.setBaseLinesVisible(false);
                                } else if (tval.equalsIgnoreCase("o")) {
                                    xylineandshaperenderer.setBaseLinesVisible(true);
                                } else {
                                    xylineandshaperenderer.setBaseLinesVisible(true);
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El valor Type no corresponde con ninguno de los esperados", this.fila, this.columna));
                                }
                                ChartFrame frame = new ChartFrame(mval, chart);

                                frame.pack();
                                frame.setVisible(true);
                            } else {
                                Object maximo = null;
                                Object minimo = null;
                                if (mvec.valores.size() > 1) {
                                    minimo = mvec.valores.get(0);
                                    maximo = mvec.valores.get(1);
                                } else {
                                    if (mvec.valores.size() == 1) {
                                        minimo = mvec.valores.get(0);
                                    }
                                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "LE faltan valores en el vector de minimo y maximo", this.fila, this.columna));
                                }
                                double max = 0.0;
                                boolean noMax = false;
                                if (maximo instanceof Integer) {
                                    max = (double) ((int) maximo);
                                } else if (maximo instanceof Double) {
                                    max = (double) maximo;
                                } else {
                                    noMax = true;
                                }
                                double min = 0.0;
                                boolean noMin = false;
                                if (minimo instanceof Integer) {
                                    min = (double) ((int) minimo);
                                } else if (maximo instanceof Double) {
                                    min = (double) minimo;
                                } else {
                                    noMin = true;
                                }

                                if (!noMin && !noMax) {
                                    if (min > max) {
                                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El valor minimo = " + min + " es mayor que el maximo = " + max, this.fila, this.columna));
                                        noMin = true;
                                    }
                                    if (max < min) {
                                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El valor maximo = " + max + " es menor que el minimo = " + min, this.fila, this.columna));
                                        noMax = true;
                                    }
                                }

                                XYSeries datos = new XYSeries("Datos");
                                for (int x = 0; x < valores.size(); x++) {
                                    Object val = valores.get(x).valor;
                                    double val_num = 0.0;
                                    if (val instanceof Integer) {
                                        val_num = (double) ((int) val);
                                    } else if (val instanceof Double) {
                                        val_num = (double) val;
                                    } else {
                                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "Los valores de la grafica deben ser numericos", this.fila, this.columna));
                                        return null;
                                    }
                                    if (!noMin && !noMax) {
                                        if (max >= val_num && val_num >= min) {
                                            datos.add((x + 1), val_num);
                                        } else {
                                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El valor " + val_num + " en la posicion: " + (x + 1) + " de la estructura no esta dentro del rango minimo = " + min + " maximo = " + max, this.fila, this.columna));
                                        }
                                    } else if (noMin && !noMax) {
                                        if (val_num >= min) {
                                            datos.add((x + 1), val_num);
                                        } else {
                                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El valor " + val_num + " en la posicion: " + (x + 1) + " de la estructura no esta dentro del rango minimo = " + min + " maximo = " + max, this.fila, this.columna));
                                        }
                                    } else if (!noMin && noMax) {
                                        if (val_num <= max) {
                                            datos.add((x + 1), val_num);
                                        } else {
                                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El valor " + val_num + " en la posicion: " + (x + 1) + " de la estructura no esta dentro del rango minimo = " + min + " maximo = " + max, this.fila, this.columna));
                                        }
                                    } else {
                                        datos.add((x + 1), val_num);
                                    }
                                }

                                XYSeriesCollection coleccion = new XYSeriesCollection();
                                coleccion.addSeries(datos);

                                JFreeChart chart = ChartFactory.createXYLineChart(yval, tval, xval, coleccion, PlotOrientation.VERTICAL, true, true, false);
                                XYPlot xyplot = (XYPlot) chart.getPlot();

                                XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
                                xylineandshaperenderer.setBaseShapesVisible(true);
                                XYItemLabelGenerator xy = new StandardXYItemLabelGenerator();
                                xylineandshaperenderer.setBaseItemLabelGenerator(xy);
                                xylineandshaperenderer.setBaseItemLabelsVisible(true);

                                xylineandshaperenderer.setBaseLinesVisible(false);

                                ChartFrame frame = new ChartFrame(yval, chart);

                                frame.pack();
                                frame.setVisible(true);

                            }
                        } else {
                            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Main en la grafica de barras debe ser String", this.fila, this.columna));
                        }
                    } else {
                        Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Ylab en la grafica de barras debe ser String", this.fila, this.columna));
                    }
                } else {
                    Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Xlab en la grafica de barras debe ser String", this.fila, this.columna));
                }
            } else {
                Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro Type en la grafica de barras debe ser String", this.fila, this.columna));
            }
        } else {
            Informacion.Informacion.agregarError(new ErrorAr("Semantico", "El parametro V en la grafica de barras debe ser un vector o matris", this.fila, this.columna));
        }

        return null;
    }
}
