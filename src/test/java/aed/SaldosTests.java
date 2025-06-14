package aed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


public class SaldosTests {
    
    @Test
    public void MaxiumoTenedorTests() {
        Saldos saldos = new Saldos(5);
        
        assertEquals(1, saldos.maximoTenedor());
        
        saldos.actualizarSaldo(2, 5);
        assertEquals(2, saldos.maximoTenedor());
        
        saldos.actualizarSaldo(2, -5);
        assertEquals(1, saldos.maximoTenedor());
        
        saldos.actualizarSaldo(4, 10);
        saldos.actualizarSaldo(4, -4);
        saldos.actualizarSaldo(5, -2);
        assertEquals(4, saldos.maximoTenedor());

    }
}
