package experiments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sample.SamplesManager;
import classifier.Classifier;
import classifier.ClassifierFactory;
import classifier.ClassifierFactory.ClassifierType;
import feature.selection.SymmetricalUncertFeatureSelector;

public class PresisionTest {

	private double[][] probs;
	private double[] cls;

	public PresisionTest(double[][] probs, double cls[]) {
		this.probs = probs;
		this.cls = cls;
	}

	private static final String PRECISION_RECALL_RES_FILE = "experiments/precision_recall/res_";
	private static final String PRECISION_RECALL_RES_EX = ".txt";
	private static final String RESULTS_DIR_NAME = "experiments/precision_recall/";

	public static void main(String[] args) throws IOException {

		ClassifierType type;
//		type = ClassifierType.SVM_HYPERBOLIC;
		type = ClassifierType.SVM_LINEAR;
//		type = ClassifierType.NAIVE_BAYSE;
		Classifier cls = ClassifierFactory.getClassifier(type, new SamplesManager(), new File(
				SamplesManager.DATA_DIR), new SymmetricalUncertFeatureSelector(840));

		File dir = new File(RESULTS_DIR_NAME);
		if (!dir.isDirectory())
			dir.mkdirs();
		File resFile = new File(PRECISION_RECALL_RES_FILE + type.name() + PRECISION_RECALL_RES_EX);
		if (resFile.exists())
			resFile.delete();
		resFile.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(resFile));

		PresisionTest res = cls.confidance(10);

		for (int id = 1; id <= 3; id++) {
			StringBuilder precision = new StringBuilder();
			precision.append("precision_class_");
			precision.append(id);
			precision.append(" = [ ");
			StringBuilder recall = new StringBuilder();
			recall.append("recall_class_");
			recall.append(id);
			recall.append(" = [ ");
			for (int i = 0; i < 100; i++) {
				precision.append(res.calcPrecision((double) i / 100.0, id));
				precision.append(", ");
				recall.append(res.calcRecall((double) i / 100.0, id));
				recall.append(", ");
			}
			precision.append("]\n");
			recall.append("]\n");
			System.out.println(precision);
			System.out.println(recall);
			bw.write(precision.toString());
			bw.write(recall.toString());
		}
		bw.close();
	}

	public double calcPrecision(double confidance, int classInd) {
		int acc = 0;
		int ret = 0;
		for (int i = 0; i < probs.length; i++) {
			if (probs[i][classInd - 1] >= confidance) {
				ret++;
				if (cls[i] == classInd)
					acc++;
			}
		}
		return (double) acc / (double) ret;
	}

	public double calcRecall(double confidance, int classInd) {
		int valid = 0;
		int ret = 0;
		for (int i = 0; i < probs.length; i++) {
			if (cls[i] == classInd) {
				valid++;
				if (probs[i][classInd - 1] >= confidance)
					ret++;
			}
		}
		return (double) ret / (double) valid;
	}
}
