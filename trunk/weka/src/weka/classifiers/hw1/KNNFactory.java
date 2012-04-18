package weka.classifiers.hw1;

import java.io.Serializable;

import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;

public class KNNFactory implements ClassifierFactory, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4409818771851476867L;

	@Override
	//TODO: generating nearest neighbor with cross validation. A change might be needed
	public Classifier getClassifier() {
		return new IBk();
	}

}
