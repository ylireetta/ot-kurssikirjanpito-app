# Käyttöohje
## Konfigurointi
Ohjelman juurihakemistossa on tiedosto _config.properties_, jonka sisällä on määritelty sekä testauksessa että varsinaisessa ohjelmassa käytettävät tietokantaosoitteet. Tietokantayhteyttä käyttävä tunnus ja salasana on tarkoituksella talletettu GitHubiin, jotta ohjelman testaus olisi mahdollista ilman suurempaa hämminkiä.
## Ohjelman käynnistäminen
TODO: Ohje tulossa siinä vaiheessa, kun jar-tiedosto on luotu!
## Kirjautuminen
Sovelluksen aloitusnäkymässä voi kirjautua sisään olemassa olevalla tunnuksella, luoda uuden tunnuksen tai sulkea ohjelman. Sisäänkirjautuminen onnistuu pelkällä tunnuksella, sillä ohjelmassa ei ole salasananhallintaa.
## Uuden käyttäjän luominen
Uuden käyttäjän luominen onnistuu sovelluksen alkunäkymästä käsin. Tekstikenttään syötetään haluttu tunnus ja painetaan _Luo uusi käyttäjä_ -nappia. Mikäli käyttäjän luominen onnistuu, ohjelma ilmoittaa siitä, ja käyttäjä pääsee kirjautumaan sisälle painamalla _Kirjaudu sisään_ -nappia.
## Kurssinäkymä
Kurssinäkymästä käsin voidaan lisätä uusia kursseja tietokantaan, käynnistää jo lisättyjen kurssien kohdalla ajanotto tai tarkastella aikaavievimpien kurssien top-listaa. Käyttäjä voi kirjautua ulos painamalla _Kirjaudu ulos_ -nappia.
### Uuden kurssin luominen
_Lisää uusi kurssi_ -napista aukeaa uusi näkymä, jossa pääsee syöttämään uuden kurssin tiedot tietokantaan. Vaadittuja tietoja ovat kurssin nimi ja opintopistemäärä (positiivinen kokonaisluku). _Palaa takaisin_ -napista pääsee takaisin kurssinäkymään.
### Kurssitiedot ja ajanotto
Käyttäjä voi tarkastella yksittäisen kurssin tietoja valitsemalla haluamansa kurssin alasvetovalikosta. Kun kurssi on valittu, käyttäjä voi käynnistää ajanoton painamalla _Käynnistä ajanotto_. Ajastin pysähtyy, kun painetaan nappia _Lopeta ajanotto_.
### Aikaavievimpien kurssien top 5
Kurssinäkymän napista _Kurssien top-lista_ pääsee uuteen näkymään, johon on listattu tietokannasta viisi aikaavievintä kurssia. Nämä kurssit eivät välttämättä ole sisäänkirjautuneen käyttäjän itse lisäämiä, vaan top-lista muodostuu koko kurssitietokannan pohjalta.
