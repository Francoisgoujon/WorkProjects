package abstraction.version4;
//==================================
class tripletEtiquetteGagnantPerdant {
//==================================

// VARIABLES D'INSTANCE / ATTRIBUTS
   private int etiquette, gagnant, perdant;

// METHODES
   tripletEtiquetteGagnantPerdant( int e, int g, int p) {
// Constructeur
      this.etiquette=e; 
      this.gagnant=g; 
      this.perdant=p; 
   }

   public void setEtiquette( int e) {
// Sortie : affecte l'etiquette a e
      this.etiquette=e;
   }

   public void setGagnant( int g) {
// Sortie : affecte le nombre de gagnants a g
      this.gagnant=g;
   }

   public void setPerdant( int p) {
// Sortie : affecte le nombre de perdants a p
      this.perdant=p;
   }

   public int getEtiquette() {
// Sortie : retourne l'etiquette
      return this.etiquette;
   }

   public int getGagnant() {
// Sortie : retourne le nombre de gagnants
      return this.gagnant;
   }

   public int getPerdant() {
// Sortie : retourne le nombre de perdants
      return this.perdant;
   }

   public boolean meilleur( tripletEtiquetteGagnantPerdant t) { 
// Sortie : retourne true si this correspond a un etat 
//          ou on a plus de chances de gagner qu'avec t.
      return ((this.gagnant>t.getGagnant()) 
          || ((this.gagnant==t.getGagnant()) && (this.perdant<t.getPerdant()))) ; 
   }

   public void ajouter( tripletEtiquetteGagnantPerdant t) {
// Sortie : ajoute le nombre de gagnants et de perdants de t a this.
      this.gagnant+=t.getGagnant();
      this.perdant+=t.getPerdant();
   }
}
