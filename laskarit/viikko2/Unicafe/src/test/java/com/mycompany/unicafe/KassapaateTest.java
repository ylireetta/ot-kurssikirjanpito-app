/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ylireett
 */
public class KassapaateTest {
    Kassapaate kassa;
    Maksukortti kortti;
    
    @Before
    public void setup() {
        this.kassa = new Kassapaate();
        this.kortti = new Maksukortti(1000);
    }
    
    @Test
    public void luonninJalkeenOikeaRahamaara() {
        // HUOM rahamäärä on sentteinä, eli kassassa on 1000 €
        assertTrue(this.kassa.kassassaRahaa() == 100000);
    }
    
    @Test
    public void luonninJalkeenEiMyytyjaLounaita() {
        int lounaitaYhteensa = this.kassa.edullisiaLounaitaMyyty() + this.kassa.maukkaitaLounaitaMyyty();
        assertTrue(lounaitaYhteensa == 0);
    }
    
    // // <editor-fold defaultstate="collapsed" desc="Käteisostot">
    
    @Test
    public void kateinenEdullinenLounasKasvattaaKassaa() {
        this.kassa.syoEdullisesti(240);
        
        assertTrue(this.kassa.kassassaRahaa() == 100240);
    }
    
    @Test
    public void kateinenEdullinenLounasKasvattaaMyytyjenLounaidenMaaraa() {
        this.kassa.syoEdullisesti(240);
        
        assertTrue(this.kassa.edullisiaLounaitaMyyty() == 1);
    }
    
    @Test
    public void kateinenMaukasLounasKasvattaaKassaa() {
        this.kassa.syoMaukkaasti(400);
        
        assertTrue(this.kassa.kassassaRahaa() == 100400);
    }
    
    @Test
    public void kateinenMaukasLounasKasvattaaMyytyjenLounaidenMaaraa() {
        this.kassa.syoMaukkaasti(400);
        
        assertTrue(this.kassa.maukkaitaLounaitaMyyty() == 1);
    }
    
    @Test
    public void kateinenKassasaldoEiMuutuJosEdullisenMaksuEiRiita() {
        this.kassa.syoEdullisesti(100);
        
        assertTrue(this.kassa.kassassaRahaa() == 100000);
    }
    
    @Test
    public void kateinenVaihtorahatJosEdullisenMaksuEiRiita() {
        assertTrue(this.kassa.syoEdullisesti(100) == 100);
    }
    
    @Test
    public void kateinenMyytyjenLounaidenMaaraEiMuutuJosEdullisenMaksuEiRiita() {
        this.kassa.syoEdullisesti(100);
        
        assertTrue(this.kassa.edullisiaLounaitaMyyty() == 0);
    }
    
    @Test
    public void kateinenKassasaldoEiMuutuJosMaukkaanMaksuEiRiita() {
        this.kassa.syoMaukkaasti(100);
        
        assertTrue(this.kassa.kassassaRahaa() == 100000);
    }
    
    @Test
    public void kateinenVaihtorahatJosMaukkaanMaksuEiRiita() {
        assertTrue(this.kassa.syoMaukkaasti(100) == 100);
    }
    
    @Test
    public void kateinenMyytyjenLounaidenMaaraEiMuutuJosMaukkaanMaksuEiRiita() {
        this.kassa.syoMaukkaasti(100);
        
        assertTrue(this.kassa.maukkaitaLounaitaMyyty() == 0);
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Korttiostot">
    @Test
    public void korttiKassasaldoEiMuutuKorttimaksuissa() {
        this.kassa.syoEdullisesti(this.kortti);
        this.kassa.syoMaukkaasti(this.kortti);
        
        assertTrue(this.kassa.kassassaRahaa() == 100000);
    }
    
    @Test
    public void korttiEdullinenLounasKasvattaaMyytyjenLounaidenMaaraa() {
        this.kassa.syoEdullisesti(this.kortti);
        
        assertTrue(this.kassa.edullisiaLounaitaMyyty() == 1);
    }
    
    @Test
    public void korttiMaukasLounasKasvattaaMyytyjenLounaidenMaaraa() {
        this.kassa.syoMaukkaasti(this.kortti);
        
        assertTrue(this.kassa.maukkaitaLounaitaMyyty() == 1);
    }
    
    @Test
    public void korttiEdullinenLounasVeloitetaanKortilta() {
        this.kassa.syoEdullisesti(this.kortti);
        
        assertTrue(this.kortti.saldo() == (1000 - 240));
    }
    
    @Test
    public void korttiPalautetaanTrueEdullisenOnnistuessa() {
        assertTrue(this.kassa.syoEdullisesti(this.kortti));
    }
    
    @Test
    public void korttiMaukasLounasVeloitetaanKortilta() {
        this.kassa.syoMaukkaasti(this.kortti);
        
        assertTrue(this.kortti.saldo() == (1000 - 400));
    }
    
    @Test
    public void korttiPalautetaanTrueMaukkaanOnnistuessa() {
        assertTrue(this.kassa.syoMaukkaasti(this.kortti));
    }
        
    @Test
    public void korttiPalautetaanFalseEdullisenEpaonnistuessa() {
        this.kassa.syoMaukkaasti(this.kortti);
        this.kassa.syoMaukkaasti(this.kortti);
        
        assertFalse(this.kassa.syoEdullisesti(this.kortti));
    }
    
    @Test
    public void korttiMyytyjenLounaidenMaaraEiMuutuJosEdullisenMaksuEiRiita() {
        this.kassa.syoMaukkaasti(this.kortti);
        this.kassa.syoMaukkaasti(this.kortti);
        this.kassa.syoEdullisesti(this.kortti);
        
        assertTrue(this.kassa.edullisiaLounaitaMyyty() == 0);
    }
    
    @Test
    public void korttiPalautetaanFalseMaukkaanEpaonnistuessa() {
        this.kassa.syoMaukkaasti(this.kortti);
        this.kassa.syoMaukkaasti(this.kortti);
        
        assertFalse(this.kassa.syoMaukkaasti(this.kortti));
    }
    
    @Test
    public void korttiMyytyjenLounaidenMaaraEiMuutuJosMaukkaanMaksuEiRiita() {
        this.kassa.syoEdullisesti(this.kortti);
        this.kassa.syoEdullisesti(this.kortti);
        this.kassa.syoEdullisesti(this.kortti);
        this.kassa.syoMaukkaasti(this.kortti);
        
        assertTrue(this.kassa.maukkaitaLounaitaMyyty() == 0);
    }
    
    @Test
    public void korttiKortinSaldoEiMuutuEdullisenEpaonnistuessa() {
        this.kassa.syoMaukkaasti(this.kortti);
        this.kassa.syoMaukkaasti(this.kortti);
        this.kassa.syoEdullisesti(this.kortti);
        
        assertTrue(this.kortti.saldo() == 200);
    }
    
    @Test
    public void korttiKortinSaldoEiMuutuMaukkaanEpaonnistuessa() {
        this.kassa.syoMaukkaasti(this.kortti);
        this.kassa.syoMaukkaasti(this.kortti);
        this.kassa.syoMaukkaasti(this.kortti);
        
        assertTrue(this.kortti.saldo() == 200);
    }
    
    @Test
    public void korttiSaldonLatausKasvattaaKortinSaldoa() {
        this.kassa.lataaRahaaKortille(this.kortti, 1000);
        
        assertTrue(this.kortti.saldo() == 2000);
    }
    
    @Test
    public void korttiSaldonLatausKasvattaaKassasaldoa() {
        this.kassa.lataaRahaaKortille(this.kortti, 1000);
        
        assertTrue(this.kassa.kassassaRahaa() == 101000);
    }
    
    @Test
    public void korttiMiinussaldonLatausEiKasvataKassasaldoa() {
        this.kassa.lataaRahaaKortille(this.kortti, -10);
        
        assertTrue(this.kassa.kassassaRahaa() == 100000);
    }
    
    // </editor-fold>
    
}
