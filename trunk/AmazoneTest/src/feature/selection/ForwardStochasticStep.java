package feature.selection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import sample.Attribute;
import sample.Sample;
import sample.SamplesManager;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.AttributeEvaluator;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class ForwardStochasticStep  {
	private final double[] scores;
	private int[] notSelected;
	private int[] currentAttributes;
	private final List<Sample> originalSamples;
	private final Random r;
	public ForwardStochasticStep(List<Sample> samples, Random r, AttributeEvaluator eval) throws Exception {
		originalSamples = samples;
		this.r = r;
		currentAttributes = new int[0];
		scores = evaluateScores(samples,eval);
		NavigableSet<Integer> atts = new TreeSet<Integer>();
		for (Sample s : samples)
			for (Attribute a : s.attributes)
				atts.add(a.attributeNumber);
					notSelected = asPrimitive(atts.toArray(new Integer[0]));
	}

	private int[] asPrimitive(Integer[] array) {
		int[] res = new int[array.length];
		int i = 0;
		for (Integer x : array) {
			res[i++] = x.intValue();
		}
		return res;
	}

	private double[] evaluateScores(List<Sample> samples, AttributeEvaluator eval) throws Exception {
		Instances instances = SamplesManager.asWekaInstances(samples);
		((ASEvaluation)eval).buildEvaluator(instances);
		int n = instances.numAttributes()-1;
		double[] res = new double[n];
		for (int i = 0; i < n; i++) {
			res[i] = eval.evaluateAttribute(i) + 0.001;
		}
		return res;
	}

	public int[] step(int numSubsets, int subsetSize,int numThreads,final ClassifierBuilder builder) {
		if (notSelected.length < subsetSize) throw new IllegalStateException("not enough features");
		final int[][] candidates = getCandidates(numSubsets,subsetSize);
		final double[] pctCorrect = new double[candidates.length];
		final BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();
		//init work queue:
		for (int i = 0; i < candidates.length; i++) { 
			queue.add(i);
		}
		//work threads:
		Thread[] threads = new Thread[numThreads];
		for (int i = 0; i < numThreads; i++) { 
			Thread t = new Thread() { 
				@Override
				public void run() {
					super.run();
					while (true) { 
						Integer x = queue.poll();
						if (x == null) break;
						int[] candidate = union(candidates[x.intValue()],currentAttributes);
						Instances instances = SamplesManager.asWekaInstances(SamplesManager.reduceDimensions(originalSamples, candidate));
						try { 
							Evaluation eval = new Evaluation(instances);
							eval.crossValidateModel(builder.build(), instances,5,r);
							pctCorrect[x.intValue()] = eval.pctCorrect();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			};
			threads[i] = t;
			t.start();
		}
		//join threads:
		for (Thread t : threads) { 
			try { 
				t.join();
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
		}
		double max = 0;
		int maxI = -1;
		for (int i = 0; i < pctCorrect.length; i++) { 
			if (max < pctCorrect[i]) {
				max = pctCorrect[i];
				maxI = i;
			}
		}
		currentAttributes = union(candidates[maxI],currentAttributes);
		notSelected = setDifference(notSelected,candidates[maxI]);
		return currentAttributes;
	}

	private int[] setDifference(int[] left, int[] right) {
		int[] res = new int[left.length - right.length];
		int i = 0;
		for (int x : left) { 
			if (Arrays.binarySearch(right, x) < 0) res[i++] = x;
		}
		Arrays.sort(res);
		return res;
	}

	private int[] union(int[] arr1, int[] arr2) {
		int[] res = new int[arr1.length + arr2.length];
		int i = 0;
		for (int x : arr1) {
			res[i++] = x;
		}
		for (int x : arr2) {
			res[i++] = x;
		}
		Arrays.sort(res);
		return res;
	}

	private int[][] getCandidates(int numSubsets, int subsetSize) {
		double sum = 0;
		for (int x : notSelected) {
			sum += scores[x];
		}
		NavigableMap<Double, Integer> map = new TreeMap<Double, Integer>();
		double p = 0; 
		for (int x : notSelected) { 
			map.put(p, x);
			p += scores[x]/sum;
		}
		int[][] candidates = new int[numSubsets][];
		for (int i = 1; i < candidates.length; i++) { 
			candidates[i] = getCandidate(map,subsetSize);
		}
		candidates[0] = getBestCandidate(subsetSize);
		return candidates;
	}
	private static class Pair<T extends Comparable<T>,S> implements Comparable<Pair<T,S>> {
		private final T key;
		private final S value;
		public Pair(T key, S value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public int compareTo(Pair<T, S> o) {
			return o.key.compareTo(key);
		}

	}
	private int[] getBestCandidate(int subsetSize) {
		List<Pair<Double, Integer>> list = new ArrayList<Pair<Double,Integer>>();
		for (int x : notSelected) {
			list.add(new Pair<Double, Integer>(scores[x], x));
		}
		Collections.sort(list);
		int[] res = new int[subsetSize];
		int i = 0;
		Iterator<Pair<Double, Integer>> iter = list.iterator();
		while (i < subsetSize) {
			if (!iter.hasNext()) throw new IllegalStateException("set was exhausted");
			res[i++] = iter.next().value;
		}
		Arrays.sort(res);
		return res;
	}

	private int[] getCandidate(NavigableMap<Double, Integer> map, int subsetSize) {
		
		NavigableSet<Integer> set = new TreeSet<Integer>();
		while (set.size() < subsetSize) {
			double d = r.nextDouble();
			set.add(map.floorEntry(d).getValue());
		}
		return asPrimitive(set.toArray(new Integer[0]));
	}

	public int[] selectAttributes(List<Sample> samples, int numFeatures, int numThreads, int numSubsets, int gap, ClassifierBuilder builder) throws Exception { 
		do { 
			step(numSubsets, gap, numThreads, builder);
		} while (currentAttributes.length + gap < numFeatures );
		if (numFeatures != currentAttributes.length) {
			step(numSubsets,numFeatures - currentAttributes.length, numThreads, builder);
		}
		return currentAttributes;
	}
}
