package resolution;

import java.util.Hashtable;

import definition.Csp;
import definition.Domain;
import definition.Variable;

public class SearchV3 {
	
	public static int nbsol;
	public static int ndexplo;
	public static int nbtest;
	

	public static void backtrackSmallDomProp(Csp csp) {
		if (csp.allInstanciated()) {
            nbtest++;
			if (csp.hasSolution()) {
                // traitement du cas où une solution a été trouvée :
                // instanciation complète ET vérification que c'est une solution
            	//System.out.println(csp.getVars() + "ok");
            	nbsol++;
            }
        }
        else {
        	Hashtable<Variable, Domain> temp = new Hashtable<Variable, Domain>();
        	for (Variable var : csp.getVars()) {
        		temp.put(var, var.getDomain().clone());
        	}
            Variable y = csp.smallerDomVar();
            for (int val : y.getDomain().clone()) { 
                y.instantiate(val);
                if (csp.partialCheck(y) && csp.necessaryCheck()) {
                	while (csp.propagation()) {
                	}
                	backtrackSmallDomProp(csp);
                	ndexplo++;
                }
                for (Variable var : csp.getVars()) {
                	var.setDomain(temp.get(var).clone());
                }
                

            }
        }
	}
	
	public static void backtrackSmallRatioProp(Csp csp) {
		if (csp.allInstanciated()) {
            nbtest++;
			if (csp.hasSolution()) {
                // traitement du cas où une solution a été trouvée :
                // instanciation complète ET vérification que c'est une solution
            	//System.out.println(csp.getVars() + "ok");
            	nbsol++;
            }
        }
        else {
        	Hashtable<Variable, Domain> temp = new Hashtable<Variable, Domain>();
        	for (Variable var : csp.getVars()) {
        		temp.put(var, var.getDomain().clone());
        	}
            Variable y = csp.smallerDomConsRatio();
            for (int val : y.getDomain().clone()) { 
                y.instantiate(val);
                if (csp.partialCheck(y) && csp.necessaryCheck()) {
                	while (csp.propagation()) {
                	}
                	backtrackSmallRatioProp(csp);
                	ndexplo++;
                }
                for (Variable var : csp.getVars()) {
                	var.setDomain(temp.get(var).clone());
                }
            }
        }
	}
	public static void backtrackSmallDom(Csp csp) {
		if (csp.allInstanciated()) {
            nbtest++;
			if (csp.hasSolution()) {
                // traitement du cas où une solution a été trouvée :
                // instanciation complète ET vérification que c'est une solution
            	//System.out.println(csp.getVars() + "ok");
            	nbsol++;
            }
        }
        else {
            Variable y = csp.smallerDomVar();
            //System.out.println(y.getDomainSize());
            Domain temp = y.getDomain().clone();
            for (int val : y.getDomain().clone()) { 
                y.instantiate(val);
                if (csp.partialCheck(y) && csp.necessaryCheck()) {
                	backtrackSmallDom(csp);
                	ndexplo++;
                }
                y.setDomain(temp.clone());

            }
        }
	}
	
	public static void backtrackSmallRatio(Csp csp) {
		if (csp.allInstanciated()) {
            nbtest++;
			if (csp.hasSolution()) {
                // traitement du cas où une solution a été trouvée :
                // instanciation complète ET vérification que c'est une solution
            	//System.out.println(csp.getVars() + "ok");
            	nbsol++;
            }
        }
        else {
            Variable y = csp.smallerDomConsRatio();
            Domain temp = y.getDomain().clone();
            for (int val : y.getDomain().clone()) { 
                y.instantiate(val);
                if (csp.partialCheck(y) && csp.necessaryCheck()) {
                	backtrackSmallRatio(csp);
                	ndexplo++;
                }
                y.setDomain(temp.clone());

            }
        }
	}
	
	public static void backtrackSmallDomProp2(Csp csp) {
		while (csp.propagation()) {
		}
		if (!csp.isEmpty()) {
			backtrackSmallDom(csp);
		}
	}
	
	public static void backtrackSmallRatioProp2(Csp csp) {
		while (csp.propagation()) {
		}
		if (!csp.isEmpty()) {
			backtrackSmallRatio(csp);
		}
	}
}
