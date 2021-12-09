package labo1.UI.main;

import java.awt.Dimension;

import labo1.methodes.HibernateQueries;

public class mainUI {
    public static void main(String args[]) {
		
		HibernateQueries session = new HibernateQueries();
		UIbuild f = new UIbuild(session);
		
		f.setLocation(80,50);
		f.setPreferredSize(new Dimension(1300, 850));
		f.pack();
		f.setVisible(true);
		
	}
}
