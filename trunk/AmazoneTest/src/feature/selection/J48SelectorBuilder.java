package feature.selection;

import java.util.List;

import sample.Sample;
import sample.SamplesManager;

import weka.classifiers.trees.J48;

public class J48SelectorBuilder implements SelectorFactory {
	private final J48 j48;
	
	public J48SelectorBuilder(J48 j48) throws Exception {
		this.j48 = j48;
	}

	public J48SelectorBuilder(List<Sample> samples) throws Exception {
		j48 = new J48();
		j48.setUnpruned(true);
		j48.buildClassifier(SamplesManager.asWekaInstances(samples));
	}
	
	@Override
	public FeatureSelector build() {
		return new J48FeatureSelector(j48, 750);
	}

	@Override
	public FeatureSelector build(int numFeatures) {
		return new J48FeatureSelector(j48, numFeatures);
	}
	

}
