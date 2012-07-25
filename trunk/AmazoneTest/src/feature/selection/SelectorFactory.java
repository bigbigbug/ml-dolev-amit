package feature.selection;

public interface SelectorFactory {
	public FeatureSelector build();
	public FeatureSelector build(int numFeatures);
}
