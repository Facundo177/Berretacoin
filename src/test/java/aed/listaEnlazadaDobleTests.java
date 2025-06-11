package aed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


//TEST LISTAENLAZADA

public class listaEnlazadaDobleTests{
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
        
        ListaEnlazadaDoble<Integer>.Nodo nodo1 = lista.agregarAtras(1);
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
        
       ListaEnlazadaDoble<Integer>.Nodo nodo22 = lista.agregarAtras(22);
       ListaEnlazadaDoble<Integer>.Nodo nodo31 =lista.agregarAtras(31);
       ListaEnlazadaDoble<Integer>.Nodo nodo0 =lista.agregarAtras(0);
       
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
        ListaEnlazadaDoble<Integer>.Nodo nodo12 =lista.agregarAtras(12);
        ListaEnlazadaDoble<Integer>.Nodo nodo23 =lista.agregarAtras(23);
        ListaEnlazadaDoble<Integer>.Nodo nodo34 =lista.agregarAtras(34);
        ListaEnlazadaDoble<Integer>.Nodo nodo45 =lista.agregarAtras(45);

        //elimino al inicio
        lista.eliminarNodo(nodo12);

        assertEquals(3, lista.longitud());
        assertEquals(23, lista.obtener(0));
        assertEquals(45, lista.obtenerUltimo());
        assertTrue(nodo23.ant == null);
        assertTrue(nodo23.sig == nodo34);
        assertTrue(nodo34.ant == nodo23);
        assertTrue(nodo45.ant == nodo34);
        assertTrue(nodo45.sig == null);

        //elimino en el medio
        lista.eliminarNodo(nodo34);

        assertTrue(nodo23.sig==nodo45);
        assertTrue(nodo45.ant==nodo23);
        assertEquals(45, lista.obtenerUltimo());
        assertEquals(2, lista.longitud());

        lista.eliminarNodo(nodo45);
        lista.eliminarNodo(nodo23);
        //lista vacia
        assertEquals(0, lista.longitud());
        Iterador<Integer> iterador = lista.iterador();
        assertFalse(iterador.haySiguiente());

    }

}


//TEST HANDLE