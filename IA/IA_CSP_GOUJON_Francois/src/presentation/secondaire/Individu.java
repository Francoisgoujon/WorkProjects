package presentation.secondaire;
import java.util.List;

import abstraction.Instanciation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Individu {
	public static final int AUCUNE=2147483647;

	private int genes[];

	public Individu( Instanciation i) {
		this.genes=new int[i.getNombreDeVariables()];
		for (int c=0; c<i.getNbAffectees(); c++) {
			this.genes[c]=i.valeur(c);
		}
	}
	
	public Individu( int longueurADN) {
		this.genes=new int[longueurADN];
	}

	public Individu( int typeGeneration, int longueurADN) {
		this.genes=new int[longueurADN];
		if (typeGeneration==1) {
			for (int i=0 ; i<longueurADN ; i++ )
				this.genes[i]=(int)(Math.random()*longueurADN);
		} else {
			ArrayList<Integer> sac = new ArrayList<Integer>();
			for (int i=0; i<longueurADN; i++) {
				sac.add(i);
			}
			for (int i=0; i<longueurADN; i++) {
				int pos = (int)(Math.random()*sac.size());
				this.genes[i]=sac.get(pos);
				sac.remove(pos);
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
	 * @return
	 */
	public int fitness( ) {
		int nombreDePairesEnConflit=0; 
		int site1, site2;
		for (site1=0 ; site1<(this.longueurADN()-1) ; site1++) {
			for (site2=site1+1 ; site2<this.longueurADN() ; site2++ ) {
				if (enConflit(site1, this.genes[site1], site2, this.genes[site2])) {
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
		// il reste a completer les genes qui ont AUCUNE pour valeur (les colonnes de colonnesSansReine)
		for (int colonne=0;colonne<this.longueurADN(); colonne++) {
			if (this.genes[colonne]==AUCUNE) {
				int index = (int)(Math.random()*lignesNonUtilisee.size());
				this.genes[colonne]=lignesNonUtilisee.get(index);
				lignesNonUtilisee.remove(index);
			}
		}
		System.out.println("-----> "+Arrays.toString(this.genes));
	}

	void muter( int typeMutation) {
		int temp;
		List<Integer> l;
		switch (typeMutation) {
		case 1: genes[(int)(Math.random()*this.longueurADN())]=(int)(Math.random()*this.longueurADN());break;
		case 2: int gene1=(int)(Math.random()*this.longueurADN());
		int gene2=(int)(Math.random()*this.longueurADN());
		while (gene1==gene2) {
			gene2=(int)(Math.random()*this.longueurADN());
		}
		temp=this.genes[gene1];
		this.genes[gene1]=this.genes[gene2];
		this.genes[gene2]=temp;
		break;
		case 3: int geneConflictuel=this.premierGeneEnConflit();
		int geneEchange=(int)(Math.random()*this.longueurADN());
		while (geneEchange==geneConflictuel) {
			geneEchange=(int)(Math.random()*this.longueurADN());
		}
		temp=genes[geneConflictuel];
		this.genes[geneConflictuel]=this.genes[geneEchange];
		this.genes[geneEchange]=temp;
		break;
		case 4: l=this.genesConflictuels();
		if (l.size()!=0) {
			int index = (int)(Math.random()*l.size());
			int geneAPermuter1=l.get(index);
			l.remove(index);
			index = (int)(Math.random()*l.size());
			int geneAPermuter2=l.get(index);
			l.remove(index);
			temp=this.genes[geneAPermuter1];
			this.genes[geneAPermuter1]=this.genes[geneAPermuter2];
			this.genes[geneAPermuter2]=temp;
		}
		break;
		case 5: l=this.genesConflictuels();
		if (l.size()!=0) {
			int index = (int)(Math.random()*l.size());
			int geneAPermuter1=l.get(index);
			l.remove(index);
			index = (int)(Math.random()*l.size());
			int geneAPermuter2=l.get(index);
			l.remove(index);
			if (enConflit( geneAPermuter1, this.genes[geneAPermuter1], geneAPermuter2, this.genes[geneAPermuter2])) {
				int troisiemeGene;
				if (l.size()!=0) {
					index = (int)(Math.random()*l.size());
					troisiemeGene=l.get(index);
					l.remove(index);
				} else {
					troisiemeGene=(int)(Math.random()*this.longueurADN());
					while ((troisiemeGene==geneAPermuter1) || (troisiemeGene==geneAPermuter2)) {
						troisiemeGene=(int)(Math.random()*this.longueurADN());
					}
				}
				temp=this.genes[geneAPermuter1];
				this.genes[geneAPermuter1]=this.genes[geneAPermuter2];
				this.genes[geneAPermuter2]=this.genes[troisiemeGene];
				this.genes[troisiemeGene]=temp;
			} else {
				temp=this.genes[geneAPermuter1];
				this.genes[geneAPermuter1]=this.genes[geneAPermuter2];
				this.genes[geneAPermuter2]=temp;
			}
		}
		break;
		default : l=genesConflictuels();
		if (l.size()!=0) {
			int index = (int)(Math.random()*l.size());
			int geneAPermuter1=l.get(index);
			l.remove(index);
			index = (int)(Math.random()*l.size());
			int geneAPermuter2=l.get(index);
			l.remove(index);
			int meilleur=0;
			int fitnessMeilleur=this.longueurADN()*this.longueurADN();
			for (int pos=0; pos<this.longueurADN(); pos++) {
				if ((pos!=geneAPermuter1) && (pos!=geneAPermuter2)) {
					temp=this.genes[pos];
					this.genes[pos]=this.genes[geneAPermuter1];
					this.genes[geneAPermuter1]=this.genes[geneAPermuter2];
					this.genes[geneAPermuter2]=temp;
					if (fitness()<fitnessMeilleur) {
						fitnessMeilleur=fitness();
						meilleur=pos;
					}
					temp=this.genes[geneAPermuter2];
					this.genes[geneAPermuter2]=this.genes[geneAPermuter1];
					this.genes[geneAPermuter1]=this.genes[pos];
					this.genes[pos]=temp;
				}
				if (fitnessMeilleur<(this.longueurADN()*this.longueurADN())) {
					temp=this.genes[meilleur];
					this.genes[meilleur]=this.genes[geneAPermuter1];
					this.genes[geneAPermuter1]=this.genes[geneAPermuter2];
					this.genes[geneAPermuter2]=temp;
				}
			}
		}
		break;
		}
	}

	int premierGeneEnConflit() {
		int site1,site2;
		for (site1=0; site1<(this.longueurADN()-1); site1++) {
			for (site2=site1 ; site2<this.longueurADN() ; site2++) {
				if (enConflit(site1, genes[site1], site2, genes[site2])) {
					return site1;
				}
			}
		}
		return AUCUNE;
	}

	public List<Integer> genesConflictuels() {
		List<Integer> res = new LinkedList<Integer>();
		for (int colonne=0; colonne<this.longueurADN(); colonne++) {
			if (enConflit(colonne)) {
				res.add(colonne);
			}
		}
		return res;
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
}

