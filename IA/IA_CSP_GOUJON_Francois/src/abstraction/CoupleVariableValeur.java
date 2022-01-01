package abstraction;
//=============================================================================
//   Cette classe permet de représenter un couple forme d'une variable et d'une
// valeur. Elle est utilisee par la classe pileCouplesVariableValeur pour 
// representer un ensemble de tels couples et ainsi permettre a forward-checking
// de memoriser les retraits de valeurs effectues a chaque niveau de l'arbre de
// recherche et l'autoriser à annuler ces retraits.
//=============================================================================
public class CoupleVariableValeur {
		    private int variable;
		    private int valeur;
		    private CoupleVariableValeur suivant;

		    CoupleVariableValeur(int variable, int valeur, CoupleVariableValeur suivant) {
		       this.variable=variable;
		       this.valeur=valeur;
		       this.suivant=suivant;
		    }

		    int variable() {
		       return variable;
		    }

		    int valeur() {
		       return valeur;
		    }

		    CoupleVariableValeur suivant() {
		       return suivant;
		    }
		 }