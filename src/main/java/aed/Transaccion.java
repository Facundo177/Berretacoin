package aed;

public class Transaccion implements Comparable<Transaccion> {
    private int id;
    private int id_comprador;
    private int id_vendedor;
    private int monto;

    public Transaccion(int id, int id_comprador, int id_vendedor, int monto) {
        this.id = id;
        this.id_comprador = id_comprador;
        this.id_vendedor = id_vendedor;
        this.monto = monto;
    }

    // Si lo que paso por parámetro es mayor, devuelve un negativo
    @Override
    public int compareTo(Transaccion otro) {
        if(otro == null){
            String mensajeDeError = "No puede compararse con null";
            throw new IllegalArgumentException(mensajeDeError);
        }
        if ((this.monto - otro.monto) == 0){
            return otro.id - this.id;
        }
        return this.monto - otro.monto;
    }


    @Override
    public boolean equals(Object otro){
        if (otro == null){
            return false;
        } else if (otro.getClass() != this.getClass()) {
            return false;
        } else{
            Transaccion ts = (Transaccion) otro;
            boolean sonIguales = (this.id == ts.id) && 
                                (this.id_comprador == ts.id_comprador) && 
                                (this.id_vendedor == ts.id_vendedor) && 
                                (this.monto == ts.monto);
            return sonIguales;
        }

    }

    public int monto() {
        return monto;
    }

    public int id_comprador() {
        return id_comprador;
    }
    
    public int id_vendedor() {
        return id_vendedor;
    }

    public int id() {
        return id;
    }


    public Transaccion copiar(){
        return new Transaccion(this.id, this.id_comprador, this.id_vendedor, this.monto);
    }
}