package classifier;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class Result {
	
	public final double[][] confMat;
	public final int numSamples;
	public final int correctSamples;
	
	public Result(double[][] confMat, double correctSamples, double numSamples) {
		this.confMat = confMat;
		this.correctSamples = new Double(correctSamples).intValue();
		this.numSamples = new Double(numSamples).intValue();
	}

	public Result(double[] expected, double[] predictions) {
		if (expected.length != predictions.length) throw new RuntimeException("Num of expected result does not match num of actual results");
	
		int correctSamplesCounter = 0;
		numSamples = predictions.length;
		Set<Double> classes = new TreeSet<Double>();
		for (int i = 0; i < predictions.length; i++) {
			classes.add(expected[i]);
			classes.add(predictions[i]);
		}
		int numClasses = classes.size();
		confMat = new double[numClasses][numClasses];
		for (int i = 0; i < numClasses; i++) {
			for (int j = 0; j < numClasses; j++) {
				confMat[i][j] = 0;
			}
		}
		for (int i = 0; i < predictions.length; i++) {
			int exp = (new Double(expected[i])).intValue();
			int pred = (new Double(predictions[i])).intValue();
			confMat[exp-1][pred-1]++; 
			if (exp == pred) correctSamplesCounter++;
		}
		correctSamples = correctSamplesCounter;
	}
	
	public double accuracy() {
		return (double)correctSamples/(double)numSamples;
	}
	
	public double[][] confusionMat() {
		return confMat;
	}
	
	private String classeLabels[] = {"Against","Natural","Pro"};
	public String confMatString(){
		StringBuilder sb = new StringBuilder();
		sb.append("\t" + Arrays.toString(classeLabels)+"\n");
        for (int i=0; i<confMat.length; i++) {
        		sb.append(classeLabels[i]);
                for (int j=0; j<confMat[i].length; j++) {
                	sb.append("\t" + confMat[i][j]);
                }
                sb.append("\n");
        }
        return sb.toString();
	}

}
