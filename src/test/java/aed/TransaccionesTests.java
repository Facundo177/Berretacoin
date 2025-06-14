package aed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


public class TransaccionesTests {
    
    @Test
    public void TestCompareTo() {
        Transaccion t1 = new Transaccion (1, 6, 1, 10);
        Transaccion t2 = new Transaccion (2, 7, 2, 20);
        Transaccion t3 = new Transaccion (3, 8, 3, 10);
        Transaccion t4 = null;

        assertTrue(t3.compareTo(t1) > 0); // igual monto pero t3.id > t1.id
        assertTrue(t1.compareTo(t2) < 0); //monto de t2 > t1
        assertEquals(0, t3.compareTo(t3));
        

         IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        t1.compareTo(t4);
    });
        // Verificar el mensaje de la excepción
        assertEquals("No puede compararse con null", exception.getMessage());
    }

    @Test
    public void TestEquals() {
        Transaccion t1 = new Transaccion (1, 2, 1, 20);
        Transaccion t2 = new Transaccion (1, 2, 1, 20);
        Transaccion t3 = new Transaccion (1, 2, 1, 10);
        Transaccion t4 = new Transaccion (1, 2, 3, 20);
        Transaccion t5 = new Transaccion (1, 6, 3, 20);
        Transaccion t6 = new Transaccion (2, 6, 3, 20);
        Transaccion tn = null;
        
        assertTrue(t1.equals(t2));
        assertFalse(t1.equals(t3));
        assertFalse(t4.equals(t1));
        assertFalse(t5.equals(t4));
        assertFalse(t5.equals(t6));
        assertFalse(t4.equals(tn));

        Usuario usuario = new Usuario(0);

        assertFalse(t1.equals(usuario));
    }

    @Test
    public void TestCopiar() {
        Transaccion t1 = new Transaccion (1, 2, 1, 20);

        assertFalse(t1 == t1.copiar());
        assertTrue(t1.equals(t1.copiar()));
        assertEquals(0, t1.compareTo(t1.copiar()));
    }

    @Test
    public void TestDeCreacion() {
        Transaccion t1 = new Transaccion (1, 0, 1, 1);
        Transaccion t2 = new Transaccion (1, 2, 1, 1);
        
        assertTrue(t1.esDeCreacion());
        assertFalse(t2.esDeCreacion());
    }
}

