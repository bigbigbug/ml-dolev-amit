package feature.selection;

public class DFSelectorBuilder implements SelectorFactory {
	@Override
	public FeatureSelector build() {
		DFSelector ig = new DFSelector(100);
		return ig;
	}

	@Override
	public FeatureSelector build(int numFeatures) {
		DFSelector ig = new DFSelector(numFeatures);
		return ig;
	}

}
