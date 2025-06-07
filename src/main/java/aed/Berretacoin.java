package aed;

public class Berretacoin {
    private ListaEnlazadaDoble<Bloque> blockchain;
    private Saldos saldos;

    public Berretacoin(int n_usuarios){
        this.blockchain = new ListaEnlazadaDoble<>();   // O(1)
        this.saldos = new Saldos(n_usuarios);           // O(P)
    }

    public void agregarBloque(Transaccion[] transacciones){
        Bloque bloque = new Bloque(transacciones);      // O(n)
        this.blockchain.agregarAtras(bloque);           // O(1)

        for (Transaccion t : transacciones) {           // O(n * logP)
            if (t.esDeCreacion()){
                this.saldos.actualizarSaldo(t.id_vendedor(), t.monto());
            } else {
                this.saldos.actualizarSaldo(t.id_comprador(), -t.monto());
                this.saldos.actualizarSaldo(t.id_vendedor(), t.monto());
            }
        }
    }

    public Transaccion txMayorValorUltimoBloque(){
        return this.blockchain.obtenerUltimo().mayorTransaccion();  // O(1)
    }

    public Transaccion[] txUltimoBloque(){
        return this.blockchain.obtenerUltimo().transaccionesOrdenadasPorId();   // O(n)
    }

    public int maximoTenedor(){
        return this.saldos.maximoTenedor();     // O(1)
    }

    public int montoMedioUltimoBloque(){
        return this.blockchain.obtenerUltimo().montoMedio();    // O(1)
    }

    public void hackearTx(){
        Bloque ultimoBloque = this.blockchain.obtenerUltimo();  // O(1)
        // actualizo el monto medio y borro la transaccion
        Transaccion t = ultimoBloque.borrarMayorTransaccion();  // O(logn)
        // actualizo saldos
        if (t.esDeCreacion()) {
            saldos.actualizarSaldo(t.id_vendedor(), -t.monto());    // O(logP)
        } else {
            saldos.actualizarSaldo(t.id_comprador(), t.monto());    // O(logP)
            saldos.actualizarSaldo(t.id_vendedor(), -t.monto());    // O(logP)
        }

        // los tests piden que se actualicen ambos saldos, 
        // haciendo que solo el vendedor pierda el dinero, "se lo lleva el hacker", no pasan
    }

}









