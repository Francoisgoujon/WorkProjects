package presentation;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import abstraction.Population;
import presentation.secondaire.FenetreGraphique;
import presentation.secondaire.FenetreIndividu;

public class FenetrePrincipale extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Population population;
	private int taille;
	private int nombreIndividus;
	private int pourcentageReproduction;
	private int pourcentageCroisement;
	private int pourcentageMutation;
	private int typeGeneration;
	private int typeMutation;
	private JLabel labelNbGen;
	private JTextField tfNombreIndividus;
	private JTextField tfTaille;
	private JTextField tfReproduction;
	private JTextField tfCroisement;
	private JTextField tfMutation;
	private JComboBox<String> comboTypesGeneration;
	private JComboBox<String> comboTypesMutation;
	private FenetreGraphique fenetreGraphique ;
	private FenetreIndividu fenetreIndividu;
	
	public FenetrePrincipale() {
		super("NReines via algos genetiques");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.taille = 40;
		this.nombreIndividus = 20;
		this.pourcentageReproduction = 0;
		this.pourcentageCroisement = 10;
		this.pourcentageMutation = 0;
		this.typeGeneration = 1;
		this.typeMutation   = 1;
		this.population     = new Population(this.nombreIndividus,this.typeGeneration,this.taille);
		this.fenetreGraphique = new FenetreGraphique("Evolution", 600, 400);
		this.fenetreGraphique.ajouter(this.population.getCourbeMeilleur());
		this.fenetreGraphique.ajouter(this.population.getCourbeMoyenne());
		this.fenetreGraphique.setVisible(true);
		this.fenetreIndividu = new FenetreIndividu("Meilleur", this.population, 500, 500);
		this.fenetreIndividu.setVisible(true);

		this.setLayout(new BorderLayout());
		
		JPanel panelCentre = new JPanel();
		panelCentre.setLayout(new BoxLayout(panelCentre, BoxLayout.Y_AXIS));
		
		JPanel pNbGen = new JPanel(new BorderLayout());
		pNbGen.add(Box.createHorizontalGlue(), BorderLayout.WEST);
		pNbGen.add(new JLabel("Nombre de generations"), BorderLayout.CENTER);
		this.labelNbGen = new JLabel("0");
		pNbGen.add(labelNbGen, BorderLayout.EAST);
		panelCentre.add(pNbGen);
		panelCentre.add(Box.createVerticalGlue());

		JPanel pTaille = new JPanel(new BorderLayout());
		pTaille.add(Box.createHorizontalGlue(), BorderLayout.WEST);
		pTaille.add(new JLabel("Taille de l'echiquier"), BorderLayout.CENTER);
		this.tfTaille = new JTextField(5);
		tfTaille.setText(this.taille+"");
		tfTaille.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int actuelle = -1;
				try {
					actuelle = Integer.parseInt(tfTaille.getText());
				} catch (Exception ex) {}
				if (actuelle>0 && actuelle<1001) {
					population.setLongueurADN(actuelle);
					labelNbGen.setText("0");
					fenetreGraphique.repaint();
					fenetreIndividu.selectionnerMeilleur();
					fenetreIndividu.repaint();
				} else {
					tfTaille.setText(population.getLongueurADN()+"");
				}
			}
		});
		pTaille.add(tfTaille, BorderLayout.EAST);
		panelCentre.add(pTaille);
		panelCentre.add(Box.createVerticalGlue());
		
		JPanel pNombreIndividus = new JPanel(new BorderLayout());
		pNombreIndividus.add(Box.createHorizontalGlue(), BorderLayout.WEST);
		pNombreIndividus.add(new JLabel("Nombre d'individus"), BorderLayout.CENTER);
	    this.tfNombreIndividus = new JTextField(5);
		tfNombreIndividus.setText(this.nombreIndividus+"");
		tfNombreIndividus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int actuelle = -1;
				try {
					actuelle = Integer.parseInt(tfNombreIndividus.getText());
				} catch (Exception ex) {}
				if (actuelle>0 && actuelle<1001) {
					population.setTaillePopulation(actuelle);
					labelNbGen.setText("0");
					fenetreGraphique.repaint();
					fenetreIndividu.selectionnerMeilleur();
					fenetreIndividu.repaint();
				} else {
					tfNombreIndividus.setText(population.getTaillePopulation()+"");
				}
			}
		});
		pNombreIndividus.add(tfNombreIndividus, BorderLayout.EAST);
		panelCentre.add(pNombreIndividus);
		panelCentre.add(Box.createVerticalGlue());
		
		JPanel pReproduction = new JPanel(new BorderLayout());
		pReproduction.add(Box.createHorizontalGlue(), BorderLayout.WEST);
		pReproduction.add(new JLabel("Pourcentage de reproduction du meilleur"), BorderLayout.CENTER);
		this.tfReproduction = new JTextField(5);
		tfReproduction.setText(this.pourcentageReproduction+"");
		tfReproduction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int actuelle = -1;
				try {
					actuelle = Integer.parseInt(tfReproduction.getText());
				} catch (Exception ex) {}
				if (actuelle>=0 && actuelle<101) {
					pourcentageReproduction=actuelle;
				} else {
					tfReproduction.setText(pourcentageReproduction+"");
				}
			}
		});
		pReproduction.add(tfReproduction, BorderLayout.EAST);
		panelCentre.add(pReproduction);
		panelCentre.add(Box.createVerticalGlue());
		
		JPanel pCroisement = new JPanel(new BorderLayout());
		pCroisement.add(Box.createHorizontalGlue(), BorderLayout.WEST);
		pCroisement.add(new JLabel("Pourcentage de croisement"), BorderLayout.CENTER);
		this.tfCroisement = new JTextField(5);
		tfCroisement.setText(this.pourcentageCroisement+"");
		tfCroisement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int actuelle = -1;
				try {
					actuelle = Integer.parseInt(tfCroisement.getText());
				} catch (Exception ex) {}
				if (actuelle>=0 && actuelle<101) {
					pourcentageCroisement=actuelle;
				} else {
					tfCroisement.setText(pourcentageCroisement+"");
				}
			}
		});
		pCroisement.add(tfCroisement, BorderLayout.EAST);
		panelCentre.add(pCroisement);
		panelCentre.add(Box.createVerticalGlue());
		
		JPanel pMutation = new JPanel(new BorderLayout());
		pMutation.add(Box.createHorizontalGlue(), BorderLayout.WEST);
		pMutation.add(new JLabel("Pourcentage de mutation"), BorderLayout.CENTER);
		this.tfMutation = new JTextField(5);
		tfMutation.setText(this.pourcentageMutation+"");
		tfMutation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int actuelle = -1;
				try {
					actuelle = Integer.parseInt(tfMutation.getText());
				} catch (Exception ex) {}
				if (actuelle>=0 && actuelle<101) {
					pourcentageMutation=actuelle;
				} else {
					tfMutation.setText(pourcentageMutation+"");
				}
			}
		});
		pMutation.add(tfMutation, BorderLayout.EAST);
		panelCentre.add(pMutation);
		panelCentre.add(Box.createVerticalGlue());

		String[] typesGeneration = {"Generation type 1", "Generation type 2"};
		this.comboTypesGeneration=new JComboBox<String>(typesGeneration);
		this.comboTypesGeneration.setEditable(false);
		this.comboTypesGeneration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboTypesGeneration.getSelectedIndex()+1!=typeGeneration) {
					typeGeneration = comboTypesGeneration.getSelectedIndex()+1;
					population.setTypeGeneration(typeGeneration);
					labelNbGen.setText("0");
					fenetreGraphique.repaint();
					fenetreIndividu.selectionnerMeilleur();
					fenetreIndividu.repaint();
				}
			}
		});
		panelCentre.add(comboTypesGeneration);
		panelCentre.add(Box.createVerticalGlue());
				
		String[] typesMutation = {"Mutation 1 : Aleatoire", "Mutation 2 : Permutation", "Mutation 3 : 1er conflit", "Mutation 4 : permut. conflits","Mutation 5 : permut. conflits", "Mutation 6 : perso"};
		this.comboTypesMutation=new JComboBox<String>(typesMutation);
		this.comboTypesMutation.setEditable(false);
		this.comboTypesMutation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeMutation = comboTypesMutation.getSelectedIndex()+1;
			}
		});
		panelCentre.add(comboTypesMutation);
		panelCentre.add(Box.createVerticalGlue());

		this.add(new JScrollPane(panelCentre),BorderLayout.CENTER);
		
		JPanel pBoutons = new JPanel(new GridLayout(1,2));
		JButton plusUn = new JButton("+1 >");
		plusUn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				population.evolution(1, pourcentageReproduction, pourcentageCroisement, pourcentageMutation, typeMutation);
				labelNbGen.setText(population.getNbGenerations()+"");
				fenetreGraphique.repaint();
				fenetreIndividu.selectionnerMeilleur();
				fenetreIndividu.repaint();
			}
		});
		pBoutons.add(plusUn);
		
		JButton plusVingt = new JButton("+20 >>>");
		plusVingt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				population.evolution(20, pourcentageReproduction, pourcentageCroisement, pourcentageMutation, typeMutation);
				labelNbGen.setText(population.getNbGenerations()+"");
				fenetreGraphique.repaint();
				fenetreIndividu.selectionnerMeilleur();
				fenetreIndividu.repaint();

			}
		});
		pBoutons.add(plusVingt);
		
		this.add(pBoutons, BorderLayout.SOUTH);
		
		this.pack();
	}

	public static void main(String[] args) {
		new FenetrePrincipale().setVisible(true);
	}
}
