package resolution;

import java.util.ArrayList;
import java.util.List;

import definition.Constraint;
import definition.ContrainteDifferent;
import definition.ContrainteInferieur;
import definition.ContrainteInferieurEgal;
import definition.ContrainteMin;
import definition.Csp;
import definition.DomainBitSet;
import definition.Variable;

public class Examen1_12_20 {

	public static void resolutionPb(int valMax) {
		DomainBitSet dom = new DomainBitSet(1,valMax);
		List<Variable> vars = new ArrayList<Variable>();
		for (int i=0;i<10;i++) {
			Variable x = new Variable();
			x.setDomain(dom.clone());
			vars.add(x);
		}
		List<Constraint> cons = new ArrayList<Constraint>();
		cons.add(new ContrainteInferieur(vars.get(0), vars.get(8)));
		cons.add(new ContrainteDifferent(vars.get(0), vars.get(3)));
		cons.add(new ContrainteInferieurEgal(vars.get(7), vars.get(0)));
		cons.add(new ContrainteInferieur(vars.get(1), vars.get(4)));
		cons.add(new ContrainteDifferent(vars.get(2), vars.get(3)));
		cons.add(new ContrainteInferieurEgal(vars.get(4), vars.get(2)));
		cons.add(new ContrainteInferieurEgal(vars.get(8), vars.get(3)));
		cons.add(new ContrainteInferieur(vars.get(6), vars.get(4)));
		cons.add(new ContrainteInferieurEgal(vars.get(4), vars.get(7)));
		cons.add(new ContrainteInferieur(vars.get(4), vars.get(9)));
		cons.add(new ContrainteInferieur(vars.get(5), vars.get(9)));
		cons.add(new ContrainteInferieurEgal(vars.get(6), vars.get(8)));
		cons.add(new ContrainteInferieur(vars.get(6), vars.get(9)));
		cons.add(new ContrainteDifferent(vars.get(7), vars.get(9)));
		cons.add(new ContrainteInferieur(vars.get(9), vars.get(8)));
		
		Csp csp = new Csp(vars,cons);
		SearchV2.nbsol = 0;
		SearchV2.nbtest = 0;
		SearchV2.ndexplo = 0;
		long debut = System.currentTimeMillis();
		SearchV2.backtrackProp2(csp);
		long temps = System.currentTimeMillis() - debut;
		System.out.println("Méthode de résolution : backtrackProp2 : nbsol = " + SearchV2.nbsol + " nbtest = " + SearchV2.nbtest + " ndexplo = " + SearchV2.ndexplo + " Temps = " + temps);
	}
	
	public static void circuitSuperieurOuEgaux(int nb) {
		DomainBitSet dom = new DomainBitSet(1,10);
		List<Variable> vars = new ArrayList<Variable>();
		for (int i=0;i<nb;i++) {
			Variable x = new Variable();
			x.setDomain(dom.clone());
			vars.add(x);
		}
		List<Constraint> cons = new ArrayList<Constraint>();
		for (int i = 0; i<nb-1 ; i++) {
			cons.add(new ContrainteInferieurEgal(vars.get(i+1), vars.get(i)));
		}
		cons.add(new ContrainteInferieurEgal(vars.get(0), vars.get(nb-1)));
		
		Csp csp = new Csp(vars, cons);
		SearchV3.nbsol = 0;
		SearchV3.nbtest = 0;
		SearchV3.ndexplo = 0;
		long debut = System.currentTimeMillis();
		SearchV3.backtrackSmallDomProp(csp);
		long temps = System.currentTimeMillis() - debut;
		System.out.println("Méthode de résolution : backtrackSmallDomProp : nbsol = " + SearchV3.nbsol + " nbtest = " + SearchV3.nbtest + " ndexplo = " + SearchV3.ndexplo + " Temps = " + temps);
		
	}
	public static void circuitSuperieurEtDifferents(int nb) {
		DomainBitSet dom = new DomainBitSet(1,10);
		List<Variable> vars = new ArrayList<Variable>();
		for (int i=0;i<nb;i++) {
			Variable x = new Variable();
			x.setDomain(dom.clone());
			vars.add(x);
		}
		List<Constraint> cons = new ArrayList<Constraint>();
		for (int i = 0; i<nb-1 ; i++) {
			cons.add(new ContrainteInferieurEgal(vars.get(i+1), vars.get(i)));
			cons.add(new ContrainteDifferent(vars.get(i+1), vars.get(i)));
		}
		cons.add(new ContrainteInferieurEgal(vars.get(0), vars.get(nb-1)));
		cons.add(new ContrainteDifferent(vars.get(0), vars.get(nb-1)));
		
		Csp csp = new Csp(vars, cons);
		SearchV3.nbsol = 0;
		SearchV3.nbtest = 0;
		SearchV3.ndexplo = 0;
		long debut = System.currentTimeMillis();
		SearchV3.backtrackSmallDomProp2(csp);
		long temps = System.currentTimeMillis() - debut;
		System.out.println("Méthode de résolution : backtrackSmallDomProp2 : nbsol = " + SearchV3.nbsol + " nbtest = " + SearchV3.nbtest + " ndexplo = " + SearchV3.ndexplo + " Temps = " + temps);
		
	}
	
	public static void circuitSuperieurStricts(int nb) {
		DomainBitSet dom = new DomainBitSet(1,10);
		List<Variable> vars = new ArrayList<Variable>();
		for (int i=0;i<nb;i++) {
			Variable x = new Variable();
			x.setDomain(dom.clone());
			vars.add(x);
		}
		List<Constraint> cons = new ArrayList<Constraint>();
		for (int i = 0; i<nb-1 ; i++) {
			cons.add(new ContrainteInferieur(vars.get(i+1), vars.get(i)));
		}
		cons.add(new ContrainteInferieur(vars.get(0), vars.get(nb-1)));
		
		Csp csp = new Csp(vars, cons);
		SearchV3.nbsol = 0;
		SearchV3.nbtest = 0;
		SearchV3.ndexplo = 0;
		long debut1 = System.currentTimeMillis();
		SearchV3.backtrackSmallRatioProp2(csp);
		long temps1 = System.currentTimeMillis() - debut1;
		System.out.println("Méthode de résolution : backtrackSmallRatioProp2 : nbsol = " + SearchV3.nbsol + " nbtest = " + SearchV3.nbtest + " ndexplo = " + SearchV3.ndexplo + " Temps = " + temps1);
	
	}
	
	public static void min() {
		List<Variable> vars = new ArrayList<Variable>();
		List<Constraint> cons = new ArrayList<Constraint>();
		DomainBitSet domx = new DomainBitSet(1,5);
		DomainBitSet domy = new DomainBitSet(6,10);
		DomainBitSet domm = new DomainBitSet(2,4);
		Variable x = new Variable(domx.clone());
		vars.add(x);
		Variable y = new Variable(domy.clone());
		vars.add(y);
		Variable m = new Variable(domm.clone());
		vars.add(m);
		cons.add(new ContrainteMin(m, x, y));
		
		Csp csp = new Csp(vars,cons);
		SearchV2.nbsol = 0;
		SearchV2.nbtest = 0;
		SearchV2.ndexplo = 0;
		long debut = System.currentTimeMillis();
		System.out.println("Affichage des solutions : ");
		SearchV2.backtrackProp2(csp);
		long temps = System.currentTimeMillis() - debut;
		System.out.println("Méthode de résolution : backtrackProp2 : nbsol = " + SearchV2.nbsol + " nbtest = " + SearchV2.nbtest + " ndexplo = " + SearchV2.ndexplo + " Temps = " + temps);
	}
		
	public static void main(String[] args) {
		
		// QUESTION 1
		
		//resolutionPb(3);   // Aucune solution pour valMax = 3
		//resolutionPb(4);   // 8 solutions pour valMax = 4
		//resolutionPb(5);   // 141 solutions pour valMax = 5
		//resolutionPb(6);   // 1200 solutions pour valMax = 6
		//resolutionPb(7);   // 6804 solutions pour valMax = 7
		//resolutionPb(8);   // 29472 solutions pour valMax = 8
		
		// QUESTION 2
		// Question 2.1
		
		//circuitSuperieurOuEgaux(10);  // Pour 10^1 : T = 5ms
		//circuitSuperieurOuEgaux(100);  // Pour 10^2 :  T= 46ms
		//circuitSuperieurOuEgaux(1000);  // Pour 10^3 :  T = 534ms
		//circuitSuperieurOuEgaux(10000);  // Pour 10^4 : T = 55393ms : PLUS GRANDE VALEUR TRAITÉE
		
		// Question 2.2
		
		//circuitSuperieurEtDifferents(10);  // Pour 10^1 : T = 39ms
		//circuitSuperieurEtDifferents(100);  // Pour 10^2 :  T = 132ms
		//circuitSuperieurEtDifferents(1000);  // Pour 10^3 :  T = 622ms
		//circuitSuperieurEtDifferents(10000);  // Pour 10^4 :  T = 5458ms  : PLUS GRANDE VALEUR TRAITÉE
		//circuitSuperieurEtDifferents(100000);  // Pour 10^5 :  T = 70478ms > 1minute : Valeur non traité
		
		// Question 2.3
		
		//circuitSuperieurStricts(10);  // Pour 10^1 : T = 1ms
		//circuitSuperieurStricts(100);  // Pour 10^2 : T = 3ms
		//circuitSuperieurStricts(1000);  // Pour 10^3 :  T = 6ms
		//circuitSuperieurStricts(10000);  // Pour 10^4 :  T = 18ms
		//circuitSuperieurStricts(100000);  // Pour 10^5 :  T = 45ms
		//circuitSuperieurStricts(1000000);  // Pour 10^6 :  T = 180ms
		//circuitSuperieurStricts(10000000);  // Pour 10^7 :  T = 1474ms : PLUS GRANDE VALEUR TRAITÉE
		//circuitSuperieurStricts(100000000);  // Pour 10^8 :  Erreur de mémoire : java.lang.OutOfMemoryError
		
		// Question 3.3
		
		min();  // 15 solutions
		
		/* Solutions : 
		    [{2}, {6}, {2}]
			[{2}, {7}, {2}]
			[{2}, {8}, {2}]
			[{2}, {9}, {2}]
			[{2}, {10}, {2}]
			[{3}, {6}, {3}]
			[{3}, {7}, {3}]
			[{3}, {8}, {3}]
			[{3}, {9}, {3}]
			[{3}, {10}, {3}]
			[{4}, {6}, {4}]
			[{4}, {7}, {4}]
			[{4}, {8}, {4}]
			[{4}, {9}, {4}]
			[{4}, {10}, {4}]
		 */
		
	}
}
