package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import classifier.Result;
import classifier.SVMWraper;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;


import crawler.amazon.files_creator.DataFilesCreator;
import feature.selection.FeatureSelector;
import feature.selection.InformationGainBuilder;
import feature.selection.InformationGainFeatureSelector;
import feature.selection.PCASelectors.DFSelector;
import feature.selection.PCASelectors.DFSelectorBuilder;
import feature.selection.PCASelectors.DoubleFeatureSelector;
import feature.selection.PCASelectors.PCABuilder;
import feature.selection.PCASelectors.PCASelector;
import feature.selection.PCASelectors.RestrictedPCASelector;
import feature.selection.notUsed.GeneticCFSFeatureSelector;

public class SamplesManager {

	private FeatureSelector featureSelector;
	public static final String DATA_DIR = "data/lenses/temp";
	private static final int DATA_DOC_IDX = 0;
	private static final int DATA_WORD_IDX = 1;
	private static final int DATA_COUNT_IDX = 2;
	public static final String CLASSIFICATION_FILE_NAME = "train.label";
	public static final String DATA_FILE_NAME = "train.data";
	public static final String TEST_CLASSIFICATION_FILE_NAME = "test.label";
	public static final String TEST_DATA_FILE_NAME = "test.data";
	private static final int MAGIC = 20000;
	private Map<Integer,Integer> idfMap;


	public SamplesManager() {
		idfMap = new HashMap<Integer, Integer>();
	}

	/**
	 * parses a train data, from the default dir and file names
	 * It also has side affect of modifying the idf list according to the data.
	 * @return a samples list
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<Sample> parseTrainData() throws FileNotFoundException, IOException {
		File dir = new File(DATA_DIR);
		return parseTrainData(dir,DATA_FILE_NAME,CLASSIFICATION_FILE_NAME,FeatureSelector.NONE);
	}
	/**
	 * parses a train data from the denoted dir and file names.
	 * It also has side affect of modifying the idf list according to the data.
	 * @param dir
	 * @param dataFileName
	 * @param classificationFileName
	 * @return a samples list 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<Sample> parseTrainData(File dir, String dataFileName, String classificationFileName,FeatureSelector featureSelector) throws FileNotFoundException, IOException {
		if (!dir.isDirectory()) throw new FileNotFoundException("The data dir was not found");
		File dataFile = new File(dir,dataFileName);
		if (!dataFile.exists()) throw new FileNotFoundException("The data file was not found");
		File labelFile = new File(dir,classificationFileName);
		if (!labelFile.exists()) throw new FileNotFoundException("The classificatrion file was not found");

		populateMaxOccurancesMap(dataFile);
		populateMinOccurancesMap(dataFile);

		populateHistogram(dataFile);
		List<Sample> samples = createSamples(dataFile,labelFile);
		this.featureSelector = featureSelector;
		return featureSelector.selectFeatresFromTrain(samples);
	}

	private Map<Integer,Integer> maxMap = new HashMap<Integer, Integer>();
	private void populateMaxOccurancesMap(File dataFile) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		String line;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.length() == 0) continue;
			String[] arr = line.split("\\s");
			Integer wordId = new Integer(arr[DATA_WORD_IDX]);
			Integer occs = maxMap.get(wordId);
			Integer currOccs = new Integer(arr[DATA_COUNT_IDX]);
			maxMap.put(wordId, occs == null? currOccs : Math.max(currOccs,occs));
		}
	}
	private Map<Integer,Integer> minMap = new HashMap<Integer, Integer>();
	private void populateMinOccurancesMap(File dataFile) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		String line;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.length() == 0) continue;
			String[] arr = line.split("\\s");
			Integer wordId = new Integer(arr[DATA_WORD_IDX]);
			Integer occs = minMap.get(wordId);
			Integer currOccs = new Integer(arr[DATA_COUNT_IDX]);
			minMap.put(wordId, occs == null? currOccs : Math.min(currOccs,occs));
		}
	}

	/**
	 * creates a list of test samples. The parser does not add the new attributes to the idf counter.
	 * It parses the data from the default files and dir. 
	 * It must be called after parseTrainDate()
	 * @return a samples list
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws IllegalStateException if parseTestData() was called before parseTrainData()
	 */
	public List<Sample> parseTestData() throws FileNotFoundException, IOException {
		File dir = new File(DATA_DIR);
		return parseTestData(dir,TEST_DATA_FILE_NAME,TEST_CLASSIFICATION_FILE_NAME);
	}

	/**
	 * creates a list of test samples. The parser does not add the new attributes to the idf counter.
	 * It must be called after parseTrainDate() 
	 * @param dir
	 * @param dataFileName
	 * @param classificationFileName
	 * @return a samples list
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws IllegalStateException if parseTestData() was called before parseTrainData()
	 */
	public List<Sample> parseTestData(File dir, String dataFileName, String classificationFileName) throws FileNotFoundException, IOException, IllegalStateException  {
		if (featureSelector == null) throw new IllegalStateException("parseTrainData() must be called befoe parseTestData()");
		if (!dir.isDirectory()) throw new FileNotFoundException("The data dir was not found");
		File dataFile = new File(dir,dataFileName);
		if (!dataFile.exists()) throw new FileNotFoundException("The data file was not found");
		File labelFile = new File(dir,classificationFileName);
		if (!labelFile.exists()) throw new FileNotFoundException("The classificatrion file was not found");
		List<Sample> samples = createSamples(dataFile,labelFile);
		return featureSelector.filterFeaturesFromTest(samples);

	}
	/**
	 * Given a data file and a label file, it gets all samples from these files, including calculating tf-idf 
	 * @param dataFile The file representiong the data (.data file)
	 * @param labelFile The file representing the labels (.label file)
	 * @return a list of samples parsed by the method
	 * @throws IOException
	 */
	private List<Sample> createSamples(File dataFile, File labelFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		Scanner sc = new Scanner(labelFile);
		String line;
		List<Sample> samples = new ArrayList<Sample>();
		int currDoc = 1;
		List<Attribute> attributes  = new ArrayList<Attribute>();
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.length() == 0) continue;
			String[] arr = line.split("\\s");
			int newCurrDoc = Integer.parseInt(arr[DATA_DOC_IDX]);
			if (newCurrDoc != currDoc) {
				int label = sc.nextInt();
				Sample sample = new Sample(attributes, label);
				samples.add(sample);
				attributes = new ArrayList<Attribute>();
				currDoc = newCurrDoc;
			}
			int wordId = Integer.parseInt(arr[DATA_WORD_IDX]);
			int count = Integer.parseInt(arr[DATA_COUNT_IDX]);
			double value = idfValue(wordId);
			//if the attribute is unknown, ignore it.
			if (value < 0) continue;
			value *= normalize(count,wordId);
			Attribute attribute = new Attribute(wordId,value);
			attributes.add(attribute);
		}
		//handle last doc:
		int label = sc.nextInt();
		Sample sample = new Sample(attributes, label);
		samples.add(sample);

		return samples;
	}

	private double normalize(int count, int wordId) {
		int max = maxMap.get(wordId);
		int min = minMap.get(wordId);
		if (max == min || count > max) return 1;
		double d  = count;
		d -= (min-1);
		d /= (max - (min - 1) );
		return d;

	}

	private void populateHistogram(File dataFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		String line;
		while ((line = br.readLine()) != null) { 
			line = line.trim();
			if (line.length() == 0) continue;
			String[] arr = line.split("\\s");
			Integer docId = new Integer(arr[DATA_WORD_IDX]);
			Integer count = idfMap.get(docId);
			idfMap.put(docId, count == null? 1 : count + 1);
		}
	}
	/**
	 * finds the idf value of a given doc. Note: The idf value is calculated ONLY on the train data.
	 * @param docId
	 * @return the idf value of this document
	 */
	private double idfValue(int docId) {
		double d  = idfMap.size();
		Integer count = idfMap.get(docId);
		if (count == null || count == 0) return -1;
		d /= count;
		if (true) return 1; //TODO: no commit
		return Math.log(d);
	}
	/**
	 * 
	 * @return
	 * @throws IllegalStateException if parseTrainData() was not already invoked
	 */
	public int numAttributes() { 
		if (featureSelector == null) throw new IllegalStateException("Must invoke parseTrainData() first");
		return featureSelector.numberOfFeatures();
	}

	public static List<Sample> reduceDimensions(List<Sample> samples, int[] selectedFeatures) {
		List<Sample> result = new ArrayList<Sample>();
		for (Sample s : samples) {
			List<Attribute> atts = new ArrayList<Attribute>();
			for (Attribute att : s.attributes) {
				int idx = Arrays.binarySearch(selectedFeatures, att.attributeNumber);
				if (idx >= 0) atts.add(new Attribute(att.attributeNumber, att.value));
			}
			result.add(new Sample(atts, s.classification));
		}
		return result;
	}

	public static List<Sample> asSamplesList(Instances instances) {
		return asSamplesList(instances,true);
	}

	public static List<Sample> asSamplesList(Instances instances, boolean b) {
		List<Sample> samples = new ArrayList<Sample>();
		Iterator<Instance> iter = instances.iterator();
		while (iter.hasNext()) {
			Instance curr = iter.next();
			List<Attribute> attributes = new ArrayList<Attribute>();
			for (int i = 0; i < curr.numValues(); i++) {
				if (curr.index(i) == curr.classIndex()) continue;
				int idx = curr.index(i);
				double value = curr.value(curr.index(i));
				Attribute attribute = new Attribute(
						b?Integer.parseInt(curr.attribute(idx).name()):idx + MAGIC,value);
				attributes.add(attribute);
			}
			samples.add(new Sample(attributes,(int)Math.round(curr.classValue())+1));
		}
		return samples;
	}
	public static List<Sample> uniteAttributes(List<Sample> samples,
			List<Sample> firstListsSamples) {
		List<Sample> res = new ArrayList<Sample>();
		Iterator<Sample> iter1 = samples.iterator();
		Iterator<Sample> iter2 = firstListsSamples.iterator();
		while (iter1.hasNext() && iter2.hasNext()) {
			List<Attribute> atts = new ArrayList<Attribute>();
			Sample s1 = iter1.next();
			Sample s2 = iter2.next();
			for (Attribute att : s1.attributes) {
				atts.add(att);
			}
			for (Attribute att : s2.attributes) {
				atts.add(att);
			}
			res.add(new Sample(atts, s1.classification));
			if (s1.classification != s2.classification) throw new RuntimeException("god damnit, not the same order");
		}
		return res;
	}
	public static Instances asWekaInstances(List<Sample> samples) {
		Set<Integer> set = new HashSet<Integer>();
		for (Sample s : samples) 
			for (Attribute a : s.attributes)
				set.add(a.attributeNumber);
		
		int numAtt = set.size();

		//		Set<Integer> attributes = new HashSet<Integer>();
		//		for (Sample s : samples) 
		//			for (Attribute att : s.attributes)
		//				attributes.add(att.attributeNumber);

		//		Map<Integer,Integer> id2ind = new HashMap<Integer, Integer>();
		ArrayList<weka.core.Attribute> instancesAtt = new ArrayList<weka.core.Attribute>();
		//		Integer j = 0;
		//		for (Integer i : attributes) {
		for (int i = 0; i<numAtt; i++) {
			weka.core.Attribute newAtt = new weka.core.Attribute(Integer.toString(i));
			//			id2ind.put(i, j);
			//			instancesAtt.add(j++,newAtt);
			instancesAtt.add(i,newAtt);

		}
		List<String> labels = new LinkedList<String>();
		labels.add("1");
		labels.add("2");
		labels.add("3");

		instancesAtt.add(numAtt, new weka.core.Attribute("class",labels));

		Instances data = new Instances("trainData", instancesAtt, samples.size());

		data.setClass(instancesAtt.get(numAtt));
		for (Sample s : samples) {
			int size = 0;
			for (Attribute a : s.attributes) {
				if (a.attributeNumber < numAtt) size++;
			}
			size++;
			int attIndex[] = new int[size]; 
			double attVals[] = new double[size];
			int location = 0;
			for (Attribute nextAtt : s.attributes) {
				//				Integer newIndex = id2ind.get(nextAtt.attributeNumber);
				//				if (newIndex == null) continue;
				if (nextAtt.attributeNumber >= numAtt) continue;
				//				attIndex[location] = newIndex; 
				attIndex[location] = nextAtt.attributeNumber; 
				attVals[location] = nextAtt.value;
				location++;
			}
			//			attIndex[location] = j; 
			attIndex[location] = numAtt; 
			attVals[location] = s.classification-1;
			data.add(new SparseInstance(1.0, attVals, attIndex, size));
		}
		return data;
	}
	public static Instances asWekaInstances(List<Sample> samples,int numFeatures) {

		int numAtt = numFeatures;


		//		Map<Integer,Integer> id2ind = new HashMap<Integer, Integer>();
		ArrayList<weka.core.Attribute> instancesAtt = new ArrayList<weka.core.Attribute>();
		//		Integer j = 0;
		//		for (Integer i : attributes) {
		for (int i = 0; i<numAtt; i++) {
			weka.core.Attribute newAtt = new weka.core.Attribute(Integer.toString(i));
			//			id2ind.put(i, j);
			//			instancesAtt.add(j++,newAtt);
			instancesAtt.add(i,newAtt);

		}
		List<String> labels = new LinkedList<String>();
		labels.add("1");
		labels.add("2");
		labels.add("3");

		instancesAtt.add(numAtt, new weka.core.Attribute("class",labels));

		Instances data = new Instances("testData", instancesAtt, samples.size());

		data.setClass(instancesAtt.get(numAtt));
		for (Sample s : samples) {
			int size = 0;
			for (Attribute a : s.attributes) {
				if (a.attributeNumber < numAtt) size++;
			}
			size++;
			int attIndex[] = new int[size]; 
			double attVals[] = new double[size];
			int location = 0;
			for (Attribute nextAtt : s.attributes) {
				if (nextAtt.attributeNumber >= numAtt) continue;
				attIndex[location] = nextAtt.attributeNumber; 
				attVals[location] = nextAtt.value;
				location++;
			}
			//			attIndex[location] = j; 
			attIndex[location] = numAtt; 
			attVals[location] = s.classification-1;
			data.add(new SparseInstance(1.0, attVals, attIndex, size));
		}
		return data;
	}
	public static int[] listAttributes(List<Sample> samples) {
		NavigableSet<Integer> set = new TreeSet<Integer>();
		for (Sample s : samples) { 
			for (Attribute att : s.attributes) {
				set.add(att.attributeNumber);
			}
		}
		int[] arr = new int[set.size()];
		int i = 0;
		for (Integer x : set) { 
			arr[i++] = x.intValue();
		}
		return arr;
	}
	public static void main(String[] args) throws Exception {
		int NUM_ATTS = 3000;
		
		SamplesManager sm = new SamplesManager();
		List<Sample> l1 = sm.parseTrainData();
		List<Sample> l2 = sm.parseTestData();
		Instances train = asWekaInstances(l1);
		Instances test = asWekaInstances(l2, train.numAttributes()-1);

		AttributeSelection filter = new AttributeSelection();  // package weka.filters.supervised.attribute!
		InfoGainAttributeEval eval = new InfoGainAttributeEval();
		Ranker search = new Ranker();
//		search.setNumToSelect(NUM_ATTS);
		filter.setEvaluator(eval);
		filter.setSearch(search);
		
//		filter.SelectAttributes(train);
//		train = filter.reduceDimensionality(train);
//		test = filter.reduceDimensionality(test);
//		System.out.println("num features=" + filter.numberAttributesSelected());
//		System.out.println();
//		
//		l1 = SamplesManager.asSamplesList(train);
//		l2 = SamplesManager.asSamplesList(test);
//		SVMWraper wrapper = new SVMWraper(l1, l2);
//		wrapper.setHyperbolic();
//		Result res = wrapper.trainTest();
//		
//		System.out.println("hyperbolic");
//		System.out.println(res.accuracy());
//		System.out.println(res.confMatString());
//		System.out.println(Arrays.toString(res.correctArr));
		
		Classifier classifier = new NaiveBayesMultinomial();
		classifier.buildClassifier(train);
		int numSamples = 0;
		int numCorrect = 0;
		boolean[] correctnessVector = new boolean[test.numInstances()];
		int[][] confusion = new int[3][3];
		int i = 0;
		for (Instance inst : test) {
			numSamples++;
			if (Double.compare(inst.classValue(), classifier.classifyInstance(inst))== 0) {
				numCorrect++;
				correctnessVector[i++] = true;
			} else correctnessVector[i++] = false;
			confusion[(int)Math.round(inst.classValue())][(int)Math.round(classifier.classifyInstance(inst))] += 1;
		}
		for (int[] arr : confusion) {
			System.out.println(Arrays.toString(arr));
		}
		System.out.println(numSamples);
		System.out.println(numCorrect);
		System.out.println(((double)numCorrect)/numSamples);
		System.out.println(Arrays.toString(correctnessVector));
	}


}
