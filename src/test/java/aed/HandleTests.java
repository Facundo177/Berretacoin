package aed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


public class HandleTests {
    
    @Test
    public void metodosBasicos() {
        // constructores
        int posicion = 3;
        Usuario objeto1 = new Usuario(4);
        Transaccion objeto2 = new Transaccion(0, 0, 0, 0);
        Handle h1 = new Handle(posicion);
        Handle h2 = new Handle(objeto1);

        // getters
        assertEquals(3, h1.obtenerPosicion());
        assertTrue(h1.obtenerPuntero() == null);

        assertEquals(objeto1, h2.obtenerPuntero());
        assertTrue(h2.obtenerPosicion() == 0);

        // setters
        h1.modificarPosicion(5);
        assertEquals(5, h1.obtenerPosicion());

        h1.modificarPuntero(objeto1);
        assertTrue(h1.obtenerPuntero() == objeto1);

        h2.modificarPosicion(7);
        assertEquals(7, h2.obtenerPosicion());

        h2.modificarPuntero(objeto2);
        assertTrue(h2.obtenerPuntero() == objeto2);

    }
}