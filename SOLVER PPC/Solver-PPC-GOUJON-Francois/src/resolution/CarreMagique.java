package resolution;

import java.util.ArrayList;
import java.util.List;


import definition.Constraint;
import definition.ContrainteDifferent;
import definition.ContrainteSommeN;
import definition.Csp;
import definition.DomainBitSet;
import definition.Variable;

public class CarreMagique {

	public static Csp makeCsp(int n) {
		DomainBitSet dom = new DomainBitSet(1,n*n);
		Variable[][] cases = new Variable[n][n];
		
		Variable sum = new Variable(n*(n*n +1)/2);
		
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				cases[i][j] = new Variable(dom.clone());
			}
		}
		
		List<Constraint> cons = new ArrayList<Constraint>();
		
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				for (int k=0; k<n; k++) {
					for (int l=0;l<n; l++) {
						if ( (i!=k) || (j!=l)) {
							cons.add(new ContrainteDifferent(cases[i][j], cases[k][l]));
						}
					}
				}
			}
		}
		
		for (int i=0 ;i<n;i++) {
			cons.add(new ContrainteSommeN(sum, cases[i]));
		}
			
		for (int j=0;j<n;j++) {
			Variable[] colonne = new Variable[n];
			for (int i=0;i<n;i++) {
				colonne[i] = cases[i][j];
			}
			cons.add(new ContrainteSommeN(sum, colonne));
		}
		Variable[] diagoMontante = new Variable[n];
		Variable[] diagoDescendante = new Variable[n];
		
		for (int i=0; i<n; i++) {
			diagoDescendante[i] = cases[i][i];
			diagoMontante[i] = cases[n-i-1][i];
		}
		cons.add(new ContrainteSommeN(sum, diagoDescendante));
		cons.add(new ContrainteSommeN(sum, diagoMontante));
		
		List<Variable> vars = new ArrayList<Variable>();
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				vars.add(cases[i][j]);
			}
		}
		Csp csp = new Csp(vars, cons);
		return csp;
		
	}
	
	public static void main(String[] args) {
		Csp csp = makeCsp(5);
		SearchV2.solutions = new ArrayList<List<Integer>>();
		SearchV2.backtrackProp2(csp);
		System.out.println(SearchV2.solutions);
		
	}
}
