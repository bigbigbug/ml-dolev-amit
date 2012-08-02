package feature.selection.PCASelectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sample.Attribute;
import sample.Sample;
import sample.SamplesManager;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import feature.selection.FeatureSelector;

public class InformationGainPCASelector implements FeatureSelector {

	private static final double DEFAULT_VAR = 0.95;
	private final double var;

	private  Map<Integer,Double> max;
	private  Map<Integer,Double> min;
	//	private Normalize normalizer;

	private AttributeSelection selection;
	private int[] firstAttributes;
	private int[] candidates;
	public final int GROUP_SIZE = 1000;
	private final int numFeatures;

	public Map<Integer, Integer> createAttributeHistogram(List<Sample> samples) { 
		Map<Integer,Integer> res = new HashMap<Integer, Integer>();
		for (Sample s : samples) {
			for (Attribute a : s.attributes) {
				Integer val = res.get(a.attributeNumber);
				if (val == null) res.put(a.attributeNumber,1);
				else res.put(a.attributeNumber,val+1);
			}
		}
		return res;
	}
	public InformationGainPCASelector(int numFeatures) {
		this(numFeatures,DEFAULT_VAR);
	}
	public InformationGainPCASelector(int numFeatures, double var) {
		this.numFeatures = numFeatures;
		this.var = var;
	}

	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		try { 
			int n = (numFeatures/GROUP_SIZE) * GROUP_SIZE;
			Ranker ranker = new Ranker();
			ranker.setNumToSelect(n);
			InfoGainAttributeEval ig = new InfoGainAttributeEval();
			Instances instances = SamplesManager.asWekaInstances(trainSet);
			ig.buildEvaluator(instances);
			firstAttributes = ranker.search(ig,instances);
			candidates = Arrays.copyOfRange(firstAttributes, n, n+GROUP_SIZE);
			firstAttributes = Arrays.copyOf(firstAttributes, n);
			Arrays.sort(firstAttributes);
			Arrays.sort(candidates);

			List<Sample> reducedSamples = SamplesManager.reduceDimensions(trainSet, candidates);
			PrincipalComponents pca = new PrincipalComponents();
			pca.setVarianceCovered(var);
			ranker = new Ranker();
			//		ranker.setNumToSelect(numFeatures);
			selection = new AttributeSelection();
			selection.setEvaluator(pca);
			selection.setSearch(ranker);
			Instances wekaInstances = SamplesManager.asWekaInstances(reducedSamples);
			selection.SelectAttributes(wekaInstances);
			//			normalizer = new Normalize();
			//			normalizer.setInputFormat(wekaInstances);
			//			wekaInstances = Filter.useFilter(wekaInstances, normalizer);			
			wekaInstances = selection.reduceDimensionality(wekaInstances);
			reducedSamples = SamplesManager.asSamplesList(wekaInstances,false);
			List<Sample> temp = SamplesManager.reduceDimensions(trainSet, firstAttributes);
			temp = SamplesManager.uniteAttributes(reducedSamples, temp);
			max = findMax(temp);
			min = findMin(temp);
			temp = normalize(temp);
			return temp;
		} catch (Exception e) { 
			e.printStackTrace();
			return null;
		}

	}

	private int[] except(int[] arr1, int[] arr2) {
		int[] arr = new int[arr1.length];
		int i = 0;
		for (int x : arr1) {
			int idx = Arrays.binarySearch(arr2, x);
			if (idx < 0) arr[i++] = x; 
		}
		return Arrays.copyOf(arr, i);
	}
	
	private double normalize(double val, int wordId) {
		double maxVal = max.get(wordId);
		double minVal = min.get(wordId);
		if (max == min || val >= maxVal) return 1;
		double d  = val;
		d -= minVal;
		d /= (maxVal - minVal);
		return d;

	}
	private List<Sample> normalize(List<Sample> samples) {
		List<Sample> result = new ArrayList<Sample>();
		for (Sample s : samples) {
			List<Attribute> atts = new ArrayList<Attribute>();
			for (Attribute a : s.attributes) { 
				atts.add(new Attribute(a.attributeNumber, normalize(a.value,a.attributeNumber)));
			}
			result.add(new Sample(atts, s.classification));
		}
		return result;
	}

	private Map<Integer,Double> findMin(List<Sample> samples) {
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		for (Sample s : samples) { 
			for (Attribute a : s.attributes) { 
				Double val = map.get(a.attributeNumber);
				if (val == null) map.put(a.attributeNumber, a.value);
				else map.put(a.attributeNumber, Math.min(a.value, val));
			}
		}
		return map;
	}
	private  Map<Integer,Double> findMax(List<Sample> samples) {
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		for (Sample s : samples) { 
			for (Attribute a : s.attributes) { 
				Double val = map.get(a.attributeNumber);
				if (val == null) map.put(a.attributeNumber, a.value);
				else map.put(a.attributeNumber, Math.max(a.value, val));
			}
		}
		return map;
	}

	@Override
	public List<Sample> filterFeaturesFromTest(List<Sample> testSet)
			throws IllegalStateException {
		if (true) return testSet; //TODO: change!
		if (selection == null) throw new IllegalStateException("must invoke selectFeaturesFromTrain()");
		List<Sample> samples = SamplesManager.reduceDimensions(testSet, candidates);
		Instances instances = SamplesManager.asWekaInstances(samples);
		try { 
			selection.reduceDimensionality(instances);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		samples = SamplesManager.asSamplesList(instances);
		List<Sample> temp = SamplesManager.reduceDimensions(testSet, firstAttributes); 
		samples = SamplesManager.uniteAttributes(temp, samples);
		normalize(samples);
		return samples;
	}

	@Override
	public int numberOfFeatures() {
		if (selection == null) throw new IllegalStateException("must invoke selectFeaturesFromTrain()");
		try { 
			return firstAttributes.length + selection.numberAttributesSelected();
		} catch (Exception e) { 
			e.printStackTrace();
			return 0;
		}
	}

}
