package abstraction;
//   Represente un ensemble de domaines. Cette classe permet de memoriser pour
// chaque variable l'ensemble des valeurs encore possibles.
public class EnsembleDomaines  {
	private Domaine ensemble[];
	private int nombreDeVariables;

	public EnsembleDomaines( int nombreDeVariables, int tailleInitialeDesDomaines) {
		this.nombreDeVariables=nombreDeVariables;
		ensemble=new Domaine[nombreDeVariables];
		for (int i=0; i<nombreDeVariables ; i++ ) {
			ensemble[i]=new Domaine(tailleInitialeDesDomaines);
		}
	}

	public boolean presente(int variable,int valeur){
		return ensemble[variable].presente(valeur);
	}

	public void ajouter( int variable, int valeur) {
		ensemble[variable].ajouter(valeur);
	}

	public void retirer( int variable, int valeur) {
		ensemble[variable].retirer(valeur);
	}

	public void rendrePremiere( int variable, int valeur) {
		ensemble[variable].rendrePremiere(valeur); 
	}

	public int premiere(int variable) {
		return  ensemble[variable].premiere();
	}

	public int suivante(int variable, int valeur) {
		return ensemble[variable].suivante(valeur);
	}

	public int nombreDeVariables() {
		return nombreDeVariables;
	}

	public void restaurer(PileCouplesVariableValeur pile) {
		CoupleVariableValeur couple;
		while (!pile.vide()) {
			couple=pile.depiler();
			ensemble[ couple.variable() ].ajouter( couple.valeur() );
		}
	}
}
