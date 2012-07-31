package feature.selection;

import java.util.List;
import java.util.Random;

import sample.Sample;
import sample.SamplesManager;
import weka.attributeSelection.AttributeEvaluator;

public class StochasticBestFirst implements FeatureSelector {
	private StochasticBestFirstStep step;
	private final Random r;
	private final AttributeEvaluator eval;
	private final int gap;
	private final int numSubsets;
	private final int numThreads;
	private final ClassifierBuilder builder;
	private final int numFeatures;
	private int[] atts;
	public StochasticBestFirst(Random r, AttributeEvaluator eval, int numSubsets, int gap, int numThreads, ClassifierBuilder builder, int numFeatures) {
		this.r = r;
		this.eval = eval;
		this.builder = builder;
		this.numSubsets = numSubsets;
		this.gap = gap;
		this.numThreads = numThreads;
		this.numFeatures = numFeatures;
	}
	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		try { 
			step = new StochasticBestFirstStep(trainSet, r, eval);
			do { 
				atts = step.step(numSubsets, gap, numThreads, builder);
			} while (atts.length > numFeatures + gap);
			if (numFeatures != atts.length) {
				atts = step.step(numSubsets,atts.length - numFeatures, numThreads, builder);
			}
			return SamplesManager.reduceDimensions(trainSet, atts);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Sample> filterFeaturesFromTest(List<Sample> testSet)
			throws IllegalStateException {
		if (atts == null) throw new IllegalStateException();
		return SamplesManager.reduceDimensions(testSet,atts);

	}

	@Override
	public int numberOfFeatures() {
		if (atts == null) throw new IllegalStateException();
		return atts.length;
	}

}
