/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import Arit.AltaAbstraccion.NodoAst;
import Error.ErrorAr;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ddani
 */
public class ReporteAst {

    LinkedList<NodoAst> sentencias;

    public ReporteAst(LinkedList<NodoAst> sentencias) {
        this.sentencias = sentencias;
    }

    public void GenerarTxt() {
        FileWriter fichero = null;
        PrintWriter sw = null;
        try {
            fichero = new FileWriter("cuerpo.txt");
            sw = new PrintWriter(fichero);

            sw.println("digraph G {");
            sw.println("node[shape=box, style=filled, color=Gray95];");
            sw.println("edge[color=lightblue];\n rankdir=UD;");
            String ast = "node0";
            sw.println(ast + "[label=\"Ast\"];");

            String sens = "node1";
            sw.println(sens + "[label=\"Sentencias\"];");
            sw.println(ast + " -> " + sens + ";");
            int numero = 2;
            for (NodoAst ins : sentencias) {
                NodoDot nodo = ins.generarDot(numero);
                numero = nodo.Numero;
                sw.println(nodo.Cuerpo);
                sw.println(sens + " -> " + nodo.Nombre+";");
            }

            sw.println("}");

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

    public void GenerarJpg() {
        try {
            String[] cmd = new String[5];
            cmd[0] = "dot.exe";
            cmd[1] = "-Tjpg";
            cmd[2] = "cuerpo.txt";
            cmd[3] = "-o";
            cmd[4] = "ast.jpg";
            Runtime rt = Runtime.getRuntime();
            rt.exec(cmd);
        } catch (IOException ex) {
            System.out.println("No se genero la imagen");
            Logger.getLogger(ReporteAst.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void GenerarReporte() {
        GenerarTxt();
        GenerarJpg();
        FileWriter fichero = null;
        PrintWriter sw = null;
        try {
            fichero = new FileWriter("ReporteAst.html");
            sw = new PrintWriter(fichero);

            sw.println("<!doctype html>");
            sw.println("<html lang=\"en\">");
            sw.println("<head>");
            sw.println("<meta charset=\"utf - 8\">");
            sw.println("<meta name=\"viewport\" content=\"width = device - width, initial - scale = 1, shrink - to - fit = no\">");
            sw.println("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css\" integrity=\"sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO\" crossorigin=\"anonymous\">");
            sw.println("<title>Reporte de Ast</title>");
            sw.println("</head>");
            sw.println("<body>");
            sw.println("<div class=\"container-fluid\">");
            sw.println("<div class=\"jumbotron\">");
            sw.println("<h1 class=\"display - 4\">Reporte de Ast</h1>");
            sw.println("<p class=\"lead\"> 201503935 | Douglas Daniel Aguilar Cuque</p>");
            Date localDate = new Date();
            DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            sw.println("<p class=\"lead\">" + hourdateFormat.format(localDate) + "</p>");
            sw.println("</div>");
            sw.println("<br>");
            sw.println("<h1> Ast: </h1>");
            sw.println("<br>");
            sw.print("<img src=\"ast.jpg\">");
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
