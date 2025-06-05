package aed;

import java.util.ArrayList;

public class ColaDePrioridad<T extends Comparable<T>> {
    ArrayList<T> heap;

    public ColaDePrioridad(){
        this.heap = new ArrayList<T>();
    }

    public ColaDePrioridad(T[] s){
        this.heap = new ArrayList<T>(s.length);
        for (T elem : s) {
            this.agregar(elem);
        }
    }

    public T maximo(){
        return this.heap.getFirst();
    }

    public void agregar(T elem){
        if (this.vacia()){
            this.heap.add(elem);
        } else{
            this.heap.add(elem);
            int posElem = this.heap.indexOf(elem);
            int posPadre = this.padre(posElem);
            T padre = this.heap.get(posPadre);
            while (elem.compareTo(padre) > 0) {
                this.heap.set(posPadre, elem);
                this.heap.set(posElem, padre); 
                posElem = posPadre;
                posPadre = this.padre(posElem);
                padre = this.heap.get(posPadre);
            }
        }
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
        T elem = this.heap.get(posElem);
        int hijoDerecho = this.hijoDerecho(posElem);
        int hijoIzquierdo = this.hijoIzquierdo(posElem); 
        if (hijoDerecho >= length && hijoIzquierdo >= length){
            return;
        } else if (hijoDerecho >= length) {
            if (elem.compareTo(this.heap.get(hijoIzquierdo)) < 0){
                T izq = this.heap.get(hijoIzquierdo);
                this.heap.set(posElem, izq);
                this.heap.set(hijoIzquierdo, elem);
            }
        } else {
            T der = this.heap.get(hijoDerecho);
            T izq = this.heap.get(hijoIzquierdo);
            if (elem.compareTo(der) < 0 || elem.compareTo(izq) < 0){
                int hijoMayor = der.compareTo(izq) > 0 ? hijoDerecho : hijoIzquierdo;
                T mayor = this.heap.get(hijoMayor);
                this.heap.set(posElem, mayor);
                this.heap.set(hijoMayor, elem);
                siftDown(hijoMayor);
            }
        }
    }

    public T sacarMaximo(){
        int posMax = 0;
        T max = this.heap.get(posMax);
        T ultimo = this.heap.getLast();
        this.heap.set(posMax, ultimo);
        this.heap.removeLast();
        this.siftDown(posMax);
        
        return max;
    }

    // private void ordenar(int i){
    //     int hijoDerecho = this.hijoDerecho(i);
    //     int hijoIzquierdo = this.hijoIzquierdo(i);
    //     int length = this.heap.size();
    //     if (i >= length) {
    //         return;  
    //     } 
    //     T valor = this.heap.get(i);
    //     if (hijoDerecho >= length && hijoIzquierdo >= length){
    //         return;
    //     } else if (hijoDerecho >= length) {
    //         if (valor.compareTo(this.heap.get(hijoIzquierdo)) < 0){
    //             T izq = this.heap.get(hijoIzquierdo);
    //             this.heap.set(i, izq);
    //             this.heap.set(hijoIzquierdo, valor); 
    //         }
    //     } else {
    //         if (valor.compareTo(this.heap.get(hijoDerecho)) < 0){
    //             T der = this.heap.get(hijoDerecho);
    //             this.heap.set(i, der);
    //             this.heap.set(hijoDerecho, valor); 
    //         }
    //     }
    //     ordenar(hijoDerecho);
    //     ordenar(hijoIzquierdo);
    // }

    public boolean vacia(){
        return this.heap.isEmpty();
    }   

    public int size(){
        return this.heap.size();
    }
    
}
