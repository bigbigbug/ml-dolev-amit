package sample;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import weka.core.Instances;
import feature.selection.FeatureSelector;

public class ArrfCreator {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		SamplesManager sm = new SamplesManager();
		Instances train = SamplesManager.asWekaInstances(sm.parseTrainData(new File(
				SamplesManager.DATA_DIR), SamplesManager.DATA_FILE_NAME,
				SamplesManager.CLASSIFICATION_FILE_NAME, FeatureSelector.NONE));
		BufferedWriter writer = new BufferedWriter(new FileWriter("./train.arff"));
		writer.write(train.toString());
		writer.flush();
		writer.close();
	}

}
