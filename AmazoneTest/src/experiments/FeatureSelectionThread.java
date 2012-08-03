package experiments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;

import sample.Sample;
import sample.SamplesManager;
import weka.attributeSelection.InfoGainAttributeEval;
import classifier.Classifier;
import classifier.ClassifierFactory;
import classifier.ClassifierFactory.ClassifierType;
import classifier.Result;
import classifier.SVMWraper;
import feature.selection.FeatureSelector;
import feature.selection.InformationGainBuilder;
import feature.selection.SelectorFactory;
import feature.selection.StochasticBestFirstStep;
import feature.selection.SymmetricalUncertBuilder;
import feature.selection.PCASelectors.RestrictedPCABuilder;

public class FeatureSelectionThread extends Thread {
	private static final String FEATURE_SELECTION_EXP_RESULTS_BAYSE = "experiments/feature_selection/pca_naive_bayse.txt";
	private static final String FEATURE_SELECTION_EXP_RESULTS_LINEAR = "experiments/feature_selection/pca_linear_svm.txt";
	private static final String FEATURE_SELECTION_EXP_RESULTS_HYPERBOLIC = "experiments/feature_selection/pca_hyperbolic_svm.txt";
	private static final String RESULTS_DIR_NAME = "experiments/feature_selection/";
	
	private final SelectorFactory selectorFactory;
	private final File dir;
	private final Map<ClassifierType,BufferedWriter> outMap;
	private final BlockingQueue<Integer> queue;
	public FeatureSelectionThread(SelectorFactory selectorFactory,
			File dir,
			BlockingQueue<Integer> queue,
			Map<ClassifierType,BufferedWriter> outMap
			) { 
		this.selectorFactory = selectorFactory;
		this.dir = dir;
		this.outMap = outMap;
		this.queue = queue;
	}

	@Override
	public void run() {
		super.run();
		try { 
			while (true) {
				Integer _numFeatures = queue.poll();
				if (_numFeatures == null) break;
				int numFeatures = _numFeatures.intValue();
				SamplesManager sm = new SamplesManager();
				FeatureSelector selector = selectorFactory.build(numFeatures);
				for (Entry<ClassifierType,BufferedWriter> next : outMap.entrySet()) {
					Classifier classifier = ClassifierFactory.getClassifier(next.getKey(), sm, dir, selector);
					if (selectorFactory instanceof StochasticBestFirstStep && next.getKey() != ClassifierType.NAIVE_BAYSE) ((SVMWraper)classifier).cancelOptimization();
					Result res = classifier.crossValidation(3);
					String out = "#features=" + selector.numberOfFeatures() + " accuracy=" + res.accuracy() + "\n";
					next.getValue().write(out);
					System.out.print(next.getKey() + ": " + out);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void threadsRunner(int numFeaturesFrom, int numFeaturesTo, int numFeaturesGap,
			int numThreads,	File dataDir, SelectorFactory selectorFactory) throws IOException, FileNotFoundException {
		BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();
		//fill the work queue:
		for (int i = numFeaturesFrom; i <= numFeaturesTo; i += numFeaturesGap)
			queue.add(i);

		// create classifiers and output
		File resFile;
		Map<ClassifierType,BufferedWriter> outMap = new ConcurrentSkipListMap<ClassifierType, BufferedWriter>();
		resFile = new File(FEATURE_SELECTION_EXP_RESULTS_BAYSE);
		if (resFile.exists()) resFile.delete();
		resFile.createNewFile();
		outMap.put(ClassifierType.NAIVE_BAYSE, new BufferedWriter(new FileWriter(resFile)));
		resFile = new File(FEATURE_SELECTION_EXP_RESULTS_LINEAR);
		if (resFile.exists()) resFile.delete();
		resFile.createNewFile();
		outMap.put(ClassifierType.SVM_LINEAR, new BufferedWriter(new FileWriter(resFile)));
		resFile = new File(FEATURE_SELECTION_EXP_RESULTS_HYPERBOLIC);
		if (resFile.exists()) resFile.delete();
		resFile.createNewFile();
		outMap.put(ClassifierType.SVM_HYPERBOLIC, new BufferedWriter(new FileWriter(resFile)));
		
		//the threads list, needed for later join
		FeatureSelectionThread[] threads = new FeatureSelectionThread[numThreads];
		//start all threads:
		for (int i = 0; i < numThreads; i++) { 
			threads[i] = new FeatureSelectionThread(selectorFactory, dataDir, queue, outMap);
			threads[i].start();
		}
		//wait for all threads to finish
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (Entry<ClassifierType,BufferedWriter> next : outMap.entrySet()) {
			next.getValue().close();
		}

	}
	public static void main(String[] args) throws Exception {
		runIG();
		runSU();
		runRestrictedPCA();
		runStochasticBestFirst();
	}

	private static void runIG() throws Exception {
		File dir = new File(RESULTS_DIR_NAME);
		if (!dir.isDirectory()) dir.mkdirs();
		//1:
		threadsRunner(120, 12000, 120, 2, new File(SamplesManager.DATA_DIR), new InformationGainBuilder());
	}
	private static void runSU() throws Exception {
		File dir = new File(RESULTS_DIR_NAME);
		if (!dir.isDirectory()) dir.mkdirs();
		//1:
		threadsRunner(120, 12000, 120, 2, new File(SamplesManager.DATA_DIR), new SymmetricalUncertBuilder());
	}
	private static void runRestrictedPCA() throws IOException, FileNotFoundException {
		File dir = new File(RESULTS_DIR_NAME);
		if (!dir.isDirectory()) dir.mkdirs();
		threadsRunner(400, 12000, 400, 3, new File(SamplesManager.DATA_DIR), new RestrictedPCABuilder() );
	}
	private static void runStochasticBestFirst() throws Exception {
		File dir = new File(RESULTS_DIR_NAME);
		if (!dir.isDirectory()) dir.mkdirs();
		SamplesManager sm = new SamplesManager();
		List<Sample> samples = sm.parseTrainData();
		threadsRunner(12000, 12000, -120, 1, new File(SamplesManager.DATA_DIR), new StochasticBestFirstStep(samples,new Random(1), new InfoGainAttributeEval()) );
	}

}
