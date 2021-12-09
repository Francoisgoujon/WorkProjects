package labo1.methodes;

public class ApiAnalyse {
    public static void main(String[] args) {
       
        HibernateQueries session = new HibernateQueries();

        System.out.println("\n \n \n");

        // Groupe age = 20 ==> Tous
        session.analyse(20, "Tous", "Tous", "Tous");
        System.out.println("\n \n");
        session.analyse(13, "Tous", "Tous", "Tous");
        System.out.println("\n \n");
        session.analyse(12, "Tous", "Tous", "Tous");
        System.out.println("\n \n");
        session.analyse(20, "BC", "Tous", "Tous");
        System.out.println("\n \n");
        session.analyse(20, "ON", "Tous", "Tous");
        System.out.println("\n \n");
        session.analyse(20, "QC", "Tous", "Tous");
        System.out.println("\n \n");
        session.analyse(13, "QC", "Tous", "Tous");
        System.out.println("\n \n");
        session.analyse(13, "BC", "Tous", "Tous");
        System.out.println("\n \n");
        session.analyse(5, "Tous", "Tous", "Tous");

        System.out.println("\n \n \n");

        session.closeSessionFactory();
    }
}


// 4 paramètres : groupe age, province, jour (en anglais), mois (en anglais)
/* groupe age : 
0 : 0 - 5 ; 
1 : 5 - 10 ;
2 : 10 - 15 ; 
3 : 15 - 20 ;
4 : 20 - 25 ;
5 : 25 - 30 ;
6 : 30 - 35 ;
7 : 35 - 40 ;
8 : 40 - 45 ; 
9 : 45 - 50 ;
10 : 50 - 55 ;
11 : 55 - 60 ; 
12 : 60 - 65 ; 
13 : 65 - 70 ;
14 : 70 - 75 ;
15 : 75 - 80 ;
16 : 80 - 85 ; 
17 : 85 - 90 ;
18 : 90 - 95 ; 
19 : 95 - 100 ;
20 : Tous
*/