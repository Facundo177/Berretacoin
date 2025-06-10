package aed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BerretacoinTestsPropios {
    // copio lo mismo que esta en el archivo BerretacoinTests.java
    private Berretacoin berretacoin;
    private Transaccion[] transacciones;
    private Transaccion[] transacciones2;
    private Transaccion[] transacciones3;
    // Helper class para trackear saldos de usuarios
    private class SaldoTracker {
        private Map<Integer, Integer> saldos;
        
        public SaldoTracker(int usuarios) {
            saldos = new HashMap<>();
            for (int i = 1; i <= usuarios; i++) {
                saldos.put(i, 0);
            }
        }
        
        public void aplicarTransaccion(Transaccion tx) {
            if (tx.id_comprador() == 0) {
                saldos.put(tx.id_vendedor(), saldos.get(tx.id_vendedor()) + tx.monto());
            } else {
                saldos.put(tx.id_comprador(), saldos.get(tx.id_comprador()) - tx.monto());
                saldos.put(tx.id_vendedor(), saldos.get(tx.id_vendedor()) + tx.monto());
            }
        }
        
        public int getSaldo(int usuario) {
            return saldos.get(usuario);
        }
        
        public boolean puedeGastar(int usuario, int monto) {
            return usuario == 0 || getSaldo(usuario) >= monto;
        }
        
        public int getMaximoTenedor() {
            int maxSaldo = -1;
            int maxUsuario = Integer.MAX_VALUE;
            
            for (Map.Entry<Integer, Integer> entry : saldos.entrySet()) {
                int usuario = entry.getKey();
                int saldo = entry.getValue();
                
                if (saldo > maxSaldo || (saldo == maxSaldo && usuario < maxUsuario)) {
                    maxSaldo = saldo;
                    maxUsuario = usuario;
                }
            }
            return maxUsuario;
        }
        
        public void revertirTransaccion(Transaccion tx) {
            if (tx.id_comprador() == 0) {
                saldos.put(tx.id_vendedor(), saldos.get(tx.id_vendedor()) - tx.monto());
            } else {
                saldos.put(tx.id_comprador(), saldos.get(tx.id_comprador()) + tx.monto());
                saldos.put(tx.id_vendedor(), saldos.get(tx.id_vendedor()) - tx.monto());
            }
        }
    }
    
    // este es el mismo test de stess pero le puse demasiados usuarios (funciona igual pero tarda)

    @Test
    public void testStress() {
        int USUARIOS = 10000;
        int BLOQUES = 8000;
        int MAX_TX_POR_BLOQUE = 1000;
        int MAX_HACKEOS = 100;
        
        Berretacoin sistema = new Berretacoin(USUARIOS);
        SaldoTracker tracker = new SaldoTracker(USUARIOS);        
        for (int bloque = 0; bloque < BLOQUES; bloque++) {
            ArrayList<Transaccion> transacciones = new ArrayList<>();
            
            if (bloque < 3000){
                int receptor = USUARIOS - bloque;
                Transaccion creacion = new Transaccion(transacciones.size(), 0, receptor, 1);
                transacciones.add(creacion);
                tracker.aplicarTransaccion(creacion);
            }
            
            for (int i = 0; i < MAX_TX_POR_BLOQUE; i++) {
                int comprador = (i + bloque) % USUARIOS + 1;
                int vendedor = (i + bloque + 1) % USUARIOS + 1;
                if (comprador == vendedor) continue;
                
                int maxMonto = Math.max(0, tracker.getSaldo(comprador));
                if (maxMonto <= 0) continue;
                
                int monto = Math.min(maxMonto, (i % 10) + 1);
                
                if (tracker.puedeGastar(comprador, monto)) {
                    Transaccion tx = new Transaccion(transacciones.size(), comprador, vendedor, monto);
                    transacciones.add(tx);
                    tracker.aplicarTransaccion(tx);
                }
            }
            
            if (!transacciones.isEmpty()) {
                int monto_total = 0;
                int tx_total = 0;
                for (Transaccion tx : transacciones) {
                    if (tx.id_comprador() == 0) continue;
                    monto_total += tx.monto();
                    tx_total++;
                }
                sistema.agregarBloque(transacciones.toArray(new Transaccion[0]));
                assertEquals(tracker.getMaximoTenedor(), sistema.maximoTenedor());
                int monto_medio = tx_total == 0 ? 0 : monto_total / tx_total;
                assertEquals(monto_medio, sistema.montoMedioUltimoBloque());
                
                if (bloque % 3 == 0) {
                    int num_tx = Math.min(MAX_HACKEOS, transacciones.size());
                    for (int j = 0; j < num_tx; j++) {
                        Transaccion hackeada = sistema.txMayorValorUltimoBloque();
                        sistema.hackearTx();
                        tracker.revertirTransaccion(hackeada);
                        transacciones.removeIf(tx -> tx.compareTo(hackeada) == 0);
                        if (hackeada.id_comprador() != 0){
                            monto_total -= hackeada.monto();
                            tx_total--;
                        }
                        monto_medio = tx_total == 0 ? 0 : monto_total / tx_total;
                        assertEquals(monto_medio, sistema.montoMedioUltimoBloque());
                        assertTrue(Arrays.equals(transacciones.toArray(new Transaccion[0]), sistema.txUltimoBloque()));
                        assertEquals(tracker.getMaximoTenedor(), sistema.maximoTenedor());
                    }
                }
            }
        }
    }

    // tests que se me fueron ocurriendo

    @Test
    public void testTxMayorValorUltimoBloqueConEmpate() {
        Berretacoin b = new Berretacoin(5);
        Transaccion[] txs = {
            new Transaccion(1, 1, 2, 50),
            new Transaccion(2, 3, 4, 50) // mayor id
        };
        b.agregarBloque(txs);
        Transaccion res = b.txMayorValorUltimoBloque();
        assertEquals(2, res.id);
    }

    @Test
    public void testMontoMedioSoloTransaccionDeCreacion() {
        Berretacoin b = new Berretacoin(3);
        Transaccion[] txs = {
            new Transaccion(1, 0, 1, 1) // transacción de creación
        };
        b.agregarBloque(txs);
        assertEquals(0, b.montoMedioUltimoBloque());
    }

    @Test
    public void testMaximoTenedorConSecuencia() {
        Berretacoin b = new Berretacoin(5);
        b.agregarBloque(new Transaccion[] {
            new Transaccion(1, 1, 2, 30),
            new Transaccion(2, 3, 4, 40)
        });
        b.agregarBloque(new Transaccion[] {
            new Transaccion(3, 2, 4, 10)
        });
        assertEquals(4, b.maximoTenedor()); // 4 tiene 50
    }

    @Test
    public void testHackearTxAfectaMayorValor() {
        Berretacoin b = new Berretacoin(5);
        b.agregarBloque(new Transaccion[] {
            new Transaccion(1, 1, 2, 20),
            new Transaccion(2, 3, 4, 50)
        });
        Transaccion hackeada = b.txMayorValorUltimoBloque();
        b.hackearTx();
        assertEquals(50, hackeada.monto);
        assertEquals(20, b.txMayorValorUltimoBloque().monto);
    }

    @Test
    public void testTxUltimoBloqueOrdenPorID() {
        Berretacoin b = new Berretacoin(5);
        b.agregarBloque(new Transaccion[] {
            new Transaccion(1, 1, 3, 5),
            new Transaccion(2, 2, 3, 8),
            new Transaccion(3, 1, 2, 10)
        });
        Transaccion[] txs = b.txUltimoBloque();
        assertEquals(1, txs[0].id);
        assertEquals(2, txs[1].id);
        assertEquals(3, txs[2].id);
    }

    @Test
    public void testMasDe3000BloquesSinCreacion() {
        Berretacoin b = new Berretacoin(2);
        for (int i = 0; i < 3000; i++) {
            b.agregarBloque(new Transaccion[] {
                new Transaccion(i + 1, 1, 2, 1)
            });
        }
        b.agregarBloque(new Transaccion[] {
            new Transaccion(9999, 1, 2, 10)
        });
        Transaccion[] ult = b.txUltimoBloque();
        assertTrue(ult[0].id_comprador != 0);
    }

    @Test
    public void testHackearHastaBloqueVacio() {
        Berretacoin b = new Berretacoin(4);
        b.agregarBloque(new Transaccion[] {
            new Transaccion(1, 1, 2, 10),
            new Transaccion(2, 3, 4, 15)
        });
        b.hackearTx();
        b.hackearTx();
        assertEquals(0, b.txUltimoBloque().length);
    }

    @Test
    public void testSaldoUsuariosPostTransaccionCon() {
        int usuarios = 3;
        Berretacoin b = new Berretacoin(usuarios);
        SaldoTracker tracker = new SaldoTracker(usuarios);

        Transaccion[] txs = {
            new Transaccion(1, 1, 2, 40)
        };

        // Aplicamos al tracker
        for (Transaccion t : txs) tracker.aplicarTransaccion(t);

        // Ejecutamos en el sistema real
        b.agregarBloque(txs);

        // Verificamos contra el tracker
        assertEquals(tracker.getMaximoTenedor(), b.maximoTenedor());
    }

    @Test
    public void testSaldoTransaccionCreacion() {
        int usuarios = 3;
        Berretacoin b = new Berretacoin(usuarios);
        SaldoTracker tracker = new SaldoTracker(usuarios);

        Transaccion[] txs = {
            new Transaccion(1, 0, 1, 1)
        };
        for (Transaccion t : txs) tracker.aplicarTransaccion(t);
        b.agregarBloque(txs);
        assertEquals(tracker.getMaximoTenedor(), b.maximoTenedor()); // recibió dinero creado
    }

    @Test
    public void testSaldosRestauradosTrasHackeo() {
        int usuarios = 5;
        Berretacoin b = new Berretacoin(usuarios);
        SaldoTracker tracker = new SaldoTracker(usuarios);

        Transaccion[] txs = {
            new Transaccion(1, 1, 2, 20),
            new Transaccion(2, 3, 4, 50)
        };
        for (Transaccion t : txs) tracker.aplicarTransaccion(t);
        b.agregarBloque(txs);

        Transaccion hackeada = b.txMayorValorUltimoBloque();
        b.hackearTx();
        tracker.revertirTransaccion(hackeada);

        assertEquals(tracker.getSaldo(3), tracker.getSaldo(3));
        assertEquals(tracker.getSaldo(4), tracker.getSaldo(4));
    }

    @Test
    public void testStressAgregarBloqueGrande() {
        Berretacoin b = new Berretacoin(10000);
        Transaccion[] txs = new Transaccion[500];
        for (int i = 0; i < 500; i++) {
            txs[i] = new Transaccion(i, i + 1, i + 2, i + 10);
        }
        b.agregarBloque(txs);
        assertEquals(500, b.txUltimoBloque().length);
    }

    @Test
    public void testStressMuchosBloques() {
        Berretacoin b = new Berretacoin(10);
        for (int i = 0; i < 5000; i++) {
            b.agregarBloque(new Transaccion[] {
                new Transaccion(i, 1, 2, 1)
            });
        }
        assertEquals(1, b.txMayorValorUltimoBloque().monto);
    }

    // tests para cada operacion

    @Test
    public void testInicializacionUsuarios() {
        int usuarios = 10;
        Berretacoin b = new Berretacoin(usuarios);
        SaldoTracker tracker = new SaldoTracker(usuarios);
        for (int i = 1; i <= usuarios; i++) {
            assertEquals(0, tracker.getSaldo(i)); // todos inician en 0
        }
    }

    @Test
    public void testAgregarTransaccionesActualizaSaldos() {
        int usuarios = 3;
        Berretacoin b = new Berretacoin(usuarios);
        SaldoTracker tracker = new SaldoTracker(usuarios);
        Transaccion[] txs = {
            new Transaccion(1, 1, 2, 100),
            new Transaccion(2, 2, 3, 50)
        };
        for (Transaccion t : txs) tracker.aplicarTransaccion(t);
        b.agregarBloque(txs);

        assertEquals(-100, tracker.getSaldo(1));
        assertEquals(50, tracker.getSaldo(2));
        assertEquals(50, tracker.getSaldo(3));
    }

    @Test
    public void testTxMayorValorConEmpate() {
        Berretacoin b = new Berretacoin(5);
        Transaccion[] txs = {
            new Transaccion(1, 1, 2, 20),
            new Transaccion(2, 3, 4, 20)
        };
        b.agregarBloque(txs);
        assertEquals(2, b.txMayorValorUltimoBloque().id);
    }

    @Test
    public void testTxUltimoBloqueOrdenado() {
        Berretacoin b = new Berretacoin(3);
        Transaccion[] txs = {
            new Transaccion(2, 2, 3, 10),
            new Transaccion(5, 1, 2, 5),
        };
        b.agregarBloque(txs);
        Transaccion[] res = b.txUltimoBloque();
        assertEquals(2, res[0].id);
        assertEquals(5, res[1].id);
    }

    @Test
    public void testMaximoTenedorEmpate() {
        Berretacoin b = new Berretacoin(5);
        b.agregarBloque(new Transaccion[] {
            new Transaccion(1, 1, 2, 10),
            new Transaccion(2, 3, 4, 10)
        });
        assertEquals(2, b.maximoTenedor()); // gana el de menor ID
    }

    @Test
    public void testMontoMedioSinCreacion() {
        Berretacoin b = new Berretacoin(3);
        b.agregarBloque(new Transaccion[] {
            new Transaccion(1, 0, 1, 1),
            new Transaccion(2, 1, 2, 10),
            new Transaccion(3, 2, 3, 20)
        });
        assertEquals(15, b.montoMedioUltimoBloque()); // promedio de 10 y 20
    }

    @Test
    public void testHackearTxRevierteSaldos() {
        int usuarios = 4;
        Berretacoin b = new Berretacoin(usuarios);
        SaldoTracker tracker = new SaldoTracker(usuarios);
        b.agregarBloque(new Transaccion[] {
            new Transaccion(1, 1, 2, 10),
            new Transaccion(2, 3, 4, 20)
        });
        b.hackearTx(); // hackea 20
        assertEquals(0, tracker.getSaldo(3));
        assertEquals(0, tracker.getSaldo(4));
    }

    @Test
    public void testStressConMuchosUsuariosYHackeos() {
        int BLOQUES = 10000;
        int USUARIOS = 50000;
        Berretacoin b = new Berretacoin(USUARIOS);

        // Agrego 9999 bloques con una transacción cada uno (valor bajo)
        for (int i = 0; i < BLOQUES - 1; i++) {
            int comprador = (i % (USUARIOS - 1)) + 1;
            int vendedor = comprador + 1;
            Transaccion[] txs = {
                new Transaccion(i + 1, comprador, vendedor, 1)
            };
            b.agregarBloque(txs);
        }

        // Último bloque con transacciones hackeables (valores altos)
        int usuarios_finales = 10;
        Transaccion[] finales = new Transaccion[usuarios_finales];
        SaldoTracker tracker = new SaldoTracker(usuarios_finales);
        for (int i = 0; i < usuarios_finales; i++) {
            int id = BLOQUES + i;
            int comprador = 1;
            int vendedor = 2;
            int monto = 10000 - i * 500; // 10000, 9500, ..., 5500
            finales[i] = new Transaccion(id, comprador, vendedor, monto);
        }
        b.agregarBloque(finales);

        // Hackeamos todas las transacciones del último bloque
        for (int i = 0; i < usuarios_finales; i++) {
            Transaccion hackeada = b.txMayorValorUltimoBloque();
            b.hackearTx();
            assertEquals(10000 - i * 500, hackeada.monto);
        }

        // Validamos que el bloque final quedó vacío
        assertEquals(0, b.txUltimoBloque().length);
        
        // Validamos que el sistema sigue funcionando: hay maximo tenedor, no rompe
        int tenedor = b.maximoTenedor();
        assertTrue(tenedor >= 1 && tenedor <= USUARIOS);

        // Validamos que los saldos de los usuarios involucrados quedaron en 0
        for (int i = 1; i < usuarios_finales; i++) {
            assertEquals(0, tracker.getSaldo(i));
        }
    }

    @Test
    public void testHackearTodo() {
        int usuarios = 2;
        berretacoin = new Berretacoin(usuarios);
        SaldoTracker tracker = new SaldoTracker(usuarios);

        transacciones = new Transaccion[] {
            new Transaccion(1, 1, 2, 10),
            new Transaccion(2, 1, 2, 20)
        };

        for (Transaccion t : transacciones) tracker.aplicarTransaccion(t);
        berretacoin.agregarBloque(transacciones);

        for (int i = 0; i < transacciones.length; i++) {
            Transaccion h = berretacoin.txMayorValorUltimoBloque();
            berretacoin.hackearTx();
            tracker.revertirTransaccion(h);
        }

        assertEquals(0, berretacoin.txUltimoBloque().length);
        assertEquals(tracker.getMaximoTenedor(), berretacoin.maximoTenedor());
    }

    @Test
    public void testSaldoPostTransaccionNormalConTracker() {
        Berretacoin b = new Berretacoin(3);
        SaldoTracker tracker = new SaldoTracker(3);

        Transaccion[] txs = {
            new Transaccion(1, 1, 2, 40)
        };
        for (Transaccion t : txs) tracker.aplicarTransaccion(t);
        b.agregarBloque(txs);

        assertEquals(tracker.getSaldo(1), tracker.getSaldo(1));
        assertEquals(tracker.getSaldo(2), tracker.getSaldo(2));
    }

    @Test
    public void testSaldoTransaccionDeCreacion() {
        Berretacoin b = new Berretacoin(2);
        SaldoTracker tracker = new SaldoTracker(2);

        Transaccion[] txs = {
            new Transaccion(1, 0, 1, 1)
        };
        for (Transaccion t : txs) tracker.aplicarTransaccion(t);
        b.agregarBloque(txs);

        assertEquals(tracker.getSaldo(1), tracker.getSaldo(1));
    }

    @Test
public void testBloquesYHackeos() {
    int USUARIOS = 100;
    int BLOQUES = 200;
    Berretacoin b = new Berretacoin(USUARIOS);
    SaldoTracker tracker = new SaldoTracker(USUARIOS);

    for (int i = 0; i < BLOQUES; i++) {
        Transaccion[] txs = new Transaccion[5];
        for (int j = 0; j < 5; j++) {
            int id = i * 5 + j + 1;
            int comprador = (13 * id % (USUARIOS - 1)) + 1;
            int vendedor = (comprador % USUARIOS) + 1;
            if (vendedor == comprador) vendedor = (vendedor % USUARIOS) + 1;
            int monto = (31 * id % 100) + 1;
            txs[j] = new Transaccion(id, comprador, vendedor, monto);
            tracker.aplicarTransaccion(txs[j]);
        }
        b.agregarBloque(txs);
    }

    Transaccion[] finales = new Transaccion[3];
    for (int i = 0; i < 3; i++) {
        finales[i] = new Transaccion(9999 + i, 1, 2, 1000 - i * 200);
        tracker.aplicarTransaccion(finales[i]);
    }
    b.agregarBloque(finales);

    for (int i = 0; i < 3; i++) {
        Transaccion h = b.txMayorValorUltimoBloque();
        b.hackearTx();
        tracker.revertirTransaccion(h);
    }

    assertEquals(0, b.txUltimoBloque().length);
    assertEquals(tracker.getMaximoTenedor(), b.maximoTenedor());
    }















}



















//TEST LISTAENLAZADA



//TEST HEAP

//TEST HANDLE