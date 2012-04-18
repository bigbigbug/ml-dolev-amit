package weka.classifiers.hw1;

import weka.classifiers.trees.J48;
import weka.classifiers.trees.j48.BinC45ModelSelection;
import weka.classifiers.trees.j48.C45ModelSelection;
import weka.classifiers.trees.j48.ModelSelection;
import weka.core.Capabilities;
import weka.core.Instances;

public class J48KNN extends J48 {
	final static boolean IGNORE_ATTRIBUTES = false;
	
	/**
	 * auto generated serial by eclipse
	 */
	private ClassifierFactory factory = new KNNFactory();
	private static final long serialVersionUID = 6562666469294387895L;

	@Override
	public Capabilities getCapabilities() {
		Capabilities result;
		try {
			if (!m_reducedErrorPruning)
				result = new C45PruneableAlg(null, !m_unpruned, m_CF, m_subtreeRaising, !m_noCleanup, m_collapseTree,factory,IGNORE_ATTRIBUTES).getCapabilities();
			else
				throw new NotImplementedException();
			//				result = new PruneableClassifierTree(null, !m_unpruned, m_numFolds, !m_noCleanup, m_Seed).getCapabilities();
		}
		catch (Exception e) {
			result = new Capabilities(this);
			result.disableAll();
		}

		result.setOwner(this);

		return result;
	}

	@Override
	public void buildClassifier(Instances instances) 
			throws Exception {

		ModelSelection modSelection;	 

		if (m_binarySplits)
			modSelection = new BinC45ModelSelection(m_minNumObj, instances, m_useMDLcorrection);
		else
			modSelection = new C45ModelSelection(m_minNumObj, instances, m_useMDLcorrection);
		if (!m_reducedErrorPruning)
			m_root = new C45PruneableAlg(modSelection, !m_unpruned, m_CF,
					m_subtreeRaising, !m_noCleanup, m_collapseTree,factory,IGNORE_ATTRIBUTES);
		else
			throw new NotImplementedException();
//			m_root = new PruneableClassifierTree(modSelection, !m_unpruned, m_numFolds,
//					!m_noCleanup, m_Seed);
		m_root.buildClassifier(instances);
		if (m_binarySplits) {
			((BinC45ModelSelection)modSelection).cleanup();
		} else {
			((C45ModelSelection)modSelection).cleanup();
		}
	}

}
