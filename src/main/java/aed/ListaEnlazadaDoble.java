package aed;

public class ListaEnlazadaDoble<T> {
    private Nodo primero;
    private Nodo ultimo;
    private int size;

    public class Nodo {
        T valor;
        Nodo sig;
        Nodo ant;
        NodoHeap handle;
        
        Nodo(T v) {valor = v;}
    }

    public ListaEnlazadaDoble() {
        this.primero = null;
        this.ultimo = null;
        this.size = 0;
    }

    public int longitud() {
        return this.size;
    }


    public Nodo agregarAtras(T elem) {
        Nodo nuevo = new Nodo(elem);
        if (this.primero == null) {
            this.primero = nuevo;
            this.ultimo = nuevo;
        } else {
            nuevo.ant = this.ultimo;
            this.ultimo.sig = nuevo;
            this.ultimo = nuevo;
        }
        this.size++;
        return nuevo;
    }

    public T obtener(int i) {
        int contador = 0;
        Nodo actual = this.primero;
        while (contador < i) {
            contador++;
            actual = actual.sig;
        }
        return actual.valor;
    }

    public T obtenerUltimo() {
        return this.ultimo.valor;
    }

    public void eliminarNodo(Nodo nodo){
        if (nodo == null){
            return;
        }
        if (nodo.ant != null && nodo.sig != null){
            nodo.ant.sig = nodo.sig;
            nodo.sig.ant = nodo.ant;
            nodo.ant = null;
            nodo.sig = null;
        } else if (nodo.ant != null){
            this.ultimo = nodo.ant;
            this.ultimo.sig = null;
            nodo.ant = null;
        } else if (nodo.sig != null) {
            this.primero = nodo.sig;
            this.primero.ant = null;
            nodo.sig = null;
        } else{
            this.primero = null;
            this.ultimo = null;
        }

        this.size--;
    }

    public ListaEnlazadaDoble(ListaEnlazadaDoble<T> lista) {
        this.primero = new Nodo(lista.obtener(0));
        Nodo actual = this.primero;
        for(int i = 0; i < lista.longitud()-1; i++){
            actual.sig = new Nodo(lista.obtener(i+1));
            actual = actual.sig;
        }  
        this.ultimo = actual;
        this.size = lista.longitud();
    }

    private class ListaIterador implements Iterador<T> {
    	Nodo nodo;
        
        ListaIterador() {this.nodo = ListaEnlazadaDoble.this.primero;}

        public boolean haySiguiente() {
	        return nodo != null;
        }

        public T siguiente() {
            T elem = nodo.valor;
            nodo = nodo.sig;
            return elem;
        }
    }

    public Iterador<T> iterador() {
	    return new ListaIterador();
    }

}
