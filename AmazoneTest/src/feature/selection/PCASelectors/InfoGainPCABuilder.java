package feature.selection.PCASelectors;

import feature.selection.FeatureSelector;
import feature.selection.SelectorFactory;

public class InfoGainPCABuilder implements SelectorFactory {

	@Override
	public FeatureSelector build() {
		return build(100);
	}

	@Override
	public FeatureSelector build(int numFeatures) {
		return new InformationGainPCASelector(numFeatures);
	}

}