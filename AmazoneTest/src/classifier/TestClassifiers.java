package classifier;

import java.io.File;
import java.io.IOException;

import sample.SamplesManager;
import classifier.ClassifierFactory.ClassifierType;

public class TestClassifiers {
	
	public static void main(String[] args) throws IOException {
		Classifier cls = ClassifierFactory.getClassifier(ClassifierType.SVM_LINEAR, 
				SamplesManager.getInstance(), new File(SamplesManager.DATA_DIR), null);
		Result res = cls.crossValidation(10);
		System.out.println("Final Results");
		System.out.println("Num Samples: " + res.numSamples);
		System.out.println("Correct Samples: " + res.correctSamples);
		System.out.println("Acuracy: " + res.accuracy());
		System.out.println("Conf mat:");
		System.out.println( res.confMatString());
		
		
	}

}
