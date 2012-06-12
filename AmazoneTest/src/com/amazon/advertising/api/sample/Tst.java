package com.amazon.advertising.api.sample;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class Tst {
	private static final String PERL_CMD = "C:/strawberry/perl/bin/perl.exe";
	private static final String DOWNLOAD_REVIEWS_SCRIPTS = "./src/scripts/perl/downloadAmazonReviews.pl";
	private static final String EXTRACT_REVIEWS_SCRIPTS = "./src/scripts/perl/extractAmazonReviews.pl";
	private static final String outPath = "./amazonreviews/";

	public static void main(String[] args) throws Exception {
		String ProducID = "B004LB4SAM";
		FileWriter fstream = new FileWriter(outPath+ProducID+".txt");
		BufferedWriter out = new BufferedWriter(fstream);
		(new Tst()).extractProductReviews(ProducID,out);
		out.close();

	}

	private void extractProductReviews(String ProducID, OutputStream destanation)
			throws Exception {
		Process p = Runtime.getRuntime().exec(
				new String[] { PERL_CMD, DOWNLOAD_REVIEWS_SCRIPTS, ProducID });
		pipeOutput(p);
		File outFolder = new File(outPath + ProducID);
		for (String file : outFolder.list()) {
			Process p2 = Runtime.getRuntime().exec(
					new String[] { PERL_CMD, EXTRACT_REVIEWS_SCRIPTS,
							outFolder.getPath() + "/" + file });
			pipeOutput(p2);
			pipe(p2.getInputStream(), destanation);
		}
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