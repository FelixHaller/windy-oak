
/*
Table description
*/

CREATE TABLE comment (
    "commentID" INTEGER PRIMARY KEY NOT NULL,
    "creator" TEXT NOT NULL,
    "title" TEXT NOT NULL,
    "content" TEXT NOT NULL,
    "dateCreated" INTEGER NOT NULL,
    "dateUpdated" INTEGER,
    "status" TEXT NOT NULL,
    "projectID" INTEGER NOT NULL
);
CREATE TABLE project (
    "projectID" INTEGER PRIMARY KEY NOT NULL,
    "creator" TEXT NOT NULL,
    "title" TEXT NOT NULL,
    "description" TEXT NOT NULL,
    "dateCreated" INTEGER NOT NULL,
    "dateUpdated" INTEGER,
    "status" TEXT NOT NULL,
     "postsURL" TEXT 
);
CREATE TABLE projectmember (
    "projectID" INTEGER NOT NULL,
    "username" TEXT NOT NULL,
    "role" TEXT NOT NULL,
    PRIMARY KEY ("projectID", "username", "role")
);
CREATE TABLE projecttag (
    "projectID" INTEGER NOT NULL,
    "tagName" TEXT NOT NULL,
    PRIMARY KEY ("projectID", "tagName")
);

CREATE TABLE "Tag" (
    "tagName" TEXT NOT NULL,
    "description" TEXT NOT NULL
);
CREATE TABLE "User" (
    "username" TEXT NOT NULL,
    "forename" TEXT NOT NULL,
    "surname" TEXT NOT NULL
);

BEGIN TRANSACTION;
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('1', 'CMusencus', 'Gute Arbeit', 'Also dass, was ich bis jetzt von eurer Arbeit gesehen habe ist wirklich super! Weiter so! ;)', '1388891495000', NULL, 'published', '1');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('2', 'TRedeflus', 'Wie ist der Stand?', 'Ich habe seit langer Zeit nicht neues mehr von eurem Projekt gehört. Wie ist der aktuelle Stand?', '1320403502000', NULL, 'published', '1');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('3', 'CVampus', 'Leider etwas eingeschlafen', 'Leider ist das Projekt auf Grund mangelnder Mitglieder, etwas eingeschlafen. Wenn dich das Thema so brennend interessiert, kannst du gerne mitmachen.', '1323289915000', '1323299915000', 'published', '1');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('4', 'GMine', 'Nur ein Test', 'Das ist nur ein Testkommentar und deshalb nicht für die Öffentlichkeit einsehbar. ', '1286115239000', NULL, 'closed', '1');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('5', 'CMusencus', 'Gute Arbeit', 'Also dass, was ich bis jetzt von eurer Arbeit gesehen habe ist wirklich super! Weiter so! ;)', '1307957439000', NULL, 'deleted', '2');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('6', 'TRedeflus', 'Wie ist der Stand?', 'Ich habe seit langer Zeit nicht neues mehr von eurem Projekt gehört. Wie ist der aktuelle Stand?', '1309224814000', NULL, 'published', '2');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('7', 'CVampus', 'Leider etwas eingeschlafen', 'Leider ist das Projekt auf Grund mangelnder Mitglieder, etwas eingeschlafen. Wenn dich das Thema so brennend interessiert, kannst du gerne mitmachen.', '1326133430000', '1326153430000', 'published', '2');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('8', 'GMine', 'Nur ein Test', 'Das ist nur ein Testkommentar und deshalb nicht für die Öffentlichkeit einsehbar. ', '1336956439000', NULL, 'published', '2');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('9', 'CMusencus', 'Gute Arbeit', 'Also dass, was ich bis jetzt von eurer Arbeit gesehen habe ist wirklich super! Weiter so! ;)', '1382141634000', NULL, 'published', '3');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('10', 'TRedeflus', 'Wie ist der Stand?', 'Ich habe seit langer Zeit nicht neues mehr von eurem Projekt gehört. Wie ist der aktuelle Stand?', '1411389015000', NULL, 'published', '3');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('11', 'CVampus', 'Leider etwas eingeschlafen', 'Leider ist das Projekt auf Grund mangelnder Mitglieder, etwas eingeschlafen. Wenn dich das Thema so brennend interessiert, kannst du gerne mitmachen.', '1380249304000', '1380269304000', 'published', '3');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('12', 'GMine', 'Nur ein Test', 'Das ist nur ein Testkommentar und deshalb nicht für die Öffentlichkeit einsehbar. ', '1390377577000', NULL, 'published', '3');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('13', 'CMusencus', 'Gute Arbeit', 'Also dass, was ich bis jetzt von eurer Arbeit gesehen habe ist wirklich super! Weiter so! ;)', '1331755633000', NULL, 'published', '4');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('14', 'TRedeflus', 'Wie ist der Stand?', 'Ich habe seit langer Zeit nicht neues mehr von eurem Projekt gehört. Wie ist der aktuelle Stand?', '1335340282000', NULL, 'published', '4');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('15', 'CVampus', 'Leider etwas eingeschlafen', 'Leider ist das Projekt auf Grund mangelnder Mitglieder, etwas eingeschlafen. Wenn dich das Thema so brennend interessiert, kannst du gerne mitmachen.', '1335340382000', '1335370382000', 'published', '4');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('16', 'GMine', 'Nur ein Test', 'Das ist nur ein Testkommentar und deshalb nicht für die Öffentlichkeit einsehbar. ', '1284594573000', NULL, 'published', '4');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('17', 'CMusencus', 'Gute Arbeit', 'Also dass, was ich bis jetzt von eurer Arbeit gesehen habe ist wirklich super! Weiter so! ;)', '1345887217000', NULL, 'published', '5');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('18', 'TRedeflus', 'Wie ist der Stand?', 'Ich habe seit langer Zeit nicht neues mehr von eurem Projekt gehört. Wie ist der aktuelle Stand?', '1435093491000', NULL, 'published', '5');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('19', 'CVampus', 'Leider etwas eingeschlafen', 'Leider ist das Projekt auf Grund mangelnder Mitglieder, etwas eingeschlafen. Wenn dich das Thema so brennend interessiert, kannst du gerne mitmachen.', '1435097491000', '1435097891000', 'published', '5');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('20', 'GMine', 'Nur ein Test', 'Das ist nur ein Testkommentar und deshalb nicht für die Öffentlichkeit einsehbar. ', '1404158038000', NULL, 'published', '5');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('21', 'CMusencus', 'Gute Arbeit', 'Also dass, was ich bis jetzt von eurer Arbeit gesehen habe ist wirklich super! Weiter so! ;)', '1373855535000', NULL, 'closed', '6');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('22', 'TRedeflus', 'Wie ist der Stand?', 'Ich habe seit langer Zeit nicht neues mehr von eurem Projekt gehört. Wie ist der aktuelle Stand?', '1422853689000', NULL, 'deleted', '6');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('23', 'CVampus', 'Leider etwas eingeschlafen', 'Leider ist das Projekt auf Grund mangelnder Mitglieder, etwas eingeschlafen. Wenn dich das Thema so brennend interessiert, kannst du gerne mitmachen.', '1408914017000', '1408918017000', 'new', '6');
insert into comment ("commentID", "creator", "title", "content", "dateCreated", "dateUpdated", "status", "projectID") values ('24', 'GMine', 'Nur ein Test', 'Das ist nur ein Testkommentar und deshalb nicht für die Öffentlichkeit einsehbar. ', '1375321251000', NULL, 'closed', '6');
COMMIT;

BEGIN TRANSACTION;
insert into project ("projectID", "creator", "title", "description", "dateCreated", "dateUpdated", "status") values ('1', 'Tutnix', 'SinnvollesProjekt', 'Ein sehr Sinnvolles Projekt', '1268395381000', '1434295200000', 'published');
insert into project ("projectID", "creator", "title", "description", "dateCreated", "dateUpdated", "status") values ('2', 'Quadratus', 'WebApp', 'Erstellung einer WebApp für die HSMW', '1321018620000', '', 'published');
insert into project ("projectID", "creator", "title", "description", "dateCreated", "dateUpdated", "status") values ('3', 'PPenatus', 'BliBlaBLubber', 'Lorem Ipsum', '1426163670000', '1441301302393', 'closed');
insert into project ("projectID", "creator", "title", "description", "dateCreated", "dateUpdated", "status") values ('4', 'CMusencus', 'intelligentes ISMS', 'Für interessierte Studenten der Informatik, kann im Rahmen eine Softwaretechnik-Projekts ein intelligentes ISMS (Information Security Management System) entwickelt werden. Dieses soll an VERINICE angelegt sein, aber mehr intelligente Funktionen besitzen.', '1326274440000', '', 'deleted');
insert into project ("projectID", "creator", "title", "description", "dateCreated", "dateUpdated", "status") values ('5', 'TRedeflus', 'Werbefilmprojekt Fakultät Angewandte Computer- und Biowissenschaften', 'Für interessierte Studenten der Fakultät Medien, soll im Bereich eines Studentischen Projekts ein Werbefilm für die angegeben Fakultät entwickelt werden.', '1326274440000', '1329888000000', 'published');
insert into project ("projectID", "creator", "title", "description", "dateCreated", "dateUpdated", "status") values ('6', 'GMine', 'Initiative neue Namensfindung Fakultät CB', 'Im Rahmen einer Petition soll für die ehemalige Fakultät MNI ein neuer und passender Name gefunden werden.', '1432983355000', '', 'published');
insert into project ("projectID", "creator", "title", "description", "dateCreated", "dateUpdated", "status") values ('7', 'Tutnix', 'BliBlaBLubber', 'Lorem Ipsum', '1440689700000', NULL, 'null');
insert into project ("projectID", "creator", "title", "description", "dateCreated", "dateUpdated", "status") values ('8', 'Tutnix', 'BliBlaBLubber', 'Lorem Ipsum', '1440689700000', NULL, 'null');
insert into project ("projectID", "creator", "title", "description", "dateCreated", "dateUpdated", "status") values ('9', 'Tutnix', 'BliBlaBLubber', 'Lorem Ipsum', '1440689700000', NULL, 'null');
insert into project ("projectID", "creator", "title", "description", "dateCreated", "dateUpdated", "status") values ('10', 'Tutnix', 'BliBlaBLubber', 'Lorem Ipsum', '1440689700000', NULL, 'published');
COMMIT;

BEGIN TRANSACTION;
insert into projectmember ("projectID", "username", "role") values ('1', 'GFaulus', 'Programmer');
insert into projectmember ("projectID", "username", "role") values ('2', 'CMusencus','');
insert into projectmember ("projectID", "username", "role") values ('2', 'AVirus','Grafiker');
insert into projectmember ("projectID", "username", "role") values ('2', 'ASubmer','Author');
insert into projectmember ("projectID", "username", "role") values ('2', 'GUeberd','Chef');
insert into projectmember ("projectID", "username", "role") values ('2', 'GMine','Redakteur');
insert into projectmember ("projectID", "username", "role") values ('3', 'GMine','');
insert into projectmember ("projectID", "username", "role") values ('3', 'CVampus','');
insert into projectmember ("projectID", "username", "role") values ('3', 'GUeberd','');
insert into projectmember ("projectID", "username", "role") values ('4', 'CMusencus','');
insert into projectmember ("projectID", "username", "role") values ('5', 'TRedeflus','');
COMMIT;

BEGIN TRANSACTION;
insert into projecttag ("projectID", "tagName") values ('Das World Wide Web (englisch für „weltweites Netz“, kurz Web, WWW, selten und vor allem in der Anfangszeit und den USA auch W3) ist ein über das Internet abrufbares System von elektronischen Hypertext-Dokumenten, sogenannten Webseiten. Sie sind durch Hyperlinks untereinander verknüpft und werden im Internet über die Protokolle HTTP oder HTTPS übertragen. Die Webseiten enthalten meist Texte, oft mit Bildern und grafischen Elementen illustriert. Häufig sind auch Videos, Tondokumente und Musikstücke eingebettet.', 'Web');
insert into projecttag ("projectID", "tagName") values ('OpenGL (Open Graphics Library; deutsch Offene Grafikbibliothek) ist eine Spezifikation für eine plattform- und programmiersprachenunabhängige Programmierschnittstelle zur Entwicklung von 2D- und 3D- Computergrafikanwendungen. Der OpenGL-Standard beschreibt etwa 250 Befehle, die die Darstellung komplexer 3D-Szenen in Echtzeit erlauben. Zudem können andere Organisationen (zumeist Hersteller von Grafikkarten) proprietäre Erweiterungen definieren.', 'OpenGL');
insert into projecttag ("projectID", "tagName") values ('Ein Medium (lat.: medium = Mitte, Mittelpunkt, von altgr. μέσov méson: das Mittlere; auch Öffentlichkeit, Gemeinwohl, öffentlicher Weg) ist nach neuerem Verständnis ein Vermittelndes im ganz allgemeinen Sinn. Das Wort „Medium“ in der Alltagssprache lässt sich oft mit Kommunikationsmittel gleichsetzen. In der Medientheorie, der Medienphilosophie und den Medienwissenschaften hat sich eine große Anzahl Konzepte mit unterschiedlichen Zielsetzungen entwickelt.', 'Medien');
insert into projecttag ("projectID", "tagName") values ('Kalender (bis Mac OS X Version 10.7 iCal, in der englischen Version Calendar) ist ein von Apple mit dem Betriebssystem OS X ausgeliefertes Programm zur Verwaltung von Terminen. Es implementiert den iCalendar-Standard (RFC 5545, früher 2445). Steve Jobs stellte das Programm am 17. Juli 2002 auf der Macworld New York vor.', 'iCal');
insert into projecttag ("projectID", "tagName") values ('Grafik  ist im weitesten Sinn der Sammelbegriff für alle künstlerischen oder technischen Zeichnungen sowie deren manuelle drucktechnische Vervielfältigung. In der engsten Begriffsverwendung bezieht sich Grafik allein auf die künstlerische Druckgrafik, die zur bildenden Kunst gehört. Eine Originalgrafik entsteht eigenständig, unabhängig von Vorlagen und in der Absicht, die Techniken der Druckgrafik für den künstlerischen Ausdruck zu nutzen.', 'Grafik');
insert into projecttag ("projectID", "tagName") values ('Representational State Transfer (abgekürzt REST, seltener auch ReST) bezeichnet ein Programmierparadigma für verteilte Systeme, insbesondere für Webservices. REST ist eine Abstraktion der Struktur und des Verhaltens des World Wide Web. REST fordert, dass eine URI (Adresse) genau einen Seiteninhalt repräsentiert, und dass ein Web-/REST-Server auf mehrfache Anfragen mit demselben URI auch mit demselben Webseiteninhalt antwortet.', 'REST');
insert into projecttag ("projectID", "tagName") values ('Java ist eine objektorientierte Programmiersprache und eine eingetragene Marke des Unternehmens Sun Microsystems (2010 von Oracle aufgekauft). Die Programmiersprache ist ein Bestandteil der Java-Technologie – diese besteht grundsätzlich aus dem Java-Entwicklungswerkzeug (JDK) zum Erstellen von Java-Programmen und der Java-Laufzeitumgebung (JRE) zu deren Ausführung. Die Laufzeitumgebung selbst umfasst die virtuelle Maschine (JVM) und die mitgelieferten Bibliotheken.', 'Java');
insert into projecttag ("projectID", "tagName") values ('Das Wort Kunst bezeichnet im weitesten Sinne jede entwickelte Tätigkeit, die auf Wissen, Übung, Wahrnehmung, Vorstellung und Intuition gegründet ist (Heilkunst, Kunst der freien Rede). Im engeren Sinne werden damit Ergebnisse gezielter menschlicher Tätigkeit benannt, die nicht eindeutig durch Funktionen festgelegt sind. Kunst ist ein menschliches Kulturprodukt, das Ergebnis eines kreativen Prozesses.', 'Kunst');
COMMIT;
BEGIN TRANSACTION;
insert into tag ("description", "tagName") values ('2', 'Web');
insert into tag ("description", "tagName") values ('3', 'OpenGL');
insert into tag ("description", "tagName") values ('3', 'Medien');
insert into tag ("description", "tagName") values ('1', 'iCal');
insert into tag ("description", "tagName") values ('3', 'Grafik');
insert into tag ("description", "tagName") values ('2', 'REST');
insert into tag ("description", "tagName") values ('2', 'Java');
insert into tag ("description", "tagName") values ('6', 'Kunst');
COMMIT;
BEGIN TRANSACTION;
insert into user ("username", "forename", "surname") values ('CMusencus', 'Cajus', 'Musencus');
insert into user ("username", "forename", "surname") values ('LNichtsals', 'Lucius', 'Nichtsalsverdrus');
insert into user ("username", "forename", "surname") values ('CKneipix', 'Cäsar', 'Kneipix');
insert into user ("username", "forename", "surname") values ('GFaulus', 'Gaius', 'Faulus');
insert into user ("username", "forename", "surname") values ('GMine', 'Gute', 'Mine');
insert into user ("username", "forename", "surname") values ('AVirus', 'Agrippus', 'Virus');
insert into user ("username", "forename", "surname") values ('GUeberd', 'Gracchus', 'Überdrus');
insert into user ("username", "forename", "surname") values ('CVampus', 'Crassus', 'Vampus');
insert into user ("username", "forename", "surname") values ('ASubmer', 'Aquis Gracchus', 'Submersus');
insert into user ("username", "forename", "surname") values ('TRedeflus', 'Tullius', 'Redeflus');
insert into user ("username", "forename", "surname") values ('PGaius', 'Pupus', 'Gaius');
insert into user ("username", "forename", "surname") values ('PPenatus', 'Pontius', 'Penatus');
insert into user ("username", "forename", "surname") values ('Tutnix', 'Mussnix', 'Tutnix');
insert into user ("username", "forename", "surname") values ('Quadratus', 'Quadratus', 'Wurzellus');
insert into user ("username", "forename", "surname") values ('klorenz1', 'Konstantin', 'Lorenz');
insert into user ("username", "forename", "surname") values ('fhaller1', 'Felix', 'Haller');


COMMIT;















