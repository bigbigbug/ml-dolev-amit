package feature.selection;

import java.util.List;
import java.util.Random;

import feature.selection.notUsed.NaiveBayseBuilder;

import sample.Sample;
import sample.SamplesManager;
import weka.attributeSelection.AttributeEvaluator;
import weka.attributeSelection.InfoGainAttributeEval;

public class ForwardStochastic implements FeatureSelector {
	private final int numThreads;
	private final int subsetSize;
	private final int numSubsets;
	private final int numFeatures;
	private final ClassifierBuilder evaluatorFactory;
	private final Random r;
	private int[] features = null;
	private final AttributeEvaluator attsEvaluator;
	public ForwardStochastic(int subsetSize, int numSubsets, int numThreads, int numFeatures, ClassifierBuilder evaluatorFactory, Random r, AttributeEvaluator attsEvaluator) {
		this.subsetSize = subsetSize;
		this.numSubsets = numSubsets;
		this.numThreads = numThreads;
		this.numFeatures = numFeatures;
		this.evaluatorFactory = evaluatorFactory;
		this.r = r;
		this.attsEvaluator = attsEvaluator;
	}
	
	public static int DEF_SUBSET_SIZE = 100;
	public static int DEF_NUM_SUBSETS = 10;
	public static int DEF_NUM_THREADS = 2;
	public static ClassifierBuilder DEF_FACTORY = new NaiveBayseBuilder();
	public static Random DEF_RANDOM = new Random(111);
	public static AttributeEvaluator DEF_EVALUATOR = new InfoGainAttributeEval();
	
	public ForwardStochastic(int numFeatures) {
		this(DEF_SUBSET_SIZE,DEF_NUM_SUBSETS,DEF_NUM_THREADS,numFeatures,DEF_FACTORY,DEF_RANDOM,DEF_EVALUATOR);
	}
	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		try { 
			ForwardStochasticStep fss = new ForwardStochasticStep(trainSet, r, attsEvaluator);
			features =  fss.selectAttributes(trainSet, numFeatures, numThreads, numSubsets, subsetSize, evaluatorFactory);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return SamplesManager.reduceDimensions(trainSet, features);
	}

	@Override
	public List<Sample> filterFeaturesFromTest(List<Sample> testSet)
			throws IllegalStateException {
		if (features == null) throw new IllegalStateException("must first invoke selectFeaturesFromTrain()");
		return SamplesManager.reduceDimensions(testSet, features);
	}

	@Override
	public int numberOfFeatures() {
		if (features == null) throw new IllegalStateException("must first invoke selectFeaturesFromTrain()");
		return features.length;
	}

}
