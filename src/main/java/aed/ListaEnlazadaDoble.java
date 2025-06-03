package aed;

public class ListaEnlazadaDoble<T> implements Secuencia<T> {
    private Nodo primero;
    private Nodo ultimo;
    private int size;

    private class Nodo {
        T valor;
        Nodo sig;
        Nodo ant;

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

    public void agregarAdelante(T elem) {
        Nodo nuevo = new Nodo(elem);
        if (this.primero == null) {
            this.primero = nuevo;
            this.ultimo = nuevo;
        } else {
            nuevo.sig = this.primero;
            this.primero.ant = nuevo;
            this.primero = nuevo;
        }
        this.size++;
    }

    public void agregarAtras(T elem) {
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

    public void eliminar(int i) {
        Nodo actual = this.primero;
        Nodo prev = this.primero;

        for(int j = 0; j < i; j++){
            prev = actual;
            actual = actual.sig;
        }
        if (i == 0) {
            this.primero = actual.sig;
        } else {
            prev.sig = actual.sig;
        }
        this.size--;
    }

    public void modificarPosicion(int indice, T elem) {
        int contador = 0;
        Nodo actual = this.primero;
        while (contador < indice) {
            contador++;
            actual = actual.sig;
        }
        actual.valor = elem;
    }

    public ListaEnlazadaDoble(ListaEnlazadaDoble<T> lista) {
        this.primero = new Nodo(lista.obtener(0));
        Nodo actual = this.primero;
        for(int i = 0; i < lista.longitud()-1; i++){
            actual.sig = new Nodo(lista.obtener(i+1));
            actual = actual.sig;
        }  
        this.size = lista.longitud();
    }
    
    @Override
    public String toString() {
        String res = "[";
        Nodo actual = this.primero;
        while (actual != null) {
            res += actual.valor + ", ";
            actual = actual.sig;
        }
        res = res.substring(0, res.length() - 2) + "]";
        return res;
    }

    private class ListaIterador implements Iterador<T> {
    	int posicion;
        
        ListaIterador() {posicion = 0;}

        public boolean haySiguiente() {
	        return posicion < size;
        }
        
        public boolean hayAnterior() {
	        return posicion > 0;
        }

        public T siguiente() {
            int aux = posicion;
            posicion++;
            return obtener(aux);
        }
        

        public T anterior() {
            posicion--;
            return obtener(posicion);
        }
    }

    public Iterador<T> iterador() {
	    return new ListaIterador();
    }

}
