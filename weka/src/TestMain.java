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

	private final static String SOURCE = "required_datasets/cmc.arff";
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		DataSource source = new DataSource(SOURCE);
		Instances data = source.getDataSet();
		data.setClassIndex(data.numAttributes()-1);
		String[] options = { "-U" };
		Evaluation eval = new Evaluation(data);
		
		Classifier classifier1 = new J48();
		eval.crossValidateModel(classifier1, data, 10, new Random(1));
		System.out.println("incorect: " + eval.incorrect() + " success rate: "+ eval.pctCorrect());
		
		eval = new Evaluation(data);
		Classifier classifier2 = new IBk();
		eval.crossValidateModel(classifier2, data, 10, new Random(1));
		System.out.println("incorect: " + eval.incorrect() + " success rate: "+ eval.pctCorrect());
		
		eval = new Evaluation(data);
		J48KNN classifier3 = new J48KNN();
		classifier3.setIgnoreAtt(false);
		eval.crossValidateModel(classifier3, data, 10, new Random(1));
		System.out.println("incorect: " + eval.incorrect() + " success rate: "+ eval.pctCorrect());
		
		eval = new Evaluation(data);
		J48KNN classifier4 = new J48KNN();
		classifier3.setIgnoreAtt(true);
		eval.crossValidateModel(classifier4, data, 10, new Random(1));
		System.out.println("incorect: " + eval.incorrect() + " success rate: "+ eval.pctCorrect());
		

	}

}

