package crawler.amazon.files_creator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import crawler.amazon.files_creator.Review.Classification;


public class DataFilesCreator {

	/**
	 * @param args
	 */
	public static final String RAW_DIR_NAME = "data/lenses/raw";
	public static final String RESULT_DIR_NAME = "data/lenses/processed";
	public static final long SEED = 5;
	public static final int NUM_REVIEWS = 3200;
	public static final double PER_PRO = 0.3333;
	public static final double PER_AGAINST = 0.3333;
	public static final double PER_NEUTRAL = 0.3333;
	private static final double PERCENT_TEST = 0.3;
	
	private List<Review> against;
	private List<Review> neutrals;
	private List<Review> pros;
	private Set<Review> allReviews;
	
	public static void main(String[] args) throws Exception {
		File dir = new File(RAW_DIR_NAME);
		if (!dir.isDirectory()) throw new FileNotFoundException("The data should be in data/lenses");
		File resultDir = new File(RESULT_DIR_NAME);
		if (!resultDir.isDirectory()) resultDir.mkdirs(); 
		DataFilesCreator creator = new DataFilesCreator();
		creator.parse(dir, resultDir);
		
	}
	
	public void parse(File rawDataDir,File resultDir) throws IOException, FileNotFoundException , ParseException {
		File[] files = rawDataDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".csv");
			}
		});
		against = new ArrayList<Review>();
		neutrals = new ArrayList<Review>();
		pros = new ArrayList<Review>();
		allReviews = new HashSet<Review>();
		for (File f : files) { 
			File descriptionFile = new File(f.getAbsolutePath().replace("_reviews.csv","_desc.txt"));
			Description desc = new Description(descriptionFile);
			if (desc.title == null) continue;
			addReveiwsToList(f,desc);
		}
		Random r = new Random(SEED);
		List<Review> reviews = choseRandomReviews(NUM_REVIEWS,r);
//		createMaps(reviews);
		writeFiles(reviews,r,resultDir);
	}
	private void writeFiles(List<Review> reviews,Random r,File outDir) throws IOException  {
		Collections.shuffle(reviews,r);
		int numTestFiles = (int)(((double)NUM_REVIEWS) * PERCENT_TEST);
		//first create the map for the train reviews, ensuring #features(train) >= max{id(feature) | all features in train}:
		createMaps(reviews.subList(numTestFiles,reviews.size()));
		//then add the test features 
		createMaps(reviews.subList(0,numTestFiles));
		//then, create files:
		createFiles(reviews.subList(numTestFiles,reviews.size()),"train",outDir);
		createFiles(reviews.subList(0,numTestFiles),"test",outDir);
	}
	private void createFiles(List<Review> subList, String fileName, File outDir) throws IOException {
		File dataFile = new File(outDir,fileName + ".data");
		if (dataFile.exists()) dataFile.delete();
		dataFile.createNewFile();
		BufferedWriter data = new BufferedWriter(new FileWriter(dataFile));
		File labelFile = new File(outDir,fileName + ".label");
		if (labelFile.exists()) labelFile.delete();
		labelFile.createNewFile();
		BufferedWriter label = new BufferedWriter(new FileWriter(labelFile));
		File mapFile = new File(outDir,fileName + ".map");
		if (mapFile.exists()) mapFile.delete();
		mapFile.createNewFile();
		BufferedWriter map = new BufferedWriter(new FileWriter(mapFile));
		int i = 1;
		for (Review rev : subList) { 
			writeLabel(rev,label);
			writeData(rev,data,i++);
		}
		writeMap(map);

		data.close();
		label.close();
		map.close();
		System.out.println(dataFile.getAbsolutePath());
	}
	private void writeLabel(Review rev, BufferedWriter label) throws IOException {
		label.write("" + rev.classification.ordinal() + "\n");
	}

	private void writeMap(BufferedWriter map) throws IOException {
		map.write("against 1\n");
		map.write("neutral 2\n");
		map.write("pro 3\n");
		
	}

	private void writeData(Review rev, BufferedWriter data, int i) throws IOException {
		for (Entry<String,Integer> e : rev.reviewWords.entrySet()) { 
			data.write(i + " " + stringToId.get(e.getKey()) + " " + e.getValue() + "\n");
		}
		for (Entry<String,Integer> e : rev.titleWords.entrySet()) { 
			data.write(i + " " + stringToId.get(e.getKey()) + " " + e.getValue() + "\n");
		}
		
	}
	private final Map<String,Integer> stringToId = new HashMap<String, Integer>();
	private final Map<Integer,String> idToString = new HashMap<Integer, String>();
	private void createMaps(List<Review> reviews) {
		for (Review rev : reviews) { 
			addListToMap(rev.reviewWords);
		}
		for (Review rev : reviews) { 
			addListToMap(rev.titleWords);
		}
		
	}

	private void addListToMap(Map<String, Integer> reviewWords) {
		for (String s : reviewWords.keySet()) {
			if (stringToId.containsKey(s)) continue;
			int id = stringToId.size();
			stringToId.put(s, id);
			idToString.put(id, s);
		}
		
	}

	private List<Review> choseRandomReviews(int numReviews, Random r) {
		int numNeutrals = (int)(PER_NEUTRAL * numReviews);
		int numAgainst = (int)(PER_AGAINST * numReviews);
		int numPros = (int)(PER_PRO * numReviews);
		//getting neutral reviews:
		List<Review> list = getRandoms(neutrals,numNeutrals,r);
		//getting pros reviews:
		list.addAll(getRandoms(pros,numPros,r));
		//getting against reviews:
		list.addAll(getRandoms(against,numAgainst,r));
		int remaining = numReviews - list.size();
		list.addAll(getRandoms(new ArrayList<Review>(allReviews),remaining,r));
		return list;
	}

	private List<Review> getRandoms(List<Review> originalList, int numElements,
			Random r) {
		Collections.shuffle(originalList, r);
		if (numElements > originalList.size()) {
			numElements = originalList.size();
			System.err.println("not enouh elements in one of the types");
		}
		List<Review> result = originalList.subList(0, numElements);
		allReviews.removeAll(result);
		return result;
	}

	private void addReveiwsToList(File f, Description desc) throws IOException, ParseException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		while ((line = br.readLine()) != null) { 
			Review review = new Review(line, desc);
			if (review.classification == Classification.AGAINST) against.add(review);
			else if (review.classification == Classification.NEUTRAL) neutrals.add(review);
			else pros.add(review);
			allReviews.add(review);
		}
	}

}
