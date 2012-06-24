package classifier;

import java.util.Set;
import java.util.TreeSet;

public class ClassificationResult {
	
	public final Double[] classes;
	public final double[][] confMat;
	public final int numSamples;
	public final int correctSamples;

	public ClassificationResult(double[] expected, double[] predictions) {
		if (expected.length != predictions.length) throw new RuntimeException("Num of expected result does not match num of actual results");
	
		int correctSamplesCounter = 0;
		numSamples = predictions.length;
		Set<Double> classes = new TreeSet<Double>();
		for (int i = 0; i < predictions.length; i++) {
			classes.add(expected[i]);
			classes.add(predictions[i]);
		}
		int numClasses = classes.size();
		this.classes = classes.toArray(new Double[numClasses]);
		confMat = new double[numClasses][numClasses];
		for (int i = 0; i < numClasses; i++) {
			for (int j = 0; j < numClasses; j++) {
				confMat[i][j] = 0;
			}
		}
		for (int i = 0; i < predictions.length; i++) {
			int exp = (new Double(expected[i])).intValue();
			int pred = (new Double(predictions[i])).intValue();
			confMat[exp][pred]++; 
			if (exp == pred) correctSamplesCounter++;
		}
		correctSamples = correctSamplesCounter;
	}
	
	public double accuracy() {
		return correctSamples/numSamples;
	}

}
