package aed;

import java.util.ArrayList;

import aed.ListaEnlazadaDoble.Nodo;

public class Bloque {
    private ListaEnlazadaDoble<Transaccion> transaccionesOrdenadasPorId;
    private ColaDePrioridad<Transaccion> transaccionesOrdenadasPorMonto;
    private int sumaMontos;
    private int cantTransacciones; 

    public Bloque(Transaccion[] transacciones){
        if (transacciones.length <= 0){
            this.sumaMontos = 0;
            this.cantTransacciones = 0;
            this.transaccionesOrdenadasPorId = new ListaEnlazadaDoble<>();
            this.transaccionesOrdenadasPorMonto = new ColaDePrioridad<>();
        } else{
            this.transaccionesOrdenadasPorId = new ListaEnlazadaDoble<Transaccion>();
            this.transaccionesOrdenadasPorMonto = new ColaDePrioridad<Transaccion>(transacciones.length);
            int sumaMontos = 0;
            int cantTransacciones = 0;
            for (int i = 0; i < transacciones.length; i++) {                                // O(n)
                Transaccion t = transacciones[i].copiar();                                  // O(1)
                Nodo nodo = this.transaccionesOrdenadasPorId.agregarAtras(t);               // O(1)
                NodoHeap nodoHeap = this.transaccionesOrdenadasPorMonto.agregarRapido(t);   // O(1)

                sumaMontos += transacciones[i].id_comprador() == 0 ? 0 : transacciones[i].monto();
                cantTransacciones += transacciones[i].id_comprador() == 0 ? 0 : 1;
            }
            this.sumaMontos = sumaMontos;
            this.cantTransacciones = cantTransacciones;
        }
    }
    
    public Transaccion[] transaccionesOrdenadasPorId(){
        ArrayList<Transaccion> copia = new ArrayList<>(transaccionesOrdenadasPorId.length);
        for (TransaccionActiva t : transaccionesOrdenadasPorId) {
            if (t.sigueActiva()) {
                copia.add(t.transaccion().copiar());
            }
        }
        Transaccion[] res = new Transaccion[copia.size()];
        for (int i = 0; i < copia.size(); i++){
            res[i] = copia.get(i);
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
        Transaccion maxima = this.transaccionesOrdenadasPorMonto.sacarMaximo();
        this.sumaMontos -= maxima.id_comprador() == 0 ? 0 : maxima.monto();
        this.cantTransacciones -= maxima.id_comprador() == 0 ? 0 : 1;

        // actualizo las transacciones por id
        this.transaccionesOrdenadasPorId[maxima.id()].borradoLogico();

        return maxima;


        // falla actualizar monto medio
    }
    
}
    
