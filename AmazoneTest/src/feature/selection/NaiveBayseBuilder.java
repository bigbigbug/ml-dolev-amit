package feature.selection;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;

public class NaiveBayseBuilder implements ClassifierBuilder {

	@Override
	public Classifier build() throws Exception {
		return new NaiveBayesMultinomial();
	}

}
