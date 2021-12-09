/* Création du lien vers la base de données externe 
2.1
*/
CREATE DATABASE LINK lien_cote_film
  CONNECT TO EXT660E IDENTIFIED BY "LoG660x" USING 'ext660ora12c';

/* Création d'un synonyme pour la table : utiliser comme nom 
de table "cote_film" pour acceder à la table des cotes */
CREATE SYNONYM cote_film
  FOR LOG6601C.COTES@lien_cote_film;

/* Création de la vue matéralisé des cotes moyennes pour chaque 
film : pour acceder à la table des cotes moyenne, utiliser comme
nom de table "cote_moyenne" 
2.2
*/
CREATE MATERIALIZED VIEW cotemoyenne
  REFRESH FORCE START WITH SYSDATE
  NEXT TRUNC(SYSDATE) + 6/48
  AS 
    SELECT idFilm, AVG(cote) as moyenne
    FROM cote_film
    GROUP BY idFilm;


/* Création de la vue matéralisé des corréraltions pour chaque paire
de film : pour acceder à la table des corrélations, utiliser comme
nom de table "correlation_films" 
2.2
*/
CREATE MATERIALIZED VIEW correlationfilms
  REFRESH FORCE START WITH SYSDATE
  NEXT TRUNC(SYSDATE) + 7/48 
  AS
    SELECT cote1.idFilm film_1, cote2.idFilm film_2,
    CAST((SUM((cote1.cote - moyennes1.moyenne)*(cote2.cote - moyennes2.moyenne)) as real)/SQRT(SUM((cote1.cote - moyennes1.moyenne)^2)*SUM((cote2.cote - moyennes2.moyenne)^2))) correlation
    FROM (SELECT idFilm, AVG(cote) moyenne FROM cote_film GROUP BY idFilm) moyennes1,
        (SELECT idFilm, AVG(cote) moyenne FROM cote_film GROUP BY idFilm) moyennes2,
        cote_film cote1,
        cote_film cote2
    WHERE cote1.idClient = cote2.idClient
        AND cote1.idFilm = moyennes1.idFilm
        AND cote2.idFilm = moyennes2.idFilm
        AND cote1.idFilm < cote2.idFilm
    GROUP BY cote1.idFilm, cote2.idFilm
    HAVING CAST((SUM((cote1.cote - moyennes1.moyenne)*(cote2.cote - moyennes2.moyenne)) as real)/SQRT(SUM((cote1.cote - moyennes1.moyenne)^2)*SUM((cote2.cote - moyennes2.moyenne)^2))) > 0 
      AND COUNT(idClient) >= 50;

/* 2.3 dans HibernateQueries ligne 662 */


/*
Implémentation java déjà faite : 
Méthode pour les recommandations : getRecommandation()
Méthode pour connaitre la cote : getCote()
Pour la vérification que les recommandations n'ont pas déjà été vues : 3 nouvelles méthodes
  - Méthode pour connaître les emprunts archivés d'un utilisateur : getEmpruntsArchive(int idpersonne)
  - Méthode pour connaître les emprunts archivés de l'utilisateur courant : getEmpruntsArchiveUtilisateur()
  - Méthode pour connaître tous les emprunts (archivés + en cours (méthode getEmpruntsUtilisateur() déjà existante))
     de l'utilisateur courant : getTousEmpruntsUtilisateur()
Ajout de la cote et des recommandations directement dans la méthode getInfoFilmById()
 qui renvoie un dictionnaire des infos complètes. La vérification que les films n'ont pas été vus se fait à ce moment là.

*/
    

