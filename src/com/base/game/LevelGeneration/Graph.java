package com.base.game.LevelGeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.base.engine.core.math.Vector3f;



public class Graph <T>
{
	private class Edge <T>
	{
        private T node;
	    private float weight;

	    public Edge(T node, float weight)
	    {
	        this.node = node;
	        this.weight = weight;
	    }

	    public T node()
	    {
	        return node;
	    }
	    
	    public float weight()
	    {
	        return weight;
	    }

	    public boolean equals(Object obj)
	    {
	        if(obj == this) { return true; }

	        if(!(obj instanceof Edge)) { return false; }

	        Edge<T> other = (Edge<T>)obj;

	        return this.equals(other);
	    }

	    public boolean equals(Edge<T> other)
	    {
	        return this.node.equals(other.node) && this.weight == other.weight;
	    }

	    public String toString()
	    {
	        return "( node: " + node + ", weight: " + weight + ")";
	    }
	}
	
    private Map<T, List<Edge<T>>> map;
    
    
    public Graph()
    {
        this.map = new HashMap<>();
    }

    public Graph(Graph<T> g)
    {
        this.map = g.map;
    }
    
    public boolean add(T node)
    {
    	if(contains(node)) { return false; }
    	
    	map.put(node, new ArrayList<Edge<T>>());
    	return true;
    }
    
    public void addEdge(T from, T to, float weight)
    {
        this.add(from);
        this.add(to);
        
        map.get(from).add(new Edge<T>(to, weight));
        map.get(to).add(new Edge<T>(from, weight));
    }
    
    public Iterator<T> iterator()
    {
    	return map.keySet().iterator();
    }
    
    
    public boolean contains(T node)
    {
        return map.containsKey(node);
    }
    
    public boolean empty()
    {
        return map.isEmpty();
    }
    
    public List<T> getNodes()
    {
    	List<T> list = new ArrayList<>();
    	
    	for(T node : map.keySet())
    	{
    		list.add(node);
    	}
    	
    	return list;
    }
    
    public void getEdgesFromNode(Vector3f vec)
    {
    	Island island = new Island(vec);
    	
    	Iterator it =  map.get(island).iterator();
    	
    	while(it.hasNext())
    	{
    		System.out.println("ELEM "+ it.next());
    	}
    }
    
    public void getEdgesFromNode(Island island)
    {
    	
    	Iterator it =  map.get(island).iterator();
    	
    	while(it.hasNext())
    	{
    		System.out.println("ELEM "+ it.next());
    	}
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer(); //because strings in java are immutable

        for(T node : map.keySet())
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
