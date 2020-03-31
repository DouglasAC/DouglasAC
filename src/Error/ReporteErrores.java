/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Error;

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
public class ReporteErrores {

    LinkedList<ErrorAr> errores;

    public ReporteErrores() {
        this.errores = new LinkedList<>();
    }

    public ReporteErrores(LinkedList<ErrorAr> errores) {
        this.errores = errores;
    }

    public void AgregarError(String tipo, String descripcion, int fila, int columna) {
        this.errores.add(new ErrorAr(tipo, descripcion, fila, columna));
    }

    public void GenearReporte() {
        FileWriter fichero = null;
        PrintWriter sw = null;
        try {
            fichero = new FileWriter("ReporteErrores.html");
            sw = new PrintWriter(fichero);

            sw.println("<!doctype html>");
            sw.println("<html lang=\"en\">");
            sw.println("<head>");
            sw.println("<meta charset=\"utf - 8\">");
            sw.println("<meta name=\"viewport\" content=\"width = device - width, initial - scale = 1, shrink - to - fit = no\">");
            sw.println("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css\" integrity=\"sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO\" crossorigin=\"anonymous\">");
            sw.println("<title>Reporte de Errores</title>");
            sw.println("</head>");
            sw.println("<body>");
            sw.println("<div class=\"container-fluid\">");
            sw.println("<div class=\"jumbotron\">");
            sw.println("<h1 class=\"display - 4\">Reporte de Errores</h1>");
            sw.println("<p class=\"lead\"> 201503935 | Douglas Daniel Aguilar Cuque</p>");
            Date localDate = new Date();
            DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            sw.println("<p class=\"lead\">" + hourdateFormat.format(localDate) + "</p>");
            sw.println("</div>");
            sw.println("<br>");
            sw.println("<h1> Errores Encontrados: </h1>");
            sw.println("<br>");
            sw.println("<table class=\"table table-bordered\" >");
            sw.println("<thead class=\"thead-dark\">");
            sw.println("<tr>");
            sw.println("<th scope=\"col\">#</th>");
            sw.println("<th scope=\"col\">Tipo</th>");
            sw.println("<th scope=\"col\">Descripcion</th>");
            sw.println("<th scope=\"col\">Linea</th>");
            sw.println("<th scope=\"col\">Columna</th>");
            sw.println("</tr>");
            sw.println("</thead>");
            sw.println("<tbody>");
            int x = 1;
            for (ErrorAr e : errores) {
                sw.println("<tr class=\"bg-danger text-white\">");
                sw.println("<th scope=\"row\">" + x + "</td>");
                sw.println("<td>" + e.tipo + "</td>");
                sw.println("<td>" + e.descripcion + "</td>");
                sw.println("<td>" + e.fila + "</td>");
                sw.println("<td>" + e.columna + "</td>");
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
