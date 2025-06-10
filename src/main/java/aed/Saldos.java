package aed;

public class Saldos {
    Usuario[] heap;
    int[] handle;

    // pos = (id_usuario - 1)
    // handle[pos] = posicion del usuario en el heap

    public Saldos(int n_usuarios){
        this.heap = new Usuario[n_usuarios];
        this.handle = new int[n_usuarios];

        for (int i = 0; i < n_usuarios; i++){
            this.heap[i] = new Usuario(i+1);
            this.handle[i] = i;
        }
    }

    public int maximoTenedor(){
        return this.heap[0].getId();
    }

    private int padre(int i){
        return (i-1)/2;
    }

    private int hijoDerecho(int i){
        return 2*i + 2;
    }

    private int hijoIzquierdo(int i){
        return 2*i + 1;
    }

    private void siftDown(int posElem){
        int length = this.heap.length;
        if (posElem >= length){
            return;
        }
        Usuario elem = this.heap[posElem];
        int hijoDerecho = this.hijoDerecho(posElem);
        int hijoIzquierdo = this.hijoIzquierdo(posElem); 
        if (hijoDerecho >= length && hijoIzquierdo >= length){
            return;
        } else if (hijoDerecho >= length) {
            if (elem.compareTo(this.heap[hijoIzquierdo]) < 0){
                intercambio(posElem, hijoIzquierdo);
            }
        } else {
            Usuario der = this.heap[hijoDerecho];
            Usuario izq = this.heap[hijoIzquierdo];
            if (elem.compareTo(der) < 0 || elem.compareTo(izq) < 0){
                int hijoMayor = der.compareTo(izq) > 0 ? hijoDerecho : hijoIzquierdo;
                intercambio(posElem, hijoMayor);
                siftDown(hijoMayor);
            }
        }
    }

    private void siftUp(int posElem){
        int length = this.heap.length;
        if (posElem >= length || posElem <= 0){
            return;
        }
        Usuario elem = this.heap[posElem];
        int posPadre = this.padre(posElem);
        Usuario padre = this.heap[posPadre];
        if (elem.compareTo(padre) > 0) {
            intercambio(posElem, posPadre);
            siftUp(posPadre);
        }
    }
    private void intercambio(int posElem1, int posElem2){
        Usuario usuario = this.heap[posElem1];
        this.heap[posElem1] = this.heap[posElem2];
        this.heap[posElem2] = usuario;
        this.handle[this.heap[posElem1].getId()-1] = posElem1;
        this.handle[this.heap[posElem2].getId()-1] = posElem2;
    }
    // al modificar un saldo reacomodo el heap como corresponda
    public void actualizarSaldo(int id_usuario, int monto){
        int posUsuario = this.handle[id_usuario-1];
        Usuario usuario = this.heap[posUsuario];
        usuario.setSaldo(usuario.getSaldo() + monto);
        siftUp(this.handle[id_usuario-1]);
        siftDown(this.handle[id_usuario-1]);
    }
}
