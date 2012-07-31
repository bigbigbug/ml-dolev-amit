package feature.selection;
//package feature.selection;
//
//import java.util.Arrays;
//import java.util.Enumeration;
//
//import weka.attributeSelection.ASEvaluation;
//import weka.attributeSelection.ASSearch;
//import weka.attributeSelection.StartSetHandler;
//import weka.core.Attribute;
//import weka.core.Instances;
//import weka.core.OptionHandler;
//
//public class StochasticBestFirstStep extends ASSearch implements
//		StartSetHandler {
//	private String startSet = null;
//
//	@Override
//	public String getStartSet() {
//		return startSet;
//	}
//
//	@Override
//	public void setStartSet(String arg0) throws Exception {
//		startSet = arg0;
//
//	}
//
//	@Override
//	public int[] search(ASEvaluation evaluation, Instances inInstances) throws Exception {
//		int[] attributes = getAttributesList(inInstances);
//		Arrays.sort(attributes);
//		int[] curr;
//		if (startSet != null) { 
//			curr = parseStart(startSet);
//		} else { 
//			curr = attributes;
//		}
//		//TODO: get the subset!
//		int[] subsetToRemove;
//		Instances instances = inInstances
//		for (int i : subsetToRemove) {
//			
//		}
//		
//	}
//
//	private int[] getAttributesList(Instances arg1) {
//		int[] attributes = new int[arg1.numAttributes()];
//		Enumeration l = arg1.enumerateAttributes();
//		int i = 0;
//		while (l.hasMoreElements()) {
//			attributes[i++] = Integer.parseInt(((Attribute)l.nextElement()).name());
//		}
//		return attributes;
//	}
//
//	private int[] parseStart(String start) {
//		String[] arr = start.replace("[", "").replace("]", " ").split(", ");
//		int[] res = new int[arr.length];
//		for (int i = 0; i < arr.length; i++) {
//			res[i] = Integer.parseInt(arr[i].trim());
//		}
//		return res;
//	}
//
//}
