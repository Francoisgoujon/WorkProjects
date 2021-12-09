package labo1.Hibernate.src;

public class CorrelationfilmsId implements java.io.Serializable {
    
    private int idFilm1;
    private int idFilm2;

    public CorrelationfilmsId() {
	}

	public CorrelationfilmsId(int idFilm1, int idFilm2) {
		this.idFilm1 = idFilm1;
        this.idFilm2 = idFilm2;
	}

	public int getIdFilm1() {
		return this.idFilm1;
	}

	public void setIdFilm1(int idFilm1) {
		this.idFilm1 = idFilm1;
	}

    public int getIdFilm2() {
		return this.idFilm2;
	}

	public void setIdFilm2(int idFilm2) {
		this.idFilm2 = idFilm2;
	}

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CorrelationfilmsId))
			return false;
		CorrelationfilmsId castOther = (CorrelationfilmsId) other;

		return (this.getIdFilm1() == castOther.getIdFilm1()) && (this.getIdFilm2() == castOther.getIdFilm2());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getIdFilm1();;
		result = 37 * result + this.getIdFilm2();
		return result;
	}
}
