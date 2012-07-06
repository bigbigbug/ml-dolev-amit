package classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import feature.selection.FeatureSelector;

import sample.Sample;
import sample.SamplesManager;

public class ClassifierFactory {
	
	public enum ClassifierType {
		SVM_LINEAR, SVM_HYPERBOLIC, NAIVE_BAYSE
	}
	
	private static final String CLASSIFICATION_FILE_NAME = "train.label";
	private static final String DATA_FILE_NAME = "train.data";
	private static final String TEST_CLASSIFICATION_FILE_NAME = "test.label";
	private static final String TEST_DATA_FILE_NAME = "test.data";
	/**
	 * Returns an instance of a {@link Classifier}.
	 * @param type: type of classifier used
	 * @param samplesManager: The SamplesManager used to extract data and train
	 * @param dir: The dir where the files are located. Assuming standard file names, 
	 * as found in the static variables in this class, and as defined in the 
	 * assignment description 
	 * @param featuresSelector:  The type of {@link FeatureSelector} to use
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static Classifier getClassifier(ClassifierType type, SamplesManager samplesManager,File dir
			, FeatureSelector featuresSelector ) 
					throws IOException, FileNotFoundException {
		
		List<Sample> train = samplesManager.parseTrainData(dir,DATA_FILE_NAME,CLASSIFICATION_FILE_NAME,featuresSelector);
		List<Sample> test = samplesManager.parseTestData(dir,TEST_DATA_FILE_NAME,TEST_CLASSIFICATION_FILE_NAME);
		
		Classifier retVal = null;
		switch (type) {
	        case SVM_LINEAR:
	        	retVal = new SVMWraper(train, test);
	        	((SVMWraper)retVal).setLinear();
	            break;
	                
	        case SVM_HYPERBOLIC:
	        	retVal = new SVMWraper(train, test);
	        	((SVMWraper)retVal).setHyperbolic();
	            break;
	                     
	        case NAIVE_BAYSE:
	            retVal = new WekaWraper(train, test);
	            break;
	                    
	        default:
	            throw new RuntimeException("this classifier does not exists");
		}
		return retVal;
	}

}
