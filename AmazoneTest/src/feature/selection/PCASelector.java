package feature.selection;

import java.util.List;

import sample.Sample;
import sample.SamplesManager;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.core.Instances;

public class PCASelector implements FeatureSelector {
	AttributeSelection selector = new AttributeSelection();
	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		PrincipalComponents pca = new PrincipalComponents();
		
		selector.setEvaluator(pca);
		selector.setSearch(new Ranker());
		Instances instances = SamplesManager.asWekaInstances(trainSet);
		try { 
			selector.SelectAttributes(instances);
			return SamplesManager.asSamplesList(selector.reduceDimensionality(instances));
		} catch (Exception e ) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Sample> filterFeaturesFromTest(List<Sample> testSet)
			throws IllegalStateException {
		try { 
			return SamplesManager.asSamplesList(selector.reduceDimensionality(SamplesManager.asWekaInstances(testSet)));
		} catch (Exception e) { 
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int numberOfFeatures() {
		try { 
			return selector.numberAttributesSelected();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

}
