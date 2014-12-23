package edu.pku.ly.semantic;

import java.util.List;

import org.semanticweb.owl.model.OWLClass;

public class SemanticNode {
	
	private OWLClass cur_node;
	
	private List<SemanticNode> sub_nodes;
	
	public SemanticNode()
	{
	}
	
	public SemanticNode(OWLClass curr_node, List<SemanticNode> sub_nodes)
	{
		this.cur_node = curr_node;
		this.sub_nodes = sub_nodes;
	}
	
	public void setCurr(OWLClass curr_node)
	{
		this.cur_node = curr_node;
	}
	
	public OWLClass getCurr()
	{
		return cur_node;
	}
	
	public void setSubNodes(List<SemanticNode> sub_nodes)
	{
		this.sub_nodes = sub_nodes;
	}
	
	public List<SemanticNode> getSubNodes()
	{
		return sub_nodes;
	}
	
	public String toString()
	{
		return cur_node.toString();
	}
}
