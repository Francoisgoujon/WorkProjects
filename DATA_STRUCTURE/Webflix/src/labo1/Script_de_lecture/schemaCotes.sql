/*
Equipe 3 : Labo 4
Creation des tables des cotes
*/

CREATE TABLE cote
(   id_film INTEGER NOT NULL,
    id_client INTEGER NOT NULL,
    cote INTEGER,
    dateentree date,
    PRIMARY KEY (id_film, id_client)
);

