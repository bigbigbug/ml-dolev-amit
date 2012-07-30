package experiments;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import sample.SamplesManager;
import classifier.Classifier;
import classifier.ClassifierFactory;
import classifier.ClassifierFactory.ClassifierType;
import classifier.SVMWraper;
import feature.selection.FeatureSelector;

public class PresisionTest {
	
	private double[][] probs;
	private double[] cls;


	public PresisionTest(double[][] probs, double cls[]) {
		this.probs = probs;
		this.cls = cls;
	}
	
	public static void main(String[] args) throws IOException {
		
		Classifier cls = ClassifierFactory.getClassifier(ClassifierType.SVM_HYPERBOLIC, new SamplesManager(),
				 new File(SamplesManager.DATA_DIR), FeatureSelector.NONE); // TODO set this
		
		PresisionTest res = ((SVMWraper)cls).confidance();
		
		for (int i = 0; i < 100; i++) {
			int id = 1;
			System.out.println("(P,R) = ("+res.calcPrecision(1/(double)i,id)+","+res.calcRecall(1/(double)i,id)+")");
		}
	}
	
	
	public double calcPrecision(double confidance, int classInd) {
		int acc = 0;
		int ret = 0;
		for (int i = 0; i < probs.length; i++) {
			if (probs[i][classInd] >= confidance) {
				ret++; 
				if (cls[i] == classInd) acc++;
			}
		}
		return (double)acc/(double)ret;
	}
	
	public double calcRecall(double confidance, int classInd) {
		int valid = 0;
		int ret = 0;
		for (int i = 0; i < probs.length; i++) {
			if (cls[i] == classInd) {
				valid++; 
				if (probs[i][classInd] >= confidance) ret++;
			}
		}
		return (double)ret/(double)valid;
	}
}
