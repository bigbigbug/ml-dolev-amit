package feature.selection.notUsed;

import java.util.List;

import sample.Sample;
import sample.SamplesManager;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.GeneticSearch;
import weka.attributeSelection.WrapperSubsetEval;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import feature.selection.FeatureSelector;

public class GeneticCFSFeatureSelector implements FeatureSelector {
	AttributeSelection selection;
	@SuppressWarnings("unused")
	private final int numFeatures;
	public GeneticCFSFeatureSelector(int numFeatures) {
		this.numFeatures  = numFeatures;
	}
	public GeneticCFSFeatureSelector() {
		this.numFeatures  = -1;
	}

	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		AttributeSelection selection = new AttributeSelection();
		GeneticSearch genetic = new GeneticSearch();

		//TODO: no commit
		genetic.setReportFrequency(1);
		genetic.setMaxGenerations(5);
		genetic.setPopulationSize(20);
		
		WrapperSubsetEval cfs = new WrapperSubsetEval();
		cfs.setClassifier(new J48());
		try {
			Instances instacnes = SamplesManager.asWekaInstances(trainSet);
			selection.setFolds(2);
			selection.setEvaluator(cfs);
			selection.setSearch(genetic);
			System.out.println("running...");
			selection.SelectAttributes(instacnes);
			System.out.println("done selecting..");
			
			Instances out = selection.reduceDimensionality(instacnes);
			return SamplesManager.asSamplesList(out);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public List<Sample> filterFeaturesFromTest(List<Sample> testSet)
			throws IllegalStateException {
		if (selection == null) throw new IllegalStateException("Must invoke selectFeatureFromTrain() first");
		try { 
			return SamplesManager.asSamplesList(selection.reduceDimensionality(SamplesManager.asWekaInstances(testSet)));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int numberOfFeatures() {
		if (selection == null) throw new IllegalStateException("Must invoke selectFeatureFromTrain() first");
		try { 
			return selection.numberAttributesSelected();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

}
