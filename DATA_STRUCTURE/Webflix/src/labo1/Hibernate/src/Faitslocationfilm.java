package labo1.Hibernate.src;

/**
 * Personne generated by hbm2java
 */
public class Faitslocationfilm implements java.io.Serializable {

	private int idlocationfilm;
	private Dimensionclient dimensionclient;
	private Dimensionfilm dimensionfilm;
	private Dimensiontemps dimensiontemps;

	public Faitslocationfilm() {
	}

	public Faitslocationfilm(int idlocationfilm, Dimensionclient dimensionclient, Dimensionfilm dimensionfilm, Dimensiontemps dimensiontemps) {
		this.idlocationfilm = idlocationfilm;
		this.dimensionclient = dimensionclient;
		this.dimensionfilm = dimensionfilm;
		this.dimensiontemps = dimensiontemps;
	}

	public int getidlocationfilm() {
		return this.idlocationfilm;
	}

	public void setidlocationfilm(int idlocationfilm) {
		this.idlocationfilm = idlocationfilm;
	}

	public Dimensiontemps getdimensiontemps() {
		return this.dimensiontemps;
	}

	public void setdimensiontemps(Dimensiontemps dimensiontemps) {
		this.dimensiontemps = dimensiontemps;
	}

	public Dimensionclient getdimensionclient() {
		return this.dimensionclient;
	}

	public void setdimensionclient(Dimensionclient dimensionclient) {
		this.dimensionclient = dimensionclient;
	}

	public Dimensionfilm getdimensionfilm() {
		return this.dimensionfilm;
	}

	public void setdimensionfilm(Dimensionfilm dimensionfilm) {
		this.dimensionfilm = dimensionfilm;
	}

}