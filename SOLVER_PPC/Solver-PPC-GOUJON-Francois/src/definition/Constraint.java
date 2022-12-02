package definition;

import java.util.List;

import definition.Variable;

public interface Constraint {

    // Retourne les variables de la contrainte
    public List<Variable> getVars();

    // Retourne vrai ssi toutes les variables de la contrainte sont instanciees et la contrainte est verifiee
    public boolean isSatisfied();

    // Une condition necessaire a la satisfaction de la contrainte :
    // retourne vrai ssi il existe encore un tuple satisfaisant la contrainte
    public boolean isNecessary();
    
    // Filtre les domaines des variables de la contrainte
    // (supprime les valeurs inconsistantes)
    // Attention : comment savoir si un appel à filter a eu un effet (une valeur supprimée d'u domaine) :
    // Renvoie true si une valeur a été supprimé et false sinon
    
    public boolean filter();
    
    // Toutes les variables sur les quelles porte la contrainte sont instanciées
    public boolean allInstantiated();

    public String toString();

}
