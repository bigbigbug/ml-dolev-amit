package weka.classifiers.hw1;

import java.io.Serializable;

import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;

public class KNNFactory implements ClassifierFactory, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4409818771851476867L;
	private int k = 1;

	@Override
	//TODO: generating nearest neighbor with cross validation. A change might be needed
	public Classifier getClassifier() {
		return new IBk(k);
	}
	
	public void setKNN(int k) {
		this.k =k;
	}

}
