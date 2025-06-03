package aed;

public class Bloque {
    private Transaccion[] transaccionesOrdenadasPorId;
    private ColaDePrioridad<Transaccion> transaccionesOrdenadasPorMonto;
    private int montoMedio;

    public Bloque(Transaccion[] transacciones){
        Transaccion[] copia = new Transaccion[transacciones.length];
        for (int i = 0; i < transacciones.length; i++) {
            copia[i] = transacciones[i].copiar();
        }
        this.transaccionesOrdenadasPorId = copia;
        this.transaccionesOrdenadasPorMonto = new ColaDePrioridad<Transaccion>(copia);
        if (transacciones.length > 0){
            int suma = 0;
            for (Transaccion t : transacciones){
                suma += t.monto();
            }
            this.montoMedio = suma / transacciones.length;
        } else{
            this.montoMedio = 0;
        }
    }
    
    public Transaccion[] transaccionesOrdenadasPorId(){

        // deberia hacer la copia acá y no en la Berretacoin

        return this.transaccionesOrdenadasPorId;
    }

    public int montoMedio(){
        return this.montoMedio;
    }

    public Transaccion mayorTransaccion(){
        return this.transaccionesOrdenadasPorMonto.maximo();
    }

    public void borrarMayorTransaccion(){
        Transaccion maxima = this.transaccionesOrdenadasPorMonto.sacarMaximo();
        int id = maxima.id();
        
        // deberia usar un ArrayList para hacer más fácil el borrado
        this.transaccionesOrdenadasPorId.remove(id);
    }
    
}
    
