/* 
Équipe 3 : labo1 du cours LOG660
Script SQL de création des procédures de la BD
Pour postgreSQL

Écrit par François Goujon
*/

CREATE OR REPLACE PROCEDURE ajouterClient(
    nomP VARCHAR,
    prenomP VARCHAR,
    courrielP VARCHAR,
    noTelephoneP VARCHAR,
    mdpP VARCHAR,
    dateNaissanceP DATE,
    numeroCiviqueP INTEGER,
    adresseP VARCHAR,
    villeP VARCHAR,
    provinceP VARCHAR,
    codePostalP VARCHAR,
    codeAboP VARCHAR,
    typeCarteP VARCHAR,
    numeroCarteP VARCHAR,
    dateExpirationP DATE, 
    CVVP INTEGER)
    language plpgsql 
    AS $$
    DECLARE
        newid_personne INTEGER;
        anneelimite INTEGER;
    BEGIN

        SELECT CURRENT_DATE - INTERVAL '18' YEAR into anneelimite;

        IF (dateNaissanceP > anneelimite) THEN 
        RAISE NOTICE 'Trop jeune';
        END IF;

        IF(LENGTH(mdpP) < 5) THEN
        RAISE NOTICE 'Mot de passe trop court';
        END IF;

        IF (dateExpirationP > CURRENT_DATE) THEN
        RAISE NOTICE 'Carte expiree';
        END IF;

        SELECT max("id_personne") into newid_personne from "personne";
        newid_personne := newid_personne + 1;
        INSERT INTO "personne" ("id_personne", "nom", "prenom", "courriel", "notelephone", "mdp", "datenaissance", "numerocivique", "adresse", "ville", "province", "codepostal") 
        Values (
            newid_personne,
            nomP,
            prenomP,
            courrielP,
            noTelephoneP,
            mdpP,
            dateNaissanceP,
            numeroCiviqueP,
            adresseP,
            villeP,
            provinceP,
            codePostalP
        );

        INSERT INTO "client" ("id_personne", "codeabo", "nbfilmsactuellementempruntes") 
        Values (
            newid_personne,
            codeAboP,
            0
        );
        INSERT INTO "cartecredit" ("id_personne", "typecarte", "numerocarte", "dateexpiration", "cvv")
        Values (
            newid_personne,
            typeCarteP,
            numeroCarteP,
            dateExpirationP,
            CVVP
        );
        --commit;
    end;$$;

CREATE OR REPLACE PROCEDURE louerFilm (
    id_personneP INTEGER,
    id_filmP INTEGER)
    language plpgsql 
    AS $$
    DECLARE
        newid_pret INTEGER;
        noCopieLibre INTEGER;
        compteur1 INTEGER;
        compteur2 INTEGER;
        nbPretAutorise INTEGER;
        compteur3 INTEGER;
        nomcompletP VARCHAR;
        groupeageP INTEGER;
        ageP INTEGER;
        courrielP VARCHAR;
        datepremierelocationP DATE;
        codepostalP VARCHAR;
        villeP VARCHAR;
        provinceP VARCHAR;
        datenaissanceP DATE;
        id_date_locationP INTEGER;
        datelocationP TIMESTAMP;
        heurejourP INTEGER;
        joursemaineP VARCHAR;
        journumP INTEGER;
        moisanneeP VARCHAR;
        moisnumP INTEGER;
        titreP VARCHAR;
        anneesortieP INTEGER;
        paysorigineP VARCHAR;
        actionP INTEGER;
        animationP INTEGER;
        aventureP INTEGER;
        biographyP INTEGER;
        comedyP INTEGER;
        dramaP INTEGER;
        familyP INTEGER;
        fantasyP INTEGER;
        romanceP INTEGER;
        scifiP INTEGER;
        thrillerP INTEGER;
    BEGIN
        select count(*) into compteur1
        from "exemplaire" where "id_film" = id_filmP and "statut" = 'disponible';

        if (compteur1 = 0) then
        raise notice 'Stock épuisé';
        RETURN;
        end if;

        select count(*) into compteur2
        from "pretencours" natural join "pret" natural join "client"
        where "id_personne" = id_personneP;

        select "nbpretmax"into nbPretAutorise from "abonnement" 
        where "codeabo" = (select "codeabo" from "client" where "id_personne" = id_personneP);

        if (compteur2 >= nbPretAutorise) then
        raise notice 'Nombre de prêt maximum déjà atteint';
        RETURN;
        end if;

        SELECT "nocopie" into noCopieLibre from "exemplaire"
        where "id_film" = id_filmP and "statut" = 'disponible'
        limit 1;
 

        select max("id_pret") into newid_pret from "pret";

        select count("id_pret") into compteur3 from "pret";

        IF (compteur3 = 0) THEN
        newid_pret := 0;
        END IF;

        newid_pret := newid_pret + 1;

        INSERT INTO "pret" ("id_pret", "id_personne", "datepret", "nocopie")
        Values (
            newid_pret,
            id_personneP,
            CURRENT_DATE,
            noCopieLibre
        );

        INSERT INTO "pretencours" ("id_pret", "enretard")
        Values (
            newid_pret,
            false
        );

        SELECT LOCALTIMESTAMP(0) into datelocationP;
        heurejourP := EXTRACT(HOUR from datelocationP);
        journumP := EXTRACT(DOW from datelocationP);
        moisnumP := EXTRACT(MONTH from datelocationP);

        IF NOT EXISTS(SELECT *
              FROM "dimensionclient"
             WHERE "id_client" = id_personneP) THEN

            select "nom", "datenaissance", "courriel", "codepostal", "ville", "province" into nomcompletP, datenaissanceP, courrielP, codepostalP, villeP, provinceP
            from "personne" where "id_personne" = id_personneP;

            ageP := EXTRACT(YEAR from AGE(datenaissanceP));
            groupeageP := ageP/5;

            INSERT INTO "dimensionclient" ("id_client", "nomcomplet", "groupeage", "courriel", "datepremierelocation", "codepostal", "ville", "province")
            Values (
                id_personneP,
                nomcompletP,
                groupeageP,
                courrielP,
                datelocationP,
                codepostalP,
                villeP,
                provinceP
            );
        END IF;

        id_date_locationP := heurejourP + (journumP * 100) + (moisnumP * 10000);

        IF NOT EXISTS(SELECT *
              FROM "dimensiontemps"
             WHERE "id_date_location" = id_date_locationP) THEN

            IF (journumP = 0) THEN
            joursemaineP := 'Sunday';
            END IF;

            IF (journumP = 1) THEN
            joursemaineP := 'Monday';
            END IF;

            IF (journumP = 2) THEN
            joursemaineP := 'Tuesday';
            END IF;

            IF (journumP = 3) THEN
            joursemaineP := 'Wednesday';
            END IF;

            IF (journumP = 4) THEN
            joursemaineP := 'Thursday';
            END IF;

            IF (journumP = 5) THEN
            joursemaineP := 'Friday';
            END IF;

            IF (journumP = 6) THEN
            joursemaineP := 'Saturday';
            END IF;

            IF (moisnumP = 0) THEN
            moisanneeP := 'January';
            END IF;

            IF (moisnumP = 1) THEN
            moisanneeP := 'February';
            END IF;

            IF (moisnumP = 2) THEN
            moisanneeP := 'March';
            END IF;

            IF (moisnumP = 3) THEN
            moisanneeP := 'April';
            END IF;

            IF (moisnumP = 4) THEN
            moisanneeP := 'May';
            END IF;

            IF (moisnumP = 5) THEN
            moisanneeP := 'June';
            END IF;

            IF (moisnumP = 6) THEN
            moisanneeP := 'July';
            END IF;

            IF (moisnumP = 7) THEN
            moisanneeP := 'August';
            END IF;

            IF (moisnumP = 8) THEN
            moisanneeP := 'September';
            END IF;

            IF (moisnumP = 9) THEN
            moisanneeP := 'October';
            END IF;

            IF (moisnumP = 10) THEN
            moisanneeP := 'November';
            END IF;

            IF (moisnumP = 11) THEN
            moisanneeP := 'December';
            END IF;

            INSERT INTO "dimensiontemps" ("id_date_location", "datelocation", "heurejour", "joursemaine", "moisannee")
            Values (
                id_date_locationP,
                datelocationP,
                heurejourP,
                joursemaineP,
                moisanneeP
            );
        END IF;

        IF NOT EXISTS(SELECT *
              FROM "dimensionfilm"
             WHERE "id_film" = id_filmP) THEN

            select "titre", "anneeproduction", "paysproduction" into titreP, anneesortieP, paysorigineP
            from "film" where "id_film" = id_filmP;

            IF (paysorigineP != 'USA') THEN
            paysorigineP := 'autre';
            END IF;

            actionP := 0;
            animationP := 0;
            aventureP := 0;
            biographyP := 0;
            comedyP := 0;
            dramaP := 0;
            familyP := 0;
            fantasyP := 0;
            romanceP := 0;
            scifiP := 0;
            thrillerP := 0;

            IF EXISTS(SELECT *
                FROM "genrefilm"
                WHERE "id_film" = id_filmP and "nom_genre" = 'Action') THEN
                actionP := 1;
            END IF;

            IF EXISTS(SELECT *
                FROM "genrefilm"
                WHERE "id_film" = id_filmP and "nom_genre" = 'Animation') THEN
                animationP := 1;
            END IF;

            IF EXISTS(SELECT *
                FROM "genrefilm"
                WHERE "id_film" = id_filmP and "nom_genre" = 'Aventure') THEN
                aventureP := 1;
            END IF;

            IF EXISTS(SELECT *
                FROM "genrefilm"
                WHERE "id_film" = id_filmP and "nom_genre" = 'Biography') THEN
                biographyP := 1;
            END IF;

            IF EXISTS(SELECT *
                FROM "genrefilm"
                WHERE "id_film" = id_filmP and "nom_genre" = 'Comedy') THEN
                comedyP:= 1;
            END IF;

            IF EXISTS(SELECT *
                FROM "genrefilm"
                WHERE "id_film" = id_filmP and "nom_genre" = 'Drama') THEN
                dramaP := 1;
            END IF;

            IF EXISTS(SELECT *
                FROM "genrefilm"
                WHERE "id_film" = id_filmP and "nom_genre" = 'Family') THEN
                familyP := 1;
            END IF;

            IF EXISTS(SELECT *
                FROM "genrefilm"
                WHERE "id_film" = id_filmP and "nom_genre" = 'Fantasy') THEN
                fantasyP := 1;
            END IF;

            IF EXISTS(SELECT *
                FROM "genrefilm"
                WHERE "id_film" = id_filmP and "nom_genre" = 'Romance') THEN
                romanceP := 1;
            END IF;

            IF EXISTS(SELECT *
                FROM "genrefilm"
                WHERE "id_film" = id_filmP and "nom_genre" = 'Scifi') THEN
                scifiP := 1;
            END IF;

            IF EXISTS(SELECT *
                FROM "genrefilm"
                WHERE "id_film" = id_filmP and "nom_genre" = 'Thriller') THEN
                thrillerP := 1;
            END IF;

            INSERT INTO "dimensionfilm" ("id_film", "titre", "anneesortie", "paysorigine", "actionfilm", "animation", "aventure", "biography", "comedy", "drama", "family", "fantasy", "romance", "scifi", "thriller")
            Values (
                id_filmP,
                titreP,
                anneesortieP,
                paysorigineP,
                actionP,
                animationP,
                aventureP,
                biographyP,
                comedyP,
                dramaP,
                familyP,
                fantasyP,
                romanceP,
                scifiP,
                thrillerP
            );
        END IF;

        INSERT INTO "faitslocationfilm" ("id_locationfilm", "id_client", "id_film", "id_date_location")
        Values (
            newid_pret,
            id_personneP,
            id_filmP,
            id_date_locationP
        );



        UPDATE "exemplaire"
        SET "statut" = 'prete'
        WHERE "nocopie" = noCopieLibre;

        UPDATE "exemplaire"
        SET "id_pret" = newid_pret
        WHERE "nocopie" = noCopieLibre;

        UPDATE "client"
        SET "nbfilmsactuellementempruntes" = "nbfilmsactuellementempruntes" + 1
        WHERE "id_personne" = id_personneP;

        --commit;
    end;$$;

CREATE OR REPLACE PROCEDURE rendreFilm (
    id_personneP INTEGER,
    noCopieP INTEGER)
    language plpgsql 
    AS $$
    DECLARE
        getid_pret INTEGER;
    BEGIN
        select "id_pret" into getid_pret
        from "pret" 
        where "id_personne" = id_personneP and "nocopie" = noCopieP;

        INSERT INTO "pretarchive" ("id_pret", "dateretour")
        Values (
            getid_pret,
            CURRENT_DATE
        );

        UPDATE "exemplaire"
        SET "statut" = 'disponible'
        WHERE "nocopie" = noCopieP;

        UPDATE "exemplaire"
        SET "id_pret" = null
        WHERE "nocopie" = noCopieP;

        UPDATE "client"
        SET "nbfilmsactuellementempruntes" = "nbfilmsactuellementempruntes" - 1
        WHERE "id_personne" = id_personneP;

        DELETE FROM "pretencours" 
        WHERE "id_pret" = getid_pret;

        --commit;
    end;$$;

CREATE OR REPLACE PROCEDURE updateRetard ()
    language plpgsql
    AS $$
    DECLARE
        dateaujo DATE;
    BEGIN
        SELECT CURRENT_DATE into dateaujo;

        UPDATE pretencours as pe
        SET enretard = true
        FROM pret as p, client as c, abonnement as a
        WHERE p.id_pret = pe.id_pret 
            and p.id_personne = c.id_personne
            and c.codeabo = a.codeabo 
            and dateaujo - p.datepret > a.dureepretmax;

    end;$$;


