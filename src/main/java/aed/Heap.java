package aed;

import java.util.ArrayList;

public class Heap<T extends Comparable<T>> {
    private ArrayList<Tupla<T>> heap;

    public Heap() {
        this.heap = new ArrayList<>();
    }

    public Heap(int capacidadInicial){
        this.heap = new ArrayList<>(capacidadInicial);
    }

    public int size(){
        return this.heap.size();
    }

    public boolean vacio(){
        return this.heap.isEmpty();
    }
      
    public T maximo(){
        return this.heap.get(0).elem;
    }

    public Tupla sacarMaximo(){
        int posMax = 0;
        Tupla max = this.heap.get(posMax);
        Tupla ultimo = this.heap.get(this.heap.size()-1);
        this.heap.set(posMax, ultimo);
        this.heap.remove(this.heap.size()-1);
        this.siftDown(posMax);
        
        return max;
    }

    public void floyd(){
        // Algoritmo de Floyd -> O(n)
        // hojas van desde n/2 hasta n-1 (se pueden ignorar) y ordeno los nodos internos
        for (int i = this.heap.size()/2; i >= 0; i--) {
            this.siftDown(i);
        }
    }

    public Handle agregar(T elem){
        int posicion = this.heap.size();
        Handle handle = new Handle(posicion);
        Tupla tupla = new Tupla<>(elem, handle);
        this.heap.add(tupla);
        this.siftUp(posicion);

        return handle;
    }

    public Handle agregarRapido(T elem){
        Handle handle = new Handle(this.heap.size());
        Tupla tupla = new Tupla<>(elem, handle);
        this.heap.add(tupla);

        return handle;
    }

    public Handle agregarRapidoConHandle(T elem, Handle handle){
        handle.modificarPosicion(this.heap.size());
        Tupla tupla = new Tupla<>(elem, handle);
        this.heap.add(tupla);

        return handle;
    }

    public Comparable acceder(Handle handle){
        Tupla tupla = this.heap.get(handle.obtenerPosicion());
        return tupla.elem;
    }

    public void reacomodar(Handle handle){
        this.siftUp(handle.obtenerPosicion());
        this.siftDown(handle.obtenerPosicion());
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
        int length = this.heap.size();
        if (posElem >= length){
            return;
        }
        Tupla elem = this.heap.get(posElem);
        int hijoDerecho = this.hijoDerecho(posElem);
        int hijoIzquierdo = this.hijoIzquierdo(posElem); 
        if (hijoDerecho >= length && hijoIzquierdo >= length){
            return;
        } else if (hijoDerecho >= length) {
            if (elem.compareTo(this.heap.get(hijoIzquierdo)) < 0){
                intercambio(posElem, hijoIzquierdo);
            }
        } else {
            Tupla der = this.heap.get(hijoDerecho);
            Tupla izq = this.heap.get(hijoIzquierdo);
            if (elem.compareTo(der) < 0 || elem.compareTo(izq) < 0){
                int hijoMayor = der.compareTo(izq) > 0 ? hijoDerecho : hijoIzquierdo;
                intercambio(posElem, hijoMayor);
                siftDown(hijoMayor);
            }
        }
    }

    private void siftUp(int posElem){
        int length = this.heap.size();
        if (posElem >= length || posElem <= 0){
            return;
        }
        Tupla elem = this.heap.get(posElem);
        int posPadre = this.padre(posElem);
        Tupla padre = this.heap.get(posPadre);
        if (elem.compareTo(padre) > 0) {
            intercambio(posElem, posPadre);
            siftUp(posPadre);
        }
    }

    private void intercambio(int pos1, int pos2){
        Tupla<T> t1 = this.heap.get(pos1);
        Tupla<T> t2 = this.heap.get(pos2);
        this.heap.set(pos1, t2);
        t2.handle.modificarPosicion(pos1);
        this.heap.set(pos2, t1);
        t1.handle.modificarPosicion(pos2);
    }

    class Tupla<T extends Comparable<T>> implements Comparable<Tupla<T>>{
        private T elem;
        private Handle handle;

        public Tupla(T elem, Handle handle) {
            this.elem = elem;
            this.handle = handle;
        }
        public T getElem() {
            return elem;
        }
        public void setElem(T elem) {
            this.elem = elem;
        }
        public Handle getHandle() {
            return handle;
        }
        public void setHandle(Handle handle) {
            this.handle = handle;
        }

        @Override
        public int compareTo(Tupla<T> o) {
            return this.elem.compareTo(o.elem);
        }
    }

}
