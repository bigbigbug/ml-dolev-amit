package experiments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class PrecisionRecallParser {
	private static final String SOURCE_FOLDER = "experiments/precision_recall/";
	private static final String RESULTS_FILE = "experiments/pres-rec.txt";
	public static void main(String[] args) throws Exception {
		File dir = new File(SOURCE_FOLDER);
		File file = new File(RESULTS_FILE);
		if (file.exists()) file.delete();
		file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write("type\t");
		for (int i = 1; i < 101; i++) {
			writer.write("p = " + (double)i/100.0+"\t");
		}
		
		writer.write('\n');
		for (String next : dir.list()) {
			if (next.equals(".svn")) continue;
			BufferedReader reader = new BufferedReader(new FileReader(new File(dir,next)));
			String[] t = next.split("res_");
			writer.write(t[t.length-1].replace(".txt", ""));
			writer.write('\n');
			String line;
			while ((line= reader.readLine()) != null) {
				writer.write(line.split(" = ")[0]);
				writer.write('\t');
				writer.write(line.split("\\[")[1].replace(", ", "\t").replace("\\]", "\n"));
				writer.write('\n');
			}
		}
		writer.close();
	}

}
