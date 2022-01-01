package resolution;

import java.util.ArrayList;
import java.util.List;

import definition.Constraint;
import definition.ContrainteDifferent;
import definition.ContrainteInferieur;
import definition.ContrainteInferieurEgal;
import definition.ContrainteSomme;
import definition.Csp;
import definition.DomainBitSet;
import definition.Variable;

public class Examen2013 {
	
	public static Csp makeCsp1(int valMax) {
		DomainBitSet dom = new DomainBitSet(1,valMax);
		List<Variable> vars = new ArrayList<Variable>();
		for (int i=0;i<10;i++) {
			Variable x = new Variable();
			x.setDomain(dom.clone());
			vars.add(x);
		}
		List<Constraint> cons = new ArrayList<Constraint>();
		cons.add(new ContrainteInferieurEgal(vars.get(9), vars.get(0)));
		cons.add(new ContrainteInferieurEgal(vars.get(9), vars.get(2)));
		cons.add(new ContrainteInferieur(vars.get(9), vars.get(4)));
		cons.add(new ContrainteInferieur(vars.get(0), vars.get(1)));
		cons.add(new ContrainteInferieur(vars.get(0), vars.get(4)));
		cons.add(new ContrainteInferieurEgal(vars.get(0), vars.get(7)));
		cons.add(new ContrainteInferieurEgal(vars.get(1), vars.get(6)));
		cons.add(new ContrainteDifferent(vars.get(2), vars.get(4)));
		cons.add(new ContrainteDifferent(vars.get(2), vars.get(5)));
		cons.add(new ContrainteDifferent(vars.get(4), vars.get(5)));
		cons.add(new ContrainteInferieur(vars.get(4), vars.get(6)));
		cons.add(new ContrainteInferieur(vars.get(4), vars.get(3)));
		cons.add(new ContrainteDifferent(vars.get(7), vars.get(8)));
		cons.add(new ContrainteInferieurEgal(vars.get(5), vars.get(8)));
		cons.add(new ContrainteDifferent(vars.get(6), vars.get(3)));
		cons.add(new ContrainteInferieur(vars.get(8), vars.get(6)));
		cons.add(new ContrainteInferieurEgal(vars.get(3), vars.get(8)));
		
		Csp csp = new Csp(vars,cons);
		return csp;
	}

	public static Csp makeCsp2(int nb) {
		DomainBitSet dom = new DomainBitSet(1,10);
		List<Variable> vars = new ArrayList<Variable>();
		for (int i=0;i<nb;i++) {
			Variable x = new Variable();
			x.setDomain(dom.clone());
			vars.add(x);
		}
		List<Constraint> cons = new ArrayList<Constraint>();
		for (int i = 0; i<nb-1 ; i++) {
			cons.add(new ContrainteInferieur(vars.get(i), vars.get(i+1)));
		}
		cons.add(new ContrainteInferieur(vars.get(nb-1), vars.get(0)));
		
		Csp csp = new Csp(vars, cons);
		return csp;
	}
	
	public static Csp makeCsp3() {
		List<Variable> vars = new ArrayList<Variable>();
		List<Constraint> cons = new ArrayList<Constraint>();
		DomainBitSet domx = new DomainBitSet(3,5);
		DomainBitSet domy = new DomainBitSet(2,10);
		DomainBitSet doms = new DomainBitSet(0,12);
		Variable x = new Variable(domx.clone());
		vars.add(x);
		Variable y = new Variable(domy.clone());
		vars.add(y);
		Variable s = new Variable(doms.clone());
		vars.add(s);
		cons.add(new ContrainteSomme(s, x, y));
		
		Csp csp = new Csp(vars,cons);
		return csp;
	}
	
	public static Csp circuitSuperieurOuEgaux(int nb) {
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
		return csp;
		
	}
	
	public static Csp circuitSuperieurStricts(int nb) {
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
		return csp;
	
	}
	
	public static void main(String[] args) {
		Csp csp = circuitSuperieurOuEgaux(10000);
		
		
		SearchV3.nbsol = 0;
		SearchV3.nbtest = 0;
		SearchV3.ndexplo = 0;
		long debut11 = System.currentTimeMillis();
		SearchV3.backtrackSmallDomProp(csp);
		long temps11 = System.currentTimeMillis() - debut11;
		System.out.println("backtrackSmallDomProp : nbsol = " + SearchV3.nbsol + " nbtest = " + SearchV3.nbtest + " ndexplo = " + SearchV3.ndexplo + " Temps = " + temps11);
		
		SearchV2.nbsol = 0;
		SearchV2.nbtest = 0;
		SearchV2.ndexplo = 0;
		long debut5 = System.currentTimeMillis();
		SearchV2.backtrackProp(csp);
		long temps5 = System.currentTimeMillis() - debut5;
		System.out.println("backtrackProp : nbsol = " + SearchV2.nbsol + " nbtest = " + SearchV2.nbtest + " ndexplo = " + SearchV2.ndexplo + " Temps = " + temps5);
	}
	
	
}
