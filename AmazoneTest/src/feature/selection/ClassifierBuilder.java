package feature.selection;


public interface ClassifierBuilder {
	weka.classifiers.Classifier build() throws Exception ;
}
