package aed;

import aed.ListaEnlazadaDoble.Nodo;

public class Bloque {
    private ListaEnlazadaDoble<Transaccion> transaccionesOrdenadasPorId;
    private ColaDePrioridad<NodoHeap> transaccionesOrdenadasPorMonto;
    private int sumaMontos;
    private int cantTransacciones; 

public Bloque(Transaccion[] transacciones){
    this.transaccionesOrdenadasPorId = new ListaEnlazadaDoble<>();
    this.transaccionesOrdenadasPorMonto = new ColaDePrioridad<>(transacciones.length);
    this.sumaMontos = 0;
    this.cantTransacciones = 0;
            
    for (int i = 0; i < transacciones.length; i++) {                                // O(n)
        Transaccion t = transacciones[i].copiar();                                  // O(1)
        Nodo nodo = this.transaccionesOrdenadasPorId.agregarAtras(t);               // O(1)
        NodoHeap nodoHeap = new NodoHeap(t, nodo);                                  // O(1)
        nodo.handle = nodoHeap;                                                     // O(1)
        this.transaccionesOrdenadasPorMonto.agregarRapido(nodoHeap);                // O(1)
        this.sumaMontos += transacciones[i].id_comprador() == 0 ? 0 : transacciones[i].monto();
        this.cantTransacciones += transacciones[i].id_comprador() == 0 ? 0 : 1;
    
    }this.transaccionesOrdenadasPorMonto.floyd();    // O(n)
}
    
    public Transaccion[] transaccionesOrdenadasPorId(){
        Transaccion[] res = new Transaccion[this.transaccionesOrdenadasPorId.longitud()];
        Iterador<Transaccion> iterador = this.transaccionesOrdenadasPorId.iterador();
        int i = 0;
        while (iterador.haySiguiente()) {
            res[i] = iterador.siguiente().copiar();
            i++;
        }
        return res;
    }

    public int montoMedio(){
        if (this.cantTransacciones == 0){
            return 0;
        } else {
            return this.sumaMontos/this.cantTransacciones;
        }
    }

    public Transaccion mayorTransaccion(){
        return this.transaccionesOrdenadasPorMonto.maximo().transaccion;
    }

    public Transaccion borrarMayorTransaccion(){
        // Borro la transaccion de la cola de prioridad
        NodoHeap nodoHeap = this.transaccionesOrdenadasPorMonto.sacarMaximo();
        Nodo nodoLista = nodoHeap.handle;
        Transaccion maxima = nodoHeap.transaccion;

        // actualizo los valores usados en el monto medio
        this.sumaMontos -= maxima.id_comprador() == 0 ? 0 : maxima.monto();
        this.cantTransacciones -= maxima.id_comprador() == 0 ? 0 : 1;

        // Borro la transaccion de la lista
        this.transaccionesOrdenadasPorId.eliminarNodo(nodoLista);

        return maxima;
    }
    
}
    
