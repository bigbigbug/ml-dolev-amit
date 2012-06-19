package sample;

/**
 * An attribute, which is represented by a number (id) and a value.
 * The class implements {@link Comparable} interface, and compares according to the id.
 * <br>Note: The class does not override hashCode() or equals() - so do not use these!
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
		return Integer.compare(attributeNumber,o.attributeNumber);
	}
	
}
