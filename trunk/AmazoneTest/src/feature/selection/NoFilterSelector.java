package feature.selection;

import java.util.List;

import sample.Sample;

public class NoFilterSelector implements FeatureSelector {
	
	private boolean wasInvoked = false;
	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		wasInvoked = true;
		return trainSet;
	}

	@Override
	public List<Sample> filterFeaturesFromTest(List<Sample> testSet) throws IllegalStateException {
		if (wasInvoked) return testSet;
		throw new IllegalStateException("Must invoke sekectFeaturesFromTrain() first");
	}

}
