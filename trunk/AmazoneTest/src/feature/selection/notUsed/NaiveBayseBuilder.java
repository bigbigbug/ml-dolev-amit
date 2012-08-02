package feature.selection.notUsed;

import feature.selection.ClassifierBuilder;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;

public class NaiveBayseBuilder implements ClassifierBuilder {

	@Override
	public Classifier build() throws Exception {
		return new NaiveBayesMultinomial();
	}

}
