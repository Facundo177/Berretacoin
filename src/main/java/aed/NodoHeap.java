package aed;

import aed.ListaEnlazadaDoble.Nodo;

public class NodoHeap implements Comparable<NodoHeap> {
    Transaccion transaccion;
    Nodo handle;

    public NodoHeap(Transaccion transaccion, Nodo handle) {
        this.transaccion = transaccion;
        this.handle = handle;
    }

    @Override
    public int compareTo(NodoHeap o) {
        return this.transaccion.compareTo(o.transaccion);
    }
    
}
