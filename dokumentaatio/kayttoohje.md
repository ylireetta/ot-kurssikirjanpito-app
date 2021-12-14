# Käyttöohje
## Konfigurointi
Hakemistossa _src/main/resources_ on tiedosto _config.properties_, jonka sisällä on määritelty sekä testauksessa että varsinaisessa ohjelmassa käytettävät tietokantaosoitteet. Tietokantayhteyttä käyttävä tunnus ja salasana on tarkoituksella talletettu GitHubiin, jotta ohjelman testaus olisi mahdollista ilman suurempaa hämminkiä.
## Ohjelman käynnistäminen
Ohjelma käynnistetään jar-tiedostosta (kyseisen tiedoston sisältävässä hakemistossa) seuraavalla komennolla:
```
java -jar ajankaytonseuranta-1.0-SNAPSHOT.jar
```
## Kirjautuminen
Sovelluksen aloitusnäkymässä voi kirjautua sisään olemassa olevalla tunnuksella, luoda uuden tunnuksen tai sulkea ohjelman. Sisäänkirjautuminen onnistuu pelkällä tunnuksella, sillä ohjelmassa ei ole salasananhallintaa.

![Aloitusnäkymä](/dokumentaatio/kuvat/loginscene.png)
## Uuden käyttäjän luominen
Uuden käyttäjän luominen onnistuu sovelluksen alkunäkymästä käsin. Tekstikenttään syötetään haluttu tunnus ja painetaan _Luo uusi käyttäjä_ -nappia. Mikäli käyttäjän luominen onnistuu, ohjelma ilmoittaa siitä, ja käyttäjä pääsee kirjautumaan sisälle painamalla _Kirjaudu sisään_ -nappia.

![Tunnus varattu](/dokumentaatio/kuvat/usernametaken.png)

![Uusi tunnus luotu](/dokumentaatio/kuvat/newusercreated.png)
## Kurssinäkymä
Kurssinäkymästä käsin voidaan lisätä uusia kursseja tietokantaan, käynnistää jo lisättyjen kurssien kohdalla ajanotto, tarkastella aikaavievimpien kurssien top-listaa, poistaa omia kursseja tai tarkastella omaa kurssiyhteenvetoa. Käyttäjä voi kirjautua ulos painamalla _Kirjaudu ulos_ -nappia.
### Uuden kurssin luominen
_Lisää uusi kurssi_ -napista aukeaa uusi näkymä, jossa pääsee syöttämään uuden kurssin tiedot tietokantaan. Vaadittuja tietoja ovat kurssin nimi ja opintopistemäärä (positiivinen kokonaisluku). _Palaa takaisin_ -napista pääsee takaisin kurssinäkymään.

![Uuden kurssin luominen](/dokumentaatio/kuvat/addnewcourse.png)
### Kurssitiedot ja ajanotto
Käyttäjä voi tarkastella yksittäisen kurssin tietoja valitsemalla haluamansa kurssin alasvetovalikosta. Kun kurssi on valittu, käyttäjä voi käynnistää ajanoton painamalla _Käynnistä ajanotto_. Ajastin pysähtyy, kun painetaan nappia _Lopeta ajanotto_.

![Kurssinäkymä](/dokumentaatio/kuvat/courseselected.png)
### Aikaavievimpien kurssien top 5
Kurssinäkymän napista _Kurssien top-lista_ pääsee uuteen näkymään, johon on listattu tietokannasta viisi aikaavievintä kurssia. Nämä kurssit eivät välttämättä ole sisäänkirjautuneen käyttäjän itse lisäämiä, vaan top-lista muodostuu koko kurssitietokannan pohjalta.

![Top 5 -kurssien näkymä](/dokumentaatio/kuvat/topcourses.png)
### Kurssien poistaminen
Käyttäjä voi poistaa joko yksittäisen kurssin tai kaikki kurssit kerralla. Käyttäjälle esitetään varmistusikkuna, ennen kuin poisto-operaatio viimeistellään.

![Kurssien poistaminen](/dokumentaatio/kuvat/deletecoursescene.png)

![Yhden kurssin poistaminen](/dokumentaatio/kuvat/deleteone.png)

![Kaikkien kurssien poistaminen](/dokumentaatio/kuvat/deleteall.png)

### Kurssien yhteenveto
Yhteenvetonäkymässä käyttäjä voi tarkastella lisäämiensä kurssien viemää aikaa kokonaisuutena piirakkadiagrammista.

![Kurssien yhteenvetonäkymä](/dokumentaatio/kuvat/coursedatascene.png)
