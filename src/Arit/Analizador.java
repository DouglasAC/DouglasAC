/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit;

import Arit.AltaAbstraccion.Expresion;
import Arit.AltaAbstraccion.Instruccion;
import Arit.AltaAbstraccion.NodoAst;
import Arit.AnalizadorJCC.GramaticaJCC;
import Arit.Entorno.Entorno;
import Arit.Entorno.Funcion;
import Arit.Entorno.Parametro;
import Arit.Entorno.Simbolo;
import Arit.OperacionersPrimitivas.Array;
import Arit.OperacionersPrimitivas.FuncionC;
import Arit.OperacionersPrimitivas.Graficas.Barras;
import Arit.OperacionersPrimitivas.Graficas.Histograma;
import Arit.OperacionersPrimitivas.Graficas.Lineas;
import Arit.OperacionersPrimitivas.Graficas.Pie;
import Arit.OperacionersPrimitivas.Length;
import Arit.OperacionersPrimitivas.List;
import Arit.OperacionersPrimitivas.Matrix;
import Arit.OperacionersPrimitivas.Media;
import Arit.OperacionersPrimitivas.Mediana;
import Arit.OperacionersPrimitivas.Moda;
import Arit.OperacionersPrimitivas.Ncol;
import Arit.OperacionersPrimitivas.Nrow;
import Arit.OperacionersPrimitivas.Remove;
import Arit.OperacionersPrimitivas.Round;
import Arit.OperacionersPrimitivas.StringLength;
import Arit.OperacionersPrimitivas.ToLowerCase;
import Arit.OperacionersPrimitivas.ToUpperCase;
import Arit.OperacionersPrimitivas.Trunk;
import Arit.OperacionersPrimitivas.TypeOf;
import Error.ErrorAr;
import Informacion.Informacion;
import Reportes.NodoTabla;
import Reportes.ReporteAst;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class Analizador {

    String texto;

    public Analizador(String texto) {
        this.texto = texto;

    }

    public void Analizar() {
        try {
            Arit.AnalizadorFlCu.SintacticoArit pars;
            pars = new Arit.AnalizadorFlCu.SintacticoArit(new Arit.AnalizadorFlCu.LexicoArit(new StringReader(texto)));
            pars.parse();
            LinkedList<NodoAst> AST = pars.getAST();
            Ejecutar(AST);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void AnalizarJCC() {
        try {
            GramaticaJCC gram = new GramaticaJCC(new BufferedReader(new StringReader(texto)));
            LinkedList<NodoAst> AST = gram.Inicio();
            Ejecutar(AST);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void Ejecutar(LinkedList<NodoAst> sentencias) {
        try {
            Informacion.ResetErrores();
            Informacion.ResetSimbolos();
            LinkedList<NodoAst> AST = sentencias;
            Entorno en = new Entorno(null);
            en.SetNombre("global");
            ponerFuncionesPrimitivas(en);
            for (NodoAst ins : AST) {
                System.out.println(ins);
                if (ins != null) {
                    if (ins instanceof Funcion) {
                        Funcion fun = (Funcion) ins;
                        if (!en.existeFuncion(fun.identificador)) {
                            en.agregarFuncion(fun.identificador, fun);
                            Simbolo nuevo = new Simbolo(fun.identificador, fun);
                            Informacion.agregarSimbolo(new NodoTabla(nuevo, "Global", "Funcion"));
                        } else {
                            Informacion.agregarError(new ErrorAr("Semantico", "Ya existe una funcion con el nombre: " + fun.identificador, fun.fila, fun.columna));
                        }
                    }

                }
            }

            for (NodoAst ins : AST) {
                System.out.println(ins);
                if (ins != null) {
                    if (ins instanceof Funcion) {

                    } else if (ins instanceof Instruccion) {
                        try {

                            ((Instruccion) ins).ejecutar(en);

                        } catch (Exception e) {
                            System.out.println("Error2 o " + e);
                            Informacion.agregarError(new ErrorAr("Semantico", "Error en ejecutar una instruccion global: ", ins.fila, ins.columna));
                        }
                    } else if (ins instanceof Expresion) {
                        try {

                            Expresion in = (Expresion) ins;
                            Object result = in.getValorImplicito(en);

                        } catch (Exception e) {
                            System.out.println("Error2 o " + e);
                            Informacion.agregarError(new ErrorAr("Semantico", "Error en ejecutar una expresion global: ", ins.fila, ins.columna));
                        }

                    }

                }
            }
            ReporteAst rep = new ReporteAst(sentencias);
            rep.GenerarReporte();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    public void ponerFuncionesPrimitivas(Entorno en) {
        //-- funcion list
        List fun_list = new List("list", new LinkedList<>(), new LinkedList<>(), 0, 0);
        en.agregarFuncion("list", fun_list);
        /// ------- funcion matrix
        LinkedList<Parametro> parametrosMatrix = new LinkedList<>();
        Parametro par_data = new Parametro("parametro&&data&&matris01210", null);
        Parametro par_row = new Parametro("parametro&&fila&&matris01210", null);
        Parametro par_col = new Parametro("parametro&&columna&&matris01210", null);
        parametrosMatrix.add(par_data);
        parametrosMatrix.add(par_row);
        parametrosMatrix.add(par_col);
        Matrix fun_matrix = new Matrix("matrix", parametrosMatrix, new LinkedList<>(), 0, 0);
        en.agregarFuncion("matrix", fun_matrix);
        /// ----------- funcion array
        LinkedList<Parametro> parametrosArray = new LinkedList<>();
        Parametro array_data = new Parametro("parametro&&data&&arreglo01210", null);
        Parametro array_vec = new Parametro("parametro&&vector&&arreglo01210", null);
        parametrosArray.add(array_data);
        parametrosArray.add(array_vec);
        Array fun_array = new Array("array", parametrosArray, new LinkedList<>(), 0, 0);
        en.agregarFuncion("array", fun_array);
        /// ---------- funcion pie
        LinkedList<Parametro> parametrosPie = new LinkedList<>();
        Parametro Pie_x = new Parametro("parametro&&x&&matris01210", null);
        Parametro Pie_l = new Parametro("parametro&&labels&&matris01210", null);
        Parametro Pie_m = new Parametro("parametro&&main&&matris01210", null);
        parametrosPie.add(Pie_x);
        parametrosPie.add(Pie_l);
        parametrosPie.add(Pie_m);
        Pie fun_Pie = new Pie("pie", parametrosPie, new LinkedList<>(), 0, 0);
        en.agregarFuncion("pie", fun_Pie);
        //-------- funcion barplot
        LinkedList<Parametro> parametrosBar = new LinkedList<>();
        Parametro bar_h = new Parametro("parametro&&h&&barras01210", null);
        Parametro bar_xl = new Parametro("parametro&&xlab&&barras01210", null);
        Parametro bar_yl = new Parametro("parametro&&ylab&&barras01210", null);
        Parametro bar_m = new Parametro("parametro&&main&&barras01210", null);
        Parametro bar_n = new Parametro("parametro&&names&&barras01210", null);

        parametrosBar.add(bar_h);
        parametrosBar.add(bar_xl);
        parametrosBar.add(bar_yl);
        parametrosBar.add(bar_m);
        parametrosBar.add(bar_n);

        Barras fun_bar = new Barras("barplot", parametrosBar, new LinkedList<>(), 0, 0);
        en.agregarFuncion("barplot", fun_bar);
        //----------- funcion plot linea

        LinkedList<Parametro> parametrosLinea = new LinkedList<>();
        Parametro l_v = new Parametro("parametro&&v&&linea01210", null);
        Parametro l_t = new Parametro("parametro&&type&&linea01210", null);
        Parametro l_xl = new Parametro("parametro&&xlab&&linea01210", null);
        Parametro l_yl = new Parametro("parametro&&ylab&&liena01210", null);
        Parametro l_m = new Parametro("parametro&&main&&linea01210", null);

        parametrosLinea.add(l_v);
        parametrosLinea.add(l_t);
        parametrosLinea.add(l_xl);
        parametrosLinea.add(l_yl);
        parametrosLinea.add(l_m);

        Lineas fun_line = new Lineas("plot", parametrosLinea, new LinkedList<>(), 0, 0);
        en.agregarFuncion("plot", fun_line);
        ///----------- Funcion Histograma
        LinkedList<Parametro> parametrosHis = new LinkedList<Parametro>();
        Parametro His_v = new Parametro("parametro&&v&&histo01210", null);
        Parametro His_xl = new Parametro("parametro&&xlabels&&histo01210", null);
        Parametro His_m = new Parametro("parametro&&main&&histo01210", null);
        parametrosHis.add(His_v);
        parametrosHis.add(His_xl);
        parametrosHis.add(His_m);
        Histograma fun_His = new Histograma("hist", parametrosHis, new LinkedList<>(), 0, 0);
        en.agregarFuncion("hist", fun_His);

        //------------- funcion C
        FuncionC fun_c = new FuncionC("c", new LinkedList<>(), new LinkedList<>(), 0, 0);
        en.agregarFuncion("c", fun_c);

        ///------------ funcion typeof
        LinkedList<Parametro> parametrosType = new LinkedList<>();
        Parametro type_val = new Parametro("parametro&&expresion&&typeof01210", null);
        parametrosType.add(type_val);
        TypeOf fun_type = new TypeOf("typeof", parametrosType, new LinkedList<>(), 0, 0);
        en.agregarFuncion("typeof", fun_type);
        ///------------ funcion length
        LinkedList<Parametro> parametroslength = new LinkedList<>();
        Parametro length_val = new Parametro("parametro&&expresion&&length01210", null);
        parametroslength.add(length_val);
        Length fun_length = new Length("length", parametroslength, new LinkedList<>(), 0, 0);
        en.agregarFuncion("length", fun_length);
        ///------------ funcion nCol
        LinkedList<Parametro> parametrosnCol = new LinkedList<>();
        Parametro length_nCol = new Parametro("parametro&&expresion&&ncol01210", null);
        parametrosnCol.add(length_nCol);
        Ncol fun_nCol = new Ncol("ncol", parametrosnCol, new LinkedList<>(), 0, 0);
        en.agregarFuncion("ncol", fun_nCol);
        ///------------ funcion nRow
        LinkedList<Parametro> parametrosnRow = new LinkedList<>();
        Parametro nRow_val = new Parametro("parametro&&expresion&&nrow01210", null);
        parametrosnRow.add(nRow_val);
        Nrow fun_nRow = new Nrow("nrow", parametrosnRow, new LinkedList<>(), 0, 0);
        en.agregarFuncion("nrow", fun_nRow);
        ///------------ funcion stringLength
        LinkedList<Parametro> parametrosStringLength = new LinkedList<>();
        Parametro StringLength_val = new Parametro("parametro&&expresion&&stringlength01210", null);
        parametrosStringLength.add(StringLength_val);
        StringLength fun_StringLength = new StringLength("stringlength", parametrosStringLength, new LinkedList<>(), 0, 0);
        en.agregarFuncion("stringlength", fun_StringLength);
        ///------------ funcion ToLowerCase
        LinkedList<Parametro> parametrosToLower = new LinkedList<>();
        Parametro ToLower_nCol = new Parametro("parametro&&expresion&&tolowercase01210", null);
        parametrosToLower.add(ToLower_nCol);
        ToLowerCase fun_ToLower = new ToLowerCase("tolowercase", parametrosToLower, new LinkedList<>(), 0, 0);
        en.agregarFuncion("tolowercase", fun_ToLower);
        ///------------ funcion ToUpperCase
        LinkedList<Parametro> parametrosToUpper = new LinkedList<>();
        Parametro ToUpper_val = new Parametro("parametro&&expresion&&touppercase01210", null);
        parametrosToUpper.add(ToUpper_val);
        ToUpperCase fun_ToUpper = new ToUpperCase("touppercase", parametrosToUpper, new LinkedList<>(), 0, 0);
        en.agregarFuncion("touppercase", fun_ToUpper);
        ///------------ funcion Trunk
        LinkedList<Parametro> parametrosTrunk = new LinkedList<>();
        Parametro Trunk_val = new Parametro("parametro&&expresion&&trunk01210", null);
        parametrosTrunk.add(Trunk_val);
        Trunk fun_Trunk = new Trunk("trunk", parametrosTrunk, new LinkedList<>(), 0, 0);
        en.agregarFuncion("trunk", fun_Trunk);
        ///------------ funcion Round
        LinkedList<Parametro> parametrosRound = new LinkedList<>();
        Parametro Round_val = new Parametro("parametro&&expresion&&round01210", null);
        parametrosRound.add(Round_val);
        Round fun_Round = new Round("round", parametrosRound, new LinkedList<>(), 0, 0);
        en.agregarFuncion("round", fun_Round);
        //------------- funcion media
        Media fun_media = new Media("mean", new LinkedList<>(), new LinkedList<>(), 0, 0);
        en.agregarFuncion("mean", fun_media);
        //------------- funcion mediana
        Mediana fun_mediana = new Mediana("median", new LinkedList<>(), new LinkedList<>(), 0, 0);
        en.agregarFuncion("median", fun_mediana);
        //------------- funcion moda
        Moda fun_moda = new Moda("mode", new LinkedList<>(), new LinkedList<>(), 0, 0);
        en.agregarFuncion("mode", fun_moda);
        //------------- funcion Remove
        LinkedList<Parametro> parametrosRemove = new LinkedList<Parametro>();
        Parametro Remove_v = new Parametro("parametro&&original&&remove01210", null);
        Parametro Remove_xl = new Parametro("parametro&&remove&&remove01210", null);

        parametrosRemove.add(Remove_v);
        parametrosRemove.add(Remove_xl);

        Remove fun_Remove = new Remove("remove", parametrosRemove, new LinkedList<>(), 0, 0);
        en.agregarFuncion("remove", fun_Remove);
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

}
