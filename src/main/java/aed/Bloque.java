package aed;

import aed.Heap.Tupla;

public class Bloque {
    private ListaEnlazadaDoble<Transaccion> transaccionesOrdenadasPorId;
    private Heap<Transaccion> transaccionesOrdenadasPorMonto;
    private int sumaMontos;
    private int cantTransacciones; 

public Bloque(Transaccion[] transacciones){
    this.transaccionesOrdenadasPorId = new ListaEnlazadaDoble<>();
    this.transaccionesOrdenadasPorMonto = new Heap<>(transacciones.length);
    this.sumaMontos = 0;
    this.cantTransacciones = 0;
            
    for (int i = 0; i < transacciones.length; i++) {                                // O(n)
        Transaccion t = transacciones[i].copiar();                                  // O(1)
        Handle handle = this.transaccionesOrdenadasPorId.agregarAtras(t);           // O(1)
        // vinculo ambas estructuras a través del handle
        this.transaccionesOrdenadasPorMonto.agregarRapidoConHandle(t, handle);      // O(1)
        this.sumaMontos += transacciones[i].esDeCreacion() ? 0 : transacciones[i].monto();
        this.cantTransacciones += transacciones[i].esDeCreacion() ? 0 : 1;
    
    }
    this.transaccionesOrdenadasPorMonto.floyd();    // O(n)
}
    
    public Transaccion[] transaccionesOrdenadasPorId(){
        // creo un nuevo array para hacer la copia
        Transaccion[] res = new Transaccion[this.transaccionesOrdenadasPorId.longitud()];
        // uso el iterador de lista para copiar una por una de las transacciones
        Iterador<Transaccion> iterador = this.transaccionesOrdenadasPorId.iterador();
        int i = 0;
        while (iterador.haySiguiente()) {
            // el método copiar() de transacción de devuelve un nuevo objeto (en otra posición de memoria)
            // que es igual al original (el equals() entre ambos da True)
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
        return this.transaccionesOrdenadasPorMonto.maximo();
    }

    public Transaccion borrarMayorTransaccion(){
        // Borro la transaccion de la cola de prioridad
        Tupla max = this.transaccionesOrdenadasPorMonto.sacarMaximo();
        Handle handle = max.getHandle();
        Transaccion maxima = (Transaccion) max.getElem();

        // Actualizo los valores usados en el monto medio
        this.sumaMontos -= maxima.id_comprador() == 0 ? 0 : maxima.monto();
        this.cantTransacciones -= maxima.id_comprador() == 0 ? 0 : 1;

        // Borro la transaccion de la lista
        this.transaccionesOrdenadasPorId.eliminarNodo(handle);

        return maxima;
    }
    
}
    
