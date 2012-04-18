package weka.classifiers.hw1;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.Assert;

import weka.classifiers.Classifier;
import weka.classifiers.trees.j48.C45PruneableClassifierTree;
import weka.classifiers.trees.j48.C45Split;
import weka.classifiers.trees.j48.ClassifierTree;
import weka.classifiers.trees.j48.ModelSelection;
import weka.core.Instance;
import weka.core.Instances;

public class TreeAlg extends C45PruneableClassifierTree {

	private static final class C45SplitComparator implements
			Comparator<C45Split> {
		@Override
		public int compare(C45Split arg0, C45Split arg1) {
			return arg1.attIndex() - arg0.attIndex();
		}
	}

	/**
	 * auto generated serial by eclipse
	 */
	private static final long serialVersionUID = -8923412903432359872L;
	private final List<Instance> instancesList = new LinkedList<Instance>();
	private Classifier leafClassifier = null;
	protected final ClassifierFactory factory;
	private final boolean ignoreAttributes;
	protected final SortedSet<C45Split> attributesUsed;

	
	public TreeAlg(ModelSelection toSelectLocModel,ClassifierFactory factory,boolean ignoreAttributes,
			boolean pruneTree, float cf, boolean raiseTree, boolean cleanup, boolean collapseTree) throws Exception {
		super(toSelectLocModel, pruneTree, cf, raiseTree, cleanup, collapseTree);
		this.ignoreAttributes = ignoreAttributes;
		attributesUsed = new TreeSet<C45Split>(new C45SplitComparator());

		if (factory == null) throw new IllegalArgumentException("Must procide a non-null factory");
		this.factory = factory;
	}

	private TreeAlg(ModelSelection toSelectLocModel,ClassifierFactory factory, Set<C45Split> fatherAttributes, C45Split currentSplit, boolean ignoreAttributes, 
			boolean pruneTree, float cf, boolean raiseTree, boolean cleanup, boolean collapseTree) throws Exception {
		super(toSelectLocModel, pruneTree, cf, raiseTree, cleanup, collapseTree);
		this.ignoreAttributes = ignoreAttributes;
		attributesUsed = new TreeSet<C45Split>(new C45SplitComparator());
		attributesUsed.addAll(fatherAttributes);
		if (currentSplit != null) attributesUsed.add(currentSplit);
		if (factory == null) throw new IllegalArgumentException("Must procide a non-null factory");
		this.factory = factory;
	}

	@Override
	protected ClassifierTree getNewTree(Instances data) throws Exception {
		ClassifierTree newTree = new TreeAlg(m_toSelectModel,factory,attributesUsed,(C45Split)m_localModel,ignoreAttributes,
				m_pruneTheTree, m_CF, m_subtreeRaising,m_cleanup, m_collapseTheTree);
		newTree.buildTree(data, false);

		return newTree;
	}

	@Override
	protected ClassifierTree getNewTree(Instances train, Instances test)  throws Exception {
		ClassifierTree newTree = new TreeAlg(m_toSelectModel,factory,attributesUsed,(C45Split)m_localModel,ignoreAttributes,
				m_pruneTheTree, m_CF, m_subtreeRaising,m_cleanup, m_collapseTheTree);
		newTree.buildTree(train, test, false);
		return newTree;
	}

	@Override
	public void buildClassifier(Instances data) throws Exception {
		super.buildClassifier(data);
		for (Instance instance : data) {
			Assert.assertEquals(0., //TODO: nocommit
					classifyInstance(instance) 
					); //TODO: nocommit
		}
		buildSecondClassifier(data);
	}

	@Override
	protected double handleLeaf(int classIndex, Instance instance, double weight)
			throws Exception {
		if (isAggregatingExamples()) { 
			instancesList.add(instance);
			return 0.;
		} else { 
			return weight * leafClassifier.classifyInstance(instance);
		}
	}

	private boolean isAggregatingExamples() {
		return leafClassifier == null;
	}

	private void buildSecondClassifier(Instances orig) throws Exception {
		if (m_isLeaf) {
			leafClassifier = factory.getClassifier();
			int numOfInstances = orig.size(); //TODO: nocommit
			Instances instances = new Instances(orig);
			instances.clear();
			Assert.assertEquals(orig.size(), numOfInstances); //TODO: no commit
			instances.addAll(instancesList);

			if (ignoreAttributes == true) {
				for (C45Split split : attributesUsed) {
					instances.deleteAttributeAt(split.attIndex());
					//safe since attributesUsed is a SortedSet, sorted from the big index to the lower
				}
			}
			leafClassifier.buildClassifier(instances);
		}
		else { 
			//TODO: add mechanism that allows removing attributes that were used on the tree classifier from the other alg classifier.
			for (int i = 0; i < m_sons.length; i++) {
				((TreeAlg)m_sons[i]).buildSecondClassifier(orig);
			}
		}

	}
}
