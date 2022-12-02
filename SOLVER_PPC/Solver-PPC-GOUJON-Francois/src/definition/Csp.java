package definition; 

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Csp {

    private List<Variable> vars; // l'ensemble des variables du CSP. Note: les domaines sont connus au travers des variables
    private List<Constraint> cons; // l'ensemble des contraintes du CSP
    
    public Csp(List<Variable> vars, List<Constraint> cons) {
    	this.vars = vars;
    	this.cons = cons;
    }
    
    
    // crée un csp de nbVar variables, toutes de domaines identiques [domMin,domMax], et nbCons constraintes aléatoires
    public Csp(int nbVar, int domMin, int domMax, int nbCons) {
		DomainBitSet dom = new DomainBitSet(domMin,domMax);
		List<Variable> vars = new ArrayList<Variable>();
		for (int i=0; i < nbVar; i++) {
			vars.add(new Variable(dom.clone()));
		}
		List<Constraint> cons = new ArrayList<Constraint>();
		for (int i=0; i < nbCons; i++) {
			int signe = new Random().nextInt(3);
			int v1 = new Random().nextInt(nbVar);
			int v2 = new Random().nextInt(nbVar);
			if (signe == 0) {
				cons.add(new ContrainteDifferent(vars.get(v1), vars.get(v2)));
			}
			if (signe == 1) {
				cons.add(new ContrainteInferieur(vars.get(v1), vars.get(v2)));
			}
			if (signe == 2) {
				cons.add(new ContrainteInferieurEgal(vars.get(v1), vars.get(v2)));
			}
		}
		this.vars = vars;
		this.cons = cons;
	}
    
    // crée un csp de nbVar variables, toutes de domaines aléatoires compris dans [0,maxDom], et nbCons constraintes aléatoires
    public Csp(int nbVar, int nbCons, int maxDom) {
		List<Variable> vars = new ArrayList<Variable>();
		for (int i=0; i < nbVar; i++) {
			int n1 = new Random().nextInt(maxDom);
			int n2 = new Random().nextInt(maxDom);
			int domMin = 0;
			int domMax = 0;
			if (n1 < n2) {
				domMin = n1;
				domMax = n2;
			} else {
				domMin = n2;
				domMax = n1;
			}
			DomainBitSet dom = new DomainBitSet(domMin,domMax);
			vars.add(new Variable(dom.clone()));
		}
		List<Constraint> cons = new ArrayList<Constraint>();
		for (int i=0; i < nbCons; i++) {
			int signe = new Random().nextInt(3);
			int v1 = new Random().nextInt(nbVar);
			int v2 = new Random().nextInt(nbVar);
			while (v1==v2) {
				v2 = new Random().nextInt(nbVar);
			}
			if (signe == 0) {
				cons.add(new ContrainteDifferent(vars.get(v1), vars.get(v2)));
			}
			if (signe == 1) {
				cons.add(new ContrainteInferieur(vars.get(v1), vars.get(v2)));
			}
			if (signe == 2) {
				cons.add(new ContrainteInferieurEgal(vars.get(v1), vars.get(v2)));
			}
		}
		this.vars = vars;
		this.cons = cons;
	}
    

    public List<Variable> getVars() {
        return this.vars;
    }
    
    public List<Constraint> getConstraints() {
    	return this.cons;
    }
    public List<Integer> getVarsInt() {
    	List<Integer> res = new ArrayList<Integer>();
    	if (this.allInstanciated()) {
    		for (Variable var : this.getVars()) {
    			res.add(var.getValue());
    		}
    	}
    	return res;
    }
    
    // Renvoie la liste des contraintes dans lesquelles var apparait
    public List<Constraint> getConstraints(Variable var) {
    	List<Constraint> list = new ArrayList<Constraint>();
    	for (Constraint cons : this.getConstraints() ) {
    		if (cons.getVars().contains(var)) {
    			list.add(cons);
    		}
    	}
    	return list;
    }
    

    // retourne la premiere variable non instanciee du csp
    public Variable nextVarToInstantiate() {
        Variable v = null;
        boolean trouve = false;
        int i = 0;
        while (i< vars.size() && !trouve ) {
        	if (vars.get(i).isInstantiated()) {
        		i = i+1;
        	}
        	else {
        		v = this.vars.get(i);
        		trouve = true;
        	}
        	
        }
        return v;
    }
    
    
    // retourne la variable non instanciée qui a le domaine de plus petite taille
    public Variable smallerDomVar() {
    	Variable v = null;
        int minsize = Integer.MAX_VALUE;
        int i = 0;
        while (i< vars.size()) {
        	if (!vars.get(i).isInstantiated()) {
        		if (this.vars.get(i).getDomainSize() < minsize) {
        			v = this.vars.get(i);
        			minsize = v.getDomainSize();		
        		}
        	}
        	i += 1;
        }
    	return v;
    }
    
    // retourne la variable non instanciée qui a le plus petit ratio entre la taille de son domaine et les contraintes dans lequelles elle apparait
    public Variable smallerDomConsRatio() {
    	Variable v = null;
    	double minratio = Double.MAX_VALUE;
    	for (Variable var : this.getVars()) {
    		if (!var.isInstantiated()) {
	    		double domsize = var.getDomainSize();
	    		double conssize = this.getConstraints(var).size();
	    		double ratio = 0;
	    		if (conssize ==0) {
	    			ratio = 1000;
	    		} else {
	    			ratio = domsize/conssize;
	    		}
	    		if (ratio < minratio) {
	    			minratio = var.getDomainSize();
	    			v = var;
	    		}	
    		}	
    	}
    	return v;
    }

    // retourne vrai ssi toutes les variables sont instanciées
    public boolean allInstanciated() {
         int n = this.getVars().size();
         for (int i=0 ; i < n ; i++ ) {
        	 if (!vars.get(i).isInstantiated()) {
        		 return false;
        	 }
         }
         return true;
    }

    // retourne vrai ssi le CSP possède (au moins) une solution : 
    // l'ensemble des contraintes du CSP est vérifié
    // ATTENTION : ce n'est pas la seule condition
    public boolean hasSolution() {
    	int n = this.getConstraints().size();
    	for (int i=0 ; i<n ; i++) {
    		if (!this.getConstraints().get(i).isSatisfied()) {
    			return false;
    		}
    	}
        return true;
    }
    
    // Verifie la validité des contraintes où les varibles sont déjà instanciees
    public boolean partialCheck() {
    	boolean b = true;
    	for (Constraint cons : this.getConstraints()) {
    		if (cons.allInstantiated()) {
    			if (!cons.isSatisfied()) {
    				b = false;
    				break;
    			}
    		}
    	}
    	return b;
    }
    
    // Verifie la validité des contraintes contenant y où les varibles sont déjà instanciees
    public boolean partialCheck(Variable y) {
    	boolean b = true;
    	for (Constraint cons : this.getConstraints(y)) {
    		if (cons.allInstantiated()) {
    			if (!cons.isSatisfied()) {
    				b = false;
    				break;
    			}
    		}
    	}
    	return b;
    }
    
    // Verifie qu'il existe encore des tuples de valeurs permettant de validé les contraintes
    public boolean necessaryCheck() {
    	boolean b = true;
    	for (Constraint cons : this.getConstraints()) {
    		if (!cons.allInstantiated()) {
    			if (!cons.isNecessary()) {
    				b = false;
    				break;
    			}
    		}
    	}
    	return b;
    }
    public boolean propagation(Variable y) {
    	boolean b = false;
    	for (Constraint cons : this.getConstraints() ) {
    		if (cons.getVars().contains(y)) {
    			boolean c = cons.filter();
    			if (c) {
    				b = true;
    			}
    		}
    	}
    	return b;
    }
    
    public boolean propagation() {
    	boolean b = false;
    	for (Constraint cons : this.getConstraints() ) {
    			boolean c = cons.filter();
    			if (c) {
    				b = true;
    			}
    	}
    	return b;
    }
    //Renvoie true si une des variables a un domaine vide
    public boolean isEmpty() {
    	boolean b = false;
    	for (Variable var : this.getVars()) {
    		if (var.isEmpty()) {
    			b = true;
    			break;
    		}
    	}
    	return b;
    }
 
}
