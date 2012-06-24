package classifier;

import java.util.List;

import sample.Sample;

public class ClassifierFactory {
	
	public enum ClassifierType {
		SVM_LINEAR, SVM_HYPERBOLIC, NAIVE_BAYSE
	}
	
	public static Classifier getClassifier(ClassifierType type, List<Sample> train, List<Sample> test) {
		Classifier retVal;
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
	            System.out.println("Weekends are best.");
	            break;
	                    
	        default:
	            throw new RuntimeException("this classifier does not exists");
		}
		return null;
	}

}
