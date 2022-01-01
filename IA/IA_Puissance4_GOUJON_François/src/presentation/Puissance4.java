package presentation;
import java.awt.*;

import javax.swing.*;

//import abstraction.version1.Etat;
//import abstraction.version2.Etat;
import abstraction.version3.Etat;
//import abstraction.version4.Etat;

import controle.EcouteurP4;

import java.io.File;

@SuppressWarnings("serial")
public class Puissance4 extends JFrame {

	private Etat grille;
	private JLabel[][] lesCases;
	private JButton[] lesBoutons;


	public void reinit() {
		grille.raz();
		for (int lig=0; lig<Etat.NB_LIGNES; lig++) {
			for (int col=0; col<Etat.NB_COLONNES; col++) {
				lesCases[lig][col].setIcon(new ImageIcon("images"+File.separator+"vide.jpg"));
			}
		}
		for (int col=0; col<Etat.NB_COLONNES; col++) {
			lesBoutons[col].setEnabled(true);
		}
	}
	public Puissance4() {
		super("Puissance 4");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.grille = new Etat();

		Container principal = this.getContentPane();
		principal.setLayout(new GridLayout(Etat.NB_LIGNES+1,Etat.NB_COLONNES));
		lesBoutons = new JButton[Etat.NB_COLONNES];
		lesCases = new JLabel[Etat.NB_LIGNES][Etat.NB_COLONNES];
		for (int col=0; col<Etat.NB_COLONNES; col++) {
			lesBoutons[col] = new JButton(new ImageIcon("images"+File.separator+"blanc.jpg"));
			lesBoutons[col].setRolloverIcon(new ImageIcon("images"+File.separator+"drop.jpg"));
			lesBoutons[col].setDisabledIcon(new ImageIcon("images"+File.separator+"blanc.jpg"));
			lesBoutons[col].setBorder(null);

			lesBoutons[col].addActionListener(new EcouteurP4(this, col, grille, lesCases, lesBoutons)); 

			principal.add(lesBoutons[col]);
		}
		for (int lig=0; lig<Etat.NB_LIGNES; lig++) {
			for (int col=0; col<Etat.NB_COLONNES; col++) {
				lesCases[lig][col] = new JLabel(new ImageIcon("images"+File.separator+"vide.jpg"));
				principal.add(lesCases[lig][col]);
			}
		}
		pack();
		this.setSize(new Dimension((int)(this.getSize().getWidth()-Etat.NB_COLONNES),(int)(this.getSize().getHeight()-Etat.NB_LIGNES)));
		this.setResizable(false);
	}
	public static void main(String[] args) {
		Puissance4 fen = new Puissance4();
		fen.setVisible(true);
	}
}

