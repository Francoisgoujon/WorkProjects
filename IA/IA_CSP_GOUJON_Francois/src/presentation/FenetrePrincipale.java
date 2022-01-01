package presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import abstraction.ConstraintNetwork;
import abstraction.Instanciation;

public class FenetrePrincipale extends JFrame {
	private static final long serialVersionUID = 1L;
	public static final String[] TAILLES = {"5", "6", "7", "8", "9",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", 
			"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", 
			"30", "31", "32", "33", "34", "35", "36", "37", "38", "39", 
			"120", "180", "236", "240", "1000"};

	private JButton btGenerate;
	private JButton btBacktrack;
	private JButton btFC;;
	private JButton btFCO;
	private JButton btQuitter;
	private JList<String> listeTailles;
	private JTextArea journal;
	private Font fonte;
	private Dimension preferredSize;
	private int n; // taille de l'echiquier

	public FenetrePrincipale() {
		super("NReines via CSP");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.fonte = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		this.preferredSize = new Dimension(400, 40);
		this.n=5;

		this.setLayout(new BorderLayout());


		JPanel panelEst = new JPanel(new BorderLayout());

		listeTailles = new JList<String>(TAILLES);
		listeTailles.setLayoutOrientation(JList.VERTICAL);
		listeTailles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(listeTailles);
		listeTailles.setFont(fonte);
		listeTailles.setSelectedIndex(0);
		listeTailles.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				try {
				n = Integer.parseInt(TAILLES[listeTailles.getSelectedIndex()]);
				} catch(Exception e) {}
			}
			
		});
		panelEst.add(scrollPane, BorderLayout.WEST);
		this.add(panelEst, BorderLayout.EAST);


		JPanel panelHaut = new JPanel();
		panelHaut.setLayout(new BoxLayout(panelHaut, BoxLayout.Y_AXIS));

		this.btGenerate = new JButton("Generate & Test");
		JPanel pbtg = new JPanel();
		pbtg.add(btGenerate);
		panelHaut.add(pbtg);
		btGenerate.setFont(fonte);
		btGenerate.setPreferredSize(preferredSize);
		btGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Instanciation i = new Instanciation(n);
				ConstraintNetwork cn = new ConstraintNetwork(n);
				journal.setText(journal.getText()+"\ngenerateAndTest( "+n+")");
		        long debut=System.currentTimeMillis();
		        cn.generateAndTest(i);
		         long fin=System.currentTimeMillis();

		        long duree=fin-debut;
		        long dureeMinutes=duree/60000;
		        long dureeSecondes=(duree-(dureeMinutes*60000))/1000;
		        long dureeMillisecondes=(duree-(dureeMinutes*60000)-(dureeSecondes*1000));
		        journal.setText(journal.getText()+" --> "+dureeMinutes+"mn "+dureeSecondes+"s "+dureeMillisecondes+"ms ");
				}
		});
		panelHaut.add(Box.createVerticalGlue());

		this.btBacktrack = new JButton("Backtrack");
		JPanel pbtb = new JPanel();
		pbtb.add(btBacktrack);
		panelHaut.add(pbtb);
		btBacktrack.setFont(fonte);
		btBacktrack.setPreferredSize(preferredSize);
		btBacktrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Instanciation i = new Instanciation(n);
				ConstraintNetwork cn = new ConstraintNetwork(n);
				journal.setText(journal.getText()+"\nbacktrack( "+n+")");
				journal.repaint();
		        long debut=System.currentTimeMillis();
		        cn.backtrack(i);
		         long fin=System.currentTimeMillis();
		        long duree=fin-debut;
		        long dureeMinutes=duree/60000;
		        long dureeSecondes=(duree-(dureeMinutes*60000))/1000;
		        long dureeMillisecondes=(duree-(dureeMinutes*60000)-(dureeSecondes*1000));
		        journal.setText(journal.getText()+" --> "+dureeMinutes+"mn "+dureeSecondes+"s "+dureeMillisecondes+"ms ");
				}
		});
		panelHaut.add(Box.createVerticalGlue());

		this.btFC = new JButton("Forward Checking");
		JPanel pbtfc = new JPanel();
		pbtfc.add(btFC);
		panelHaut.add(pbtfc);
		btFC.setFont(fonte);
		btFC.setPreferredSize(preferredSize);
		btFC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Instanciation i = new Instanciation(n);
				ConstraintNetwork cn = new ConstraintNetwork(n);
				journal.setText(journal.getText()+"\nforwardChecking( "+n+")");
		        long debut=System.currentTimeMillis();
		        cn.forwardChecking(i);
		         long fin=System.currentTimeMillis();

		        long duree=fin-debut;
		        long dureeMinutes=duree/60000;
		        long dureeSecondes=(duree-(dureeMinutes*60000))/1000;
		        long dureeMillisecondes=(duree-(dureeMinutes*60000)-(dureeSecondes*1000));
		        journal.setText(journal.getText()+" --> "+dureeMinutes+"mn "+dureeSecondes+"s "+dureeMillisecondes+"ms ");
				}
		});
		panelHaut.add(Box.createVerticalGlue());

		this.btFCO = new JButton("Forward Checking + Ordre");
		JPanel pbtfco = new JPanel();
		pbtfco.add(btFCO);
		panelHaut.add(pbtfco);
		btFCO.setFont(fonte);
		btFCO.setPreferredSize(preferredSize);
		btFCO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Instanciation i = new Instanciation(n);
				ConstraintNetwork cn = new ConstraintNetwork(n);
				journal.setText(journal.getText()+"\nordre+forwardChecking( "+n+")");
		        long debut=System.currentTimeMillis();
		        cn.ordre(n);
		        cn.forwardChecking(i);
		         long fin=System.currentTimeMillis();

		        long duree=fin-debut;
		        long dureeMinutes=duree/60000;
		        long dureeSecondes=(duree-(dureeMinutes*60000))/1000;
		        long dureeMillisecondes=(duree-(dureeMinutes*60000)-(dureeSecondes*1000));
		        journal.setText(journal.getText()+" --> "+dureeMinutes+"mn "+dureeSecondes+"s "+dureeMillisecondes+"ms ");
				}
		});
		panelHaut.add(Box.createVerticalGlue());

		this.btQuitter = new JButton("Quitter");
		JPanel pbtq = new JPanel();
		pbtq.add(btQuitter);
		panelHaut.add(pbtq);
		btQuitter.setFont(fonte);
		btQuitter.setPreferredSize(preferredSize);
		btQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);			}
		});
		panelHaut.add(Box.createVerticalGlue());
		
		this.add(panelHaut, BorderLayout.WEST);
		
		this.journal=new JTextArea();
		journal.setMinimumSize(new Dimension(300,200));
		journal.setFont(fonte);
		this.add(new JScrollPane(this.journal), BorderLayout.CENTER);

		this.pack();
		this.setSize(new Dimension(800,(int)(this.getSize().getHeight())));
	}

	public static void main(String[] args) {
		new FenetrePrincipale().setVisible(true);
	}
}
