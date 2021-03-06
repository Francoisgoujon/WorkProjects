package labo1.Hibernate.src;

// default package
// Generated 4 juin 2021 à 20:24:20 by Hibernate Tools 5.4.7.Final

import java.util.HashSet;
import java.util.Set;

/**
 * Genre generated by hbm2java
 */
public class Genre implements java.io.Serializable {

	private String nomGenre;
	private String descriptif;
	private Set films = new HashSet(0);

	public Genre() {
	}

	public Genre(String nomGenre) {
		this.nomGenre = nomGenre;
	}

	public Genre(String nomGenre, String descriptif, Set films) {
		this.nomGenre = nomGenre;
		this.descriptif = descriptif;
		this.films = films;
	}

	public String getNomGenre() {
		return this.nomGenre;
	}

	public void setNomGenre(String nomGenre) {
		this.nomGenre = nomGenre;
	}

	public String getDescriptif() {
		return this.descriptif;
	}

	public void setDescriptif(String descriptif) {
		this.descriptif = descriptif;
	}

	public Set getFilms() {
		return this.films;
	}

	public void setFilms(Set films) {
		this.films = films;
	}

}
