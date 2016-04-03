package com.base.game.LevelGeneration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.base.engine.core.math.Vector3f;

public class IslandGeneration
{	
	private Random rand;
	private Graph<Island> graph;
	private final float discountFactor = 0.6f;
	
	
	public IslandGeneration(int size, float radius, float minBridgeLength, float maxBridgeLength)
	{
		long seed = 1337;
		//long seed = new Random().nextLong();
		rand = new Random(seed);		
		
		graph = new Graph<>();
		
		Island main = null;
		Island prev = null;
		
		for(int i = 0; i < size; i++)
		{
			Island island;
			
			if(prev == null)
			{
				island = new Island(new Vector3f(0, 0, 0));
				graph.add(island);
				
				main = island;
				
			}
			else
			{
				
				if(getChance(prev))
				{	
					int quadrant = getNewQuadrant(prev);
					
					island = new Island(prev, getNewVector(quadrant, radius));
					
					graph.addEdge(island.parent, island, 0.0f);
				}
				else
				{
					int quadrant = getNewQuadrant(main);
					
					island = new Island(main, getNewVector(quadrant, radius));
										
					graph.addEdge(island.parent, island, 0.0f);
					
				}
			}
			
			prev = island;
			
		}
	}
	
	public boolean getChance(Island parent)
	{	
		return parent.depth == 0 ? true : (float)Math.pow(discountFactor, parent.depth) >= rand.nextFloat();
	}
	
	public int getNewQuadrant(Island parent)
	{
		int quadrant = rand.nextInt(4) + 1;
		
		if(quadrant > 2)
		{			
			return quadrant - 2 == parent.quadrant ? getNewQuadrant(parent) : quadrant;
		}
		else
		{
			return quadrant + 2 == parent.quadrant ? getNewQuadrant(parent) : quadrant;
		}
	}
	
	public Vector3f getNewVector(int quadrant, float radius)
	{
		final float min = 150.0f, max = 350.00f;
		
		Vector3f vec;
		
		if(quadrant == 1)
		{
			vec = new Vector3f(randomFloat(min, max), randomFloat(min, max), randomFloat(-max, max));
		}
		else if(quadrant == 2)
		{
			vec = new Vector3f(randomFloat(-min, -max), randomFloat(min, max), randomFloat(-max, max));
		}
		else if(quadrant == 3)
		{
			vec = new Vector3f(randomFloat(-min, -max), randomFloat(-min, -max), randomFloat(-max, max));
		}
		else
		{
			vec = new Vector3f(randomFloat(min, max), randomFloat(-min, -max), randomFloat(-max, max));
		}
		
		List<Island> list = graph.getNodes();
			
		return vec;
	}
	
	public List<Vector3f> getVectors()
	{
		List<Vector3f> list = new ArrayList<>();
		
		for(Island is : graph.getNodes())
		{
			list.add(is.location);
		}
		
		return list;		
	}	
	
	public Vector3f getNormalVector(Island first, Island second)
	{
		return (first.location.sub(second.location)).normal();
	}
	
	public float randomFloat(float min, float max)
	{
		//Java's nextFloat returns a number from 0.0 to 1.0.  There is no
		//version that takes a range, so we have to do here.
		return rand.nextFloat() * (max - min) + min;
	}
	
	public Graph<Island> getGraph()
	{
		return graph;
	}
}
