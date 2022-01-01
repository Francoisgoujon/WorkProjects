package resolution;

import java.util.ArrayList;
import java.util.List;

import definition.*;

public class Test {
	
	public static void main(String[] args) {
		//Csp csp = new Csp(7, 0, 10, 7);
		//Csp csp = Sudoku.makeSudoku();
		//Csp csp = Ordonnancement.makeOrdo();
		Csp csp = new Csp(10,4,15);     		 //variable, contrainte et domaines aléatoires
		//Csp csp = Examen2013.makeCsp1(5);
		//Csp csp = Examen2013.makeCsp2(10000);  // à faire tourner avec backtrackSmallRatioProp2 ou backtrackSmallDomProp2
		//Csp csp = Examen2013.makeCsp3();
		//Csp csp = Examen2013.circuitSuperieurOuEgaux(10);
		//Csp csp = Examen2013.circuitSuperieurStricts(1000);
		
		
		
		//System.out.println(csp.getVars());
		//System.out.println(csp.getConstraints());
		
		/*
		SearchV1.nbsol = 0;
		SearchV1.nbtest = 0;
		SearchV1.ndexplo = 0;
		long debut1 = System.currentTimeMillis();
		SearchV1.generateAndTest(csp);
		long temps1 = System.currentTimeMillis() - debut1;
		System.out.println("generateAndTest : nbsol = " + SearchV1.nbsol + " nbtest = " + SearchV1.nbtest + " ndexplo = " + SearchV1.ndexplo + " Temps = " + temps1 );
		*/
		
		SearchV2.solutions = new ArrayList<List<Integer>>();
		
		
		SearchV2.nbsol = 0;
		SearchV2.nbtest = 0;
		SearchV2.ndexplo = 0;
		long debut2 = System.currentTimeMillis();
		SearchV2.backtrack1(csp);
		long temps2 = System.currentTimeMillis() - debut2;
		System.out.println("backtrack1 : nbsol = " + SearchV2.nbsol + " nbtest = " + SearchV2.nbtest + " ndexplo = " + SearchV2.ndexplo + " Temps = " + temps2);
		
		
		
		SearchV2.nbsol = 0;
		SearchV2.nbtest = 0;
		SearchV2.ndexplo = 0;
		long debut3 = System.currentTimeMillis();
		SearchV2.backtrack2(csp);
		long temps3 = System.currentTimeMillis() - debut3;
		System.out.println("backtrack2 : nbsol = " + SearchV2.nbsol + " nbtest = " + SearchV2.nbtest + " ndexplo = " + SearchV2.ndexplo + " Temps = " + temps3);
		
		
		/*
		SearchV2.nbsol = 0;
		SearchV2.nbtest = 0;
		SearchV2.ndexplo = 0;
		long debut4 = System.currentTimeMillis();
		SearchV2.backtrack3(csp);
		long temps4 = System.currentTimeMillis() - debut4;
		System.out.println("backtrack3 : nbsol = " + SearchV2.nbsol + " nbtest = " + SearchV2.nbtest + " ndexplo = " + SearchV2.ndexplo + " Temps = " + temps4);
		*/
		
		
		SearchV2.nbsol = 0;
		SearchV2.nbtest = 0;
		SearchV2.ndexplo = 0;
		long debut5 = System.currentTimeMillis();
		SearchV2.backtrackProp(csp);
		long temps5 = System.currentTimeMillis() - debut5;
		System.out.println("backtrackProp : nbsol = " + SearchV2.nbsol + " nbtest = " + SearchV2.nbtest + " ndexplo = " + SearchV2.ndexplo + " Temps = " + temps5);
		
		
		
		SearchV3.nbsol = 0;
		SearchV3.nbtest = 0;
		SearchV3.ndexplo = 0;
		long debut6 = System.currentTimeMillis();
		SearchV3.backtrackSmallDom(csp);
		long temps6 = System.currentTimeMillis() - debut6;
		System.out.println("backtrackSmallDom : nbsol = " + SearchV3.nbsol + " nbtest = " + SearchV3.nbtest + " ndexplo = " + SearchV3.ndexplo + " Temps = " + temps6);
		
		
		
		
		SearchV3.nbsol = 0;
		SearchV3.nbtest = 0;
		SearchV3.ndexplo = 0;
		long debut7 = System.currentTimeMillis();
		SearchV3.backtrackSmallRatio(csp);
		long temps7 = System.currentTimeMillis() - debut7;
		System.out.println("backtrackSmallRatio : nbsol = " + SearchV3.nbsol + " nbtest = " + SearchV3.nbtest + " ndexplo = " + SearchV3.ndexplo + " Temps = " + temps7);
		
		
		
		SearchV3.nbsol = 0;
		SearchV3.nbtest = 0;
		SearchV3.ndexplo = 0;
		long debut8 = System.currentTimeMillis();
		SearchV3.backtrackSmallDomProp(csp);
		long temps8 = System.currentTimeMillis() - debut8;
		System.out.println("backtrackSmallDomProp : nbsol = " + SearchV3.nbsol + " nbtest = " + SearchV3.nbtest + " ndexplo = " + SearchV3.ndexplo + " Temps = " + temps8);
		
		
		/*
		SearchV3.nbsol = 0;
		SearchV3.nbtest = 0;
		SearchV3.ndexplo = 0;
		long debut9 = System.currentTimeMillis();
		SearchV3.backtrackSmallRatioProp(csp);
		long temps9 = System.currentTimeMillis() - debut9;
		System.out.println("backtrackSmallRatioProp : nbsol = " + SearchV3.nbsol + " nbtest = " + SearchV3.nbtest + " ndexplo = " + SearchV3.ndexplo + " Temps = " + temps9);
		*/
		
		SearchV2.nbsol = 0;
		SearchV2.nbtest = 0;
		SearchV2.ndexplo = 0;
		long debut10 = System.currentTimeMillis();
		SearchV2.backtrackProp2(csp);
		long temps10 = System.currentTimeMillis() - debut10;
		System.out.println("backtrackProp2 : nbsol = " + SearchV2.nbsol + " nbtest = " + SearchV2.nbtest + " ndexplo = " + SearchV2.ndexplo + " Temps = " + temps10);
		
		
		SearchV3.nbsol = 0;
		SearchV3.nbtest = 0;
		SearchV3.ndexplo = 0;
		long debut11 = System.currentTimeMillis();
		SearchV3.backtrackSmallDomProp2(csp);
		long temps11 = System.currentTimeMillis() - debut11;
		System.out.println("backtrackSmallDomProp2 : nbsol = " + SearchV3.nbsol + " nbtest = " + SearchV3.nbtest + " ndexplo = " + SearchV3.ndexplo + " Temps = " + temps11);
		
		
		
		SearchV3.nbsol = 0;
		SearchV3.nbtest = 0;
		SearchV3.ndexplo = 0;
		long debut12 = System.currentTimeMillis();
		SearchV3.backtrackSmallRatioProp2(csp);
		long temps12 = System.currentTimeMillis() - debut12;
		System.out.println("backtrackSmallRatioProp2 : nbsol = " + SearchV3.nbsol + " nbtest = " + SearchV3.nbtest + " ndexplo = " + SearchV3.ndexplo + " Temps = " + temps12);
		
	}

}
