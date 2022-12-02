package definition;

public class Variable {
    private Domain dom; // le domaine associe

    public Variable(Domain dom) {
    	this.dom = dom;
    }
    public Variable() {
    	this.dom = null;
    }
    public Variable(int n) {
    	DomainBitSet dom = new DomainBitSet(n,n);
    	this.dom = dom;
    }
    
    public Domain getDomain() {
        return this.dom;
    }
    public void setDomain(Domain dom) {
    	this.dom = dom;
    }
    
    // retourne vrai ssi la variable est instanciee
    public boolean isInstantiated() {
        if (this.getDomainSize() == 1 ) {
        	return true;
        }
        return false;
    }

    // retourne vrai ssi le domaine de la variable contient la valeur v
    public boolean canBeInstantiatedTo(int v) {
        if (this.getDomain().contains(v)) {
        	return true;
        }
        else {
        	return false;
        }
    }

    // retourne le nombre de valeurs dans le domaine de la variable
    public int getDomainSize() {
        return this.getDomain().size();
    }

    // supprime la valeur v du domaine de la variable
    public void remValue(int v) {
    	this.getDomain().remove(v);
    }

    // supprime toutes les valeurs entre min (inclus) et max (inclus)
    public void remValues(int min, int max) {
    	this.getDomain().remove(min, max);
    }

    // vide le domaine : supprime toutes ses valeurs
    public void remAllValues() {
    	this.getDomain().removeAll();
    }

    // instantie la variable a la valeur v
    public void instantiate(int v) {
    	this.getDomain().instantiate(v);
    	
    }

    // retourne la plus petite valeur du domaine
    public int getInf() {
        return this.getDomain().firstValue();
    }

    // retourne la plus grande valeur du domaine
    public int getSup() {
        return this.getDomain().lastValue();
    }

    // retourne la valeur affectee a la variable ssi la variable est effectivement instanciee, sinon -1
    public int getValue() {
        if (this.isInstantiated()) {
        	return this.getDomain().firstValue();
        }
        else {
        	return -1;
        }
    }

    public boolean isEmpty() {
        return (this.getDomain().size() == 0);
    }
    public String toString() {
    	return  dom.toString();
    }
}