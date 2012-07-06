package sample;

import java.util.HashMap;
import java.util.Map;

/**
 * An attribute, which is represented by a number (id) and a value.
 * The class implements {@link Comparable} interface, and compares according to the id.
 * The hashCode() and equals() are also implemented according to ID alone.
 * @author AmitGross
 *
 */
public class Attribute implements Comparable<Attribute> {
	public final int attributeNumber;
	private final double value;
	
	private class AttributeStatistics {
		public void addValue(double v) {
			max = Math.max(max, v);
			min = Math.min(min, v);
//			count++;
//			average += (double)v/(double)count;
		}
		
		double max = 0;
		double min = 0;
//		double average = 0;
//		int count = 0;
	}
	
	private static Map<Integer,AttributeStatistics> stats = new HashMap<Integer,AttributeStatistics>();
	public static void clearAttributeStatistics(){
		stats = new HashMap<Integer,AttributeStatistics>();
	}
	
	Attribute(int attributeNumber, double value) {
		AttributeStatistics attStat = stats.get(attributeNumber);
		if (attStat == null) {
			attStat = new AttributeStatistics();
			stats.put(attributeNumber, attStat);
		}
		attStat.addValue(value);
		
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

	public double getValue() {
		AttributeStatistics attStat = stats.get(attributeNumber);
		return Math.min(1, value / (attStat.max - attStat.min) );
	}
	
}
