package labo1.methodes;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;

import labo1.Hibernate.src.Acteurfilms;
import labo1.Hibernate.src.Artiste;
import labo1.Hibernate.src.Bandeannonce;
import labo1.Hibernate.src.Cotemoyenne;
import labo1.Hibernate.src.Dimensionclient;
import labo1.Hibernate.src.Dimensionfilm;
import labo1.Hibernate.src.Dimensiontemps;
import labo1.Hibernate.src.Exemplaire;
import labo1.Hibernate.src.Faitslocationfilm;
import labo1.Hibernate.src.Film;
import labo1.Hibernate.src.Genre;
import labo1.Hibernate.src.Personne;
import labo1.Hibernate.src.Scenariste;

public class HibernateQueries {

  private SessionFactory sessionFactory;
  private Transaction tx;
  private Personne personne;
  private String message;
  private boolean addAll;

  public HibernateQueries() {
    this.sessionFactory = new Configuration().configure("labo1/Hibernate/src/hibernate.cfg.xml").buildSessionFactory();
    this.tx = null;
    this.personne = null;
    this.message = null;
  }

  public void closeSessionFactory() {
    this.sessionFactory.close();
  }

  public boolean connextionUtilisateur(String courriel, String mdp) {
    Session session = this.sessionFactory.openSession(); 

    boolean reussite = false;
    try {
      this.tx = session.beginTransaction(); 

      System.out.println("Trying to log client"); 

      Query query = session.createQuery("FROM Personne p WHERE p.courriel = :mail");
      query.setParameter("mail", courriel);
      List lesPersonnes = query.list();

      if (!lesPersonnes.isEmpty()) {
        Personne unePersonne = (Personne) lesPersonnes.get(0);

        if (mdp.equals(unePersonne.getMdp())) {
          this.personne = unePersonne;
          this.message = "Connexion Réussie : Bienvenue " + unePersonne.getPrenom() + " !";
          System.out.println(this.message);
          reussite = true;
        } else {
          this.message = "Mauvaise combinaison courriel/mot de passe";
          System.out.println(this.message);
        }
      } else {
        this.message = "Courriel inconnu";
        System.out.println(this.message);
      }

      tx.commit();

      return reussite;

    } catch (Exception e) {
      if (tx != null) {
        tx.rollback();
      }
      throw e;
    } finally { 
      session.close(); 
    } 
  }

  public void deconnextionUtilisateur() {
    this.personne = null;
    this.message = "Déconnexion Réussie";
    System.out.println(this.message);
  }

  public Personne getUtilisateur() {
    return this.personne;
  }

  public String getMessage() {
    return this.message;
  }

  public String louerfilm(int idclient, int idfilm ) {
    Session session = this.sessionFactory.openSession(); 

    String res;
    
    if (nbExemplaireDispo(idfilm) == 0) {
      res = "Film non disponible pour le moment";
      this.message = res;
      System.out.println(res);
      return res;
    } 
    if (nbExemplaireEmprunte(idclient) >= nbExemplaireAutorise(idclient)) {
      res = "Nombre maximum de prêts déjà atteint";
      this.message = res;
      System.out.println(res);
      return res;

    } else {

      try { 
        this.tx = session.beginTransaction(); 

        //System.out.println("Location du film id : " + idfilm + " par le client id : " + idclient ); 
        SQLQuery exQuery = session.createSQLQuery("CALL " +
                "louerfilm(:idpers,:idfilm);");
        exQuery.setParameter("idpers", idclient);
        exQuery.setParameter("idfilm", idfilm);
        int exRows = exQuery.executeUpdate();
        this.message = "Location du film id : " + idfilm + " par le client id : " + idclient +" réussie.";
        System.out.println(this.message);

        tx.commit();

        res = this.message;

        return res;

      } catch (Exception e) {
        if (tx != null) {
          tx.rollback();
        }
        throw e;
      } finally { 
        session.close(); 
      } 
    }
    
    //sessionFactory.close();
  }


  public void rendrefilm(int idclient, int nocopie ) {
    Session session = this.sessionFactory.openSession(); 

    try { 
      tx = session.beginTransaction(); 

      //System.out.println("Retour de l'exemplaire no : " + nocopie + " par le client id : " + idclient); 
      SQLQuery exQuery = session.createSQLQuery("CALL " +
              "rendrefilm(:idpers,:nocop);");
      exQuery.setParameter("idpers", idclient);
      exQuery.setParameter("nocop", nocopie);
      int exRows = exQuery.executeUpdate();
      this.message = "Retour de l'exemplaire no : " + nocopie + " par le client id : " + idclient + " réussi";
      System.out.println(this.message);

      tx.commit();
    } catch (Exception e) {
      if (tx != null) {
        tx.rollback();
      }
      throw e;
    } finally { 
      session.close(); 
    } 
    
    //sessionFactory.close();
  }

  public String louerFilmUtilisateur(int idfilm) {
    String res;
    if (this.getUtilisateur() != null) {
      res = this.louerfilm(this.getUtilisateur().getIdPersonne(), idfilm);
    } else {
      this.message = "Aucun utilisateur connecté";
      res = this.message;
      System.out.println(this.message);
    }
    return res;
  }

  public void rendreFilmUtilisateur(int nocopie) {
    if (this.getUtilisateur() != null) {
      this.rendrefilm(this.getUtilisateur().getIdPersonne(), nocopie);
    } else {
      this.message = "Aucun utilisateur connecté";
      System.out.println(this.message);
    }
  }

  public Long nbExemplaireDispo(int idfilm) {
    Session session = this.sessionFactory.openSession(); 

    try {
      this.tx = session.beginTransaction(); 

      System.out.println("Comptage des examplaires disponibles"); 

      String hql = "SELECT count(*) FROM Exemplaire WHERE id_film = :id_filmP AND statut = 'disponible'";
      Query query = session.createQuery(hql);
      query.setParameter("id_filmP", idfilm);
      List result = query.list();
      Long nbdispo = (Long)result.get(0);

      tx.commit();

      System.out.println("Il y a " + nbdispo + " exemplaires disponibles.");

      return nbdispo;

    } catch (Exception e) {
      if (tx != null) {
        tx.rollback();
      }
      throw e;
    } finally { 
      session.close(); 
    } 
  }

  public int nbExemplaireEmprunte(int id_personne) {
    Session session = this.sessionFactory.openSession(); 

    try {
      this.tx = session.beginTransaction(); 

      String hql = "SELECT nbfilmsactuellementempruntes FROM Client WHERE id_personne = :id_P";
      Query query = session.createQuery(hql);
      query.setParameter("id_P", id_personne);
      List result = query.list();
      int nb = (int)result.get(0);

      tx.commit();

      System.out.println("Il y a " + nb + " exemplaires actuellement emprunté(s).");

      return nb;

    } catch (Exception e) {
      if (tx != null) {
        tx.rollback();
      }
      throw e;
    } finally { 
      session.close(); 
    } 
  }

  public int nbExemplaireAutorise(int id_personne) {
    Session session = this.sessionFactory.openSession(); 

    try {
      this.tx = session.beginTransaction(); 

      String sql = "SELECT nbpretmax FROM Abonnement A, Client C WHERE C.codeabo = A.codeabo AND id_personne = :id_P";
      SQLQuery query = session.createSQLQuery(sql);
      query.setParameter("id_P", id_personne);
      List result = query.list();
      int nb = (int)result.get(0);

      tx.commit();

      System.out.println("Nombre de pret max : " + nb);

      return nb;

    } catch (Exception e) {
      if (tx != null) {
        tx.rollback();
      }
      throw e;
    } finally { 
      session.close(); 
    } 
  }
  
  // Emprunts en cours
  public List getEmprunts(int idpersonne) {
    Session session = this.sessionFactory.openSession(); 

      try {
        this.tx = session.beginTransaction(); 

        System.out.println("Liste des examplaires empruntés par l'Utilisateur"); 

        String sql = "SELECT E.noCopie, E.id_film, E.id_pret, P.datePret, PE.enRetard, F.titre " + 
         "FROM Exemplaire E, Pret P, Pretencours PE, Film F " + 
         "WHERE P.id_personne = :idPP AND P.id_pret = PE.id_pret AND E.id_pret = P.id_pret AND E.id_film = F.id_film";
        SQLQuery query = session.createSQLQuery(sql);
        query.setParameter("idPP", idpersonne);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List result = query.list();

        
        for(Object object : result) {
          Map row = (Map)object;
          //System.out.println(row.keySet());
          System.out.print("ID_PRET: " + row.get("id_pret")); 
          //System.out.print(", DATE PRET: " + row.get("datepret")); 
          //System.out.print(", ID_FILM: " + row.get("id_film")); 
          //System.out.print(", NOCOPIE: " + row.get("nocopie")); 
          //System.out.println(", EN RETARD: " + row.get("enretard"));
          //System.out.println(", TITRE: " + row.get("titre")); 
        }
        

        tx.commit();

        //System.out.println(result);

        return result;

      } catch (Exception e) {
        if (tx != null) {
          tx.rollback();
        }
        throw e;
      } finally { 
        session.close(); 
      } 
  }

  public List getEmpruntsUtilisateur() {
    List emprunts = null;
    if (this.getUtilisateur() != null) {
      emprunts = this.getEmprunts(this.getUtilisateur().getIdPersonne());
    } else {
      this.message = "Aucun utilisateur connecté";
      System.out.println(this.message);
    }
    return emprunts;
  }

  //Nouveau
  public List getEmpruntsArchive(int idpersonne) {
    Session session = this.sessionFactory.openSession(); 

      try {
        this.tx = session.beginTransaction(); 

        System.out.println("Liste des emprunts archivés par l'Utilisateur"); 

        String sql = "SELECT E.noCopie, E.id_film, E.id_pret, P.datePret, F.titre " + 
         "FROM Exemplaire E, Pret P, Pretarchive PA, Film F " + 
         "WHERE P.id_personne = :idPP AND P.id_pret = PA.id_pret AND E.id_pret = P.id_pret AND E.id_film = F.id_film";
        SQLQuery query = session.createSQLQuery(sql);
        query.setParameter("idPP", idpersonne);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List result = query.list();

        for(Object object : result) {
          Map row = (Map)object;
          System.out.print("ID_PRET: " + row.get("id_pret")); 
          //System.out.print(", DATE PRET: " + row.get("datepret")); 
          //System.out.print(", ID_FILM: " + row.get("id_film")); 
          //System.out.print(", NOCOPIE: " + row.get("nocopie")); 
          //System.out.println(", TITRE: " + row.get("titre")); 
       }

        tx.commit();

        return result;

      } catch (Exception e) {
        if (tx != null) {
          tx.rollback();
        }
        throw e;
      } finally { 
        session.close(); 
      } 
  }

  public List getEmpruntsSansCommit(Session session, int idpersonne) {

        String sql = "SELECT E.noCopie, E.id_film, E.id_pret, P.datePret, PE.enRetard, F.titre " + 
         "FROM Exemplaire E, Pret P, Pretencours PE, Film F " + 
         "WHERE P.id_personne = :idPP AND P.id_pret = PE.id_pret AND E.id_pret = P.id_pret AND E.id_film = F.id_film";
        SQLQuery query = session.createSQLQuery(sql);
        query.setParameter("idPP", idpersonne);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List result = query.list();

        return result;

  }

  public List getEmpruntsUtilisateurSansCommit(Session session) {
    List emprunts = null;
    if (this.getUtilisateur() != null) {
      emprunts = this.getEmpruntsSansCommit(session,this.getUtilisateur().getIdPersonne());
    } else {
      this.message = "Aucun utilisateur connecté";
      System.out.println(this.message);
    }
    return emprunts;
  }

  //Nouveau
  public List getEmpruntsArchiveSansCommit(Session session, int idpersonne) {
  
        String sql = "SELECT E.noCopie, E.id_film, E.id_pret, P.datePret, F.titre " + 
         "FROM Exemplaire E, Pret P, Pretarchive PA, Film F " + 
         "WHERE P.id_personne = :idPP AND P.id_pret = PA.id_pret AND E.id_pret = P.id_pret AND E.id_film = F.id_film";
        SQLQuery query = session.createSQLQuery(sql);
        query.setParameter("idPP", idpersonne);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List result = query.list();

        return result;

      } 


  //Nouveau
  public List getEmpruntsArchiveUtilisateurSansCommit(Session session) {
    List emprunts = null;
    if (this.getUtilisateur() != null) {
      emprunts = this.getEmpruntsArchiveSansCommit(session,this.getUtilisateur().getIdPersonne());
    } else {
      this.message = "Aucun utilisateur connecté";
      System.out.println(this.message);
    }
    return emprunts;
  }

  public List getEmpruntsArchiveUtilisateur() {
    List emprunts = null;
    if (this.getUtilisateur() != null) {
      emprunts = this.getEmpruntsArchive(this.getUtilisateur().getIdPersonne());
    } else {
      this.message = "Aucun utilisateur connecté";
      System.out.println(this.message);
    }
    return emprunts;
  }

  //Nouveau
  public List getTousEmpruntsUtilisateur() {
    List res = null;
    if (this.getUtilisateur() != null) {
      List empruntsEnCours = this.getEmpruntsUtilisateur();
      List empruntsArchives = this.getEmpruntsArchiveUtilisateur();
      res.addAll(empruntsEnCours);
      res.addAll(empruntsArchives);
    } else {
      this.message = "Aucun utilisateur connecté";
      System.out.println(this.message);
    }
    return res; 
  }

  public List getTousEmpruntsUtilisateurSansCommit(Session session) {
    List res = null;
    if (this.getUtilisateur() != null) {
      List empruntsEnCours = this.getEmpruntsUtilisateurSansCommit(session);
      List empruntsArchives = this.getEmpruntsArchiveUtilisateurSansCommit(session);
      res.addAll(empruntsEnCours);
      res.addAll(empruntsArchives);
    } else {
      this.message = "Aucun utilisateur connecté";
      System.out.println(this.message);
    }
    return res; 
  }

  // à roll une fois par jour
  public void updateRetard() {
    Session session = this.sessionFactory.openSession(); 

    try { 
      tx = session.beginTransaction(); 

      SQLQuery exQuery = session.createSQLQuery("CALL " +
              "updateretard();");
      int exRows = exQuery.executeUpdate();
      this.message = "Update réussi";
      System.out.println(this.message);

      tx.commit();
    } catch (Exception e) {
      if (tx != null) {
        tx.rollback();
      }
      throw e;
    } finally { 
      session.close(); 
    } 
  }

  public List getEmpruntsEnRetard() {
    Session session = this.sessionFactory.openSession(); 

      try {
        this.tx = session.beginTransaction(); 

        System.out.println("Liste des prêt en retard"); 

        String sql = "SELECT P.*, C.prenom, C.nom, C.courriel, C.notelephone " +
        "FROM personne as C natural join pret as P natural join pretencours as PE " +
        "WHERE enretard = true";
        SQLQuery query = session.createSQLQuery(sql);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List result = query.list();

        for(Object object : result) {
          Map row = (Map)object;
          //System.out.println(row.keySet());
          System.out.print("ID_PRET: " + row.get("id_pret")); 
          System.out.print(", ID_PERSONNE: " + row.get("id_personne")); 
          System.out.print(", DATE PRET: " + row.get("datepret")); 
          System.out.print(", NOCOPIE: " + row.get("nocopie")); 
          System.out.print(", NOM CLIENT : " + row.get("prenom") + " " + row.get("nom")); 
          System.out.print(", COURRIEL: " + row.get("courriel")); 
          System.out.println(", NoTEL: " + row.get("notelephone")); 
       }

        tx.commit();

        //System.out.println(result);

        return result;

      } catch (Exception e) {
        if (tx != null) {
          tx.rollback();
        }
        throw e;
      } finally { 
        session.close(); 
      } 
  }

  public Map<String, Integer> getInfoActorByID(int noartiste) {

    Session session = this.sessionFactory.openSession(); 
    Map<String, Integer> map = new HashMap<String, Integer>();

    try {
      this.tx = session.beginTransaction(); 
      Criteria criteria = session.createCriteria(Artiste.class, "Artiste");
	  	criteria.add(Restrictions.eq("noartiste", noartiste));

      List result = criteria.list();
      Artiste artiste = (Artiste)result.get(0);

      map.put("Prenom : " + artiste.getPrenom(), 0);
      map.put("Nom : " + artiste.getNom(), 1);
      map.put("Date de naissance : " + artiste.getDatenaissance().toString(), 2);
      map.put("Lieu de naissance : " +artiste.getLieunaissance(), 3);
      map.put("Photo : " + artiste.getPhoto(), 4);
      map.put("Biographie : " + artiste.getBiographie(),5);

      tx.commit();

      //System.out.println(result);

      return map;

    } catch (Exception e) {
      if (tx != null) {
        tx.rollback();
      }
      throw e;
    } finally { 
      session.close(); 
    } 

  }

  public Film getFilmByID(int id_film) {

    Session session = this.sessionFactory.openSession(); 

    try {
      this.tx = session.beginTransaction(); 
      Criteria criteria = session.createCriteria(Film.class, "Film");
	  	criteria.add(Restrictions.eq("idFilm", id_film));

      List result = criteria.list();
      Film film = (Film)result.get(0);


      tx.commit();

      //System.out.println(result);

      return film;

    } catch (Exception e) {
      if (tx != null) {
        tx.rollback();
      }
      throw e;
    } finally { 
      session.close(); 
    } 

  }

  public Film getFilmByIDSansCommit(Session session, int id_film) {

      Criteria criteria = session.createCriteria(Film.class, "Film");
	  	criteria.add(Restrictions.eq("idFilm", id_film));

      List result = criteria.list();
      Film film = (Film)result.get(0);

      return film;

    } 

 // Nouveau
 // Méthode permettant de connaitre la cote d'un film
  
  public Double getCoteFilm(Session session, int id_film) {
    
      Double res = null;

      Criteria criteria = session.createCriteria(Cotemoyenne.class, "Cotemoyenne");
	  	criteria.add(Restrictions.eq("idFilm", id_film));

      List result = criteria.list();
      if (result.size() >0) {
        Cotemoyenne cote = (Cotemoyenne)result.get(0);
        res = cote.getMoyenne();
      }

      return res;

  }
  

  //Nouveau
  // Méthode permettant de connaitre les 10 films les plus corrélés à un film donné ainsi que les indices de corrélations
  // correspondants, ordonné du plus dans au plus petit
  public Map<Integer, Double> getRecommendation(Session session, int id_film) {
      String sql = "SELECT film_1, film_2, correlation " + 
       "FROM correlationfilms " + 
       "WHERE film_1 = :idF OR film_2 = :idF " +
       "ORDER BY correlation DESC " +
       "LIMIT 20";
      SQLQuery query = session.createSQLQuery(sql);
      query.setParameter("idF", id_film);
      query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
      List result = query.list();

      // On fait une hashmap avec comme clé l'id du film corrélé et en valeur la correlation
      Map<Integer, Double> map = new HashMap<Integer, Double>();

      for(Object object : result) {
        Map row = (Map)object;
        int film1 = (int)row.get("film_1");
        int film2 = (int)row.get("film_2");
        Double correlation = (Double)row.get("correlation");

        if (film1==id_film) {
          map.put(film2, correlation);
        } else {
          map.put(film1, correlation);
        }
     }
     Map<Integer, Double> mapsorted = sortHashMapByValues(map);
     // retourne une map ordonnée par valeur de correlation croissante
     return mapsorted;

  }

  public Map<String, Integer> getInfoFilmByID(int id_film) {
    Session session = this.sessionFactory.openSession(); 

    Map<String, Integer> map = new HashMap<String, Integer>();

    try {
      this.tx = session.beginTransaction(); 
      Criteria criteria = session.createCriteria(Film.class, "Film");
	  	criteria.add(Restrictions.eq("idFilm", id_film));

      List result = criteria.list();
      Film film = (Film)result.get(0);

      map.put("Titre : " + film.getTitre(), 0);
      map.put("Annee : " + film.getAnneeproduction().toString(), 1);
      map.put("Pays : " + film.getPaysproduction(), 2);
      map.put("VO : " + film.getLangueoriginale(), 3);
      map.put("Durée : " + film.getDuree().toString(), 4);
      Set lesGenres = film.getGenres();
      for( Iterator iterGenres = lesGenres.iterator(); iterGenres.hasNext();) {
        Genre unGenreCharge = (Genre) iterGenres.next();
        map.put("Genre : " + unGenreCharge.getNomGenre(), 5);
      }
      Set lesRealisateurs = film.getArtistes();
      for( Iterator iterRealisateurs = lesRealisateurs.iterator(); iterRealisateurs.hasNext();) {
          Artiste unRealisateurCharge = (Artiste) iterRealisateurs.next();
          map.put("Realisateur : " + unRealisateurCharge.getPrenom() + " " +unRealisateurCharge.getNom(), unRealisateurCharge.getNoartiste() );
      }
      Set lesScenaristes = film.getScenaristes();
      for( Iterator iterScenaristes = lesScenaristes.iterator(); iterScenaristes.hasNext();) {
          Scenariste unScenaristeCharge = (Scenariste) iterScenaristes.next();
          map.put("Scenariste : " + unScenaristeCharge.getId().getPrenom() + " " +unScenaristeCharge.getId().getNom(), 6 );
      } 
      Set lesActeurs = film.getActeurfilmses();
      for( Iterator iterActeurs = lesActeurs.iterator(); iterActeurs.hasNext();) {
          Acteurfilms unActeurCharge = (Acteurfilms) iterActeurs.next();
          String prenomperso = "";
          String nomperso = "";
          if (unActeurCharge.getPrenompersonnage() != "NULL") {
              prenomperso = unActeurCharge.getPrenompersonnage();
          }
          if (unActeurCharge.getNompersonnage() != "NULL") {
              nomperso = unActeurCharge.getNompersonnage();
          }
          map.put("Acteur : " + unActeurCharge.getArtiste().getPrenom() + " " + unActeurCharge.getArtiste().getNom() +
           " Dans le rôle de : " + prenomperso + " " +nomperso, unActeurCharge.getArtiste().getNoartiste() );
    }
      map.put("Resumé : " + film.getResumefilm(), 7);
      map.put("Affiche : " + film.getAffiche(), 8);

      Set lesBA = film.getBandeannonces();
        for( Iterator iterBA = lesBA.iterator(); iterBA.hasNext();) {
          Bandeannonce uneBACharge = (Bandeannonce) iterBA.next();
          map.put("Bande-annonce : " + uneBACharge.getId().getLien(), 9 );
        }

      // Implémentation ajout lab 3 pour la cote et les recommandations

      // 2.3

      // Nouveau
      
      
      Double cote = getCoteFilm(session, id_film);
      DecimalFormat df = new DecimalFormat("#.#");
      String cotestr;
      if (cote == null) {
        cotestr = "Pas de note";
      } else {
        cotestr = df.format(cote);
      }
      map.put("Note du film : " + cotestr, 10);
      

      String strReco = "";

      if (cote != null) {
      Map<Integer, Double> mapReco = getRecommendation(session, id_film);
      Set<Integer> keys = mapReco.keySet();

      List dejavu = getTousEmpruntsUtilisateurSansCommit(session);
      List<Integer> dejavuID = new ArrayList<Integer>();

      if (dejavu != null) {
        for (Object object : dejavu) {
          Map row = (Map) object;
          Integer id = (Integer) row.get("id_film");
          dejavuID.add(id);
        }
      }
      int count = 0;
        for( Iterator iterSet = keys.iterator(); iterSet.hasNext();) {
          int key =  (Integer)iterSet.next();
          System.out.println(key);
          if (!dejavuID.contains(key)) {
            Film filmReco = getFilmByIDSansCommit(session,key); 
            String titreReco = filmReco.getTitre();
            System.out.println(titreReco);
            int numReco = count + 1;
            strReco += "N°" + numReco + " : " + titreReco + " ; ";
            count += 1;
            if (count >= 3) {
              break;
            }
          }
        } 
      } else {
        strReco = "Pas de recommandation";
      }

      map.put("Recommandations : " + strReco, 11);
      
      //System.out.println(strReco);
      

      // fin ajout

      tx.commit();

      return map;

    } catch (Exception e) {
      if (tx != null) {
        tx.rollback();
      }
      throw e;
    } finally { 
      session.close(); 
    } 
  }

  // Sort par value décroissante
  public static LinkedHashMap<Integer, Double> sortHashMapByValues(Map<Integer, Double> passedMap) {
    List<Integer> mapKeys = new ArrayList<>(passedMap.keySet());
    List<Double> mapValues = new ArrayList<>(passedMap.values());
    Collections.sort(mapValues);
    Collections.reverse(mapValues);
    Collections.sort(mapKeys);

    LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<>();

    Iterator<Double> valueIt = mapValues.iterator();
    while (valueIt.hasNext()) {
        Double val = valueIt.next();
        Iterator<Integer> keyIt = mapKeys.iterator();

        while (keyIt.hasNext()) {
            Integer key = keyIt.next();
            Double comp1 = passedMap.get(key);
            Double comp2 = val;

            if (comp1.equals(comp2)) {
                keyIt.remove();
                sortedMap.put(key, val);
                break;
            }
        }
    }
    return sortedMap;
}

  /****************************************************************
   * Retourne une liste de film en fonction des criteres de recherche      *
   ****************************************************************/
  public List<Film> findAllFilmsByCriteria(Integer idFilm,String titre,Integer anneeproductionMin, Integer anneeproductionMax,String paysproduction, String langueoriginale,
  String genre, String acteur,String realisateur)
  {
	  Session session = this.sessionFactory.openSession(); 
      List<Film> lesFilms = null;
        
      try {
          tx = session.beginTransaction();
          
          lesFilms = rechercher(session,idFilm,titre,anneeproductionMin,anneeproductionMax,paysproduction,langueoriginale,
        		  genre, acteur, realisateur);
           
          
          Boolean afficher = false;

          if (afficher) {
            //Iteres sur chacun des films pour afficher l information
            for ( Iterator iterFilms = lesFilms.iterator(); iterFilms.hasNext(); ) {
              Film unFilmCharge = (Film) iterFilms.next();
              //Verification que les valeurs sont bien trouvees par la querry pour chaque atrbut d un film
              System.out.println("Film: " + unFilmCharge.getTitre() );
              System.out.println("IdFilm: " + unFilmCharge.getIdFilm() );
              System.out.println("Date de production: " + unFilmCharge.getAnneeproduction());
              System.out.println("Resume: " + unFilmCharge.getResumefilm());  
              System.out.println("Pays production: " + unFilmCharge.getPaysproduction());
              System.out.println("Langue originale: " + unFilmCharge.getLangueoriginale());
              //Iter sur chacun des réalisateurs (il n'y en a qu'un seul dans notre cas mais 
              //le schema SQL permettrait d'en avoir plusieurs) pour savoir leurs informations
              Set lesRealisateurs = unFilmCharge.getArtistes();
              for( Iterator iterRealisateurs = lesRealisateurs.iterator(); iterRealisateurs.hasNext();) {
                Artiste unRealisateurCharge = (Artiste) iterRealisateurs.next();
                System.out.println("Realisateur : " + unRealisateurCharge.getPrenom() + " " +unRealisateurCharge.getNom() );
              }
              //Iter sur chacun des acteurs pour savoir leurs informations
              Set lesActeurs = unFilmCharge.getActeurfilmses();
              for( Iterator iterActeurs = lesActeurs.iterator(); iterActeurs.hasNext();) {
                Acteurfilms unActeurCharge = (Acteurfilms) iterActeurs.next();
                System.out.println("Acteur : " + unActeurCharge.getArtiste().getPrenom() + " " + unActeurCharge.getArtiste().getNom());
                System.out.println("Personnage : " + unActeurCharge.getPrenompersonnage() + " " +unActeurCharge.getNompersonnage() );
              }
              //Iter sur chacun des scenaristes pour savoir leurs informations
              Set lesScenaristes = unFilmCharge.getScenaristes();
              for( Iterator iterScenaristes = lesScenaristes.iterator(); iterScenaristes.hasNext();) {
                Scenariste unScenaristeCharge = (Scenariste) iterScenaristes.next();
                System.out.println("Scenariste : " + unScenaristeCharge.getId().getPrenom() + " " +unScenaristeCharge.getId().getNom() );
              }
              //Iter sur chacun des genres pour savoir leurs informations
              Set lesGenres = unFilmCharge.getGenres();
              for( Iterator iterGenres = lesGenres.iterator(); iterGenres.hasNext();) {
                Genre unGenreCharge = (Genre) iterGenres.next();
                System.out.println("Genre : " + unGenreCharge.getNomGenre());
              }
              // Iter sur les bande annonces
              Set lesBA = unFilmCharge.getBandeannonces();
              for( Iterator iterBA = lesBA.iterator(); iterBA.hasNext();) {
                Bandeannonce uneBACharge = (Bandeannonce) iterBA.next();
                System.out.println("Bande-annonce : " + uneBACharge.getId().getLien() );
              }

              System.out.println();
            }
          } 
          if(lesFilms.size() == 0) {
        	  System.out.println("Aucun film ne correspond a cette recherche");
          }
          tx.commit();
      }
      catch (Exception e) {
          if (tx != null) {
            tx.rollback();
          }
          throw e;
        } finally { 
          session.close(); 
         
      }
      return lesFilms;
  }
  
  
  
  
  /**************************
   *  CRITERES DE RECHERCHE *
   **************************/
  public List rechercher(Session session,
		  Integer idFilm,
          String titre,
          Integer anneeproductionMin,
          Integer anneeproductionMax,
          String paysproduction,
          String langueoriginale,
          String genre,         
          String acteur,
          String realisateur) {
	  

      Criteria criteria = session.createCriteria(Film.class, "Film");
	  		  
		  //Criteres pour ID,titre,date, langue et pays
	  	if (idFilm != null) {
	  		criteria.add(Restrictions.eq("idFilm", idFilm));
	  	}
	  	if (titre != null ) {
	  		criteria.add(Restrictions.ilike("titre", "%"+titre+"%"));
	  	}
	  	if (anneeproductionMin != null) {
	  		criteria.add(Restrictions.ge("anneeproduction", anneeproductionMin));
	  	}
	  	if (anneeproductionMax != null) {
	  		criteria.add(Restrictions.le("anneeproduction", anneeproductionMax));
	  	}
	  	if (paysproduction != null) {
	  		criteria.add(Restrictions.ilike("paysproduction", "%"+paysproduction+"%"));
	  	}
	 	  if (langueoriginale != null) {
	  		criteria.add(Restrictions.ilike("langueoriginale", "%"+langueoriginale+"%"));
	  	}
	 		
      // Critère sur l'acteur
      if (acteur != null) {	  		
        criteria.createAlias("Film.acteurfilmses", "af");
        criteria.createAlias("af.artiste", "a");
	  		Criterion checkprenomAc = Restrictions.ilike("a.prenom", "%"+acteur+"%");
        Criterion checknomAc = Restrictions.ilike("a.nom", "%"+acteur+"%");
        LogicalExpression orExpAc = Restrictions.or(checknomAc, checkprenomAc);
        criteria.add(orExpAc);
	  	}
        
      // Critère sur le réalisateur
	 	  if (realisateur != null) {	
        criteria.createAlias("Film.artistes", "ar");
	  		Criterion checkprenomRe = Restrictions.ilike("ar.prenom", "%"+realisateur+"%");
        Criterion checknomRe = Restrictions.ilike("ar.nom", "%"+realisateur+"%");
        LogicalExpression orExpRe = Restrictions.or(checknomRe, checkprenomRe);
        criteria.add(orExpRe);
	  	}

      // Critère sur le genre
	 	  if (genre != null) {	  
        criteria.createAlias("Film.genres", "g");		
	  		criteria.add(Restrictions.ilike("g.nomGenre", "%"+genre+"%"));
	  	}

	  	
	  	List resultat = criteria.list();

return resultat;
} 

// Nouveau

// Methode pour l'analyse des données de location : API lab3
// 4 paramètres : groupe age, province, jour (en anglais), mois (en anglais)
/* groupe age : 
0 : 0 - 5 ; 
1 : 5 - 10 ;
2 : 10 - 15 ; 
3 : 15 - 20 ;
4 : 20 - 25 ;
5 : 25 - 30 ;
6 : 30 - 35 ;
7 : 35 - 40 ;
8 : 40 - 45 ; 
9 : 45 - 50 ;
10 : 50 - 55 ;
11 : 55 - 60 ; 
12 : 60 - 65 ; 
13 : 65 - 70 ;
14 : 70 - 75 ;
15 : 75 - 80 ;
16 : 80 - 85 ; 
17 : 85 - 90 ;
18 : 90 - 95 ; 
19 : 95 - 100 ;
20 : Tous
*/

// Nouveau
public void analyse(int groupeage, String province, String jour, String mois) {
    Session session = this.sessionFactory.openSession(); 

    try { 
      tx = session.beginTransaction(); 

      Criteria criteria = session.createCriteria(Faitslocationfilm.class, "Faitslocationfilm");
	 		
      criteria.createAlias("Faitslocationfilm.dimensionclient", "dc");
      // Critère sur le groupe age
      if (groupeage != 20) {
	  		Criterion checkGroupeAge = Restrictions.eq("dc.groupeage", groupeage);
        criteria.add(checkGroupeAge);
	  	}
        
      // Critère sur la province
	 	  if (province != "Tous") {	
	  		Criterion checkProvince = Restrictions.ilike("dc.province", "%"+province+"%");
        criteria.add(checkProvince);
	  	}

      criteria.createAlias("Faitslocationfilm.dimensiontemps", "dt");
      // Critère sur le jour
	 	  if (jour != "Tous") {	  	
        Criterion checkJour = Restrictions.ilike("dt.joursemaine", "%"+jour+"%");
	  		criteria.add(checkJour);
	  	}
      // Critère sur le mois
	 	  if (mois != "Tous") {	  		
        Criterion checkMois = Restrictions.ilike("dt.moisannee", "%"+mois+"%");
	  		criteria.add(checkMois);
	  	}

	  	
	  	List resultat = criteria.list();

      String groupeagestr = groupeage*5 + "-" + (groupeage*5 + 5) + " ans (Groupe " + groupeage + ")";
      if (groupeage == 20) {
        groupeagestr = "Tous";
      }

      System.out.println("Critères : \n Groupe âge : " + groupeagestr + " \n Province : " +
                  province + " \n Jour : " + jour + " \n Mois : " + mois + " \n \n Nombre de locations : " + resultat.size());
      System.out.println("\nid des locations(pret) : ");

      for ( Iterator iterfait = resultat.iterator(); iterfait.hasNext(); ) {
        Faitslocationfilm fait = (Faitslocationfilm) iterfait.next();

        System.out.println("---------");
        System.out.println("idLocationFilm: " + fait.getidlocationfilm() );
        
        /*
        System.out.println("|idClient: " + fait.getdimensionclient().getidclient());
        System.out.println("|Nom Client: " + fait.getdimensionclient().getnomcomplet() );
        System.out.println("|Nom Client: " + fait.getdimensionclient().getgroupeage() );
        System.out.println("|idFilm: " + fait.getdimensionfilm().getidfilm());
        System.out.println("|Titre: " + fait.getdimensionfilm().gettitre());
        System.out.println("|idDate: " + fait.getdimensiontemps().getiddatelocation());
        System.out.println("|Heure: " + fait.getdimensiontemps().getheurejour());
        System.out.println("|Jour: " + fait.getdimensiontemps().getjoursemaine());
        System.out.println("|Mois: " + fait.getdimensiontemps().getmoisannee());
        */
      }

      tx.commit();
    } catch (Exception e) {
      if (tx != null) {
        tx.rollback();
      }
      throw e;
    } finally { 
      session.close(); 
    } 
  }


  public void analyseclient() {
    Session session = this.sessionFactory.openSession(); 

    try { 
      tx = session.beginTransaction(); 

      Criteria criteria = session.createCriteria(Dimensionclient.class, "Dimensionclient");
	  	
	  	List resultat = criteria.list();
      
      for ( Iterator iterDC = resultat.iterator(); iterDC.hasNext(); ) {
        Dimensionclient DC = (Dimensionclient) iterDC.next();

        System.out.println("##########################");
    
        System.out.println(DC.getidclient() );
        System.out.println(DC.getnomcomplet() );
        System.out.println(DC.getcourriel());
        System.out.println(DC.getcodepostal());
        System.out.println(DC.getville());
        System.out.println(DC.getprovince());
        System.out.println(DC.getgroupeage());
        
        System.out.println("##########################");
      }

      tx.commit();
    } catch (Exception e) {
      if (tx != null) {
        tx.rollback();
      }
      throw e;
    } finally { 
      session.close(); 
    } 
  }

  public void analysetemps() {
    Session session = this.sessionFactory.openSession(); 

    try { 
      tx = session.beginTransaction(); 

      Criteria criteria = session.createCriteria(Dimensiontemps.class, "Dimensiontemps");
	  	
	  	List resultat = criteria.list();
      
      for ( Iterator iterDT = resultat.iterator(); iterDT.hasNext(); ) {
        Dimensiontemps DT = (Dimensiontemps) iterDT.next();

        System.out.println("##########################");
    
        System.out.println(DT.getiddatelocation() );
        System.out.println(DT.getdatelocation() );
        System.out.println(DT.getheurejour());
        System.out.println(DT.getjoursemaine());
        System.out.println(DT.getmoisannee());
        
        System.out.println("##########################");
      }

      tx.commit();
    } catch (Exception e) {
      if (tx != null) {
        tx.rollback();
      }
      throw e;
    } finally { 
      session.close(); 
    } 
  }

  public void analyseexemplaire(Integer id_film) {
    Session session = this.sessionFactory.openSession(); 

    try { 
      tx = session.beginTransaction(); 

      Criteria criteria = session.createCriteria(Exemplaire.class, "Exemplaire");

      if (id_film != null) {
        criteria.createAlias("Exemplaire.film", "film");
	  		criteria.add(Restrictions.eq("film.idFilm", id_film));
	  	}
	  	
	  	List resultat = criteria.list();
      
      for ( Iterator iterF = resultat.iterator(); iterF.hasNext(); ) {
        Exemplaire exemp = (Exemplaire) iterF.next();
        
        System.out.println("##########################");
        System.out.println(exemp.getNocopie());
        System.out.println(exemp.getStatut() );
        System.out.println("##########################");
      }

      tx.commit();
    } catch (Exception e) {
      if (tx != null) {
        tx.rollback();
      }
      throw e;
    } finally { 
      session.close(); 
    } 
  }

  public void analysefilm() {
    Session session = this.sessionFactory.openSession(); 

    try { 
      tx = session.beginTransaction(); 

      Criteria criteria = session.createCriteria(Dimensionfilm.class, "Dimensionfilm");
	  	
	  	List resultat = criteria.list();
      
      for ( Iterator iterDF = resultat.iterator(); iterDF.hasNext(); ) {
        Dimensionfilm DF = (Dimensionfilm) iterDF.next();

        System.out.println("##########################");
    
        System.out.println(DF.getidfilm() );
        System.out.println(DF.gettitre() );
        System.out.println(DF.getanneesortie());
        System.out.println(DF.getpaysorigine());
        System.out.println(DF.getactionfilm());
        System.out.println(DF.getanimation());
        System.out.println(DF.getaventure());
        System.out.println(DF.getbiography());
        System.out.println(DF.getcomedy());
        System.out.println(DF.getdrama());
        System.out.println(DF.getfamily());
        System.out.println(DF.getfantasy());
        System.out.println(DF.getromance());
        System.out.println(DF.getscifi());
        System.out.println(DF.getthriller());
        
        System.out.println("##########################");
      }

      tx.commit();
    } catch (Exception e) {
      if (tx != null) {
        tx.rollback();
      }
      throw e;
    } finally { 
      session.close(); 
    } 
  }


  public static void main(String args[]) throws Exception {
    HibernateQueries test = new HibernateQueries();

    
    //test.connextionUtilisateur("RobertCFlores21@gmail.com", "eishie3meiH"); // utilisateur A
    // MichaelCVelasquez5@gmail.com ; mif2Phoiw
    //test.connextionUtilisateur("TravisMDouglas83@gmail.com", "bohgh8Cae"); // utilisateur D

    //test.louerFilmUtilisateur(118884);

    /*
    test.nbExemplaireDispo(119190);
    test.nbExemplaireDispo(118884);

    test.louerFilmUtilisateur(119190);
    test.louerFilmUtilisateur(118884);

    test.nbExemplaireDispo(119190);
    test.nbExemplaireDispo(118884);

    test.louerFilmUtilisateur(118884);

    test.nbExemplaireDispo(118884);

    test.getEmpruntsUtilisateur();

    test.deconnextionUtilisateur();

    */
    test.findAllFilmsByCriteria(198781, null, null, null, null, null, null, null, null);

    //test.deconnextionUtilisateur();
    test.closeSessionFactory();
  } 
}