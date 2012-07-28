package classifier;

import java.io.File;
import java.io.IOException;

import sample.SamplesManager;
import classifier.ClassifierFactory.ClassifierType;
import feature.selection.FeatureSelector;

public class TestParamOptimization {
	public static void main(String[] args) throws IOException {
		
		ClassifierType types[] = { ClassifierType.SVM_HYPERBOLIC, ClassifierType.SVM_LINEAR };
		// 10 folds no optimizations : SVM_HYPERBOLIC=68s  SVM_LINEAR=56s
		// 3 folds no optimizations  : SVM_HYPERBOLIC=16s  SVM_LINEAR=14s
		// 3 folds optimizations 5x5 : SVM_HYPERBOLIC=291s  SVM_LINEAR=340
		// 3 folds optimizations 5x5 : SVM_HYPERBOLIC=393s  SVM_LINEAR=315
		for (ClassifierType type : types) {
			long start = System.currentTimeMillis();
			Classifier cls = ClassifierFactory.getClassifier(type, new SamplesManager(),
					new File(SamplesManager.DATA_DIR), FeatureSelector.NONE);
			
			Result res = cls.crossValidation(3);
			long runtime = System.currentTimeMillis() - start;
			System.err.println(runtime/(double)1000);
			System.err.println(res.accuracy());
		}
		
	}
}
