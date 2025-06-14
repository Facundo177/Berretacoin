package aed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


public class HeapTests {
    
    @Test
    public void agregar() {
        Integer[] numeros = {5,9,32,7,679,35,213};
        int capacidadInicial = numeros.length;
        Heap heapVacio = new Heap();
        Heap heap = new Heap(capacidadInicial);

        assertTrue(heapVacio.vacio());
        assertTrue(heapVacio.size() == 0);
        assertTrue(heap.vacio());
        assertTrue(heap.size() == 0);

        // usamos el agregado con posicionamiento automático en uno y el agregado rápido en otro
        for (Integer n : numeros) {
            heapVacio.agregar(n);
            heap.agregarRapido(n);
        }
        // acomodamos el que agregamos rápido
        heap.floyd();

        // comparamos que de ambas formas nos haya dado el mismo resultado
        Integer[] resultado1 = new Integer[capacidadInicial];
        Integer[] resultado2 = new Integer[capacidadInicial];

        int i = 0;
        while (!heap.vacio()) {
            assertEquals(heapVacio.maximo(), heap.maximo());
            resultado1[i] = (Integer) heapVacio.sacarMaximo().getElem();
            resultado2[i] = (Integer) heap.sacarMaximo().getElem();
            i++;
        }

        for (int j = 0; j < resultado1.length; j++) {
            assertEquals(resultado1[j], resultado2[j]);
        }

    }

}
