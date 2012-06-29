package feature.selection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sample.Attribute;
import sample.Sample;

public class NoFilterSelector implements FeatureSelector {
	private Set<Attribute> set; 
	private boolean wasInvoked = false;
	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		wasInvoked = true;
		set = new HashSet<Attribute>();
		for (Sample s : trainSet)
			set.addAll(s.attributes);
		return trainSet;
	}

	@Override
	public List<Sample> filterFeaturesFromTest(List<Sample> testSet) throws IllegalStateException {
		if (wasInvoked) return testSet;
		throw new IllegalStateException("Must invoke sekectFeaturesFromTrain() first");
	}

	@Override
	public int numberOfFeatures() {
		return set.size();
	}

}
