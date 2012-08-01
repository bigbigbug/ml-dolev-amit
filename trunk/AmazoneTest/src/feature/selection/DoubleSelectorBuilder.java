package feature.selection;

public class DoubleSelectorBuilder implements SelectorFactory {

	private final SelectorFactory builder1, builder2;
	public DoubleSelectorBuilder(SelectorFactory builder1, SelectorFactory builder2) {
		this.builder1 = builder1;
		this.builder2 = builder2;
	}
	@Override
	public FeatureSelector build() {
		return build(100);
	}

	@Override
	public FeatureSelector build(int numFeatures) {
		return new DoubleFeatureSelector(builder1, builder2, numFeatures);
	}

}
