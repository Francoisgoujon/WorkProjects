package abstraction;

//=============================================================================
// CLASSE PileCouplesVariableValeur <<< UTILE UNIQUEMENT POUR LA QUESTION 3 >>>
//=============================================================================
//  Permet de representer une pile de couples (variable, valeur). Utilisee par
// forward checking pour memoriser les retraits de valeurs effectues a chaque
// niveau de l'arbre et ainsi pouvoir defaire ces retraits.
//=============================================================================

public class PileCouplesVariableValeur {
	private CoupleVariableValeur sommet;

	public PileCouplesVariableValeur() {
		sommet=null;
	}

	public boolean vide() {
		return (sommet==null);
	}

	public void empiler(int var,int val){
		sommet=new CoupleVariableValeur(var,val,sommet);
	}

	public CoupleVariableValeur depiler() {
		CoupleVariableValeur aRetourner=sommet; 
		if (sommet!=null) {
			sommet=sommet.suivant();
		}
		return aRetourner;
	}
}