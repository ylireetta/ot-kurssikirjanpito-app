# Arkkitehtuurikuvaus

![Arkkitehtuuri](/dokumentaatio/kuvat/classdiagram.jpg)


## Rakenne
Ohjelman rakenne on jaettu kolmeen tasoon, joilla on omat pakettinsa - _ajankaytonseuranta.ui_, _ajankaytonseuranta.domain_ ja _ajankaytonseuranta.dao_.

Pakkaus _ajankaytonseuranta.ui_ sisältää JavaFX-käyttöliittymään liittyvät asiat, _ajankaytonseuranta.domain_ paketoi sovelluslogiikan ja _ajankaytonseuranta.dao_ sisältää tietokantayhteydestä huolehtivan toteutuksen.

## Käyttöliittymä
Käyttöliittymässä on yhteensä viisi erilaista näkymää:
* Sisäänkirjautuminen
* Kurssi- ja ajanottonäkymä
* Käyttäjän kurssidata
* Järjestelmän kurssiranking
* Kurssien poistaminen

Eri näkymien rakentaminen on hajautettu omiin luokkiinsa _ajankaytonseuranta.ui_-pakkauksessa. Varsinainen rakennustyö tapahtuu luokassa _ajankaytonseuranta.ui.AjankaytonseurantaUi_.

## Sovelluslogiikka
Sovelluksen perimmäistä tarkoitusta palvelevat _User_- ja _Course_-luokkien oliot. _User_-oliot kuvaavat käyttäjiä ja _Course_-oliot kursseja, joita käyttäjät luovat.

Sovelluslogiikasta huolehtii _TimeManagementService_-luokan olio, joka ei suoraan ole vuorovaikutuksessa käyttäjiä ja kursseja mallintavien olioiden kanssa. _TimeManagementService_ hallinnoi käyttäjiä ja kursseja erillisten, _UserDao_- ja _CourseDao_-rajapinnat toteuttavien luokkion kautta. Nämä tietokantaoperaatioista vastaavat toteutukset injektoidaan _TimeManagementService_-luokan konstruktorikutsussa, kun sovellus käynnistetään.

### Tietojen pysyväistallennus
Käyttäjät ja kurssit talletetaan MongoDB-tietokantaan _ConcreteUserDao_- ja _ConcreteCourseDao_-luokkien olioiden avulla. Luokat toteuttavat _UserDao_- ja _CourseDao_-rajapinnat, joten varsinaisesta tallennuksesta huolehtivat luokat voidaan tarvittaessa vaihtaa tai niiden toiminnallisuutta muuttaa, kunhan ne vain edelleenkin toteuttavat rajapinnoissa määritellyt metodit.

Sovelluksen _resources_-hakemistossa sijaitsevaan _config.properties_-tiedostoon on määritelty tietokannan osoite ja "mäppäyksessä" käytettävät tiedot. _User_-luokkien olioiden yhteyteen tallennetaan vain käyttäjätunnus, ja _Course_-olioihin kurssin nimi, opintopisteiden määrä, kurssiin käytetty aika sekä viite kurssin luoneen käyttäjän _User_-olioon.

### Päätoiminnallisuudet
#### Sisäänkirjautuminen
Kun käyttäjä on kirjoittanut kirjautumisnäkymän tekstikenttään ennestään olemassa olevan käyttäjätunnuksen ja painaa _Kirjaudu sisään_ -nappia, ohjelman suoritus etenee seuraavan kaavion mukaan:
![Sisäänkirjautumisen sekvenssikaavio](/dokumentaatio/kuvat/loginSequenceDiagram.png)

Napin tapahtumankäsittelijään on sidottu _TimeManagementService_-olion _login_-metodin kutsu. Tämä metodi tarkistaa ensin, onko syötetty käyttäjätunnus olemassa tietokannassa. Tätä varten _TimeManagementService_ kutsuu _UserDao_-rajapinnan toteuttavan olion _findByUsername_-metodia, joka palauttaa syötettyä käyttäjätunnusta vastaavan _User_-olion tietokannasta. _TimeManagementService_ asettaa kyseisen olion omaan yksityiseen _loggedInUser_-oliomuuttujaansa. Lopuksi käyttöliittymän näkymä vaihdetaan sisäänkirjautuneen käyttäjän kurssinäkymään. Näkymä piirretään _CourseListScene_-olion metodikutsulla. Käyttöliittymän piirtäminen on jaettu useaan eri luokkaan, eikä tässä kaaviossa oteta _LogInScene_-luokkaa lukuunottamatta tarkempaa katsausta eri näkymien luomiseen.

#### Kurssin luominen
Uuden kurssin luontinäkymässä käyttäjä syöttää lisättävän kurssin tiedot kenttiin _Kurssin nimi_ ja _Opintopisteet_ ja painaa _Lisää kurssi_ -nappia.

![Uuden kurssin luomisen sekvenssikaavio](/dokumentaatio/kuvat/addCourseSequenceDiagram.png)

Napin tapahtumankäsittelijässä tarkistetaan ensin, onko käyttäjä antanut kurssille nimen, ja onko opintopisteet annettu positiivisena kokonaislukuna. Sen jälkeen kutsutaan _TimeManagementService_-olion _createCourse_-metodia, joka puolestaan kutsuu _CourseDao_-rajapinnan toteuttavan luokan _createCourse_-metodia. Tietokantaoperaatio palauttaa onnistuessaan totuusarvon _true_. Kun kurssi on tallennettu tietokantaan, _NewCourseScene_ kutsuu vielä edellisen näkymän (käyttäjän kurssinäkymä) metodia _redrawCourseList_, jotta uusi kurssi saadaan näkyviin alasvetovalikkoon.

#### Ajankäytön päivittäminen


### Kehityskohteet
