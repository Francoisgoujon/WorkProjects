package abstraction;

//Permet de representer une instanciation, partielle ou complete, en ayant
//acces � la fois aux variables affectees (et leurs valeurs) et aux variables
//pas encore affectees.
public  class Instanciation {

	public static final int AUCUNE =2147483647;

	private int valeur[];
	private int precedente[];
	private int suivante[];
	private int premiereAffectee;
	private int premiereNonAffectee;
	private int derniereAffectee;
	private int derniereNonAffectee;

	public Instanciation( int nombreDeVariables ) {
		valeur=new int[nombreDeVariables];
		precedente=new int[nombreDeVariables];
		suivante=new int[nombreDeVariables];
		premiereAffectee=AUCUNE;
		derniereAffectee=AUCUNE;
		premiereNonAffectee=0;
		derniereNonAffectee=nombreDeVariables-1;
		for (int i=0 ; i<nombreDeVariables ; i++ ) {
			valeur[i]=AUCUNE;
			precedente[i]=i-1;
			suivante[i]=i+1;
		}
		precedente[0]=AUCUNE;
		suivante[nombreDeVariables-1]=AUCUNE;
	}

	public int getNombreDeVariables() {
		return this.valeur.length;
	}

	public int getNbAffectees() {
		int nb=0; 
		int aff = this.premiereAffectee;
		while (aff!=AUCUNE) {
			nb++;
			aff = this.suivante(aff);
		}
		return nb;
	}

	public int valeur( int variable ) {
		return valeur[variable];
	}

	public int premiereNonAffectee() {
		return premiereNonAffectee;
	}

	public int premiereAffectee() {
		return premiereAffectee;
	}

	public int derniereAffectee() {
		return derniereAffectee;
	}

	public int suivante( int variable ) {
		return suivante[variable];
	}

	public void affecter( int variable, int valeur) {
		if (this.valeur[variable]!=AUCUNE) {
			System.out.println("....Appel de affecter sur une variable deja affectee.....");
		}
		this.valeur[variable]=valeur;
		if (premiereNonAffectee==variable) {
			premiereNonAffectee=suivante[variable];
			if (premiereNonAffectee!=AUCUNE) {
				precedente[premiereNonAffectee]=AUCUNE;
			}
			if (derniereNonAffectee==variable) {
				derniereNonAffectee=AUCUNE;
			}
		} else {
			if (derniereNonAffectee==variable) {
				derniereNonAffectee=precedente[variable];
				suivante[derniereNonAffectee]=AUCUNE;
				if (premiereNonAffectee==variable) {
					premiereNonAffectee=AUCUNE;
				}
			} else {
				suivante[precedente[variable]]=suivante[variable];
				precedente[suivante[variable]]=precedente[variable];
			}
		}
		this.precedente[variable]=derniereAffectee;
		this.suivante[variable]=AUCUNE;
		if (derniereAffectee!=AUCUNE) {
			suivante[derniereAffectee]=variable;
		}
		derniereAffectee=variable;
		if (premiereAffectee==AUCUNE) {
			premiereAffectee=variable;
		}
	}

	public void desaffecter( int variable ) {
		if (valeur[variable]==AUCUNE) {
			System.out.println("....Appel de desaffecter sur une variable non affectee.....");
		}
		valeur[variable]=AUCUNE;
		if (premiereAffectee==variable) {
			premiereAffectee=suivante[variable];
			if (premiereAffectee!=AUCUNE) {
				precedente[premiereAffectee]=AUCUNE;
			}
			if (derniereAffectee==variable) {
				derniereAffectee=AUCUNE;
			}
		} else {
			if (derniereAffectee==variable) {
				derniereAffectee=precedente[variable];
				suivante[derniereAffectee]=AUCUNE;
				if (premiereAffectee==variable) {
					premiereAffectee=AUCUNE;
				}
			} else {
				suivante[precedente[variable]]=suivante[variable];
				precedente[suivante[variable]]=precedente[variable];
			}
		}
		precedente[variable]=derniereNonAffectee;
		suivante[variable]=AUCUNE;
		if (derniereNonAffectee!=AUCUNE) {
			suivante[derniereNonAffectee]=variable;
		}
		derniereNonAffectee=variable;
		if (premiereNonAffectee==AUCUNE) {
			premiereNonAffectee=variable;
		}
	}

	public static boolean consistant( int variable1, int valeur1, int variable2, int valeur2) {
		return ((valeur1!=valeur2) && 
				(((valeur2-valeur1)!=(variable2-variable1)) && 
						((valeur2-valeur1)!=(variable1-variable2)) ));
	}

	/**
	 * @return Retourne true si les variables affectees respectent les contraintes (si les reines placees
	 * ne sont pas en prise)
	 * 
	 * Rappel : 
	 * -  premiereAffectee() retourne la premiere variable affectee
	 * -  si var est une variable affectee, suivante(var) retourne la variable affectee suivante (AUCUNE si var est la derniere affectee)
	 * -  si var est une variable affectee, valeur(var) retourne la valeur affectee a var.
	 */
	public boolean consistante( ) {
		boolean consistance=true;
		int var1 = this.premiereAffectee();
		while (var1 != AUCUNE) {
			int var2 = this.suivante(var1);
			while (var2 != AUCUNE) {
				if (!consistant(var1, valeur(var1), var2, valeur(var2))) {
					consistance = false;
					break;
				}
			var2 = this.suivante(var2);
			}
			var1 = this.suivante(var1);
		}

		return consistance;
	}
}