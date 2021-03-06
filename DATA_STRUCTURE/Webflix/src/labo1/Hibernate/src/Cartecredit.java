package labo1.Hibernate.src;

// default package
// Generated 4 juin 2021 à 20:24:20 by Hibernate Tools 5.4.7.Final

import java.util.Date;

/**
 * Cartecredit generated by hbm2java
 */
public class Cartecredit implements java.io.Serializable {

	private int idPersonne;
	private Client client;
	private String typecarte;
	private String numerocarte;
	private Date dateexpiration;
	private Integer cvv;

	public Cartecredit() {
	}

	public Cartecredit(Client client, String typecarte, String numerocarte, Date dateexpiration) {
		this.client = client;
		this.typecarte = typecarte;
		this.numerocarte = numerocarte;
		this.dateexpiration = dateexpiration;
	}

	public Cartecredit(Client client, String typecarte, String numerocarte, Date dateexpiration, Integer cvv) {
		this.client = client;
		this.typecarte = typecarte;
		this.numerocarte = numerocarte;
		this.dateexpiration = dateexpiration;
		this.cvv = cvv;
	}

	public int getIdPersonne() {
		return this.idPersonne;
	}

	public void setIdPersonne(int idPersonne) {
		this.idPersonne = idPersonne;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getTypecarte() {
		return this.typecarte;
	}

	public void setTypecarte(String typecarte) {
		this.typecarte = typecarte;
	}

	public String getNumerocarte() {
		return this.numerocarte;
	}

	public void setNumerocarte(String numerocarte) {
		this.numerocarte = numerocarte;
	}

	public Date getDateexpiration() {
		return this.dateexpiration;
	}

	public void setDateexpiration(Date dateexpiration) {
		this.dateexpiration = dateexpiration;
	}

	public Integer getCvv() {
		return this.cvv;
	}

	public void setCvv(Integer cvv) {
		this.cvv = cvv;
	}

}
