package feature.selection;

public class ReliefFBuilder implements SelectorFactory {
	@Override
	public FeatureSelector build() {
		ReliefFFeatureSelector ig = new ReliefFFeatureSelector(100);
		return ig;
	}

	@Override
	public FeatureSelector build(int numFeatures) {
		ReliefFFeatureSelector ig = new ReliefFFeatureSelector(numFeatures);
		return ig;
	}

}
