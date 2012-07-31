package feature.selection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class RandomSubset {

	private class AttScore implements Comparable<AttScore>{
		Integer attId;
		Double position;
		@Override
		public int compareTo(AttScore o) { return position.compareTo(o.position); }
	}
	
	private TreeSet<AttScore> attLeft;
	private Random r;
	private double[] scores;
	private double EPSILON = 10e-3;
	
	public RandomSubset(double scores[], Random r) {
		this.r = r;
		this.scores = scores;
		int valids[] = new int[scores.length];
		for (int i = 0; i < valids.length; i++) { valids[i] = i; }
		update(valids);
	}

	public void update(int[] valids) {
		TreeSet<AttScore> newAtts = new TreeSet<AttScore>();
		double probs[] = new double[valids.length];
		double sum = 0;
		for (int i = 0; i < probs.length; i++) {
			probs[i] = scores[valids[i]]+EPSILON;
			sum+=scores[valids[i]]+EPSILON;
		}
		probs[0] = probs[0]/sum;
		for (int i = 1; i < probs.length; i++) {
			probs[i] = probs[i]/sum + probs[i-1];
		}
		for (int i = 0; i < probs.length; i++) {
			AttScore atts = new AttScore();
			atts.attId = i;
			atts.position = probs[i];
			newAtts.add(atts);
		}
		attLeft = newAtts;
	}
	
	public Integer[][] getSubsets(int numSubsets, int subsetSize) {
		Integer[][] retVal = new Integer[numSubsets][];
		
		if (subsetSize > attLeft.size()) {
			System.err.println("not enough att left");
			return null;
		}
		
		for (int i = 0; i < retVal.length; i++) {
			retVal[i] = getStohasticSubset(subsetSize);
			Arrays.sort(retVal[i]);
		}
		
		return retVal;
	}

	private Integer[] getStohasticSubset(int numCandidates) {
		Set<Integer> candidates = new HashSet<Integer>();
		while (candidates.size()<numCandidates){
			AttScore atts = new AttScore();
			atts.position = r.nextDouble();
			atts = attLeft.higher(atts);
			candidates.add(atts.attId);
		}
		return (Integer[]) candidates.toArray(new Integer[candidates.size()]);
	}
	
	public static void main(String[] args) {
		double scores[] = new double[1000];
		scores[0]=1000;
		for (int i = 1; i < scores.length; i++) {
			scores[i] = scores[i-1]-1;
		}
		RandomSubset rs = new RandomSubset(scores, new Random(1));
		int[] valids = new int[13];
		for (int i = 0; i < valids.length; i++) {
			valids[i] = 3*i;
		}
		Integer[][] res = rs.getSubsets(5, 20);
		for (int i = 0; i < res.length; i++) {
			System.out.println(Arrays.toString(res[i]));
		}
		rs.update(valids);
		res = rs.getSubsets(5, 20);
		for (int i = 0; i < res.length; i++) {
			System.out.println(Arrays.toString(res[i]));
		}
		
	}
}
