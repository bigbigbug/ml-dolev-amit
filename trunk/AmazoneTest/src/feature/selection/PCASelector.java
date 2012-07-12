package feature.selection;

import java.util.List;

import sample.Sample;
import weka.attributeSelection.PrincipalComponents;

public class PCASelector implements FeatureSelector {

	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		PrincipalComponents pca = new PrincipalComponents();
		try {
			pca.buildEvaluator(null);
		} catch (Exception e ) {}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> filterFeaturesFromTest(List<Sample> testSet)
			throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numberOfFeatures() {
		// TODO Auto-generated method stub
		return 0;
	}

}
