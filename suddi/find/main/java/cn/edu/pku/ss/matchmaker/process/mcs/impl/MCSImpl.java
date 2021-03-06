package cn.edu.pku.ss.matchmaker.process.mcs.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.clarkparsia.sparqlowl.parser.antlr.SparqlOwlParser.booleanLiteral_return;

import cn.edu.pku.ss.matchmaker.process.mcs.MCS;
import cn.edu.pku.ss.matchmaker.process.model.Edge;
import cn.edu.pku.ss.matchmaker.process.model.Node;
import cn.edu.pku.ss.matchmaker.process.model.NodeType;
import cn.edu.pku.ss.matchmaker.process.model.ProcessGraphModel;
import cn.edu.pku.ss.matchmaker.process.model.ServiceNode;
import cn.edu.pku.ss.matchmaker.similarity.process.AtomicPairSimilarity;
import cn.edu.pku.ss.matchmaker.similarity.process.StructureNodeSimilarity;
import cn.edu.pku.ss.matchmaker.similarity.process.impl.AtomicPairSimilarityImpl;
import cn.edu.pku.ss.matchmaker.similarity.process.impl.StructureNodeSimilarityImpl;
import cn.edu.pku.ss.matchmaker.process.model.StructrueNode;

public class MCSImpl implements MCS {
	private List<MCSNode> mcsNodeList;
	private List<MCSEdge> mcsEdgeList;
	
	private List<MCSNode> atomicNodeList;
	
	private int srcGraphNodeNum;
	private int srcGraphEdgeNum;
	private int destGraphNodeNum;
	private int destGraphEdgeNum;
	
	private int srcAtomicNodeNum;
	private int destAtomicNodeNum;
	
	private StructureNodeSimilarity structureNodeSimilarity;
	private AtomicPairSimilarity atomicPairSimilarity;
	
	private Hashtable<Node, Node> commonNodes;
	private Hashtable<Node, Hashtable<Node, Edge>> twoModelEdges;
	
	//private final double ATOMIC_PROCESS_SIMILARITY_THRESHOLD = 0.4;
	private final double ATOMIC_PROCESS_SIMILARITY_THRESHOLD = 0;
	private final double STRUCTURE_SIMILARITY_THRESHOLD = 0.5;
	
	private Logger logger;
	
	public MCSImpl() {
		// TODO Auto-generated constructor stub
		structureNodeSimilarity = new StructureNodeSimilarityImpl();
		atomicPairSimilarity = new AtomicPairSimilarityImpl();
		
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.process.mcs.impl.MCSImpl.class);
	}
	
	public boolean createMCS(ProcessGraphModel srcModel,
			ProcessGraphModel destModel) {
		// TODO Auto-generated method stub
		if (srcModel == null | destModel == null) {
			logger.error("parameter is illegal");
			return false;
		}
		
		clear();
		commonNodes = new Hashtable<Node, Node>();		
		mcsNodeList = new ArrayList<MCSNode>();
		mcsEdgeList = new ArrayList<MCSEdge>();
		atomicNodeList = new ArrayList<MCSNode>();
		twoModelEdges = new Hashtable<Node, Hashtable<Node,Edge>>();
		
		List<Node> srcNodes = srcModel.getNode();
		List<Node> destNodes = destModel.getNode();
		List<Edge> srcEdges = srcModel.getEdge();
		List<Edge> destEdges = destModel.getEdge();
		
		srcGraphNodeNum = srcModel.getNodeNum();
		srcGraphEdgeNum = srcModel.getEdgeNum();
		destGraphNodeNum = destModel.getNodeNum();
		destGraphEdgeNum = destModel.getEdgeNum();
		
		srcAtomicNodeNum = srcModel.getAtomicProcessNum();
		destAtomicNodeNum = destModel.getAtomicProcessNum();
	
		// init all edges to hashtable
		initEdgesToHashtable(srcEdges);
		initEdgesToHashtable(destEdges);
		
		Queue<MCSNode> mcsNodesQueue  = new LinkedBlockingQueue<MCSNode>();
		
		if (getCommonAtomicProcessNodes(srcNodes, destNodes, mcsNodesQueue) == false) {
			logger.error("failed to get common atomic process");
			return false;
		}
		
		if (getCommonStructureNodesAndCommonEdges(mcsNodesQueue) == false) {
			logger.error("failed to get common structure node and common edges");
			return false;
		}
		
		return true;
	}

	private boolean getCommonStructureNodesAndCommonEdges(Queue<MCSNode> queue) {
		if (queue == null || queue.isEmpty() == true) {
			logger.error("mcs queue is null or empty");
			return false;
		}
		
		while(queue.isEmpty() == false) {
			MCSNode mcsNode = queue.poll();
			Node srcNode = mcsNode.getSrcNode();
			Node destNode = mcsNode.getDestNode();
			boolean precursorFlag = false;
			boolean successorFlag = false;
			
			if ((precursorFlag = processPrecursorNode(srcNode, destNode, queue)) == false)
				logger.error("failed to process precursor node");
			
			if ((successorFlag = processSuccessorNode(srcNode, destNode, queue)) == false)
				logger.error("failed to process successor node");
			
			if (precursorFlag && successorFlag) {
				srcNode.setIsTraversed(true);
				destNode.setIsTraversed(true);
			}
		}
		
		return true;
	}
	
	
	// process precursor
	private boolean processPrecursorNode(Node srcNode, Node destNode, Queue<MCSNode> queue) {		
		if (srcNode == null || destNode == null)
			return false;
		
		if (srcNode.isTraversed() && destNode.isTraversed())
			return true;
		
		List<Node> successorSrcNode = srcNode.getSuccessor();
		List<Node> successorDestNode = destNode.getSuccessor();

		boolean flag[] = new boolean[successorDestNode.size()];
		for (int i = 0; i < flag.length; ++i)
			flag[i] = false;
		
		// check precursor is common node
		for (int i = 0; i < successorSrcNode.size(); ++i) {
			Node tmpSrcNode = successorSrcNode.get(i);
			double maxSimilarity = 0;
			Node candidateNode = null;
			int pos = -1;
			boolean isCommon = false;
			for (int j = 0; j < successorDestNode.size(); ++j) {
				Node tmpDestNode = successorDestNode.get(j);
				
				// if precurssor is common node锛�� then add relative edge to mcs
				if (isCommonNode(tmpSrcNode, tmpDestNode)) {
					flag[j] = true;
					isCommon = true;
					
					if (tmpSrcNode.isTraversed() && tmpDestNode.isTraversed())
						break;
					
					MCSEdge mcsEdge = getSpecifiedEdge(srcNode, tmpSrcNode, destNode, tmpDestNode);
					//if (commonEdges.)
					if (mcsEdge != null)
						mcsEdgeList.add(mcsEdge);
					break;
				}
				
				if (tmpSrcNode.getNodeType().equals(NodeType.STRUCTURE) == false
						|| flag[j] == true
						|| tmpSrcNode.getNodeType().equals(NodeType.STRUCTURE) == false)
					continue;
				
				StructrueNode srcStructrueNode = (StructrueNode) tmpSrcNode;
				StructrueNode destStructrueNode = (StructrueNode) tmpDestNode;
				double similarity = structureNodeSimilarity.getSimilarity(srcStructrueNode.getStructureType(), 
						srcStructrueNode.getBeginEndType(), 
						destStructrueNode.getStructureType(),
						destStructrueNode.getBeginEndType());
				
				if (similarity > maxSimilarity) {
					maxSimilarity = similarity;
					candidateNode = tmpDestNode;
					pos = j;
				}
			}
			
			if (isCommon == false 
					&& maxSimilarity != 0 
					&& pos != -1 
					&& maxSimilarity >= STRUCTURE_SIMILARITY_THRESHOLD) {
				MCSNode tmpMCSNode = new MCSNode(tmpSrcNode, candidateNode, maxSimilarity);
				if (queue.offer(tmpMCSNode) == true) {
					commonNodes.put(tmpSrcNode, candidateNode);
				
					MCSEdge mcsEdge = getSpecifiedEdge(srcNode, tmpSrcNode, destNode, candidateNode);
					if (mcsEdge != null)
						mcsEdgeList.add(mcsEdge);
					mcsNodeList.add(tmpMCSNode);
					flag[pos] = true;
				}
			}
		}
		
		return true;
	}
	
	
	// process precursor
	private boolean processSuccessorNode(Node srcNode, Node destNode, Queue<MCSNode> queue) {
		if (srcNode == null || destNode == null)
			return false;
		
		if (srcNode.isTraversed() && destNode.isTraversed())
			return true;
		
		List<Node> precursorSrcNode = srcNode.getPrecursor();
		List<Node> precursorDestNode = destNode.getPrecursor();	
		
		boolean flag[] = new boolean[precursorDestNode.size()];
		for (int i = 0; i < flag.length; ++i)
			flag[i] = false;
		
		// check precursor is common node
		for (int i = 0; i < precursorSrcNode.size(); ++i) {
			Node tmpSrcNode = precursorSrcNode.get(i);
			double maxSimilarity = 0;
			Node candidateNode = null;
			int pos = -1;
			boolean isCommon = false;
			for (int j = 0; j < precursorDestNode.size(); ++j) {
				Node tmpDestNode = precursorDestNode.get(j);
				
				// if precurssor is common node锛�� then add relative edge to mcs
				if (isCommonNode(tmpSrcNode, tmpDestNode)) {
					flag[j] = true;
					isCommon = true;
					
					if (tmpSrcNode.isTraversed() && tmpDestNode.isTraversed())
						break;
					
					MCSEdge mcsEdge = getSpecifiedEdge(tmpSrcNode, srcNode, tmpDestNode, destNode);
					//if (commonEdges.)
					if (mcsEdge != null)
						mcsEdgeList.add(mcsEdge);					
					break;
				}
				
				if (tmpSrcNode.getNodeType().equals(NodeType.STRUCTURE) == false
						|| flag[j] == true
						|| tmpSrcNode.getNodeType().equals(NodeType.STRUCTURE) == false)
					continue;
				
				StructrueNode srcStructrueNode = (StructrueNode) tmpSrcNode;
				StructrueNode destStructrueNode = (StructrueNode) tmpDestNode;
				double similarity = structureNodeSimilarity.getSimilarity(srcStructrueNode.getStructureType(), 
						srcStructrueNode.getBeginEndType(), 
						destStructrueNode.getStructureType(),
						destStructrueNode.getBeginEndType());
				
				if (similarity > maxSimilarity) {
					maxSimilarity = similarity;
					candidateNode = tmpDestNode;
					pos = j;
				}
			}
			
			if (isCommon == false 
					&& maxSimilarity != 0 
					&& pos != -1 
					&& maxSimilarity >= STRUCTURE_SIMILARITY_THRESHOLD) {
				MCSNode tmpMCSNode = new MCSNode(tmpSrcNode, candidateNode, maxSimilarity);
				if (queue.offer(tmpMCSNode) == true) {				
					commonNodes.put(tmpSrcNode, candidateNode);
					MCSEdge mcsEdge = getSpecifiedEdge(tmpSrcNode, srcNode, candidateNode, destNode);
					if (mcsEdge != null)
						mcsEdgeList.add(mcsEdge);
					mcsNodeList.add(tmpMCSNode);
					flag[pos] = true;
				}
			}
		}
		
		return true;
	}
	
	// check whether Node is common node
	private boolean isCommonNode(Node srcNode, Node destNode) {
		if (commonNodes.containsKey(srcNode)) {
			Node result = commonNodes.get(srcNode);
			if (result == destNode)
				return true;
		}
		
		return false;
	}
	
	private MCSEdge getSpecifiedEdge(Node srcPrecursorNode, Node srcSuccessorNode, 
			Node destPrecursorNode, Node destSuccessorNode) {
		
		Edge srcEdge = null;
		if (twoModelEdges.containsKey(srcPrecursorNode)) {
			Hashtable<Node, Edge> tmp = twoModelEdges.get(srcPrecursorNode);
			if (tmp.containsKey(srcSuccessorNode))
				srcEdge = tmp.get(srcSuccessorNode); 
		}
		
		Edge destEdge = null;
		
		if (twoModelEdges.containsKey(destPrecursorNode)) {
			Hashtable<Node, Edge> tmp = twoModelEdges.get(destPrecursorNode);
			if (tmp.containsKey(destSuccessorNode))
				destEdge = tmp.get(destSuccessorNode); 
		}
		
		MCSEdge mcsEdge = null;
		if (srcEdge != null &&  destEdge != null)
			mcsEdge = new MCSEdge(srcEdge, destEdge);
		
		return mcsEdge;
	}
	
	
	private boolean getCommonAtomicProcessNodes(List<Node> srcNodes, List<Node> destNodes, Queue<MCSNode> queue) {
		if (srcNodes == null || destNodes == null || queue == null) {
		    logger.error("parameter is illegal");
			return false;
		}
		
		boolean flag[] = new boolean[destNodes.size()];
		for (int i = 0; i < flag.length; ++i)
			flag[i] = false;
		
		// find mcs node in the ServiceNodes
		for (int i = 0; i < srcNodes.size(); ++i) {
			Node srcNode = srcNodes.get(i);
			if (srcNode.getNodeType().equals(NodeType.STRUCTURE))
				continue;
			
			double maxSimilarity = 0;
			Node candidateNode = null;
			int pos = -1;
			for (int j = 0; j < destNodes.size(); ++j) {
				Node destNode = destNodes.get(j);
				if (destNode.getNodeType().equals(NodeType.STRUCTURE))
					continue;
				
				if (srcNode.getNodeType().equals(NodeType.SERVICE) == false
						|| flag[j] == true
						|| srcNode.getNodeType().equals(destNode.getNodeType()) == false)
					continue;
				ServiceNode srcServiceNode = (ServiceNode) srcNode;
				ServiceNode destServiceNode = (ServiceNode) destNode;
				double similarity = atomicPairSimilarity.getSimilarity(srcServiceNode.getProcess(), destServiceNode.getProcess());
				if (similarity > maxSimilarity) {
					maxSimilarity = similarity;
					candidateNode = destNode;
					pos = j;
				}
			}
			
			if (maxSimilarity != 0 && pos != -1) {
				MCSNode mcsNode = new MCSNode(srcNode, candidateNode, maxSimilarity);
				// in order to compute atomic similarity
				atomicNodeList.add(mcsNode);
				
				if (maxSimilarity >= ATOMIC_PROCESS_SIMILARITY_THRESHOLD) {
					if (queue.offer(mcsNode) == true) {
						commonNodes.put(srcNode, candidateNode);
					//	commonNodes.put(candidateNode, srcNode);
						flag[pos] = true;
						mcsNodeList.add(mcsNode);
					}
				}
			}
		}
		
		return true;
	}
	
	// put all edges to hashtalbe
	private void initEdgesToHashtable(List<Edge> edges) {	
		for (int i = 0; i < edges.size(); ++i) {
			Edge edge = edges.get(i);
			Node precursor = edge.getPrecursor();
			Node successor = edge.getSuccessor();
			
			if (twoModelEdges.containsKey(precursor)) {
				Hashtable<Node, Edge> value = twoModelEdges.get(precursor);
				
				if (value.containsKey(successor) == false)
					value.put(successor, edge);
			} else {		
				Hashtable<Node, Edge> tmp = new Hashtable<Node, Edge>();
			    tmp.put(successor, edge);
			    twoModelEdges.put(precursor, tmp);
			}
		}
	}
	
	 
	public List<MCSEdge> getMcsEdges() {
		// TODO Auto-generated method stub
		return mcsEdgeList;
	}

	 
	public List<MCSNode> getMcsNodes() {
		// TODO Auto-generated method stub
		return mcsNodeList;
	}
	
	private void clear() {
		if (mcsEdgeList != null)
			mcsEdgeList.clear();
		if (mcsNodeList != null)
			mcsNodeList.clear();
		if (atomicNodeList != null)
			atomicNodeList.clear();
		if (commonNodes != null)
			commonNodes.clear();
		
		if (twoModelEdges != null) {
			Collection<Hashtable<Node, Edge>> edgesCollection = twoModelEdges.values();
			for (Hashtable<Node, Edge> table : edgesCollection)
				table.clear();
			
			twoModelEdges.clear();
		}
		
		
		srcAtomicNodeNum = 0;
		srcGraphEdgeNum = 0;
		srcGraphNodeNum = 0;
		
		destAtomicNodeNum = 0;
		destGraphEdgeNum = 0;
		destGraphNodeNum = 0;
	}

	 
	public int getDestModelEdgeNum() {
		// TODO Auto-generated method stub
		return destGraphEdgeNum;
	}

	 
	public int getDestModelNodeNum() {
		// TODO Auto-generated method stub
		return destGraphNodeNum;
	}

	 
	public int getSrcModelEdgeNum() {
		// TODO Auto-generated method stub
		return srcGraphEdgeNum;
	}

	 
	public int getSrcModelNodeNum() {
		// TODO Auto-generated method stub
		return srcGraphNodeNum;
	}

	 
	public List<MCSNode> getAtomicNodes() {
		// TODO Auto-generated method stub
		return atomicNodeList;
	}

	 
	public int getDestAtomicNodeNum() {
		// TODO Auto-generated method stub
		return destAtomicNodeNum;
	}

	 
	public int getSrcAtomicNodeNum() {
		// TODO Auto-generated method stub
		return srcAtomicNodeNum;
	}
}
