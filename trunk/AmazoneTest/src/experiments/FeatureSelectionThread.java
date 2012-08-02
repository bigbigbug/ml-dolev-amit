package experiments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
import feature.selection.FeatureSelector;
import feature.selection.InformationGainBuilder;
import feature.selection.SelectorFactory;
import feature.selection.SymmetricalUncertBuilder;
import feature.selection.PCASelectors.DFSelectorBuilder;
import feature.selection.PCASelectors.DoubleSelectorBuilder;
import feature.selection.PCASelectors.PCABuilder;

public class FeatureSelectionThread extends Thread {
	private static final String FEATURE_SELECTION_EXP_RESULTS_BAYSE = "experiments/feature_selection/double_df_naive_bayse.txt";
	private static final String FEATURE_SELECTION_EXP_RESULTS_LINEAR = "experiments/feature_selection/double_df_linear_svm.txt";
	private static final String FEATURE_SELECTION_EXP_RESULTS_HYPERBOLIC = "experiments/feature_selection/double_df_hyperbolic_svm.txt";
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
					Result res = classifier.crossValidation(3);
					next.getValue().write("#features=" + numFeatures + " accuracy=" + res.accuracy() + "\n");
					System.out.println(next.getKey() + ": #features=" + numFeatures + " accuracy=" + res.accuracy() + "\n");
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
		
//		
//		
//		BufferedWriter bw = new BufferedWriter(new FileWriter(resFile));
//		for (Entry<Integer, Double> entry : map.entrySet()) { 
//			bw.write("#features=" + entry.getKey() + " accuracy=" + entry.getValue() + "\n");
//			System.out.println("#features=" + entry.getKey() + " accuracy=" + entry.getValue() + "\n");
//		}
//		bw.close();

	}
	public static void main(String[] args) throws Exception {
//		runIG();
//		runSU();
		runDFPCA();
	}

	private static void runIG() throws IOException, FileNotFoundException {
		File dir = new File(RESULTS_DIR_NAME);
		if (!dir.isDirectory()) dir.mkdirs();
		//1:
		threadsRunner(120, 12000, 120, 2, new File(SamplesManager.DATA_DIR), new InformationGainBuilder());
	}
	private static void runSU() throws IOException, FileNotFoundException {
		File dir = new File(RESULTS_DIR_NAME);
		if (!dir.isDirectory()) dir.mkdirs();
		//1:
		threadsRunner(120, 12000, 120, 2, new File(SamplesManager.DATA_DIR), new SymmetricalUncertBuilder());
	}
	private static void runDFPCA() throws IOException, FileNotFoundException {
		File dir = new File(RESULTS_DIR_NAME);
		if (!dir.isDirectory()) dir.mkdirs();
		//1:
		threadsRunner(120, 12000, 120, 2, new File(SamplesManager.DATA_DIR), new DoubleSelectorBuilder(new DFSelectorBuilder(), new PCABuilder()) );
	}

}
