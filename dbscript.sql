
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
insert into projecttag ("projectID", "tagName") values ('2', 'Web');
insert into projecttag ("projectID", "tagName") values ('3', 'OpenGL');
insert into projecttag ("projectID", "tagName") values ('3', 'Medien');
insert into projecttag ("projectID", "tagName") values ('1', 'iCal');
insert into projecttag ("projectID", "tagName") values ('3', 'Grafik');
insert into projecttag ("projectID", "tagName") values ('2', 'REST');
insert into projecttag ("projectID", "tagName") values ('2', 'Java');
insert into projecttag ("projectID", "tagName") values ('6', 'Kunst');
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
COMMIT;















