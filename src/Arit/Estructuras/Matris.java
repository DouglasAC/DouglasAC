/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arit.Estructuras;

/**
 *
 * @author ddani
 */
public class Matris implements Cloneable {

    public Nodo[][] valores;
    int fila;
    int columna;
    String tipo;

    public Matris(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.valores = new Nodo[fila][columna];
    }

    public boolean insertar(int fila, int columna, Object valor) {
        if ((fila < this.fila && fila >= 0) && (columna < this.columna && columna >= 0)) {
            if (this.valores[fila][columna] == null) {
                this.valores[fila][columna] = new Nodo(valor);
            } else {
                this.valores[fila][columna].valor = valor;
            }
            return true;
        }
        return false;
    }

    public boolean insertarColumna(int columna, Object valor) {
        if (columna >= 0 && columna < this.columna) {
            for (int x = 0; x < this.fila; x++) {
                this.valores[x][columna].valor = valor;
            }
            return true;
        }
        return false;
    }

    public boolean insertarFila(int fila, Object valor) {
        if (fila >= 0 && fila < this.fila) {
            for (int x = 0; x < this.columna; x++) {
                this.valores[fila][x].valor = valor;
            }
            return true;
        }
        return false;
    }

    public boolean InsertarPosicion(int posicion, Object valor) {
        int num = 0;
        for (int x = 0; x < this.columna; x++) {
            for (int y = 0; y < this.fila; y++) {
                if (num == posicion) {
                    this.valores[y][x].valor = valor;
                    return true;
                }
                num++;
            }
        }
        return false;
    }

    public void ponerTipo() {
        int prioridad = 0;
        for (int x = 0; x < this.fila; x++) {
            for (int y = 0; y < this.columna; y++) {
                int nueva = this.prioridad(this.valores[x][y].valor);
                if (nueva > prioridad) {
                    prioridad = nueva;
                }
            }
        }
        switch (prioridad) {
            case 1:
                this.tipo = "integer";
                break;
            case 2:
                this.tipo = "numeric";
                break;
            case 0:
                this.tipo = "boolean";
                break;
            case 3:
                this.tipo = "string";
                break;
            default:
                break;
        }
        this.CastearValores(prioridad);
    }

    public Vector ObtenerPosicion(int fila, int columna) {
        if (fila >= 0 && columna >= 0 && fila < this.fila && columna < this.columna) {
            Vector nuevo = new Vector();
            nuevo.agregarFinal(this.valores[fila][columna]);
            nuevo.ponerTipoNuevo();
            return nuevo;

        }
        return null;
    }

    public Vector ObtenerFila(int fila) {
        if (fila >= 0 && fila < this.fila) {
            Vector nuevo = new Vector();
            for (int x = 0; x < this.columna; x++) {
                nuevo.agregarFinal(this.valores[fila][x]);
            }
            nuevo.ponerTipoNuevo();
            return nuevo;
        }
        return null;
    }

    public Vector ObtenerColumna(int columna) {
        if (columna >= 0 && columna < this.columna) {
            Vector nuevo = new Vector();
            for (int x = 0; x < this.fila; x++) {
                nuevo.agregarFinal(this.valores[x][columna]);
            }
            nuevo.ponerTipoNuevo();
            return nuevo;
        }
        return null;
    }

    public Vector ObtenerPosicion(int posicion) {
        int num = 0;
        for (int x = 0; x < this.columna; x++) {
            for (int y = 0; y < this.fila; y++) {
                if (num == posicion) {
                    Vector nuevo = new Vector();
                    nuevo.agregarFinal(this.valores[y][x]);
                    nuevo.ponerTipoNuevo();
                    return nuevo;
                }
                num++;
            }
        }

        return null;
    }

    public int prioridad(Object val) {
        if (val instanceof Integer) {
            return 1;
        } else if (val instanceof Double) {
            return 2;
        } else if (val instanceof Boolean) {
            return 0;
        } else if (val instanceof String) {
            return 3;
        }

        return 0;
    }

    public void CastearValores(int prioridad) {
        for (int x = 0; x < this.fila; x++) {
            for (int y = 0; y < this.columna; y++) {

                Object actual = this.valores[x][y].valor;
                if (prioridad == 1 && actual instanceof Boolean) {
                    boolean val = (boolean) actual;
                    int nuevo = val ? 1 : 0;
                    this.valores[x][y].valor = nuevo;
                }
                if (prioridad == 2 && actual instanceof Boolean) {
                    boolean val = (boolean) actual;
                    double nuevo = val ? 1.0 : 0.0;
                    this.valores[x][y].valor = nuevo;
                } else if (prioridad == 2 && actual instanceof Integer) {
                    int val = (int) actual;
                    double nuevo = val + 0.0;
                    this.valores[x][y].valor = nuevo;
                }
                if (prioridad == 3 && actual instanceof Boolean) {
                    boolean val = (boolean) actual;
                    String nuevo = val ? "true" : "false";
                    this.valores[x][y].valor = nuevo;
                } else if (prioridad == 3 && actual instanceof Integer) {
                    int val = (int) actual;
                    String nuevo = String.valueOf(val);
                    this.valores[x][y].valor = nuevo;
                } else if (prioridad == 3 && actual instanceof Double) {
                    double val = (double) actual;
                    String nuevo = String.valueOf(val);
                    this.valores[x][y].valor = nuevo;
                }

            }
        }
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Matris clone = null;
        try {
            clone = (Matris) super.clone();

            Nodo[][] nueva = new Nodo[this.fila][this.columna];
            for (int x = 0; x < this.fila; x++) {
                for (int y = 0; y < this.columna; y++) {
                    nueva[x][y] = (Nodo) this.valores[x][y].clone();
                }
            }

            clone.valores = nueva;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }

    @Override // this must return String
    public String toString() {
        String cuerpo = "       ";
        for (int x = 0; x < this.columna; x++) {
            cuerpo += "[," + (x + 1) + "]   ";
        }
        cuerpo += "\n";
        for (int x = 0; x < this.fila; x++) {
            cuerpo += "[" + (x + 1) + ",]   ";
            for (int y = 0; y < this.columna; y++) {
                cuerpo += this.valores[x][y].toString() + "    ";
            }
            cuerpo += "\n";
        }

        return cuerpo;
    }

}
