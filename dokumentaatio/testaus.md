# Testausdokumentti

## Yksikkö- ja integraatiotestaus
Ohjelman testaus on toteutettu JUnit- että manuaalitesteillä. Automaattitestauksella on hoidettu sekä yksikkö- että integraatiotestaus.

### Sovelluslogiikka
_ajankaytonseuranta.domain_-pakkauksessa sijaitseva sovelluslogiikka on testattu syöttämällä testattavalle _TimeManagementService_-oliolle _FakeCourseDao_- ja _FakeUserDao_-luokkien oliot. _Fake_-alkuiset DAO-luokat käyttävät tietokannan testipuolta, joten kanta voidaan aina testien päätteeksi tyhjentää kokonaan vaarantamatta varsinaista ohjelman käyttämää dataa. _Fake_-oliot toteuttavat normaaliin tapaan _UserDao_- ja _CourseDao_-rajapinnat.

Sovelluslogiikan testaaminen keskittyy nimenomaan _TimeManagementService_-luokan olion testaamiseen, mutta myös _User_- ja _Course_-luokalle on tehty muutama testi varmistamaan, että niiden metodit toimivat odotetulla tavalla.

### DAO-luokat
Sekä _ConcreteUserDao_ että _ConcreteCourseDao_ on testattu automaattitestein. Testeissä on jätetty sovelluslogiikasta huolehtiva _TimeManagementService_ kokonaan pois ja käytetty sen sijaan DAO-luokkien metodeja suoraan.

### Testauskattavuus
Testauksen rivikattavuus on 94 % ja haarautumakattavuus 76 %. Jälkimmäinen luku johtuu pääosin koodiin jääneistä try-catch-blokeista, sillä en päässyt testeissäni catch-blokkeihin lainkaan. Lisäksi esimerkiksi equals-metodien yhteydessä jäi muutama haara testaamatta.

![Testikattavuus](/dokumentaatio/kuvat/testcoverage.jpg)

Kaikki käyttöliittymän muodostamiseen liittyvät luokat on jätetty testien ulkopuolelle.

## Järjestelmätestaus
Järjestelmätestaus on tehty manuaalisesti. Testauksessa on pyritty perustoiminnallisuuden testaamisen lisäksi käyttämään kelvottomia syötteitä ja käyttäytymään odottamattomalla tavalla, jotta mahdolliset virhetilanteet tulisivat esille.

### Asennus ja konfigurointi
Sovellusta on testattu vain Linux-ympäristössä. Sovelluksen hakemistorakenne on sisältänyt _config.properties_-tiedoston, joka sisältää tietokantayhteyden luomisessa tarvittavat tiedot.

### Toiminnallisuudet
Ohjelma on pyritty testaamaan vaatimusmäärittelyssä lueteltuja käyttötapauksia silmälläpitäen. Testauksessa on käytetty niin kelvollisia kuin epäkelpojakin syötteitä.

## Huomiotta jääneet seikat
Sovelluksen käyttöliittymää ei ole testattu kovinkaan laajaa ajankäyttöä vaatineilla kursseilla. Mikäli kurssiin käytettäisiin esimerkiksi tunteja, näytettäisiin käytetty aika edelleen minuutteina ja sekunteina.

Käyttäjän oma kurssidata (piirakkadiagramminäkymä) on testattu suhteellisen vähäisellä kurssimäärällä, enintään seitsemällä. Jos kurssien määrä kasvaisi paljon, voi olla, että käyttöliittymän visuaalisuus kärsisi. Toisaalta käyttäjällä on mahdollisuus poistaa "vanhoja" kursseja, jolloin datanäkymä voisi pysyä järkevänä.
