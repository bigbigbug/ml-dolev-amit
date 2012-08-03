package experiments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultsParser {
	private static final String RESULTS_FILE = "experiments/feature_selection/info_gain_naive_bayse.txt";
	private static final Matcher ACCURACY_MATCHER = Pattern.compile("accuracy=").matcher("");
	private static final Matcher NUM_FEATURES_MATCHER = Pattern.compile("[0-9]+").matcher("");
	public static void main(String[] args) throws Exception {
		File file = new File(RESULTS_FILE);

		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		System.out.println("num features:");
		System.out.print('[');
		while ((line = br.readLine()) != null) { 
			NUM_FEATURES_MATCHER.reset(line);
			if (!NUM_FEATURES_MATCHER.find()) continue;
			System.out.print(NUM_FEATURES_MATCHER.group() + ", ");
		}
		System.out.println(']');
		
		br = new BufferedReader(new FileReader(file));
		System.out.println("accuracy:");
		System.out.print('[');
		while ((line = br.readLine()) != null) { 
			ACCURACY_MATCHER.reset(line);
			if (!ACCURACY_MATCHER.find()) continue;
			System.out.print(line.substring(ACCURACY_MATCHER.end()) + ", ");
		}
		System.out.println(']');
	}
}
