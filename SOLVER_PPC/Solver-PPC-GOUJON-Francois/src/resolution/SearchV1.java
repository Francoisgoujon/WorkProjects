package resolution;

import java.util.ArrayList;
import java.util.List;

import definition.Constraint;
import definition.ContrainteDifferent;
import definition.ContrainteInferieur;
import definition.Csp;
import definition.Domain;
import definition.DomainBitSet;
import definition.Variable;

public class SearchV1 {
    
	public static int nbsol;
	public static int ndexplo;
	public static int nbtest;
    //---------------------------------------------------------------------------------------------------
    // bruteForceSearch : on génère toutes les instanciations possibles :
    // aucune contrainte : toute instanciation complète est une solution
    //---------------------------------------------------------------------------------------------------

    public static void bruteForceSearch(Csp csp) {
    	//System.out.println("test" + csp.getVars());
        if (csp.allInstanciated()) {
            // traitement du cas où une instanciation est complète
        	System.out.println(csp.getVars() + "fini");
        	nbsol++;
        }
        else {
            Variable y = csp.nextVarToInstantiate();
            //System.out.println("1 " + y.hashCode() + y.getDomain());
            Domain temp = y.getDomain().clone();
            for (int val : y.getDomain().clone()) {
            	//System.out.println("2 " + y.hashCode() + y.getDomain() + val);
                y.instantiate(val);
                bruteForceSearch(csp);
                y.setDomain(temp.clone());
                //System.out.println("3 " + y.hashCode() + y.getDomain()+ val);
            }
        }
    }

    //---------------------------------------------------------------------------------------------------
    // generateAndTest : on ajoute un test pour vérifier si une instanciation complète est une solution
    // Note : si le csp n'a aucune contrainte, c'est le même comportement que bruteForceSearch
    //---------------------------------------------------------------------------------------------------

    public static void generateAndTest(Csp csp) {
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
            Variable y = csp.nextVarToInstantiate();
            Domain temp = y.getDomain().clone();
            for (int val : y.getDomain().clone()) { 
                y.instantiate(val);
                generateAndTest(csp);
                ndexplo++;
                y.setDomain(temp.clone());

            }
        }
    }
    
    public static void main(String[] args) {
		DomainBitSet dom = new DomainBitSet(0,2);
		Variable x1 = new Variable();
		x1.setDomain(dom.clone());
		Variable x2 = new Variable();
		x2.setDomain(dom.clone());
		Variable x3 = new Variable();
		x3.setDomain(dom.clone());
		List<Variable> vars = new ArrayList<Variable>();
		vars.add(x1);
		vars.add(x2);
		vars.add(x3);
		List<Constraint> cons = new ArrayList<Constraint>();
		Constraint c1 = new ContrainteInferieur(x1, x2);
		Constraint c2 = new ContrainteDifferent(x1, x3);
		cons.add(c1);
		cons.add(c2);
		Csp csp1 = new Csp(vars, cons);
		//System.out.println("avant");
		nbsol =0;
		generateAndTest(csp1);
		System.out.println(nbsol);
		
	}

}
