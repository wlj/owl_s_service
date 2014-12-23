package edu.pku.ly.semantic;

import java.util.ArrayList;
import java.util.List;


public class SemanticDistanceImpl implements SemanticDistance {
	
	private SemanticNode rootnode;
	
	public SemanticDistanceImpl()
	{
	}
	
	public SemanticDistanceImpl(SemanticNode root_node)
	{
		this.rootnode = root_node;
	}
	
	public int GetDistance(SemanticNode node1, SemanticNode node2) {

		if(rootnode == null)
		{
			return -1;
		}
		
		List<SemanticNode> path1 = new ArrayList<SemanticNode>();
		GetNodePath(rootnode, node1, path1);
		path1.add(node1);
		
		List<SemanticNode> path2 = new ArrayList<SemanticNode>();
		GetNodePath(rootnode, node2, path2);
		path2.add(node2);

		return GetSemanticDistance(path1, path2);
	}
	
	private int GetSemanticDistance(List<SemanticNode> path1, List<SemanticNode> path2)
	{
		int len1 = path1.size();
		int len2 = path2.size();
		
		if(IsSubsume(path1, path2))
		{
			return Math.abs(len1 - len2);
		}
		
		SemanticNode lastcommonnode = GetLastCommonNode(path1, path2);
		int lastcommonnode_index = 0;
		if(lastcommonnode != null)
		{
			for(int i = 0; i < path1.size(); i++)
			{
				if(path1.get(i).getCurr().getURI().toString() 
						== lastcommonnode.getCurr().getURI().toString())
				{
					lastcommonnode_index = i;
				}
			}
		}
		
		return len1 + len2 - 2 * lastcommonnode_index - 2;
	}
	
	private boolean IsSubsume(List<SemanticNode> path1, List<SemanticNode> path2) {
		// TODO Auto-generated method stub
		
		int len1 = path1.size();
		int len2 = path2.size();
		
		if(len1 > len2 && path1.get(len2 - 1).getCurr().getURI().toString() == 
			path2.get(len2 - 1).getCurr().getURI().toString())
		{
			return true;
		}
		
		if(len1 < len2 && path1.get(len1 - 1).getCurr().getURI().toString() == 
			path2.get(len1 - 1).getCurr().getURI().toString())
		{
			return true;
		}
		
		return false;
	}
	
	private SemanticNode GetLastCommonNode(List<SemanticNode> path1, List<SemanticNode> path2) {
		// TODO Auto-generated method stub
		
		SemanticNode parent = null;
		
		int len1 = path1.size();
		int len2 = path2.size();
		
		int i = 0, j = 0;
		
		while(i < len1 && j < len2)
		{
			if(path1.get(i).getCurr().getURI().toString()
				== path2.get(j).getCurr().getURI().toString()) 
			{
				parent = path1.get(i);
			}			
			i++;
			j++;
		}
		
		return parent;
	}

	private boolean GetNodePath(
			SemanticNode tmproot, 
			SemanticNode node, 
			List<SemanticNode> path)
	{
		if(tmproot.getCurr().getURI().toString() == node.getCurr().getURI().toString())
		{
			return true;
		}
		
		path.add(tmproot);
		boolean found = false;
		
		List<SemanticNode> nodes = tmproot.getSubNodes();
		int i = 0, len = nodes.size();
		while(!found && i < len)
		{
			found = GetNodePath(nodes.get(i), node, path);
			i++;
		}
		
		if(!found)
			path.remove(path.size() - 1);
		
		return found;
	}
	
}
