package feature.selection;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import feature.selection.notUsed.NaiveBayseBuilder;

import sample.Sample;
import sample.SamplesManager;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.AttributeEvaluator;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class StochasticBestFirstStep implements FeatureSelector, SelectorFactory  {
	private final double[] scores;
	private int[] currentAttributes;
	private List<Sample> currentSamples;
	private final Random r;
	private final RandomSubset rss; 
	public StochasticBestFirstStep(List<Sample> samples, Random r, AttributeEvaluator eval) throws Exception {
		currentSamples = samples;
		this.r = r;
		currentAttributes = SamplesManager.listAttributes(samples);
		scores = evaluateScores(samples,eval);
		rss = new RandomSubset(scores, r);
	}

	private double[] evaluateScores(List<Sample> samples, AttributeEvaluator eval) throws Exception {
		Instances instances = SamplesManager.asWekaInstances(samples);
		((ASEvaluation)eval).buildEvaluator(instances);
		int n = instances.numAttributes();
		double[] res = new double[n];
		for (int i = 0; i < n; i++) {
			res[i] = eval.evaluateAttribute(i);
		}
		return res;
	}

	public int[] step(int numSubsets, int subsetSize,int numThreads,final ClassifierBuilder builder) {
		final int[][] candidates = rss.getSubsets(numSubsets, subsetSize);
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
						int[] candidate = removeFromCurrent(candidates[x.intValue()]);
						Instances instances = SamplesManager.asWekaInstances(SamplesManager.reduceDimensions(currentSamples, candidate));
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
		currentAttributes = removeFromCurrent(candidates[maxI]);
		rss.update(currentAttributes);
		return currentAttributes;
	}

	private int[] removeFromCurrent(int[] remove) {
		Arrays.sort(remove);
		int[] res = new int[currentAttributes.length - remove.length];
		int resI = 0;
		int removeI = 0;
		for (int x : currentAttributes) {
			if (removeI < remove.length && x == remove[removeI]) {
				removeI++;
			} else { 
				res[resI++] = x;
			}
		}
		return res;
	}
	public int[] selectAttributes(List<Sample> samples, int numFeatures, int numThreads, int numSubsets, int gap, ClassifierBuilder builder) throws Exception { 
		do { 
			step(numSubsets, gap, numThreads, builder);
		} while (currentAttributes.length > numFeatures + gap);
		if (numFeatures != currentAttributes.length) {
			step(numSubsets,currentAttributes.length - numFeatures, numThreads, builder);
		}
	return currentAttributes;
	}

	@Override
	public List<Sample> selectFeatresFromTrain(List<Sample> trainSet) {
		return SamplesManager.reduceDimensions(trainSet, currentAttributes);
	}

	@Override
	public List<Sample> filterFeaturesFromTest(List<Sample> testSet) throws IllegalStateException {
		return SamplesManager.reduceDimensions(testSet, currentAttributes);
	}

	@Override
	public int numberOfFeatures() {
		return currentAttributes.length;
	}

	@Override
	public FeatureSelector build() {
		return build(100);
	}

	@Override
	public FeatureSelector build(int numFeatures) {
		while (currentAttributes.length > numFeatures) {
			step(50,120,2,new NaiveBayseBuilder());
		}
		return this;
	}


}
