package classifier;

import java.util.LinkedList;
import java.util.List;

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

	public SVMWraper(List<Sample> train, List<Sample> test) {

		if (param.gamma == 0)
			param.gamma = 0.5;

		// build problem
		trainProb = createSVMProb(train);
		testProb = createSVMProb(test);
	}

	/* (non-Javadoc)
	 * @see classifier.classifierAlg#crossValidation(int)
	 */
	@Override
	public Result crossValidation(int folds) {
		// optimize params
		double[] predictions = new double[trainProb.l];
		svm.svm_cross_validation(trainProb, param, folds, predictions);
		return new Result(trainProb.y, predictions);
	}

	/* (non-Javadoc)
	 * @see classifier.classifierAlg#trainTest()
	 */
	@Override
	public Result trainTest() {
		// optimize params
		double[] predictions = new double[testProb.l];
		svm_model model = svm.svm_train(trainProb, param);
		for (int i = 0; i < testProb.l; i++) {
			predictions[i] = svm.svm_predict(model, testProb.x[i]);
		}
		return new Result(testProb.y, predictions);
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
		param.kernel_type = svm_parameter.RBF; // TODO make sure this is correct
	}

	public void setLinear(){
		param.kernel_type = svm_parameter.LINEAR;
	}
}
