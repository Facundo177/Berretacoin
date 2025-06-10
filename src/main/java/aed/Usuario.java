package aed;

public class Usuario implements Comparable<Usuario> {
    private int id;
    public int saldo;

    public Usuario(int id){
        this.id = id;
        this.saldo = 0;
    }

    @Override
    public boolean equals(Object otro){
        if (otro == null){
            return false;
        } else if (otro.getClass() != this.getClass()) {
            return false;
        } else{
            Usuario usuario = (Usuario) otro;
            boolean sonIguales = (this.id == usuario.id) && 
                                (this.saldo == usuario.saldo);
            return sonIguales;
        }
    }

    // da positivo cuando el saldo es mayor, o en el caso de empate, cuando el id es menor
    @Override
    public int compareTo(Usuario otro) {
        if(otro == null){
            String mensajeDeError = "No puede compararse con null";
            throw new IllegalArgumentException(mensajeDeError);
        }
        if ((this.saldo - otro.saldo) == 0){
            return otro.id - this.id;
        }
        return this.saldo - otro.saldo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }


    
}
