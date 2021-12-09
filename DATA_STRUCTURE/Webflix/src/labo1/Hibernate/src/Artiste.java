package labo1.Hibernate.src;

// default package
// Generated 4 juin 2021 à 20:24:20 by Hibernate Tools 5.4.7.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Artiste generated by hbm2java
 */
public class Artiste implements java.io.Serializable {

	private int noartiste;
	private String nom;
	private String prenom;
	private Date datenaissance;
	private String lieunaissance;
	private String photo;
	private String biographie;
	private Set films = new HashSet(0);
	private Set acteurfilmses = new HashSet(0);

	public Artiste() {
	}

	public Artiste(int noartiste, String nom) {
		this.noartiste = noartiste;
		this.nom = nom;
	}

	public Artiste(int noartiste, String nom, String prenom, Date datenaissance, String lieunaissance, String photo,
			String biographie, Set films, Set acteurfilmses) {
		this.noartiste = noartiste;
		this.nom = nom;
		this.prenom = prenom;
		this.datenaissance = datenaissance;
		this.lieunaissance = lieunaissance;
		this.photo = photo;
		this.biographie = biographie;
		this.films = films;
		this.acteurfilmses = acteurfilmses;
	}

	public int getNoartiste() {
		return this.noartiste;
	}

	public void setNoartiste(int noartiste) {
		this.noartiste = noartiste;
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

	public Date getDatenaissance() {
		return this.datenaissance;
	}

	public void setDatenaissance(Date datenaissance) {
		this.datenaissance = datenaissance;
	}

	public String getLieunaissance() {
		return this.lieunaissance;
	}

	public void setLieunaissance(String lieunaissance) {
		this.lieunaissance = lieunaissance;
	}

	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getBiographie() {
		return this.biographie;
	}

	public void setBiographie(String biographie) {
		this.biographie = biographie;
	}

	public Set getFilms() {
		return this.films;
	}

	public void setFilms(Set films) {
		this.films = films;
	}

	public Set getActeurfilmses() {
		return this.acteurfilmses;
	}

	public void setActeurfilmses(Set acteurfilmses) {
		this.acteurfilmses = acteurfilmses;
	}

}