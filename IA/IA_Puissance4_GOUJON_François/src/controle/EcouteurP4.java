package controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

//import abstraction.version1.Etat;
//import abstraction.version2.Etat;
import abstraction.version3.Etat;
//import abstraction.version4.Etat;

import presentation.Puissance4;

public class EcouteurP4  implements ActionListener {
	private int colonne;
	private Puissance4 fenetre;
	private Etat grille;
	private JLabel[][] lesCases;
	private JButton[] lesBoutons;

	public EcouteurP4(Puissance4 fenetre, int colonne, Etat grille, JLabel[][] lesCases, JButton[] lesBoutons) {
		this.fenetre = fenetre;
		this.colonne = colonne;
		this.grille = grille;
		this.lesCases=lesCases;
		this.lesBoutons=lesBoutons;
	}

	public void actionPerformed(ActionEvent arg0) {
		if (grille.peutDeposerPion(this.colonne)) {
			int ligne = grille.deposerPion(this.colonne, Etat.HUMAIN);
			lesCases[Etat.NB_LIGNES-1-ligne][this.colonne].setIcon(new ImageIcon("images"+File.separator+"rouge.jpg"));
			if (!grille.peutDeposerPion(this.colonne)) {
				lesBoutons[this.colonne].setEnabled(false);
			}
			if (grille.gagnant()==Etat.HUMAIN) {
				JOptionPane.showMessageDialog(this.fenetre, "BRAVO ! Vous avez gagne !");
				this.fenetre.reinit();
			} else {
				int col = grille.meilleurCoupPourOrdinateur();
				ligne = grille.deposerPion(col, Etat.ORDI);
				lesCases[Etat.NB_LIGNES-1-ligne][col].setIcon(new ImageIcon("images"+File.separator+"jaune.jpg"));
				if (!grille.peutDeposerPion(col)) {
					lesBoutons[col].setEnabled(false);
				}
				if (grille.gagnant()==Etat.ORDI) {
					JOptionPane.showMessageDialog(this.fenetre, "Helas, vous avez perdu !");
					this.fenetre.reinit();
				}
			}
			if (grille.pleine()) {
				JOptionPane.showMessageDialog(this.fenetre, "Personne ne gagne !");
				this.fenetre.reinit();
			}
		}
	}
}	
