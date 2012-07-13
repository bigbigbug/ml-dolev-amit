package classifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sample.Sample;
import sample.SamplesManager;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;

public class WekaWraper implements Classifier {
	
	private final Instances trainDataSet;
	private final Instances testDataSet;
	private final NaiveBayesMultinomial classifier = new NaiveBayesMultinomial();
	
	private static final int numAtts = SamplesManager.getInstance().numAttributes();
	private static final ArrayList<Attribute> atts = new ArrayList<Attribute>();
	static {
		System.out.println();
		for (int i = 0; i < numAtts+1; i++) {
			atts.add(i,new Attribute(Integer.toString(i)));
		}
	}

	public WekaWraper(List<Sample> train, List<Sample> test) {
		trainDataSet = SamplesManager.asWekaInstances(train);
		testDataSet = SamplesManager.asWekaInstances(test);
	}

	private void convertInstances(List<Sample> train, Instances data) {
		data.setClass(atts.get(numAtts));
		for (Sample s : train) {
			Instance instance= new SparseInstance(s.attributes.size());
			instance.setDataset(data);
			data.add(instance);
			for (sample.Attribute nextAtt : s.attributes) {
				instance.setValue(atts.get(nextAtt.attributeNumber), nextAtt.getValue());
			}
			instance.setClassValue(s.classification);
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
