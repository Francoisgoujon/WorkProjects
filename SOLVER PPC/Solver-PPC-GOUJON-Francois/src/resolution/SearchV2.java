package resolution;

import java.util.Hashtable;
import java.util.List;

import definition.Csp;
import definition.Domain;
import definition.Variable;

public class SearchV2 {
	
	public static int nbsol;
	public static int ndexplo;
	public static int nbtest;
	public static List<List<Integer>> solutions;
	
	public static void backtrack1(Csp csp) {
		if (csp.allInstanciated()) {
            nbtest++;
			if (csp.hasSolution()) {
                // traitement du cas où une solution a été trouvée :
                // instanciation complète ET vérification que c'est une solution
            	//System.out.println(csp.getVars() + "ok");
            	nbsol++;
            	solutions.add(csp.getVarsInt());
            	//System.out.println(solutions.get(nbsol -1));
            }
        }
        else {
            Variable y = csp.nextVarToInstantiate();
            Domain temp = y.getDomain().clone();
            for (int val : y.getDomain().clone()) { 
                y.instantiate(val);
                if (csp.partialCheck(y)) {
                	backtrack1(csp);
                	ndexplo++;
                }
                y.setDomain(temp.clone());

            }
        }
	}
	
	
	public static void backtrack2(Csp csp) {
		if (csp.allInstanciated()) {
            nbtest++;
			if (csp.hasSolution()) {
                // traitement du cas où une solution a été trouvée :
                // instanciation complète ET vérification que c'est une solution
            	//System.out.println(csp.getVars() + "ok");
				//System.out.println(csp.getVars());
            	nbsol++;
            	solutions.add(csp.getVarsInt());
            }
        }
        else {
            Variable y = csp.nextVarToInstantiate();
            Domain temp = y.getDomain().clone();
            for (int val : y.getDomain().clone()) { 
                y.instantiate(val);
                if (csp.partialCheck(y) && csp.necessaryCheck()) {
                	backtrack2(csp);
                	ndexplo++;
                }
                y.setDomain(temp.clone());

            }
        }
	}
	public static void backtrack3(Csp csp) {
		if (csp.necessaryCheck() && csp.partialCheck()) {
			nbtest++;
			if (csp.allInstanciated()) {
				//System.out.println(csp.getVars() + "ok");
				nbsol++;
			}
			else {
				Variable y = csp.nextVarToInstantiate();
	            Domain temp = y.getDomain().clone();
	            for (int val : y.getDomain().clone()) { 
	                y.instantiate(val);
		            backtrack3(csp);
		            ndexplo++;
	                y.setDomain(temp.clone());
	            }
			}
		}
	}
	public static void backtrackProp(Csp csp) {
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
            Variable y = csp.nextVarToInstantiate();
            for (int val : y.getDomain().clone()) { 
                y.instantiate(val);
                if (csp.partialCheck(y) && csp.necessaryCheck()) {
                	while (csp.propagation()) {
                	}
                	backtrackProp(csp);
                	ndexplo++;
                }
                for (Variable var : csp.getVars()) {
                	var.setDomain(temp.get(var).clone());
                }
                

            }
        }
	}
	
	
	public static void backtrackProp2(Csp csp) {
		while (csp.propagation()) {
		}
		if (!csp.isEmpty()) {
			backtrack2(csp);
		}
	}

}
