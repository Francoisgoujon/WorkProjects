package abstraction;

import presentation.secondaire.FenetreSolution;

public class ConstraintNetwork {
	public static final int AUCUNE =2147483647;

	private EnsembleDomaines domaines=new EnsembleDomaines(5,5); 

	public ConstraintNetwork(int n) {
		this.domaines = new EnsembleDomaines(n, n);
	}

	public static void afficher(Instanciation i) {
		new FenetreSolution(i,400,400).setVisible(true);
	}

	public boolean generateAndTest( Instanciation i) {
		int variableCourante= i.premiereNonAffectee();
		if (variableCourante==ConstraintNetwork.AUCUNE) {
			if (!(i.consistante())) {
				return false;
			}
			afficher(i);
			return true;
		}

		int valeurCourante= domaines.premiere( variableCourante );
		boolean solutionTrouvee=false;

		while ((!solutionTrouvee) && (valeurCourante!=ConstraintNetwork.AUCUNE)) {
			i.affecter( variableCourante, valeurCourante );
			solutionTrouvee = generateAndTest( i );
			if (!solutionTrouvee) {
				i.desaffecter( variableCourante );
				valeurCourante= domaines.suivante( variableCourante , valeurCourante);
			}
		}
		return solutionTrouvee;
	}

	/**
	 * Similaire a generateAndTest mais : 
	 * - verifie la consistance de l'instanciation avant d'appeler recursivement backtrack
	 * - si l'instanciation est complete, il est inutile de verifier la consistance puisqu'on l'aura verifie au fur et a mesure.
	 */
	public boolean backtrack( Instanciation i) {
		boolean solutionTrouvee=false;
		int variableCourante= i.premiereNonAffectee();
		if (variableCourante==ConstraintNetwork.AUCUNE) {
			afficher(i);
			return true;
		}

		int valeurCourante= domaines.premiere( variableCourante );

		while ((!solutionTrouvee) && (valeurCourante!=ConstraintNetwork.AUCUNE)) {
			i.affecter( variableCourante, valeurCourante );
			if (i.consistante()) {
				solutionTrouvee = backtrack( i );
			}
			if (!solutionTrouvee) {
				i.desaffecter( variableCourante );
				valeurCourante= domaines.suivante( variableCourante , valeurCourante);
			}
		}
		

		return solutionTrouvee;
	}

/**
 * - supprime des domaines les positions qui sont en prises avec la derniere affectation.
 * - chaque suppression d'une valeur d'un domaine entre l'ajout d'une paire sur la pile supressions afin de garder trace des retraits
 * - retourne false si un des domaines est completement vide (sinon retourne true).
 */
	public boolean filtrer(Instanciation I, PileCouplesVariableValeur suppressions) {
		int lastvar = I.derniereAffectee();
		int var = I.premiereNonAffectee();
		while (var != AUCUNE) {
			int val = I.valeur(lastvar);
			if (domaines.presente(var, val)) {
			domaines.retirer(var, val);
			suppressions.empiler(var, val);
			}
			if (((val + (var-lastvar)) < domaines.nombreDeVariables() && ((val + (var-lastvar)) >= 0 )) && domaines.presente(var, val + (var-lastvar))) {
			domaines.retirer(var, val + (var-lastvar));
			suppressions.empiler(var, val + (var-lastvar));
			}
			if (((val - (var-lastvar)) < domaines.nombreDeVariables() && ((val - (var-lastvar)) >= 0 )) && domaines.presente(var, val - (var-lastvar))) {
			domaines.retirer(var, val - (var-lastvar));
			suppressions.empiler(var, val - (var-lastvar));
			}
			if (domaines.premiere(var) == AUCUNE) {
				return false;
			}
			
			var = I.suivante(var);
		}
		
		return true;
	}

	public boolean forwardChecking( Instanciation i) {
		int variableCourante= i.premiereNonAffectee();
		if (variableCourante==ConstraintNetwork.AUCUNE) {
			afficher(i);
			return true;
		}

		PileCouplesVariableValeur suppressions=new PileCouplesVariableValeur();

		int valeurCourante= domaines.premiere( variableCourante );
		boolean solutionTrouvee=false;

		while ((!solutionTrouvee) && (valeurCourante!=ConstraintNetwork.AUCUNE)) {
			i.affecter( variableCourante, valeurCourante );
			if (filtrer(i, suppressions)) {
				solutionTrouvee = forwardChecking( i );
			}
			if (!solutionTrouvee) {
				i.desaffecter( variableCourante );
				valeurCourante= domaines.suivante( variableCourante , valeurCourante);
			}
			domaines.restaurer( suppressions );
		}
		return solutionTrouvee;
	}

	public void ordrePair1( int n ) {
		int p = 0;
		int i = 0;
		for (int colonne = 0 ; colonne < n ; colonne++) {
			if ((colonne%2) == 0) {
				domaines.rendrePremiere(colonne, n/(2+p));
				p++;
			}
			else {
				domaines.rendrePremiere(colonne, i);
				i++;
			}
		}
	
	}

	public void ordrePair2( int n ) {
		for (int ligne=1; ligne<=n/2; ligne++) {
			domaines.rendrePremiere((2*ligne+(n/2)-3) %n, ligne-1);
		}
		for (int ligne=(n/2)+1; ligne<=n; ligne++) {
			domaines.rendrePremiere(n-( (2*(n-ligne+1)+(n/2)-3)%n)-1, ligne-1);
		}
	}

	public void ordre(int n ) {
		if ((n%2)!=0) {
			ordre(n-1);
			domaines.rendrePremiere(n-1,n-1);
		} else {
			if ((n%6)!=2) {
				ordrePair1(n);
			} else {
				ordrePair2(n);
			}
		}
	}
}
