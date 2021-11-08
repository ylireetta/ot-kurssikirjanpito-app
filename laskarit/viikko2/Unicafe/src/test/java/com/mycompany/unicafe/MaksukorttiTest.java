package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void kortinSaldoAlussaOikein() {
        assertTrue(kortti.saldo() == 10);
    }
    
    @Test
    public void rahanLataaminenKasvattaaSaldoaOikein() {
        kortti.lataaRahaa(10);
        
        assertTrue(kortti.saldo() == 20);
    }
    
    @Test
    public void saldoVaheneeOikeinJosRahaaTarpeeksi() {
        kortti.otaRahaa(5);
        
        assertTrue(kortti.saldo() == 5);
    }
    
    @Test
    public void saldoEiMuutuJosRahaaEiTarpeeksi() {
        kortti.otaRahaa(20);
        
        assertTrue(kortti.saldo() == 10);
    }
    
    @Test
    public void trueJosRahaRiittaa() {
        assertTrue(kortti.otaRahaa(8));
    }
    
    @Test
    public void falseJosRahaEiRiita() {
        assertFalse(kortti.otaRahaa(80));
    }
    
    @Test
    public void kortinTulostusToimii() {
        assertEquals("saldo: 0.10", kortti.toString());
    }
}
