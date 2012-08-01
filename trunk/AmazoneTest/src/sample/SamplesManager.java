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

import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;


import crawler.amazon.files_creator.DataFilesCreator;
import feature.selection.DFSelectorBuilder;
import feature.selection.DoubleFeatureSelector;
import feature.selection.FeatureSelector;
import feature.selection.GeneticCFSFeatureSelector;
import feature.selection.InformationGainBuilder;
import feature.selection.InformationGainFeatureSelector;
import feature.selection.PCABuilder;
import feature.selection.PCASelector;

public class SamplesManager {

	private FeatureSelector featureSelector;
	public static final String DATA_DIR = DataFilesCreator.RESULT_DIR_NAME;
	private static final int DATA_DOC_IDX = 0;
	private static final int DATA_WORD_IDX = 1;
	private static final int DATA_COUNT_IDX = 2;
	public static final String CLASSIFICATION_FILE_NAME = "train.label";
	public static final String DATA_FILE_NAME = "train.data";
	public static final String TEST_CLASSIFICATION_FILE_NAME = "test.label";
	public static final String TEST_DATA_FILE_NAME = "test.data";
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
				if (idx >= 0) atts.add(new Attribute(idx, att.value));
			}
			result.add(new Sample(atts, s.classification));
		}
		return result;
	}
	
	public static List<Sample> asSamplesList(Instances instances) {
		List<Sample> samples = new ArrayList<Sample>();
		Iterator<Instance> iter = instances.iterator();
		while (iter.hasNext()) {
			Instance curr = iter.next();
			List<Attribute> attributes = new ArrayList<Attribute>();
			for (int i = 0; i < curr.numValues(); i++) {
				if (curr.index(i) == curr.classIndex()) continue;
				int idx = curr.index(i);
				double value = curr.value(curr.index(i));
				Attribute attribute = new Attribute(Integer.parseInt(curr.attribute(idx).name()),value);
				attributes.add(attribute);
			}
			samples.add(new Sample(attributes,(int)Math.round(curr.classValue())+1));
		}
		return samples;
	}
	public static Instances asWekaInstances(List<Sample> samples) {

		Set<Integer> attributes = new HashSet<Integer>();
		for (Sample s : samples) 
			for (Attribute att : s.attributes)
				attributes.add(att.attributeNumber);
		
		int numAtts = attributes.size();
		ArrayList<weka.core.Attribute> atts = new ArrayList<weka.core.Attribute>();
		for (int i = 0; i < numAtts; i++) {
			atts.add(i,new weka.core.Attribute(Integer.toString(i)));
		}
		List<String> labels = new LinkedList<String>();
		labels.add("1");
		labels.add("2");
		labels.add("3");

		atts.add(numAtts,new weka.core.Attribute("class",labels));

		Instances data = new Instances("trainData", atts, samples.size());

		data.setClass(atts.get(numAtts));
		for (Sample s : samples) {
			int size = s.attributes.size()+1;
			int attIndex[] = new int[size]; 
			double attVals[] = new double[size];
			int location = 0;
			for (sample.Attribute nextAtt : s.attributes) {
				if (nextAtt.attributeNumber>=numAtts) continue;
				attIndex[location] = nextAtt.attributeNumber; 
				attVals[location] = nextAtt.value;
				location++;
			}
			attIndex[location] = numAtts; 
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
		SamplesManager sm = new SamplesManager();
		long start = System.currentTimeMillis();
		List<Sample> l = sm.parseTrainData(new File(DATA_DIR),DATA_FILE_NAME,CLASSIFICATION_FILE_NAME,new DoubleFeatureSelector(new InformationGainBuilder(), new PCABuilder(),100));
		System.out.println(((double)System.currentTimeMillis()-start)/60000);
		NavigableSet<Integer> set = new TreeSet<Integer>();
		for (Sample s : l) { 
			for (Attribute a : s.attributes) {
				set.add(a.attributeNumber);
			}
		}
		System.out.println(set.size());
	}

}
