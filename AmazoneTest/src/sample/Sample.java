package sample;

import java.util.Collections;
import java.util.List;

public class Sample {
	/**
	 * The list of attributes representing this sample. a non existing attribute has 0 value.
	 */
	public final List<Attribute> attributes;
	/**
	 * The classifcation of this sample.
	 */
	public final int classification;
	
	Sample(List<Attribute> attributes, int classification) {
		this.classification = classification;
		this.attributes = attributes;
		Collections.sort(attributes);
	}
}
