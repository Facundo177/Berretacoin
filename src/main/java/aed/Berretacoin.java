package aed;

public class Berretacoin {
    private ListaEnlazadaDoble<Bloque> blockchain;
    private Saldos saldos;

    public Berretacoin(int n_usuarios){
        // tomamos que los pedidos de memoria son en O(1)
        this.blockchain = new ListaEnlazadaDoble<>();   // O(1)
        // el constructor hace un ciclo que depende de la cantidad de usuarios
        this.saldos = new Saldos(n_usuarios);           // O(P)
    }

    public void agregarBloque(Transaccion[] transacciones){
        // el constructor de bloque hace un ciclo por el tamaño de las transacciones recibidas 
        // y después el algoritmo de Floyd
        // ambos O(n)
        Bloque bloque = new Bloque(transacciones);  // O(n)
        this.blockchain.agregarAtras(bloque);       // O(1)

        // por cada transaccion (n) se hace una actualización o dos de los saldos (logP)
        for (Transaccion t : transacciones) {   // O(n * logP)
            if (t.esDeCreacion()){
                this.saldos.actualizarSaldo(t.id_vendedor(), t.monto());
            } else {
                this.saldos.actualizarSaldo(t.id_comprador(), -t.monto());
                this.saldos.actualizarSaldo(t.id_vendedor(), t.monto());
            }
        }
    }

    // obtener el ultimo bloque es en tiempo constante
    // leer el mayor valor en una cola de prioridad es en tiempo constante 
    public Transaccion txMayorValorUltimoBloque(){
        return this.blockchain.obtenerUltimo().mayorTransaccion();  // O(1)
    }

    // obtener el ultimo bloque es en tiempo constante
    // copiar cada transacción en una lista tiene una complejidad lineal 
    public Transaccion[] txUltimoBloque(){
        return this.blockchain.obtenerUltimo().transaccionesOrdenadasPorId();   // O(n)
    }

    // leer el mayor valor de un heap es en tiempo constante
    public int maximoTenedor(){
        return this.saldos.maximoTenedor();     // O(1)
    }

    // calcular el monto medio es hacer una división entre dos valores (complejidad constante)
    public int montoMedioUltimoBloque(){
        return this.blockchain.obtenerUltimo().montoMedio();    // O(1)
    }

    // obtener el ultimo bloque es en tiempo constante
    // actualizar el monto medio y borrar la transaccion tiene complejidad O(logn)
    public void hackearTx(){
        Bloque ultimoBloque = this.blockchain.obtenerUltimo();  // O(1)

        // sacar el maximo de una cola de prioridad O(logn)
        // borrar un nodo de la lista a través del handle O(1)
        // actualizar el monto medio O(1)
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









