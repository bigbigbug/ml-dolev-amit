package feature.selection;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import sample.Sample;
import sample.SamplesManager;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class J48FeatureSelector extends Object implements FeatureSelector {
	private final J48 j48;
	private final int numFeatures;
	private int[] selectedAtts;
	public J48FeatureSelector(J48 j48,int numFeatures) {
		this.j48 = j48;
		this.numFeatures = numFeatures;
	}
	
	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		J48 tree = null;
		try { 
			if (j48 != null) tree = j48;
			else tree = buildTree(SamplesManager.asWekaInstances(trainSet));
			selectedAtts = getAttributes(tree);
		} catch (Exception e) { 
			throw new RuntimeException(e);
		}
		if (selectedAtts == null) throw new RuntimeException("attributes in tree were exhausted");
		return SamplesManager.reduceDimensions(trainSet, selectedAtts);
	}

	private int[] getAttributes(J48 tree) throws ParseException{
		String[] arr =  tree.toString().split("\n");
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 3; i < arr.length; i++) {
			String s = arr[i];
			if (s.startsWith("Number")) break;
			String[] depthArr = s.split("\\|");
			int depth = depthArr.length;
			if (s.trim().length() == 0) continue;
			Integer attributeNumber = new Integer(depthArr[depth-1].trim().split("\\s")[0]);
			Integer lastDepth = map.get(attributeNumber);
			if (lastDepth == null) map.put(attributeNumber,depth);
			else if (lastDepth > depth) map.put(attributeNumber, depth);
		}
		List<Entry<Integer, Integer>> list = new ArrayList<Map.Entry<Integer,Integer>>(map.entrySet());
		Collections.sort(list, new Comparator<Entry<Integer,Integer>>() {

			@Override
			public int compare(Entry<Integer, Integer> o1,Entry<Integer, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		if (numFeatures > list.size()) return null;
		int i = 0;
		int[] res = new int[numFeatures];
		for (Entry<Integer, Integer> entry : list) {
			if (i == numFeatures) break;
			res[i++] = entry.getKey();
		}
		return res;
		
	}

	private J48 buildTree(Instances instances) throws Exception {
		J48 tree = new J48();
		tree.buildClassifier(instances);
		return tree;
	}

	@Override
	public List<Sample> filterFeaturesFromTest(List<Sample> testSet)
			throws IllegalStateException {
		if (selectedAtts == null) throw new IllegalStateException("Must first invoke selectFeaturesFromTrain()");
		return SamplesManager.reduceDimensions(testSet, selectedAtts);
	}

	@Override
	public int numberOfFeatures() {
		if (selectedAtts == null) throw new IllegalStateException("Must first invoke selectFeaturesFromTrain()");
		return selectedAtts.length;
	}

}
