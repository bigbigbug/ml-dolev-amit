package classifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import sample.Sample;
import sample.SamplesManager;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.ArffSaver;

public class WekaWraper implements Classifier {
	
	private final Instances trainDataSet = new Instances("trainData", atts, 3000);
	private final Instances testDataSet = new Instances("testData", atts, 3000);
	private final NaiveBayes classifier = new NaiveBayes();
	
	private static final int numAtts = SamplesManager.getInstance().numAttributes();
	private static final ArrayList<Attribute> atts = new ArrayList<Attribute>();
	static {
		System.out.println();
		for (int i = 0; i < numAtts; i++) {
			atts.add(i,new Attribute(Integer.toString(i)));
		}
		List<String> labels = new LinkedList<String>();
		labels.add("1");
		labels.add("2");
		labels.add("3");
		
		atts.add(numAtts,new Attribute("class",labels));
	}

	public WekaWraper(List<Sample> train, List<Sample> test) {
		convertInstances(train,trainDataSet);
		convertInstances(test,testDataSet);
		ArffSaver saver = new ArffSaver();
		saver.setInstances(trainDataSet);
		try {
			saver.setFile(new File("./test.arff"));
			saver.writeBatch();
		} catch (IOException e) {
			e.printStackTrace();}
	}

	private void convertInstances(List<Sample> train, Instances data) {
		data.setClass(atts.get(numAtts));
		for (Sample s : train) {
			int size = s.attributes.size()+1;
			int attIndex[] = new int[size]; 
			double attVals[] = new double[size];
			int location = 0;
			for (sample.Attribute nextAtt : s.attributes) {
				attIndex[location] = nextAtt.attributeNumber; 
				attVals[location] = nextAtt.getValue();
				location++;
			}
			attIndex[location] = numAtts; 
			attVals[location] = s.classification-1;
			data.add(new SparseInstance(1.0, attVals, attIndex, size));
		}
	}

	@Override
	public Result crossValidation(int folds) {
		try {
			Evaluation eval = new Evaluation(trainDataSet);
			eval.crossValidateModel(classifier, trainDataSet, 10, new Random(1));
			return new Result(eval.confusionMatrix(), eval.correct(), eval.numInstances());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Result trainTest() {
		try {
			Evaluation eval = new Evaluation(trainDataSet);
			eval.evaluateModel(classifier, testDataSet);
			return new Result(eval.confusionMatrix(), eval.correct(), eval.numInstances());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
