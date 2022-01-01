package presentation.secondaire;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import abstraction.Individu;
import abstraction.Population;

public class FenetreIndividu extends JFrame implements KeyListener, ComponentListener{
	private static final long serialVersionUID = 1l;

	/** la largeur et la hauteur de la fenetre.	 */
	private int largeur, hauteur; 
	/** image sur laquelle seront tracees les courbes (l'image est ensuite affichee)	 */
	private Image image;
	private Graphics gc;

	private Population population;
	private int individuCourant;

	/**
	 * Constructeur initialisant le graphique avec t pour titre, une largeur de l pixels et une hauteur de h pixels. Par defaut, les unites seront choisies par le programme, le graphique est encadre, il y a des traits horizontaux et verticaux realisant un quadrillage, les nombres sur les axes n'ont que deux chiffres apres la virgule, la taille de la police est de 12, la largeur des traits de legende est de 25 et le graphique ne comporte aucune courbe. 
	 * @param t String, titre du graphique 
	 * @param l int, largeur en pixels du graphique
	 * @param h int, hauteur en pixels du graphique 
	 */
	public FenetreIndividu(String t, Population population,	int l, int h) {
		this.largeur = l;
		this.hauteur = h;
		this.population = population;
		this.individuCourant=population.indexMieuxAdapte();
		this.image=null;
		this.setTitle(t);
		this.setSize(largeur, hauteur);
		this.addComponentListener(this);
		this.addKeyListener(this);
	}

	public void selectionnerMeilleur() {
		this.individuCourant=population.indexMieuxAdapte();
	}
	
	/**
	 * Methode tracant le graphisme sur l'instance de Graphics passee en parametre
	 * (Cette methode est appelee automatiquement lorsque le contenu de la fenetre doit etre redessine).
	 */ 
	public void paint(Graphics g) {
			if (this.image==null) {
				this.image = createImage ( 2048, 1200);
				this.gc = this.image.getGraphics ();
			}
			// on efface l'image
			gc.setColor(Color.WHITE);
			gc.fillRect(0,0,this.largeur, this.hauteur);
			
			Individu individu = population.get(this.individuCourant);
			setTitle(this.individuCourant+"/"+this.population.getTaillePopulation()+" fit="+individu.fitness()+" meilleur="+this.population.indexMieuxAdapte()+"/"+this.population.getTaillePopulation());
						
			int n = individu.longueurADN();
			int largeurCase =(int) Math.max(3.0,  Math.min(this.largeur-this.getInsets().left-this.getInsets().right,  this.hauteur-this.getInsets().top-this.getInsets().bottom)/n);
			for (int c = 0; c<n; c++) {
				for (int l=0; l<n; l++) {
					if ((c+l)%2==1) {
						gc.setColor(Color.LIGHT_GRAY);
					} else {
						gc.setColor(new Color(255,222,123));
					}
					gc.fillRect(this.getInsets().left+c*largeurCase, this.getInsets().top+l*largeurCase, largeurCase, largeurCase);
				}
				int dame = individu.gene(c);
				if (individu.enConflit(c)) {
					gc.setColor(Color.RED);
				} else {
					gc.setColor(Color.blue);
				}
				if (largeurCase>3) {
					gc.fillOval(this.getInsets().left+c*largeurCase, this.getInsets().top+dame*largeurCase, largeurCase, largeurCase);
				} else {
					gc.fillRect(this.getInsets().left+c*largeurCase, this.getInsets().top+dame*largeurCase, largeurCase, largeurCase);
				}
			}
			gc.setColor(Color.RED);
			for (int c1 = 0; c1<n; c1++) {
				for (int c2 = 0; c2<n; c2++) {
					int l1 =  individu.gene(c1);
					int l2 =  individu.gene(c2);
					if (Individu.enConflit(c1, l1, c2, l2)) {
						gc.drawLine(this.getInsets().left+c1*largeurCase+(largeurCase/2),
								this.getInsets().top+l1*largeurCase+(largeurCase/2),
								this.getInsets().left+c2*largeurCase+(largeurCase/2),
								this.getInsets().top+l2*largeurCase+(largeurCase/2));
					}
				}
			}
			g.drawImage(image, 0, 0, null);
		}
	public void componentShown(ComponentEvent arg0) {}
	public void componentHidden(ComponentEvent arg0) {}
	public void componentMoved(ComponentEvent arg0) {}
	public void componentResized(ComponentEvent arg0) {
		if (this.getSize().width>=100) {
			this.largeur = this.getSize().width;
		}
		if (this.getSize().height>=100) {
			this.hauteur = this.getSize().height;
		}
	}

	public void keyReleased(KeyEvent arg0) {	}
	public void keyTyped(KeyEvent arg0) {	}
	public void keyPressed(KeyEvent arg0) {
		int code = arg0.getKeyCode();
		if (code==37 || code==40) {
			individuCourant=individuCourant>0 ? individuCourant-1 : 0;
		} else if (code==38 || code==39) {
			individuCourant=individuCourant<population.getTaillePopulation()-1 ? individuCourant+1 : population.getTaillePopulation()-1;
		}
		this.repaint();
	}
}
