/* 
Équipe 3 : labo1 du cours LOG660
Script SQL de réinitialisation de la BD
Pour postgreSQL

Écrit par Olivier St-Pierre
*/


DROP PROCEDURE IF EXISTS ajouterClient;
DROP PROCEDURE IF EXISTS louerFilm;
DROP PROCEDURE IF EXISTS rendreFilm;

DROP DOMAIN IF EXISTS DomainStatut CASCADE;

DROP TABLE IF EXISTS Personne CASCADE;
DROP TABLE IF EXISTS Abonnement CASCADE;
DROP TABLE IF EXISTS Film CASCADE;
DROP TABLE IF EXISTS Client CASCADE;
DROP TABLE IF EXISTS Employe CASCADE;
DROP TABLE IF EXISTS CarteCredit CASCADE;
DROP TABLE IF EXISTS Pret CASCADE;
DROP TABLE IF EXISTS PretArchive CASCADE;
DROP TABLE IF EXISTS PretEnCours CASCADE;
DROP TABLE IF EXISTS Exemplaire CASCADE;
DROP TABLE IF EXISTS Artiste CASCADE;
DROP TABLE IF EXISTS RealisateurFilms CASCADE;
DROP TABLE IF EXISTS ScenaristeFilms CASCADE;
DROP TABLE IF EXISTS ActeurFilms CASCADE;
DROP TABLE IF EXISTS BandeAnnonce CASCADE;
DROP TABLE IF EXISTS Genre CASCADE;
DROP TABLE IF EXISTS GenreFilm CASCADE;
DROP TABLE IF EXISTS Scenariste CASCADE;