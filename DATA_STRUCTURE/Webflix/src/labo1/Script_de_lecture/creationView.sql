/*
Equipe 3 : Labo 4
Creation des view des cotes moyennes et correlations
*/


CREATE MATERIALIZED VIEW cotemoyenne
  AS 
    SELECT id_film, AVG(cote) as moyenne
    FROM cote
    GROUP BY id_film;

CREATE MATERIALIZED VIEW correlationfilms
  AS
    SELECT cote1.id_film film_1, cote2.id_film film_2,
    CAST(SUM((cote1.cote - moyennes1.moyenne)*(cote2.cote - moyennes2.moyenne)) as real)/SQRT(SUM((cote1.cote - moyennes1.moyenne)^2)*SUM((cote2.cote - moyennes2.moyenne)^2)) correlation
    FROM cotemoyenne moyennes1,
        cotemoyenne moyennes2,
        cote cote1,
        cote cote2
    WHERE cote1.id_client = cote2.id_client
        AND cote1.id_film = moyennes1.id_film
        AND cote2.id_film = moyennes2.id_film
        AND cote1.id_film < cote2.id_film
    GROUP BY cote1.id_film, cote2.id_film
    HAVING CAST(SUM((cote1.cote - moyennes1.moyenne)*(cote2.cote - moyennes2.moyenne)) as real)/SQRT(SUM((cote1.cote - moyennes1.moyenne)^2)*SUM((cote2.cote - moyennes2.moyenne)^2)) > 0 AND COUNT(cote1.id_client) >= 50;
