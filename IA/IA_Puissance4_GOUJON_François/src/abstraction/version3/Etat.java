package abstraction.version3;

public class Etat {

	// CONSTANTES
	//-----------
	public final static int NB_COLONNES = 7;
	public final static int NB_LIGNES   = 6;

	public final static int PERSONNE = 0;
	public final static int HUMAIN   = 1;
	public final static int ORDI     = 2; 

	public final static int PROFONDEUR_MAX = 6;

	// VARIABLES D'INSTANCE (ATTRIBUTS)
	// --------------------------------
	private int grille[][];
	// grille[c][l] vaut PERSONNE, HUMAIN ou ORDI selon que la
	// case a la colonne c et la ligne l est occupee par 
	// personne, un pion du joueur ou un pion de l'ordinateur.

	// METHODES
	// --------

	/**
	 * Constructeur initialisant la grille de sorte que chaque case est inoccupee (~est occupee par PERSONNE)
	 */
	public Etat() {
		this.raz();
	}

	/**
	 * Constructeur utile uniquement pour les tests
	 */
	public Etat(int[][] g) {
		this.grille = g;
	}

	public void raz() {
		this.grille =new int[NB_COLONNES][NB_LIGNES];
		for (int col=0 ; col<NB_COLONNES ; col++ ) {
			for (int lig=0 ; lig<NB_LIGNES ; lig++) {
				this.grille[col][lig]=PERSONNE;
			}
		}
	}
	/**
	 * Modifie la grille pour qu'elle corresponde a e
	 * @param e, une grille
	 */
	public void recopier(Etat e) {
		for (int col=0 ; col<NB_COLONNES ; col++ ) {
			for (int lig=0 ; lig<NB_LIGNES ; lig++) {
				this.grille[col][lig]=e.grille[col][lig];
			}
		}
	}

	public int get(int c, int l) {
		if (c>=0 && c<this.grille.length && l>=0 && l<this.grille[c].length) {
			return this.grille[c][l];
		}
		else {
			return Etat.PERSONNE;
		}
	}

	/**
	 * @param joueur, HUMAIN ou ORDI
	 * @return retourne ORDI si joueur vaut joueur, HUMAIN sinon.
	 */
	public int adversaire(int joueur) {
		if (joueur==HUMAIN) {
			return ORDI; 
		} else {
			return HUMAIN;
		}
	}

	/**
	 * @param colonne, l'index d'une colonne
	 * @return true si et seulement si la colonne colonne n'est pas completement remplie
	 */
	public boolean peutDeposerPion(int colonne) {
		return (this.grille[colonne][NB_LIGNES-1]==0);
	}


	/**
	 * depose un pion du joueur joueur dans la colonne colonne si celle-ci n'est pas pleine.
	 * @param colonne, une colonne (entier de [0, NB_COLONNES[ )
	 * @param joueur, HUMAIN ou ORDI
	 * @return la ligne dans laquelle le pion a ete depose 
	 */
	public int deposerPion(int colonne, int joueur) {
		int ligne=0;
		while ((ligne<NB_LIGNES) && (this.grille[colonne][ligne]!=PERSONNE)) {
			ligne++;
		}
		if (ligne<NB_LIGNES) {
			this.grille[colonne][ligne]=joueur;
		}
		return ligne;
	}

	/**
	 * @return Retourne true si et seulement si la grille est pleine
	 */
	public boolean pleine() {
		boolean pleine = true;
		int i=0;
		while (i<Etat.NB_COLONNES && pleine) {
			if (this.peutDeposerPion(i)) {
				pleine = false;
			}
			i++;
		}
		return pleine;
	}	/**
	 * 
	 * @return retourne true si l'etat est une feuille, 
	 *          c'est a dire si il existe un gagnant ou 
	 *          si la grille est pleine.
	 */
	public boolean feuille() {
		return (this.gagnant()!=PERSONNE) || this.pleine();
	}

	/**
	 * 
	 * @param col, col est dans [0, NB_COLONNES-4]
	 * @param lig, lig est dans [0, NB_LIGNES]
	 * @return retourne PERSONNE si les 4 cases horizontales
	 *          de (col,lig) a (col+3, lig) n'appartiennent 
	 *          pas toutes au meme joueur. Sinon, retourne
	 *          HUMAIN ou ORDI selon a quel joueur appartiennent
	 *          les 4 cases.
	 */
	public int gagnantHorizontal(int col, int lig) {
		if ((this.grille[col][lig]==this.grille[col+1][lig]) 
				&& (this.grille[col][lig]==this.grille[col+2][lig]) 
				&& (this.grille[col][lig]==this.grille[col+3][lig])) {
			return this.grille[col][lig];
		} else {
			return PERSONNE;
		}
	}
	
	/**
	 * 
	 * @param col, col est dans [0, NB_COLONNES]
	 * @param lig, lig est dans [0, NB_LIGNES-4]
	 * @return retourne PERSONNE si les 4 cases verticales
	 *          de (col,lig) a (col, lig+3) n'appartiennent 
	 *          pas toutes au meme joueur. Sinon, retourne
	 *          HUMAIN ou ORDI selon a quel joueur appartiennent
	 *          les 4 cases.
	 */
	public int gagnantVertical(int col, int lig) {
		if ((this.grille[col][lig]==this.grille[col][lig+1]) 
				&& (this.grille[col][lig]==this.grille[col][lig+2]) 
				&& (this.grille[col][lig]==this.grille[col][lig+3])) {
			return this.grille[col][lig];
		} else {
			return PERSONNE;
		}
	}
	
	/**
	 * 
	 * @param col, col est dans [0, NB_COLONNES-4]
	 * @param lig, lig est dans [0, NB_LIGNES-4]
	 * @return retourne PERSONNE si les 4 cases 
	 *        	(col,lig), (col+1,lig+1), (col+2,lig+2), (col+3, lig+3) n'appartiennent 
	 *          pas toutes au meme joueur. Sinon, retourne
	 *          HUMAIN ou ORDI selon a quel joueur appartiennent
	 *          les 4 cases.
	 */
	public int gagnantDiagonaleMontante(int col, int lig) {
		if ((this.grille[col][lig]==this.grille[col+1][lig+1]) 
				&& (this.grille[col][lig]==this.grille[col+2][lig+2]) 
				&& (this.grille[col][lig]==this.grille[col+3][lig+3])) {
			return this.grille[col][lig];
		} else {
			return PERSONNE;
		}
	}
	
	/**
	 * 
	 * @param col, col est dans [3, NB_COLONNES-1]
	 * @param lig, lig est dans [0, NB_LIGNES-4]
	 * @return retourne PERSONNE si les 4 cases 
	 *        	(col,lig), (col-1,lig+1), (col-2,lig+2), (col-3, lig+3) n'appartiennent 
	 *          pas toutes au meme joueur. Sinon, retourne
	 *          HUMAIN ou ORDI selon a quel joueur appartiennent
	 *          les 4 cases.
	 */
	public int gagnantDiagonaleDescendante(int col, int lig) {
		if ((this.grille[col][lig]==this.grille[col-1][lig+1]) 
				&& (this.grille[col][lig]==this.grille[col-2][lig+2]) 
				&& (this.grille[col][lig]==this.grille[col-3][lig+3])) {
			return this.grille[col][lig];
		} else {
			return PERSONNE;
		}
	}
	

	/**
	 * 
	 * @return Si il existe 4 cases alignees dans la grille,
	 *          retourne le proprietaire des 4 cases, sinon 
	 *          retourne PERSONNE.
	 */
	public int gagnant() {
		int vainqueur=PERSONNE;
		int col=0, lig;

		while ((vainqueur==PERSONNE) && (col<NB_COLONNES)) {
			lig=0;
			while ((vainqueur==PERSONNE) && (lig<NB_LIGNES)) {
				if ((col<(NB_COLONNES-3)) && (this.gagnantHorizontal(col,lig)!=PERSONNE)) {
					vainqueur=this.gagnantHorizontal(col, lig);
				}
				if ((lig<(NB_LIGNES-3)) && (this.gagnantVertical(col,lig)!=PERSONNE)) {
					vainqueur=this.gagnantVertical(col, lig);
				}
				if ((col<(NB_COLONNES-3)) && (lig<(NB_LIGNES-3)) && (this.gagnantDiagonaleMontante(col,lig)!=PERSONNE)) {
					vainqueur=this.gagnantDiagonaleMontante(col, lig);
				}
				if ((col > 2) && (col<NB_COLONNES) && (lig<(NB_LIGNES-3)) && (this.gagnantDiagonaleDescendante(col,lig)!=PERSONNE)) {
					vainqueur=this.gagnantDiagonaleDescendante(col, lig);
				}
				lig++;
			}
			col++;
		}
		return vainqueur;
	}

	// POUR LA QUESTION 3 UNIQUEMENT !!!!
	public int estimeHorizontal(int col, int lig, int joueur) {
		//-------------------------------------------------
		int adv = adversaire( joueur );
		if   ( (this.grille[col][lig]!=adv) 
				&& (this.grille[col+1][lig]!=adv)  
				&& (this.grille[col+2][lig]!=adv) 
				&& (this.grille[col+3][lig]!=adv) ) {
			return (int)Math.pow(2,(this.grille[col][lig]+this.grille[col+1][lig]+this.grille[col+2][lig]+this.grille[col+3][lig])/joueur);
		}
		else {
			return 0;
		}
	}
	
	public int estimeVertical(int col, int lig, int joueur) {
		//-------------------------------------------------
		int adv = adversaire( joueur );
		if   ( (this.grille[col][lig]!=adv) 
				&& (this.grille[col][lig+1]!=adv)  
				&& (this.grille[col][lig+2]!=adv) 
				&& (this.grille[col][lig+3]!=adv) ) {
			return (int)Math.pow(2,(this.grille[col][lig]+this.grille[col][lig+1]+this.grille[col][lig+2]+this.grille[col][lig+3])/joueur);
		}
		else {
			return 0;
		}
	}
	
	public int estimeDiagoMontante(int col, int lig, int joueur) {
		//-------------------------------------------------
		int adv = adversaire( joueur );
		if   ( (this.grille[col][lig]!=adv) 
				&& (this.grille[col+1][lig+1]!=adv)  
				&& (this.grille[col+2][lig+2]!=adv) 
				&& (this.grille[col+3][lig+3]!=adv) ) {
			return (int)Math.pow(2,(this.grille[col][lig]+this.grille[col+1][lig+1]+this.grille[col+2][lig+2]+this.grille[col+3][lig+3])/joueur);
		}
		else {
			return 0;
		}
	}
	
	public int estimeDiagoDescendante(int col, int lig, int joueur) {
		//-------------------------------------------------
		int adv = adversaire( joueur );
		if   ( (this.grille[col][lig]!=adv) 
				&& (this.grille[col-1][lig+1]!=adv)  
				&& (this.grille[col-2][lig+2]!=adv) 
				&& (this.grille[col-3][lig+3]!=adv) ) {
			return (int)Math.pow(2,(this.grille[col][lig]+this.grille[col-1][lig+1]+this.grille[col-2][lig+2]+this.grille[col-3][lig+3])/joueur);
		}
		else {
			return 0;
		}
	}


	public int estime() {
		//-------------------------------------------------
		int col=0, lig;
		int ordi=0;
		int humain=0;

		for (col=0; col<NB_COLONNES; col++) {
			for (lig=0; lig<NB_LIGNES; lig++) {
				if (col<(NB_COLONNES-3)) {
					ordi+=estimeHorizontal( col, lig, ORDI);
					humain+=estimeHorizontal( col, lig, HUMAIN);
				}
				if (lig<(NB_LIGNES-3)) {
					ordi+=estimeVertical( col, lig, ORDI);
					humain+=estimeVertical( col, lig, HUMAIN);
				}
				if ((col<(NB_COLONNES -3)) && (lig<(NB_LIGNES -3))) {
					ordi+=estimeDiagoMontante( col, lig, ORDI);
					humain+=estimeDiagoMontante( col, lig, HUMAIN);
				}
				if ((col > 2) && (col<NB_COLONNES) && (lig<(NB_LIGNES -3))) {
					ordi+=estimeDiagoDescendante( col, lig, ORDI);
					humain+=estimeDiagoDescendante( col, lig, HUMAIN);
				}
				
			}
		}
		return (ordi-humain);
	}

	int etiquette(int joueurCourant, int profondeur) {
		if (feuille() || profondeur == 0) {
			return gagnant();
		}
		else {
			boolean filsNul=false;
			int etiquetteFils;
			Etat fils=new Etat();
			// On va tester les differentes possibilites, 
			// c'est-a-dire essayer de mettre un pion dans 
			// les differentes colonnes pour voir si l'un des 
			// ces coups peut nous garantir la victoire.
			for (int colonne=0; colonne<NB_COLONNES; colonne++) {
				fils.recopier(this);
				if (fils.peutDeposerPion(colonne)) {
					// La colonne colonne n'est pas pleine : on peut essayer 
					// de voir ce que donnerait le depot d'un pion dans cette colonne.
					fils.deposerPion(colonne, joueurCourant);
					etiquetteFils=fils.etiquette( adversaire(joueurCourant), profondeur -1 );
					if (etiquetteFils==joueurCourant) {
						// en jouant dans cette colonne on est assure de gagner !
						return joueurCourant;
					}
					if (etiquetteFils==PERSONNE) {
						// Il existe au moins un coup qui mene a un match nul.
						filsNul=true;
					}
				}
			}
			if (filsNul) {
				return PERSONNE;
			} else {
				// Toutes les colonnes mene l'adversaire a gagner : on est sur de perdre.
				return adversaire(joueurCourant);
			}
		}
	}

	/**
	 * @return La colonne correspondant au meilleur coup que peut joueur l'ordinateur avec une profondeur de raisonnement limitee a PROFONDEUR_MAX
	 */
	public   int meilleurCoupPourOrdinateur() {
		int filsNul=NB_COLONNES;
		int coupPossible=NB_COLONNES;
		int etiquetteFils;
		int meilleureNotePersonne = - 1000;
		int meilleureNotePerdant = - 1000;

		Etat fils=new Etat();
		int noteFils;
		for (int colonne=0; colonne<NB_COLONNES; colonne++) {
			fils.recopier(this);
			if (fils.peutDeposerPion(colonne)) {
				fils.deposerPion(colonne, ORDI);
				etiquetteFils=fils.etiquette( HUMAIN, PROFONDEUR_MAX );
				if (etiquetteFils==ORDI) {
					return colonne;
				}
				noteFils = fils.estime();
				if (etiquetteFils==PERSONNE) {
					if (noteFils > meilleureNotePersonne) {
						meilleureNotePersonne = noteFils;
						filsNul=colonne;
					}
				} else {
					if (noteFils > meilleureNotePerdant) {
						meilleureNotePerdant = noteFils;
						coupPossible=colonne;
					}
				}
			}
		}
		if (filsNul<NB_COLONNES) {
			return filsNul;
		} else {
			return coupPossible;
		}
	}
	public String toString() {
		String[] lettres = {"P","H","O"};
		String res="";
		for (int lig=Etat.NB_LIGNES-1; lig>=0; lig--) {
			for (int col=0; col<Etat.NB_COLONNES; col++) {
				res=res+lettres[this.get(col, lig)];
			}
			res=res+"\n";
		}
		return res;
	}

}
