package aed;

public class Berretacoin {
    private ListaEnlazadaDoble<Bloque> blockchain;
    private Saldos saldos;

    public Berretacoin(int n_usuarios){
        this.blockchain = new ListaEnlazadaDoble<>();
        this.saldos = new Saldos(n_usuarios);
    }

    public void agregarBloque(Transaccion[] transacciones){
        Bloque bloque = new Bloque(transacciones);
        this.blockchain.agregarAtras(bloque);

        for (Transaccion t : transacciones) {
            this.saldos.actualizarSaldo(t.id_comprador(), -t.monto());
            this.saldos.actualizarSaldo(t.id_vendedor(), t.monto());
        }
    }

    public Transaccion txMayorValorUltimoBloque(){
        return this.blockchain.obtenerUltimo().mayorTransaccion();
    }

    public Transaccion[] txUltimoBloque(){
        return this.blockchain.obtenerUltimo().transaccionesOrdenadasPorId();
    }

    public int maximoTenedor(){
        return this.saldos.maximoTenedor();
    }

    public int montoMedioUltimoBloque(){
        return this.blockchain.obtenerUltimo().montoMedio();
    }

    public void hackearTx(){
        Bloque ultimoBloque = this.blockchain.obtenerUltimo();
        // actualizo el monto medio y borro la transaccion
        Transaccion t = ultimoBloque.borrarMayorTransaccion();
        // el vendedor pierde el monto ganado en la transaccion, "se lo lleva el hacker"
        saldos.actualizarSaldo(t.id_vendedor(), -t.monto());
    }

}









