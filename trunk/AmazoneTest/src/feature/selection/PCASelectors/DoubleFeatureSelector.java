package feature.selection.PCASelectors;

import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import feature.selection.FeatureSelector;
import feature.selection.SelectorFactory;

import sample.Attribute;
import sample.Sample;
import sample.SamplesManager;

public class DoubleFeatureSelector implements FeatureSelector {
	public static final int GROUP_SIZE = 500;
	private final SelectorFactory firstBuilder, secondBuilder;
	private final int numFeatures;
	private FeatureSelector firstSelector, secondSelector;
	public DoubleFeatureSelector(SelectorFactory firstBuilder, SelectorFactory secondBuilder, int numFeatures) {
		this.firstBuilder = firstBuilder;
		this.secondBuilder = secondBuilder;
		this.numFeatures = numFeatures;
	}
	
	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		List<Sample> firstListsSamples = new LinkedList<Sample>(), samples;
		int n = (numFeatures/GROUP_SIZE) * GROUP_SIZE;
		NavigableSet<Integer> firstGroups;
		if (numFeatures > GROUP_SIZE) {
			NavigableSet<Integer> allAtts = getAttributes(trainSet);
			firstSelector = firstBuilder.build(n);
			firstListsSamples = firstSelector.selectFeatresFromTrain(trainSet);
			firstGroups = getAttributes(firstListsSamples);
			allAtts.removeAll(firstGroups);
			int[] AttsToSelectFrom = asPrimitiveArray(allAtts);
			samples = SamplesManager.reduceDimensions(trainSet, AttsToSelectFrom);
		} else { 
			samples = trainSet;
			firstGroups = new TreeSet<Integer>();
		}
		samples = firstBuilder.build(GROUP_SIZE).selectFeatresFromTrain(samples);
		Set<Integer> temp = getAttributes(samples); //TODO: no commit
		System.out.println(temp.size());
		secondSelector = secondBuilder.build(numFeatures - n);
		samples = secondSelector.selectFeatresFromTrain(samples);
		return SamplesManager.uniteAttributes(samples,firstListsSamples);
	}


	private int[] asPrimitiveArray(NavigableSet<Integer> set) {
		int[] res = new int[set.size()];
		int i = 0;
		for (Integer x : set) { 
			res[i++] = x.intValue();
		}
		return res;
	}

	private NavigableSet<Integer> getAttributes(List<Sample> samples) {
		NavigableSet<Integer> result = new TreeSet<Integer>();
		for (Sample s : samples) 
			for (Attribute a : s.attributes)
				result.add(a.attributeNumber);
		return result;
	}

	@Override
	public List<Sample> filterFeaturesFromTest(List<Sample> testSet)
			throws IllegalStateException {
		if (firstSelector == null) throw new IllegalStateException("must first invoke selectFeaturesFromTrain()");
		return SamplesManager.uniteAttributes(firstSelector.filterFeaturesFromTest(testSet), secondSelector.filterFeaturesFromTest(testSet));
	}

	@Override
	public int numberOfFeatures() {
		if (firstSelector == null) throw new IllegalStateException("must first invoke selectFeaturesFromTrain()");
		return firstSelector.numberOfFeatures() + secondSelector.numberOfFeatures();
	}

}
