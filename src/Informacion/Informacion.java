/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Informacion;

import Error.ErrorAr;
import Reportes.NodoTabla;
import java.util.LinkedList;
import javax.swing.JTextArea;

/**
 *
 * @author ddani
 */
public class Informacion {

    public static LinkedList<ErrorAr> errores;
    public static LinkedList<NodoTabla> simbolos;
    public static JTextArea consola;

    public static void setUp(JTextArea con) {
        errores = new LinkedList<>();
        simbolos = new LinkedList<>();
        consola = con;
    }

    public static void agregarError(ErrorAr error) {
        errores.add(error);
    }

    public static void ImprimeEnConsola(String mensaje) {
        consola.append(mensaje + "\n");
    }

    public static void ResetErrores() {
        errores = new LinkedList<>();
    }

    public static void agregarSimbolo(NodoTabla sim) {
        simbolos.add(sim);
    }

    public static void ResetSimbolos() {
        simbolos = new LinkedList<>();
    }
}
