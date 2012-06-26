package classifier;

import java.util.List;
import java.util.TreeSet;

import sample.Sample;
import weka.core.Instance;
import weka.core.Instances;

public class WekaWraper implements Classifier {
	
	public WekaWraper(List<Sample> train, List<Sample> test) {
		Sample
		SortedSet attributes = new TreeSet<String>()
		
		
		Instances train = new Instances("trainData", attInfo, Integer.MAX_VALUE);
		Instances test;
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public ClassificationResult crossValidation(int folds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassificationResult trainTest() {
		// TODO Auto-generated method stub
		return null;
	}

}
