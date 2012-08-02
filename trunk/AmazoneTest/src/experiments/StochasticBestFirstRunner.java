package experiments;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Random;

import classifier.ClassifierFactory;
import classifier.ClassifierFactory.ClassifierType;

import sample.Sample;
import sample.SamplesManager;
import weka.attributeSelection.AttributeEvaluator;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import feature.selection.ClassifierBuilder;
import feature.selection.StochasticBestFirstStep;
import feature.selection.notUsed.J48Builder;
import feature.selection.notUsed.NaiveBayseBuilder;

public class StochasticBestFirstRunner {
	public static void runner(List<Sample> samples, BufferedWriter writer, int gap, int numThreads,AttributeEvaluator attributeEvaluator
			,int numSubSets, ClassifierBuilder builder,
			ClassifierType type
			) throws Exception {
		Random r = new Random(29);
		StochasticBestFirstStep sbf = new StochasticBestFirstStep(samples, r, attributeEvaluator);
		int[] atts;
		do { 
			atts = sbf.step(numSubSets, gap, numThreads, builder);
			Instances instances = SamplesManager.asWekaInstances(SamplesManager.reduceDimensions(samples, atts));
			Evaluation eval = new Evaluation(instances);
			eval.crossValidateModel(builder.build(), instances, 3, new Random(11));
			writer.write("#features=" + atts.length + " accuracy=" + eval.pctCorrect() + "\n");
			System.out.println("#features=" + atts.length + " accuracy=" + eval.pctCorrect());
		} while (atts.length > gap);
		writer.close();
	}

	public static void main(String[] args) throws Exception {
		SamplesManager sm = new SamplesManager();
		List<Sample> samples = sm.parseTrainData();
		StringWriter sw = new StringWriter();
		BufferedWriter writer = new BufferedWriter(sw);
		
		ClassifierType type = ClassifierType.NAIVE_BAYSE;
		runner(samples, writer, 120, 1, new InfoGainAttributeEval(), 50, new NaiveBayseBuilder(), type);
		System.out.println(sw.toString());
	}
}
