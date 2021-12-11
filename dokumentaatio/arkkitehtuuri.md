# Arkkitehtuurikuvaus

![Arkkitehtuuri](/dokumentaatio/kuvat/classdiagram.jpg)


## Rakenne
Ohjelman rakenne on jaettu kolmeen tasoon, joilla on omat pakettinsa - _ajankaytonseuranta.ui_, _ajankaytonseuranta.domain_ ja _ajankaytonseuranta.dao_.

Pakkaus _ajankaytonseuranta.ui_ sisältää JavaFX-käyttöliittymään liittyvät asiat, _ajankaytonseuranta.domain_ paketoi sovelluslogiikan ja _ajankaytonseuranta.dao_ sisältää tietokantayhteydestä huolehtivan toteutuksen.

## Käyttöliittymä

## Sovelluslogiikka
Sovelluksen perimmäistä tarkoitusta palvelevat User- ja Course-luokkien oliot. User-oliot kuvaavat käyttäjiä ja Course-oliot kursseja, joita käyttäjät luovat.

Sovelluslogiikasta huolehtii TimeManagementService-luokan olio, joka ei suoraan ole vuorovaikutuksessa käyttäjiä ja kursseja mallintavien olioiden kanssa. TimeManagementService hallinnoi käyttäjiä ja kursseja erillisten, UserDao- ja CourseDao-rajapinnat toteuttavien luokkion kautta. Nämä tietokantaoperaatioista vastaavat toteutukset injektoidaan TimeManagementService-luokan konstruktorikutsussa, kun sovellus käynnistetään.

### Päätoiminnallisuudet
#### Sisäänkirjautuminen
Kun käyttäjä on kirjoittanut kirjautumisnäkymän tekstikenttään ennestään olemassa olevan käyttäjätunnuksen ja painaa _Kirjaudu sisään_ -nappia, ohjelman suoritus etenee seuraavan kaavion mukaan:
![Sisäänkirjautumisen sekvenssikaavio](/dokumentaatio/kuvat/loginSequenceDiagram.png)

Napin tapahtumankäsittelijään on sidottu TimeManagementService-olion login-metodin kutsu. Tämä metodi tarkistaa ensin, onko syötetty käyttäjätunnus olemassa tietokannassa. Tätä varten TimeManagementService kutsuu UserDao-rajapinnan toteuttavan olion findByUsername-metodia, joka palauttaa syötettyä käyttäjätunnusta vastaavan User-olion tietokannasta. TimeManagementService asettaa kyseisen olion omaan yksityiseen _loggedInUser_-oliomuuttujaansa. Lopuksi käyttöliittymän näkymä vaihdetaan sisäänkirjautuneen käyttäjän kurssinäkymään. Näkymä piirretään drawLoggedInScene-metodikutsulla.
