package resolution;

import java.util.ArrayList;
import java.util.List;

import definition.*;

public class Sudoku {
	
	public static Csp makeSudoku() {
		DomainBitSet dom = new DomainBitSet(1,9);
		Variable[][] cases = new Variable[9][9];
		
		Variable un = new Variable(1);
		Variable deux = new Variable(2);
		Variable trois = new Variable(3);
		Variable quatre = new Variable(4);
		Variable cinq = new Variable(5);
		Variable six = new Variable(6);
		Variable sept = new Variable(7);
		Variable huit = new Variable(8);
		//Variable neuf = new Variable(9);

		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				cases[i][j] = new Variable(dom.clone());
			}
		}
		
		
		cases[0][2] = sept;
		cases[0][3] = cinq;
		cases[0][6] = trois;
		cases[1][1] = quatre;
		cases[1][4] = deux;
		cases[1][6] = un;
		cases[2][0] = un;
		cases[2][4] = sept;
		cases[2][7] = cinq;
		cases[3][2] = trois;
		cases[3][3] = un;
		cases[3][4] = quatre;
		cases[3][6] = deux;
		cases[3][8] = six;
		cases[4][0] = quatre;
		cases[4][4] = six;
		cases[4][5] = deux;
		cases[4][6] = sept;
		cases[5][1] = six;
		cases[5][2] = cinq;
		cases[5][4] = trois;
		cases[5][8] = huit;
		cases[6][1] = sept;
		cases[6][2] = un;
		cases[6][6] = six;
		cases[7][0] = huit;
		cases[8][1] = cinq;
		cases[8][3] = sept;
		cases[8][7] = quatre;
		cases[8][8] = un;
		
		
		List<Constraint> cons = new ArrayList<Constraint>();
		
		for (int i=0 ;i<9;i++) {   //  9 chiffres différents par ligne
			for (int j=0;j<9;j++) {
				for (int k=0;k<9;k++) {
					if (k!=j) {
						cons.add(new ContrainteDifferent(cases[i][j], cases[i][k]));
					}
				}
			}
		}
		
		for (int j=0;j<9;j++) {    // 9 chiffres différents par colonne
			for (int i=0;i<9;i++) {
				for (int k=0;k<9;k++) {
					if (k!=i) {
						cons.add(new ContrainteDifferent(cases[i][j], cases[k][j]));
					}
				}
			}
			
		}
		Variable[][] blocs = new Variable[9][9];
		
		for (int i=0;i<3;i++) {     
			for (int j=0;j<3;j++) {
				for (int k=0;k<3;k++) {
					for (int l=0;l<3;l++) {
						blocs[3*i + j][3*k + l] = cases[3*i + k][3*j + l];
					}
				}
			}
		}
		for (int i=0;i<9;i++) {     // 9chiffres différents par bloc
			for (int j=0;j<9;j++) {
				for (int k=0;k<9;k++) {
					if (k!=j) {
						cons.add(new ContrainteDifferent(blocs[i][j], blocs[i][k]));
					}
				}
			}
		}
		
		List<Variable> vars = new ArrayList<Variable>();
		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				vars.add(cases[i][j]);
			}
		}
		Csp csp = new Csp(vars, cons);
		return csp;
	}

	

	public static void main(String[] args) {
		Csp csp1 = makeSudoku();
		SearchV2.solutions = new ArrayList<List<Integer>>();
		SearchV2.backtrackProp2(csp1);
		System.out.println(SearchV2.solutions);
		
	}
		
}
