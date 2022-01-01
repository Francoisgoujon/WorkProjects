package abstraction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class Individu {
	public static final int AUCUNE=2147483647;

	private int genes[];

	public Individu( int longueurADN) {
		this.genes=new int[longueurADN];
	}

	public Individu( int typeGeneration, int longueurADN) {
		this.genes=new int[longueurADN];
		if (typeGeneration==1) {
			for (int i=0 ; i<longueurADN ; i++ ) {
				this.genes[i]=(int)(Math.random()*longueurADN);
			}
		} else {
			LinkedList<Integer> temp = new LinkedList<Integer>();
			for (int i=0; i<longueurADN; i++) {
				temp.add(i);
			}
			for (int i=0; i<longueurADN; i++) {
			int index = new Random().nextInt(temp.size());
			this.genes[i] = temp.get(index);
			temp.remove(index);
			}
		}
	}

	public int longueurADN() {
		return this.genes.length;
	}

	public int gene(int site) {
		if (site>=0 && site<=this.longueurADN()) {
			return this.genes[site];
		} else {
			return -1;
		}
	}  

	public void cloner(Individu dolly) {
		for (int gene=0; gene<this.longueurADN(); gene++) {
			this.genes[gene]=dolly.genes[gene];
		}
	}

	/**
	 * Retourne true si une reine placee en (colonne1, ligne1) est en prise avec une reine placee en (colonne2, ligne2)
	 */
	public static boolean enConflit( int colonne1, int ligne1, int colonne2, int ligne2) {
		return ((ligne1==ligne2) || ((ligne2-ligne1)==(colonne2-colonne1)) || ((ligne2-ligne1)==(colonne1-colonne2)) );
	}

	/**
	 * Retourne le nombre de paires de reines en conflit
	 */
	public int fitness( ) {
		int nombreDePairesEnConflit=0;
			for (int colonne=0; colonne < this.longueurADN()-1; colonne++) {
				int c = colonne;
				int l = this.gene(colonne);
				for (int colonne2=colonne+1; colonne2 < this.longueurADN(); colonne2++) {
					int c2 = colonne2;
					int l2 = this.gene(colonne2);
					if (enConflit(c,l,c2,l2)) {
						nombreDePairesEnConflit++;
					}
				}
			}
		return nombreDePairesEnConflit;
	}

	/**
	 * Retourne true si il y a un conflit entre la reine situee dans la colonne position et l'une des reines placee dans une colonne d'index inferieur
	 */
	boolean conflitAvantAvec(int position) {
		if (position<1) {
			return false;
		}
		for (int site2=position-1; site2>=0; site2--) {
			if (enConflit(position, this.genes[position], site2, this.genes[site2])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Modifie this en combinant les genes de this et de conjoint (cf. enonce)
	 */
	void croiser(Individu conjoint) {
		System.out.println("croise "+Arrays.toString(this.genes));
		System.out.println("  avec "+Arrays.toString(conjoint.genes));
		int temp;
		LinkedList<Integer> lignesNonUtilisee = new LinkedList<Integer>();
		for (int i=0; i<this.longueurADN(); i++) {
			lignesNonUtilisee.add(i);
		}
		for (int colonne=0 ; colonne<this.longueurADN(); colonne++) {
			if (colonne%2==0) { // priorite au gene de this
				if (conflitAvantAvec(colonne)) {
					this.genes[colonne]=conjoint.genes[colonne];
					if (conflitAvantAvec(colonne)) {
						this.genes[colonne]=AUCUNE;
					}
				}
			} else {// priorite au conjoint
				temp=this.genes[colonne];
				this.genes[colonne]=conjoint.genes[colonne];
				if (conflitAvantAvec(colonne)) {
					this.genes[colonne]=temp;
					if (conflitAvantAvec(colonne)) {
						this.genes[colonne]=AUCUNE;
					}
				}
			}
			if (this.genes[colonne]!=AUCUNE) {
				lignesNonUtilisee.remove(Integer.valueOf(this.genes[colonne]));
			}
		}
		// il reste a completer les genes qui ont AUCUNE pour valeur en tirant au sort parmi les lignes de lignesNonUtilisees
		for (int colonne=0 ; colonne<this.longueurADN(); colonne++) {
			if (this.genes[colonne] == AUCUNE) {
				int index = new Random().nextInt(lignesNonUtilisee.size());
				this.genes[colonne] = lignesNonUtilisee.get(index);
				lignesNonUtilisee.remove(index);
			}
		}
		

		System.out.println("-----> "+Arrays.toString(this.genes));
	}

	void muter( int typeMutation) {
		switch (typeMutation) {
		case 1: genes[(int)(Math.random()*this.longueurADN())]=(int)(Math.random()*this.longueurADN());break;
		case 2: 
			int c1 = new Random().nextInt(this.longueurADN());
			int c2 = new Random().nextInt(this.longueurADN());
			int temp = this.gene(c2);
			this.genes[c2] = this.gene(c1);
			this.genes[c1] = temp;
		break;
		case 3: 
			int premiere = this.premiereEnConflit();
			if (premiere == -1) {
				break;
			}
			int reine = new Random().nextInt(this.longueurADN());
			while (reine==premiere) {
				 reine = new Random().nextInt(this.longueurADN());
			}
			int temp2 = this.gene(reine);
			this.genes[reine] = this.gene(premiere);
			this.genes[premiere] = temp2;
		break;
		case 4: 
			LinkedList<Integer> enPrise = this.reinesEnPrise();
			int taille = enPrise.size();
			int index1 = new Random().nextInt(taille);
			int index2 = new Random().nextInt(taille);
			while (index1==index2) {
				 index2 = new Random().nextInt(taille);
			}
			int reine1 = enPrise.get(index1);
			int reine2 = enPrise.get(index2);
			int temp3 = this.gene(reine2);
			this.genes[reine2] = this.gene(reine1);
			this.genes[reine1] = temp3;
		break;
		case 5: 
			LinkedList<Integer> enPrise2 = this.reinesEnPrise();
			int taille2 = enPrise2.size();
			int index12 = new Random().nextInt(taille2);
			int index22 = new Random().nextInt(taille2);
			while (index22==index12) {
				 index22 = new Random().nextInt(taille2);
			}
			int reine12 = enPrise2.get(index12);
			int reine22 = enPrise2.get(index22);
			if (enConflit(reine12, this.gene(reine12), reine22, this.gene(reine22))) {
				if (taille2 > 2) {
					int index32 = new Random().nextInt(taille2);
					while (index32==index12 || index32 == index22) {
						 index32 = new Random().nextInt(taille2);
					}
					int reine32 = enPrise2.get(index32);
					int temp4 = this.gene(reine12);
					this.genes[reine12] = this.gene(reine22);
					this.genes[reine22] = this.gene(reine32);
					this.genes[reine32] = temp4;
				}
				else {
					int reine32 = new Random().nextInt(this.longueurADN());
					while (reine32==reine12 || reine32 == reine22) {
						 reine32 = new Random().nextInt(this.longueurADN());
					}
					int temp4 = this.gene(reine12);
					this.genes[reine12] = this.gene(reine22);
					this.genes[reine22] = this.gene(reine32);
					this.genes[reine32] = temp4;
				}
			} else {
			int temp4 = this.gene(reine22);
			this.genes[reine22] = this.gene(reine12);
			this.genes[reine12] = temp4;
			}
		break;
		default : 
			//============================
			//
			// QUESTION 4 f)
			//
			// A COMPLETER PAR VOS SOINS 
			//
			//============================
		break;
		}
	}

	public boolean enConflit(int colonne) {
		boolean conflit=false;
		int col2=0;
		while ((!conflit) && (col2<this.longueurADN())) {
			conflit=((col2!=colonne) && (enConflit(colonne, this.genes[colonne], col2, this.genes[col2])));
			col2++;
		}
		return conflit;
	}
	
	public int premiereEnConflit() {
		int premiere = -1;
		int i =0;
		while (premiere <0 && i < this.longueurADN()) {
			if (this.enConflit(i)) {
				premiere = i;
			}
		}
		return i;
	}
	
	public LinkedList<Integer> reinesEnPrise() {
		LinkedList<Integer> list = new LinkedList<Integer>();
		for (int colonne=0; colonne<this.longueurADN(); colonne++) {
			if (enConflit(colonne)) {
				list.add(colonne);
			}
		}
		return list;
	}
}

