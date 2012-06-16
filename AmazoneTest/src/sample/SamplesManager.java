package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import crawler.amazon.files_creator.DataFilesCreator;

public class SamplesManager {
	public static final String DATA_DIR = DataFilesCreator.RESULT_DIR_NAME;
	private static final int DATA_DOC_IDX = 0;
	private static final int DATA_WORD_IDX = 1;
	private static final int DATA_COUNT_IDX = 2;
	private static final String CLASSIFICATION_FILE_NAME = "train.label";
	private static final String DATA_FILE_NAME = "train.data";
	private static SamplesManager INSTANCE;
	private Map<Integer,Integer> idfMap;
	private SamplesManager() {
		idfMap = new HashMap<Integer, Integer>();
	}

	/**
	 * @return an instance of the SamplesManager object
	 */
	public static SamplesManager getInstance() { 
		if (INSTANCE == null) INSTANCE = new SamplesManager();
		return INSTANCE;
	}
	/**
	 * parses a train data, from the default dir and file names
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<Sample> parseTrainData() throws FileNotFoundException, IOException {
		File dir = new File(DATA_DIR);
		return parseTrainData(dir,DATA_FILE_NAME,CLASSIFICATION_FILE_NAME);
	}
	/**
	 * parses a train data from the denoted dir and file names.
	 * @param dir
	 * @param dataFileName
	 * @param classificationFileName
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<Sample> parseTrainData(File dir, String dataFileName, String classificationFileName) throws FileNotFoundException, IOException {
		if (!dir.isDirectory()) throw new FileNotFoundException("The data dir was not found");
		File dataFile = new File(dir,dataFileName);
		if (!dataFile.exists()) throw new FileNotFoundException("The data file was not found");
		File labelFile = new File(dir,classificationFileName);
		if (!labelFile.exists()) throw new FileNotFoundException("The classificatrion file was not found");
		populateHistogram(dataFile);
		List<Sample> samples = createSamples(dataFile,labelFile);

		return samples;
	}
	/**
	 * Given a data file and a label file, it gets all samples from these files, including calculating tf-idf 
	 * @param dataFile The file representiong the data (.data file)
	 * @param labelFile The file representing the labels (.label file)
	 * @return a list of samples parsed by the method
	 * @throws IOException
	 */
	public List<Sample> createSamples(File dataFile, File labelFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		Scanner sc = new Scanner(labelFile);
		String line;
		List<Sample> samples = new ArrayList<Sample>();
		int currDoc = -1;
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
			int docId = Integer.parseInt(arr[DATA_WORD_IDX]);
			int count = Integer.parseInt(arr[DATA_COUNT_IDX]);
			double value = idfValue(docId);
			//if the attribute is unknown, ignore it.
			if (value < 0) continue;
			value *= count;
			Attribute attribute = new Attribute(docId,value);
			attributes.add(attribute);
		}
		return samples;
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
	public double idfValue(int docId) {
		double d  = idfMap.size();
		Integer count = idfMap.get(docId);
		if (count == 0) return -1;
		d /= count;
		return Math.log(d);
	}
	
	public static void main(String[] args) throws Exception {
		SamplesManager sm = SamplesManager.getInstance();
		List<Sample> l = sm.parseTrainData();
		System.out.println(l.size());
	}
}
