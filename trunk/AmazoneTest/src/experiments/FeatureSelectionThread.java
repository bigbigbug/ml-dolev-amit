package experiments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import classifier.Classifier;
import classifier.ClassifierFactory;
import classifier.ClassifierFactory.ClassifierType;
import classifier.Result;

import sample.SamplesManager;

import feature.selection.FeatureSelector;
import feature.selection.InformationGainBuilder;
import feature.selection.SelectorFactory;

public class FeatureSelectionThread extends Thread {
	private static final String FEATURE_SELECTION_EXP_DIR = "experiments/feature_selection";
	//	
	private final SelectorFactory selectorFactory;
	private final File dir;
	private final ClassifierType type;
	private final int numFeaturesFrom;
	private final int numFeaturesTo;
	private final int numFeaturesGap;
	private final BufferedWriter writer;
	public FeatureSelectionThread(SelectorFactory selectorFactory,
			ClassifierType type, 
			File dir,
			int numFeaturesFrom,
			int numFeaturesTo,
			int numFeaturesGap,
			BufferedWriter writer
			) { 
		this.selectorFactory = selectorFactory;
		this.dir = dir;
		this.type = type;
		this.numFeaturesFrom = numFeaturesFrom;
		this.numFeaturesTo = numFeaturesTo;
		this.numFeaturesGap = numFeaturesGap;
		this.writer = writer;
	}

	@Override
	public void run() {
		super.run();
		try { 
			for (int x = numFeaturesFrom; x < numFeaturesTo; x += numFeaturesGap) {
				SamplesManager sm = new SamplesManager();
				FeatureSelector selector = selectorFactory.build(x);
				Classifier classifier = ClassifierFactory.getClassifier(type, sm, dir, selector);
				Result res = classifier.crossValidation(3);

				//TODO: remove and switch to file write
				writer.write("#features=" + x + " accuracy=" + res.accuracy() + "\n");
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public static void threadsRunner(int numFeaturesFrom, int numFeaturesTo, int numFeaturesGap,
			int numThreads,	ClassifierType type,File dataDir,File outputDir,SelectorFactory selectorFactory) throws IOException, FileNotFoundException {
		int totalWork = (numFeaturesTo - numFeaturesFrom) / numFeaturesGap;
		totalWork += 1;
		int work = totalWork / numThreads;
		work *= numFeaturesGap;
		System.out.println(work);
		FeatureSelectionThread[] threads = new FeatureSelectionThread[numThreads];
		if (outputDir.isDirectory() == false) outputDir.mkdirs();
		for (int i = 0; i < numThreads; i++) { 
			String fileName = "_tmp_" + i;
			File file = new File(outputDir,fileName);
			if (file.exists()) file.delete();
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			threads[i] = new FeatureSelectionThread(selectorFactory, type, dataDir, numFeaturesFrom + i*work, 
					(i==numThreads-1 ? numFeaturesTo : numFeaturesFrom + work*(i+1))
					, numFeaturesGap,bw);
			threads[i].start();
		}
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		File[] files = outputDir.listFiles();
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				return f1.getName().compareTo(f2.getName());
			}
		});
		File resFile = new File(outputDir,"accuracy_results.txt");
		if (resFile.exists()) resFile.delete();
		resFile.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(resFile));
		for (File file : files) { 
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				bw.write(line + "\n");
			}
		}
		bw.close();

	}
	public static void main(String[] args) throws Exception {
		threadsRunner(50, 4000, 100, 2, ClassifierType.SVM_LINEAR, new File(SamplesManager.DATA_DIR), new File(FEATURE_SELECTION_EXP_DIR), new InformationGainBuilder());
	}

}
