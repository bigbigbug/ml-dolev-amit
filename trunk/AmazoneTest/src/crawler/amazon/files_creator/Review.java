package crawler.amazon.files_creator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Review {
	public static enum Classification {
		ZERO, AGAINST, NEUTRAL, PRO;
	}
	private static final String DATE_PATTERN = "yyyyMMdd";
	private static final int DATE_IDX = 0;
	private static final int RATING_IDX = 1;
	private static final int TITLE_IDX = 2;
	private static final int REV_IDX = 3;
	public final Date date;
	public final int rating;
	public final Map<String, Integer> titleWords;
	public final Map<String, Integer> reviewWords;
	private final String review;
	public final Classification classification;
	public final Description description;
	private boolean useBiGrams = true;

	public void setUseBiGrams(boolean useBiGrams) {
		this.useBiGrams = useBiGrams;
	}
	public Review(String line,Description description) throws ParseException {
		String[] arr = line.split("\",\"",-1);
		for (int i = 0;i < arr.length; i++) { 
			arr[i] = arr[i].replaceAll("\"", "");
		}
		SimpleDateFormat dateFormater = new SimpleDateFormat(DATE_PATTERN);
		this.date = dateFormater.parse(arr[DATE_IDX]);
		rating = Math.round(Float.parseFloat((arr[RATING_IDX])));
		titleWords = createHistogram(arr[TITLE_IDX],description.stemmedWords,"t_");
		if (useBiGrams ) titleWords.putAll(getBiGrams(arr[TITLE_IDX], description.stemmedWords,"t_"));
		reviewWords = createHistogram(arr[REV_IDX],description.stemmedWords,"");
		if (useBiGrams) reviewWords.putAll(getBiGrams(arr[RATING_IDX], description.stemmedWords,""));
		if (rating < 3) classification = Classification.AGAINST;
		else if (rating == 3) classification = Classification.NEUTRAL;
		else classification = Classification.PRO;
		this.description = description;
		this.review = arr[REV_IDX];
	}

	@Override
	public int hashCode() {
		return review.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Review)) return false;
		return review.equals(((Review)obj).review);
	}

	private Map<String, Integer> createHistogram(String string,Set<String> stopWords,String prefix) {
		Map<String,Integer> histogram = new HashMap<String, Integer>();
		for (String str : Stemmer.getTerms(string, stopWords)) {
			String s = prefix + str;
			Integer x = histogram.get(s);
			histogram.put(s,(x == null? 1 : x + 1));
		}
		return histogram;
	}
	private Map<? extends String, ? extends Integer> getBiGrams(String string,
			Set<String> stopWords, String prefix) {
		Map<String,Integer> histogram = new HashMap<String, Integer>();
		String prev = null;
		for (String str : Stemmer.getTerms(string, stopWords)) {
			if (prev != null) {
				String s = prefix + prev + "_" + str;
				Integer x = histogram.get(s);
				histogram.put(s,(x == null? 1 : x + 1));
			}
			prev = str;
		}
		return histogram;
	}
}
