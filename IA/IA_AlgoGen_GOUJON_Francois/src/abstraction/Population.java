package abstraction;

import presentation.secondaire.Courbe;

public class Population {
	private Individu individus[];
	private int nbGenerations;
	private int typeGeneration;
	private Courbe courbeMoyenne, courbeMeilleur;

	public Population( int taillePopulation, int typeGeneration, int longueurADN  ) {
		this.courbeMoyenne = new Courbe("Moyenne");
		this.courbeMeilleur = new Courbe("Meilleur");
		this.typeGeneration = typeGeneration;
		this.reset(taillePopulation, longueurADN);
	}

	public void reset(int taillePopulation, int longueurADN  ) {
		this.nbGenerations=0;
		this.courbeMeilleur.reset();
		this.courbeMoyenne.reset();
		this.individus=new Individu[taillePopulation];
		for (int individu=0; individu<taillePopulation; individu++) {
			this.individus[individu]=new Individu(typeGeneration, longueurADN);
		}
		this.courbeMeilleur.ajouter(0, indexMieuxAdapte());
		this.courbeMoyenne.ajouter(0, fitnessMoyenne());
		System.out.println("0 : fitness meilleur= "+this.individus[indexMieuxAdapte()].fitness()+"  fitness moyenne= "+fitnessMoyenne());
	}
	
	public Courbe getCourbeMeilleur() {
		return this.courbeMeilleur;
	}
	
	public Courbe getCourbeMoyenne() {
		return this.courbeMoyenne;
	}
	
	public int getTaillePopulation() {
		return this.individus.length;
	}
	
	public int getLongueurADN() {
		return this.individus[0].longueurADN();
	}
	
	public int getNbGenerations() {
		return this.nbGenerations;
	}
	
	public Individu get(int i) {
		return (i>=0 && i<this.individus.length) ? this.individus[i] : null;
	}

	public void setLongueurADN(int longueurADN) {
		reset(getTaillePopulation(), longueurADN);
	}
	
	public void setTaillePopulation(int taillePopulation) {
		reset(taillePopulation, getLongueurADN());
	}
	
	public void setTypeGeneration(int typeGeneration) {
		this.typeGeneration = typeGeneration;
		this.reset(getTaillePopulation(), getLongueurADN());
	}
	
	public void evolution( int nombreDeGenerations, int pourcentageReproduction, int pourcentageCroisements, int pourcentageMutations, int typeMutation) {
		for (int evolution=1; evolution<=nombreDeGenerations; evolution++) {
			this.nbGenerations++;
			selection(pourcentageReproduction);
			croisement(pourcentageCroisements);
			mutation(pourcentageMutations, typeMutation);

			this.courbeMeilleur.ajouter(this.nbGenerations, this.individus[indexMieuxAdapte()].fitness());
			this.courbeMoyenne.ajouter(this.nbGenerations, fitnessMoyenne());
			System.out.println(this.nbGenerations+" : fitness meilleur= "+this.individus[indexMieuxAdapte()].fitness()+"  fitness moyenne= "+fitnessMoyenne());
		}
	}

	public void selection(int pourcentageDeReproduction) {
		// on trie la generation du meilleur individu au plus mauvais
		int individu, individu2, meilleur;
		Individu clone=new Individu(this.individus[0].longueurADN());

		for (individu=0; individu<this.individus.length-1;individu++ ) {
			meilleur=individu;
			for (individu2=individu+1; individu2<this.individus.length;individu2++ ) {
				if (this.individus[meilleur].fitness()>this.individus[individu2].fitness()) {
					meilleur=individu2;
				}
			}
			if (meilleur!=individu) {
				clone.cloner(this.individus[individu]);
				this.individus[individu].cloner(this.individus[meilleur]);
				this.individus[meilleur].cloner(clone);
			}
		}
		// on substitue les plus mauvais individus par des clones des meilleurs
		int individuACloner=0;
		int pos=this.individus.length-1;
		int nombreDeClones;
		while ((pos>individuACloner) && (pourcentageDeReproduction>0)) {
			for (nombreDeClones=((this.individus.length*pourcentageDeReproduction)/100); nombreDeClones>0; nombreDeClones--) {
				this.individus[pos].cloner(this.individus[individuACloner]);
				pos--;
			}
			individuACloner++;
			pourcentageDeReproduction--;
		}
	}

	public void croisement(int pourcentageCroisements) {
		Individu clonePere=new Individu(this.individus[0].longueurADN());
		for (int pere=0; pere<this.individus.length-1; pere++) {
			for (int mere=pere+1; mere<this.individus.length; mere++) {
				if ((int)(Math.random()*100)<pourcentageCroisements) {
					clonePere.cloner(this.individus[pere]);
					this.individus[pere].croiser(this.individus[mere]);
					this.individus[mere].croiser(clonePere);
				}
			}
		}
	}

	public void mutation(int pourcentageMutations, int typeMutation) {
		for (int individu=1; individu<this.individus.length; individu++) {
			if ((int)(Math.random()*100)<pourcentageMutations) {
				this.individus[individu].muter(typeMutation);
			}
		}
	}

	public int indexMieuxAdapte() {
		int meilleur=0;
		for (int individuCourant=1 ; individuCourant<this.individus.length ; individuCourant++ ) {
			if (this.individus[individuCourant].fitness()<this.individus[meilleur].fitness()) {
				meilleur=individuCourant;
			}
		}
		return meilleur;
	}
	
	public Individu mieuxAdapte() {
		return this.individus[indexMieuxAdapte()];
	}

	public int fitnessMoyenne() {
		long sommeFitness=this.individus[0].fitness();
		for (int i=1; i<this.individus.length; i++) {
			sommeFitness+=this.individus[i].fitness();
		}
		return ((int)(sommeFitness/(this.individus.length)));
	}
}
