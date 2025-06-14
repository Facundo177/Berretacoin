package aed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import aed.ListaEnlazadaDoble.Nodo;


//TEST LISTAENLAZADA

public class ListaEnlazadaDobleTests{
    @Test
    void listaVacia() {
        ListaEnlazadaDoble<Integer> lista = new ListaEnlazadaDoble<>();
        Iterador<Integer> iterador = lista.iterador();

        assertEquals(0, lista.longitud());
        assertFalse(iterador.haySiguiente());

    }

    @Test
    void unSoloElemento() {
        ListaEnlazadaDoble<Integer> lista = new ListaEnlazadaDoble<>();
        
        Nodo nodo1 = (Nodo )lista.agregarAtras(1).obtenerPuntero();
        Iterador<Integer> iterador = lista.iterador();

        assertEquals(1, lista.longitud());
        assertEquals(1, lista.obtener(0));
        assertEquals(1, lista.obtenerUltimo());
        assertEquals(1, iterador.siguiente());
        assertEquals(null, nodo1.sig);
        assertEquals(null, nodo1.ant);
        assertFalse(iterador.haySiguiente());
    }

    @Test
    void variosElementos() {
        ListaEnlazadaDoble<Integer> lista = new ListaEnlazadaDoble<>();

        Nodo nodo22 = (Nodo) lista.agregarAtras(22).obtenerPuntero();
        Nodo nodo31 = (Nodo) lista.agregarAtras(31).obtenerPuntero();
        Nodo nodo0 = (Nodo) lista.agregarAtras(0).obtenerPuntero();
       
        assertEquals(3, lista.longitud());
        assertEquals(22, lista.obtener(0));
        assertEquals(0, lista.obtenerUltimo());
        assertEquals(31, lista.obtener(1));
        assertTrue(nodo22.sig == nodo31);
        assertTrue(nodo22.ant == null);
        assertTrue(nodo31.sig == nodo0);
        assertTrue(nodo31.ant == nodo22);
        assertTrue(nodo0.sig == null);
        assertTrue(nodo0.ant == nodo31);
    }

    @Test
    void eliminarNodos() {
        ListaEnlazadaDoble<Integer> lista = new ListaEnlazadaDoble<>();
        Handle h12 = lista.agregarAtras(12);
        Handle h23 = lista.agregarAtras(23);
        Handle h34 = lista.agregarAtras(34);
        Handle h45 = lista.agregarAtras(45);
        Nodo nodo12 = (Nodo) h12.obtenerPuntero();
        Nodo nodo23 = (Nodo) h23.obtenerPuntero();
        Nodo nodo34 = (Nodo) h34.obtenerPuntero();
        Nodo nodo45 = (Nodo) h45.obtenerPuntero();
        Handle hnull = new Handle(2);

        //elimino un null
        int l1 = lista.longitud();
        lista.eliminarNodo(null);
        assertEquals(l1, lista.longitud());

        // elimino un Handle no nulo cuyo puntero a un nodo es null
        int l2 = lista.longitud();
        lista.eliminarNodo(hnull);
        assertEquals(l2, lista.longitud());


        //elimino al inicio
        lista.eliminarNodo(h12);

        assertEquals(3, lista.longitud());
        assertEquals(23, lista.obtener(0));
        assertEquals(45, lista.obtenerUltimo());
        assertTrue(nodo23.ant == null);
        assertTrue(nodo23.sig == nodo34);
        assertTrue(nodo34.ant == nodo23);
        assertTrue(nodo45.ant == nodo34);
        assertTrue(nodo45.sig == null);

        //elimino en el medio
        lista.eliminarNodo(h34);

        assertTrue(nodo23.sig==nodo45);
        assertTrue(nodo45.ant==nodo23);
        assertEquals(45, lista.obtenerUltimo());
        assertEquals(2, lista.longitud());

        lista.eliminarNodo(h45);
        lista.eliminarNodo(h23);
        //lista vacia
        assertEquals(0, lista.longitud());
        Iterador<Integer> iterador = lista.iterador();
        assertFalse(iterador.haySiguiente());

        
    }

}
