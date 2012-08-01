package feature.selection;

import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import sample.Attribute;
import sample.Sample;
import sample.SamplesManager;

public class DoubleFeatureSelector implements FeatureSelector {
	public static final int GROUP_SIZE = 1000;
	private final SelectorFactory firstBuilder, secondBuilder;
	private final int numFeatures;
	private int[] attributes;
	public DoubleFeatureSelector(SelectorFactory firstBuilder, SelectorFactory secondBuilder, int numFeatures) {
		this.firstBuilder = firstBuilder;
		this.secondBuilder = secondBuilder;
		this.numFeatures = numFeatures;
	}
	
	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		List<Sample> samples;
		int n = (numFeatures/GROUP_SIZE) * GROUP_SIZE;
		NavigableSet<Integer> firstGroups;
		if (numFeatures > GROUP_SIZE) {
			NavigableSet<Integer> allAtts = getAttributes(trainSet);
			firstGroups = getAttributes(firstBuilder.build(n).selectFeatresFromTrain(trainSet));
			allAtts.removeAll(firstGroups);
			int[] AttsToSelectFrom = asPrimitiveArray(allAtts);
			samples = SamplesManager.reduceDimensions(trainSet, AttsToSelectFrom);
		} else { 
			samples = trainSet;
			firstGroups = new TreeSet<Integer>();
		}
		samples = firstBuilder.build(GROUP_SIZE).selectFeatresFromTrain(samples);
		samples = secondBuilder.build(numFeatures - n).selectFeatresFromTrain(samples);
		firstGroups.addAll(getAttributes(samples));
		attributes = asPrimitiveArray(firstGroups);
		return SamplesManager.reduceDimensions(trainSet, attributes);
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
		if (attributes == null) throw new IllegalStateException("must first invoke selectFeaturesFromTrain()");
		return SamplesManager.reduceDimensions(testSet, attributes);
	}

	@Override
	public int numberOfFeatures() {
		if (attributes == null) throw new IllegalStateException("must first invoke selectFeaturesFromTrain()");
		return attributes.length;
	}

}
