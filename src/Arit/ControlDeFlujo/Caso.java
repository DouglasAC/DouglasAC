/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.ControlDeFlujo;

import Arit.AltaAbstraccion.Expresion;
import Arit.AltaAbstraccion.NodoAst;
import java.util.LinkedList;

/**
 *
 * @author ddani
 */
public class Caso {
    
    Expresion expresion;
    LinkedList<NodoAst> sentencias;

    public Caso(Expresion expresion, LinkedList<NodoAst> sentencias) {
        this.expresion = expresion;
        this.sentencias = sentencias;
    }

    public Expresion getExpresion() {
        return expresion;
    }

    public void setExpresion(Expresion expresxion) {
        this.expresion = expresxion;
    }

    public LinkedList<NodoAst> getSentencias() {
        return sentencias;
    }

    public void setSentencias(LinkedList<NodoAst> sentencias) {
        this.sentencias = sentencias;
    }
    
}
