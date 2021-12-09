/* 
Équipe 3 : labo1 du cours LOG660
Script SQL de création des tables de la BD 
Pour postgreSQL

Écrit par François Goujon
*/


CREATE TABLE Personne
( id_personne INTEGER NOT NULL UNIQUE,
nom VARCHAR(30) NOT NULL,
prenom VARCHAR(30) NOT NULL,
courriel VARCHAR(100) NOT NULL UNIQUE,
noTelephone VARCHAR(15) NOT NULL,
mdp VARCHAR(60) NOT NULL CHECK (LENGTH(mdp) > 5),
dateNaissance DATE NOT NULL CHECK (dateNaissance < CURRENT_DATE - INTERVAL '18' YEAR),
numeroCivique INTEGER,
adresse VARCHAR(100) NOT NULL,
ville VARCHAR(60) NOT NULL,
province VARCHAR(40) NOT NULL,
codePostal VARCHAR(10) NOT NULL,
PRIMARY KEY (id_personne)
);

CREATE TABLE Abonnement
( codeAbo VARCHAR(2) NOT NULL UNIQUE,
nbPretMax INTEGER NOT NULL, 
dureePretMax INTEGER,
PRIMARY KEY (codeAbo)
);

CREATE TABLE Film
( id_film INTEGER NOT NULL UNIQUE,
titre VARCHAR(100) NOT NULL,
anneeProduction INTEGER,
paysProduction VARCHAR(50),
langueOriginale VARCHAR(20),
duree INTEGER,
resumeFilm VARCHAR(2000),
affiche VARCHAR(2000),
quantiteStock INTEGER DEFAULT 0 NOT NULL,
CHECK (quantiteStock >= 0),
PRIMARY KEY (id_film)
);

CREATE TABLE Client
( id_personne INTEGER NOT NULL UNIQUE,
codeAbo VARCHAR(2) NOT NULL,
nbFilmsActuellementEmpruntes INTEGER NOT NULL, 
PRIMARY KEY (id_personne),
FOREIGN KEY (id_personne) REFERENCES Personne,
FOREIGN KEY (codeAbo) REFERENCES Abonnement
);

CREATE TABLE Employe
( matricule INTEGER NOT NULL UNIQUE,
id_personne INTEGER NOT NULL UNIQUE, 
PRIMARY KEY (matricule),
FOREIGN KEY (id_personne) REFERENCES Personne
);

CREATE TABLE CarteCredit
( id_personne INTEGER NOT NULL UNIQUE,
typeCarte VARCHAR(20) NOT NULL,
numeroCarte VARCHAR(30) NOT NULL UNIQUE,
dateExpiration DATE NOT NULL CHECK (dateExpiration > CURRENT_DATE),
CVV INTEGER,
PRIMARY KEY (id_personne),
FOREIGN KEY (id_personne) REFERENCES Client
);


CREATE TABLE Pret
( id_pret INTEGER NOT NULL UNIQUE,
id_personne INTEGER NOT NULL,
datePret DATE NOT NULL,
noCopie INTEGER NOT NULL,
PRIMARY KEY (id_pret),
FOREIGN KEY (id_personne) REFERENCES Client
);

CREATE TABLE PretArchive
( id_pret INTEGER NOT NULL UNIQUE REFERENCES Pret,
dateRetour DATE NOT NULL,
PRIMARY KEY (id_pret),
FOREIGN KEY (id_pret) REFERENCES Pret
);

CREATE TABLE PretEnCours
( id_pret INTEGER NOT NULL UNIQUE,
enRetard BOOLEAN NOT NULL,
PRIMARY KEY (id_pret),
FOREIGN KEY (id_pret) REFERENCES Pret
);

CREATE DOMAIN DomainStatut AS VARCHAR(15) CHECK (value = 'prete' OR value = 'disponible' OR value = 'retire');

CREATE TABLE Exemplaire
( noCopie INTEGER NOT NULL UNIQUE,
id_film INTEGER NOT NULL,
id_pret INTEGER UNIQUE,
statut DomainStatut NOT NULL,
PRIMARY KEY (noCopie),
FOREIGN KEY (id_film) REFERENCES Film,
FOREIGN KEY (id_pret) REFERENCES Pret
);


CREATE TABLE Artiste
( noArtiste INTEGER NOT NULL UNIQUE,
nom VARCHAR(100) NOT NULL,
prenom VARCHAR(100),
dateNaissance DATE,
lieuNaissance VARCHAR(200),
photo VARCHAR(2000),
biographie VARCHAR(60000),
PRIMARY KEY (noArtiste)
);

CREATE TABLE RealisateurFilms 
( noArtiste INTEGER NOT NULL,
id_film INTEGER NOT NULL,
PRIMARY KEY (noArtiste, id_film),
FOREIGN KEY (id_film) REFERENCES Film,
FOREIGN KEY (noArtiste) REFERENCES Artiste
);

CREATE TABLE ActeurFilms 
( noArtiste INTEGER NOT NULL,
id_film INTEGER NOT NULL,
prenomPersonnage VARCHAR(60),
nomPersonnage VARCHAR(60),
PRIMARY KEY (noArtiste, id_film),
FOREIGN KEY (id_film) REFERENCES Film,
FOREIGN KEY (noArtiste) REFERENCES Artiste
);

CREATE TABLE Scenariste
( id_film INTEGER NOT NULL,
nom VARCHAR(100) NOT NULL,
prenom VARCHAR(100) NOT NULL,
PRIMARY KEY (id_film, nom, prenom),
FOREIGN KEY (id_film) REFERENCES Film
);

CREATE TABLE BandeAnnonce
( lien VARCHAR(300) NOT NULL,
id_film INTEGER NOT NULL,
PRIMARY KEY (lien, id_film),
FOREIGN KEY (id_film) REFERENCES Film
);

CREATE TABLE Genre
( nom_genre VARCHAR(30) NOT NULL UNIQUE,
descriptif VARCHAR(2000),
PRIMARY KEY (nom_genre)
);

CREATE TABLE GenreFilm
( id_film INTEGER NOT NULL,
nom_genre VARCHAR(30) NOT NULL,
PRIMARY KEY (id_film, nom_genre),
FOREIGN KEY (nom_genre) REFERENCES Genre,
FOREIGN KEY (id_film) REFERENCES Film
);
