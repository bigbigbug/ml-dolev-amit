package feature.selection;

import java.util.List;

import sample.Sample;

/**
 * interface for the feature selector
 * @author AmitGross
 *
 */
public interface FeatureSelector {
	
	public static FeatureSelector NONE = new NoFilterSelector();
	/**
	 * Returns a list of new samples, after each sample filtered out the non selected features
	 * @param trainSet - the set of samples we need to train upon 
	 * @return
	 */
	List<Sample> selectFeatresFromTrain(List<Sample> trainSet);

	/**
	 * filters the unwanted features from the samples  
	 * @param testSet - the set of samples we need to filter form the model that was already created
	 * @return a list of samples, where each sample contains only the selected features
	 * @throws IllegalStateException - if it is invoked before selctFeaturesFromTrain() was invoked
	 */
	List<Sample> filterFeaturesFromTest(List<Sample> testSet) throws IllegalStateException;
	/**
	 * gets the number of features selected by this selector
	 * @return the number of features selected by this feature selector
	 * @throws IllegalStateException - if it is invoked before selctFeaturesFromTrain() was invoked
	 */
	int numberOfFeatures(); 
	
}
