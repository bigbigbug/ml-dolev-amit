package crawler.amazon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReviewExtractor {
	private String perlCmd;
	
	private static final String DOWNLOAD_REVIEWS_SCRIPTS = "./src/scripts/perl/downloadAmazonReviews.pl";
	private static final String EXTRACT_REVIEWS_SCRIPTS = "./src/scripts/perl/extractAmazonReviews.pl";
	private static final String OUT_FOLDER = "./data/";
	private static final String TEMP_FOLDER =  "./amazonreviews/";

	public ReviewExtractor(String ProducID, String perlCmd) throws Exception {
		this.perlCmd = perlCmd;
		File dir = new File(TEMP_FOLDER);
		if (!dir.isDirectory()) dir.mkdirs();
		File productFile = new File(dir,ProducID+".txt");
		if (!productFile.exists()) productFile.createNewFile();
		FileOutputStream outStream = new FileOutputStream(productFile, true); //TODO: shoule remain as append? 
		extractProductReviews(ProducID,outStream);
		cannonize(productFile,ProducID);
		
	}
	private static final int DATE_IDX  = 1;
	private static final int RATING_IDX  = 3;
	private static final int REVIEW_IDX  = 7;
	private static final int TITLE_IDX  = 6;
	
	private void cannonize(File productFile,String productId) throws IOException {
		System.out.println(productFile);
		FileReader fr = new FileReader(productFile);
		BufferedReader br = new BufferedReader(fr);
		File dir = new File(OUT_FOLDER);
		if (!dir.isDirectory()) dir.mkdirs();
		File reviewsFile = new File(dir,productId + "_reveiws.csv");
		if (reviewsFile.exists()) reviewsFile.delete();
		reviewsFile.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(reviewsFile));
		String line = null;
		while (( line = br.readLine()) != null) {
			System.out.println("reading");
			String[] arr = line.split("\",\"",-1);
			if (arr.length < 8) { 
				System.err.println("the length is only " + arr.length);
				continue;
			}
			bw.write("\"" + arr[DATE_IDX].trim() + "\",");
			bw.write("\"" + arr[RATING_IDX].trim() + "\",");
			bw.write("\"" + arr[TITLE_IDX].trim() + "\",");
			bw.write("\"" + arr[REVIEW_IDX].trim() + "\"\n");
		}
		bw.close();
		
	}

	private void extractProductReviews(String ProducID, OutputStream destination)
			throws Exception {
		Process p = Runtime.getRuntime().exec(
				new String[] { perlCmd, DOWNLOAD_REVIEWS_SCRIPTS, ProducID });
		pipeOutput(p);
		p.waitFor();
		File outFolder = new File(TEMP_FOLDER + ProducID);
		int i =1;
		for (String file : outFolder.list()) {
			System.out.println("At file:"+ i++ +" out of:"+outFolder.list().length);
			Process p2 = Runtime.getRuntime().exec(
					new String[] { perlCmd, EXTRACT_REVIEWS_SCRIPTS,
							outFolder.getPath() + "/" + file });
			pipe(p2.getInputStream(), destination);
			p2.waitFor();
		}
		destination.close();
	}

	private void pipeOutput(Process process) {
		pipe(process.getErrorStream(), System.err);
		pipe(process.getInputStream(), System.out);
	}

	private void pipe(final InputStream src, final OutputStream dest) {
		new Thread(new Runnable() {
			public void run() {
				try {
					byte[] buffer = new byte[1024];
					for (int n = 0; n != -1; n = src.read(buffer)) {
						dest.write(buffer, 0, n);
					}
				} catch (IOException e) { // just exit
				}
			}
		}).start();
	}
}