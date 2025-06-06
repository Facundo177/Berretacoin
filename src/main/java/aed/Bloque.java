package aed;

import java.util.ArrayList;

public class Bloque {
    private TransaccionActiva[] transaccionesOrdenadasPorId;
    private ColaDePrioridad<Transaccion> transaccionesOrdenadasPorMonto;
    private float montoMedio;

    public Bloque(Transaccion[] transacciones){
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
                suma += transacciones[i].id_comprador() == 0 ? 0 : transacciones[i].monto();
            }
            if (transacciones[0].id_comprador() == 0){
                this.montoMedio = transacciones.length == 1 ? suma : suma/(transacciones.length-1);
            } else{
                this.montoMedio = suma/transacciones.length;
            }
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
        Transaccion[] res = new Transaccion[copia.size()];
        for (int i = 0; i < copia.size(); i++){
            res[i] = copia.get(i);
        }
        return res;
    }

    public int montoMedio(){
        return Math.round(this.montoMedio);
    }

    public Transaccion mayorTransaccion(){
        return this.transaccionesOrdenadasPorMonto.maximo();
    }

    public Transaccion borrarMayorTransaccion(){
        boolean hayCreacion = this.transaccionesOrdenadasPorId[0].sigueActiva() ?
                            this.transaccionesOrdenadasPorId[0].transaccion().id_comprador() == 0 :
                            false;
        // actualizo monto medio y transacciones por monto
        int n = hayCreacion ? this.transaccionesOrdenadasPorMonto.size()-1 : this.transaccionesOrdenadasPorMonto.size();
        Transaccion maxima = this.transaccionesOrdenadasPorMonto.sacarMaximo();
        this.montoMedio = (this.montoMedio*n - maxima.monto())/(n-1);

        // actualizo las transacciones por id
        this.transaccionesOrdenadasPorId[maxima.id()].borradoLogico();

        return maxima;


        // falla actualizar monto medio
    }
    
}
    
