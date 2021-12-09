package labo1.Script_de_lecture;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/*
Équipe 3 : labo1 du cours LOG660
Script JAVA faisant interface avec la base de donnée
La méthode main de ce fichier permet de générer la base de donnée postgreSQL nommée "dvdrental"
La classe LectureBD de manipuler la BD

Écrit par Olivier St-Pierre
*/

public class LectureBD {

   public class Role {
      public Role(int i, String n, String p) {
         id = i;
         nom = n;
         personnage = p;
      }
      protected int id;
      protected String nom;
      protected String personnage;
   }
   
   public LectureBD() {
      connection = null;
      connectionBD();
      genresSet = new HashSet<String>();
   }

   private Connection connection;
   private Statement statement;
   private Set<String> genresSet;

   // Méthode pour la lecture des fichiers de personnes en xml

   public void lecturePersonnes(String nomFichier){
      try {
         XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
         XmlPullParser parser = factory.newPullParser();

         InputStream is = new FileInputStream(nomFichier);
         parser.setInput(is, null);

         int eventType = parser.getEventType();

         String tag = null, 
                nom = null,
                anniversaire = null,
                lieu = null,
                photo = null,
                bio = null;
         
         int id = -1;
         
         while (eventType != XmlPullParser.END_DOCUMENT) 
         {
            if(eventType == XmlPullParser.START_TAG) 
            {
               tag = parser.getName();
               
               if (tag.equals("personne") && parser.getAttributeCount() == 1)
                  id = Integer.parseInt(parser.getAttributeValue(0));
            } 
            else if (eventType == XmlPullParser.END_TAG) 
            {                              
               tag = null;
               
               if (parser.getName().equals("personne") && id >= 0)
               {
                  insertionPersonne(id,nom,anniversaire,lieu,photo,bio);
                                    
                  id = -1;
                  nom = null;
                  anniversaire = null;
                  lieu = null;
                  photo = null;
                  bio = null;
               }
            }
            else if (eventType == XmlPullParser.TEXT && id >= 0) 
            {
               if (tag != null)
               {                                    
                  if (tag.equals("nom"))
                     nom = parser.getText();
                  else if (tag.equals("anniversaire"))
                     anniversaire = parser.getText();
                  else if (tag.equals("lieu"))
                     lieu = parser.getText();
                  else if (tag.equals("photo"))
                     photo = parser.getText();
                  else if (tag.equals("bio"))
                     bio = parser.getText();
               }              
            }
            
            eventType = parser.next();            
         }
      }
      catch (XmlPullParserException e) {
          System.out.println(e);   
       }
       catch (IOException e) {
         System.out.println("IOException while parsing " + nomFichier); 
       }
   }   
   
   // Méthode pour la lecture des fichiers de films en xml

   public void lectureFilms(String nomFichier){
      try {
         XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
         XmlPullParser parser = factory.newPullParser();

         InputStream is = new FileInputStream(nomFichier);
         parser.setInput(is, null);

         int eventType = parser.getEventType();

         int nbinsertion = 0;

         String tag = null, 
                titre = null,
                langue = null,
                poster = null,
                roleNom = null,
                rolePersonnage = null,
                realisateurNom = null,
                resume = null;
         
         ArrayList<String> pays = new ArrayList<String>();
         ArrayList<String> genres = new ArrayList<String>();
         ArrayList<String> scenaristes = new ArrayList<String>();
         ArrayList<Role> roles = new ArrayList<Role>();         
         ArrayList<String> annonces = new ArrayList<String>();
         
         int id = -1,
             annee = -1,
             duree = -1,
             roleId = -1,
             realisateurId = -1;
         
         while (eventType != XmlPullParser.END_DOCUMENT) 
         {
            if(eventType == XmlPullParser.START_TAG) 
            {
               tag = parser.getName();
               
               if (tag.equals("film") && parser.getAttributeCount() == 1)
                  id = Integer.parseInt(parser.getAttributeValue(0));
               else if (tag.equals("realisateur") && parser.getAttributeCount() == 1)
                  realisateurId = Integer.parseInt(parser.getAttributeValue(0));
               else if (tag.equals("acteur") && parser.getAttributeCount() == 1)
                  roleId = Integer.parseInt(parser.getAttributeValue(0));
            } 
            else if (eventType == XmlPullParser.END_TAG) 
            {                              
               tag = null;
               
               if (parser.getName().equals("film") && id >= 0)
               {
                  nbinsertion++;

                  insertionFilm(id,titre,annee,pays,langue,
                             duree,resume,genres,realisateurNom,
                             realisateurId, scenaristes,
                             roles,poster,annonces, nbinsertion);
                  
                                    
                  id = -1;
                  annee = -1;
                  duree = -1;
                  titre = null;                                 
                  langue = null;                  
                  poster = null;
                  resume = null;
                  realisateurNom = null;
                  roleNom = null;
                  rolePersonnage = null;
                  realisateurId = -1;
                  roleId = -1;
                  
                  genres.clear();
                  scenaristes.clear();
                  roles.clear();
                  annonces.clear();  
                  pays.clear();
               }
               if (parser.getName().equals("role") && roleId >= 0) 
               {              
                  roles.add(new Role(roleId, roleNom, rolePersonnage));
                  roleId = -1;
                  roleNom = null;
                  rolePersonnage = null;
               }
            }
            else if (eventType == XmlPullParser.TEXT && id >= 0) 
            {
               if (tag != null)
               {                                    
                  if (tag.equals("titre"))
                     titre = parser.getText();
                  else if (tag.equals("annee"))
                     annee = Integer.parseInt(parser.getText());
                  else if (tag.equals("pays"))
                     pays.add(parser.getText());
                  else if (tag.equals("langue"))
                     langue = parser.getText();
                  else if (tag.equals("duree"))                 
                     duree = Integer.parseInt(parser.getText());
                  else if (tag.equals("resume"))                 
                     resume = parser.getText();
                  else if (tag.equals("genre"))
                     genres.add(parser.getText());
                  else if (tag.equals("realisateur"))
                     realisateurNom = parser.getText();
                  else if (tag.equals("scenariste"))
                     scenaristes.add(parser.getText());
                  else if (tag.equals("acteur"))
                     roleNom = parser.getText();
                  else if (tag.equals("personnage"))
                     rolePersonnage = parser.getText();
                  else if (tag.equals("poster"))
                     poster = parser.getText();
                  else if (tag.equals("annonce"))
                     annonces.add(parser.getText());                  
               }              
            }
            
            eventType = parser.next();            
         }
      }
      catch (XmlPullParserException e) {
          System.out.println(e);   
      }
      catch (IOException e) {
         System.out.println("IOException while parsing " + nomFichier); 
      }
   }
   
   // Méthode pour la lecture des fichiers de clients en xml

   public void lectureClients(String nomFichier){
      try {
         XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
         XmlPullParser parser = factory.newPullParser();

         InputStream is = new FileInputStream(nomFichier);
         parser.setInput(is, null);

         int eventType = parser.getEventType();               

         String tag = null, 
                nomFamille = null,
                prenom = null,
                courriel = null,
                tel = null,
                anniv = null,
                adresse = null,
                ville = null,
                province = null,
                codePostal = null,
                carte = null,
                noCarte = null,
                motDePasse = null,
                forfait = null;                                 
         
         int id = -1,
             expMois = -1,
             expAnnee = -1;
         
         while (eventType != XmlPullParser.END_DOCUMENT) 
         {
            if(eventType == XmlPullParser.START_TAG) 
            {
               tag = parser.getName();
               
               if (tag.equals("client") && parser.getAttributeCount() == 1)
                  id = Integer.parseInt(parser.getAttributeValue(0));
            } 
            else if (eventType == XmlPullParser.END_TAG) 
            {                              
               tag = null;
               
               if (parser.getName().equals("client") && id >= 0)
               {
                  insertionClient(id,nomFamille,prenom,courriel,tel,
                             anniv,adresse,ville,province,
                             codePostal,carte,noCarte, 
                             expMois,expAnnee,motDePasse,forfait);               
                                    
                  nomFamille = null;
                  prenom = null;
                  courriel = null;               
                  tel = null;
                  anniv = null;
                  adresse = null;
                  ville = null;
                  province = null;
                  codePostal = null;
                  carte = null;
                  noCarte = null;
                  motDePasse = null; 
                  forfait = null;
                  
                  id = -1;
                  expMois = -1;
                  expAnnee = -1;
               }
            }
            else if (eventType == XmlPullParser.TEXT && id >= 0) 
            {         
               if (tag != null)
               {                                    
                  if (tag.equals("nom-famille"))
                     nomFamille = parser.getText();
                  else if (tag.equals("prenom"))
                     prenom = parser.getText();
                  else if (tag.equals("courriel"))
                     courriel = parser.getText();
                  else if (tag.equals("tel"))
                     tel = parser.getText();
                  else if (tag.equals("anniversaire"))
                     anniv = parser.getText();
                  else if (tag.equals("adresse"))
                     adresse = parser.getText();
                  else if (tag.equals("ville"))
                     ville = parser.getText();
                  else if (tag.equals("province"))
                     province = parser.getText();
                  else if (tag.equals("code-postal"))
                     codePostal = parser.getText();
                  else if (tag.equals("carte"))
                     carte = parser.getText();
                  else if (tag.equals("no"))
                     noCarte = parser.getText();
                  else if (tag.equals("exp-mois"))                 
                     expMois = Integer.parseInt(parser.getText());
                  else if (tag.equals("exp-annee"))                 
                     expAnnee = Integer.parseInt(parser.getText());
                  else if (tag.equals("mot-de-passe"))                 
                     motDePasse = parser.getText();  
                  else if (tag.equals("forfait"))                 
                     forfait = parser.getText(); 
               }              
            }
            
            eventType = parser.next();            
         }
      }
      catch (XmlPullParserException e) {
          System.out.println(e);   
      }
      catch (IOException e) {
         System.out.println("IOException while parsing " + nomFichier); 
      }
   }   
   // Méthode pour l'insertion des personnes dans la BD
   
   private void insertionPersonne(int id, String nom, String dateNaissance, String lieuNaissance, String photo, String biographie) {
      try {
         var prenomNomSplit = prenomNomSplit(nom);
         var prenom = prenomNomSplit[0];
         nom = prenomNomSplit[1];

         nom = stringConvertion(nom);
         prenom = stringConvertion(prenom);
         dateNaissance = stringConvertion(dateNaissance);
         lieuNaissance = stringConvertion(lieuNaissance);
         photo = stringConvertion(photo);
         biographie = stringConvertion(biographie);
         String stat = "INSERT INTO Artiste (noArtiste, prenom, nom, dateNaissance, lieuNaissance, photo, biographie) " +
                 "VALUES ("
                 + id + ","
                 + prenom + ","
                 + nom + ","
                 + dateNaissance + ","
                 + lieuNaissance + ","
                 + photo + ","
                 + biographie +")";
         statement.addBatch(stat);
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }
   // Méthode pour l'insertion des films dans la BD

   private void insertionFilm(int id, String titre, int annee,
                           ArrayList<String> pays, String langue, int duree, String resume,
                           ArrayList<String> genres, String realisateurNom, int realisateurId,
                           ArrayList<String> scenaristes,
                           ArrayList<Role> roles, String poster,
                           ArrayList<String> annonces, int nbinsertion) {
      try {
         var quantiteStock = ThreadLocalRandom.current().nextInt(1, 101); // Entre 1 a 100

         titre = stringConvertion(titre);
         langue = stringConvertion(langue);
         resume = stringConvertion(resume);
         realisateurNom = stringConvertion(realisateurNom);
         poster = stringConvertion(poster);

         // Insertion des pays
         var strBuildPays = new StringBuilder();
         strBuildPays.append(pays.get(0));
         int i = 1;
         while (i < pays.size()) {
            strBuildPays.append(", ");
            strBuildPays.append(pays.get(i));
            ++i;
         }
         var strPays = stringConvertion(strBuildPays.toString());

         String stat = "INSERT INTO Film (id_film, titre, anneeProduction, paysProduction, duree, langueOriginale, resumeFilm, affiche, quantiteStock) " +
                 "VALUES ("
                 + id + ", "
                 + titre + ", "
                 + annee + ", "
                 + strPays + ", "
                 + duree + ", "
                 + langue + ", "
                 + resume + ", "
                 + poster + ", "
                 + quantiteStock +")";
         statement.addBatch(stat);

         // Insertion des GenreFilm
         for (var genre: genres) {
            if (!(genresSet.contains(genre))) {

               stat = "INSERT INTO Genre (nom_genre) " +
                       "VALUES ("
                       + stringConvertion(genre) + ")";
               statement.addBatch(stat);
               genresSet.add(genre);
            }
            stat = "INSERT INTO GenreFilm (id_film, nom_genre) " +
                    "VALUES ("
                    + id + ", "
                    + stringConvertion(genre) + ")";
            statement.addBatch(stat);
         }

         // Insertion du realisateur
         if (realisateurId != -1) {
            stat = "INSERT INTO RealisateurFilms (id_film, noArtiste) " +
                    "VALUES ("
                    + id + ", "
                    + realisateurId + ")";
            statement.addBatch(stat);
         }

         // Insertion des roles
         for (var role: roles) {
            var prenomNomSplit = prenomNomSplit(role.personnage);
            var prenomPerso = stringConvertion(prenomNomSplit[0]);
            var nomPerso = stringConvertion(prenomNomSplit[1]);

            stat = "INSERT INTO ActeurFilms (id_film, noArtiste, prenomPersonnage, nomPersonnage) " +
                    "VALUES ("
                    + id + ", "
                    + role.id + ", "
                    + prenomPerso + ", "
                    + nomPerso +")";
            statement.addBatch(stat);
         }

         // Insertion des scenariste
         for (var scenariste: scenaristes) {
            var prenomNomSplit = prenomNomSplit(scenariste);
            var prenomPerso = stringConvertion(prenomNomSplit[0]);
            var nomPerso = stringConvertion(prenomNomSplit[1]);

            stat = "INSERT INTO Scenariste (id_film, nom, prenom) " +
                    "VALUES ("
                    + id + ", "
                    + nomPerso + ", "
                    + prenomPerso + ")";
            statement.addBatch(stat);
         }

         // Insertion des annonces
         for (var annonce: annonces) {
            stat = "INSERT INTO BandeAnnonce (id_film, lien) " +
                    "VALUES ("
                    + id + ", "
                    + stringConvertion(annonce) + ")";
            statement.addBatch(stat);
         }

         insertionExemplaire(id, quantiteStock, nbinsertion);

      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   // Méthode pour l'insertion des clients dans la BD

   private void insertionClient(int id, String nom, String prenom,
                                String courriel, String noTelephone, String dateNaissance,
                                String adresse, String ville, String province,
                                String codePostal, String carte, String noCarte,
                                int expMois, int expAnnee, String mdp,
                                String forfait) {
      try {
         nom = stringConvertion(nom);
         prenom = stringConvertion(prenom);
         noTelephone = stringConvertion(noTelephone);
         courriel = stringConvertion(courriel);
         mdp = stringConvertion(mdp);
         dateNaissance = stringConvertion(dateNaissance);
         codePostal = stringConvertion(codePostal);
         province = stringConvertion(province);
         ville = stringConvertion(ville);
         adresse = stringConvertion(adresse);

         String stat = "INSERT INTO Personne (id_personne, prenom, nom, courriel, noTelephone, mdp, dateNaissance," +
                 "province, codePostal, ville, adresse) " +
                 "VALUES ("
                 + id + ","
                 + prenom + ","
                 + nom + ","
                 + courriel + ","
                 + noTelephone + ","
                 + mdp + ","
                 + dateNaissance + ","
                 + province + ","
                 + codePostal + ","
                 + ville + ","
                 + adresse +")";
         statement.addBatch(stat);

         // Insertion client
         forfait = stringConvertion(forfait);
         stat = "INSERT INTO Client (id_personne, CodeAbo, nbFilmsActuellementEmpruntes) " +
                 "VALUES ("
                 + id + ","
                 + forfait + ","
                 + 0 + ")";
         statement.addBatch(stat);

         // Insertion carte credit
         carte = stringConvertion(carte);
         noCarte = stringConvertion(noCarte);
         stat = "INSERT INTO CarteCredit (id_personne, typeCarte, numeroCarte, dateExpiration) " +
                 "VALUES ("
                 + id + ","
                 + carte + ","
                 + noCarte + ","
                 + "date '" + "2030" + "-" + "11" + "-01')";
         statement.addBatch(stat);
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   // Méthode pour l'insertion des exemplaires de films dans la BD

   private void insertionExemplaire(int id_film, int quantite, int nbinsertion) {
      try {
         for (int numcopie = 1 ; numcopie <= quantite ; numcopie++) {
            int copie = nbinsertion*100 + numcopie;
            String stat = "INSERT INTO Exemplaire (nocopie, id_film, statut) " +
                 "VALUES ("
                 + copie + ","
                 + id_film + ","
                 + "'disponible'" + ")";
            statement.addBatch(stat);
         }
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public static Set<Integer> createSetInt(Statement statement, String query) {
      var set = new HashSet<Integer>();
      try {
         ResultSet rs = statement.executeQuery(query);
         while (rs.next()) {
            set.add(rs.getInt(0));
         }
         return set;
      }
      catch(Exception e) {
         return null;
      }
   }

   public static Set<String> createSet(Statement statement, String query, int numberOfColum) {
      var set = new HashSet<String>();
      try {
         ResultSet rs = statement.executeQuery(query);
         while (rs.next()) {
            StringBuilder data = new StringBuilder();
            for (int i = 1; i <= numberOfColum; ++i) {
               data.append(rs.getString(i));
            }
            set.add(data.toString());
         }
         return set;
      }
      catch(Exception e) {
         return null;
      }
   }

   public static String stringConvertion(String param) {
      if (param == null) {
         return "NULL";
      } else {
         return "'" + param.replace("'", "''") + "'";
      }
   }

   public static String[] prenomNomSplit(String param) {
      var prenomNomSplit = param.split(" ");
      var prenomNom = new String[]{"NULL", param};
      if (prenomNomSplit.length == 2) {
         prenomNom[0] = prenomNomSplit[0];
         prenomNom[1] = prenomNomSplit[1];
      }
      return prenomNom;
   }

   public Connection getConnection() {
      return this.connection;
   }

   public Statement getStatement() {
      return this.statement;
   }
   
   private void connectionBD() {
      try
      {
         // create a database connection
         // Connexion sur la BD par défaut
         // Rajouter le nom de la DB après 5432/ : "jdbc:postgresql://localhost:5432/<nom_DB>"
         connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dvdrental", "postgres", "password");
         connection.setAutoCommit(false);
         statement = connection.createStatement();
         statement.setQueryTimeout(60);
      }
      catch(SQLException e)
      {
         // if the error message is "out of memory",
         // it probably means no database file is found
         System.err.println(e.getMessage());
      }
      finally
      {
//         try
//         {
//            if(connection != null)
//               connection.close();
//         }
//         catch(SQLException e)
//         {
//            // connection close failed.
//            System.err.println(e.getMessage());
//         }
      }
   }

   public void importSqlFile(String file) {
      try {
         String schema = Files.readString(Path.of(file));
         statement.executeUpdate(schema);

         connection.commit();
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public void createProcedures() {
      try {
         String schema = Files.readString(Path.of("src/labo1/Script_de_lecture/procedurePsql.sql"));
         statement.executeUpdate(schema);

         connection.commit();
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public void closeConnection() {
      try {
         this.connection.close();
      } 
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public void createTables() {
      try {
         String schema = Files.readString(Path.of("src/labo1/Script_de_lecture/reset.sql"));
         statement.executeUpdate(schema);

         schema = Files.readString(Path.of("src/labo1/Script_de_lecture/SchemaPSQL.sql"));
         statement.executeUpdate(schema);

         schema = Files.readString(Path.of("src/labo1/Script_de_lecture/procedurePsql.sql"));
         statement.executeUpdate(schema);

         schema = Files.readString(Path.of("src/labo1/Script_de_lecture/SchemaPSQL_InsertionDonnees.sql"));
         statement.executeUpdate(schema);         

         connection.commit();
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public void createETL() {
      try {
         String schema = Files.readString(Path.of("src/labo1/Script_de_lecture/schemaEtoile.sql"));
         statement.executeUpdate(schema);

         connection.commit();

      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public void createCote() {
      try {
         String schema = Files.readString(Path.of("src/labo1/Script_de_lecture/schemaCotes.sql"));
         statement.executeUpdate(schema);

         schema = Files.readString(Path.of("src/labo1/Script_de_lecture/insert_cote.sql"));
         statement.executeUpdate(schema);

         connection.commit();
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public void createMView() {
      try {
         String schema = Files.readString(Path.of("src/labo1/Script_de_lecture/creationView.sql"));
         statement.executeUpdate(schema);
   
         connection.commit();
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }


   public static void main(String[] args) throws SQLException {
      LectureBD lecture = new LectureBD();
      int[] results = null;
      lecture.createTables();  // create tables and procedures
      lecture.createETL();   // create etl pour analyse de données
      lecture.createCote();   // creation table des côtes
      lecture.createMView();  // creation des view

      if (args.length > 0 ) {
      lecture.lecturePersonnes(args[0]);
      lecture.lectureFilms(args[1]);
      lecture.lectureClients(args[2]);
      }
      else {
      lecture.lecturePersonnes("donnees_db_latin1/personnes_latin1.xml");
      lecture.lectureFilms("donnees_db_latin1/films_latin1.xml");
      lecture.lectureClients("donnees_db_latin1/clients_latin1.xml");

      }

      try {
         results = lecture.statement.executeBatch();
         lecture.connection.commit();
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
      lecture.connection.close();
   }
}
