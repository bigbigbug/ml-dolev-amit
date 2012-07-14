package feature.selection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sample.Attribute;
import sample.Sample;
import sample.SamplesManager;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GeneticSearch;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.core.Instances;

public class GeneticCFSFeatureSelector implements FeatureSelector {
	private int[] selectedFeatures;

	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		CfsSubsetEval cfs = new CfsSubsetEval();
		GeneticSearch genetic = new GeneticSearch();
		Instances instances = SamplesManager.asWekaInstances(trainSet);
		try { 
			selectedFeatures = genetic.search(cfs,instances);
			Arrays.sort(selectedFeatures);
			System.out.println(Arrays.toString(selectedFeatures));
			System.out.println(selectedFeatures.length);
			return SamplesManager.reduceDimensions(trainSet,selectedFeatures);
		} catch (Exception e) { 
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public List<Sample> filterFeaturesFromTest(List<Sample> testSet)
			throws IllegalStateException {
		if (selectedFeatures == null) throw new IllegalStateException("Must first invoke selectFeaturesFromTrain()");
		return SamplesManager.reduceDimensions(testSet,selectedFeatures);
	}

	@Override
	public int numberOfFeatures() {
		if (selectedFeatures == null) throw new IllegalStateException("Must first invoke selectFeaturesFromTrain()");
		return selectedFeatures.length;
	}

}
