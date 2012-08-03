package classifier;

import experiments.PresisionTest;

public interface Classifier {

	public abstract Result crossValidation(int folds);

	public abstract Result trainTest();

	public abstract PresisionTest confidance(int folds);

}