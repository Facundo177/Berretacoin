package aed;

import java.util.ArrayList;

public class Bloque {
    private TransaccionActiva[] transaccionesOrdenadasPorId;
    private ColaDePrioridad<Transaccion> transaccionesOrdenadasPorMonto;
    private float montoMedio;

    public Bloque(Transaccion[] transacciones){

        
        // faltan las de creacion


        if (transacciones.length <= 0){
            this.montoMedio = 0;
            this.transaccionesOrdenadasPorId = new TransaccionActiva[0];
            this.transaccionesOrdenadasPorMonto = new ColaDePrioridad<Transaccion>();
        } else{
            Transaccion[] copia = new Transaccion[transacciones.length];
            this.transaccionesOrdenadasPorId = new TransaccionActiva[transacciones.length];
            int suma = 0;
            for (int i = 0; i < transacciones.length; i++) {
                copia[i] = transacciones[i].copiar();
                this.transaccionesOrdenadasPorId[i] = new TransaccionActiva(copia[i]);
                suma += transacciones[i].monto();
            }
            this.montoMedio = suma / transacciones.length;
            this.transaccionesOrdenadasPorMonto = new ColaDePrioridad<Transaccion>(copia);
        }
    }
    
    public Transaccion[] transaccionesOrdenadasPorId(){
        ArrayList<Transaccion> copia = new ArrayList<>(transaccionesOrdenadasPorId.length);
        for (TransaccionActiva t : transaccionesOrdenadasPorId) {
            if (t.sigueActiva()) {
                copia.add(t.transaccion().copiar());
            }
        }
        return (Transaccion[]) copia.toArray();
    }

    public int montoMedio(){
        return (int) this.montoMedio;
    }

    public Transaccion mayorTransaccion(){
        return this.transaccionesOrdenadasPorMonto.maximo();
    }

    public Transaccion borrarMayorTransaccion(){
        // actualizo monto medio y transacciones por monto
        int n = this.transaccionesOrdenadasPorMonto.size();
        Transaccion maxima = this.transaccionesOrdenadasPorMonto.sacarMaximo();
        this.montoMedio = (this.montoMedio*n - maxima.monto())/(n-1);

        // actualizo las transacciones por id
        this.transaccionesOrdenadasPorId[maxima.id()].borradoLogico();

        return maxima;
    }
    
}
    
