package feature.selection;

public class PCABuilder implements SelectorFactory {

	@Override
	public FeatureSelector build() {
		return build(100);
	}

	@Override
	public FeatureSelector build(int numFeatures) {
		return new PCASelector(numFeatures);
	}

}
