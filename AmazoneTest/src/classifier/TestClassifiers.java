package classifier;

import java.io.File;
import java.io.IOException;
import java.util.List;

import sample.Sample;
import sample.SamplesManager;
import classifier.ClassifierFactory.ClassifierType;
import feature.selection.FeatureSelector;

public class TestClassifiers {
	private static final String CLASSIFICATION_FILE_NAME = "train.label";
	private static final String DATA_FILE_NAME = "train.data";
	private static final String TEST_CLASSIFICATION_FILE_NAME = "test.label";
	private static final String TEST_DATA_FILE_NAME = "test.data";
	
	
	public static void main(String[] args) throws IOException {
		
		ClassifierType type = ClassifierType.SVM_HYPERBOLIC; 	// Accuracy: 0.753125 (train-test), 0.35044642857142855 (cross)
//		ClassifierType type = ClassifierType.SVM_LINEAR;		// Accuracy: 0.7766 (train-test), 0.3788 (cross) 
//		ClassifierType type = ClassifierType.NAIVE_BAYSE;		// Accuracy: 0.77   (train-test), 0.7477 (cross)
		
		
		List<Sample> train = SamplesManager.getInstance().parseTrainData(new File(SamplesManager.DATA_DIR), 
				DATA_FILE_NAME, CLASSIFICATION_FILE_NAME, FeatureSelector.NONE);
		List<Sample> test = SamplesManager.getInstance().parseTrainData(new File(SamplesManager.DATA_DIR), 
				TEST_DATA_FILE_NAME, TEST_CLASSIFICATION_FILE_NAME, FeatureSelector.NONE);
		
		Classifier cls = ClassifierFactory.getClassifier(type, train, test);
		Result res = cls.trainTest();
//		Result res = cls.crossValidation(10);
		
		System.err.println("Final Results");
		System.err.println("Num Samples: " + res.numSamples);
		System.err.println("Correct Samples: " + res.correctSamples);
		System.err.println("Accuracy: " + res.accuracy());
		System.err.println("Conf mat:");
		System.err.println(res.confMatString());
	}

}
