# Ohjelmistotekniikka: harjoitustyö
Monien kurssien yhteydessä opiskelijan odotetaan pitävän kirjaa kurssin parissa vietetystä ajasta, ja tämän sovelluksen avulla tehtävä helpottuu (ainakin hieman). Käyttäjä voi lisätä tietokantaan omat kurssinsa ja käynnistää ajanoton aina kun alkaa työskennellä kurssin harjoitustöiden parissa. Ajanoton keskeytyessä syystä tai toisesta (napin painallus, uloskirjautuminen, sovellusikkunan sulkeminen) käytetty aika päivitetään tietokantaan.
## Dokumentaatio
[Vaatimusmäärittely](https://github.com/ylireetta/ot-harjoitustyo/blob/master/dokumentaatio/vaatimusmaarittely.md)

[Arkkitehtuurikuvaus](https://github.com/ylireetta/ot-harjoitustyo/blob/master/dokumentaatio/arkkitehtuuri.md)

[Käyttöohje](https://github.com/ylireetta/ot-harjoitustyo/blob/master/dokumentaatio/kayttoohje.md)

[Tuntikirjanpito](https://github.com/ylireetta/ot-harjoitustyo/blob/master/dokumentaatio/tuntikirjanpito.md)

## Releaset
[Viikko 5](https://github.com/ylireetta/ot-harjoitustyo/releases/tag/viikko5)

## Komentorivikomennot
Koko ohjelman ajamisen pitäisi onnistua seuraavalla komennolla:
```
mvn compile exec:java
```
Jos ohjelman pääluokan tarkentaminen on jostain syystä tarpeen, voi käyttää seuraavaa komentoa:
```
mvn compile exec:java -Decex.mainClass=ajankaytonseuranta.ui.Main
```

### Testaus
Testit ajetaan seuraavalla komennolla:
```
mvn test
```
Testiraportin luominen polun _target/site/jacoco/index.html_ päähän onnistuu seuraavalla komennolla:

```
mvn test jacoco:report
```

### Suoritettavan jarin generointi
Suoritettava jar-tiedosto generoidaan projektin juurihakemistossa komennolla:
```
mvn package
```
Tiedosto _ajankaytonseuranta-1.0-SNAPSHOT.jar_ löytyy _target_-hakemistosta. Em. komennolla luotu jar-tiedosto ajetaan (tiedoston sisältävässä hakemistossa) komennolla:
```
java -jar ajankaytonseuranta-1.0-SNAPSHOT.jar
```

### JavaDoc
JavaDocin generointi polun _target/site/apidocs/index.html_ päähän onnistuu komennolla:
```
mvn javadoc:javadoc
```

### Checkstyle
Tiedostossa _checkstyle.xml_ määritellyt tarkistukset voi suorittaa seuraavalla komennolla:
```
mvn jxr:jxr checkstyle:checkstyle
```
Tuloksia voi tarkastella polun _target/site/checkstyle.html_ päästä. Tarkistuksen ulkopuolelle jäävät tiedostot (käyttöliittymän muodostava koodi) on määritelty tiedostossa _skipped_files.xml_.
