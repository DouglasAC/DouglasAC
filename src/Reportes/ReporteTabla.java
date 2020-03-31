/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import Arit.Entorno.Funcion;
import Arit.Estructuras.Arreglo;
import Arit.Estructuras.Lista;
import Arit.Estructuras.Matris;
import Arit.Estructuras.Vector;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class ReporteTabla {

    LinkedList<NodoTabla> simbolos;

    public ReporteTabla(LinkedList<NodoTabla> simbolos) {
        this.simbolos = simbolos;
    }

    public void escribirReporte() {
        FileWriter fichero = null;
        PrintWriter sw = null;
        try {
            fichero = new FileWriter("ReporteTabla.html");
            sw = new PrintWriter(fichero);

            sw.println("<!doctype html>");
            sw.println("<html lang=\"en\">");
            sw.println("<head>");
            sw.println("<meta charset=\"utf - 8\">");
            sw.println("<meta name=\"viewport\" content=\"width = device - width, initial - scale = 1, shrink - to - fit = no\">");
            sw.println("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css\" integrity=\"sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO\" crossorigin=\"anonymous\">");
            sw.println("<title>Reporte de Tabla de Simbolos</title>");
            sw.println("</head>");
            sw.println("<body>");
            sw.println("<div class=\"container-fluid\">");
            sw.println("<div class=\"jumbotron\">");
            sw.println("<h1 class=\"display - 4\">Reporte de Tabla de Simbolos</h1>");
            sw.println("<p class=\"lead\"> 201503935 | Douglas Daniel Aguilar Cuque</p>");
            Date localDate = new Date();
            DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            sw.println("<p class=\"lead\">" + hourdateFormat.format(localDate) + "</p>");
            sw.println("</div>");
            sw.println("<br>");

            sw.println("<br>");
            sw.println("<table class=\"table table-bordered\" >");
            sw.println("<thead class=\"thead-dark\">");
            sw.println("<tr>");
            sw.println("<th scope=\"col\">#</th>");
            sw.println("<th scope=\"col\">Nombre</th>");
            sw.println("<th scope=\"col\">Entorno</th>");
            sw.println("<th scope=\"col\">Rol</th>");
            sw.println("<th scope=\"col\">Tipo</th>");
            sw.println("<th scope=\"col\">Dimensiones</th>");
            sw.println("<th scope=\"col\"># Parametros</th>");
            sw.println("<th scope=\"col\">Tamaño</th>");
            sw.println("</tr>");
            sw.println("</thead>");
            sw.println("<tbody>");
            int x = 1;
            Object val = null;
            for (NodoTabla e : simbolos) {
                sw.println("<tr class=\"bg-success text-white\">");
                sw.println("<th scope=\"row\">" + x + "</td>");
                sw.println("<td>" + e.sim.identificador + "</td>");
                sw.println("<td>" + e.entorno + "</td>");
                sw.println("<td>" + e.rol + "</td>");
                val = e.sim.valor;
                if (val instanceof Vector) {
                    Vector vec = (Vector) val;
                    sw.println("<td>Vector</td>");
                    sw.println("<td>1</td>");
                    sw.println("<td>-</td>");
                    sw.println("<td>" + vec.tamaño() + "</td>");
                } else if (val instanceof Matris) {
                    Matris mat = (Matris) val;
                    sw.println("<td>Matris</td>");
                    sw.println("<td>2</td>");
                    sw.println("<td>-</td>");
                    sw.println("<td>" + mat.getColumna() * mat.getFila() + "</td>");
                } else if (val instanceof Lista) {
                    Lista l = (Lista) val;
                    sw.println("<td>Lista</td>");
                    sw.println("<td>1</td>");
                    sw.println("<td>-</td>");
                    sw.println("<td>" + l.tamaño() + "</td>");
                } else if (val instanceof Arreglo) {
                    Arreglo ar = (Arreglo) val;
                    sw.println("<td>Lista</td>");
                    sw.println("<td>1</td>");
                    sw.println("<td>-</td>");
                    sw.println("<td>" + ar.getTamaño() + "</td>");
                } else if (val instanceof Funcion) {
                    Funcion f = (Funcion) val;
                    sw.println("<td>Funcion</td>");
                    sw.println("<td>-</td>");
                    sw.println("<td>" + f.getParametros().size() + "</td>");
                    sw.println("<td>-</td>");
                }

                sw.println("</tr>");
                x++;
            }
            sw.println("</tbody>");
            sw.println("</table>");
            sw.println("<br>");

            sw.println("</div>");
            sw.println("<script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\" integrity=\"sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo\" crossorigin=\"anonymous\"></script>");
            sw.println("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js\" integrity=\"sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49\" crossorigin=\"anonymous\"></script>");
            sw.println("<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js\" integrity=\"sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy\" crossorigin=\"anonymous\"></script>");
            sw.println("</body>");
            sw.println("</html>");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
