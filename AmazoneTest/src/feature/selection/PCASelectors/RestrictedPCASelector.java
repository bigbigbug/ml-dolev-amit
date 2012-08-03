package feature.selection.PCASelectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sample.Attribute;
import sample.Sample;
import sample.SamplesManager;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import feature.selection.FeatureSelector;

public class RestrictedPCASelector implements FeatureSelector {

	private final double var;
	
	private  Map<Integer,Double> max;
	private  Map<Integer,Double> min;
	
	private AttributeSelection selection;
	private int[] firstAttributes;
	private int[] candidates;
	public final int GROUP_SIZE = 2000;
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
	private static final double[] VAR_VALS = {0.15,0.55,0.75,0.97,1.0};
	public RestrictedPCASelector(int numFeatures) {
		this.numFeatures = numFeatures;
		this.var = VAR_VALS[numFeatures%GROUP_SIZE / 400];
	}
	
	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		final Map<Integer,Integer> hist = createAttributeHistogram(trainSet);
		int n = (numFeatures/GROUP_SIZE) * GROUP_SIZE;
		Integer[] arr = hist.keySet().toArray(new Integer[0]);
		Arrays.sort(arr,new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return hist.get(o2).compareTo(hist.get(o1));
			}
		});
		Integer[] firstFeaturesArr = Arrays.copyOf(arr, n);
		Arrays.sort(firstFeaturesArr);
		firstAttributes = toPrimitive(firstFeaturesArr);
		candidates = toPrimitive(Arrays.copyOfRange(arr, n, n+GROUP_SIZE));
		List<Sample> reducedSamples = SamplesManager.reduceDimensions(trainSet, candidates);
		PrincipalComponents pca = new PrincipalComponents();
		pca.setVarianceCovered(var);
		Ranker ranker = new Ranker();
		selection = new AttributeSelection();
		selection.setEvaluator(pca);
		selection.setSearch(ranker);
		try { 
			Instances wekaInstances = SamplesManager.asWekaInstances(reducedSamples);
			selection.SelectAttributes(wekaInstances);
			wekaInstances = selection.reduceDimensionality(wekaInstances);
			reducedSamples = SamplesManager.asSamplesList(wekaInstances,false);
		} catch (Exception e) { 
			e.printStackTrace();
			return null;
		}
		List<Sample> temp = SamplesManager.reduceDimensions(trainSet, firstAttributes);
		temp = SamplesManager.uniteAttributes(reducedSamples, temp);
		max = findMax(temp);
		min = findMin(temp);
		temp = normalize(temp);
		return temp;
		
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
	private int[] toPrimitive(Integer[] objects) {
		int[] res = new int[objects.length];
		int i = 0;
		for (Integer x : objects) {
			res[i++] = x.intValue();
		}
		Arrays.sort(res);
		return res;
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
	
	// test
	public static void main(String[] args) {
		
		for (int i = 400; 12000 > i; i+=400) System.out.println(new RestrictedPCASelector(i).var);
	}

}
