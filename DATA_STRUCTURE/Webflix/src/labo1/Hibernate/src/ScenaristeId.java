package labo1.Hibernate.src;

// default package
// Generated 4 juin 2021 à 20:24:20 by Hibernate Tools 5.4.7.Final

/**
 * ScenaristeId generated by hbm2java
 */
public class ScenaristeId implements java.io.Serializable {

	private int idFilm;
	private String nom;
	private String prenom;

	public ScenaristeId() {
	}

	public ScenaristeId(int idFilm, String nom, String prenom) {
		this.idFilm = idFilm;
		this.nom = nom;
		this.prenom = prenom;
	}

	public int getIdFilm() {
		return this.idFilm;
	}

	public void setIdFilm(int idFilm) {
		this.idFilm = idFilm;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ScenaristeId))
			return false;
		ScenaristeId castOther = (ScenaristeId) other;

		return (this.getIdFilm() == castOther.getIdFilm()) && ((this.getNom() == castOther.getNom())
				|| (this.getNom() != null && castOther.getNom() != null && this.getNom().equals(castOther.getNom())))
				&& ((this.getPrenom() == castOther.getPrenom()) || (this.getPrenom() != null
						&& castOther.getPrenom() != null && this.getPrenom().equals(castOther.getPrenom())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getIdFilm();
		result = 37 * result + (getNom() == null ? 0 : this.getNom().hashCode());
		result = 37 * result + (getPrenom() == null ? 0 : this.getPrenom().hashCode());
		return result;
	}

}