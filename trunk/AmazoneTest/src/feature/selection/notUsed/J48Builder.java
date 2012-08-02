package feature.selection.notUsed;

import feature.selection.ClassifierBuilder;
import weka.classifiers.trees.J48;

public class J48Builder implements ClassifierBuilder {

	@Override
	public J48 build() throws Exception {
		return new J48();
	}

}
