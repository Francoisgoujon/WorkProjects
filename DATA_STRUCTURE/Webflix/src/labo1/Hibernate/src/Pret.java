package labo1.Hibernate.src;

// default package
// Generated 4 juin 2021 à 20:24:20 by Hibernate Tools 5.4.7.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Pret generated by hbm2java
 */
public class Pret implements java.io.Serializable {

	private int idPret;
	private Client client;
	private Date datepret;
	private int nocopie;
	private Pretarchive pretarchive;
	private Pretencours pretencours;
	private Set exemplaires = new HashSet(0);

	public Pret() {
	}

	public Pret(int idPret, Client client, Date datepret, int nocopie) {
		this.idPret = idPret;
		this.client = client;
		this.datepret = datepret;
		this.nocopie = nocopie;
	}

	public Pret(int idPret, Client client, Date datepret, int nocopie, Pretarchive pretarchive, Pretencours pretencours, Set exemplaires) {
		this.idPret = idPret;
		this.client = client;
		this.datepret = datepret;
		this.nocopie = nocopie;
		this.pretarchive = pretarchive;
		this.pretencours = pretencours;
		this.pretarchive = pretarchive;
		this.exemplaires = exemplaires;
	}

	public int getIdPret() {
		return this.idPret;
	}

	public void setIdPret(int idPret) {
		this.idPret = idPret;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Date getDatepret() {
		return this.datepret;
	}

	public void setDatepret(Date datepret) {
		this.datepret = datepret;
	}

	public int getNocopie() {
		return this.nocopie;
	}

	public void setNocopie(int nocopie) {
		this.nocopie = nocopie;
	}

	public Pretarchive getPretarchive() {
		return this.pretarchive;
	}

	public void setPretarchive(Pretarchive pretarchive) {
		this.pretarchive = pretarchive;
	}

	public Pretencours getPretencours() {
		return this.pretencours;
	}

	public void setPretencours(Pretencours pretencours) {
		this.pretencours = pretencours;
	}

	public Set getExemplaires() {
		return this.exemplaires;
	}

	public void setExemplaires(Set exemplaires) {
		this.exemplaires = exemplaires;
	}

}
