package sample;

/**
 * An attribute, which is represented by a number (id) and a value. The class
 * implements {@link Comparable} interface, and compares according to the id.
 * The hashCode() and equals() are also implemented according to ID alone.
 * 
 * @author AmitGross
 * 
 */
public class Attribute implements Comparable<Attribute> {
	public final int attributeNumber;
	public final double value;

	Attribute(int attributeNumber, double value) {
		this.attributeNumber = attributeNumber;
		this.value = value;
	}

	@Override
	public int compareTo(Attribute o) {
		return attributeNumber - o.attributeNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + attributeNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attribute other = (Attribute) obj;
		if (attributeNumber != other.attributeNumber)
			return false;
		return true;
	}
}
