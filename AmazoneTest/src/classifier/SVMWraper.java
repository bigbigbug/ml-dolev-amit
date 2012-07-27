package classifier;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import sample.Attribute;
import sample.Sample;

public class SVMWraper implements Classifier {

	private svm_parameter param = defaultParams();

	private final svm_problem trainProb;
	private final svm_problem testProb;

	private int badAtt = 0;

	public SVMWraper(List<Sample> train, List<Sample> test) {

		if (param.gamma == 0)
			param.gamma = 0.5;

		// build problem
		trainProb = createSVMProb(train);
		testProb = createSVMProb(test);

		if (badAtt > 0) {
			throw new IllegalArgumentException("Found " + badAtt
					+ " attribute with value not in range [0,1]");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see classifier.classifierAlg#crossValidation(int)
	 */
	@Override
	public Result crossValidation(int numFolds) {
		// split to folds
		svm_problem folds[] = new svm_problem[numFolds];
		// get random permutation
		Random rand = new Random(1);
		List<Integer> perm = randPerm(trainProb.l, rand);
		int prev = 0;
		for (int i = 0; i < folds.length; i++) {
			folds[i] = new svm_problem();
			folds[i].l = new Double(Math.ceil((trainProb.l - i) / (double) numFolds)).intValue();
			folds[i].y = new double[folds[i].l];
			folds[i].x = new svm_node[folds[i].l][];
			for (int j = 0; j < folds[i].l; j++) {
				int index = perm.get(j + prev);
				folds[i].y[j] = trainProb.y[index];
				folds[i].x[j] = trainProb.x[index];
			}
			prev += folds[i].l;
		}

		double foldResults[][] = new double[numFolds][];
		double foldExpected[][] = new double[numFolds][];
		// grouping problems and simulating (including params optimization)
		for (int i = 0; i < folds.length; i++) {
			svm_problem agrigatetProblem = new svm_problem();
			agrigatetProblem.l = 0;
			double y[][] = new double[numFolds - 1][];
			svm_node x[][][] = new svm_node[numFolds - 1][][];
			int pos = 0;
			for (int j = 0; j < folds.length; j++) {
				if (i == j)
					continue;
				agrigatetProblem.l += folds[j].l;
				y[pos] = folds[j].y;
				x[pos] = folds[j].x;
				pos++;
			}
			agrigatetProblem.y = concatAll(y);
			agrigatetProblem.x = concatAll(x);
			foldResults[i] = privateTrainTest(agrigatetProblem, folds[i]); //
			foldExpected[i] = folds[i].y; //
		}

		return new Result(concatAll(foldExpected), concatAll(foldResults));
	}

	private List<Integer> randPerm(int length, Random rand) {
		List<Integer> perm = new LinkedList<Integer>();
		for (int i = 0; i < length; i++) perm.add(i);
		java.util.Collections.shuffle(perm, rand);
		return perm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see classifier.classifierAlg#trainTest()
	 */
	@Override
	public Result trainTest() {
		return new Result(testProb.y, privateTrainTest(trainProb, testProb));
	}

	private double[] privateTrainTest(svm_problem train, svm_problem test) {
		// optimize parameters
		optimizeParams(train, param);
		double[] predictions = new double[test.l];
		svm_model model = svm.svm_train(train, param);
		for (int i = 0; i < test.l; i++) {
			predictions[i] = svm.svm_predict(model, test.x[i]);
		}
		return predictions;
	}

	// TODO when performing real tests increase the search
	private static final int C_MIN_POWER = -3; // -5
	private static final int C_MAX_POWER = 3; // +5
	private static final int G_MIN_POWER = -3; // -9
	private static final int G_MAX_POWER = 3; // 0 not sure
	private static final int PARAM_OPTIMIZE_FOLDS = 2;

	private void optimizeParams(svm_problem problem, svm_parameter param) {
		if (true) { // TODO Run optimization
			param.C = 1000;
			param.gamma = 0.01;
			return;
		}
		double[] predictions = new double[problem.l];
		double bestC = 0;
		double bestG = 0;
		double bestCacc = 0;
		for (int c = C_MIN_POWER; c <= C_MAX_POWER; c++) {
			for (int g = G_MIN_POWER; g <= G_MAX_POWER; g++) {
				param.C = Math.pow(10, c);
				param.gamma = Math.pow(10, g);
				svm.svm_cross_validation(problem, param, PARAM_OPTIMIZE_FOLDS, predictions);
				double accuracy = (new Result(problem.y, predictions)).accuracy();
				if (accuracy > bestCacc) {
					bestC = c;
					bestG = g;
					bestCacc = accuracy;
				}
			}
		}
		System.err.println("Best C : " + Math.pow(10, bestC));
		System.err.println("Best gamma : " + Math.pow(10, bestG));
		param.C = Math.pow(10, bestC);
		param.gamma = Math.pow(10, bestC);
		;

	}

	private svm_problem createSVMProb(List<Sample> samples) {
		svm_problem prob = new svm_problem();
		prob.l = samples.size();
		prob.y = new double[prob.l];

		prob.x = new svm_node[prob.l][];
		for (int i = 0; i < prob.l; i++) {
			List<Attribute> samplesAtt = samples.get(i).attributes;
			int numAtt = samplesAtt.size();
			prob.x[i] = new svm_node[numAtt];
			for (int j = 0; j < numAtt; j++) {
				Attribute att = samplesAtt.get(j);
				if (att.value > 2)
					badAtt++;
				prob.x[i][j] = new svm_node();
				prob.x[i][j].index = att.attributeNumber;
				prob.x[i][j].value = att.value;
			}
			prob.y[i] = samples.get(i).classification;
		}
		return prob;
	}

	private svm_parameter defaultParams() {
		svm_parameter param = new svm_parameter();

		// default values
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = 0;
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 40;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];
		return param;
	}

	public void setHyperbolic() {
		param.kernel_type = svm_parameter.RBF;
	}

	public void setLinear() {
		param.kernel_type = svm_parameter.LINEAR;
	}

	// TODO check moving to generic
	private double[] concatAll(double[][] toMerge) {
		int totalLength = 0;
		for (double[] array : toMerge) {
			totalLength += array.length;
		}
		double[] result = new double[totalLength];
		int offset = 0;
		for (double[] array : toMerge) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}

	private svm_node[][] concatAll(svm_node[][][] toMerge) {
		int totalLength = 0;
		for (svm_node[][] array : toMerge) {
			totalLength += array.length;
		}
		svm_node[][] result = new svm_node[totalLength][];
		int offset = 0;
		for (svm_node[][] array : toMerge) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}
}
