import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.hw1.J48KNN;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Q4 {
	private static final int NUM_SAMPLES = 1000;

	static String attDecleration = "@relation test$\n"
			+ "@attribute att1 real\n" + "@attribute att2 real\n"
			+ "@attribute class { C1, C2 }\n" + "@data\n";
	
	static String endFileString = "%\n%\n%\n";

	public static void main(String[] args) throws Exception {
		File dir = new File("./resources/test2/");
		if (dir.isDirectory() == false) dir.mkdirs();
		File files[] = new File[50];
			for (int i = 0; i < files.length; i++) {
				files[i] = new File("./resources/test2/file" + i + ".arff");
			}
			try {
				Random random = new Random(5);
				for (int i = 0; i < files.length; i++) { 
					System.out.println("generating file " + (i+1));
					files[i].delete();
//					createFirstTest(files[i],random);
					createSecondTest(files[i],random);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		
		int k = 7;
		double[] resultsTree = new double[files.length];
		double[] resultsModified = new double[files.length];
		int i = 0;
		String[] options = { "-C 0.25 -M 51" };
		for (File file : files) {

			Evaluation eval;
			System.out.println(file.getCanonicalPath());
			Classifier classifier1 = new J48();
			((J48)classifier1).setOptions(options);

			J48KNN classifier2 = new J48KNN();
			classifier2.setIgnoreAtt(false);
			classifier2.setKNN((k));
			((J48)classifier2).setOptions(options);
			
			DataSource source = new DataSource(file.getCanonicalPath());
			Instances data = source.getDataSet();
			data.setClassIndex(data.numAttributes() - 1);

			// J48
			eval = new Evaluation(data);
			eval.crossValidateModel(classifier1, data, 10, new Random(1));
			System.out.println("J48:" + eval.pctCorrect());
			resultsTree[i] = eval.pctCorrect();
			// c45(knn)
			eval = new Evaluation(data);
			eval.crossValidateModel(classifier2, data, 10, new Random(1));
			System.out.println("c45(knn): " + eval.pctCorrect());
			resultsModified[i++] = eval.pctCorrect();
		}
		System.out.println("tree");
		System.out.println(Arrays.toString(resultsTree));
		System.out.println("C45(7NN)");
		System.out.println(Arrays.toString(resultsModified));
	}

	private static void createSecondTest(File file, Random random) throws IOException {
		file.delete();
		file.createNewFile();

		FileWriter writer = new FileWriter(file);
		BufferedWriter out = new BufferedWriter(writer);
		out.write(attDecleration.replace('$', '2'));

		double val1, val2;
		String classId;
		for (int i = 0; i < NUM_SAMPLES; i++) {
			val1 = random.nextDouble();
			val2 = random.nextDouble();
			classId = (val1 < 0.5)||((val1>0.65)&&(val1<0.75)&&(val2>0.65)&&(val2<0.75)) ? "C1" : "C2";
			out.write(val1 + "," + val2 + "," + classId + "\n");
		}
		for (int i = 0; i < NUM_SAMPLES/20; i++) {
			val1 = random.nextDouble() / 10 + 0.65;
			val2 = random.nextDouble() / 10 + 0.65;
			classId = "C1";
			out.write(val1 + "," + val2 + "," + classId + "\n");
		}
		out.write(endFileString);
		out.close();

	}

	private static void createFirstTest(File file, Random random) throws IOException {
		file.delete();
		file.createNewFile();

		FileWriter writer = new FileWriter(file);
		BufferedWriter out = new BufferedWriter(writer);
		out.write(attDecleration.replace('$', '1'));

		double val1, val2;
		String classId;
		for (int i = 0; i < NUM_SAMPLES; i++) {
			val1 = random.nextDouble();
			val2 = random.nextDouble();
			classId = (val1 > 0.5 && val2 < 0.5) || (val2 > 0.5 && val1 < 0.5) ? "C1"
					: "C2";
			out.write(val1 + "," + val2 + "," + classId + "\n");
		}

		out.write(endFileString);
		out.close();
	}
}
