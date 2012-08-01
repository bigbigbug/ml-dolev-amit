package feature.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import sample.Attribute;
import sample.Sample;

public class DFSelector implements FeatureSelector {
	
	private class Feature implements Comparable<Feature>{
		public Feature(Integer id) {
			this.id = id;
		}
		final Integer id;
		Integer df = 1;
		@Override
		public int compareTo(Feature o) {
			return o.df.compareTo(df);
		}
		@Override
		public int hashCode() {
			return id.hashCode();
		}
		@Override
		public boolean equals(Object obj) {
			return id.equals(obj);
		}
	}
	
	public DFSelector(int numFeatures) {
		this.numFeatures = numFeatures;
	}
	
	private final int numFeatures;
	private static Set<Feature> validFeatures = null;

	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		
		if (validFeatures == null) {
			ArrayList<Feature> dfArr = new ArrayList<Feature>();
			int size = 12049;
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
			validFeatures = new HashSet<Feature>(dfArr.subList(0, numFeatures));
		}

		return filterFeaturesFromTest(trainSet);
	}

	private void extendFeaturesArr(ArrayList<Feature> dfArr, int upTo) {
		for (int i = dfArr.size(); i < upTo; i++) {
			dfArr.add(i, new Feature(i));
		}
	}

	@Override
	public List<Sample> filterFeaturesFromTest(List<Sample> testSet)
			throws IllegalStateException {
		List<Sample> newSamples = new LinkedList<Sample>();
		for (Sample sample : testSet) {
			List<Attribute> newAttribute = new LinkedList<Attribute>();
			for (Attribute att : sample.attributes) {
				if (!validFeatures.contains(new Feature(att.attributeNumber))) newAttribute.add(att);
			}
			newSamples.add(new Sample(newAttribute,sample.classification));
		}
		return newSamples;
	}

	@Override
	public int numberOfFeatures() {
		if (validFeatures == null) throw new IllegalStateException("Must invoke selectFeatureFromTrain() first");
		return validFeatures.size();
	}
	
	public static void reset() {
		validFeatures = null;
	}
}