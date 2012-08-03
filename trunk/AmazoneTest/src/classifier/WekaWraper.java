package classifier;

import java.util.List;
import java.util.Random;

import sample.Sample;
import sample.SamplesManager;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.core.Instance;
import weka.core.Instances;
import experiments.PresisionTest;

public class WekaWraper implements Classifier {

	private final Instances trainDataSet;
	private final Instances testDataSet;
	private final weka.classifiers.Classifier classifier = new NaiveBayesMultinomial();

	public WekaWraper(List<Sample> train, List<Sample> test) {
		trainDataSet = SamplesManager.asWekaInstances(train);
		testDataSet = SamplesManager.asWekaInstances(test);
	}

	@Override
	public Result crossValidation(int folds) {
		try {
			Evaluation eval = new Evaluation(trainDataSet);
			eval.crossValidateModel(classifier, trainDataSet, folds, new Random(1));
			return new Result(eval.confusionMatrix(), eval.correct(), eval.numInstances());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Result trainTest() {
		try {
			classifier.buildClassifier(trainDataSet);
			Evaluation eval = new Evaluation(trainDataSet);
			eval.evaluateModel(classifier, testDataSet);
			return new Result(eval.confusionMatrix(), eval.correct(), eval.numInstances());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public PresisionTest confidance(int folds) {
		System.err.println("start");
		double[][] prob = new double[trainDataSet.size()][3];
		double[] cls = new double[trainDataSet.size()];
		trainDataSet.randomize(new Random(1));
		trainDataSet.stratify(folds);
		// Do the folds
		try {
			int index = 0;
			for (int i = 0; i < folds; i++) {
				Instances train = trainDataSet.trainCV(folds, i, new Random(1));
				weka.classifiers.Classifier copiedClassifier = AbstractClassifier.makeCopy(classifier);
				copiedClassifier.buildClassifier(train);
				Instances test = trainDataSet.testCV(folds, i);
				for (Instance instance : test) {
					prob[index] = copiedClassifier.distributionForInstance(instance);
					cls[index++] = instance.classValue()+1;
				}
			}
			return new PresisionTest(prob, cls);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
