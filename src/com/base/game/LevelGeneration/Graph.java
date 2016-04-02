package com.base.game.LevelGeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Graph <T>
{
	private class Edge <T>
	{
		private T node;

	    private int weight;

	    public Edge(T node, int weight)
	    {
	        this.node = node;
	        this.weight = weight;
	    }

	    public T node()
	    {
	        return node;
	    }

	    public int weight()
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
    private Map<T , Integer> inDegrees;
    
    
    public Graph()
    {
        this.map = new HashMap<>();
        this.inDegrees = new HashMap<>();
    }

    public Graph(Graph<T> g)
    {
        this.map = g.map;
        this.inDegrees = g.inDegrees;
    }
    
    public boolean add(T node)
    {
    	if(contains(node)) { return false; }
    	
    	inDegrees.put(node, 0);
    	map.put(node, new ArrayList<Edge<T>>());
    	return true;
    }
    
    public void addEdge(T from, T to, int weight)
    {
        this.add(from);
        this.add(to);

        inDegrees.put(to, inDegrees.get(to) + 1);
        map.get(from).add(new Edge<T>(to, weight));
    }
    
    public boolean contains(T node)
    {
        return map.containsKey(node);
    }
    
    public boolean empty()
    {
        return map.isEmpty();
    }
    
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer(); //because strings in java are immutable

        for(T node : map.keySet())
        {
            sb.append(node + " -> " + map.get(node) + "\n");
        }

        return sb.toString();
    }    
}
