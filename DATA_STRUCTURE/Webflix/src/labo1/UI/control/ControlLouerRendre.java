package labo1.UI.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import labo1.methodes.HibernateQueries;

// Controlleur qui gère la sélection des films à louer ou rendre dans les listes
// ainsi que les boutons pour louer et rendre une fois la sélection effectuée.

public class ControlLouerRendre implements ListSelectionListener, ActionListener {

    private JList<String> listefilms;
    private JList<String> listeemprunts;
    private DefaultListModel<String> modelemprunts;
    private HibernateQueries session;
    private int currentfilmid;
    private int currentnocopie;
    private JLabel message;
    private JButton louer;
    private JButton rendre;
    private Map<String, Integer> copiestr;
    private Map<String, Integer> idstr;
    private DefaultListModel<String> modeldetailfilm;
    private Map<String, Integer> idactorstr;
    private JList<String> listedetailfilms;
    private int currentactorid;
    private JList<String> listedetailactor;
    private DefaultListModel<String> modeledetailactor;

    public ControlLouerRendre(HibernateQueries session) {
        this.session = session;
        this.currentfilmid = 0;
        this.currentnocopie = 0;
    }

    public void connectToListeFilm(JList<String> liste) {
        this.listefilms = liste;
    }

    public void connectToListeEmprunt(JList<String> liste) {
        this.listeemprunts = liste;
    }

    public void connectToMessage(JLabel message) {
        this.message = message;
    }

    public void connectToModelEmprunts(DefaultListModel<String> modelemprunts) {
        this.modelemprunts = modelemprunts;
    }

    public void connectToLouerButton(JButton louer) {
        this.louer = louer;
    }

    public void connectToRendreButton(JButton rendre) {
        this.rendre = rendre;
    }

    public void connectToMapID(Map<String, Integer> idstr) {
        this.idstr = idstr;
    }

    public void connectToMapCopie(Map<String, Integer> copiestr) {
        this.copiestr = copiestr;
    }

    public void connectToModelDetail(DefaultListModel<String> modeldetailfilm) {
        this.modeldetailfilm = modeldetailfilm;
    }

    public void connectToMapactorID(Map<String, Integer> idactorstr) {
        this.idactorstr = idactorstr;
    }

    public void connectToListeDetailFilm(JList<String> liste) {
        this.listedetailfilms = liste;
    }
    public void connectToListeDetailActor(JList<String> listedetailactor) {
        this.listedetailactor = listedetailactor;
    }
    public void connectToModelDetialActor(DefaultListModel<String> modeledetailactor) {
        this.modeledetailactor = modeledetailactor;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {//Ligne pour éviter un double event click unclick
            if (e.getSource() == this.listefilms) {
                if (!this.listefilms.isSelectionEmpty()) {
                    String currentselec = this.listefilms.getModel().getElementAt(this.listefilms.getSelectedIndex());
                    //System.out.println(this.idstr);
                    this.currentfilmid = this.idstr.get(currentselec);
                    System.out.println("Debut");
                    System.out.println(this.currentfilmid);
                    System.out.println("Fin");
                    this.listeemprunts.clearSelection();
                    this.currentnocopie = 0;

                    this.idactorstr.clear();
                    this.idactorstr = session.getInfoFilmByID(this.currentfilmid);
                    //System.out.println("1");
                    //System.out.println((this.idactorstr));
                    this.listedetailfilms.clearSelection();
                    this.modeldetailfilm.removeAllElements();
                    //System.out.println(sortHashMapByValues(this.idactorstr));
                    Map<String, Integer> mapmodel = sortHashMapByValues(this.idactorstr);
                    this.modeldetailfilm.addAll(mapmodel.keySet());
                    
                }
            } else if (e.getSource() == this.listeemprunts) {
                if (!this.listeemprunts.isSelectionEmpty()) {
                    String currentselec = this.listeemprunts.getModel().getElementAt(this.listeemprunts.getSelectedIndex());
                    this.currentnocopie =  this.copiestr.get(currentselec);
                    System.out.println("Debut");
                    System.out.println(this.currentnocopie);
                    System.out.println("Fin");
                }
            } else if (e.getSource() == this.listedetailfilms) {
                if (!this.listedetailfilms.isSelectionEmpty()) {
                    String currentselec = this.listedetailfilms.getModel().getElementAt(this.listedetailfilms.getSelectedIndex());
                    System.out.println(currentselec);
                    System.out.println("2");
                    System.out.println((this.idactorstr));

                    this.currentactorid = this.idactorstr.get(currentselec);
                    System.out.println("Debut");
                    System.out.println(this.currentactorid);
                    System.out.println("Fin");
                    if (this.currentactorid > 50) {
                        this.listedetailactor.clearSelection();
                        this.modeledetailactor.removeAllElements();
                        Map<String, Integer> mapmodelact = sortHashMapByValues(session.getInfoActorByID(currentactorid));
                        this.modeledetailactor.addAll(mapmodelact.keySet());
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.louer) {
            if (this.currentfilmid != 0) {
                session.louerFilmUtilisateur(this.currentfilmid);
                List emprunts = session.getEmpruntsUtilisateur();
                if (emprunts != null) {
                    this.copiestr.clear();
                    for (Object object : emprunts) {
                        Map row = (Map) object;
                        String str = "NoCopie : " + row.get("nocopie") + " ; Date du prêt : " + row.get("datepret")
                                + " ; En retard : " + row.get("enretard") + " ; Titre : " + row.get("titre");
                        Integer id = (Integer) row.get("nocopie");
                        this.copiestr.put(str, id);
                    }
                    // this.listefilms.clearSelection();
                    this.currentfilmid = 0;
                    // this.listeemprunts.clearSelection();
                    this.currentnocopie = 0;
                    this.modelemprunts.removeAllElements();
                    this.modelemprunts.addAll(this.copiestr.keySet());
                }
            } else {
                message.setText("Un film doit être sélectionné");
            }

        } else if (e.getSource() == this.rendre) {
            if (this.currentnocopie != 0) {
                session.rendreFilmUtilisateur(this.currentnocopie);
                List emprunts = session.getEmpruntsUtilisateur();
                if (emprunts != null) {
                    this.copiestr.clear();
                    for (Object object : emprunts) {
                        Map row = (Map) object;
                        String str = "NoCopie : " + row.get("nocopie") + " ; Date du prêt : " + row.get("datepret")
                                + " ; En retard : " + row.get("enretard")  + " ; Titre : " + row.get("titre");
                        Integer id = (Integer) row.get("nocopie");
                        this.copiestr.put(str, id);
                    }
                    this.listeemprunts.clearSelection();
                    this.currentnocopie = 0;
                    this.modelemprunts.removeAllElements();
                    this.modelemprunts.addAll(this.copiestr.keySet());
                    System.out.println("1tour");
                }
            } else {
                message.setText("Un film doit être sélectionné");
            }
        }
        this.message.setText(session.getMessage());
    }

    public static LinkedHashMap<String, Integer> sortHashMapByValues(Map<String, Integer> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Integer> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

        Iterator<Integer> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Integer val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                Integer comp1 = passedMap.get(key);
                Integer comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
}
