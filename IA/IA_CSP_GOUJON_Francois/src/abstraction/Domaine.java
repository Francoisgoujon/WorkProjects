package abstraction;

public class Domaine  {
	public static final int AUCUNE =2147483647;
	private boolean presence[];
	private int precedente[];
	private int suivante[];
	private int premiere;

	public Domaine( int taille ) {
		presence=new boolean[taille];
		precedente=new int[taille];
		suivante=new int[taille];
		premiere=0;
		for (int i=0 ; i<taille ; i++ ) {
			precedente[i]=i-1;
			suivante[i]=i+1;
			presence[i]=true;
		}
		suivante[taille-1]=AUCUNE;
		precedente[0]=AUCUNE;
	}

	public boolean presente( int valeur ) {
		return presence[valeur];
	}

	public void ajouter( int valeur ) {
		presence[valeur]=true;
		suivante[valeur]=premiere; 
		if (premiere!=AUCUNE) {
			precedente[premiere]=valeur;
		}
		premiere=valeur;
	}

	public void retirer( int valeur ) {
		presence[valeur]=false;
		if (premiere==valeur) {
			if (suivante[valeur]!=AUCUNE) {
				precedente[suivante[valeur]]=AUCUNE;
			}
			premiere=suivante[valeur];
		}else {
			if (suivante[valeur]!=AUCUNE) {
				precedente[suivante[valeur]]=precedente[valeur];
			}
			suivante[precedente[valeur]]=suivante[valeur];
		}
	}

	public void rendrePremiere( int valeur ) {
		retirer(valeur);ajouter(valeur);
	}

	public int premiere() {
		return premiere; 
	}

	public int suivante( int valeur) {
		return suivante[valeur];
	}
}
