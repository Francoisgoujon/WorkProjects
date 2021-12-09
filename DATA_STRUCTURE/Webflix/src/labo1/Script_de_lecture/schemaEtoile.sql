/*
Equipe 3 : Labo 4
Creation des tables du schema Etoile
*/


CREATE TABLE dimensionclient
(   id_client INTEGER NOT NULL UNIQUE,
    nomcomplet VARCHAR(100) NOT NULL,
    groupeage INTEGER,
    courriel VARCHAR(100),
    datepremierelocation DATE,
    codepostal VARCHAR(7),
    ville VARCHAR(60),
    province VARCHAR(40),
    PRIMARY KEY (id_client)
);

CREATE DOMAIN Domainpays AS VARCHAR(15) CHECK (value IN ('USA', 'autre'));

CREATE TABLE dimensionfilm
(   id_film INTEGER NOT NULL UNIQUE,
    titre VARCHAR(100) NOT NULL,
    anneesortie INTEGER,
    paysorigine      Domainpays NOT NULL,
    actionfilm       INTEGER DEFAULT 0 NOT NULL CHECK (actionfilm IN (0, 1)),
    animation   	 INTEGER DEFAULT 0 NOT NULL CHECK (animation IN (0, 1)),
    aventure		 INTEGER DEFAULT 0 NOT NULL CHECK (aventure IN (0, 1)),
    biography   	 INTEGER DEFAULT 0 NOT NULL CHECK (biography IN (0, 1)),
    comedy           INTEGER DEFAULT 0 NOT NULL CHECK (comedy IN (0, 1)),
    drama            INTEGER DEFAULT 0 NOT NULL CHECK (drama IN (0, 1)),
    family      	 INTEGER DEFAULT 0 NOT NULL CHECK (family IN (0, 1)),
    fantasy          INTEGER DEFAULT 0 NOT NULL CHECK (fantasy IN (0, 1)),
    romance          INTEGER DEFAULT 0 NOT NULL CHECK (romance IN (0, 1)),
    scifi            INTEGER DEFAULT 0 NOT NULL CHECK (scifi IN (0, 1)),
    thriller         INTEGER DEFAULT 0 NOT NULL CHECK (thriller IN (0, 1)),
    PRIMARY KEY (id_film)
);

CREATE DOMAIN DomainJour AS VARCHAR(15) CHECK (value IN ('Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'));
CREATE DOMAIN DomainMois AS VARCHAR(15) CHECK (value IN ('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'));

CREATE TABLE dimensiontemps(
    id_date_location INTEGER NOT NULL UNIQUE,
    datelocation TIMESTAMP NOT NULL,
    heurejour INTEGER NOT NULL,
    joursemaine DomainJour NOT NULL,
    moisannee DomainMois NOT NULL,
    PRIMARY KEY(id_date_location)

);

CREATE TABLE faitslocationfilm(
    id_locationfilm INTEGER NOT NULL UNIQUE,
    id_client INTEGER NOT NULL,
    id_film INTEGER NOT NULL,
    id_date_location INTEGER NOT NULL,
    PRIMARY KEY(id_locationfilm),
    FOREIGN KEY (id_client) references dimensionclient(id_client),
    FOREIGN KEY (id_film) references dimensionfilm(id_film),
    FOREIGN KEY (id_date_location) references dimensiontemps(id_date_location)   
);

