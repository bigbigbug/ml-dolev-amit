package data.files.creator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

@SuppressWarnings("deprecation")
public class Stemmer {
	public static List<String> getTerms(String s) { 
		Analyzer analyzer = new SnowballAnalyzer(Version.LUCENE_36, "English", StandardAnalyzer.STOP_WORDS_SET);
		TokenStream ts = analyzer.tokenStream(null, new StringReader(s));
		try {
			return tokensToList(ts);
		} catch (IOException e) { 
			e.printStackTrace();
			return null;
			//could not really happen, it is just a StringReader
		}
	}
	public static List<String> getTerms(String s,Set<String> additinalStopWords) {
		Set<?> stopWords = createStopWordsSet(additinalStopWords);
		Analyzer analyzer = new SnowballAnalyzer(Version.LUCENE_36, "English", stopWords);
		TokenStream ts = analyzer.tokenStream(null, new StringReader(s));
		try {
			return tokensToList(ts);
		} catch (IOException e) { 
			e.printStackTrace();
			return null;
			//could not really happen, it is just a StringReader
		}
	}

	private static Set<?> createStopWordsSet(Set<String> additinalStopWords) {
		Set<Object> set = new HashSet<Object>(StandardAnalyzer.STOP_WORDS_SET);
		for (String s : additinalStopWords) {
			set.add((Object)s.toCharArray());
		}
		return set;
	}
	public static List<String> getTerms(File f) throws FileNotFoundException, IOException { 
		Analyzer analyzer = new SnowballAnalyzer(Version.LUCENE_36, "English", StandardAnalyzer.STOP_WORDS_SET);
		TokenStream ts = analyzer.tokenStream(null, new FileReader(f));
		return tokensToList(ts);
	}

	private static List<String> tokensToList(TokenStream ts) throws IOException { 
		ArrayList<String> result = new ArrayList<String>();
		while (ts.incrementToken()) {
			String curr = ts.getAttributeImplsIterator().next().toString();
			result.add(curr);
		} 
		return result;
	}
}
