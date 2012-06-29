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
	
	private final Instances trainDataSet = new Instances("trainData", atts, 10000);
	private final Instances testDataSet = new Instances("testData", atts, 10000);
	private final NaiveBayesMultinomial classifier = new NaiveBayesMultinomial();
	
	private static ArrayList<Attribute> atts = new ArrayList<Attribute>();
	static {
		int numAtts = SamplesManager.getInstance().numAttributes();
		
		for (int i = 0; i < numAtts; i++) {
			atts.add(i,new Attribute(Integer.toString(i)));
		}
	}

	public WekaWraper(List<Sample> train, List<Sample> test) {
		convertInstances(train,trainDataSet);
		convertInstances(test,testDataSet);
	}

	private void convertInstances(List<Sample> train, Instances data) {
		for (Sample s : train) {
			Instance instance= new SparseInstance(s.attributes.size());
			instance.setClassValue(s.classification);
			for (sample.Attribute nextAtt : s.attributes) {
				instance.setValue(atts.get(nextAtt.attributeNumber), nextAtt.value);
			}
			data.add(instance);
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
