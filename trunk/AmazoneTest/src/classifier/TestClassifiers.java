package classifier;

import java.io.File;
import java.io.IOException;

import sample.SamplesManager;
import classifier.ClassifierFactory.ClassifierType;
import feature.selection.FeatureSelector;

public class TestClassifiers {
	
	public static void main(String[] args) throws IOException {
		
//		ClassifierType type = ClassifierType.SVM_HYPERBOLIC; 	// Accuracy: 0.753 (train-test), 0.72 (cross)
//		ClassifierType type = ClassifierType.SVM_LINEAR;		// Accuracy: 0.751 (train-test), 0.713 (cross) 
		ClassifierType type = ClassifierType.NAIVE_BAYSE;		// Accuracy: 0.658 (train-test), 0.704 (cross)
		
		Classifier cls = ClassifierFactory.getClassifier(type, new SamplesManager(),
				 new File(SamplesManager.DATA_DIR), FeatureSelector.NONE);
		
//		Result res = cls.trainTest();
		Result res = cls.crossValidation(10);
		
		System.err.println("Final Results");
		System.err.println("Num Samples: " + res.numSamples);
		System.err.println("Correct Samples: " + res.correctSamples);
		System.err.println("Accuracy: " + res.accuracy());
		System.err.println("Conf mat:");
		System.err.println(res.confMatString());
	}

}
