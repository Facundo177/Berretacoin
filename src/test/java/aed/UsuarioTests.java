package aed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class UsuarioTests {

    @Test
    public void TestCompareTo () {
        Usuario usuario1 = new Usuario(1);
        Usuario usuario2 = new Usuario(2);
        Usuario usuario3 = null;

        usuario1.setSaldo(20);
        usuario2.setSaldo(10);

        assertTrue(usuario1.compareTo(usuario2) > 0);

        usuario1.setSaldo(10);

        assertTrue(usuario1.compareTo(usuario2) > 0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuario1.compareTo(usuario3);
        });
        // Verificar el mensaje de la excepción
        assertEquals("No puede compararse con null", exception.getMessage());
    }
    

    @Test
    public void TestEquals() {
        Usuario usuario1 = new Usuario(1);
        Usuario usuario2 = new Usuario(2);

        usuario1.setSaldo(10);
        usuario2.setSaldo(10);
        
        assertFalse(usuario1.equals(usuario2));

        usuario2.setId(1);

        assertTrue(usuario1.equals(usuario2));
        assertFalse(usuario1 == usuario2);
        assertTrue(usuario1.equals(usuario1));

        usuario2.setSaldo(20);

        assertFalse(usuario1.equals(usuario2));

        Transaccion transaccion = new Transaccion(0, 0, 1, 1);

        assertFalse(usuario1.equals(null));
        assertFalse(usuario1.equals(transaccion));
        
    }
}
