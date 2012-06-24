package classifier;

public interface Classifier {

	public abstract ClassificationResult crossValidation(int folds);

	public abstract ClassificationResult trainTest();

}