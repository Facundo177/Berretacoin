package aed;

public class TransaccionActiva {
    private Transaccion transaccion;
    private boolean activa;

    public TransaccionActiva(Transaccion transaccion) {
        this.transaccion = transaccion;
        this.activa = true;
    }

    public Transaccion transaccion() {
        return transaccion;
    }

    public boolean sigueActiva() {
        return activa;
    }

    public void borradoLogico() {
        this.activa = false;
    }


}
