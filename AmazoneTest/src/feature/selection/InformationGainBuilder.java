package feature.selection;

public class InformationGainBuilder implements SelectorFactory {

	@Override
	public FeatureSelector build() {
		InformationGainFeatureSelector ig = new InformationGainFeatureSelector(100);
		return ig;
	}

	@Override
	public FeatureSelector build(int numFeatures) {
		InformationGainFeatureSelector ig = new InformationGainFeatureSelector(numFeatures);
		return ig;
	}

}
