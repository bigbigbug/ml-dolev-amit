package feature.selection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import sample.Attribute;
import sample.Sample;
import sample.SamplesManager;

public class DFSelector implements FeatureSelector {

	private class Feature implements Comparable<Feature>{
		public Feature(Integer id) {
			this.id = id;
			hashCode = new Integer(id).hashCode();
		}
		private final int id;
		private int df = 1;
		private final int hashCode;
		@Override
		public int compareTo(Feature o) {
			return Integer.compare(o.df, df);
		}
		@Override
		public int hashCode() {
			return hashCode;
		}
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Feature)) 
				return false;
			return ((Feature)obj).id == id;
		}
		@Override
		public String toString() {
			return "id=" + id + " df=" + df;
		}
	}

	public DFSelector(int numFeatures) {
		this.numFeatures = numFeatures;
	}

	private final int numFeatures;
	private int[] validFeatures = null;

	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		ArrayList<Feature> dfArr = new ArrayList<Feature>();
		int size = 12050;
		extendFeaturesArr(dfArr, size);

		for (Sample sample : trainSet) {
			for (Attribute att : sample.attributes) {
				if (att.attributeNumber>=dfArr.size()) {
					size = att.attributeNumber;
					extendFeaturesArr(dfArr, size);
				}
				dfArr.get(att.attributeNumber).df++;
			}
		}
		Collections.sort(dfArr);

		if (size < numFeatures) throw new RuntimeException("Not enough features");
		validFeatures = asPrimitiveArray(dfArr.subList(0, numFeatures));
		Arrays.sort(validFeatures);
		return SamplesManager.reduceDimensions(trainSet,validFeatures);
	}

	private int[] asPrimitiveArray(List<Feature> list) {
		int[] res = new int[list.size()];
		int i = 0;
		for (Feature f : list) { 
			res[i++] = f.id;
		}
		return res;
	}

	private void extendFeaturesArr(ArrayList<Feature> dfArr, int upTo) {
		for (int i = dfArr.size(); i <= upTo; i++) {
			dfArr.add(i, new Feature(i));
		}
	}

	@Override
	public List<Sample> filterFeaturesFromTest(List<Sample> testSet)
			throws IllegalStateException {
		return SamplesManager.reduceDimensions(testSet, validFeatures);
	}

	@Override
	public int numberOfFeatures() {
		if (validFeatures == null) throw new IllegalStateException("Must invoke selectFeatureFromTrain() first");
		return validFeatures.length;
	}

}