package labo1.UI.control;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import labo1.methodes.HibernateQueries;


//Controleur qui gère les boutons de connection et deconnection


public class ControlCoDiscoButton implements ActionListener {
    private HibernateQueries session;
    private JLabel message;
    private JButton Cobutton;
    private JButton DiscoButton;
    private boolean connecte;
    private JTextField mail;
    private JPasswordField mdp;
    private DefaultListModel<String> modelemprunts;
    private Map<String,Integer> copiestr;

    public ControlCoDiscoButton(HibernateQueries session) {
        this.session = session;
    }

    public void connectToMessage(JLabel message) {
        this.message = message;
    }

    public void connectToConnectButton(JButton cobtn) {
        this.Cobutton = cobtn;
    }
    
    public void connectToDisconnectButton(JButton discobtn) {
        this.DiscoButton = discobtn;
    }

    public void connectToMail(JTextField mail) {
        this.mail = mail;
    }

    public void connectTomdp(JPasswordField mdp) {
        this.mdp = mdp;
    }

    public void connectToModelEmprunts(DefaultListModel<String> liste) {
        this.modelemprunts = liste;
    }
    public void connectToConnecter(boolean bool) {
        this.connecte = bool;
    }
    public void connectToMap(Map<String,Integer> copiestr) {
        this.copiestr = copiestr;
    }
    
	@Override
	 public void actionPerformed(ActionEvent e) {
       if (e.getSource() == Cobutton) {
           String pw = new String(this.mdp.getPassword());
           boolean res = session.connextionUtilisateur(this.mail.getText(), pw);
           if (res) {
           this.copiestr.clear();
           List emprunts = session.getEmpruntsUtilisateur();
           for(Object object : emprunts) {
            Map row = (Map)object;
            String str = "NoCopie : " + row.get("nocopie") + " ; Date du prêt : " + row.get("datepret") + " ; En retard : " + 
                row.get("enretard") + " ; Titre : " + row.get("titre");
            Integer id = (Integer)row.get("nocopie");
            copiestr.put(str, id);
            }
            this.modelemprunts.removeAllElements();
            this.modelemprunts.addAll(copiestr.keySet());
           this.connecte = true;
        }
       }
       else if (e.getSource() == DiscoButton) {
           session.deconnextionUtilisateur();
           this.modelemprunts.removeAllElements();
           this.connecte = false;
       }
       message.setText(session.getMessage());
       Cobutton.setEnabled(!this.connecte);
       DiscoButton.setEnabled(this.connecte);
   }

}

