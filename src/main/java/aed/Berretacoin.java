package aed;

public class Berretacoin {
    private ListaEnlazadaDoble<Bloque> blockchain;
    private int[] saldos;
    private int maxTenedor;

    // Ej:
    // ids:     1 2 3 4 5
    // saldos: [0,0,0,0,0]

    // saldo por id -> array[id-1]
    // array[id_comprador - 1] -= monto 
    // array[id_vendedor - 1] += monto

    // Esto es todo O(1)

    public Berretacoin(int n_usuarios){
        this.blockchain = new ListaEnlazadaDoble();
        this.saldos = new int[n_usuarios];
        this.maxTenedor = 1;
    }

    public void agregarBloque(Transaccion[] transacciones){
        Bloque bloque = new Bloque(transacciones);
        this.blockchain.agregarAtras(bloque);
        for (Transaccion t : transacciones) {
            this.saldos[t.id_comprador()-1] -= t.monto();
            this.saldos[t.id_vendedor()-1] += t.monto();
            if (saldos[t.id_vendedor()] > saldos[maxTenedor-1]){
                this.maxTenedor = t.id_vendedor()+1;
            }
        }
    }

    public Transaccion txMayorValorUltimoBloque(){
        return this.blockchain.obtenerUltimo().mayorTransaccion();
    }

    public Transaccion[] txUltimoBloque(){
        Transaccion[] transacciones = this.blockchain.obtenerUltimo().transaccionesOrdenadasPorId();
        Transaccion[] copia = new Transaccion[transacciones.length];
        for (int i = 0; i < transacciones.length; i++) {
            copia[i] = transacciones[i].copiar();
        }
        return copia;
    }

    public int maximoTenedor(){
        return this.maxTenedor;
    }

    public int montoMedioUltimoBloque(){
        return this.blockchain.obtenerUltimo().montoMedio();
    }

    public void hackearTx(){
        // actualizar monto medio
        // actualizar saldos
        // borrar transaccion en ambas estructuras

        this.blockchain.obtenerUltimo().borrarMayorTransaccion();
    }

}









