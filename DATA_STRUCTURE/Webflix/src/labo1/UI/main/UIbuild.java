package labo1.UI.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import labo1.Hibernate.src.Film;
import labo1.UI.control.ControlCoDiscoButton;
import labo1.UI.control.ControlLouerRendre;
import labo1.UI.control.ControlRecherche;
import labo1.methodes.HibernateQueries;

public class UIbuild extends JFrame {
    /*
     * on déclare en données membres les composants importants de l'interface 
     * qui vont servir à plusieurs endroits
     */

    private HibernateQueries session;

    private JPanel westPanel; // panel de gauche pour les recherches
    private List<Film> films;
    private JLabel messagefield; // message de notification
    private JButton connectButton; // bouton de connection
    private JButton disconnectButton; // bouton de deconnection
    private JTextField mail;  // champ mail
    private JPasswordField mdp;  // champ mot de passe
    private ControlCoDiscoButton controlbtn; // controlleur bouton connexion et deconexion
    private ControlRecherche controlrecherchebtn; // Controlleur bouton recherche
    private JButton recherchebtn; // bouton recherche
    private JList<String> listefilms;  // liste des films avec Titre(annee)
    private JList<String> listeemprunts; // liste des emprunts actuels
    private DefaultListModel<String> modelfilms;  // modele pour les listes
    private DefaultListModel<String> modelemprunts;  // modele pour les listes
    private JButton louer;  //bouton louer
    private JButton rendre;  //bouton rendre
    private ControlLouerRendre controlLR;  // controlleur des boutons louer et rendre, de la selection dans les liste de films et d'emprunts aisni que pour les détails de films et acteurs
    private boolean connecte;  // permet de savoir si un utilisateur est actuellement connecté
    private Map<String,Integer> copiestr;  //permet de faire le lien entre l'emprunt sélectionné dans la liste d'emprunts et son nocopie
    private Map<String,Integer> idstr;  //permet de faire le lien entre le film sélectionné dans la liste de films et son idfilm
    private DefaultListModel<String> modeldetailfilm; //modele pour les détails du film
    private JList<String> listedetailfilm; // liste contenant les détails du film
    private Map<String,Integer> idactorstr; //permet de faire le lien entre l'acteur sélectionné dans les détails du film et son noartiste
    private DefaultListModel<String> modeldetailactor; // modele pour les détails de l'acteur
    private JList<String> listedetailactor; // liste pour les détails de l'acteur
    
    
    public UIbuild(HibernateQueries session) {

        this.session = session;
        this.idstr = new HashMap<String,Integer>();
        this.copiestr = new HashMap<String,Integer>();
        this.idactorstr = new HashMap<String,Integer>();

        // parametrage de la fenetre principale : la JFrame est organisee en BorderLayout
        this.setTitle("Labo2 log660");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    session.closeSessionFactory();
                    System.exit(0);
                }
            });

       
        this.buildPanelNorth();
        this.buildPanelCenter();
        this.buildPanelEast();
        this.buildPanelWest();
        this.buildPanelSouth();

        /* 
         *  on les place ici plutôt que de tout passer en paramètres au constructeur afin d'éviter les problèmes d'ordre d'initialisation
         */
        this.controlrecherchebtn.connectToFilms(this.films);
        this.controlrecherchebtn.connectToModelFilm(this.modelfilms);
        this.controlrecherchebtn.connectToMap(this.idstr);
        this.controlrecherchebtn.connectToMsg(this.messagefield);
        this.controlrecherchebtn.connectToListeFilm(this.listefilms);

        this.controlbtn.connectToMessage(this.messagefield);
        this.controlbtn.connectToConnectButton(this.connectButton);
        this.controlbtn.connectToDisconnectButton(this.disconnectButton);
        this.controlbtn.connectTomdp(this.mdp);
        this.controlbtn.connectToMail(this.mail);
        this.controlbtn.connectToConnecter(this.connecte);
        this.controlbtn.connectToModelEmprunts(this.modelemprunts);
        this.controlbtn.connectToMap(this.copiestr);

        this.controlLR.connectToListeEmprunt(this.listeemprunts);
        this.controlLR.connectToListeFilm(this.listefilms);
        this.controlLR.connectToLouerButton(this.louer);
        this.controlLR.connectToMessage(this.messagefield);
        this.controlLR.connectToRendreButton(this.rendre);
        this.controlLR.connectToMapCopie(this.copiestr);
        this.controlLR.connectToMapID(this.idstr);
        this.controlLR.connectToModelEmprunts(this.modelemprunts);
        this.controlLR.connectToModelDetail(this.modeldetailfilm);
        this.controlLR.connectToMapactorID(this.idactorstr);
        this.controlLR.connectToListeDetailFilm(this.listedetailfilm);
        this.controlLR.connectToListeDetailActor(this.listedetailactor);
        this.controlLR.connectToModelDetialActor(this.modeldetailactor);

    }


    private void buildPanelCenter() {
        JPanel centrPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        centrPanel.setBorder(BorderFactory.createBevelBorder(1));
        centrPanel.setPreferredSize(new Dimension(300, 550));
        this.add(centrPanel, BorderLayout.CENTER);

        this.controlLR = new ControlLouerRendre(session);

        JLabel filmscritere = new JLabel("Films répondant aux critères");
        centrPanel.add(filmscritere);
        this.modelfilms = new DefaultListModel<String>();
        this.listefilms = new JList<String>(modelfilms);
        this.listefilms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listefilms.setLayoutOrientation(JList.VERTICAL);
        this.listefilms.addListSelectionListener(this.controlLR);
        JScrollPane scrollfilm = new JScrollPane(this.listefilms);
        scrollfilm.setPreferredSize(new Dimension(270, 350));
        centrPanel.add(scrollfilm);

        this.louer = new JButton("Louer");
        this.louer.addActionListener(this.controlLR);
        centrPanel.add(this.louer);
    }

    private void buildPanelEast() {
        JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        eastPanel.setBorder(BorderFactory.createBevelBorder(1));
        eastPanel.setPreferredSize(new Dimension(500, 450));
        this.add(eastPanel, BorderLayout.EAST);
        JLabel filmsempruntes = new JLabel("Films actuellements empruntés");
        eastPanel.add(filmsempruntes);
        this.modelemprunts = new DefaultListModel<String>();
        this.listeemprunts = new JList<String>(modelemprunts);
        this.listeemprunts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listeemprunts.setLayoutOrientation(JList.VERTICAL);
        this.listeemprunts.addListSelectionListener(this.controlLR);
        JScrollPane scrollemprunt = new JScrollPane(this.listeemprunts);
        scrollemprunt.setPreferredSize(new Dimension(450, 350));
        eastPanel.add(scrollemprunt);

        this.rendre = new JButton("Rendre");
        this.rendre.addActionListener(this.controlLR);
        eastPanel.add(this.rendre);
    }

    private void buildPanelSouth() {
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        southPanel.setBorder(BorderFactory.createBevelBorder(1));
        southPanel.setPreferredSize(new Dimension(1300, 320));
        this.add(southPanel, BorderLayout.SOUTH);
        JPanel southPanelGauche = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        southPanelGauche.setBorder(BorderFactory.createBevelBorder(1));
        southPanelGauche.setPreferredSize(new Dimension(600, 300));
        southPanel.add(southPanelGauche,BorderLayout.WEST);
        JLabel detailfilm = new JLabel("Détails du film");
        southPanelGauche.add(detailfilm);
        this.modeldetailfilm = new DefaultListModel<String>();
        this.listedetailfilm = new JList<String>(modeldetailfilm);
        this.listedetailfilm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listedetailfilm.setLayoutOrientation(JList.VERTICAL);
        this.listedetailfilm.addListSelectionListener(this.controlLR);
        JScrollPane scrolldetailfilm = new JScrollPane(this.listedetailfilm);
        scrolldetailfilm.setPreferredSize(new Dimension(550, 250));
        southPanelGauche.add(scrolldetailfilm);

        JPanel southPanelDroit = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        southPanelDroit.setBorder(BorderFactory.createBevelBorder(1));
        southPanelDroit.setPreferredSize(new Dimension(600, 300));
        southPanel.add(southPanelDroit,BorderLayout.EAST);
        JLabel detailacteur = new JLabel("Détails de l'acteur");
        southPanelDroit.add(detailacteur);
        this.modeldetailactor = new DefaultListModel<String>();
        this.listedetailactor = new JList<String>(modeldetailactor);
        this.listedetailactor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listedetailactor.setLayoutOrientation(JList.VERTICAL);
        JScrollPane scrolldetailactor = new JScrollPane(this.listedetailactor);
        scrolldetailactor.setPreferredSize(new Dimension(550, 250));
        southPanelDroit.add(scrolldetailactor);

    }

    private void buildPanelWest() {
        // JPanel west
        this.westPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        this.westPanel.setPreferredSize(new Dimension(500, 350));
        this.westPanel.setBorder(BorderFactory.createBevelBorder(1));
        this.add(this.westPanel, BorderLayout.WEST);
        // text field de recherche
        JLabel lTitreFilm = new JLabel("Titre Film");
        JTextField tfTitreFilm = new JTextField();
        westPanel.add(createHorizontalJPanel(lTitreFilm, tfTitreFilm));

        int currentYear = 2021;
        JLabel lYearMin = new JLabel("Années min.");
        JSpinner sYearMin = new JSpinner(
                new SpinnerNumberModel(currentYear-120, currentYear- 120, currentYear, 1));
        westPanel.add(createHorizontalJPanel(lYearMin, sYearMin));
        JLabel lYearMMax = new JLabel("Années max.");
        JSpinner sYearMax = new JSpinner(
                new SpinnerNumberModel(currentYear, currentYear- 120, currentYear, 1));
        westPanel.add(createHorizontalJPanel(lYearMMax, sYearMax));
        // TODO: aletrer si date invalide (ou resync avec refresh)

        JLabel lPays = new JLabel("Pays");
        JTextField tfPays = new JTextField();
        westPanel.add(createHorizontalJPanel(lPays, tfPays));

        JLabel lLangueOriginal = new JLabel("Langue originale");
        JTextField tfLangueOriginal = new JTextField();
        westPanel.add(createHorizontalJPanel(lLangueOriginal, tfLangueOriginal));

        JLabel lGenre = new JLabel("Genre");
        JTextField tfGenre = new JTextField();
        westPanel.add(createHorizontalJPanel(lGenre, tfGenre));

        JLabel lNomRealisateur = new JLabel("Realisateur");
        JTextField tfNomRealisateur = new JTextField();
        westPanel.add(createHorizontalJPanel(lNomRealisateur, tfNomRealisateur));

        JLabel lNomActeur= new JLabel("Acteur");
        JTextField tfNomActeur = new JTextField();
        westPanel.add(createHorizontalJPanel(lNomActeur, tfNomActeur));

        this.controlrecherchebtn = new ControlRecherche(this.session);
        this.controlrecherchebtn.connectToFields(tfTitreFilm, sYearMin, sYearMax, tfPays, tfLangueOriginal, tfNomActeur, tfGenre, tfNomRealisateur);
        this.recherchebtn = new JButton("Rechercher");
        this.recherchebtn.addActionListener(this.controlrecherchebtn);
        westPanel.add(this.recherchebtn);

    }

    void buildPanelNorth() {
        JPanel panelUp = new JPanel();
        panelUp.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        this.add(panelUp, BorderLayout.NORTH);

        // Message
        this.messagefield = new JLabel("<html><h4> Bienvenue chez Webflix </h4></html>", SwingConstants.CENTER);
        panelUp.setAlignmentX(0.7f);
        panelUp.add(this.messagefield);

        JLabel lEmail = new JLabel("Email address");
        this.mail = new JTextField();
        panelUp.add(createHorizontalJPanel(lEmail, this.mail));

        JLabel lPassword = new JLabel("Password");
        this.mdp = new JPasswordField();
        panelUp.add(createHorizontalJPanel(lPassword, this.mdp));

        // bouton Connection
        this.connectButton = new JButton("Connexion");     
        this.controlbtn = new ControlCoDiscoButton(this.session);
        this.connectButton.addActionListener(this.controlbtn);
        panelUp.add(this.connectButton);
       
        
        // bouton Disconnect
        this.disconnectButton = new JButton("Deconnexion");
        this.disconnectButton.addActionListener(this.controlbtn);
        panelUp.add(this.disconnectButton);
    }

    public static JPanel createHorizontalJPanel(Component... components) {
        JPanel jPanel = new JPanel();
        for (Component component: components) {
            if (component instanceof JTextField) {
                ((JTextField) component).setColumns(10);
            }
            jPanel.add(component);
        }
        return jPanel;
    }
}
