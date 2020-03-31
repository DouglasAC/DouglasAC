/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.ValorImplicito;

import Arit.AltaAbstraccion.Expresion;
import Arit.Entorno.Entorno;
import Arit.Estructuras.Matris;
import Arit.Estructuras.Nodo;
import Arit.Estructuras.Vector;
import Error.ErrorAr;
import Informacion.Informacion;
import Reportes.NodoDot;

/**
 *
 * @author ddani
 */
public class Operacion extends Expresion {

    Expresion operando1;
    Expresion operando2;
    Expresion operandoU;
    Operador operador;

    public Operacion(Expresion operando1, Expresion operando2, Operador operador, int fila, int columna) {
        super(fila, columna);
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.operador = operador;
    }

    public Operacion(Expresion operandoU, Operador operador, int fila, int columna) {
        super(fila, columna);
        this.operandoU = operandoU;
        this.operador = operador;
    }

    @Override
    public NodoDot generarDot(int numero) {
        if (operandoU == null) {
            NodoDot op1 = this.operando1.generarDot(numero);
            NodoDot op2 = this.operando2.generarDot(op1.Numero);
            String nodo = "node" + op2.Numero;
            String cuerpo = nodo + "[label=\"Operacion, Operador: " + this.operador + "\"];\n";
            cuerpo += nodo + "->" + op1.Nombre + ";\n";
            cuerpo += nodo + "->" + op2.Nombre + ";\n";
            NodoDot nuevo = new NodoDot(nodo, op1.Cuerpo + op2.Cuerpo + cuerpo, op2.Numero + 1);
            return nuevo;
        } else {
            NodoDot op1 = this.operandoU.generarDot(numero);
            String nodo = "node" + op1.Numero;
            String cuerpo = nodo + "[label=\"Operacion, Operador: " + this.operador + "\"];\n";
            cuerpo += nodo + "->" + op1.Nombre + ";\n";
            NodoDot nuevo = new NodoDot(nodo, op1.Cuerpo + cuerpo, op1.Numero + 1);
            return nuevo;
        }

    }

    public enum Operador {
        SUMA,
        RESTA,
        MULTIPLICACION,
        DIVISION,
        POTENCIA,
        MODULO,
        MENOS_UNARIO,
        MAYOR_QUE,
        MENOR_QUE,
        MAYOR_IGUAL,
        MENOR_IGUAL,
        IGUAL_IGUAL,
        DIFERENTE_QUE,
        OR,
        AND,
        NOT,
        DESCONOCIDO
    }

    public static Operador getOperador(String op) {
        switch (op) {
            case "+":
                return Operador.SUMA;
            case "-":
                return Operador.RESTA;
            case "*":
                return Operador.MULTIPLICACION;
            case "/":
                return Operador.DIVISION;
            case "^":
                return Operador.POTENCIA;
            case "%%":
                return Operador.MODULO;
            case ">":
                return Operador.MAYOR_QUE;
            case "<":
                return Operador.MENOR_QUE;
            case "==":
                return Operador.IGUAL_IGUAL;
            case "!=":
                return Operador.DIFERENTE_QUE;
            case "!":
                return Operador.NOT;
            case "|":
                return Operador.OR;
            case "&":
                return Operador.AND;
            case ">=":
                return Operador.MAYOR_IGUAL;
            case "<=":
                return Operador.MENOR_IGUAL;
            default:
                return Operador.DESCONOCIDO;
        }
    }

    @Override
    public Object getValorImplicito(Entorno en) {
        Object op1 = new Object(), op2 = new Object(), opU = new Object();
        try {
            if (operandoU == null) {
                op1 = operando1.getValorImplicito(en);
                op2 = operando2.getValorImplicito(en);
                if (op1 instanceof Vector && op2 instanceof Vector) {
                    Vector v1 = (Vector) op1;
                    Vector v2 = (Vector) op2;
                    if (v1.tamaño() != v2.tamaño()) {
                        if (v1.tamaño() == 1) {
                            Vector nuevo = new Vector();
                            for (int x = 0; x < v2.tamaño(); x++) {
                                Object val = RealizarOperacion(v1.valores.get(0).valor, v2.valores.get(x).valor, opU);
                                if (val != null) {
                                    nuevo.agregarFinal(new Nodo(val));
                                } else {
                                    return null;
                                }
                            }
                            nuevo.ponerTipoNuevo();
                            return nuevo;
                        } else if (v2.tamaño() == 1) {
                            Vector nuevo = new Vector();
                            for (int x = 0; x < v1.tamaño(); x++) {
                                Object val = RealizarOperacion(v1.valores.get(x).valor, v2.valores.get(0).valor, opU);
                                if (val != null) {
                                    nuevo.agregarFinal(new Nodo(val));
                                } else {
                                    return null;
                                }
                            }

                            nuevo.ponerTipoNuevo();
                            return nuevo;
                        } else {
                            Informacion.agregarError(new ErrorAr("Semantico", "Los vectores no son del mislo tamaño y ni uno de los dos es de tamaño 1", this.fila, this.columna));
                        }
                    } else {
                        Vector nuevo = new Vector();
                        for (int x = 0; x < v2.tamaño(); x++) {
                            Object val = RealizarOperacion(v1.valores.get(x).valor, v2.valores.get(x).valor, opU);
                            if (val != null) {
                                nuevo.agregarFinal(new Nodo(val));
                            } else {
                                return null;
                            }
                        }
                        nuevo.ponerTipoNuevo();
                        return nuevo;
                    }
                } else if (op1 instanceof Vector && op2 instanceof Matris) {
                    Vector vecO = (Vector) op1;
                    if (vecO.tamaño() == 1) {
                        Matris mat = (Matris) op2;
                        Matris nueva = new Matris(mat.getFila(), mat.getColumna());

                        Object val = vecO.valores.get(0).valor;
                        for (int x = 0; x < mat.getFila(); x++) {
                            for (int y = 0; y < mat.getColumna(); y++) {
                                Vector matV = mat.ObtenerPosicion(x, y);
                                Object res = this.RealizarOperacion(val, matV.valores.get(0).valor, opU);
                                if (res != null) {
                                    nueva.insertar(x, y, res);
                                } else {
                                    return null;
                                }
                            }
                        }
                        nueva.ponerTipo();
                        return nueva;
                    } else {
                        Informacion.agregarError(new ErrorAr("Semantico", "El vector debe de ser de tamaño 1 para poder operarce con la matris", this.fila, this.columna));
                        return null;
                    }

                } else if (op1 instanceof Matris && op2 instanceof Vector) {
                    Vector vecO = (Vector) op2;
                    if (vecO.tamaño() == 1) {
                        Matris mat = (Matris) op1;
                        Matris nueva = new Matris(mat.getFila(), mat.getColumna());

                        Object val = vecO.valores.get(0).valor;
                        for (int x = 0; x < mat.getFila(); x++) {
                            for (int y = 0; y < mat.getColumna(); y++) {
                                Vector matV = mat.ObtenerPosicion(x, y);
                                Object res = this.RealizarOperacion(matV.valores.get(0).valor, val, opU);
                                if (res != null) {
                                    nueva.insertar(x, y, res);
                                } else {
                                    return null;
                                }
                            }
                        }
                        nueva.ponerTipo();
                        return nueva;
                    } else {
                        Informacion.agregarError(new ErrorAr("Semantico", "El vector debe de ser de tamaño 1 para poder operarce con la matris", this.fila, this.columna));
                        return null;
                    }

                } else if (op1 instanceof Matris && op2 instanceof Matris) {
                    Matris mat = (Matris) op1;
                    Matris mat2 = (Matris) op2;
                    if (mat.getFila() == mat2.getFila() && mat.getColumna() == mat2.getColumna()) {

                        Matris nueva = new Matris(mat.getFila(), mat.getColumna());

                        for (int x = 0; x < mat.getFila(); x++) {
                            for (int y = 0; y < mat.getColumna(); y++) {
                                Vector matV = mat.ObtenerPosicion(x, y);
                                Vector mat2V = mat2.ObtenerPosicion(x, y);
                                Object res = this.RealizarOperacion(matV.valores.get(0).valor, mat2V.valores.get(0).valor, opU);
                                if (res != null) {
                                    nueva.insertar(x, y, res);
                                } else {
                                    return null;
                                }
                            }
                        }
                        nueva.ponerTipo();
                        return nueva;
                    } else {
                        Informacion.agregarError(new ErrorAr("Semantico", "Las filas y columnas de las dos matrices deben ser iguales", this.fila, this.columna));
                        return null;
                    }

                }
            } else {
                opU = operandoU.getValorImplicito(en);
                if (opU instanceof Vector) {
                    Vector vU = (Vector) opU;
                    Vector nuevo = new Vector();
                    for (int x = 0; x < vU.tamaño(); x++) {
                        Object val = RealizarOperacion(null, null, vU.valores.get(x).valor);
                        if (val != null) {
                            nuevo.agregarFinal(new Nodo(val));
                        } else {
                            return null;
                        }
                    }
                    String tipo = nuevo.ponerTipo();
                    nuevo.setTipo(tipo);
                    return nuevo;
                } else if (opU instanceof Matris) {
                    Matris mat = (Matris) opU;

                    Matris nueva = new Matris(mat.getFila(), mat.getColumna());

                    for (int x = 0; x < mat.getFila(); x++) {
                        for (int y = 0; y < mat.getColumna(); y++) {
                            Vector matV = mat.ObtenerPosicion(x, y);

                            Object res = this.RealizarOperacion(null, null, matV.valores.get(0).valor);
                            if (res != null) {
                                nueva.insertar(x, y, res);
                            } else {
                                return null;
                            }
                        }
                    }
                    nueva.ponerTipo();
                    return nueva;

                }
            }
        } catch (Exception e) {

        }
        return null;
    }

    public Object RealizarOperacion(Object op1, Object op2, Object opU) {
        try {

            switch (this.operador) {
                case SUMA: {
                    return Sumar(op1, op2);
                }
                case RESTA: {
                    return Restar(op1, op2);
                }
                case MULTIPLICACION: {
                    return Multiplicar(op1, op2);
                }
                case DIVISION: {
                    return Dividir(op1, op2);
                }
                case POTENCIA: {
                    return Elevar(op1, op2);
                }
                case MODULO: {
                    return Modulo(op1, op2);
                }
                case MENOS_UNARIO: {
                    return MenosUn(opU);
                }
                case MAYOR_QUE: {
                    return Mayor(op1, op2);
                }
                case MENOR_QUE: {
                    return Menor(op1, op2);
                }
                case MAYOR_IGUAL: {
                    return MayorIgual(op1, op2);
                }
                case MENOR_IGUAL: {
                    return MenorIgual(op1, op2);
                }
                case IGUAL_IGUAL: {
                    return IgualIgual(op1, op2);
                }
                case DIFERENTE_QUE: {
                    return Diferente(op1, op2);
                }
                case OR: {
                    return Or(op1, op2);
                }
                case AND: {
                    return And(op1, op2);
                }
                case NOT: {
                    return Not(opU);
                }

            }
        } catch (Exception e) {
            Informacion.agregarError(new ErrorAr("Ejecucion", "Error al momento de operar", this.fila, this.columna));
        }
        return null;
    }

    private Object Sumar(Object op1, Object op2) {
        if (op1 instanceof Integer && op2 instanceof Double) {
            double res = (int) op1 + (double) op2;
            return res;
        } else if (op1 instanceof Double && op2 instanceof Integer) {
            double res = (double) op1 + (int) op2;
            return res;
        } else if (op1 instanceof Double && op2 instanceof Double) {
            double res = (double) op1 + (double) op2;
            return res;
        } else if (op1 instanceof Integer && op2 instanceof Integer) {
            int res = (int) op1 + (int) op2;
            return res;
        } else if (op1 instanceof String && op2 instanceof Integer) {
            String res = (String) op1 + (int) op2;
            return res;
        } else if (op1 instanceof Integer && op2 instanceof String) {
            String res = (int) op1 + (String) op2;
            return res;
        } else if (op1 instanceof String && op2 instanceof Double) {
            String res = (String) op1 + (double) op2;
            return res;
        } else if (op1 instanceof Double && op2 instanceof String) {
            String res = (double) op1 + (String) op2;
            return res;
        } else if (op1 instanceof String && op2 instanceof Boolean) {
            String bool = (boolean) op2 ? "true" : "false";
            String res = (String) op1 + bool;
            return res;
        } else if (op1 instanceof Boolean && op2 instanceof String) {
            String bool = (boolean) op1 ? "true" : "false";
            String res = bool + (String) op2;
            return res;
        } else if (op1 instanceof String && op2 instanceof String) {
            String res = (String) op1 + (String) op2;
            return res;
        } else {
            Informacion.agregarError(new ErrorAr("Semantico", "Error de tipos en la suma", this.fila, this.columna));
        }
        return null;
    }

    private Object Restar(Object op1, Object op2) {
        if (op1 instanceof Integer && op2 instanceof Double) {
            double res = (int) op1 - (double) op2;
            return res;
        } else if (op1 instanceof Double && op2 instanceof Integer) {
            double res = (double) op1 - (int) op2;
            return res;
        } else if (op1 instanceof Double && op2 instanceof Double) {
            double res = (double) op1 - (double) op2;
            return res;
        } else if (op1 instanceof Integer && op2 instanceof Integer) {
            int res = (int) op1 - (int) op2;
            return res;
        } else {
            Informacion.agregarError(new ErrorAr("Semantico", "Error de tipos en la resta", this.fila, this.columna));
        }
        return null;
    }

    private Object Multiplicar(Object op1, Object op2) {
        if (op1 instanceof Integer && op2 instanceof Double) {
            double res = (int) op1 * (double) op2;
            return res;
        } else if (op1 instanceof Double && op2 instanceof Integer) {
            double res = (double) op1 * (int) op2;
            return res;
        } else if (op1 instanceof Double && op2 instanceof Double) {
            double res = (double) op1 * (double) op2;
            return res;
        } else if (op1 instanceof Integer && op2 instanceof Integer) {
            int res = (int) op1 * (int) op2;
            return res;
        } else {
            Informacion.agregarError(new ErrorAr("Semantico", "Error de tipos en la Multiplicacion", this.fila, this.columna));
        }
        return null;
    }

    private Object Dividir(Object op1, Object op2) {
        if (op1 instanceof Integer && op2 instanceof Double) {
            if ((double) op2 != 0.0) {
                double res = (int) op1 / (double) op2;
                return res;
            } else {
                Informacion.agregarError(new ErrorAr("Semantico", "Error en la division operador 2 es cero", this.fila, this.columna));
            }
        } else if (op1 instanceof Double && op2 instanceof Integer) {
            if ((int) op2 != 0) {
                double res = (double) op1 / (int) op2;
                return res;
            } else {
                Informacion.agregarError(new ErrorAr("Semantico", "Error en la division operador 2 es cero", this.fila, this.columna));
            }
        } else if (op1 instanceof Double && op2 instanceof Double) {
            if ((double) op2 != 0.0) {
                double res = (double) op1 / (double) op2;
                return res;
            } else {
                Informacion.agregarError(new ErrorAr("Semantico", "Error en la division operador 2 es cero", this.fila, this.columna));
            }
        } else if (op1 instanceof Integer && op2 instanceof Integer) {
            if ((int) op2 != 0) {
                double res = (int) op1 / (int) op2;
                Double val = res;
                return val.intValue();
            } else {
                Informacion.agregarError(new ErrorAr("Semantico", "Error en la division operador 2 es cero", this.fila, this.columna));
            }
        } else {
            Informacion.agregarError(new ErrorAr("Semantico", "Error de tipos en la division", this.fila, this.columna));
        }
        return null;
    }

    private Object Elevar(Object op1, Object op2) {
        if (op1 instanceof Integer && op2 instanceof Double) {
            double res = Math.pow((int) op1, (double) op2);
            return res;
        } else if (op1 instanceof Double && op2 instanceof Integer) {
            double res = Math.pow((double) op1, (int) op2);
            return res;
        } else if (op1 instanceof Double && op2 instanceof Double) {
            double res = Math.pow((double) op1, (double) op2);
            return res;
        } else if (op1 instanceof Integer && op2 instanceof Integer) {
            double res = Math.pow((int) op1, (int) op2);
            return res;
        } else {
            Informacion.agregarError(new ErrorAr("Semantico", "Error de tipos en la Potencia", this.fila, this.columna));
        }
        return null;
    }

    private Object Modulo(Object op1, Object op2) {
        if (op1 instanceof Integer && op2 instanceof Double) {
            double res = (int) op1 % (double) op2;
            return res;
        } else if (op1 instanceof Double && op2 instanceof Integer) {
            double res = (double) op1 % (int) op2;
            return res;
        } else if (op1 instanceof Double && op2 instanceof Double) {
            double res = (double) op1 % (double) op2;
            return res;
        } else if (op1 instanceof Integer && op2 instanceof Integer) {
            double val = (int) op1 % (int) op2;
            Double res = val;
            return res.intValue();
        } else {
            Informacion.agregarError(new ErrorAr("Semantico", "Error de tipos en la Mpdulo", this.fila, this.columna));
        }
        return null;
    }

    private Object MenosUn(Object opU) {
        if (opU instanceof Double) {
            double res = 0.0 - (double) opU;
            return res;
        } else if (opU instanceof Integer) {
            int res = 0 - (int) opU;
            return res;
        } else {
            Informacion.agregarError(new ErrorAr("Semantico", "Error de tipos, se utilizo el operador menos unario incorrectamente", this.fila, this.columna));
        }
        return null;
    }

    private Object Mayor(Object op1, Object op2) {
        if (op1 instanceof Integer && op2 instanceof Double) {
            return (int) op1 > (double) op2;
        } else if (op1 instanceof Double && op2 instanceof Integer) {
            return (double) op1 > (int) op2;
        } else if (op1 instanceof Double && op2 instanceof Double) {
            return (double) op1 > (double) op2;
        } else if (op1 instanceof Integer && op2 instanceof Integer) {
            return (int) op1 > (int) op2;
        } else if (op1 instanceof String && op2 instanceof String) {
            String cad1 = (String) op1;
            String cad2 = (String) op2;
            int val = cad1.compareTo(cad2);
            return val > 0;
        } else {
            Informacion.agregarError(new ErrorAr("Semantico", "Error de tipos en la Mayor que", this.fila, this.columna));
        }
        return null;
    }

    private Object Menor(Object op1, Object op2) {
        if (op1 instanceof Integer && op2 instanceof Double) {
            return (int) op1 < (double) op2;
        } else if (op1 instanceof Double && op2 instanceof Integer) {
            return (double) op1 < (int) op2;
        } else if (op1 instanceof Double && op2 instanceof Double) {
            return (double) op1 < (double) op2;
        } else if (op1 instanceof Integer && op2 instanceof Integer) {
            return (int) op1 < (int) op2;
        } else if (op1 instanceof String && op2 instanceof String) {
            String cad1 = (String) op1;
            String cad2 = (String) op2;
            int val = cad1.compareTo(cad2);
            return val < 0;
        } else {
            Informacion.agregarError(new ErrorAr("Semantico", "Error de tipos en la Menor que", this.fila, this.columna));
        }
        return null;
    }

    private Object MayorIgual(Object op1, Object op2) {
        if (op1 instanceof Integer && op2 instanceof Double) {
            return (int) op1 >= (double) op2;
        } else if (op1 instanceof Double && op2 instanceof Integer) {
            return (double) op1 >= (int) op2;
        } else if (op1 instanceof Double && op2 instanceof Double) {
            return (double) op1 >= (double) op2;
        } else if (op1 instanceof Integer && op2 instanceof Integer) {
            return (int) op1 >= (int) op2;
        } else if (op1 instanceof String && op2 instanceof String) {
            String cad1 = (String) op1;
            String cad2 = (String) op2;
            int val = cad1.compareTo(cad2);
            return val >= 0;
        } else {
            Informacion.agregarError(new ErrorAr("Semantico", "Error de tipos en la Mayor Igual que", this.fila, this.columna));
        }
        return null;
    }

    private Object MenorIgual(Object op1, Object op2) {
        if (op1 instanceof Integer && op2 instanceof Double) {
            return (int) op1 <= (double) op2;
        } else if (op1 instanceof Double && op2 instanceof Integer) {
            return (double) op1 <= (int) op2;
        } else if (op1 instanceof Double && op2 instanceof Double) {
            return (double) op1 <= (double) op2;
        } else if (op1 instanceof Integer && op2 instanceof Integer) {
            return (int) op1 <= (int) op2;
        } else if (op1 instanceof String && op2 instanceof String) {
            String cad1 = (String) op1;
            String cad2 = (String) op2;
            int val = cad1.compareTo(cad2);
            return val <= 0;
        } else {
            Informacion.agregarError(new ErrorAr("Semantico", "Error de tipos en la Menor Igual que", this.fila, this.columna));
        }
        return null;
    }

    private Object IgualIgual(Object op1, Object op2) {
        if (op1 instanceof Integer && op2 instanceof Double) {
            return (int) op1 == (double) op2;
        } else if (op1 instanceof Double && op2 instanceof Integer) {
            return (double) op1 == (int) op2;
        } else if (op1 instanceof Double && op2 instanceof Double) {
            return (double) op1 == (double) op2;
        } else if (op1 instanceof Integer && op2 instanceof Integer) {
            boolean res = (int) op1 == (int) op2;
            return res;
        } else if (op1 instanceof String && op2 instanceof String) {
            String cad1 = (String) op1;
            String cad2 = (String) op2;
            int val = cad1.compareTo(cad2);
            return val == 0;
        } else if (op1 instanceof Boolean && op2 instanceof Boolean) {
            return (boolean) op1 == (boolean) op2;
        } else {
            Informacion.agregarError(new ErrorAr("Semantico", "Error de tipos en Igual Igual que", this.fila, this.columna));
        }
        return null;
    }

    private Object Diferente(Object op1, Object op2) {
        if (op1 instanceof Integer && op2 instanceof Double) {
            return (int) op1 != (double) op2;
        } else if (op1 instanceof Double && op2 instanceof Integer) {
            return (double) op1 != (int) op2;
        } else if (op1 instanceof Double && op2 instanceof Double) {
            return (double) op1 != (double) op2;
        } else if (op1 instanceof Integer && op2 instanceof Integer) {
            return (int) op1 != (int) op2;
        } else if (op1 instanceof String && op2 instanceof String) {
            String cad1 = (String) op1;
            String cad2 = (String) op2;
            int val = cad1.compareTo(cad2);
            return val != 0;
        } else if (op1 instanceof Boolean && op2 instanceof Boolean) {
            return (boolean) op1 != (boolean) op2;
        } else {
            Informacion.agregarError(new ErrorAr("Semantico", "Error de tipos en la Diferente que", this.fila, this.columna));
        }
        return null;
    }

    private Object Or(Object op1, Object op2) {
        if (op1 instanceof Boolean && op2 instanceof Boolean) {
            boolean res = (boolean) op1 || (boolean) op2;
            return res;
        } else {
            Informacion.agregarError(new ErrorAr("Semantico", "Error de tipos en OR", this.fila, this.columna));
        }
        return null;
    }

    private Object And(Object op1, Object op2) {
        if (op1 instanceof Boolean && op2 instanceof Boolean) {
            return (boolean) op1 && (boolean) op2;
        } else {
            Informacion.agregarError(new ErrorAr("Semantico", "Errorde tipos en And", this.fila, this.columna));
        }
        return null;
    }

    private Object Not(Object opU) {
        if (opU instanceof Boolean) {
            return !(boolean) opU;
        } else {
            Informacion.agregarError(new ErrorAr("Semantico", "Se utilizo un operador not, el operando debe ser de tipo bool", this.fila, this.columna));
        }
        return null;
    }

}
