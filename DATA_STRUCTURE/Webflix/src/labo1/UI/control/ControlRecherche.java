package labo1.UI.control;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import labo1.Hibernate.src.Film;
import labo1.methodes.HibernateQueries;

//Controlleur qui gère la recherche de film avec le bouton rechercher

public class ControlRecherche implements ActionListener {

    private HibernateQueries session;
    private JTextField titre;
    private JSpinner annnemin;
    private JSpinner annnemax;
    private JTextField pays;
    private JTextField langue;
    private JTextField acteur;
    private JTextField genre;
    private JTextField realisateur;
    private List<Film> films;
    private DefaultListModel<String> modelfilm;
    private Map<String,Integer> idstr;
    private JLabel messagefield;
    private JList<String> listefilms;

    public ControlRecherche(HibernateQueries session) {
        this.session = session;
    }

    public void connectToFilms(List<Film> films) {
        this.films = films;
    }

    public void connectToModelFilm(DefaultListModel<String> model) {
        this.modelfilm = model;
    }
    public void connectToMap( Map<String,Integer> idstr) {
        this.idstr = idstr;
    }
    public void connectToMsg(JLabel msg) {
        this.messagefield = msg;
    }
    public void connectToListeFilm(JList<String> listefilms) {
        this.listefilms = listefilms;
    }

    public void connectToFields(JTextField titre, JSpinner anneemin, JSpinner anneemax,
        JTextField pays, JTextField langue, JTextField acteur, JTextField genre, JTextField realisateur) {
        this.titre = titre;
        this.annnemin = anneemin;
        this.annnemax = anneemax;
        this.pays = pays;
        this.langue = langue;
        this.acteur = acteur;
        this.genre = genre;
        this.realisateur = realisateur;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int yearMax = (Integer) annnemax.getValue();
        int yearMin = (Integer) annnemin.getValue();
        String titreFilm = this.titre.getText();
        if (titreFilm.isEmpty()) {
            titreFilm = null;
        }
        String paysfilm= this.pays.getText();
        if (paysfilm.isEmpty()) {
            paysfilm = null;
        }
        String languesOriginal = this.langue.getText();
        if (languesOriginal.isEmpty()) {
            languesOriginal = null;
        }
        String genre = this.genre.getText();
        if (genre.isEmpty()) {
            genre = null;
        }
        String nomRealisateur = this.realisateur.getText();
        if (nomRealisateur.isEmpty()) {
            nomRealisateur = null;
        }
        String nomActeur = this.acteur.getText();
        if (nomActeur.isEmpty()) {
            nomActeur = null;
        }


        this.films = session.findAllFilmsByCriteria(null, titreFilm, yearMin, yearMax, paysfilm, languesOriginal, genre,
                        nomActeur, nomRealisateur);
        this.idstr.clear();
        if (!this.films.isEmpty()) {
            for ( Iterator iterFilms = this.films.iterator(); iterFilms.hasNext(); ) {
                Film unFilmCharge = (Film) iterFilms.next();
                String str =  (String)unFilmCharge.getTitre() + "(" + unFilmCharge.getAnneeproduction().toString() + ")"; 
                int id = unFilmCharge.getIdFilm();
                idstr.put(str, id);
            }
            //System.out.println(idstr);
            this.listefilms.clearSelection();
            this.modelfilm.removeAllElements();
            this.modelfilm.addAll(idstr.keySet());
        } else {
            this.messagefield.setText("Aucun film ne correspond à cette recherche");
        }
    }
    
}
