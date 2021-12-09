package labo1.Hibernate.src;

// default package
// Generated 4 juin 2021 à 20:24:20 by Hibernate Tools 5.4.7.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Personne generated by hbm2java
 */
public class Personne implements java.io.Serializable {

	private int idPersonne;
	private String nom;
	private String prenom;
	private String courriel;
	private String notelephone;
	private String mdp;
	private Date datenaissance;
	private Integer numerocivique;
	private String adresse;
	private String ville;
	private String province;
	private String codepostal;
	private Client client;
	private Set employes = new HashSet(0);

	public Personne() {
	}

	public Personne(int idPersonne, String nom, String prenom, String courriel, String notelephone, String mdp,
			Date datenaissance, String adresse, String ville, String province, String codepostal) {
		this.idPersonne = idPersonne;
		this.nom = nom;
		this.prenom = prenom;
		this.courriel = courriel;
		this.notelephone = notelephone;
		this.mdp = mdp;
		this.datenaissance = datenaissance;
		this.adresse = adresse;
		this.ville = ville;
		this.province = province;
		this.codepostal = codepostal;
	}

	public Personne(int idPersonne, String nom, String prenom, String courriel, String notelephone, String mdp,
			Date datenaissance, Integer numerocivique, String adresse, String ville, String province, String codepostal,
			Client client, Set employes) {
		this.idPersonne = idPersonne;
		this.nom = nom;
		this.prenom = prenom;
		this.courriel = courriel;
		this.notelephone = notelephone;
		this.mdp = mdp;
		this.datenaissance = datenaissance;
		this.numerocivique = numerocivique;
		this.adresse = adresse;
		this.ville = ville;
		this.province = province;
		this.codepostal = codepostal;
		this.client = client;
		this.employes = employes;
	}

	public int getIdPersonne() {
		return this.idPersonne;
	}

	public void setIdPersonne(int idPersonne) {
		this.idPersonne = idPersonne;
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

	public String getCourriel() {
		return this.courriel;
	}

	public void setCourriel(String courriel) {
		this.courriel = courriel;
	}

	public String getNotelephone() {
		return this.notelephone;
	}

	public void setNotelephone(String notelephone) {
		this.notelephone = notelephone;
	}

	public String getMdp() {
		return this.mdp;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}

	public Date getDatenaissance() {
		return this.datenaissance;
	}

	public void setDatenaissance(Date datenaissance) {
		this.datenaissance = datenaissance;
	}

	public Integer getNumerocivique() {
		return this.numerocivique;
	}

	public void setNumerocivique(Integer numerocivique) {
		this.numerocivique = numerocivique;
	}

	public String getAdresse() {
		return this.adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getVille() {
		return this.ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCodepostal() {
		return this.codepostal;
	}

	public void setCodepostal(String codepostal) {
		this.codepostal = codepostal;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Set getEmployes() {
		return this.employes;
	}

	public void setEmployes(Set employes) {
		this.employes = employes;
	}

}
