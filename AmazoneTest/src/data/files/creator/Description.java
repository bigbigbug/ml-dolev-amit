package data.files.creator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Description {
	public final String title;
	public final String details;
	/**
	 * DO NOT CHANGE THE CONTENT OF THIS SET!
	 */
	public final Set<String> stemmedWords;
	public Description(String title, String details) {
		this.title = title;
		this.details = details;
		stemmedWords = new HashSet<String>(Stemmer.getTerms(title));
	}
	
	public Description(File file) throws IOException, FileNotFoundException { 
		BufferedReader br = new BufferedReader(new FileReader(file));
		String title = br.readLine();
		String description = br.readLine();
		this.title = title;
		this.details = description;
		if (title == null) stemmedWords = new HashSet<String>();
		else stemmedWords = new HashSet<String>(Stemmer.getTerms(title));
		
	}
	
}
