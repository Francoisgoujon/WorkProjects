package resolution;

import java.util.ArrayList;
import java.util.List;

import definition.*;

public class Ordonnancement {
	
	public static Csp makeOrdo() {
		
		List<Variable> vars = new ArrayList<Variable>();
		List<Constraint> cons = new ArrayList<Constraint>();
		
		DomainBitSet dom0 = new DomainBitSet(1,10);
		DomainBitSet dom1 = new DomainBitSet(3,7);
		DomainBitSet dom2 = new DomainBitSet(5,15);
		DomainBitSet dom3 = new DomainBitSet(4,10);
		DomainBitSet dom4 = new DomainBitSet(6,8);
		DomainBitSet dom5 = new DomainBitSet(6,12);
		
		Variable t0 = new Variable(dom0);
		vars.add(t0);
		Variable t1 = new Variable(dom1);
		vars.add(t1);
		Variable t2 = new Variable(dom2);
		vars.add(t2);
		Variable t3 = new Variable(dom3);
		vars.add(t3);
		Variable t4 = new Variable(dom4);
		vars.add(t4);
		Variable t5 = new Variable(dom5);
		vars.add(t5);
		

		int[][] adja = new int[6][6];
		
		for (int i=0;i<6;i++) {
			for (int j=0;j<6;j++) {
				adja[i][j] = 0;
			}
		}
		adja[0][1] = 1;
		adja[0][2] = 1;
		adja[1][3] = 1;
		adja[1][4] = 1;
		adja[2][3] = 1;
		adja[2][4] = 1;
		adja[3][5] = 1;
		adja[4][5] = 1;
		
		for (int i=0;i<6;i++) {
			for (int j=0;j<6;j++) {
				if (adja[i][j] == 1) {
					cons.add(new ContrainteInferieurEgal(vars.get(i), vars.get(j)));
				}
			}
		}
		Csp csp = new Csp(vars,cons);
		return csp;
	}
	
	public static void main(String[] args) {
		Csp csp = makeOrdo();
		
		SearchV2.solutions = new ArrayList<List<Integer>>();
		//System.out.println(SearchV2.solutions);
		SearchV2.backtrack1(csp);
		System.out.println(SearchV2.solutions);
		
		
		List<Integer> bestSol = SearchV2.solutions.get(0);
		int minTime = SearchV2.solutions.get(0).get(SearchV2.solutions.get(0).size()-1);
		for (List<Integer> sol : SearchV2.solutions) {
			int time = sol.get(sol.size()-1);
			if (time < minTime) {
				bestSol = sol;
				minTime = time;
			}
		}
		System.out.println(minTime);
		System.out.println(bestSol);
		
	}
	
}
