import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.hw1.J48KNN;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Q4 {
	private static final int NUM_SAMPLES = 1000;

	private static File files[] = new File[2];

	static String attDecleration = "@relation test$\n"
			+ "@attribute att1 real\n" + "@attribute att2 real\n"
			+ "@attribute class { C1, C2 }\n" + "@data\n";
	
	static String endFileString = "%\n%\n%\n";
	static {
		files[0] = new File("./src/test1.arff");
		files[1] = new File("./src/test2.arff");
		try {
			files[0].delete();
			createFirstTest();
			files[1].delete();
			createSecondTest();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		
		Q4 q4 = new Q4();

		int k = 7;
		for (File file : files) {

			Evaluation eval;
			System.out.println(file.getCanonicalPath());
			Classifier classifier1 = new J48();

			J48KNN classifier2 = new J48KNN();
			classifier2.setIgnoreAtt(false);
			classifier2.setKNN((k + 2));

			DataSource source = new DataSource(file.getCanonicalPath());
			Instances data = source.getDataSet();
			data.setClassIndex(data.numAttributes() - 1);

			// J48
			eval = new Evaluation(data);
			eval.crossValidateModel(classifier1, data, 10, new Random(1));
			System.out.println("J48:" + eval.pctCorrect());

			// c45(knn)
			eval = new Evaluation(data);
			eval.crossValidateModel(classifier2, data, 10, new Random(1));
			System.out.println("c45(knn): " + eval.pctCorrect());
		}
	}

	private static void createSecondTest() throws IOException {
		files[1].createNewFile();

		FileWriter writer = new FileWriter(files[1]);
		BufferedWriter out = new BufferedWriter(writer);
		out.write(attDecleration.replace('$', '2'));
		Random random = new Random();

		double val1, val2;
		String classId;
		for (int i = 0; i < NUM_SAMPLES; i++) {
			val1 = random.nextDouble();
			val2 = random.nextDouble();
			classId = (val1 > 0.5) ? "C1" : "C2";
			out.write(val1 + "," + val2 + "," + classId + "\n");
		}
		for (int i = 0; i < NUM_SAMPLES/20; i++) {
			val1 = random.nextDouble() / 5 + 0.65;
			val2 = random.nextDouble() / 5 + 0.65;
			classId = "C2";
			out.write(val1 + "," + val2 + "," + classId + "\n");
		}
//		for (int i = 0; i < NUM_SAMPLES/100; i++) {
//			val1 = random.nextDouble() / 10 + 0.8;
//			val2 = random.nextDouble() / 10 + 0.4;
//			classId = "C2";
//			out.write(val1 + "," + val2 + "," + classId + "\n");
//		}
		out.write(endFileString);
		out.close();

	}

	private static void createFirstTest() throws IOException {
		files[0].createNewFile();

		FileWriter writer = new FileWriter(files[0]);
		BufferedWriter out = new BufferedWriter(writer);
		out.write(attDecleration.replace('$', '1'));
		Random random = new Random();

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
