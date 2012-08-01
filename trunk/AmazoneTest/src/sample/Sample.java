package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Sample {
	/**
	 * The list of attributes representing this sample. a non existing attribute has 0 value.
	 */
	public final List<Attribute> attributes;
	/**
	 * The classifcation of this sample.
	 */
	public final int classification;
	
	public Sample(List<Attribute> attributes, int classification) {
		this.classification = classification;
		this.attributes = attributes;
		Collections.sort(this.attributes);
	}
	
	public Sample(Sample original, Set<Attribute> useOnlyThisAttributes) { 
		attributes = new ArrayList<Attribute>();
		classification = original.classification;
		for (Attribute att : original.attributes) {
			if (useOnlyThisAttributes.contains(att)) 
				attributes.add(att);
		}
		Collections.sort(attributes);
	}
}
