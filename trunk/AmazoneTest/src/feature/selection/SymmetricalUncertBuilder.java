package feature.selection;

public class SymmetricalUncertBuilder implements SelectorFactory {
	@Override
	public FeatureSelector build() {
		SymmetricalUncertFeatureSelector ig = new SymmetricalUncertFeatureSelector(100);
		return ig;
	}

	@Override
	public FeatureSelector build(int numFeatures) {
		SymmetricalUncertFeatureSelector ig = new SymmetricalUncertFeatureSelector(numFeatures);
		return ig;
	}

}
