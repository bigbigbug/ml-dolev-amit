package sample;

import java.util.List;

public class Sample {
	public final List<Attribute> attributes;
	public final int classification;
	public Sample(List<Attribute> attributes, int classification) {
		this.classification = classification;
		this.attributes = attributes;
	}
}
