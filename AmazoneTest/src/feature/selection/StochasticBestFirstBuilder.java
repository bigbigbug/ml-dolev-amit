package feature.selection;

import java.util.Random;

import weka.attributeSelection.InfoGainAttributeEval;

import feature.selection.notUsed.NaiveBayseBuilder;

public class StochasticBestFirstBuilder implements SelectorFactory {
	private static final int NUM_SUBSETS = 100;
	private static final int GAP = 120;
	private static final int NUM_THREADS = 4;
	private Random r = new Random(29);
	@Override
	public FeatureSelector build() {
		return build(100);
	}

	@Override
	public FeatureSelector build(int numFeatures) {
		return new StochasticBestFirst(r, new InfoGainAttributeEval(), NUM_SUBSETS, GAP, NUM_THREADS, new NaiveBayseBuilder(), numFeatures);
	}

}
