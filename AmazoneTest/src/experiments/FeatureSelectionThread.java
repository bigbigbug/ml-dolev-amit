package experiments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;

import sample.SamplesManager;
import classifier.Classifier;
import classifier.ClassifierFactory;
import classifier.ClassifierFactory.ClassifierType;
import classifier.Result;
import feature.selection.DFSelectorBuilder;
import feature.selection.DoubleSelectorBuilder;
import feature.selection.FeatureSelector;
import feature.selection.PCABuilder;
import feature.selection.ReliefFBuilder;
import feature.selection.SelectorFactory;

public class FeatureSelectionThread extends Thread {
	private static final String FEATURE_SELECTION_EXP_RESULTS_BAYSE = "experiments/feature_selection/double_df_naive_bayse.txt";
	private static final String FEATURE_SELECTION_EXP_RESULTS_LINEAR = "experiments/feature_selection/double_df_linear_svm.txt";
	private static final String FEATURE_SELECTION_EXP_RESULTS_HYPERBOLIC = "experiments/feature_selection/double_df_hyperbolic_svm.txt";
	private static final String RESULTS_DIR_NAME = "experiments/feature_selection/";
	
	private final SelectorFactory selectorFactory;
	private final File dir;
	private final ClassifierType type;
	private final ConcurrentMap<Integer,Double> resultsMap;
	private final BlockingQueue<Integer> queue;
	public FeatureSelectionThread(SelectorFactory selectorFactory,
			ClassifierType type, 
			File dir,
			BlockingQueue<Integer> queue,
			ConcurrentNavigableMap<Integer,Double> resultsMap
			) { 
		this.selectorFactory = selectorFactory;
		this.dir = dir;
		this.type = type;
		this.resultsMap = resultsMap;
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
				Classifier classifier = ClassifierFactory.getClassifier(type, sm, dir, selector);
				Result res = classifier.crossValidation(3);
				resultsMap.put(numFeatures, res.accuracy());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void threadsRunner(int numFeaturesFrom, int numFeaturesTo, int numFeaturesGap,
			int numThreads,	ClassifierType type,File dataDir,File resFile,SelectorFactory selectorFactory) throws IOException, FileNotFoundException {
		BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();
		//fill the work queue:
		for (int i = numFeaturesFrom; i <= numFeaturesTo; i += numFeaturesGap)
			queue.add(i);
		//the map that will hold the results:
		ConcurrentNavigableMap<Integer, Double> map = new ConcurrentSkipListMap<Integer, Double>();
		//the threads list, needed for later join
		FeatureSelectionThread[] threads = new FeatureSelectionThread[numThreads];
		//start all threads:
		for (int i = 0; i < numThreads; i++) { 
			threads[i] = new FeatureSelectionThread(selectorFactory, type, dataDir, queue, map);
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

		if (resFile.exists()) resFile.delete();
		resFile.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(resFile));
		for (Entry<Integer, Double> entry : map.entrySet()) { 
			bw.write("#features=" + entry.getKey() + " accuracy=" + entry.getValue() + "\n");
			System.out.println("#features=" + entry.getKey() + " accuracy=" + entry.getValue() + "\n");
		}
		bw.close();

	}
	public static void main(String[] args) throws Exception {
		File dir = new File(RESULTS_DIR_NAME);
		if (!dir.isDirectory()) dir.mkdirs();
		//1:
		threadsRunner(120, 12000, 120, 2, ClassifierType.SVM_HYPERBOLIC, new File(SamplesManager.DATA_DIR), new File(FEATURE_SELECTION_EXP_RESULTS_HYPERBOLIC), new DoubleSelectorBuilder(new DFSelectorBuilder(), new PCABuilder()));
		//2:
		threadsRunner(120, 12000, 120, 2, ClassifierType.SVM_LINEAR, new File(SamplesManager.DATA_DIR), new File(FEATURE_SELECTION_EXP_RESULTS_LINEAR), new DoubleSelectorBuilder(new DFSelectorBuilder(), new PCABuilder()));
		//3:
		threadsRunner(120, 12000, 120, 2, ClassifierType.NAIVE_BAYSE, new File(SamplesManager.DATA_DIR), new File(FEATURE_SELECTION_EXP_RESULTS_BAYSE), new DoubleSelectorBuilder(new DFSelectorBuilder(), new PCABuilder()) );
	}

}
