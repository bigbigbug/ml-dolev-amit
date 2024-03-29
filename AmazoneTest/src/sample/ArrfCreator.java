package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import feature.selection.FeatureSelector;
import feature.selection.InformationGainFeatureSelector;

public class ArrfCreator {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		SamplesManager sm = new SamplesManager();
		Instances train = SamplesManager.asWekaInstances(sm.parseTrainData(new File(
				SamplesManager.DATA_DIR), SamplesManager.DATA_FILE_NAME,
				SamplesManager.CLASSIFICATION_FILE_NAME, FeatureSelector.NONE));
		Instances test = SamplesManager.asWekaInstances(sm.parseTestData(new File(
				SamplesManager.DATA_DIR), SamplesManager.DATA_FILE_NAME,
				SamplesManager.CLASSIFICATION_FILE_NAME));
		train.compactify();
		ArffSaver saver = new ArffSaver();
		saver.setInstances(train);
		saver.setFile(new File("./train.arff"));
		saver.setInstances(test);
		saver.setFile(new File("./test.arff"));
		saver.writeBatch();
		System.out.println("done!");
	}

}
