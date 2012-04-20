import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.hw1.J48KNN;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.J48Test;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


public class TestMain {
	private static class ExperimentResultsHandler {
		private final static String RESULTS_DIR = "experiment_results/";
		private final static String CONFUSION_SUFFIX = "_confusion.txt";

		private final String[] expNames;
		private final FileWriter[] confusionMatrices;
		private final StringBuilder[] correctRatings;

		private int last = 0;
		int exp = 0;

		public ExperimentResultsHandler(String... expNames) throws IOException {
			File resultsDir = new File(RESULTS_DIR);
			if (resultsDir.exists() == false) resultsDir.mkdir();
			this.expNames = expNames;

			confusionMatrices = new FileWriter[expNames.length];
			for (int i = 0; i < expNames.length; i++) {
				File confusion1 = new File(resultsDir,expNames[i] + CONFUSION_SUFFIX);
				if (confusion1.exists()) confusion1.delete();
				confusion1.createNewFile();
				FileWriter fw1 = new FileWriter(confusion1);
				confusionMatrices[i] = fw1;
			}
			correctRatings = new StringBuilder[expNames.length];
			for (int i = 0; i < correctRatings.length; i++) {
				correctRatings[i] = new StringBuilder();
				correctRatings[i].append('[');
			}

		}
		public void nextResults(double[][] confusionMatrix, double correct) throws IOException {
			if (last == 0 ) exp++;
			correctRatings[last].append(correct).append(',');
			StringBuilder confusion = new StringBuilder();
			for (double[] line : confusionMatrix) {
				confusion.append(Arrays.toString(line)).append('\n');
			}
			confusionMatrices[last].write("experiment #" + exp);
			confusionMatrices[last].write(confusion.toString());
			last = (last + 1) % expNames.length;
		}

		public void finalize() throws IOException {
			for (int i = 0; i < expNames.length; i++) {
				correctRatings[i].append(']');
				System.out.println(expNames[i] + ": " + correctRatings[i].toString());
				confusionMatrices[i].close();
			}
		}
	}

	private final static String SOURCE = "required_datasets/cmc.arff";
	private final static String SOURCE_DIR = "required_datasets/";


	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		File dir = new File(SOURCE_DIR);
		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File arg0) {
				if (arg0.isDirectory()) return false;
				return true;
			}
		});


		String[] expNames = { "ignoreAttributes" , "useAllAttributes" };
		ExperimentResultsHandler erh = new ExperimentResultsHandler(expNames);
		try { 
			int tempCount = 0; //TODO: nocommit
			for (File file : files) {
				if (tempCount++ == 1) break; //TODO: nocommit
				Evaluation eval;
				System.out.println(file.getCanonicalPath());
				Classifier classifier1 = new J48KNN();
				
//				classifier1.setIgnoreAtt(true);
				Classifier classifier2 = new J48KNN();
//				classifier2.setIgnoreAtt(false);

				DataSource source = new DataSource(file.getCanonicalPath());
				Instances data = source.getDataSet();
				data.setClassIndex(data.numAttributes()-1);
				//ignoring attributes
				eval = new Evaluation(data);
				eval.crossValidateModel(classifier1, data, 10, new Random(1));
				erh.nextResults(eval.confusionMatrix(), eval.pctCorrect());

				System.out.println("pctCorrect=" + eval.pctCorrect() + " pctIncorrect=" + eval.pctIncorrect() + " correct=" + eval.correct() + " incorrect=" + eval.incorrect());
				for (double[] line : eval.confusionMatrix()) {
					System.out.println(Arrays.toString(line));
				}
				
				
				//don't ignore attributes
//				eval = new Evaluation(data);
//				eval.crossValidateModel(classifier2, data, 10, new Random(1));
//				erh.nextResults(eval.confusionMatrix(), eval.pctCorrect());
			}
		} finally { 
			erh.finalize();
		}




		//		String[] options = { "-U" };
		//		
		//		Classifier classifier1 = new J48();
		//		eval.crossValidateModel(classifier1, data, 10, new Random(1));
		//		System.out.println("incorect: " + eval.incorrect() + " success rate: "+ eval.pctCorrect());
		//		
		//		eval = new Evaluation(data);
		//		Classifier classifier2 = new IBk();
		//		eval.crossValidateModel(classifier2, data, 10, new Random(1));
		//		System.out.println("incorect: " + eval.incorrect() + " success rate: "+ eval.pctCorrect());
		//		
		//		eval = new Evaluation(data);
		//		J48KNN classifier3 = new J48KNN();
		//		classifier3.setIgnoreAtt(false);
		//		eval.crossValidateModel(classifier3, data, 10, new Random(1));
		//		System.out.println("incorect: " + eval.incorrect() + " success rate: "+ eval.pctCorrect());
		//		
		//		eval = new Evaluation(data);
		//		J48KNN classifier4 = new J48KNN();
		//		classifier3.setIgnoreAtt(true);
		//		eval.crossValidateModel(classifier4, data, 10, new Random(1));
		//		System.out.println("incorect: " + eval.incorrect() + " success rate: "+ eval.pctCorrect());
		//		

	}

}

