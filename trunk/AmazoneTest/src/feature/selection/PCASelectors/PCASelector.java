package feature.selection.PCASelectors;

import java.util.List;

import feature.selection.FeatureSelector;

import sample.Sample;
import sample.SamplesManager;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

public class PCASelector implements FeatureSelector {
	private final int numFeatures;
	private final Normalize normalizer;
	private AttributeSelection selection;

	public PCASelector(int numFeatures) {
		this.numFeatures = numFeatures;
		this.normalizer = new Normalize();
	}

	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		PrincipalComponents pca = new PrincipalComponents();
		Ranker ranker = new Ranker();
		ranker.setNumToSelect(numFeatures);
		Instances instances = SamplesManager.asWekaInstances(trainSet);
		selection = new AttributeSelection();
		selection.setEvaluator(pca);
		try {
			normalizer.setInputFormat(instances);
			instances = Filter.useFilter(instances, normalizer);
			selection.setSearch(ranker);
			selection.SelectAttributes(instances);
			instances = selection.reduceDimensionality(instances);
			return SamplesManager.asSamplesList(instances, false);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Sample> filterFeaturesFromTest(List<Sample> testSet) throws IllegalStateException {
		if (selection == null)
			throw new IllegalStateException("Must invoke selectFeaturesFromTrain() first");
		try {
			return SamplesManager.asSamplesList(selection.reduceDimensionality(SamplesManager
					.asWekaInstances(testSet)));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int numberOfFeatures() {
		if (selection == null)
			throw new IllegalStateException("Must invoke selectFeaturesFromTrain() first");
		try {
			return selection.numberAttributesSelected();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
