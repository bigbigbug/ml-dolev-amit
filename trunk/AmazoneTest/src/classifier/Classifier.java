package classifier;

public interface Classifier {

	public abstract Result crossValidation(int folds);

	public abstract Result trainTest();

}