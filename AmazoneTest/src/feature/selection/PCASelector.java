package feature.selection;

import java.nio.channels.IllegalSelectorException;
import java.util.List;

import sample.Sample;
import sample.SamplesManager;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.core.Instances;

public class PCASelector implements FeatureSelector {
	
	private final int numFeatures;
	private int[] attributes;
	public PCASelector(int numFeatures) {
		this.numFeatures = numFeatures;
	}
	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		
		PrincipalComponents pca = new PrincipalComponents();
		Ranker ranker = new Ranker();
		ranker.setNumToSelect(numFeatures);
		Instances instances = SamplesManager.asWekaInstances(trainSet);
		try { 
			pca.buildEvaluator(instances);
			attributes = ranker.search(pca, instances);
		} catch (Exception e ) {
			e.printStackTrace();
			return null;
		}
		return SamplesManager.reduceDimensions(trainSet,attributes);
	}

	@Override
	public List<Sample> filterFeaturesFromTest(List<Sample> testSet)
			throws IllegalStateException {
		if (attributes == null) throw new IllegalStateException("Must invoke selectFeaturesFromTrain() first");
		return SamplesManager.reduceDimensions(testSet, attributes);
	}

	@Override
	public int numberOfFeatures() {
		if (attributes == null) throw new IllegalStateException("Must invoke selectFeaturesFromTrain() first");
		return numFeatures;
	}

}
