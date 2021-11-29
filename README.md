# Ohjelmistotekniikka: harjoitustyö

## Dokumentaatio
[Vaatimusmäärittely](https://github.com/ylireetta/ot-harjoitustyo/blob/master/dokumentaatio/vaatimusmaarittely.md)

[Arkkitehtuurikuvaus](https://github.com/ylireetta/ot-harjoitustyo/blob/master/dokumentaatio/arkkitehtuuri.md)

[Käyttöohje](https://github.com/ylireetta/ot-harjoitustyo/blob/master/dokumentaatio/kayttoohje.md)

[Tuntikirjanpito](https://github.com/ylireetta/ot-harjoitustyo/blob/master/dokumentaatio/tuntikirjanpito.md)

## Releaset

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
```
mvn test jacoco:report
```

### Suoritettavan jarin generointi

### JavaDoc

### Checkstyle
