package labo1.Hibernate.src;

/**
 * Film generated by hbm2java
 */
public class Cotemoyenne implements java.io.Serializable {

	private int idFilm;
	private Double moyenne;

	public Cotemoyenne() {
	}

	public Cotemoyenne(int idFilm, Double moyenne) {
		this.idFilm = idFilm;
		this.moyenne = moyenne;
	}


	public int getIdFilm() {
		return this.idFilm;
	}

	public void setIdFilm(int idFilm) {
		this.idFilm = idFilm;
	}

	public Double getMoyenne() {
		return this.moyenne;
	}

	public void setMoyenne(Double moyenne) {
		this.moyenne = moyenne;
	}

}
