package aed;

public class Saldos {
    private Heap<Usuario> heap;
    private Handle[] handle;

    // pos = (id_usuario - 1)
    // handle[pos] = posicion del usuario en el heap

    public Saldos(int n_usuarios){
        this.heap = new Heap<>(n_usuarios);
        this.handle = new Handle[n_usuarios];

        for (int i = 0; i < n_usuarios; i++){
            Handle handle = this.heap.agregarRapido(new Usuario(i+1));
            this.handle[i] = handle;
        }
    }

    public int maximoTenedor(){
        return this.heap.maximo().getId();
    }

    public void actualizarSaldo(int id_usuario, int monto){
        Handle handle = this.handle[id_usuario-1];
        Usuario usuario = (Usuario) this.heap.acceder(handle);
        usuario.setSaldo(usuario.getSaldo() + monto);
        this.heap.reacomodar(handle);
    }
}
