package experiments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
	
	private static final String PRECISION_RECALL_RES_FILE = "experiments/precision_recall/res.txt";
	private static final String RESULTS_DIR_NAME = "experiments/precision_recall/";
	
	public static void main(String[] args) throws IOException {
		
		Classifier cls = ClassifierFactory.getClassifier(ClassifierType.SVM_HYPERBOLIC, new SamplesManager(),
				 new File(SamplesManager.DATA_DIR), FeatureSelector.NONE); // TODO set this
		
		File dir = new File(RESULTS_DIR_NAME);
		if (!dir.isDirectory()) dir.mkdirs();
		File resFile = new File(PRECISION_RECALL_RES_FILE);
		if (resFile.exists()) resFile.delete();
		resFile.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(resFile));
		
		PresisionTest res = ((SVMWraper)cls).confidance(10);
		double paramOptMat[][] = ((SVMWraper)cls).getLastOptMat();
		
		
		for (int id = 1; id <= 3; id++){
			StringBuilder precision = new StringBuilder();
			precision.append("precision_class_");
			precision.append(id);
			precision.append(" = [ ");
			StringBuilder recall = new StringBuilder();
			recall.append("recall_class_");
			recall.append(id);
			recall.append(" = [ ");
			for (int i = 0; i < 100; i++) {
				precision.append(res.calcPrecision((double)i/100.0,id));
				precision.append(", ");
				recall.append(res.calcRecall((double)i/100.0,id));
				recall.append(", ");
			}
			precision.append("]\n");
			recall.append("]\n");
			System.out.println(precision);
			System.out.println(recall);
			bw.write(precision.toString());
			bw.write(recall.toString());
		}
		bw.write("\noptimization search matrix\n");
		for (double[] ds : paramOptMat) {
			bw.write("\n"+Arrays.toString(ds));
		}
		bw.close();
	}
	
	
	public double calcPrecision(double confidance, int classInd) {
		int acc = 0;
		int ret = 0;
		for (int i = 0; i < probs.length; i++) {
			if (probs[i][classInd-1] >= confidance) {
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
				if (probs[i][classInd-1] >= confidance) ret++;
			}
		}
		return (double)ret/(double)valid;
	}
}
