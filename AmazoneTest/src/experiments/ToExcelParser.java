package experiments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToExcelParser {
	private static final String[] FILES  = {
//		"experiments/feature_selection/symm_naive_bayse.txt",
//		"experiments/feature_selection/symm_linear_svm.txt",
//		"experiments/feature_selection/symm_hyperbolic_svm.txt",
//
//		"experiments/feature_selection/info_gain_naive_bayse.txt",
//		"experiments/feature_selection/info_gain_linear_svm.txt",
//		"experiments/feature_selection/info_gain_hyperbolic_svm.txt",
//
//		"experiments/feature_selection/ReliefF_naive_bayse.txt",
//		"experiments/feature_selection/ReliefF_linear_svm.txt",
//		"experiments/feature_selection/ReliefF_hyperbolic_svm.txt",

		"experiments/feature_selection/pca_naive_bayse.txt",
		"experiments/feature_selection/pca_linear_svm.txt",
		"experiments/feature_selection/pca_hyperbolic_svm.txt",


	};
	private static final String EXCEL_FILE = "experiments/feature_selection/sum.txt";
	private static final Matcher ACCURACY_MATCHER = Pattern.compile("accuracy=").matcher("");
	private static final Matcher NUM_FEATURES_MATCHER = Pattern.compile("[0-9]+").matcher("");
	public static void main(String[] args) throws Exception {
		File f = new File(EXCEL_FILE);
		if (f.exists()) f.delete();
		f.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(f));
		writer.write("#features\t");
		BufferedReader[] readers = new BufferedReader[FILES.length];
		int i = 0;
		for (String s : FILES) { 
			readers[i++] = new BufferedReader(new FileReader(s));
			String[] t = s.split("/");
			writer.write(t[t.length-1].replace(".txt", ""));
			writer.write('\t');
		}
		boolean stillReading = true;
		while (stillReading) { 
			writer.write('\n');
			stillReading = false;
			for (i = 0; i < readers.length; i++) { 
				BufferedReader reader = readers[i];
				String line = reader.readLine();
				if (line != null) {
					if (i == 0) {
						stillReading = true;
						System.out.println(line);
						NUM_FEATURES_MATCHER.reset(line);
						if (!NUM_FEATURES_MATCHER.find()) continue;
						writer.write(NUM_FEATURES_MATCHER.group());
						writer.write('\t');
					}
					ACCURACY_MATCHER.reset(line);
					if (!ACCURACY_MATCHER.find()) continue;
					writer.write(line.substring(ACCURACY_MATCHER.end()));
				}
				writer.write('\t');
			}
		}
		writer.close();
	}
}
