package com.base.game.LevelGeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.base.engine.core.math.Vector3f;

public class Graph <Island>
{
    private Map<Island, List<Island>> map;
    
    
    public Graph()
    {
        this.map = new HashMap<>();
    }

    public Graph(Graph<Island> g)
    {
        this.map = g.map;
    }
    
    public boolean add(Island node)
    {
    	if(contains(node)) { return false; }
    	
    	map.put(node, new ArrayList<Island>());
    	return true;
    }
    
    public void addEdge(Island from, Island to, float weight)
    {
        this.add(from);
        this.add(to);
        
        map.get(from).add(to);
        map.get(to).add(from);
    }
    
    public Iterator<Island> iterator()
    {
    	return map.keySet().iterator();
    }
    
    
    public boolean contains(Island node)
    {
        return map.containsKey(node);
    }
    
    public boolean empty()
    {
        return map.isEmpty();
    }
    
    public List<Island> getNodes()
    {
    	List<Island> list = new ArrayList<>();
    	
    	for(Island node : map.keySet())
    	{
    		list.add(node);
    	}
    	
    	return list;
    }
    
    public List<Island> getEdges(Island T)
    {
    	return map.get(T);
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer(); //because strings in java are immutable

        for(Island node : map.keySet())
        {
            sb.append(node + " -> " + map.get(node) + "\n");
            
            Iterator it = map.get(node).iterator();
            
            while(it.hasNext())
            {
            	System.out.println("ITNODE " + it.next());
            }
        }

        return sb.toString();
    }    
}
