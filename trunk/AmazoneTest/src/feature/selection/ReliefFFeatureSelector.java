package feature.selection;

import java.util.List;

import sample.Sample;
import sample.SamplesManager;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.Ranker;
import weka.attributeSelection.ReliefFAttributeEval;
import weka.core.Instances;

public class ReliefFFeatureSelector  implements FeatureSelector {
	public ReliefFFeatureSelector(int numFeatures) {
		this.numFeatures = numFeatures;
	}
	private final int numFeatures;
	private AttributeSelection selector;
	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		selector = new AttributeSelection();
		Ranker ranker = new Ranker();
		ranker.setNumToSelect(numFeatures);
		selector.setSearch(ranker);
		ReliefFAttributeEval ig = new ReliefFAttributeEval();
		selector.setEvaluator(ig);
		try {
			Instances instacnes = SamplesManager.asWekaInstances(trainSet);
			selector.SelectAttributes(instacnes);
			return SamplesManager.asSamplesList(selector.reduceDimensionality(instacnes));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Sample> filterFeaturesFromTest(List<Sample> testSet)
			throws IllegalStateException {
		if (selector == null) throw new IllegalStateException("Must invoke selectFeatureFromTrain() first");
		try { 
			return SamplesManager.asSamplesList(selector.reduceDimensionality(SamplesManager.asWekaInstances(testSet)));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int numberOfFeatures() {
		if (selector == null) throw new IllegalStateException("Must invoke selectFeatureFromTrain() first");
		try { 
			return selector.numberAttributesSelected();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

}
